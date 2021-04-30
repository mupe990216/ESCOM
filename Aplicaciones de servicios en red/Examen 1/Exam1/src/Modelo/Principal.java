package Modelo;

import Vista.Ventana;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class Principal extends Thread {

    public static void main(String args[]) {
        boolean Existan_Hilos_Vivos = true;
        int numero = Integer.parseInt(JOptionPane.showInputDialog("Coloca El Numero de Hilos a Utilizar (Minimo 3 - Maximo 10)"));
        if (numero > 10 || numero < 3) {
            JOptionPane.showMessageDialog(null, "Valor Invalido, los parametros son minimo 3 maximo 10");
            System.exit(1);
        }
        int Hilos_Vivos = numero;

        Ventana Principal = new Ventana();

        ActionListener oyenteAccion = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Principal.HilosActivos.setText("" + Hilos_Vivos);
                Principal.NodoAnterior.setText("");
                Principal.NodoSiguiente.setText("");
                Principal.CarpetaAsociada.setText("");
                Principal.jta_Notificaciones.setText("");
                SeccionCritica ObjCritico = new SeccionCritica(numero, Principal);
                Hilos hilos[] = new Hilos[numero];
                /*
                Ventana ventanasHilos[] = new Ventana[numero];
                int rangoVentana = 0;
                 */
                for (int i = 0; i < numero; i++) {
                    /*
                    //Creamos Una ventana Por cada hilo
                    String nombreHilo = "hilo";
                    nombreHilo += "" + i;
                    ventanasHilos[i] = new Ventana(nombreHilo, rangoVentana);
                    ventanasHilos[i].setVisible(true);
                    try {
                        sleep(100);
                    } catch (InterruptedException error) {
                        System.out.println("Error al crear las ventanas de los hilos: " + error.getMessage());
                    }
                    rangoVentana += 30;
                     */
                    //Creamos Cada Hilo
                    hilos[i] = new Hilos(i, Principal.jtf_NombreArchivo.getText(), ObjCritico);
                    hilos[i].start();
                }
                int aux = Hilos_Vivos;
                for (int i = 0; i < numero; i++) {
                    if (!(hilos[i].isAlive())) {
                        aux--;
                        Principal.HilosActivos.setText("" + aux);
                    }
                }
            }
        };
        Principal.btn_Buscar.addActionListener(oyenteAccion);
        Principal.setVisible(true);

    }

}
