/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videoTransmisor;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author fnico
 */
// Hilo para la transmision de video
class Vidthread extends Thread {

    int clientno;
    JFrame jf = new JFrame("Iniciamos Streaming");
    JLabel jleb = new JLabel();

    DatagramSocket soc;

    // Objeto Robot para controlar eventos 
    Robot rb = new Robot();

    byte[] outbuff = new byte[62000];

    BufferedImage mybuf;
    ImageIcon img;
    Rectangle rc;

    int bord = Canvas_Demo.panel.getY() - Canvas_Demo.frame.getY();

    public Vidthread(DatagramSocket ds) throws Exception {
        soc = ds;

        System.out.println(soc.getPort());
        jf.setSize(500, 400);
        jf.setLocation(500, 400);
        jf.setVisible(true);
    }

    public void run() {
        while (true) {
            try {

                int num = JavaServer.i;

                rc = new Rectangle(new Point(Canvas_Demo.frame.getX() + 8, Canvas_Demo.frame.getY() + 27),
                        new Dimension(Canvas_Demo.panel.getWidth(), Canvas_Demo.frame.getHeight() / 2));

                mybuf = rb.createScreenCapture(rc);

                img = new ImageIcon(mybuf);

                jleb.setIcon(img);
                jf.add(jleb);
                jf.repaint();
                jf.revalidate();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                //Enviamos la transmision de video por fotogramas JPG
                ImageIO.write(mybuf, "jpg", baos);

                outbuff = baos.toByteArray();

                for (int j = 0; j < num; j++) {
                    DatagramPacket dp = new DatagramPacket(outbuff, outbuff.length, JavaServer.inet[j],
                            JavaServer.port[j]);
                    soc.send(dp);
                    baos.flush();
                }
                Thread.sleep(15);

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

    }

}
