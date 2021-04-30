package V;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Tablero extends JFrame implements ActionListener{

    public JPanel panel;
    public JLabel jugador1, versus, jugador2, cronometro;    
    public JButton btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10;
    public JButton btn11, btn12, btn13, btn14, btn15, btn16, btn17, btn18, btn19, btn20;
    public JButton btn21, btn22, btn23, btn24, btn25, btn26, btn27, btn28, btn29, btn30;
    public JButton btn31, btn32, btn33, btn34, btn35, btn36, btn37, btn38, btn39, btn40;

    public Tablero() {
        setSize(500, 880);//Establecemos el tamaño de la ventana (Ancho, Alto)
        setTitle("Muñoz Primero Elias - Examen Segundo Parcial");//Establecemos titulo de la ventana
        setLocationRelativeTo(null);//Establecemos la ventana en el centro
        setResizable(false);//Establecemos si la ventana puede cambiar de tamaño; False-No puede, True-Si puede        
        iniciarComponentes();        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void iniciarComponentes() {
        creaPanel();
        creaEtiquetas();
        creaBotones();
        addListeners();
    }

    public void creaPanel() {
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
        versus.setBounds(150, 0, 50, 50); // (PosX,PosY,TamX,TamY)
        versus.setOpaque(true);//Desactivamos el diseño por defecto para modificar a gusto
        versus.setForeground(Color.white);
        versus.setBackground(new Color(85, 85, 205));
        versus.setText("vs");
        versus.setHorizontalAlignment(SwingConstants.CENTER);//Alineacion horizontal del texto en la etiqueta
        versus.setFont(new Font("arial", 1, 25));
        panel.add(versus);
        
        jugador2 = new JLabel();
        jugador2.setBounds(200, 0, 150, 50); // (PosX,PosY,TamX,TamY)
        jugador2.setOpaque(true);//Desactivamos el diseño por defecto para modificar a gusto
        jugador2.setForeground(Color.white);
        jugador2.setBackground(new Color(85, 85, 205));
        jugador2.setText("Jugador 2");
        jugador2.setHorizontalAlignment(SwingConstants.CENTER);//Alineacion horizontal del texto en la etiqueta
        jugador2.setFont(new Font("arial", 1, 20));
        panel.add(jugador2);
        
        cronometro = new JLabel();
        cronometro.setBounds(350, 0, 150, 50); // (PosX,PosY,TamX,TamY)
        cronometro.setOpaque(true);//Desactivamos el diseño por defecto para modificar a gusto
        cronometro.setForeground(Color.white);
        cronometro.setBackground(new Color(85, 85, 205));
        cronometro.setText("Cronometro");
        cronometro.setHorizontalAlignment(SwingConstants.CENTER);//Alineacion horizontal del texto en la etiqueta
        cronometro.setFont(new Font("arial", 1, 20));
        panel.add(cronometro);
    }
    
    public void creaBotones(){                
        btn1 = new JButton("1");
        btn1.setBounds(0,50,100,100);
        btn1.setBackground(new Color(160, 205, 240));
        btn1.setForeground(new Color(160, 205, 240));
        panel.add(btn1);
        
        btn2 = new JButton("2");
        btn2.setBounds(100,50,100,100);
        btn2.setBackground(new Color(160, 205, 240));
        btn2.setForeground(new Color(160, 205, 240));
        panel.add(btn2);
        
        btn3 = new JButton("3");
        btn3.setBounds(200,50,100,100);
        btn3.setBackground(new Color(160, 205, 240));
        btn3.setForeground(new Color(160, 205, 240));
        panel.add(btn3);
        
        btn4 = new JButton("4");
        btn4.setBounds(300,50,100,100);
        btn4.setBackground(new Color(160, 205, 240));
        btn4.setForeground(new Color(160, 205, 240));
        panel.add(btn4);
        
        btn5 = new JButton("5");
        btn5.setBounds(400,50,100,100);
        btn5.setBackground(new Color(160, 205, 240));
        btn5.setForeground(new Color(160, 205, 240));
        panel.add(btn5);
        
        btn6 = new JButton("6");
        btn6.setBounds(0,150,100,100);
        btn6.setBackground(new Color(160, 205, 240));
        btn6.setForeground(new Color(160, 205, 240));
        panel.add(btn6);
        
        btn7 = new JButton("7");
        btn7.setBounds(100,150,100,100);
        btn7.setBackground(new Color(160, 205, 240));
        btn7.setForeground(new Color(160, 205, 240));
        panel.add(btn7);
        
        btn8 = new JButton("8");
        btn8.setBounds(200,150,100,100);
        btn8.setBackground(new Color(160, 205, 240));
        btn8.setForeground(new Color(160, 205, 240));
        panel.add(btn8);
        
        btn9 = new JButton("9");
        btn9.setBounds(300,150,100,100);
        btn9.setBackground(new Color(160, 205, 240));
        btn9.setForeground(new Color(160, 205, 240));
        panel.add(btn9);
        
        btn10 = new JButton("10");
        btn10.setBounds(400,150,100,100);
        btn10.setBackground(new Color(160, 205, 240));
        btn10.setForeground(new Color(160, 205, 240));
        panel.add(btn10);
        
        btn11 = new JButton("11");
        btn11.setBounds(0,250,100,100);
        btn11.setBackground(new Color(160, 205, 240));
        btn11.setForeground(new Color(160, 205, 240));
        panel.add(btn11);
        
        btn12 = new JButton("12");
        btn12.setBounds(100,250,100,100);
        btn12.setBackground(new Color(160, 205, 240));
        btn12.setForeground(new Color(160, 205, 240));
        panel.add(btn12);
        
        btn13 = new JButton("13");
        btn13.setBounds(200,250,100,100);
        btn13.setBackground(new Color(160, 205, 240));
        btn13.setForeground(new Color(160, 205, 240));
        panel.add(btn13);
        
        btn14 = new JButton("14");
        btn14.setBounds(300,250,100,100);
        btn14.setBackground(new Color(160, 205, 240));
        btn14.setForeground(new Color(160, 205, 240));
        panel.add(btn14);
        
        btn15 = new JButton("15");
        btn15.setBounds(400,250,100,100);
        btn15.setBackground(new Color(160, 205, 240));
        btn15.setForeground(new Color(160, 205, 240));
        panel.add(btn15);
        
        btn16 = new JButton("16");
        btn16.setBounds(0,350,100,100);
        btn16.setBackground(new Color(160, 205, 240));
        btn16.setForeground(new Color(160, 205, 240));
        panel.add(btn16);
        
        btn17 = new JButton("17");
        btn17.setBounds(100,350,100,100);
        btn17.setBackground(new Color(160, 205, 240));
        btn17.setForeground(new Color(160, 205, 240));
        panel.add(btn17);
        
        btn18 = new JButton("18");
        btn18.setBounds(200,350,100,100);
        btn18.setBackground(new Color(160, 205, 240));
        btn18.setForeground(new Color(160, 205, 240));
        panel.add(btn18);
        
        btn19 = new JButton("19");
        btn19.setBounds(300,350,100,100);
        btn19.setBackground(new Color(160, 205, 240));
        btn19.setForeground(new Color(160, 205, 240));
        panel.add(btn19);
        
        btn20 = new JButton("20");
        btn20.setBounds(400,350,100,100);
        btn20.setBackground(new Color(160, 205, 240));
        btn20.setForeground(new Color(160, 205, 240));
        panel.add(btn20);
        
        btn21 = new JButton("21");
        btn21.setBounds(0,450,100,100);
        btn21.setBackground(new Color(160, 205, 240));
        btn21.setForeground(new Color(160, 205, 240));
        panel.add(btn21);
        
        btn22 = new JButton("22");
        btn22.setBounds(100,450,100,100);
        btn22.setBackground(new Color(160, 205, 240));
        btn22.setForeground(new Color(160, 205, 240));
        panel.add(btn22);
        
        btn23 = new JButton("23");
        btn23.setBounds(200,450,100,100);
        btn23.setBackground(new Color(160, 205, 240));
        btn23.setForeground(new Color(160, 205, 240));
        panel.add(btn23);
        
        btn24 = new JButton("24");
        btn24.setBounds(300,450,100,100);
        btn24.setBackground(new Color(160, 205, 240));
        btn24.setForeground(new Color(160, 205, 240));
        panel.add(btn24);
        
        btn25 = new JButton("25");
        btn25.setBounds(400,450,100,100);
        btn25.setBackground(new Color(160, 205, 240));
        btn25.setForeground(new Color(160, 205, 240));
        panel.add(btn25);
        
        btn26 = new JButton("26");
        btn26.setBounds(0,550,100,100);
        btn26.setBackground(new Color(160, 205, 240));
        btn26.setForeground(new Color(160, 205, 240));
        panel.add(btn26);
        
        btn27 = new JButton("27");
        btn27.setBounds(100,550,100,100);
        btn27.setBackground(new Color(160, 205, 240));
        btn27.setForeground(new Color(160, 205, 240));
        panel.add(btn27);
        
        btn28 = new JButton("28");
        btn28.setBounds(200,550,100,100);
        btn28.setBackground(new Color(160, 205, 240));
        btn28.setForeground(new Color(160, 205, 240));
        panel.add(btn28);
        
        btn29 = new JButton("29");
        btn29.setBounds(300,550,100,100);
        btn29.setBackground(new Color(160, 205, 240));
        btn29.setForeground(new Color(160, 205, 240));
        panel.add(btn29);
        
        btn30 = new JButton("30");
        btn30.setBounds(400,550,100,100);
        btn30.setBackground(new Color(160, 205, 240));
        btn30.setForeground(new Color(160, 205, 240));
        panel.add(btn30);
        
        btn31 = new JButton("31");
        btn31.setBounds(0,650,100,100);
        btn31.setBackground(new Color(160, 205, 240));
        btn31.setForeground(new Color(160, 205, 240));
        panel.add(btn31);
        
        btn32 = new JButton("32");
        btn32.setBounds(100,650,100,100);
        btn32.setBackground(new Color(160, 205, 240));
        btn32.setForeground(new Color(160, 205, 240));
        panel.add(btn32);
        
        btn33 = new JButton("33");
        btn33.setBounds(200,650,100,100);
        btn33.setBackground(new Color(160, 205, 240));
        btn33.setForeground(new Color(160, 205, 240));
        panel.add(btn33);
        
        btn34 = new JButton("34");
        btn34.setBounds(300,650,100,100);
        btn34.setBackground(new Color(160, 205, 240));
        btn34.setForeground(new Color(160, 205, 240));
        panel.add(btn34);
        
        btn35 = new JButton("35");
        btn35.setBounds(400,650,100,100);
        btn35.setBackground(new Color(160, 205, 240));
        btn35.setForeground(new Color(160, 205, 240));
        panel.add(btn35);
        
        btn36 = new JButton("36");
        btn36.setBounds(0,750,100,100);
        btn36.setBackground(new Color(160, 205, 240));
        btn36.setForeground(new Color(160, 205, 240));
        panel.add(btn36);
        
        btn37 = new JButton("37");
        btn37.setBounds(100,750,100,100);
        btn37.setBackground(new Color(160, 205, 240));
        btn37.setForeground(new Color(160, 205, 240));
        panel.add(btn37);
        
        btn38 = new JButton("38");
        btn38.setBounds(200,750,100,100);
        btn38.setBackground(new Color(160, 205, 240));
        btn38.setForeground(new Color(160, 205, 240));
        panel.add(btn38);
        
        btn39 = new JButton("39");
        btn39.setBounds(300,750,100,100);
        btn39.setBackground(new Color(160, 205, 240));
        btn39.setForeground(new Color(160, 205, 240));
        panel.add(btn39);
        
        btn40 = new JButton("40");
        btn40.setBounds(400,750,100,100);
        btn40.setBackground(new Color(160, 205, 240));
        btn40.setForeground(new Color(160, 205, 240));
        panel.add(btn40);
        
        ImageIcon fondo = new ImageIcon("./img/fondo.jpg");
        btn1.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn2.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn3.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn4.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn5.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn6.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn7.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn8.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn9.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn10.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn11.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn12.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn13.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn14.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn15.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn16.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn17.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn18.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn19.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn20.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn21.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn22.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn23.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn24.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn25.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn26.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn27.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn28.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn29.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn30.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn31.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn32.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn33.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn34.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn35.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn36.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn37.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn38.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn39.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        btn40.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
   }
    
    public void addListeners(){
        btn1.addActionListener(this);
        btn2.addActionListener(this);
        btn3.addActionListener(this);
        btn4.addActionListener(this);
        btn5.addActionListener(this);
        btn6.addActionListener(this);
        btn7.addActionListener(this);
        btn8.addActionListener(this);
        btn9.addActionListener(this);
        btn10.addActionListener(this);
        btn11.addActionListener(this);
        btn12.addActionListener(this);
        btn13.addActionListener(this);
        btn14.addActionListener(this);
        btn15.addActionListener(this);
        btn16.addActionListener(this);
        btn17.addActionListener(this);
        btn18.addActionListener(this);
        btn19.addActionListener(this);
        btn20.addActionListener(this);
        btn21.addActionListener(this);
        btn22.addActionListener(this);
        btn23.addActionListener(this);
        btn24.addActionListener(this);
        btn25.addActionListener(this);
        btn26.addActionListener(this);
        btn27.addActionListener(this);
        btn28.addActionListener(this);
        btn29.addActionListener(this);
        btn30.addActionListener(this);
        btn31.addActionListener(this);
        btn32.addActionListener(this);
        btn33.addActionListener(this);
        btn34.addActionListener(this);
        btn35.addActionListener(this);
        btn36.addActionListener(this);
        btn37.addActionListener(this);
        btn38.addActionListener(this);
        btn39.addActionListener(this);
        btn40.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btn1){
            JOptionPane.showMessageDialog(null, "Boton: "+btn1.getText());
            ImageIcon fondo = new ImageIcon("./img/"+btn1.getText());
            btn1.setIcon(new ImageIcon(fondo.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH)));
        }
    }

}
