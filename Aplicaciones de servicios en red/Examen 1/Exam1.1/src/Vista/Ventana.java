package Vista;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

public class Ventana extends JFrame {

    public JPanel panel;
    public JLabel HilosActivos, NodoAnterior, NodoSiguiente, CarpetaAsociada, etiqueta1, etiqueta2, etiqueta3, etiqueta4;
    public JButton btn_Buscar;
    public JTextField jtf_NombreArchivo;
    public JTextArea jta_Notificaciones;
    public JScrollPane barra_scroll;
    public String idHilo;

    /*
        Constructor principal
     */
    public Ventana() {
        setSize(535, 860);//Establecemos el tamaño de la ventana (Ancho, Alto)
        setTitle("Muñoz Primero Elias - Examen Primer Parcial");//Establecemos titulo de la ventana
        setLocationRelativeTo(null);//Establecemos la ventana en el centro
        setResizable(false);//Establecemos si la ventana puede cambiar de tamaño; False-No puede, True-Si puede

        iniciarComponentes();

        setDefaultCloseOperation(EXIT_ON_CLOSE);//Establecemos la opcion de que se cierre el programa con la cruz
    }

    /*
        Constructor de ventana para hilos
     */
    public Ventana(String nombreHilo) {
        setSize(545, 815);//Establecemos el tamaño de la ventana (Ancho, Alto)
        //setBounds(rango, rango, 550, 860); //Posicion y luego tamaño
        setTitle(nombreHilo);//Establecemos titulo de la ventana
        setLocationRelativeTo(null);
        setResizable(false);//Establecemos si la ventana puede cambiar de tamaño; False-No puede, True-Si puede

        iniciarComponentes();
        panel.setBackground(new Color(130, 200, 235));
        HilosActivos.setBackground(new Color(90, 150, 235));
        NodoAnterior.setBackground(new Color(90, 150, 235));
        NodoSiguiente.setBackground(new Color(90, 150, 235));
        CarpetaAsociada.setBackground(new Color(90, 150, 235));
        etiqueta1.setBackground(new Color(90, 150, 235));
        etiqueta2.setBackground(new Color(90, 150, 235));
        etiqueta3.setBackground(new Color(90, 150, 235));
        etiqueta4.setBackground(new Color(90, 150, 235));
        btn_Buscar.setBackground(new Color(20, 125, 190));        
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);//Decimos que unicamente se cierra la ventana del hilo, no el programa
        //setDefaultCloseOperation(DISPOSE_ON_CLOSE);//Decimos que unicamente se cierra la ventana del hilo, no el programa
    }    
    
    
    
    private void iniciarComponentes() {
        colocaPanel();
        colocaEtiquetas();
        colocaBotones();
        colocaAreasTexto();
    }

    private void colocaPanel() {
        panel = new JPanel();
        panel.setLayout(null); // Desactivamos el diseño por defecto para modificar a gusto
        panel.setBackground(new Color(135, 145, 225));
        this.getContentPane().add(panel); //Agregamos el panel a la ventana
    }

    private void colocaEtiquetas() {
        etiqueta1 = new JLabel();
        etiqueta1.setBounds(50, 20, 200, 50);
        etiqueta1.setOpaque(true);//Desactivamos el diseño por defecto para modificar a gusto
        etiqueta1.setForeground(Color.white);
        etiqueta1.setBackground(new Color(85, 85, 205));
        etiqueta1.setText("Hilos Activos:");
        etiqueta1.setHorizontalAlignment(SwingConstants.CENTER);//Alineacion horizontal del texto en la etiqueta
        etiqueta1.setFont(new Font("arial", 1, 20));
        panel.add(etiqueta1);

        HilosActivos = new JLabel();
        HilosActivos.setBounds(290, 20, 200, 50);
        HilosActivos.setOpaque(true);//Desactivamos el diseño por defecto para modificar a gusto
        HilosActivos.setForeground(Color.white);
        HilosActivos.setBackground(new Color(85, 85, 205));
        HilosActivos.setText("#hilosactivos");
        HilosActivos.setHorizontalAlignment(SwingConstants.CENTER);//Alineacion horizontal del texto en la etiqueta
        HilosActivos.setFont(new Font("arial", 1, 20));
        panel.add(HilosActivos);

        etiqueta2 = new JLabel();
        etiqueta2.setBounds(50, 90, 200, 50);
        etiqueta2.setOpaque(true);//Desactivamos el diseño por defecto para modificar a gusto
        etiqueta2.setForeground(Color.white);
        etiqueta2.setBackground(new Color(85, 85, 205));
        etiqueta2.setText("Nodo Anterior:");
        etiqueta2.setHorizontalAlignment(SwingConstants.CENTER);//Alineacion horizontal del texto en la etiqueta
        etiqueta2.setFont(new Font("arial", 1, 20));
        panel.add(etiqueta2);

        NodoAnterior = new JLabel();
        NodoAnterior.setBounds(290, 90, 200, 50);
        NodoAnterior.setOpaque(true);//Desactivamos el diseño por defecto para modificar a gusto
        NodoAnterior.setForeground(Color.white);
        NodoAnterior.setBackground(new Color(85, 85, 205));
        NodoAnterior.setText("#nodoAnterior");
        NodoAnterior.setHorizontalAlignment(SwingConstants.CENTER);//Alineacion horizontal del texto en la etiqueta
        NodoAnterior.setFont(new Font("arial", 1, 20));
        panel.add(NodoAnterior);

        etiqueta3 = new JLabel();
        etiqueta3.setBounds(50, 160, 200, 50);
        etiqueta3.setOpaque(true);//Desactivamos el diseño por defecto para modificar a gusto
        etiqueta3.setForeground(Color.white);
        etiqueta3.setBackground(new Color(85, 85, 205));
        etiqueta3.setText("Nodo Siguiente:");
        etiqueta3.setHorizontalAlignment(SwingConstants.CENTER);//Alineacion horizontal del texto en la etiqueta
        etiqueta3.setFont(new Font("arial", 1, 20));
        panel.add(etiqueta3);

        NodoSiguiente = new JLabel();
        NodoSiguiente.setBounds(290, 160, 200, 50);
        NodoSiguiente.setOpaque(true);//Desactivamos el diseño por defecto para modificar a gusto
        NodoSiguiente.setForeground(Color.white);
        NodoSiguiente.setBackground(new Color(85, 85, 205));
        NodoSiguiente.setText("#nodoSiguiente");
        NodoSiguiente.setHorizontalAlignment(SwingConstants.CENTER);//Alineacion horizontal del texto en la etiqueta
        NodoSiguiente.setFont(new Font("arial", 1, 20));
        panel.add(NodoSiguiente);

        etiqueta4 = new JLabel();
        etiqueta4.setBounds(50, 230, 200, 50);
        etiqueta4.setOpaque(true);//Desactivamos el diseño por defecto para modificar a gusto
        etiqueta4.setForeground(Color.white);
        etiqueta4.setBackground(new Color(85, 85, 205));
        etiqueta4.setText("Carpeta Asociada:");
        etiqueta4.setHorizontalAlignment(SwingConstants.CENTER);//Alineacion horizontal del texto en la etiqueta
        etiqueta4.setFont(new Font("arial", 1, 20));
        panel.add(etiqueta4);

        CarpetaAsociada = new JLabel();
        CarpetaAsociada.setBounds(290, 230, 200, 50);
        CarpetaAsociada.setOpaque(true);//Desactivamos el diseño por defecto para modificar a gusto
        CarpetaAsociada.setForeground(Color.white);
        CarpetaAsociada.setBackground(new Color(85, 85, 205));
        CarpetaAsociada.setText("#carpetaAsociada");
        CarpetaAsociada.setHorizontalAlignment(SwingConstants.CENTER);//Alineacion horizontal del texto en la etiqueta
        CarpetaAsociada.setFont(new Font("arial", 1, 20));
        panel.add(CarpetaAsociada);
    }

    private void colocaBotones() {
        btn_Buscar = new JButton();
        btn_Buscar.setBounds(290, 300, 200, 50);
        btn_Buscar.setText("Buscar");
        btn_Buscar.setForeground(Color.white);
        btn_Buscar.setFont(new Font("arial", 1, 22));
        btn_Buscar.setBackground(new Color(105, 35, 195));
        panel.add(btn_Buscar);
    }

    private void colocaAreasTexto() {
        jtf_NombreArchivo = new JTextField();
        jtf_NombreArchivo.setBounds(50, 300, 200, 50);
        jtf_NombreArchivo.setFont(new Font("arial", 1, 22));
        jtf_NombreArchivo.setBackground(new Color(200, 215, 240));
        panel.add(jtf_NombreArchivo);

        jta_Notificaciones = new JTextArea();
        jta_Notificaciones.setBounds(20, 380, 500, 380);
        jta_Notificaciones.setFont(new Font("arial", 0, 15));
        //jta_Notificaciones.append("HOLA");//Agrega mas texto
        jta_Notificaciones.setBackground(new Color(200, 215, 240));
        panel.add(jta_Notificaciones);

        barra_scroll = new JScrollPane(jta_Notificaciones);
        barra_scroll.setBounds(20, 380, 500, 380);
        barra_scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        barra_scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(barra_scroll);
    }

}
