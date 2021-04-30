import java.net.InetAddress;
import java.util.ArrayList;

/**
 *
 * @author fnico
 */
public class Nodos {
    ArrayList <InetAddress> nodos;
    public Nodos(InetAddress firstIP){
        nodos = new ArrayList<InetAddress>();
        nodos.add(firstIP);
    }
    public void anadirNodo(InetAddress ip){
        if(!nodos.contains(ip)){
            nodos.add(ip);
        }
    }
    
}
