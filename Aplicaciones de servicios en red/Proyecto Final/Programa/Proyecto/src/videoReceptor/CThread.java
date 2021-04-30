/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videoReceptor;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;

/**
 *
 * @author fnico
 */
class CThread extends Thread {

    BufferedReader inFromServer;
    Button sender = new Button("Mandar mensaje ->");
    DataOutputStream outToServer;
    public static String sentence;
    int RW_Flag;

    public CThread(BufferedReader in, DataOutputStream out, int rwFlag) {
        inFromServer = in;
        outToServer = out;
        RW_Flag = rwFlag;
        if (rwFlag == 0) {
            Vidshow.half.add(sender);
            sender.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    sentence = Vidshow.tb.getText();
                    Vidshow.ta.append("Mi mensaje: " + sentence + "\n\n");
                    try {
                        outToServer.writeBytes(sentence + '\n');
                    } catch (Exception E) {
                        System.out.println("Error: " + E.getMessage());
                    }
                    Vidshow.tb.setText(null);
                }
            });
        }
        start();
    }

    public void run() {
        String mysent;
        try {
            while (true) {
                if (RW_Flag == 0) {
                    if (sentence.length() > 0) {
                        Vidshow.ta.append(sentence + "\n");
                        Vidshow.ta.setCaretPosition(Vidshow.ta.getDocument().getLength());
                        Vidshow.half.revalidate();
                        Vidshow.half.repaint();
                        Vidshow.jp.revalidate();
                        Vidshow.jp.repaint();
                        outToServer.writeBytes(sentence + '\n');
                        sentence = null;
                        Vidshow.tb.setText(null);
                    }
                } else if (RW_Flag == 1) {
                    mysent = inFromServer.readLine();

                    Vidshow.ta.append(mysent + "\n");
                    Vidshow.ta.setCaretPosition(Vidshow.ta.getDocument().getLength());
                    Vidshow.half.revalidate();
                    Vidshow.half.repaint();
                    Vidshow.jp.revalidate();
                    Vidshow.jp.repaint();

                    System.out.println("Desde : " + sentence);
                    sentence = null;

                }
            }
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
        }
    }
}
