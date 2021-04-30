package Modelo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;

public class Servidor_NB {   

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
                        
                    }

                    //Si es del tipo leer, leemos el buffer ligado al canal en cuestion
                    if (llave_canal.isReadable()) {
                        SocketChannel canal = (SocketChannel) llave_canal.channel();
                        recibeJuego(canal);
                        canal.close();
                        return;
                        //llave_canal.interestOps(SelectionKey.OP_WRITE);
                        //continue;
                    }

                }//While - ListaCanales

            }//while(true)

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void recibeJuego(SocketChannel canal) throws IOException, ClassNotFoundException {
        //Partida juego = new Partida();
        ByteBuffer buffer_Datos = ByteBuffer.allocate(100000000);
        canal.read(buffer_Datos);
        ByteArrayInputStream msj_Entrada = new ByteArrayInputStream(buffer_Datos.array());
        ObjectInputStream obj_Entrada = new ObjectInputStream(msj_Entrada);
        //Partida seleccion = (Partida) obj_Entrada.readObject();        
        //System.out.println("Modo Juego:" + seleccion);
    }
    
}
