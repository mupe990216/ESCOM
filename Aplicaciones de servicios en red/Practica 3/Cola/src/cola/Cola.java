package cola;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author loko_estelar
 */
public class Cola {
    public static void main(String[] args) {
        try{
            ServerSocket s = new ServerSocket(7001);
            
            while(true){
                // Esperamos una conexión 
                Socket cl = s.accept();             
                DataOutputStream dos = null;
                System.out.println("Conexión establecida desde"+cl.getInetAddress()+":"+cl.getPort());
                DataInputStream dis = new DataInputStream(cl.getInputStream());
                int numArchivos = dis.readInt();
                int tamBuffer = dis.readInt();
                
                byte[] b = new byte[tamBuffer];
                
                System.out.println(cl.getInetAddress()+":"+cl.getPort()+"/numero de archivos:"+numArchivos);
                System.out.println(cl.getInetAddress()+":"+cl.getPort()+"/tamano de buffer:"+tamBuffer);
                
                //empieza recepcion de archivos
                for(int i = 0; i < numArchivos; i++){
                    String nombreArchivo = dis.readUTF();
                    System.out.println(cl.getInetAddress()+":"+cl.getPort()+"/recibiendo nombre:"+nombreArchivo);
                    long tam = dis.readLong();
                    System.out.println(cl.getInetAddress()+":"+cl.getPort()+"/recibiendo tam:"+tam);
                    dos = new DataOutputStream(new FileOutputStream(nombreArchivo));
                    long recibidos = 0;
                    int n, porcentaje;
                    while(tam > 0 && (n = dis.read(b, 0, (int)Math.min(b.length, tam))) != -1){   
                        System.out.println("n: " + n);
                        dos.write(b, 0, n);
                        //dos.flush();
                        tam -= n;
                    }//While
                    System.out.println("Archivo " + (i+1)+" recibido.");
                    dos.close();
                }
                
                dis.close();
                cl.close();
            }
        }catch(Exception e){
                e.printStackTrace();
        }//cat
    }
}
