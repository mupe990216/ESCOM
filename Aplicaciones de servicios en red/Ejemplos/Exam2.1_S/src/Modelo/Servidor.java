package Modelo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Servidor {
    // cuando inicia el servidor, se queda escuchando peticiones
    private final int port;
    private ServerSocketChannel s;
    private Selector sel;
    private ArrayList<Record> records = new ArrayList<>();
    private ArrayList<Partida> partidas = new ArrayList<>();
    
    Map<SocketChannel, Integer> idTracking = new HashMap<>();   
    Map<Integer, Partida> partidaTracking = new HashMap<>();
    Map<Integer, String> actividadTracking = new HashMap<>();
    
    String [][] ultimaPartidaGenerada;
    int numJugadores;
    
    public Servidor(int port) {
        this.port = port;
        this.numJugadores = 0;
        init();
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
    
    
    private void init(){
        String pos [][] = generarPosicionesAleatoriasMemorama();
        
        try {
            //configurar ServerSocketChannel
            System.out.println("--- SERVIDOR INICIADO ---");
            s = ServerSocketChannel.open();
            s.configureBlocking(false); //hacer no bloqueante
            s.socket().bind(new InetSocketAddress(port));
            System.out.println("Accion > Servidor escuchando el puerto: " + port);
            System.out.println("Accion > Esperando clientes");
            sel = Selector.open();
            s.register(sel, SelectionKey.OP_ACCEPT);
            
            while(true){
                sel.select(); //revisar si hay nuevos eventos
                Iterator <SelectionKey> it = sel.selectedKeys().iterator();
                while(it.hasNext()){
                    SelectionKey k = (SelectionKey) it.next();
                    it.remove();
                    if(k.isAcceptable()){//se intentan conectar al servidor
                        SocketChannel cl = s.accept();
                        System.out.println("Accion > Cliente conectado " + cl.socket().getInetAddress()+":"+cl.socket().getPort());
                        cl.configureBlocking(false);
                        cl.register(sel, SelectionKey.OP_READ);
                        continue;
                    }
                    if(k.isReadable()){
                        try{
                            SocketChannel ch = (SocketChannel)k.channel();
                            ByteBuffer b = ByteBuffer.allocate(2000);
                            b.clear();
                            int n=0; 
                            String msj="";
                            n=ch.read(b);
                            b.flip();
                            if(n>0)
                               msj = new String(b.array(),0,n);
                            //System.out.println("Recibido: "+msj);
                            if (msj.equalsIgnoreCase("SALIR")){
                                k.interestOps(SelectionKey.OP_WRITE);
                                ch.close();
                            }else if(msj.substring(0, 5).equals("INFIN")){//fin de partida individual, se recibe su record
                                records.add(new Record(msj.substring(6)));
                                
                                imprimirRecords();
                                k.interestOps(SelectionKey.OP_WRITE);
                            }else if(msj.substring(0, 5).equals("MUINI")){//inicio de partida multijugador
                                numJugadores++;
                                //System.out.println("Jugador "+numJugadores + " ha ingresado");
                                if(numJugadores % 2 == 1){//llega un nuevo jugador 1
                                    //crear partida
                                    //System.out.println("Creando partida...");
                                    partidas.add(new Partida(generarPosicionesEnOrdenMemorama(), numJugadores));
                                    //partidas.add(new Partida(generarPosicionesAleatoriasMemorama(), numJugadores));
                                    //mapear ch con el id del usuario, asi siempre que se tenga su canal, sabemos que id tiene el usuairo
                                    idTracking.put(ch, numJugadores);
                                    //mapear id del usuario con la partida, asi podemos encontrar en que partida juega x usuario
                                    partidaTracking.put(numJugadores, partidas.get(partidas.size()-1));
                                    actividadTracking.put(numJugadores, "EnviarID");
                                }else{ //llega jugador 2   
                                    //obtener ultima partida creada y asignarle el id del segundo jugador
                                    //System.out.println("se ingresa a ultima partida creada...");
                                    partidas.get(partidas.size()-1).idJugador2 = numJugadores;
                                    partidas.get(partidas.size()-1).comenzado = true;
                                    idTracking.put(ch, numJugadores);
                                    //mapear id del usuario con la partida, asi podemos encontrar en que partida juega x usuario
                                    partidaTracking.put(numJugadores, partidas.get(partidas.size()-1));
                                    actividadTracking.put(numJugadores, "EnviarID");
                                }
                                
//                                for(int i = 0; i < partidas.size(); i++){
//                                    System.out.println("Partida "+i+": idJugador1:"+partidas.get(i).idJugador1+", idJugador2:"+partidas.get(i).idJugador2);
//                                }
                                //checar si hay partidas, y unirme a una si esta libre, sino crear sala
                                k.interestOps(SelectionKey.OP_WRITE);
                            }else if(msj.substring(0, 5).equals("MUTAB")){//instruccion de enviar tablero
                                int idJugador = idTracking.get(ch);
                                actividadTracking.remove(idJugador);
                                actividadTracking.put(idJugador, "EnviarTablero");    
                                k.interestOps(SelectionKey.OP_WRITE);
                            }else if(msj.substring(0, 5).equals("MUTUR")){//instruccion de enviar turno
                                int idJugador = idTracking.get(ch);
                                actividadTracking.remove(idJugador);
                                actividadTracking.put(idJugador, "EnviarTurno");    
                                k.interestOps(SelectionKey.OP_WRITE);
                            }else if(msj.substring(0, 5).equals("MUPAR")){//instruccion de inicio de partida
                                int idJugador = idTracking.get(ch);
                                actividadTracking.remove(idJugador);
                                actividadTracking.put(idJugador, "EnviarEmpezado");    
                                k.interestOps(SelectionKey.OP_WRITE);
                            }else if(msj.substring(0, 5).equals("MUJUG")){//instruccion de jugada
                                int idJugador = idTracking.get(ch);
                                Partida pAux = partidaTracking.get(idJugador);
                                pAux.ultimaJugada = msj.substring(6);
                                actividadTracking.remove(idJugador);
                                actividadTracking.put(idJugador, "EnviarJugada");    
                                k.interestOps(SelectionKey.OP_WRITE);
                            }else if(msj.substring(0, 5).equals("MULMO")){//instruccion de lectura de ultima jugada
                                int idJugador = idTracking.get(ch);
                                actividadTracking.remove(idJugador);
                                actividadTracking.put(idJugador, "LeyendoJugada");   
                                k.interestOps(SelectionKey.OP_WRITE);
                            }else{
                                k.interestOps(SelectionKey.OP_WRITE);
                            }
                            
                        }catch(IOException io){
                        }
                        continue;
                    }
                    if(k.isWritable()){
                        try{
                            SocketChannel ch = (SocketChannel)k.channel();
                            ByteBuffer bb;
                            boolean turno;
                            if(idTracking.get(ch) != null){
                                //obtener el id del jugador en el canal
                                int idJugador = idTracking.get(ch);

                                //System.out.println("Id del jugador basasdo en el ch es: " + idJugador);
                                //obtener partida para saber de quien es el turno
                                Partida partidaAux = partidaTracking.get(idJugador);
                                String actividad = actividadTracking.get(idJugador);
                                //System.out.println("Actividad: " + actividad);
                                if(actividad.equals("EnviarID")){
                                    //System.out.println("Actividad: Enviando id...");
                                    bb = ByteBuffer.wrap(Integer.toString(idJugador).getBytes());
                                }else if(actividad.equals("EnviarTablero")){
                                    //System.out.println("Actividad: enviando tablero del juego...");
                                    bb = ByteBuffer.wrap(partidaAux.convertirPosicionesAString().getBytes());
                                }else if(actividad.equals("EnviarTurno")){
                                    //System.out.println("Actividad: enviando turno de la partida...");
                                    if(idJugador %2 ==1){
                                        bb = ByteBuffer.wrap(Boolean.toString(partidaAux.turno1).getBytes());
                                    }else{
                                        bb = ByteBuffer.wrap(Boolean.toString(partidaAux.turno2).getBytes());
                                    }
                                }else if(actividad.equals("EnviarEmpezado")){
                                    //System.out.println("Actividad: enviando si ya se inicio la partida...");
                                    bb = ByteBuffer.wrap(Boolean.toString(partidaAux.comenzado).getBytes());
                                }else if(actividad.equals("EnviarJugada")){
                                    //System.out.println("Actividad: enviando jugada de partida...");
                                    bb = ByteBuffer.wrap("jugada recibida".getBytes());
                                }else if(actividad.equals("LeyendoJugada")){
                                    String ultimaJugadaAgregada = partidaAux.ultimaJugada;
                                    if(!ultimaJugadaAgregada.equals(" ")){
                                        //System.out.println("Actividad: leyendo ultima jugada de partida...");
                                        partidaAux.ultimaJugada = " ";
                                    }else{
                                        //System.out.println("Actividad: no hay jugada...");
                                    }
                                    bb = ByteBuffer.wrap(ultimaJugadaAgregada.getBytes());
                                }else{
                                    bb = ByteBuffer.wrap("wat".getBytes());
                                }    
                            }else{
                                //System.out.println("Actividad: otros");
                                bb = ByteBuffer.wrap("fin".getBytes());
                                System.out.println("Accion > Juego Solitario Finalizado");
                            }
                            ch.write(bb);
                            //System.out.println("Mensaje enviado");
                        }catch(Exception e){
                        }
                        k.interestOps(SelectionKey.OP_READ);
                        continue;
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void imprimirRecords(){
        Comparator<Record> comparator = Comparator.comparing(record -> record.minutos);
        comparator = comparator.thenComparing(Comparator.comparing(record -> record.segundos));
        comparator = comparator.thenComparing(Comparator.comparing(record -> record.milesimas));
        
        Stream<Record> recordStream = records.stream().sorted(comparator);
        
        List<Record> sortedRecords = recordStream.collect(Collectors.toList());
        System.out.println("\n \t ******* Tiempos Registrados Solitario *******");
        System.out.println("\t\t Jugador \tTiempo \t(mm:ss:mm)\n");
        for(int i = sortedRecords.size()-1; i >= 0; i--) {
            System.out.println("\t\t "+sortedRecords.get(i).nombre+" \t\t"+sortedRecords.get(i).minutos +":"+sortedRecords.get(i).segundos+":"+sortedRecords.get(i).milesimas);
        }
        System.out.println(" \t ***********************************************\n");
    }
}
