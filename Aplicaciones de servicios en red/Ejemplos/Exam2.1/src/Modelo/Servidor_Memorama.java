package Modelo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;

/*
    Instituto Politécnico Nacional
    Escuela Superior de Computo
    Aplicaciones Para Comunicaciones De Red
    Examen Segundo Parcial
 */

 /*
    Clase Servidor de Memorama
 */

public class Servidor_Memorama {
    
    public static int _seleccion;
    public static int _modoJuego;
    public static boolean Mando_ModoJuego;
    public static boolean Instru_4;
    public static boolean Instru_41;    
    public static ArrayList<Partida> PartidasEnEspera;
    public static ArrayList<Partida> PartidasIniciadas;
    public static ArrayList<Partida> PartidasFinalizadas;
    public static Partida juego;

    public static void main(String args[]) {
        try {
            //Paso 0) Inicializamos las variables y buffers que se vayan a utilizar            

            //Paso 1) Creamos el servidor
            ServerSocketChannel socket_Servidor = ServerSocketChannel.open();
            socket_Servidor.configureBlocking(false);
            socket_Servidor.socket().bind(new InetSocketAddress(9000));
            System.out.println("--- SERVIDOR INICIADO: Esperando Clientes ---\n");

            //Paso 2) Creamos un selector 
            Selector seleccionador = Selector.open();
            socket_Servidor.register(seleccionador, SelectionKey.OP_ACCEPT);

            //Paso 3) Mantenemos el Servidor Corriendo de manera infinita
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
                    //Si es del tipo aceptar conexion, iniciamos la conexion con el cliente
                    if (llave_canal.isAcceptable()) {
                        SocketChannel socket_cliente = socket_Servidor.accept();
                        System.out.println("Cliente Conectado > " + socket_cliente.socket().getInetAddress() + " :" + socket_cliente.socket().getPort());
                        socket_cliente.configureBlocking(false);
                        socket_cliente.register(seleccionador, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        continue;
                    }

                    //Si es del tipo escribir, escribimos en el buffer ligado al canal en cuestion
                    if (llave_canal.isWritable()) {
                        //Copiar Imagenes al cliente
                        SocketChannel canal = (SocketChannel) llave_canal.channel();
                        //enviaImagenes(canal);
                        llave_canal.interestOps(SelectionKey.OP_READ);
                        continue;
                    }

                    //Si es del tipo leer, leemos el buffer ligado al canal en cuestion
                    if (llave_canal.isReadable()) {

                    }

                }//While - ListaCanales

            }//while(true)

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void enviaImagenes(SocketChannel canal, int numIMG) throws IOException {
        String ruta_img = "./img/imagen"+numIMG+".jpg";        
        int BufferSize = 10240;
        Path ruta_archivo = Paths.get(ruta_img);
        FileChannel canal_Archivo = FileChannel.open(ruta_archivo);
        ByteBuffer buf = ByteBuffer.allocate(BufferSize);
        int numBytesLeidos = 0;
        int counter = 0;
        do {
            numBytesLeidos = canal_Archivo.read(buf);
            if (numBytesLeidos <= 0) {
                break;
            }
            counter += numBytesLeidos;
            buf.flip();
            do {
                numBytesLeidos -= canal.write(buf);
            } while (numBytesLeidos > 0);
            buf.clear();
        } while (true);
        canal_Archivo.close();
        //System.out.println("Bytes Enviados: "+counter);
    }

}
