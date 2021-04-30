package Modelo;

public class Record {
    String nombre;
    String raw;
    int minutos, segundos, milesimas;
    long tiempoTotal;

    public Record(String raw) {
        this.raw = raw;
        formatearRaw();
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getRaw() {
        return raw;
    }

    public int getMilesimas() {
        return milesimas;
    }

    public int getSegundos() {
        return segundos;
    }

    public int getMinutos() {
        return minutos;
    }

    public String getNombre() {
        return nombre;
    }

    public long getTiempoTotal() {
        return tiempoTotal;
    }
    
    private void formatearRaw(){
        String partes[] = raw.split("\\|");      
        
        nombre = partes[0];
        String tiempo[] = partes[1].split(":");
        minutos = Integer.parseInt(tiempo[0]);
        segundos = Integer.parseInt(tiempo[1]);
        milesimas = Integer.parseInt(tiempo[2]);
    }
}
