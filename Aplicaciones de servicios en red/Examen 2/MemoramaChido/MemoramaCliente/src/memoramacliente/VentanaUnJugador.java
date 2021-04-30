
package memoramacliente;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.Iterator;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class VentanaUnJugador extends JFrame implements Runnable{
    private final int numCartasX = 5, numCartasY = 8;
    private final int tamCarta = 100;
    private final int width = numCartasX * tamCarta;
    private final int height = numCartasY * tamCarta + 100;
    private String tipoPartida;
    boolean tableroObtenido = false;
    Carta[][] cartas = new Carta[numCartasY][numCartasX];
    Icon [] iconosCartas = new Icon[20];
    Container contenedor;
    int movimientos;
    JButton btn_iniciar;
    JLabel crono;
    Thread hiloTiempo;
    boolean cronometroActivado;
    JFrame faux;
    //conexion
    String dir="127.0.0.1";
    int pto = 9000;
    ByteBuffer b1 = null, b2 = null;
    InetSocketAddress dst;
    SocketChannel cl;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    Selector sel;  
    
    public VentanaUnJugador(String tipoPartida, JFrame s) {
        try {
            dst = new InetSocketAddress(dir, pto);
            cl = SocketChannel.open();
            br = new BufferedReader(new InputStreamReader(System.in));
            cl.configureBlocking(false);
            sel = Selector.open();
            cl.register(sel, SelectionKey.OP_CONNECT);
            cl.connect(dst);
        } catch (Exception e) {
        }
        this.tipoPartida = tipoPartida;
        this.cronometroActivado = false;
        this.faux  = s;
        this.movimientos = 0;
        iniciar();  
       
    }
    
    private void iniciar() {
        contenedor = getContentPane();
        contenedor.setBackground(new Color(135, 135, 205));
        setBounds(0, 0, width, height);
        setLayout(null);
        setLocationRelativeTo(null);
        setTitle(tipoPartida + "  >  ID Partida: [545@hgg45:458]");
        
        setResizable(false);
        
        //conectarse al servidor
        conectarse();
        //obtener archvios del servidor
        //obtenerImagenes();        
//      conexion();
        //cargar las imagenes a iconos
        cargarImagenes();

        btn_iniciar = new JButton("Iniciar Juego"); 
        btn_iniciar.setBounds(width-480, height-80, 250, 30);        
        btn_iniciar.setBackground(new Color(85, 85, 205));
        btn_iniciar.setForeground(Color.white);
        btn_iniciar.setFont(new Font("arial", 1, 22));
        btn_iniciar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent evt) {
                        //Metodo a llamar cuando se pulse el botón
                        btn_iniciar.setText("Partida En Curso");
                        btn_iniciar.setEnabled(false);
                        System.out.println("Accion > Partida Iniciada");
                        iniciarCronometro(evt);
                    }
                });
        contenedor.add(btn_iniciar);
        
        crono = new JLabel();
        crono.setText( "00:00:000" );
        crono.setBounds(width-170, height-80, 150, 30);
        crono.setOpaque(true);
        crono.setForeground(Color.white);
        crono.setBackground(new Color(85, 85, 205));        
        crono.setHorizontalAlignment(SwingConstants.CENTER);//Alineacion horizontal del texto en la etiqueta
        crono.setFont(new Font("arial", 1, 22));
        contenedor.add(crono);
        
        System.out.println("Accion > Patron Tablero Recibido");
        //esperar el orden del memorama del servidor,
        //se recibe un array bidimensional con cadenas de 1 al 20, y deben de ser 40 elementos
        //crear el array de iconos
        //System.out.println("Generando tablero aleatorio...");
        String posicionesAleatorias [][] = generarPosicionesEnOrdenMemorama(); //posiciones EnOrden o Aleatorias
        System.out.println("Accion > Tablero generado");
        for(int i = 0; i < numCartasY; i++){
            for (int j = 0; j < numCartasX; j++) {
                Carta carta = new Carta(Integer.parseInt(posicionesAleatorias[i][j])); 
                carta.setBounds(tamCarta*j, tamCarta*i, tamCarta, tamCarta);
                
                //ponerles a todos la imagen de fondo
                ImageIcon foto = new ImageIcon("imagenes/fondo.jpg");
                Icon icono = new ImageIcon(foto.getImage().getScaledInstance(carta.getWidth(),carta.getHeight(), Image.SCALE_DEFAULT));
                carta.setIcon(icono);
                carta.setIconoNormal(icono);
                //agregar el icono oculto segun la posicion del array bidemnsional recibido anteriormente
                carta.setIconoOculto(iconosCartas[Integer.parseInt(posicionesAleatorias[i][j]) -1]);
                        
                carta.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent evt) {
                        //Metodo a llamar cuando se pulse el botón
                        cartaPulsada(evt);
                    }
                });
                cartas[i][j] = carta;
                contenedor.add(carta);
            }
        }
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
    }
    
    private void iniciarCronometro(MouseEvent e){
        JButton b = (JButton) e.getComponent();
        if(e.getButton() == MouseEvent.BUTTON1){
            if(!cronometroActivado){
                cronometroActivado = true;
                hiloTiempo = new Thread(this);
                hiloTiempo.start();
            }  
        }
    }
    
    private void pararCronometro(){
        cronometroActivado = false;
    }
     
    private void cartaPulsada(MouseEvent e) {
        Carta c = (Carta) e.getComponent();
        if (e.getButton() == MouseEvent.BUTTON1){
            if(cronometroActivado){
                if(movimientos < 2){
                    if(!c.getPresionado()){
                        System.out.println("Accion > Boton Presionado: "+c.getId());
                        c.setIcon(c.getIconoOculto());
                        c.setPresionado(true);
                        movimientos++;
                    }else{
                        //System.out.println("carta ya presionada");
                    }
                }else{
                    System.out.println("Accion > Girando Cartas");
                }
            }
            
        }
    }
    
    private void cargarImagenes(){
        for (int i = 0; i < 20; i++) {
            ImageIcon foto = new ImageIcon("imagenes/imagen"+(i+1)+".jpg");
            iconosCartas[i] = new ImageIcon(foto.getImage().getScaledInstance(tamCarta, tamCarta, Image.SCALE_DEFAULT));
        }
    }
    
    private String [][] generarPosicionesAleatoriasMemorama(){
       String [][] posiciones = new String [8][5];
        int aux = 0, im = 1;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 5; j++) {   
                posiciones[i][j] = "" + im;
                aux++;
                if(aux == 2){
                    im++;
                    aux = 0;
                }
            }
        }

        //revolver
        String auxS = "";
        int x1, y1, x2, y2;
        for (int i = 0; i < 1000; i++) {
            x1 = (int)Math.floor(Math.random()*8);
            y1 =(int)Math.floor(Math.random()*5);
            x2 = (int)Math.floor(Math.random()*8);
            y2 =(int)Math.floor(Math.random()*5);
            
            auxS = posiciones[x1][y1];

            posiciones[x1][y1] = posiciones[x2][y2];
            posiciones[x2][y2] = auxS;
        }
        return posiciones;
    }
    
    private String[][] generarPosicionesEnOrdenMemorama(){
        String [][] posiciones = new String [8][5];
        int aux = 0, im = 1;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 5; j++) {   
                posiciones[i][j] = "" + im;
                aux++;
                if(aux == 2){
                    im++;
                    aux = 0;
                }
            }
        }
        return posiciones;
    }
   
    private boolean ganaste(){
        boolean finPartida = true;
        for (int i = 0; i < numCartasY; i++) {
            for (int j = 0; j < numCartasX; j++) {
                finPartida = finPartida & cartas[i][j].getParejaEncontrada();
            }
        }
        return finPartida;
    }
    
    private boolean checarTablero(){
        boolean parejaEncontrada = false;
        int id[] = new int [2];
        int x[] = new int [2];
        int y[] = new int [2];
        int aux = 0;
        //buscar cartas presionadas
        for (int i = 0; i < numCartasY; i++) {
            for (int j = 0; j < numCartasX; j++) {
                if(cartas[i][j].getPresionado() && !cartas[i][j].getParejaEncontrada()){
                    id[aux] = cartas[i][j].getId();
                    x[aux] = j;
                    y[aux] = i;
                    aux++;
                }
            }
        }
        //System.out.println("id 1="+id[0]+", id2="+id[1]);
        //checar las cartas
        if(id[0] == id[1]){
            parejaEncontrada = true;
            cartas[y[0]][x[0]].setParejaEncontrada(true);
            cartas[y[1]][x[1]].setParejaEncontrada(true);
        }   
        return parejaEncontrada;
    }
    
    private void girarCartasErroneas(){
        for (int i = 0; i < numCartasY; i++) {
            for (int j = 0; j < numCartasX; j++) {
                if(cartas[i][j].getPresionado() && cartas[i][j].getParejaEncontrada()){
                    
                }else{
                    cartas[i][j].setIcon(cartas[i][j].getIconoNormal());
                    cartas[i][j].setPresionado(false);
                }
            }
        }
    }
    
    
    public void conectarse() {
        try {
            sel.select();
            Iterator<SelectionKey>it = sel.selectedKeys().iterator();
            while(it.hasNext()){
                SelectionKey k = (SelectionKey)it.next();
                it.remove();
                if(k.isConnectable()){
                        SocketChannel ch = (SocketChannel)k.channel();
                        if(ch.isConnectionPending()){
                            System.out.println("--- CLIENTE INICIADO ---");
                            System.out.println("Modo > Solitario");
                            try{
                                ch.finishConnect();
                            }catch(Exception e){
                                e.printStackTrace();
                            }//catch
                            System.out.println("Accion > Conexion Exitosa");
                            System.out.println("Accion > Recibiendo Patron Tablero");
                        }//if
                        ch.register(sel, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
                        continue;
                    }//if
            }
        } catch (Exception e) {
        }
    }
    
    public void enviarDatos(String dato){
        try {
            sel.select();
            Iterator<SelectionKey>it = sel.selectedKeys().iterator();
            while(it.hasNext()){
                SelectionKey k = (SelectionKey)it.next();
                it.remove();
                if(k.isWritable()){
                    SocketChannel ch = (SocketChannel)k.channel();

                    byte[]mm = dato.getBytes();
                    //System.out.println("Enviando datos de "+mm.length+" bytes..");
                    System.out.println("Accion > Enviando objeto");
                    b2 = ByteBuffer.wrap(mm);
                    ch.write(b2);
                    k.interestOps(SelectionKey.OP_READ);
                }
            }
        } catch (Exception e) {
        }
    }
    
    public void recibirDatos(){
        try {
            sel.select();
            Iterator<SelectionKey>it = sel.selectedKeys().iterator();
            while(it.hasNext()){
                SelectionKey k = (SelectionKey)it.next();
                it.remove();
                if(k.isReadable()){
                    SocketChannel ch = (SocketChannel)k.channel();
                    b1 = ByteBuffer.allocate(2000);
                    b1.clear();
                    int n = ch.read(b1);
                    b1.flip();
                   String eco = new String(b1.array(),0,n);
                    //System.out.println("Eco  de "+n+" bytes recibido: "+eco);
                    k.interestOps(SelectionKey.OP_WRITE);
                }
            }
        } catch (Exception e) {
        }
    }
    
    public void desconectarse(){
        try {
            sel.select();
            Iterator<SelectionKey>it = sel.selectedKeys().iterator();
            while(it.hasNext()){
                SelectionKey k = (SelectionKey)it.next();
                it.remove();
                if(k.isWritable()){
                    SocketChannel ch = (SocketChannel)k.channel();
                    System.out.println("Accion > Desconexion Exitosa");
                    byte[]mm = "SALIR".getBytes();
                    b2 = ByteBuffer.wrap(mm);
                    ch.write(b2);
                    k.interestOps(SelectionKey.OP_READ);
                    k.cancel();
                    ch.close();
                }
            }
        } catch (Exception e) {
        }
    }
    
    //run para el modo un jugador
    @Override
    public void run() {
        int minutos = 0 , segundos = 0, milesimas = 0, segundosTemp = 0;
        boolean fallo = false;
        //min es minutos, seg es segundos y mil es milesimas de segundo
        String min="", seg="", mil="";
        try{
            while(cronometroActivado){
                Thread.sleep(5);
                milesimas += 5;
                if(milesimas == 1000){
                    milesimas = 0;
                    segundos += 1;
                    if(segundos == 60){
                        segundos = 0;
                        minutos++;
                    }
                }
 

                if( minutos < 10 ) min = "0" + minutos;
                else min = Integer.toString(minutos);
                if( segundos < 10 ) seg = "0" + segundos;
                else seg = Integer.toString(segundos);
                 
                if( milesimas < 10 ) mil = "00" + milesimas;
                else if( milesimas < 100 ) mil = "0" + milesimas;
                else mil = Integer.toString(milesimas);

                crono.setText( min + ":" + seg + ":" + mil );      
                
                if(movimientos == 2){
                    if(checarTablero()){
                        movimientos = 0;
                        //System.out.println("encontranste pareja");
                        if(ganaste()){
                            //System.out.println("Ganasteee");
                            pararCronometro();
                            //enviar tiempo y nombre al servidor
                            JOptionPane.showMessageDialog(null, "Accion > Partida Finalizada");
                            String nombre = JOptionPane.showInputDialog("Accion > Nombre Jugador");
                            enviarDatos("INFIN:"+nombre+"|"+crono.getText());
                            recibirDatos();
                            //JOptionPane.showMessageDialog(null, "Tu tiempo se ha enviado :D");
                            desconectarse();
                            dispose();
                            faux.setVisible(true);                        }
                    }else{
                        segundosTemp = segundos;
                        movimientos = 3;
                        //System.out.println("cartas no concuerdan: "+minutos+":"+segundos+":"+milesimas);
                    }
                }else if(movimientos == 3){
                    if((segundosTemp+2)%60 == segundos){
                        //girar cartas
                        //System.out.println("Pasaron 2 segundos de penalizacion");
                        movimientos = 0;
                        girarCartasErroneas();
                    }
                }
            }
        }catch(Exception e){}
    }

    
}
