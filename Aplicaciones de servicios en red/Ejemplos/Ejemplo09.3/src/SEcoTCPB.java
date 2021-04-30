
import java.net.*;
import java.io.*;

public class SEcoTCPB {

    public static void main(String[] args) {
        try {
            // Se crea el socket
            ServerSocket s = new ServerSocket(1234);
            System.out.println("Esperando cliente ...");
            // Iniciamos el ciclo infinito 
            for (;;) {
                // Tenemos un bloqueo, en el momento que llegue una conexión continua el programa
                Socket cl = s.accept();
                System.out.println("Conexión establecida desde " + cl.getInetAddress() + ":" + cl.getPort());
                BufferedReader br1 = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
                String mensaje = null;
                String respuesta = "Me llego mensaje, esta es mi respuesta";
                while (true) {
                    mensaje = br1.readLine();
                    System.out.println("Cliente dice: " + mensaje);
                    if (mensaje.contains("Adios")) {
                        respuesta = "Adios cliente";
                        pw.println(respuesta);
                        pw.flush();
                        break;
                    }
                    pw.println(respuesta);
                    pw.flush();
                }
                br1.close();
                pw.close();
                cl.close();
            }//for
        } catch (Exception e) { // Manejo de excepciones
            e.printStackTrace();
        }//catch
    }

}
