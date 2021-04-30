package V;

import javax.swing.JFrame;
import java.awt.*;
import javax.swing.*;

public class Ventana extends JFrame {

    public JPanel panel;
    public JButton btn1;

    public Ventana() {
        setSize(300, 300);//Establecemos el tamaño de la ventana (Ancho, Alto)
        setTitle("Muñoz Primero Elias - Examen Segundo Parcial");//Establecemos titulo de la ventana
        setLocationRelativeTo(null);//Establecemos la ventana en el centro
        setResizable(false);//Establecemos si la ventana puede cambiar de tamaño; False-No puede, True-Si puede        
        iniciarComponentes();        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    
    public void iniciarComponentes(){
        panel = new JPanel();
        panel.setLayout(null); // Desactivamos el diseño por defecto para modificar a gusto
        panel.setBackground(new Color(135, 145, 225));
        this.getContentPane().add(panel); //Agregamos el panel a la ventana
        
        btn1 = new JButton("imagen2.jpg");
        btn1.setBounds(50,50,200,200);        
        panel.add(btn1);
    }
}
