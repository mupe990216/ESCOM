/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videoReceptor;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author fnico
 */
class Vidshow extends Thread {

    JFrame jf = new JFrame();
    public static JPanel jp = new JPanel(new GridLayout(2, 1));
    public static JPanel half = new JPanel(new GridLayout(3, 1));
    JLabel jl = new JLabel();
    public static JTextArea ta, tb;
    byte[] rcvbyte = new byte[62000];
    DatagramPacket dp = new DatagramPacket(rcvbyte, rcvbyte.length);
    BufferedImage bf;
    ImageIcon imc;

    public Vidshow(Socket clien) throws Exception {
        jf.setSize(640, 960);
        jf.setTitle("Cliente");
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt, clien);
            }
        });
        jf.setAlwaysOnTop(true);
        jf.setLayout(new BorderLayout());
        jf.setVisible(true);
        jp.add(jl);
        jp.add(half);
        jf.add(jp);
        JScrollPane jpane = new JScrollPane();
        jpane.setSize(300, 200);
        ta = new JTextArea();
        tb = new JTextArea();
        jpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jpane.add(ta);
        jpane.setViewportView(ta);
        half.add(jpane);
        half.add(tb);
        ta.setText("--- Chat iniciado ---\n\n");
    }
    
    private void formWindowClosed(WindowEvent evt, Socket clien) {
        try {
            clien.close();
            
        } catch (IOException ex) {
            System.out.println("Error cerrando sockets de visualizacion");
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Empezando");
            do {
                System.out.println("Inicio");
                System.out.println(JavaClient.ds.getPort());

                JavaClient.ds.receive(dp);
                System.out.println("Recibido");
                ByteArrayInputStream bais = new ByteArrayInputStream(rcvbyte);

                bf = ImageIO.read(bais);

                if (bf != null) {
                    imc = new ImageIcon(bf);
                    jl.setIcon(imc);
                    Thread.sleep(15);
                }
                jf.revalidate();
                jf.repaint();

            } while (true);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
