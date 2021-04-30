package chatmulticast;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author fnico
 */
public class ChatMulticast {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String nombre = JOptionPane.showInputDialog(null, "Ingresa tu nombre de usuario", null);
        while(nombre.equals("")){
            nombre = JOptionPane.showInputDialog(null, "Ingresa un nombre", null);
        }
        Ventana cliente = new Ventana(nombre);
        Thread t = new Thread(cliente);
        t.start();
        cliente.setVisible(true);
        cliente.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
