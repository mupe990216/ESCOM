package ejemplo02;

import java.io.File;

public class Ejemplo02 extends Thread {

    private final long _IdHilo;

    public Ejemplo02(int id) {
        _IdHilo = id;
    }

    public int AumentaContador(int contador) {
        return contador++;
    }

    public static void main(String[] args) {
        int _Contador = 0;
        Ejemplo02 hilos[] = new Ejemplo02[10];
        for (int i = 0; i < 10; i++) {
            hilos[i] = new Ejemplo02(i);
            hilos[i].start();
            _Contador = hilos[i].AumentaContador(_Contador);
            System.out.println("Valor contador "+_Contador);
        }
        System.out.println("Valor Final contador "+_Contador);
    }

    @Override
    public void run() {
        /*File archivito = new File("./hola.txt");
        if (archivito.exists()) {
            System.out.println("Holi Soy el hilo: " + _IdHilo+" y el archivo existe :3");            
        }else{
            System.out.println("Holi Soy el hilo: " + _IdHilo+" y el archivo NO existe :c");
        }*/
    }
}
