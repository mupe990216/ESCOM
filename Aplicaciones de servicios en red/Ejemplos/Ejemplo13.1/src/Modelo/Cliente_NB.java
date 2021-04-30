package Modelo;

import Vista.Tablero;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import javax.swing.JOptionPane;

public class Cliente_NB {
        
    public static Partida juego;

    public static void main(String args[]) {
        juego = new Partida(1,"Prueba");
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
                        
                    }

                    //Si es del tipo escribir, escribimos en el buffer ligado al canal en cuestion
                    if (llave_canal.isWritable()) {                        

                        SocketChannel canal = (SocketChannel) llave_canal.channel();
                        preguntaModoJuego(canal);                        
                        canal.close();
                        return;                        
                    }

                }//While(ListaCanales.hasNext())
            }//while(true)

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void preguntaModoJuego(SocketChannel canal) throws IOException {
        String opciones[] = {"Solitario", "Multijugador", "Cancelar"};
        int seleccion = JOptionPane.showOptionDialog(
                null,
                "Seleccione un modo de juego",
                "Memorama",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );                
                
        if (seleccion == 0) {
            System.out.println("Solitario");
            //nuevaPartida = new Partida(1,"UsrPrueba");
            //juego = new Partida(1,"Prueba");
            Tablero Tablerojuego = new Tablero();
            Tablerojuego.setVisible(true);
        } else if (seleccion == 1) {
            System.out.println("Multijugador");
        } else {
            System.out.println("Adios");              
        }
        
        ByteArrayOutputStream msj_Salida = new ByteArrayOutputStream();
        ObjectOutputStream obj_Salida = new ObjectOutputStream(msj_Salida);
        obj_Salida.writeObject(juego);
        obj_Salida.flush();
        canal.write(ByteBuffer.wrap(msj_Salida.toByteArray()));

    }
}
