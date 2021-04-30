package M;

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
            _seleccion = -1;
            Mando_ModoJuego = false;
            juego = new Partida();

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
                        //llave_canal.interestOps(SelectionKey.OP_READ);
                        /*
                            1) Pasamos las imagenes al cliente y nos podemos en modo lectura
                            3) Verificamos el tipo de juego del Obj_Partida, si el modo de juego es 1 entonces mandamos el estatus INICIADA, 
                                si el modo de juego es 2 entonces esperamos a que se conecte otro cliente, cuando se conecta el segundo cliente, 
                                se manda el Obj_Partida al primer y segundo cliente y pasamos a modo lectura. (Asignamos al J1 el primero que inicia)
                            5) Enviamos el Obj_Partida al cliente que NO sea su turno y volvemos a modo lectura. (Primera Tirada - Lo ve el J2)
                            7) Enviamos el Obj_Partida al cliente que NO sea su turno y permanecemos en modo escritura. (Segunda Tirada - Lo ve el J2)                        
                            8) Verificamos si el jugador en turno realizo coincidencia: 
                               8.1) Si realizo coincidencia:
                                8.1.1) verificamos si ya se acabo el juego:
                                8.1.2) Si el numJugadas es igual a 20 entonces, se acaba el juego.
                                8.1.3) Si el numJugadas es menor a 20entonces, sigue como jugador en turno y reiniciamos: jugada1,jugada2 y coincidencia.
                               8.2) Sino realizo coincidencia, se cambia al otro jugador en turno y se envia el Obj_Partida al nuevo jugador en turno, y reiniciamos: jugada1,jugada2 y coincidencia.
                               8.3) Pasamos a modo lectura y regresamos al paso 4.2
                         */
                        //SocketChannel canal = (SocketChannel) llave_canal.channel();
                        //mandaModoJuego(canal);
                        //canal.close();
                        //return;       

                        if (Mando_ModoJuego == true) {
                            System.out.println("El Cliente Mando Modo de Juego");
                            if(juego._modoJuego==1){//Solitario
                                SocketChannel canal = (SocketChannel) llave_canal.channel();
                                llave_canal.interestOps(SelectionKey.OP_READ);
                                Mando_ModoJuego = false;
                                continue;
                            }
                            
                            if(juego._modoJuego==2){//Multiplayer
                            }                            
                            //mandaModoJuego(canal);                                                        
                            //canal.close();
                            //return;

                            //Creamos la partida con el modo de juego que el jugador selecciono _modoJuego
                        }
                    }

                    //Si es del tipo leer, leemos el buffer ligado al canal en cuestion
                    if (llave_canal.isReadable()) {
                        /*
                            2) Recibimos el Obj_Partida y pasamos a modo escritura
                            4.1) Si el modo de juego es 1 solo vamos a recibir el tiempo jugado y se termina la conexion.
                            4.2) Si el modo de juego es 2 entonces vamos a recibir el Obj_Partida y pasamos a modo escritura. (Primera tirada - Lo ve el J1)
                            6) Recibimos el Obj_Partida del jugador quien es su turno y volvemos a modo escritura. (Segunda tirada - Lo ve el J1) 
                            
                         */
                        SocketChannel canal = (SocketChannel) llave_canal.channel();
                        recibeModoJuego(canal);
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

    public static void recibeModoJuego(SocketChannel canal) throws IOException, ClassNotFoundException {
        ByteBuffer buffer_Datos = ByteBuffer.allocate(100);
        canal.read(buffer_Datos);
        ByteArrayInputStream msj_Entrada = new ByteArrayInputStream(buffer_Datos.array());
        ObjectInputStream obj_Entrada = new ObjectInputStream(msj_Entrada);
        juego = (Partida) obj_Entrada.readObject();        
        Mando_ModoJuego = true;
        System.out.println("Modo Juego:" + juego._modoJuego);
    }

    public static void mandaModoJuego(SocketChannel canal) throws IOException {
        System.out.println("Mandado ECO");
        ByteArrayOutputStream msj_Salida = new ByteArrayOutputStream();
        ObjectOutputStream obj_Salida = new ObjectOutputStream(msj_Salida);
        obj_Salida.writeObject(_seleccion);
        obj_Salida.flush();
        canal.write(ByteBuffer.wrap(msj_Salida.toByteArray()));
    }

    public static void CreaObjPartida(SocketChannel canal) throws IOException {
        
    }
}
