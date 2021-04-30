
import java.net.*;
import java.io.*;

public class CEcoTCPB {

    public static void main(String[] args) {
        try {
            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
            System.out.printf("Escriba la dirección del servidor: ");
            String host = br1.readLine();
            System.out.printf("\n\nEscriba el puerto:");
            int pto = Integer.parseInt(br1.readLine());
            // Creamos el socket y nos conectamos
            Socket cl = new Socket(host, pto);

            BufferedReader br2 = new BufferedReader(new InputStreamReader(cl.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
            String mensaje = null;
            // Nos conectamos
            while (true) {
                System.out.println("¿Que le quiere decir al servidor?");
                String envia = br1.readLine();
                pw.println(envia);
                pw.flush();
                mensaje = br2.readLine();
                System.out.println("Servidor dice: " + mensaje);
                if (envia.contains("Adios")) {
                    break;
                }
            }
            // Cerramos flujos y socket
            br1.close();
            br2.close();
            cl.close();
        } catch (Exception e) { //Manejo de excepciones
            e.printStackTrace();
        }
    }

}
