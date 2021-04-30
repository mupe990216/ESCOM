package Modelo;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Partida {

    public int _idPartida;//ID de la partida
    public int _modoJuego;// 1) Solitario, 2)Multiplayer
    public int _estadoPartida;// 1) En espera, 2) Iniciada, 3) Terminada 
    public int _numJugadas;//Contador que incrementara en 1 cada vez que un jugadar realice una coincidencia
    public String _nombreJ1;//Nombre Jugador1
    public String _nombreJ2;//Nombre Jugador1
    public String _tiempoJ1;//Nombre Jugador2
    public String _Ganador;//Nombre del Ganador
    public String _turnoJugador;//Jugador que esta jugando actualmente
    public String _JugadaUno;//Primera Carta volteada (Nombre Carta)
    public String _JugadaDos;//Segunda Carta volteada (Nombre Carta)
    public boolean _coincidencia;//Validar si la primera y segunda carta volteada sean la misma
    public boolean _matrizBotones[] = new boolean[41];//Sirve para la interaccion del usuario con el tablero
    public ArrayList<String> _jugadasJ1;//Nombre de las cartas encontradas por el Jugador1
    public ArrayList<String> _jugadasJ2;//Nombre de las cartas encontradas por el Jugador2
    public ArrayList<String> _PatronTablero;//Patron del memorama
    public SocketChannel _canalJ1;//Canal de comunicacion con el Jugador1
    public SocketChannel _canalJ2;//Canal de comunicacion con el Jugador2

    /*
        Contructor Generico para el servidor
    */
    public Partida() {
        _idPartida = -1;
        _estadoPartida = 1;//En espera
        _modoJuego = 1;
        _numJugadas = 0;
        _nombreJ1 = "Prueba";
        _nombreJ2 = "";
        _Ganador = "";
        _turnoJugador = "";
        _tiempoJ1 = "";
        _JugadaUno = "";
        _JugadaDos = "";
        _coincidencia = false;
        _jugadasJ1 = new ArrayList<>();
        _jugadasJ2 = new ArrayList<>();
        _PatronTablero = new ArrayList<>();
        _canalJ1 = null;
        _canalJ2 = null;
    }

    /*
        Constructores del Objeto Partida para:
        1) El modo de Juego Solitario
        2) El modo de Juego Multiplayer
     */
    public Partida(int modoJuego, String nombreJugador) {
        _modoJuego = modoJuego;
        _nombreJ1 = nombreJugador;

        //Modo Solitario
        if (_modoJuego == 1) {
            _idPartida = -1;
            _estadoPartida = 1;//En espera
            _tiempoJ1 = "";
            _JugadaUno = "";
            _JugadaDos = "";
            _coincidencia = false;
            //_controlaTablero[] = new boolean[40];
            _jugadasJ1 = new ArrayList<>();
            _PatronTablero = new ArrayList<>();
            _canalJ1 = null;

            //Funciones Para Llenar Tablero
            //_PatronTablero = TableroAleatorio();
            _PatronTablero = TablerOrdenado();
            iniciaTablero();
        }

        //Modo Multiplayer
        if (_modoJuego == 2) {
            _idPartida = -1;
            _estadoPartida = 1;//En espera
            _numJugadas = 0;
            _nombreJ2 = "";
            _Ganador = "";
            _turnoJugador = "";
            _JugadaUno = "";
            _JugadaDos = "";
            _coincidencia = false;
            _jugadasJ1 = new ArrayList<>();
            _jugadasJ2 = new ArrayList<>();
            _PatronTablero = new ArrayList<>();
            _canalJ1 = null;
            _canalJ2 = null;

            //Funciones Para Llenar Tablero
            //_PatronTablero = TableroAleatorio();
            _PatronTablero = TablerOrdenado();
            iniciaTablero();

        }
    }

    /*
        Metodo iniciaTablero: Se le asignara una posicion en la _matrizBotones a cada boton del tablero,
        con el fin de controlar las acciones que puede o no hacer el jugador en cuestion.
     */
    public void iniciaTablero() {
        for (int i = 1; i < 41; i++) {
            _matrizBotones[i] = true;
            //System.out.println("Boton "+i+" puesto en true");
        }
    }

    /*
        Metodo bloqueaTablero: Se le asignara una posicion en la _matrizBotones a cada boton del tablero,
        con el fin de bloquear el tablero al jugador que no sea su turno.
     */
    public void bloqueaTablero() {
        for (int i = 1; i < 41; i++) {
            _matrizBotones[i] = false;
            //System.out.println("Boton "+i+" puesto en false");
        }
    }

    /*
        Método propio o estatico de la clase, es decir, no lo tendran los objetos de esta clase.
        TableroAleatorio: solo se usa para genera el patronAleatorio que tendra el tablero cuando se crea un objeto de esta clase
     */
    public static ArrayList<String> TableroAleatorio() {
        ArrayList<String> TableroTemporal = new ArrayList<>();
        TableroTemporal.add("null");

        String nombreImagenes[] = {
            "imagen1.jpg", "imagen2.jpg", "imagen3.jpg", "imagen4.jpg", "imagen5.jpg",
            "imagen6.jpg", "imagen7.jpg", "imagen8.jpg", "imagen9.jpg", "imagen10.jpg",
            "imagen11.jpg", "imagen12.jpg", "imagen13.jpg", "imagen14.jpg", "imagen15.jpg",
            "imagen16.jpg", "imagen17.jpg", "imagen18.jpg", "imagen19.jpg", "imagen20.jpg",
            "imagen1.jpg", "imagen2.jpg", "imagen3.jpg", "imagen4.jpg", "imagen5.jpg",
            "imagen6.jpg", "imagen7.jpg", "imagen8.jpg", "imagen9.jpg", "imagen10.jpg",
            "imagen11.jpg", "imagen12.jpg", "imagen13.jpg", "imagen14.jpg", "imagen15.jpg",
            "imagen16.jpg", "imagen17.jpg", "imagen18.jpg", "imagen19.jpg", "imagen20.jpg"
        };

        // Metemos en una lista los números del 1 al 40.
        List<Integer> numbers = new ArrayList<>(40);
        for (int i = 0; i < 40; i++) {
            numbers.add(i);
        }

        // Instanciamos la clase Random
        Random random = new Random();

        // Mientras queden numeros en la lista
        while (numbers.size() > 1) {
            // Elegimos un índice al azar, entre 0 y la cantidad de numeros que quedan por sacar
            int randomIndex = random.nextInt(numbers.size());

            // Damos la carta al jugador (sacamos el número por pantalla)
            //System.out.println("Not Repeated Random Number " + numbers.get(randomIndex));
            TableroTemporal.add(nombreImagenes[numbers.get(randomIndex)]);

            // Y eliminamos la carta del mazo (la borramos de la lista)
            numbers.remove(randomIndex);
        }
        TableroTemporal.add(nombreImagenes[numbers.get(0)]);

        /*
        Probar que se genere aleatoriamente
        int i = 0;
        for (String elemento : TableroTemporal) {
            System.out.println(i + " > " + elemento);
            i++;
        }
         */
        return TableroTemporal;
    }

    /*
        Método propio o estatico de la clase, es decir, no lo tendran los objetos de esta clase.
        TablerOrdenado: solo se usa para genera el patronOrdenado que tendra el tablero cuando se crea un objeto de esta clase
     */
    public static ArrayList<String> TablerOrdenado() {
        ArrayList<String> TableroTemporal = new ArrayList<>();
        TableroTemporal.add("null");

        String nombreImagenes[] = {
            "imagen1.jpg", "imagen2.jpg", "imagen3.jpg", "imagen4.jpg", "imagen5.jpg",
            "imagen6.jpg", "imagen7.jpg", "imagen8.jpg", "imagen9.jpg", "imagen10.jpg",
            "imagen11.jpg", "imagen12.jpg", "imagen13.jpg", "imagen14.jpg", "imagen15.jpg",
            "imagen16.jpg", "imagen17.jpg", "imagen18.jpg", "imagen19.jpg", "imagen20.jpg",
            "imagen1.jpg", "imagen2.jpg", "imagen3.jpg", "imagen4.jpg", "imagen5.jpg",
            "imagen6.jpg", "imagen7.jpg", "imagen8.jpg", "imagen9.jpg", "imagen10.jpg",
            "imagen11.jpg", "imagen12.jpg", "imagen13.jpg", "imagen14.jpg", "imagen15.jpg",
            "imagen16.jpg", "imagen17.jpg", "imagen18.jpg", "imagen19.jpg", "imagen20.jpg"
        };
        TableroTemporal.addAll(Arrays.asList(nombreImagenes));

        /*
        Probar que se genere ordenadamente
        int i = 0;
        for (String elemento : TableroTemporal) {
            System.out.println(i + " > " + elemento);
            i++;
        }
         */
        return TableroTemporal;
    }

}
