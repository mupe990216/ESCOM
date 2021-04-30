package Modelo;

import java.io.*;
import java.net.*;

public class ServidorECO {

    public static void main(String[] args) throws IOException {

        try {
            //Inicializamos Variables
            ServerSocket servidor = null;
            Socket cliente = null;
            BufferedReader entrada = null;
            PrintWriter salida = null;
            String mensaje;

            //Creamos Socket Servidor 
            servidor = new ServerSocket(1234);
            System.out.println("Esperando clientes ... \n");

            //Esperamos Clientes
            while (true) {
                cliente = servidor.accept();
                System.out.println("Conexion establecida desde > " + cliente.getInetAddress() + " : " + cliente.getPort());
                entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream())), true);
                mensaje = entrada.readLine();
                System.out.println("Mensaje Cliente > " + mensaje);
                salida.println(mensaje);
                if (mensaje.equals("exit")) {
                    break;
                }
            }

            //Cerrariamos Flujos, Sockets y Buffers si estos no son nulos
            if (salida != null) {
                salida.close();
            }
            if (entrada != null) {
                entrada.close();
            }
            if (cliente != null) {
                cliente.close();
            }
            servidor.close();
        } catch (IOException e) {
            System.out.println("Error en flujo de datos: " + e.getMessage());
        }

    }

}
