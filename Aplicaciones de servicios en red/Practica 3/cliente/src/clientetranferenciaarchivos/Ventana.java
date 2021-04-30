package clientetranferenciaarchivos;

import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import javax.swing.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.text.AbstractDocument;

/**
 *
 * @author fnico
 */
public class Ventana extends JFrame {
    JButton btn_seleccionarArchivos, btn_enviarArchivos;
    JLabel lbl_instruccion, lbl_tamBuffer,lbl_porcentaje ;
    JTextArea txta_archivos;
    JTextField txtf_tamBuffer;
    JCheckBox chkbx_nagle;
    JProgressBar progressBar;
    
    Socket cl;
    DataOutputStream dos;
    DataInputStream dis;
    
    private final int puerto = 7001;
    private final String host = "127.0.0.1";
    
    private File[] files;
    private int porcentaje;
    public Ventana(){
        files = null;
        init();
    }
    
    public void init(){
        Container contenedor = getContentPane();
        setBounds(0, 0, 500, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        setTitle("Servicio de transferencia de archivos");
        setResizable(false);
        
        lbl_instruccion = new JLabel();
        lbl_instruccion.setText("Seleccione los arhivos a enviar");
        lbl_instruccion.setBounds(10,10,200,30);
        contenedor.add(lbl_instruccion);   
        
        btn_seleccionarArchivos = new JButton("Seleccionar");
        btn_seleccionarArchivos.setBounds(10, 50, 150, 30);
        btn_seleccionarArchivos.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent evt) {
                        abrirSelectorDeArchivos(evt);
                    }
                });        
        contenedor.add(btn_seleccionarArchivos);
        
        txta_archivos = new JTextArea();
        txta_archivos.setBounds(10,100,460,400);
        contenedor.add(txta_archivos);
        
        lbl_porcentaje = new JLabel();
        lbl_porcentaje.setText("Numero de bytes neviados");
        lbl_porcentaje.setBounds(10,600,200,30);
        contenedor.add(lbl_porcentaje);
        
        btn_enviarArchivos = new JButton("Enviar");
        btn_enviarArchivos.setBounds(360, 510, 100, 30);
        btn_enviarArchivos.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent evt) {
                        try {
                            //Metodo a llamar cuando se pulse el botón
                            iniciarEnvioArchivos();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });        
        contenedor.add(btn_enviarArchivos);
        
        lbl_tamBuffer = new JLabel();
        lbl_tamBuffer.setText("Tamaño de buffer (En Bytes):");
        lbl_tamBuffer.setBounds(200,20,180,30);
        contenedor.add(lbl_tamBuffer);   
        
        txtf_tamBuffer = new JTextField();
        txtf_tamBuffer.setBounds(380, 20, 80, 30);
        ((AbstractDocument)txtf_tamBuffer.getDocument()).setDocumentFilter(new FiltroNumeros());
        contenedor.add(txtf_tamBuffer);
        
        chkbx_nagle = new JCheckBox("Algoritmo de Nagle");  
        chkbx_nagle.setBounds(300, 50, 150, 50);
        contenedor.add(chkbx_nagle);
    }

    private void iniciarEnvioArchivos(){
        
        if(txtf_tamBuffer.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Ingrese el tamano de buffer.");
            return;
        }
        if(Integer.parseInt(txtf_tamBuffer.getText()) < 1){
            JOptionPane.showMessageDialog(this, "El tamaño del buffer debe ser de 1 byte o mayor");
            return;
        }
        if(files == null){
            JOptionPane.showMessageDialog(this, "No se han seleccionado archivos.");
            return;
        }
        
        //inicializar socket
        try {
            cl = new Socket(host, puerto);
            dos = new DataOutputStream(cl.getOutputStream());  
        } catch (Exception e) {
            System.out.println("Error al inicializar el socket");
            
        }
            
        //algoritmo de nagle
        try {
            if(chkbx_nagle.isSelected()){ //descomentarlas cuando se inicialice el socket
                cl.setTcpNoDelay(false);
                System.out.println("Algoritmo de nagle activado");
            }else{
                cl.setTcpNoDelay(true);
                System.out.println("Algoritmo de nagle desactivado");
            }
        } catch (Exception e) {
            System.out.println("Error al cambiar el algoritmo de nagle.");
        }
            
        enviarNumArchivos();
        enviarTamBuffer();
        enviarArchivos();  
        
        try {
            dos.close();
            cl.close();
        } catch (Exception e) {
            System.out.println("Error al cerrar el socket y output stream.");
        }
    }
    
    private void abrirSelectorDeArchivos(MouseEvent evt){
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.showOpenDialog(this);
        files = fc.getSelectedFiles();
        txta_archivos.append("Archivos seleccionados (" + files.length +")\n");
        for (int i=0; i<files.length; i++) {
            txta_archivos.append("Nombre del archivo: " + files[i].getName() + " tamaño del archivo: " + files[i].length() + " bytes\n");
            System.out.println("Nombre del archivo: " + files[i].getName() + " tamaño del archivo: " + files[i].length() + " bytes");
        }                      
    }

    private void enviarNumArchivos() {
        try {
            System.out.println("Num de archivos:" +files.length);
            dos.writeInt(files.length);
            dos.flush();
        } catch (Exception e) {
            System.out.println("Error al enviar el numero de archivos");
            e.printStackTrace();
        }
    }

    private void enviarTamBuffer() {
        try {
            System.out.println("Tam de buffer:" + txtf_tamBuffer.getText());
            dos.writeInt(Integer.parseInt(txtf_tamBuffer.getText()));
            dos.flush();
        } catch (Exception e) {
            System.out.println("Error al enviar el tamano de buffer");
            e.printStackTrace();
        }
    }

    private void enviarArchivos(){
        try {
            for (File file : files) {
                dis = new DataInputStream(new FileInputStream(file)); 
                dos.writeUTF(file.getName());
                dos.flush();               
                dos.writeLong(file.length());
                dos.flush();        
                byte[] b = new byte[Integer.parseInt(txtf_tamBuffer.getText())];
                long enviados = 0;
                int porcentaje, n;
                //progressBar.setMaximum((int) file.length());
                while (enviados < file.length()){
                    System.out.println("enviado " +enviados +"de " + file.length());
                    lbl_porcentaje.setText("enviado " +enviados +"de " + file.length());
                    if(enviados + Integer.parseInt(txtf_tamBuffer.getText()) >= file.length()){
                        System.out.println("ultimo bloque enviado");
                        n = dis.read(b, 0, (int) (file.length() - enviados));   
                        System.out.println("n:" + n);
                        dos.write(b, 0, n);
                        //dos.flush();
                        enviados = enviados+n;
                        lbl_porcentaje.setText("enviado " +enviados +"de " + file.length());
                        
                    }else{
                        System.out.println("nloque normal enviado");
                        n = dis.read(b);  
                        System.out.println("n:" + n);
                        dos.write(b, 0, n);
                        //dos.flush();
                        enviados = enviados+n;
                        lbl_porcentaje.setText("enviado " +enviados +"de " + file.length());
                        
                    }
                }
                System.out.print("\n\nArchivo enviado");
            }
            dis.close(); //checar si se hace con cada archivo o hasta el final
        } catch (Exception e) {
            System.out.println("Error al enviar los archivos");
            e.printStackTrace();
        }
        
    }
    
    public void Barra_Progreso(int n){
        progressBar.setValue(n);
    }
}
