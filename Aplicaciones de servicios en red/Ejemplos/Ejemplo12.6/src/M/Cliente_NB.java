package M;

import V.Tablero;
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
    
    public static int _seleccion;
    public static String _nombreCliente;
    public static boolean _matrizBotones[] = new boolean[40];//Sirve para la interaccion del usuario con el tablero
    public static Partida juego;
    public static Tablero Tablerojuego;

    public static void main(String args[]) {
        try {
            //Paso 0) Inicializamos las varialbles y los buffers que se vayan a necesitar
            _seleccion=-1;
            _nombreCliente="";
            
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
                        /* 
                            0) Copiamos las imagenes del servidor y pasamos a modo escritura.
                            1.2) Recibimos el Modo de Juego y el Obj_ Partida, nos mantenemos en modo lectura.
                            1.3) Establecemos Como Mandaremos Las jugadas o el tiempo, 
                                hasta recibir estadus de la partida en INICIADA se pasa al modo escritura (siguiente paso), 
                                de lo contrario, se mantiene en lectura constante.
                            1.5.1) Recibimos constantemente el Obj_Partida si es modo de juego multiplayer, es decir,
                                despues de recibirlo, volvemos a poner la opcion llave_canal.interestOps(SelectionKey.OP_READ)
                                hasta que se termine el turno del otro cliente, despues ponemos en modo escritura.
                                Se termina la conexion si el otro cliente gana.
                         */                        
                    }

                    //Si es del tipo escribir, escribimos en el buffer ligado al canal en cuestion
                    if (llave_canal.isWritable()) {
                        /*                             
                            1.1) Preguntamos Modo de Juego y pasamos (Enviamos 1 o 2) a modo lectura
                            1.4.1) Enviamos Constamente El Obj_Partida si es modo de juego multiplayer, es decir, 
                                despues de mandarlo, volvemos a poner la opcion llave_canal.interestOps(SelectionKey.OP_WRITE)
                                hasta que se termine el turno de este cliente, despues ponemos en modo lectura.
                                -- Se envia el Obj_Partida cada vez que se active un accion listener
                                -- Se cambia el turno del jugador si no encontro una pareja correcta
                                -- Se termina la conexion si este cliente gana.
                            1.4.2) Enviamos El Tiempo final de la partida si es modo de juego solitario y 
                                se termina la conexion en este punto.
                         */

                        SocketChannel canal = (SocketChannel) llave_canal.channel();
                        preguntaModoJuego(canal);
                        //llave_canal.interestOps(SelectionKey.OP_READ);
                        //continue;
                        canal.close();
                        return;
                        //llave_canal.interestOps(SelectionKey.OP_READ);
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
        
        _seleccion=seleccion;       
        
        Partida nuevaPartida = new Partida();
        if (seleccion == 0) {
            System.out.println("Solitario");
            nuevaPartida = new Partida(1,"UsrPrueba");
            //juego = new Partida(1,"Prueba");
            Tablerojuego = new Tablero();
            Tablerojuego.setVisible(true);
        } else if (seleccion == 1) {
            System.out.println("Multijugador");
        } else {
            System.out.println("Adios");              
        }
        
        ByteArrayOutputStream msj_Salida = new ByteArrayOutputStream();
        ObjectOutputStream obj_Salida = new ObjectOutputStream(msj_Salida);
        obj_Salida.writeObject(nuevaPartida);
        obj_Salida.flush();
        canal.write(ByteBuffer.wrap(msj_Salida.toByteArray()));

    }

    public static void recibeModoJuego(SocketChannel canal, SelectionKey llave_canal) throws IOException, ClassNotFoundException {
        System.out.println("Recibiendo ECO");
        ByteBuffer buffer_Datos = ByteBuffer.allocate(10000);
        canal.read(buffer_Datos);
        ByteArrayInputStream msj_Entrada = new ByteArrayInputStream(buffer_Datos.array());
        ObjectInputStream obj_Entrada = new ObjectInputStream(msj_Entrada);
        int seleccion = (int) obj_Entrada.readObject();
        System.out.println("ECO > "+seleccion);
    }
}
