package Modelo;

import java.io.*;
import java.net.*;

public class ClienteECO {

    public static void main(String[] args) throws IOException {

        try {
            //Inicializamos Variables
            BufferedReader escribir = new BufferedReader(new InputStreamReader(System.in));
            Socket cliente = null;
            BufferedReader entrada = null;
            PrintWriter salida = null;
            String host, mensaje;
            int puerto;

            //Solicitamos Datos
            System.out.printf("Servidor > ");
            host = escribir.readLine();
            System.out.printf("Puerto > ");
            puerto = Integer.parseInt(escribir.readLine());

            //Creamos Conexion Con El Servidor
            cliente = new Socket(host, puerto);
            entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream())), true);
            System.out.printf("Mensaje > ");
            mensaje = escribir.readLine();

            //Obtenemos Respuesta Del Servidor
            salida.println(mensaje);
            mensaje = entrada.readLine();
            System.out.println("Respuesta Servidor > " + mensaje);

            //Cerramos Flujos, Sockets y Buffers
            salida.close();
            entrada.close();
            escribir.close();
            cliente.close();
        } catch (IOException e) {
            System.out.println("Error en flujo de datos: " + e.getMessage());
        }

    }

}
