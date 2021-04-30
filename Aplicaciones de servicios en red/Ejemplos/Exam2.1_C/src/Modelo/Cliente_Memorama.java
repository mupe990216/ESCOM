package Modelo;

import Vista.Juego;
import javax.swing.JFrame;

/*
    Cliente Del Moemorama
*/

public class Cliente_Memorama {
    public static void main(String[] args) {
        Juego g = new Juego();
        g.setVisible(true);
        g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
