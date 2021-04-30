/*
    Instituto Politecnico Nacional
    Muñoz Primero Elias
    Aplicaciones para comunicaciones de red
    Practica Dos - Juego del gato con hilos
 */
package Modelo;

import javax.swing.JOptionPane;

public class Principal {

    /*Metodo Principal Donde Mandamos a Llamar a los hilos para su creacion*/
    public static void main(String args[]) throws InterruptedException {        

        boolean Correr_Programa = true;
        do {
            int numero = Integer.parseInt(JOptionPane.showInputDialog("Número De Partidas A Correr"));
            if (numero < 1) {
                JOptionPane.showMessageDialog(null, "Valor Invalido, Solo Se Aceptan Valores Positivos");
                System.exit(1);
            }
            SeccionCritica objetoCritico[] = new SeccionCritica[numero];
            Hilo jugadores[] = new Hilo[numero * 2];
            for (int i = 0, j = 0; j < (numero); i += 2, j++) {
                objetoCritico[j] = new SeccionCritica(j);
                jugadores[i] = new Hilo(i, objetoCritico[j]);
                jugadores[i + 1] = new Hilo(i + 1, objetoCritico[j]);
                jugadores[i].start();
                jugadores[i + 1].start();
            }
            for (int i = 0, j = 0; i < (numero); i++, j += 2) {
                jugadores[j].join();
                jugadores[j + 1].join();
            }
            System.out.println("\n \t ***** Ya se terminaron de correr todos los hilos ***** ");
            JOptionPane.showMessageDialog(null, "Accion Finalizada", "Hilos Finalizados", JOptionPane.INFORMATION_MESSAGE);
            int respuestas = JOptionPane.showConfirmDialog(null, "¿Quieres Ejecutar El Programa Otra Vez?", "Esta Apunto De Salir", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (respuestas == JOptionPane.YES_OPTION) {//Reiniciamos la busqueda
                Correr_Programa = true;
            } else if (respuestas == JOptionPane.NO_OPTION) {//Salimos del bucle padre
                Correr_Programa = false;
            }
        } while (Correr_Programa);
    }

}
