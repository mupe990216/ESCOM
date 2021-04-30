package Modelo;

public class Principal extends Thread{    
    public static void main(String args[]) {
        SeccionCritica ObjCritico = new SeccionCritica(10);
        Hilos hilos[] = new Hilos[10];   
        
        for (int i = 0; i < 10; i++) {
            hilos[i] = new Hilos(i,"hola.txt",ObjCritico);
            hilos[i].start();          
        }
        /*
        for (int i = 0; i < 10; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException error) {
                System.out.println("Error en la espera del hilo: "+error.getMessage());
            }
        }
        */
    }

}
