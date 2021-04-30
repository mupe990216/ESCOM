import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author fnico
 */
public class NodoTest {
    
    public static ventanaInicio ventanaPrincipal;
    catalogoPeliculas ventanaCatalogo;
    String username,ip;
    int puerto;
    public static void main(String[] args) {
        
      //  ventanaPrincipal = new ventanaInicio();
      //ventanaPrincipal.setVisible(true);
        try { //siempre dentre de bloques try catch cuando trabajemos con sockets
            
            Socket cl = new Socket("127.0.0.1", 1255);
            
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(cl.getOutputStream(), true); 
            BufferedReader in = new BufferedReader(new InputStreamReader(cl.getInputStream()));
            
            //para que alguien transmita se envia la siguiente cadena:
            //String registrarPelicula = "transmitir:nombre_de_pelicula";
            //regresa un:ok si si se registro la pelicula en el nodo central
            
            //para degar de transmitir:
            //se envia: dejarTransmitir:nombrepelicula
            //retorna: Se elimino la informacion del nodo central
            
            //para obtener la ip de una pelicula:
            //se envia: obtener:nombrepelicula
            //se regresa string con la ip que la trasnmite
            String msj = "";
            
                System.out.println("¿Que le quiere decir al servidor?");
                msj = stdIn.readLine();
                out.println(msj);
                System.out.println("Servidor dice: " + in.readLine());
                
            
            //para obtener las peliculas
     
            //regresa: nombrepelicula1,nombrepelicula2,...,
            //si no hay peliculas regresa solo una ,

            
            
            //primero cerrar flujos
            stdIn.close();
            in.close();
            out.flush();
            out.close();
            cl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
