/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videoTransmisor;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.*;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 *
 * @author fnico
 */
public class JavaServer {

    public static InetAddress[] inet;
    public static int[] port;
    public static int i;
    static int count = 0;
    public static BufferedReader[] inFromClient;
    public static DataOutputStream[] outToClient;


    public JavaServer() throws Exception {

        NativeLibrary.addSearchPath("libvlc", "C:\\Program Files\\VideoLAN\\VLC"); // VLC de 64 bits
        //NativeLibrary.addSearchPath("libvlc", "c:\\Program Files (x86)\\VideoLAN\\VLC"); // VLC de 32 bits 

        JavaServer.inet = new InetAddress[30];
        port = new int[30];
        ServerSocket welcomeSocket = new ServerSocket(6782);
        System.out.println(welcomeSocket.isClosed());

        Socket connectionSocket[] = new Socket[30];
        inFromClient = new BufferedReader[30];
        outToClient = new DataOutputStream[30];

        DatagramSocket serv = new DatagramSocket(1234);
        byte[] buf = new byte[62000];
        DatagramPacket dp = new DatagramPacket(buf, buf.length);

        Canvas_Demo canv = new Canvas_Demo(welcomeSocket, serv);
        System.out.println("Iniciando servidor");
        i = 0;

        SThread[] st = new SThread[30];

        while (true) {

            try {
                System.out.println(serv.getPort());
                serv.receive(dp);
                System.out.println(new String(dp.getData()));
                buf = "starts".getBytes();

                inet[i] = dp.getAddress();
                port[i] = dp.getPort();

                DatagramPacket dsend = new DatagramPacket(buf, buf.length, inet[i], port[i]);
                serv.send(dsend);

                Vidthread sendvid = new Vidthread(serv);

                System.out.println("Esperando... \n ");
                connectionSocket[i] = welcomeSocket.accept();
                System.out.println("¡Conectado! " + i);

                inFromClient[i] = new BufferedReader(new InputStreamReader(connectionSocket[i].getInputStream()));
                outToClient[i] = new DataOutputStream(connectionSocket[i].getOutputStream());
                outToClient[i].writeBytes("Conectado > Servidor \n");

                st[i] = new SThread(i);
                st[i].start();

                if (count == 0) {
                    Sentencefromserver sen = new Sentencefromserver();
                    sen.start();
                    count++;
                }

                System.out.println(inet[i]);
                sendvid.start();
                
                i++;

                if (i == 30) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("algo paso xd javaServer constructor");
                e.printStackTrace();
                break;
            }
        }
    }
}