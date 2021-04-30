package Vista;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Tablero extends JFrame {
    
    JPanel panel;
    public JLabel jugador1, versus, jugador2;
    public JButton btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9;    

    /*
        Constructor del Tablero: 
            Recibimos el nombre del hilo para establecer en el titulo que hilo controla que ventana
    */
    public Tablero(String nombreHilo, int idpartida, String simbolo) {
        setSize(450, 530);//Tamaño Ventana (X,Y)
        setTitle("Partida: "+idpartida+"   Jugador: "+nombreHilo+"   Simbolo: "+simbolo);//Ponemos titulo a la ventana
        setResizable(false);//No podemos redimensionar la ventana        
        inicializaComponentes();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public void inicializaComponentes(){
        creaPanel();
        creaEtiquetas();
        creaBotones();        
    }
    
    public void creaPanel(){
        panel = new JPanel();
        panel.setLayout(null); // Desactivamos el diseño por defecto para modificar a gusto
        panel.setBackground(new Color(135, 145, 225));
        this.getContentPane().add(panel); //Agregamos el panel a la ventana
    }
    
    public void creaEtiquetas(){
        jugador1 = new JLabel();
        jugador1.setBounds(0, 0, 150, 50); // (PosX,PosY,TamX,TamY)
        jugador1.setOpaque(true);//Desactivamos el diseño por defecto para modificar a gusto
        jugador1.setForeground(Color.white);
        jugador1.setBackground(new Color(85, 85, 205));
        jugador1.setText("Jugador 1");
        jugador1.setHorizontalAlignment(SwingConstants.CENTER);//Alineacion horizontal del texto en la etiqueta
        jugador1.setFont(new Font("arial", 1, 20));
        panel.add(jugador1);
        
        versus = new JLabel();
        versus.setBounds(150, 0, 150, 50); // (PosX,PosY,TamX,TamY)
        versus.setOpaque(true);//Desactivamos el diseño por defecto para modificar a gusto
        versus.setForeground(Color.white);
        versus.setBackground(new Color(85, 85, 205));
        versus.setText("vs");
        versus.setHorizontalAlignment(SwingConstants.CENTER);//Alineacion horizontal del texto en la etiqueta
        versus.setFont(new Font("arial", 1, 25));
        panel.add(versus);
        
        jugador2 = new JLabel();
        jugador2.setBounds(300, 0, 150, 50); // (PosX,PosY,TamX,TamY)
        jugador2.setOpaque(true);//Desactivamos el diseño por defecto para modificar a gusto
        jugador2.setForeground(Color.white);
        jugador2.setBackground(new Color(85, 85, 205));
        jugador2.setText("Jugador 2");
        jugador2.setHorizontalAlignment(SwingConstants.CENTER);//Alineacion horizontal del texto en la etiqueta
        jugador2.setFont(new Font("arial", 1, 20));
        panel.add(jugador2);
    }
    
    public void creaBotones(){
        btn1 = new JButton("1");
        btn1.setBounds(0,50,150,150);
        btn1.setBackground(new Color(160, 205, 240));
        btn1.setForeground(new Color(160, 205, 240));
        panel.add(btn1);
        
        btn2 = new JButton("2");
        btn2.setBounds(150,50,150,150);
        btn2.setBackground(new Color(160, 205, 240));
        btn2.setForeground(new Color(160, 205, 240));
        panel.add(btn2);
        
        btn3 = new JButton("3");
        btn3.setBounds(300,50,150,150);
        btn3.setBackground(new Color(160, 205, 240));
        btn3.setForeground(new Color(160, 205, 240));
        panel.add(btn3);
        
        btn4 = new JButton("4");
        btn4.setBounds(0,200,150,150);
        btn4.setBackground(new Color(160, 205, 240));
        btn4.setForeground(new Color(160, 205, 240));
        panel.add(btn4);
        
        btn5 = new JButton("5");
        btn5.setBounds(150,200,150,150);
        btn5.setBackground(new Color(160, 205, 240));
        btn5.setForeground(new Color(160, 205, 240));
        panel.add(btn5);
        
        btn6 = new JButton("6");
        btn6.setBounds(300,200,150,150);
        btn6.setBackground(new Color(160, 205, 240));
        btn6.setForeground(new Color(160, 205, 240));
        panel.add(btn6);
        
        btn7 = new JButton("7");
        btn7.setBounds(0,350,150,150);
        btn7.setBackground(new Color(160, 205, 240));
        btn7.setForeground(new Color(160, 205, 240));
        panel.add(btn7);
        
        btn8 = new JButton("8");
        btn8.setBounds(150,350,150,150);
        btn8.setBackground(new Color(160, 205, 240));
        btn8.setForeground(new Color(160, 205, 240));
        panel.add(btn8);
        
        btn9 = new JButton("9");
        btn9.setBounds(300,350,150,150);
        btn9.setBackground(new Color(160, 205, 240));
        btn9.setForeground(new Color(160, 205, 240));
        panel.add(btn9);        
   }
    
}
