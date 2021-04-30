/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videoReceptor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author fnico
 */
public class JavaClient {

    public static DatagramSocket ds;
    public  JavaClient(String ip) throws Exception{
        try {
            ds = new DatagramSocket();

            byte[] init = new byte[62000];
            init = "givedata".getBytes();

            InetAddress addr = InetAddress.getByName(ip);
            DatagramPacket dp = new DatagramPacket(init, init.length, addr, 1234);
            ds.send(dp);

            DatagramPacket rcv = new DatagramPacket(init, init.length);

            ds.receive(rcv);
            System.out.println(new String(rcv.getData()));
            System.out.println(ds.getPort());
            InetAddress inetAddress = InetAddress.getByName(ip);
            System.out.println(inetAddress);

            Socket clientSocket = new Socket(inetAddress, 6782);
            Vidshow vd = new Vidshow(clientSocket);
            vd.start();
            String modifiedSentence;

            DataOutputStream outToServer
                    = new DataOutputStream(clientSocket.getOutputStream());

            BufferedReader inFromServer
                    = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToServer.writeBytes("Conectado\n");

            CThread write = new CThread(inFromServer, outToServer, 0);
            CThread read = new CThread(inFromServer, outToServer, 1);

            write.join();
            read.join();
            clientSocket.close();
        } catch (Exception e) {
        }
    }
}
