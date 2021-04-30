package chatmulticast;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import static java.lang.System.exit;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 *
 * @author fnico
 */
public class Ventana extends JFrame implements Runnable{
    private final int height;
    private int width;
    private final int puerto;
    private final String host;
    private final String username;
    private ArrayList <String> usuarios;
    
    JLabel lbl_usuarios;
    JTextField txtf_mensaje;
    JTextArea txta_usuarios;
    JTextPane txtp_mensajes;
    JButton btn_enviar;
    StyledDocument doc;
    JScrollPane scroll_usuarios, scroll_mensajes;
    
    MulticastSocket c;
    InetAddress gpo;
    
    public Ventana(String username){
        width = 500;
        height = 550;
        puerto = 4000;
        host = "230.1.1.1";
        usuarios = new ArrayList<String>();
        this.username = username;
        init();
    }

    private void init() {
        Container contenedor = getContentPane();
        setBounds(0, 0, width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        setTitle("Chat: " + this.username);
        setResizable(false);
        
        
        txtp_mensajes = new JTextPane(); 
        txtp_mensajes.setEditable(false);
        doc = txtp_mensajes.getStyledDocument();
        addStylesToDocument(doc);
        
        
        scroll_mensajes = new JScrollPane(txtp_mensajes);
        scroll_mensajes.setBounds(10, 50, 270, 400);  
        scroll_mensajes.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);  
        scroll_mensajes.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll_mensajes.setPreferredSize(new Dimension(270, 400));
        contenedor.add(scroll_mensajes);  
         
        lbl_usuarios = new JLabel("Usuarios en linea:");
        lbl_usuarios.setBounds(300, 20, 160, 20);
        contenedor.add(lbl_usuarios);  
        
        txta_usuarios = new JTextArea();
        txta_usuarios.setEditable(false);
        scroll_usuarios = new JScrollPane(txta_usuarios);
        scroll_usuarios.setBounds(300, 50, 160, 400);

        scroll_usuarios.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);  
        scroll_usuarios.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  
        contenedor.add(scroll_usuarios);  
        
        
        txtf_mensaje = new JTextField();
        txtf_mensaje.setBounds(10, 460, 360, 30);
        ((AbstractDocument)txtf_mensaje.getDocument()).setDocumentFilter(new Filtro());
        contenedor.add(txtf_mensaje);
        
        btn_enviar = new JButton("Enviar");
        btn_enviar.setBounds(380, 460, 80, 30);
        btn_enviar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent evt) {
                        //Metodo a llamar cuando se pulse el botón
                        enviarMensaje(evt);
                    }
                });
        contenedor.add(btn_enviar);
    }
    
    private void enviarMensaje(MouseEvent evt){
        if(evt.getButton() == MouseEvent.BUTTON1){
            try {
                if(!txtf_mensaje.equals("")){
                    
                    //privados son /priv username [mensaje]
                    try {
                        if(txtf_mensaje.getText().length() >= 7 && txtf_mensaje.getText().substring(0,5).equals("/priv")){//mensaje privado
                            int inicioMsj = txtf_mensaje.getText().substring(6).indexOf(" ");
                            String msj = txtf_mensaje.getText().substring(6);

                            inicioMsj = msj.indexOf(" ");                                   
                            String nombrePrivado = msj.substring(0,inicioMsj);
                            imprimirMensaje("Susurro a "+nombrePrivado+": "+ msj.substring(inicioMsj+1));
                            msj = "<privado><"+username+"><"+nombrePrivado+">"+ msj.substring(inicioMsj+1);

                            enviar(msj);
                        }else if(txtf_mensaje.getText().length() >=6 && txtf_mensaje.getText().substring(0,6).equals("/salir")){//mensaje privado
                            System.out.println("entro al salir");
                            String msj = "<fin>"+username+",";   
                            imprimirMensaje("Adios "+ username);
                            enviar(msj);
                        }
                        else{//mensaje publico
                            imprimirMensaje("Tú: "+ txtf_mensaje.getText());
                            String msj = "<msj><"+username+">"+txtf_mensaje.getText();
                            enviar(msj);

                        }
                        ((AbstractDocument)txtf_mensaje.getDocument()).remove(0, txtf_mensaje.getText().length());
                    } catch (Exception e) {
                        System.out.println("Error evento de boton");
                        e.printStackTrace();
                    }
                    //enviar datos al multicast
                }  
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void imprimirMensaje(String mensaje){   
        String aux = "";
        while(mensaje.length() > 0){
            if(mensaje.length() >= 2 && mensaje.substring(0, 2).equals(":)")){
                mensaje = mensaje.substring(2);
                try {
                    doc.insertString(doc.getLength(), " ", doc.getStyle("icon_risa"));
                    doc.insertString(doc.getLength(), " ", doc.getStyle("regular"));
                } catch (Exception e) {
                    System.out.println("Error imprimiendo emoji de risa");
                }
            }else if(mensaje.length() >= 2 && mensaje.substring(0, 2).equals(":S")){
                mensaje = mensaje.substring(2);
                try {
                    doc.insertString(doc.getLength(), " ", doc.getStyle("icon_enojo"));
                    doc.insertString(doc.getLength(), " ", doc.getStyle("regular"));
                } catch (Exception e) {
                    System.out.println("Error imprimiendo emoji de enojo");
                }
            }else if(mensaje.length() >= 2 && mensaje.substring(0, 2).equals(":o")){
                
                mensaje = mensaje.substring(2);
                try {
                    doc.insertString(doc.getLength(), " ", doc.getStyle("icon_love"));
                    doc.insertString(doc.getLength(), " ", doc.getStyle("regular"));
                } catch (Exception e) {
                    System.out.println("Error imprimiendo emoji de love");
                }
            }else if(mensaje.length() >= 6 && mensaje.substring(0, 6).equals("_loco_")){
                mensaje = mensaje.substring(6);
                try {
                    doc.insertString(doc.getLength(), " ", doc.getStyle("icon_loco"));
                    doc.insertString(doc.getLength(), " ", doc.getStyle("regular"));
                } catch (Exception e) {
                    System.out.println("Error imprimiendo emoji de lco");
                }
            }else if(mensaje.length() >= 8 && mensaje.substring(0, 8).equals("_Homero_")){
                mensaje = mensaje.substring(8);
                try {
                    doc.insertString(doc.getLength(), " ", doc.getStyle("icon_homer"));
                    doc.insertString(doc.getLength(), " ", doc.getStyle("regular"));
                } catch (Exception e) {
                    System.out.println("Error imprimiendo emoji de homerao");
                }
            }else{
                try {
                    doc.insertString(doc.getLength(), mensaje.substring(0, 1), doc.getStyle("regular"));
                } catch (Exception e) {
                    System.out.println("Error imprimiendo emoji de homerao");
                }
                mensaje = mensaje.substring(1);
                
            }
            
        }
        try {
            doc.insertString(doc.getLength(), "\n", doc.getStyle("regular"));
        } catch (Exception e) {
            System.out.println("Error imprimiendo salto de linea");
        }
    }
    
    
    private void enviar(String text) {
        try {
            byte b[] = text.getBytes();
            DatagramPacket p = new DatagramPacket(b, b.length, gpo, puerto);
            c.send(p);
            System.out.println("Enviando mensaje "+text+ " con un TTL= "+c.getTimeToLive());   
        } catch (Exception e) {
            System.out.println("Error al transmitir el mensaje al multicast");
        }
    }
    
    private ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = Ventana.class.getResource(path);   
        if (imgURL != null) {
            ImageIcon ii = new ImageIcon(imgURL, description);
            //System.out.println("Icono tipo 1 cargado");
            return new ImageIcon(ii.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    private ImageIcon createImageIconURL(String path, String description) throws MalformedURLException, IOException {
        URL url = new URL(path);
        Image image = ImageIO.read(url);
        if (image != null) {
            ImageIcon ii = new ImageIcon(image, description);
            //System.out.println("Icono tipo 2 cargado");
            return new ImageIcon(ii.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private void addStylesToDocument(StyledDocument doc) {
        //Initialize some styles.
        Style def = StyleContext.getDefaultStyleContext().
                        getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");
        
        Style s = doc.addStyle("icon_love", null);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
        ImageIcon amorIcon = createImageIcon("images/amor.png", "corazon");
        if (amorIcon != null) {
            StyleConstants.setIcon(s, amorIcon);
        }
        
        s = doc.addStyle("icon_enojo", null);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
        ImageIcon enojoIcon = createImageIcon("images/enojo.png", "enojo");
        if (enojoIcon != null) {
            StyleConstants.setIcon(s, enojoIcon);
        }
        
        s = doc.addStyle("icon_loco", null);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
        ImageIcon locoIcon = createImageIcon("images/loco.gif", "loco");
        if (locoIcon != null) {
            StyleConstants.setIcon(s, locoIcon);
        }
        
        s = doc.addStyle("icon_risa", null);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
        ImageIcon risaIcon = createImageIcon("images/risa.png", "risa");
        if (risaIcon != null) {
            StyleConstants.setIcon(s, risaIcon);
        }
        
        
        s = doc.addStyle("icon_homer", null);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
        try {
            ImageIcon homerIcon = createImageIconURL("http://tusimagenesde.com/wp-content/uploads/2015/01/gifs-animados-5.gif", "homero");
            if (homerIcon != null) {
                StyleConstants.setIcon(s, homerIcon);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar gif de homero");
        } 
    }

    @Override
    public void run() {
        System.out.println("Conectando cliente...");
        try {
            c = new MulticastSocket(puerto);
            gpo = InetAddress.getByName(host);
            this.c.joinGroup(gpo);
        } catch (IOException ex) {
            System.out.println("Error al entrar a la sala de chat");
        }
        
        //entrar a chat
        enviar("<inicio>"+username+",");
        
        try {
            //enviar mensaje de inicio de chat
            doc.insertString(doc.getLength(), "entre al chat\n", doc.getStyle("regular"));
        } catch (BadLocationException ex) {
            System.out.println("Error con entrar al chat");
        }
        
        //
        try {
            boolean endFlag = false;
            while(!endFlag){
                DatagramPacket p = new DatagramPacket(new byte[132],132);
                c.receive(p);
                String msj = new String(p.getData());
                System.out.println("Datagrama recibido: "+msj);
                try {
                    if(msj.substring(0, 8).equals("<inicio>")){
                        String aux = msj.substring(8);
                        int tamNombre = -1;
                        
                        for (int i = 0; i < aux.length(); i++) {
                            System.out.println(aux.charAt(i));
                            System.out.println(i);
                            if(aux.charAt(i) == ','){
                                tamNombre = i;
                                break;
                            }
                        }
                        
                        boolean soyYo = true;
                        
                        for (int i = 0; i < aux.length() && i < username.length(); i++) {
                            if(aux.charAt(i) == username.charAt(i)){
                                soyYo = soyYo & true;
                            }else{
                                soyYo = soyYo & false; 
                            }
                        }
                        
                        if(tamNombre != username.length()){
                            soyYo = soyYo & false;
                        }
                        if(!soyYo){
                            imprimirMensaje(aux+" ha entrado al chat");
                        }
                        usuarios.add(aux.substring(0, tamNombre));
                        cambiarListaDeUsuarios();
                        enviarLista();
                        
                    }else if(msj.substring(0, 5).equals("<msj>")){
                        String nombre = msj.substring(6);
                        int posEtiqueta = nombre.indexOf(">");
                        nombre = nombre.substring(0, posEtiqueta);
                        System.out.println("Nuevo mensaje de: " + nombre);
                        System.out.println("|"+msj.substring(7+nombre.length())+"|");
                        //escribir en mi chat
                        if(!nombre.equals(username)){ //no lo envie yo
                            imprimirMensaje(nombre+ ": "+ msj.substring(7+nombre.length()));
                        }
                    }else if(msj.substring(0, 9).equals("<privado>")){
                        System.out.println("recepcion de mensaje privado");
                        System.out.println("|"+msj.substring(9)+"|");
                        String emisor = msj.substring(10);
                        String receptor = emisor;
                        int finNombre = emisor.indexOf(">");
                        emisor = emisor.substring(0, finNombre);
                        receptor = receptor.substring(finNombre+2);
                        finNombre = receptor.indexOf(">");
                        receptor = receptor.substring(0, finNombre);
                        System.out.println("Emisor: "+ emisor);
                        System.out.println("Receptor: "+ receptor);
                        
                        if(receptor.equals(username)){
                            System.out.println("ese mensaje es mio xd");
                            imprimirMensaje("Susuro de "+emisor+": "+ msj.substring(9+emisor.length()+receptor.length()+4));
                        }else{
                            System.out.println("No estoy espiendo conversaciones privadass");
                        }
                        
                    }else if(msj.substring(0, 5).equals("<fin>")){
                        System.out.println("Alguien salio del chat");
                        System.out.println("|"+msj.substring(5)+"|");
                        String aux = msj.substring(5);
                        int tamNombre = -1;
                        for (int i = 0; i < aux.length(); i++) {
                            System.out.println(aux.charAt(i));
                            System.out.println(i);
                            if(aux.charAt(i) == ','){
                                tamNombre = i;
                                break;
                            }
                        }
                        System.out.println(aux.substring(0, tamNombre));
                        if(username.equals(aux.substring(0, tamNombre))){
                            endFlag = true;
                        }else{
                            
                            imprimirMensaje(aux.substring(0, tamNombre)+" ha salido del chat");
                        }
                        usuarios.remove(aux.substring(0, tamNombre));
                        cambiarListaDeUsuarios();
                        
                    }else if(msj.substring(0, 7).equals("<lista>")){
                        String nombres = msj.substring(7);
                        int tamNombre = -1;
                        tamNombre = nombres.indexOf(",");
                        
                        //System.out.println("nombres iniciales:" + nombres);
                        while(tamNombre != -1){
                            
                            String nombreAux = nombres.substring(0, tamNombre);
                            nombres = nombres.substring(tamNombre+1);
                            tamNombre = nombres.indexOf(",");
                            //System.out.println("nombres:" + nombres);
                            if(!usuarios.contains(nombreAux)){
                                usuarios.add(nombreAux);
                            }
                            //System.out.println("nombre obtenido:"+nombreAux);
                        }
                        cambiarListaDeUsuarios();
                    }
                } catch (Exception e) {
                    System.out.println("Error al detectar el mensaje");
                }
            }
            c.close();
        } catch (Exception e) {
            System.out.println("Error escuchando mensajes");
        }
        exit(0);
    }

    private void cambiarListaDeUsuarios() {
        txta_usuarios.setText("");
        usuarios.forEach(usuario -> {
            txta_usuarios.append(usuario+"\n");
        });
    }
    
    private void enviarLista(){
        String usuariosData = "<lista>";
        for (String usuario : usuarios) {
            usuariosData += usuario;
            usuariosData += ",";
        }
        usuariosData += ".";
        System.out.println(usuariosData);
        try {
            byte b[] = usuariosData.getBytes();
            DatagramPacket p = new DatagramPacket(b, b.length, gpo, puerto);
            c.send(p);
            System.out.println("Enviando lista: "+ usuariosData);   
        } catch (Exception e) {
            System.out.println("Error al transmitir el mensaje al multicast");
        }
    }

}
