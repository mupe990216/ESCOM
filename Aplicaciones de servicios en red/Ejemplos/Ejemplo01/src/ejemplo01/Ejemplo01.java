/*
    Muñoz Primero Elias
    Crash Course Java - Repaso General
    Programa 01 - Entrada, Salida y Tipos de datos en Java
*/
package ejemplo01;

import java.util.Scanner;
import javax.swing.JOptionPane;

public class Ejemplo01 {

    public static void main(String[] args) {
        /*Entrada y Salida de Datos en Java*/
        Scanner entrada = new Scanner(System.in);
        final int constante = 5;
        final float constante2 = 5.4f;
        int numero;
        float decimal;
        char letra;
        String oracion;        
        
        /* Pedimos y mostramos datos en formato de consola */     
        System.out.println("Hola soy una constante entera: "+constante+"\n");
        System.out.println("Hola soy una constante flotante: "+constante2+"\n");
        
        System.out.println("Escribe una letra: ");
        letra = entrada.next().charAt(0);
        System.out.println(" **** Letra ingresada:  "+letra+" ****  \n");
                
        entrada.nextLine();//Limpiamos el buffer con el metodo nextLine en el objeto Scanner
        
        System.out.println("Escribe una cadena: ");
        oracion = entrada.nextLine();
        System.out.println(" **** Cadena ingresada:  "+oracion+" ****  \n");
        
        System.out.println("Escribe un numero entero: ");
        numero = entrada.nextInt();        
        System.out.println(" **** Entero ingresado:  "+numero+" ****  \n");        
        
        System.out.println("Escribe un numero decimal: ");
        decimal = entrada.nextFloat();
        System.out.println(" **** Decimal ingresado:  "+decimal+" ****  \n");        
        
        
        /* Pedimos y mostramos los datos con GUI Swing de Java*/        
        oracion = JOptionPane.showInputDialog("Escribe Una Cadena");
        JOptionPane.showMessageDialog(null, "La Cadena es: "+oracion);
        
        letra = JOptionPane.showInputDialog("Escribe Un Caracter").charAt(0);
        JOptionPane.showMessageDialog(null, "EL caracter es: "+letra);
        
        numero = Integer.parseInt(JOptionPane.showInputDialog("Ingresa Un Numero Entero"));
        JOptionPane.showMessageDialog(null, "EL entero es: "+numero);
        
        decimal = (float)Double.parseDouble(JOptionPane.showInputDialog("Ingresa Un Numero Decimal"));
        JOptionPane.showMessageDialog(null, "EL decimal es: "+decimal);
    }

}
