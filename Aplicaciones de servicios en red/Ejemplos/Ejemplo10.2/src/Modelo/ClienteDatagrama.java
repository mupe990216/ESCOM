package Modelo;

import java.net.*;
import java.io.*;
import java.util.Hashtable;
import java.util.Map;

public class ClienteDatagrama {

    public static void main(String[] args) {
        String host = "127.0.0.1";
        Integer puerto = 1234;
        Integer longitud_paquete = 20;

        String mensaje_fin = "FINDECONECCION";// longitud(mensaje_fin) <= longitud_paquete        
        byte[] byte_mensaje_fin = mensaje_fin.getBytes();

        Map<String, StringBuilder> mensajes = new Hashtable<>();

        try {
            DatagramSocket cl = new DatagramSocket();
            System.out.println("\t\t ---------- Cliente ---------- ");
            System.out.print("Escribe Mensaje > ");            

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String mensaje = br.readLine();
            byte[] byte_mensaje = mensaje.getBytes();

            // Envia paquetes de 20 bytes
            for (int i = 0; i < byte_mensaje.length; i += longitud_paquete) {
                if ((i + longitud_paquete) > byte_mensaje.length) {
                    DatagramPacket p = new DatagramPacket(byte_mensaje, i, byte_mensaje.length - i, InetAddress.getByName(host), puerto);
                    System.out.println("\tPaquete > " + new String(p.getData(), p.getOffset(), p.getLength()));
                    cl.send(p);
                } else {
                    DatagramPacket p = new DatagramPacket(byte_mensaje, i, longitud_paquete, InetAddress.getByName(host), puerto);
                    System.out.println("\tPaquete > " + new String(p.getData(), p.getOffset(), p.getLength()));
                    cl.send(p);
                }
            }

            // Envia bandera de termino de mensaje completo
            DatagramPacket p = new DatagramPacket(byte_mensaje_fin, byte_mensaje_fin.length, InetAddress.getByName(host), puerto);
            cl.send(p);

            System.out.println("\n\t\t ****** Mensaje Enviado *****\n");

            // Ciclo infinito, hasta que reciba el mensaje de echo
            while (true) {
                p = new DatagramPacket(new byte[longitud_paquete], longitud_paquete);
                cl.receive(p);

                if (!mensajes.containsKey(p.getAddress() + ":" + p.getPort())) {
                    mensajes.put(p.getAddress() + ":" + p.getPort(), new StringBuilder());
                }

                if (!(new String(p.getData(), 0, p.getLength())).equals(mensaje_fin)) { // no es fin de coneccion
                    mensajes.get(p.getAddress() + ":" + p.getPort()).append(new String(p.getData(), 0, p.getLength()));
                    System.out.println("\tRecibiendo > " + new String(p.getData(), 0, p.getLength()));
                } else { // es fin de coneccion
                    System.out.println("\tOrigen Datagrama > " + p.getAddress() + ":" + p.getPort());
                    String mensaje_eco = mensajes.get(p.getAddress() + ":" + p.getPort()).toString();
                    System.out.println("\tMensaje Final > " + mensaje_eco);

                    mensajes.remove(p.getAddress() + ":" + p.getPort());
                    break;
                }
            }
            cl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }//catch
    }//main
}
