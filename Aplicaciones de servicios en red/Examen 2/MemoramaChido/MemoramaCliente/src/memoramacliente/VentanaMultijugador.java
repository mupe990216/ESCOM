package memoramacliente;

import java.awt.Container;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class VentanaMultijugador extends JFrame implements Runnable{
    private final int numCartasX = 5, numCartasY = 8;
    private final int tamCarta = 100;
    private final int width = numCartasX * tamCarta;
    //private final int width = numCartasX * tamCarta + 200;
    private final int height = numCartasY * tamCarta + 25;
    //private final int height = numCartasY * tamCarta + 100;
    private String tipoPartida;
    boolean tableroObtenido = false;
    Carta[][] cartas = new Carta[numCartasY][numCartasX];
    Icon [] iconosCartas = new Icon[20];
    Container contenedor;
    int movimientos;
    int movimientosOtro;
    int misPuntos, susPuntos;
    boolean tuTurno, partidaIniciada;
    int idJugador;
    
    JFrame faux;
    //conexion
    String dir="127.0.0.1";
    int pto = 9000;
    ByteBuffer b1 = null, b2 = null;
    InetSocketAddress dst;
    SocketChannel cl;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    Selector sel;  
    
    public VentanaMultijugador(String tipoPartida, JFrame s) {
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
        this.faux  = s;
        this.movimientos = 0;
        this.movimientosOtro = 0;
        this.misPuntos = 0;
        this.susPuntos = 0;
        tuTurno = false;
        partidaIniciada = false;
        iniciar();  
       
    }
    
    private void iniciar() {
        contenedor = getContentPane();
        setBounds(0, 0, width, height);
        setLayout(null);
        setLocationRelativeTo(null);
        setTitle(tipoPartida + "  >  ID Partida: [abttr4@hg845:780]");
        
        setResizable(false);
        
        //conectarse al servidor
        conectarse();
        
        enviarDatos("MUINI");
        String miId = recibirDatos();
        //System.out.println("Mi id es = " + miId);
        idJugador = Integer.parseInt(miId);
        
        //recibir posiciones de tablero
        enviarDatos("MUTAB");
        String posiciones = recibirDatos();
        //System.out.println("posiciones: " + posiciones);
        //convertir posiciones a array bidimensional
        //System.out.println("parseando string a tablero aleatorio...");
        String posicionesAleatorias [][] = posicionesString2Array2D(posiciones);
        //System.out.println("Tablero parseado.");        
        //cargar las imagenes a iconos
        cargarImagenes();
        //System.out.println("imagenes obtenidas...");
        System.out.println("Accion > Patron Tablero Recibido");
        
        //obtener turno
        
        
        for(int i = 0; i < numCartasY; i++){
            for (int j = 0; j < numCartasX; j++) {
                Carta carta = new Carta(Integer.parseInt(posicionesAleatorias[i][j]), j, i); 
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
        
        enviarDatos("MUTUR");
        String turnoString = recibirDatos();
        //System.out.println("Turno: " + turnoString);
        tuTurno = Boolean.parseBoolean(turnoString);
    }
    
    private void cartaPulsada(MouseEvent e) {
        Carta c = (Carta) e.getComponent();
        if (e.getButton() == MouseEvent.BUTTON1){
            if(partidaIniciada && tuTurno){
                if(movimientos < 2){
                    if(!c.getPresionado()){
                        System.out.println("Accion > Boton Presionado: "+c.getId());
                        c.setIcon(c.getIconoOculto());
                        
                        c.setPresionado(true);
                        movimientos++;
                        
                        //enviar movimiento al servidor
                        enviarDatos("MUJUG:"+c.getX_pos()+","+c.getY_pos());
                        String resultadoJugada = recibirDatos();
                        //System.out.println(resultadoJugada);
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
    
    private String [][] posicionesString2Array2D(String pos){
        String resultado [][] = new String[8][5];
        //System.out.println("Pos dentro de funcion: " + pos);
        String filas []= pos.split("\\.");
        
        for (int i = 0; i < 8; i++) {
            String obtenidas [] = filas[i].split(",");
            for (int j = 0; j < 5; j++) {
                resultado[i][j] = obtenidas[j];
            }
        }
        return resultado;
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
                            System.out.println("Modo > Multijugador");
                            try{
                                ch.finishConnect();
                            }catch(Exception e){
                                e.printStackTrace();
                            }//catch
                            System.out.println("Accion > Conexion Exitosa");
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
                    System.out.println("Accion > Actualizando objeto partida");
                    b2 = ByteBuffer.wrap(mm);
                    ch.write(b2);
                    k.interestOps(SelectionKey.OP_READ);
                }
            }
        } catch (Exception e) {
        }
    }
    
    public String recibirDatos(){
        String recibido = "";
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
                    recibido = new String(b1.array(),0,n);
                    //System.out.println("Eco  de "+n+" bytes recibido: "+recibido);
                    k.interestOps(SelectionKey.OP_WRITE);
                }
            }
        } catch (Exception e) {
        }
        return recibido;
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
        //System.out.println("Iniciamos paps");
        //obtenemos turno
        try{
            while(true){
                if(partidaIniciada){
                    if(tuTurno){
                        if(movimientos == 2){
                            if(checarTablero()){
                                movimientos = 0;
                                System.out.println("Accion > Pareja Encontrada");
                                misPuntos++;
                                if(ganaste()){
                                    JOptionPane.showMessageDialog(null, "Accion > Partida Finalizada");
                                    //enviar tiempo y nombre al servidor
                                    if(misPuntos > susPuntos){
                                        JOptionPane.showMessageDialog(null, "Ganador > Parejas Encontradas: " +misPuntos);
                                    }else if(susPuntos > misPuntos){
                                        JOptionPane.showMessageDialog(null, "Perdedor > Parejas Encontradas: " +misPuntos);
                                    }else{
                                        JOptionPane.showMessageDialog(null, "Empate > Nadie Gana");
                                    }
                                    

                                    //enviarDatos("INFIN:"+nombre+"|"+crono.getText());
                                    //recibirDatos();
                                    desconectarse();
                                    dispose();
                                    faux.setVisible(true);                        
                                }
                            }else{
                                movimientos = 3;
                                System.out.println("Accion > Girando Cartas");
                                //pasarle turno al otro
                            }
                        }else if(movimientos == 3){
                                //girar cartas
                                //System.out.println("Pasaron 2 segundos de penalizacion");
                                movimientos = 0;
                                girarCartasErroneas();
                                tuTurno = false;
                        }
                        
                    }else{ //turno del otro jugador
                        if(movimientosOtro == 2){
                            if(checarTablero()){
                                movimientosOtro = 0;
                                susPuntos++;
                                if(ganaste()){
                                    //System.out.println("fin de juego multiplayer");
                                    JOptionPane.showMessageDialog(null, "Accion > Partida Finalizada");
                                    //enviar tiempo y nombre al servidor
                                    if(misPuntos > susPuntos){
                                        JOptionPane.showMessageDialog(null, "Ganador > Parejas Encontradas: " +misPuntos);
                                    }else if(susPuntos > misPuntos){
                                        JOptionPane.showMessageDialog(null, "Perdedor > Parejas Encontradas: " +misPuntos);
                                    }else{
                                        JOptionPane.showMessageDialog(null, "Empate > Nadie Gana");
                                    }
                                    

                                    //enviarDatos("INFIN:"+nombre+"|"+crono.getText());
                                    //recibirDatos();
                                    desconectarse();
                                    dispose();
                                    faux.setVisible(true);                        
                                }
                            }else{
                                 movimientosOtro = 3;
                            }
                        }else if(movimientosOtro == 3){
                            //System.out.println("Pasaron 2 segundos de penalizacion");
                            movimientosOtro = 0;
                            girarCartasErroneas();
                            tuTurno = true;
                        }
                        //estar escuchando jugada
                        //System.out.println("Preguntar por jugada..");
                        enviarDatos("MULMO");
                        String ultimoMovString = recibirDatos();
                        //System.out.println("|"+ultimoMovString+"|");
                        if(!ultimoMovString.equals(" ")){
                            String coords [] = ultimoMovString.split(",");
                            int x_aux = Integer.parseInt(coords[0]);
                            int y_aux = Integer.parseInt(coords[1]);
                            
                            cartas[y_aux][x_aux].setPresionado(true);
                            cartas[y_aux][x_aux].setIcon(cartas[y_aux][x_aux].getIconoOculto());
                            
                            movimientosOtro++;
                        }else{
                            //System.out.println("No hay jugada");
                        }
                    }
                }else{//checar si ya inicio la partida
                    enviarDatos("MUPAR"); //preguntar si ya inicio la partida
                    String iniciaPartidaString = recibirDatos();
                    //System.out.println("ya inicio la partida: " + iniciaPartidaString);
                    System.out.println("Accion > Actualizando Estatus Partida");
                    partidaIniciada = Boolean.parseBoolean(iniciaPartidaString);
                } 
                Thread.sleep(100);
            }
        }catch(Exception e){}
    }

    
}
