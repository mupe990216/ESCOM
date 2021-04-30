package Modelo;

public class Ejemplo05 {
    
    public static void main(String args[]){
        SeccionCritica objetoCritico[] = new SeccionCritica[1];
        //Preguntamos el numero de partidas - recordar mutiplicar por dos ese numero
        objetoCritico[0] = new SeccionCritica(0);
        Hilo jugador1 = new Hilo(0,objetoCritico[0]);
        jugador1.start();
        Hilo jugador2 = new Hilo(1,objetoCritico[0]);
        jugador2.start();
    }
    
}
