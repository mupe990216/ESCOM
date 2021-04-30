/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memoramacliente;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author fnico
 */
public class Juego extends JFrame {
    Container contenedor;
    JButton btn_unJugador, btn_multijugador;
    
    
    public Juego(){
        iniciar();
    }

    private void iniciar() {
        contenedor = getContentPane();
        contenedor.setBackground(new Color(135, 135, 205));
        setBounds(0, 0, 470, 120);
        setLayout(null);
        setLocationRelativeTo(null);
        setTitle("Cliente > Memorama");
        setResizable(false);
        
        btn_unJugador = new JButton("Solitario");
        btn_unJugador.setBounds(20, 20, 200,50);
        btn_unJugador.setBackground(new Color(85, 85, 205));
        btn_unJugador.setForeground(Color.white);
        btn_unJugador.setFont(new Font("arial", 1, 22));
        btn_unJugador.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                botonPulsadoUnJugador(evt);
            }
        });
        contenedor.add(btn_unJugador);
        
        btn_multijugador = new JButton("Multijugador"); 
        btn_multijugador.setBounds(240, 20, 200,50);
        btn_multijugador.setBackground(new Color(85, 85, 205));
        btn_multijugador.setForeground(Color.white);
        btn_multijugador.setFont(new Font("arial", 1, 22));
        btn_multijugador.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                botonPulsadoMultijugador(evt);
            }
        });
        contenedor.add(btn_multijugador);
    }

    private void botonPulsadoUnJugador(MouseEvent e) {
        JButton b = (JButton) e.getComponent();
        if (e.getButton() == MouseEvent.BUTTON1){//dibujar ventaja un jugador
            setVisible(false);
            new VentanaUnJugador("Un Jugador", this);  
        }
    }
    
    private void botonPulsadoMultijugador(MouseEvent e) {
        JButton b = (JButton) e.getComponent();
        if (e.getButton() == MouseEvent.BUTTON1){//dibujar ventaja multijugador
            dispose();
            Thread t = new Thread(new VentanaMultijugador("Multijugador", this));
            t.start();
        }
    }
}
