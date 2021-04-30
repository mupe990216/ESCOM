package M;

import V.Tablero;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;

public class Principal {

    public static Tablero tab;
    public static Partida Objeto;

    /*
        Clase para probar codigos
     */
    public static void main(String args[]) {
        generarPosicionesAleatoriasMemorama();
        //generarPosicionesEnOrdenMemorama();
    }  

    public static void generarPosicionesAleatoriasMemorama() {
        ArrayList<String> TableroTemporal = new ArrayList<>();
        TableroTemporal.add("null");

        String nombreImagenes[] = {
            "imagen1.jpg", "imagen2.jpg", "imagen3.jpg", "imagen4.jpg", "imagen5.jpg",
            "imagen6.jpg", "imagen7.jpg", "imagen8.jpg", "imagen9.jpg", "imagen10.jpg",
            "imagen11.jpg", "imagen12.jpg", "imagen13.jpg", "imagen14.jpg", "imagen15.jpg",
            "imagen16.jpg", "imagen17.jpg", "imagen18.jpg", "imagen19.jpg", "imagen20.jpg",
            "imagen1.jpg", "imagen2.jpg", "imagen3.jpg", "imagen4.jpg", "imagen5.jpg",
            "imagen6.jpg", "imagen7.jpg", "imagen8.jpg", "imagen9.jpg", "imagen10.jpg",
            "imagen11.jpg", "imagen12.jpg", "imagen13.jpg", "imagen14.jpg", "imagen15.jpg",
            "imagen16.jpg", "imagen17.jpg", "imagen18.jpg", "imagen19.jpg", "imagen20.jpg"
        };

        // Metemos en una lista los números del 1 al 40.
        List<Integer> numbers = new ArrayList<>(40);
        for (int i = 0; i < 40; i++) {
            numbers.add(i);
        }

        // Instanciamos la clase Random
        Random random = new Random();

        // Mientras queden cartas en el mazo (en la lista de numbers)
        while (numbers.size() > 1) {
            // Elegimos un índice al azar, entre 0 y el número de cartas que quedan por sacar
            int randomIndex = random.nextInt(numbers.size());

            // Damos la carta al jugador (sacamos el número por pantalla)
            //System.out.println("Not Repeated Random Number " + numbers.get(randomIndex));
            TableroTemporal.add(nombreImagenes[numbers.get(randomIndex)]);

            // Y eliminamos la carta del mazo (la borramos de la lista)
            numbers.remove(randomIndex);
        }
        TableroTemporal.add(nombreImagenes[numbers.get(0)]);
        int i = 0;
        for (String elemento : TableroTemporal) {
            System.out.println(i + " > " + elemento);
            i++;
        }
    }

    public static void generarPosicionesEnOrdenMemorama() {
        ArrayList<String> TableroTemporal = new ArrayList<>();
        TableroTemporal.add("null");

        String nombreImagenes[] = {
            "imagen1.jpg", "imagen1.jpg", "imagen2.jpg", "imagen2.jpg",
            "imagen3.jpg", "imagen3.jpg", "imagen4.jpg", "imagen4.jpg",
            "imagen5.jpg", "imagen5.jpg", "imagen6.jpg", "imagen6.jpg",
            "imagen7.jpg", "imagen7.jpg", "imagen8.jpg", "imagen9.jpg",
            "imagen10.jpg", "imagen10.jpg",
            "imagen11.jpg", "imagen11.jpg", "imagen12.jpg", "imagen12.jpg",
            "imagen13.jpg", "imagen13.jpg", "imagen14.jpg", "imagen14.jpg",
            "imagen15.jpg", "imagen15.jpg", "imagen16.jpg", "imagen16.jpg",
            "imagen17.jpg", "imagen17.jpg", "imagen18.jpg", "imagen19.jpg",
            "imagen20.jpg", "imagen20.jpg"
        };
        TableroTemporal.addAll(Arrays.asList(nombreImagenes));

        int i = 0;
        for (String elemento : TableroTemporal) {
            System.out.println(i + " > " + elemento);
            i++;
        }

    }

    public void creaVentanaOpciones() {
        String opciones[] = {"Solitario", "Multijugador", "Cancelar"};
        int seleccion = JOptionPane.showOptionDialog(
                null,
                "Seleccione un modo de juego",
                "Memorama",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );
        if (seleccion == 0) {
            System.out.println("Solitario");
        } else if (seleccion == 1) {
            System.out.println("Multijugador");
        } else {
            System.out.println("Adios");
            System.exit(0);
        }
    }

}
