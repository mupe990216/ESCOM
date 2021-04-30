package memoramaservidor;

public class Partida {
    String [][] posicionesIniciales;
    int idJugador1, idJugador2;
    boolean turno1, turno2, comenzado;
    String ultimaJugada;
    
    public Partida(String [][] posicionesIniciales, int idJugador1){
        this.posicionesIniciales = posicionesIniciales;
        this.idJugador1 = idJugador1;
        this.idJugador2 = -1;
        turno1 = true;
        turno2 = false;
        comenzado = false;
        ultimaJugada = " ";
    }
    
    public void anadirJugador(int idJugador2){
        this.idJugador2 = idJugador2;
    }
    
    public void jugada(int idJugador, int x, int y){
        
    }
    
    public String convertirPosicionesAString(){
        String posicionesConvetidas = "";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 5; j++) {
                posicionesConvetidas += posicionesIniciales[i][j];
                if(j != 4){
                    posicionesConvetidas += ",";
                }
            }
            if(i != 7){
                posicionesConvetidas += ".";
            }
        }
        return posicionesConvetidas;
    }
}
