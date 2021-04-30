/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videoTransmisor;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

/**
 *
 * @author fnico
 */
// Objeto Canvas para mandar la transmision
class Canvas_Demo {

    // Create a media player factory
    private MediaPlayerFactory mediaPlayerFactory;

    // Create a new media player instance for the run-time platform
    private EmbeddedMediaPlayer mediaPlayer;

    public static JPanel panel;
    public static JPanel myjp;
    private Canvas canvas;
    public static JFrame frame;
    public static JTextArea ta;
    public static JTextArea txinp;
    public static int xpos = 0, ypos = 0;
    String url = "D:\\DownLoads\\Video\\freerun.MP4"; //URL de prueba
    // Constructor
    public Canvas_Demo(ServerSocket welcomeSocket, DatagramSocket serv) {

        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel mypanel = new JPanel();
        mypanel.setLayout(new GridLayout(2, 1));

        canvas = new Canvas();
        canvas.setBackground(Color.BLACK);
        panel.add(canvas, BorderLayout.CENTER);

        mediaPlayerFactory = new MediaPlayerFactory();
        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
        CanvasVideoSurface videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
        mediaPlayer.setVideoSurface(videoSurface);

        frame = new JFrame("Servidor de Video");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt, welcomeSocket, serv);
            }
        });
        frame.setLocation(200, 0);
        frame.setSize(640, 960);
        frame.setAlwaysOnTop(true);

        mypanel.add(panel);
        frame.add(mypanel);
        frame.setVisible(true);
        xpos = frame.getX();
        ypos = frame.getY();

        // Area del video
        myjp = new JPanel(new GridLayout(4, 1));
        Button bn = new Button("Elige un Archivo");
        myjp.add(bn);
        Button sender = new Button("Mandar mensaje ->");
        JScrollPane jpane = new JScrollPane();
        jpane.setSize(300, 200);
        ta = new JTextArea();
        txinp = new JTextArea();
        jpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jpane.add(ta);
        jpane.setViewportView(ta);
        myjp.add(jpane);
        myjp.add(txinp);
        myjp.add(sender);
        ta.setText("Servidor Iniciado \n");
        ta.setCaretPosition(ta.getDocument().getLength());
        mypanel.add(myjp);
        mypanel.revalidate();
        mypanel.repaint();

        bn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jf = new JFileChooser();
                jf.showOpenDialog(frame);
                File f;
                f = jf.getSelectedFile();
                url = f.getPath();
                System.out.println(url);
                ta.setText("--- Chat iniciado --- \n\n");
                ta.append("Streaming del archivo: \n" + url + "\n\n");
                mediaPlayer.playMedia(url);
            }
        });
        sender.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                Sentencefromserver.sendingSentence = txinp.getText();
                txinp.setText(null);
                Canvas_Demo.ta.append("Mi mensaje: " + Sentencefromserver.sendingSentence + "\n");
                Canvas_Demo.myjp.revalidate();
                Canvas_Demo.myjp.repaint();
            }
        });

    }
    
    private void formWindowClosed(java.awt.event.WindowEvent evt, ServerSocket welcomeSocket, DatagramSocket serv) {                                  
        try {
            welcomeSocket.close();
            serv.close();
        } catch (IOException ex) {
            System.out.println("Error cerrando sockets de transmision");
            ex.printStackTrace();
        }
    } 
}
