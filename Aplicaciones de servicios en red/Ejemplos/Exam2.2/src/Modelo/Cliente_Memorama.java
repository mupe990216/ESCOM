package Modelo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.Iterator;

/*
    Cliente para el memorama
 */
public class Cliente_Memorama {

    public static void main(String args[]) {
        try {
            //Paso 0) Inicializamos las varialbles y los buffers que se vayan a necesitar            

            //Paso 1) Creamos el canal para el socket cliente
            InetSocketAddress Dir_Servidor = new InetSocketAddress("127.0.0.1", 9000);
            SocketChannel socket_Cliente = SocketChannel.open();
            socket_Cliente.configureBlocking(false);

            //Paso 2) Creamos un selector y conectamos el socket cliente
            Selector seleccionador = Selector.open();
            socket_Cliente.register(seleccionador, SelectionKey.OP_CONNECT);
            socket_Cliente.connect(Dir_Servidor);

            //Paso 3) Mantenemos corriendo el cliente de manera infinita
            while (true) {
                //Paso 4) Activamos el seleccionador en escucha infinita y guardamos los canales que se conecten al seleccionador en una lista
                seleccionador.select();
                Iterator<SelectionKey> ListaCanales = seleccionador.selectedKeys().iterator();

                //Paso 5) Cada vez que algun canal se agregue al selector lo recorremos con un iterador
                while (ListaCanales.hasNext()) {
                    //Paso 6) Obtenemos la llave del canal que vamos a estar usando y lo removemos de la lista de canales en espera
                    SelectionKey llave_canal = (SelectionKey) ListaCanales.next();
                    ListaCanales.remove();

                    //Paso 7) Vemos que tipo de accion vamos a realizar con la llave del canal seleccionado
                    //Si es del tipo conectando, se establece la conexion con el servidor
                    if (llave_canal.isConnectable()) {
                        SocketChannel canal_Servidor = (SocketChannel) llave_canal.channel();
                        if (canal_Servidor.isConnectionPending()) {
                            System.out.println("Estableciendo Conexion Con el Servidor ...\n");
                            try {
                                canal_Servidor.finishConnect();
                            } catch (Exception e) {
                                System.out.println("Error el conectar con el servidor: " + e.getMessage());
                            }
                            System.out.println("CONEXION ESTABLECIDA");
                        }
                        canal_Servidor.register(seleccionador, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        continue;
                    }

                    //Si es del tipo leer, leemos el buffer ligado al canal en cuestion
                    if (llave_canal.isReadable()) {
                        SocketChannel canal = (SocketChannel) llave_canal.channel();
                        //recibeImagenes(canal);
                        llave_canal.interestOps(SelectionKey.OP_WRITE);
                        continue;
                    }

                    //Si es del tipo escribir, escribimos en el buffer ligado al canal en cuestion
                    if (llave_canal.isWritable()) {

                    }

                }//While(ListaCanales.hasNext())
            }//while(true)

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void recibeImagenes(SocketChannel canal, int numIMG) throws IOException {
        String salidaArchivo = "./img/imagen"+numIMG+".jpg";
        int bufferSize = 10240;
        Path ruta = Paths.get(salidaArchivo);
        FileChannel canal_Archivo = FileChannel.open(
                ruta,
                EnumSet.of(StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE)
        );

        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        int res = 0;
        int counter = 0;
        do {
            buffer.clear();
            res = canal.read(buffer);
            System.out.println(res);
            buffer.flip();
            if (res > 0) {
                canal_Archivo.write(buffer);
                counter += res;
            }
        } while (res >= 0);
        //canal.close();
        canal_Archivo.close();        
        //System.out.println("Bytes Recibidos: "+counter);
    }
}
