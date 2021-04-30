package Modelo;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import javax.swing.JOptionPane;

public class SeccionCritica {

    public final int id_Partida;
    int contadorAcciones;
    boolean matrizBotones[] = new boolean[10];
    boolean eventoRealizado;
    boolean partidaIniciada;
    Semaphore semaforo;
    ArrayList<String> jugadas_jugador1;
    ArrayList<String> jugadas_jugador2;
    String nom_jugador1;
    String nom_jugador2;
    String turno;
    String ganador;

    /*
        Constructor de la clase SeccionCritica
     */
    public SeccionCritica(int numPartida) {
        semaforo = new Semaphore(1);
        id_Partida = numPartida;
        contadorAcciones = 0;
        eventoRealizado = false;
        partidaIniciada = false;
        nom_jugador1 = "";
        nom_jugador2 = "";
        ganador = "";
        turno = "";
        jugadas_jugador1 = new ArrayList<>();
        jugadas_jugador2 = new ArrayList<>();
        iniciaTablero();
    }

    /*
        #Funcion iniciaTablero: inicializa el arreglo 'matrizBotones[]' en true, es decir, este arreglo
            se encargara de habilitar los botones dentro de nuestro tablero doble (se actualiza el tablero de cada hilo)
        #Entrada: void - no recibe nada
        #Salida: void - no regresa nada
     */
    public void iniciaTablero() {
        for (int i = 1; i < 10; i++) {
            matrizBotones[i] = true;
            //System.out.println("Boton "+i+" puesto en true");
        }
    }

    public void bloqueaTablero() {
        for (int i = 1; i < 10; i++) {
            matrizBotones[i] = false;
            //System.out.println("Boton "+i+" puesto en true");
        }
    }

    public void evento_realizado(int opc) {
        try {
            //Bloqueamos seccion critica - Ponemos al otro hilo en espera
            semaforo.acquire();
            if (opc == 1) {
                eventoRealizado = true;
            }
            if (opc == 0) {
                eventoRealizado = false;
            }
            //Desbloqueamos seccion critica - Despertamos al otro hilo
            semaforo.release();
        } catch (InterruptedException error) {
            System.out.println("Error al realizar un evento: " + error.getMessage());
        }
    }

    public void aumentaContador() {
        try {
            //Bloqueamos seccion critica - Ponemos al otro hilo en espera
            semaforo.acquire();
            contadorAcciones++;
            //System.out.println("Contador: " + contadorAcciones);
            //Desbloqueamos seccion critica - Despertamos al otro hilo
            semaforo.release();
        } catch (InterruptedException error) {
            System.out.println("Error al aumentar el contador: " + error.getMessage());
        }
    }

    /*
        #Funcion asignaJugadores: indica quien es el jugador1 y quien el jugador2            
        #Entrada: nombreHilo - sera el nombre del jugador a guardar
        #Salida: void - no regresa nada
     */
    public void asignaJugadores(String nombreHilo) {
        try {
            //Bloqueamos seccion critica - Ponemos al otro hilo en espera
            semaforo.acquire();
            if (partidaIniciada == false) {
                nom_jugador1 = nombreHilo;
                turno = nombreHilo;
                JOptionPane.showMessageDialog(null, "Partida: "+id_Partida+" - El primero en jugar es: "+nombreHilo);
                partidaIniciada = true;
                System.out.println("Partida > " + id_Partida + "   El jugador 1 es: " + nombreHilo);
            } else {
                nom_jugador2 = nombreHilo;
                System.out.println("Partida > " + id_Partida + "   El jugador 2 es: " + nombreHilo);
            }
            //Desbloqueamos seccion critica - Despertamos al otro hilo
            semaforo.release();
        } catch (InterruptedException error) {
            System.out.println("Error al asignar jugadores: " + error.getMessage());
        }
    }

    /*
        #Funcion agregarJugadas: agrega las jugadas de cada jugador, para asi actualizar su respectivo tablero
        #Entrada: nombreHilo - sera el nombre del jugador a guardar sus jugadas correspondientes
        #Salida: void - no regresa nada
     */
    public void agregarJugadas(String nombreHilo, String posicion) {
        try {
            //Bloqueamos seccion critica - Ponemos al otro hilo en espera
            semaforo.acquire();

            if (nom_jugador1.equals(nombreHilo)) {
                jugadas_jugador1.add(posicion);
                //System.out.println(nombreHilo + "  " + posicion);
            }

            if (nom_jugador2.equals(nombreHilo)) {
                jugadas_jugador2.add(posicion);
                //System.out.println(nombreHilo + "  " + posicion);
            }

            //Desbloqueamos seccion critica - Despertamos al otro hilo
            semaforo.release();
        } catch (InterruptedException error) {
            System.out.println("Error al asignar jugadas: " + error.getMessage());
        }
    }

    /*
        #Funcion get_jugadas1: Devuelve las jugadas del jugador 1
        #Entrada: void - no regresa nada
        #Salida: Regresa el ArrayLisy donde se guardan las jugadas del jugador 1
     */
    public ArrayList get_jugadas1() {
        ArrayList<String> jugadas1 = new ArrayList<String>();
        try {
            //Bloqueamos seccion critica - Ponemos al otro hilo en espera
            semaforo.acquire();

            for (String elemento : jugadas_jugador1) {
                jugadas1.add(elemento);
            }

            //Desbloqueamos seccion critica - Despertamos al otro hilo
            semaforo.release();
        } catch (InterruptedException error) {
            System.out.println("Error al regresar jugadas1: " + error.getMessage());
        }
        return jugadas1;
    }

    /*
        #Funcion get_jugadas2: Devuelve las jugadas del jugador 2
        #Entrada: void - no regresa nada
        #Salida: Regresa el ArrayLisy donde se guardan las jugadas del jugador 2
     */
    public ArrayList get_jugadas2() {
        ArrayList<String> jugadas2 = new ArrayList<String>();
        try {
            //Bloqueamos seccion critica - Ponemos al otro hilo en espera
            semaforo.acquire();

            for (String elemento : jugadas_jugador2) {
                jugadas2.add(elemento);
            }

            //Desbloqueamos seccion critica - Despertamos al otro hilo
            semaforo.release();
        } catch (InterruptedException error) {
            System.out.println("Error al regresar jugadas1: " + error.getMessage());
        }
        return jugadas2;
    }
    
    public void asignaTurno(String nombreHilo) {
        try {
            //Bloqueamos seccion critica - Ponemos al otro hilo en espera
            semaforo.acquire();

            if(nom_jugador1.equals(nombreHilo)){
                turno = nom_jugador2;
            }
            
            if(nom_jugador2.equals(nombreHilo)){
                turno = nom_jugador1;
            }

            //Desbloqueamos seccion critica - Despertamos al otro hilo
            semaforo.release();
        } catch (InterruptedException error) {
            System.out.println("Error al asignar el ganador: " + error.getMessage());
        }
    }
    
    public void asignaGanador(String nombreHilo) {
        try {
            //Bloqueamos seccion critica - Ponemos al otro hilo en espera
            semaforo.acquire();

            ganador = nombreHilo;

            //Desbloqueamos seccion critica - Despertamos al otro hilo
            semaforo.release();
        } catch (InterruptedException error) {
            System.out.println("Error al asignar el ganador: " + error.getMessage());
        }
    }   
}
