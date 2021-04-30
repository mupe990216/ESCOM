/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videoTransmisor;

import java.io.BufferedReader;
import java.io.DataOutputStream;

/**
 *
 * @author fnico
 */
// Hilo para reportar los mensajes que le mandan al servidor
class SThread extends Thread {

    public static String clientSentence;
    int srcid;
    BufferedReader inFromClient = JavaServer.inFromClient[srcid];
    DataOutputStream outToClient[] = JavaServer.outToClient;

    public SThread(int a) {
        srcid = a;
    }

    public void run() {
        while (true) {
            try {
                clientSentence = inFromClient.readLine();
                System.out.println("Cliente " + srcid + " > " + clientSentence);
                Canvas_Demo.ta.append("Cliente " + srcid + " > " + clientSentence + "\n");
                for (int i = 0; i < JavaServer.i; i++) {
                    if (i != srcid) {
                        outToClient[i].writeBytes("Cliente " + srcid + " > " + clientSentence + '\n');	//'\n' is necessary
                    }
                }
                Canvas_Demo.myjp.revalidate();
                Canvas_Demo.myjp.repaint();

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
