package clientetranferenciaarchivos;

import java.io.IOException;
import javax.swing.JFrame;

/**
 *
 * @author loko_estelar
 */
public class ClienteTranferenciaArchivos {

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Ventana v = new Ventana();
        v.setVisible(true);
        v.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
