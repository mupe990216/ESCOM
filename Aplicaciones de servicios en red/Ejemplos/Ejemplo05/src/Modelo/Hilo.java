package Modelo;

import Vista.Tablero;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Hilo extends Thread implements ActionListener {

    SeccionCritica _ObjCritico;
    Tablero _tablero;
    boolean HiloIniciado;

    /*
        #Constructor del objeto de la clase Hilos.
        Se utilizan dos parametros: 
        #id_hilo: es el ID de cada hilo y se hace llamada de su respectivo constructor de la
          clase 'Thread' la cual heradamos y a traves del metodo 'super()' inicializamos su nombre
          de tal forma que tenga la nomenclatura que queremos, en este caso seria ('hilo'+i).        
        #objetoCritico: Objeto de la clase SeccionCritica, como el nombre lo indica, sera el objeto
          que contiene todos los elementos por los cuales los hilos competiran por modificar, asi mismo
          se hace el paso por referencia ya que todos los hilos tendran el mismo objetoCritico a pares,
          es decir, cada partida iniciada tendra un objeto critico que compartiran dos hilos (cada jugador).
     */
    public Hilo(int id_hilo, SeccionCritica objetoCritico) {
        super("hilo" + id_hilo);
        this._ObjCritico = objetoCritico;
        HiloIniciado = false;
    }

    /*
        #Metodo Run(): Este metodo se hereda de la clase 'Thread', se tiene que sobreescribir para 
          utilizarlo en este problema, debido a que este metodo indica como se comportaran TODOS
          los hilos u objetos instanciados desde esta clase 'Hilos', es decir, todos los objetos 
          del tipo 'Hilo' al momento de ser ejecutados (invocados con el metodo start en el main)
          se comportaran como lo indica el metodo run() que sobreescribimos.
        - Nota: Como se sobreescribe, antes de la declaracion del metodo se coloca la instruccion '@Override' 
            para que java entienda que este metodo run() sera diferente unicamente para los objetos
            del tipo 'Hilo'.
     */
    @Override
    public void run() {
        // 1) Inicializamos tablero, jugadores y listeners
        while (true) {
            if (HiloIniciado == false) {
                asignaOrdenJuego();
                creaTablero();
                espera(2000);
                actualizaJugadores();
                implementarOyente();
                HiloIniciado = true;
            } else {
                //Aqui podemos actualizar el tablero
                if (_ObjCritico.ganador.equals("")) {//Si aun no hay un ganador
                    actualizaTablero();
                    if (_ObjCritico.contadorAcciones == 9) {
                        actualizaTablero();
                        bloqueaTablero();
                        //break;
                    }
                } else {//Si ya hay un ganador antes de usar todas las casillas
                    actualizaTablero();
                    bloqueaTablero();
                    break;
                }
            }

            //Partida terminada en empate
            if ((_ObjCritico.contadorAcciones == 9) && _ObjCritico.ganador.equals("")) {
                JOptionPane.showMessageDialog(null, getName() + ":  Empate");
                break;
            }

        }//While
        _tablero.dispose();
        System.out.println("Partida > " + _ObjCritico.id_Partida + "    " + getName() + " Finalizado");
    }

    /*
        #Funcion creaTablero: Crea un objeto del tipo tablero, sera el tablero del juego de gato 
            asignado al hilo en cuestion.
     */
    public void creaTablero() {
        if (_ObjCritico.nom_jugador1.equals(getName())) {
            _tablero = new Tablero(getName(), _ObjCritico.id_Partida, "X");
        }
        if (_ObjCritico.nom_jugador2.equals(getName())) {
            _tablero = new Tablero(getName(), _ObjCritico.id_Partida, "O");
        }
        _tablero.setVisible(true);
        //System.out.println("Ventana creada por: " + getName() + " para la partida: " + _ObjCritico.id_Partida);
    }

    public void bloqueaTablero() {
        _ObjCritico.bloqueaTablero();
        //Preguntar si quiere reiniciar las cosas.
    }

    /*
        #Funcion asignaOrdenJuego: Hace uso del objeto critico, ya que los hilos se van a pelear
            para ver quien sera el jugador1 y quien el jugador 2.
     */
    public void asignaOrdenJuego() {
        _ObjCritico.asignaJugadores(getName());//Pasamos el nombre del hilo actual
    }

    /*
        #Funcion actualizaTablero: Sirve para actualizar las etiquetas en la ventana de cada hilo
     */
    public void actualizaJugadores() {
        this._tablero.jugador1.setText(_ObjCritico.nom_jugador1);
        this._tablero.jugador2.setText(_ObjCritico.nom_jugador2);
    }

    /*
        #Funcion espera: implementamos un sleep con su manejo de excepcion con reutilizacion de codigo
     */
    public void espera(int duracion) {
        try {
            sleep(duracion);
        } catch (InterruptedException error) {
            System.out.println("Error al esperar: " + error.getMessage());
        }
    }

    /*
        #Funcion implementarOyente: implementamos la interfaz actionlistener a los elementos que esten dentro de la funcion
     */
    public void implementarOyente() {
        _tablero.btn1.addActionListener(this);
        _tablero.btn2.addActionListener(this);
        _tablero.btn3.addActionListener(this);
        _tablero.btn4.addActionListener(this);
        _tablero.btn5.addActionListener(this);
        _tablero.btn6.addActionListener(this);
        _tablero.btn7.addActionListener(this);
        _tablero.btn8.addActionListener(this);
        _tablero.btn9.addActionListener(this);
    }

    /*
        #Funcion actualizaTablero: Sirve para actualizar los cambios que se hagan a lo largo del juego
            dentro del tablero correspondiente a cada hilo involucrado.
     */
    public void actualizaTablero() {

        //Si el jugador actual es el 1, entonces actualiza lo que hizo el 2
        if (_ObjCritico.nom_jugador1.equals(getName())) {
            ArrayList<String> Jugadas2 = new ArrayList<>();
            Jugadas2 = _ObjCritico.get_jugadas2();
            for (String elemento : Jugadas2) {
                int id = Integer.parseInt(elemento);
                modifica_Tablero(id, 0);
            }
        }

        //Si el jugador actual es el 2, entonces actualiza lo que hizo el 1
        if (_ObjCritico.nom_jugador2.equals(getName())) {
            ArrayList<String> Jugadas1 = new ArrayList<>();
            Jugadas1 = _ObjCritico.get_jugadas1();
            for (String elemento : Jugadas1) {
                int id = Integer.parseInt(elemento);
                modifica_Tablero(id, 0);
            }
        }

    }

    /*
        #Funcion actionPerformed: Sirve para implementar las acciones que haran cada boton, dada la interfaz actionlistener
        #Entrada:
            e    -   objeto del tipo ActionEvent, el cual nos permitira conocer el origen del evento, 
                    en este caso, saber cual boton se acciono.        
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (_ObjCritico.contadorAcciones == 9) {
            System.out.println("Partida > " + _ObjCritico.id_Partida + "   Finalizada");
        } else if (_ObjCritico.turno.equals(getName())) {

            _ObjCritico.asignaTurno(getName());

            if (e.getSource() == _tablero.btn1) {
                if (_ObjCritico.matrizBotones[1] == true) {
                    modifica_Tablero(1, 1);
                }
            }
            if (e.getSource() == _tablero.btn2) {
                if (_ObjCritico.matrizBotones[2] == true) {
                    modifica_Tablero(2, 1);
                }
            }
            if (e.getSource() == _tablero.btn3) {
                if (_ObjCritico.matrizBotones[3] == true) {
                    modifica_Tablero(3, 1);
                }
            }
            if (e.getSource() == _tablero.btn4) {
                if (_ObjCritico.matrizBotones[4] == true) {
                    modifica_Tablero(4, 1);
                }
            }
            if (e.getSource() == _tablero.btn5) {
                if (_ObjCritico.matrizBotones[5] == true) {
                    modifica_Tablero(5, 1);
                }
            }
            if (e.getSource() == _tablero.btn6) {
                if (_ObjCritico.matrizBotones[6] == true) {
                    modifica_Tablero(6, 1);
                }
            }
            if (e.getSource() == _tablero.btn7) {
                if (_ObjCritico.matrizBotones[7] == true) {
                    modifica_Tablero(7, 1);
                }
            }
            if (e.getSource() == _tablero.btn8) {
                if (_ObjCritico.matrizBotones[8] == true) {
                    modifica_Tablero(8, 1);
                }
            }
            if (e.getSource() == _tablero.btn9) {
                if (_ObjCritico.matrizBotones[9] == true) {
                    modifica_Tablero(9, 1);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Espera a que el otro jugador tire");
        }
    }

    /*
        #Funcion modifica_Tablero: Sirve para modificar los botones agregandoles una imagen de fondo.
        #Entrada:
            idBoton   -   id del boton a modificar
                    
            opc   -  Sirve para establecer el comportamiento de este rutina
                opc 1: agregar el fondo a un boton dado un evento (el usuario dio click)
                opc 2: solo actualizar (actualizamos la eleccion del otro jugador)
     */
    public void modifica_Tablero(int idBoton, int opc) { //
        ImageIcon fondo = new ImageIcon();

        //Establecemos el fondo del boton con base en la accion que hizo el jugador actual
        if (opc == 1) {
            _ObjCritico.matrizBotones[idBoton] = false;
            _ObjCritico.agregarJugadas(getName(), "" + idBoton);
            _ObjCritico.aumentaContador();
            if (_ObjCritico.nom_jugador1.equals(getName())) {
                fondo = new ImageIcon("x.png");
            }
            if (_ObjCritico.nom_jugador2.equals(getName())) {
                fondo = new ImageIcon("o.png");
            }
            //verificaQuienGana: Checo las jugadas del jugador actual - opc 0
            verificaQuienGana();
        }

        //Actualizamos el tabler con base en las acciones del otro jugador - 
        if (opc == 0) {
            if (_ObjCritico.nom_jugador1.equals(getName())) {
                fondo = new ImageIcon("o.png");
            }
            if (_ObjCritico.nom_jugador2.equals(getName())) {
                fondo = new ImageIcon("x.png");
            }
            //verificaQuienGana: Checo si ya hay un ganador - opc 1
            //verificaQuienGana(1);
        }

        //Verificamos que el contador == 9, si es nueve y no hay ganador, mostrar empate
        if (idBoton == 1) {
            _tablero.btn1.setIcon(fondo);
        }
        if (idBoton == 2) {
            _tablero.btn2.setIcon(fondo);
        }
        if (idBoton == 3) {
            _tablero.btn3.setIcon(fondo);
        }
        if (idBoton == 4) {
            _tablero.btn4.setIcon(fondo);
        }
        if (idBoton == 5) {
            _tablero.btn5.setIcon(fondo);
        }
        if (idBoton == 6) {
            _tablero.btn6.setIcon(fondo);
        }
        if (idBoton == 7) {
            _tablero.btn7.setIcon(fondo);
        }
        if (idBoton == 8) {
            _tablero.btn8.setIcon(fondo);
        }
        if (idBoton == 9) {
            _tablero.btn9.setIcon(fondo);
        }
    }

    /*
        #Funcion verificaQuienGana: Sirve para verificar que jugador ha ganado.
        #Entrada: void - no recibe nada            
     */
    public void verificaQuienGana() {

        //Si el jugador actual es el 1, entonces verifica lo que hizo el 1 y asigna el ganador a 1
        if (_ObjCritico.nom_jugador1.equals(getName())) {
            ArrayList<String> Jugadas1 = new ArrayList<>();
            Jugadas1 = _ObjCritico.get_jugadas1();
            int btn_1 = Jugadas1.indexOf("1");
            int btn_2 = Jugadas1.indexOf("2");
            int btn_3 = Jugadas1.indexOf("3");
            int btn_4 = Jugadas1.indexOf("4");
            int btn_5 = Jugadas1.indexOf("5");
            int btn_6 = Jugadas1.indexOf("6");
            int btn_7 = Jugadas1.indexOf("7");
            int btn_8 = Jugadas1.indexOf("8");
            int btn_9 = Jugadas1.indexOf("9");
            if ((btn_1 != -1) && (btn_2 != -1) && (btn_3 != -1)) {
                System.out.println("Partida > " + _ObjCritico.id_Partida + "   " + getName() + " - Gano con combinacion 1");
                _ObjCritico.asignaGanador(getName());
                JOptionPane.showMessageDialog(null, "¡Felicidades " + getName() + " Ganaste! :3");
            }
            if ((btn_1 != -1) && (btn_4 != -1) && (btn_7 != -1)) {
                System.out.println("Partida > " + _ObjCritico.id_Partida + "   " + getName() + " - Gano con combinacion 2");
                _ObjCritico.asignaGanador(getName());
                JOptionPane.showMessageDialog(null, "¡Felicidades " + getName() + " Ganaste! :3");
            }
            if ((btn_1 != -1) && (btn_5 != -1) && (btn_9 != -1)) {
                System.out.println("Partida > " + _ObjCritico.id_Partida + "   " + getName() + " - Gano con combinacion 3");
                _ObjCritico.asignaGanador(getName());
                JOptionPane.showMessageDialog(null, "¡Felicidades " + getName() + " Ganaste! :3");
            }
            if ((btn_2 != -1) && (btn_5 != -1) && (btn_8 != -1)) {
                System.out.println("Partida > " + _ObjCritico.id_Partida + "   " + getName() + " - Gano con combinacion 4");
                _ObjCritico.asignaGanador(getName());
                JOptionPane.showMessageDialog(null, "¡Felicidades " + getName() + " Ganaste! :3");
            }
            if ((btn_3 != -1) && (btn_6 != -1) && (btn_9 != -1)) {
                System.out.println("Partida > " + _ObjCritico.id_Partida + "   " + getName() + " - Gano con combinacion 5");
                _ObjCritico.asignaGanador(getName());
                JOptionPane.showMessageDialog(null, "¡Felicidades " + getName() + " Ganaste! :3");
            }
            if ((btn_3 != -1) && (btn_5 != -1) && (btn_7 != -1)) {
                System.out.println("Partida > " + _ObjCritico.id_Partida + "   " + getName() + " - Gano con combinacion 6");
                _ObjCritico.asignaGanador(getName());
                JOptionPane.showMessageDialog(null, "¡Felicidades " + getName() + " Ganaste! :3");
            }
            if ((btn_4 != -1) && (btn_5 != -1) && (btn_6 != -1)) {
                System.out.println("Partida > " + _ObjCritico.id_Partida + "   " + getName() + " - Gano con combinacion 7");
                _ObjCritico.asignaGanador(getName());
                JOptionPane.showMessageDialog(null, "¡Felicidades " + getName() + " Ganaste! :3");
            }
            if ((btn_7 != -1) && (btn_8 != -1) && (btn_9 != -1)) {
                System.out.println("Partida > " + _ObjCritico.id_Partida + "   " + getName() + " - Gano con combinacion 8");
                _ObjCritico.asignaGanador(getName());
                JOptionPane.showMessageDialog(null, "¡Felicidades " + getName() + " Ganaste! :3");
            }
        }

        //Si el jugador actual es el 2, entonces verifica lo que hizo el 2 y asigna el ganador a 2
        if (_ObjCritico.nom_jugador2.equals(getName())) {
            ArrayList<String> Jugadas2 = new ArrayList<>();
            Jugadas2 = _ObjCritico.get_jugadas2();
            int btn_1 = Jugadas2.indexOf("1");
            int btn_2 = Jugadas2.indexOf("2");
            int btn_3 = Jugadas2.indexOf("3");
            int btn_4 = Jugadas2.indexOf("4");
            int btn_5 = Jugadas2.indexOf("5");
            int btn_6 = Jugadas2.indexOf("6");
            int btn_7 = Jugadas2.indexOf("7");
            int btn_8 = Jugadas2.indexOf("8");
            int btn_9 = Jugadas2.indexOf("9");
            if ((btn_1 != -1) && (btn_2 != -1) && (btn_3 != -1)) {
                System.out.println("Partida > " + _ObjCritico.id_Partida + "   " + getName() + " - Gano con combinacion 1");
                _ObjCritico.asignaGanador(getName());
                JOptionPane.showMessageDialog(null, "¡Felicidades " + getName() + " Ganaste! :3");
            }
            if ((btn_1 != -1) && (btn_4 != -1) && (btn_7 != -1)) {
                System.out.println("Partida > " + _ObjCritico.id_Partida + "   " + getName() + " - Gano con combinacion 2");
                _ObjCritico.asignaGanador(getName());
                JOptionPane.showMessageDialog(null, "¡Felicidades " + getName() + " Ganaste! :3");
            }
            if ((btn_1 != -1) && (btn_5 != -1) && (btn_9 != -1)) {
                System.out.println("Partida > " + _ObjCritico.id_Partida + "   " + getName() + " - Gano con combinacion 3");
                _ObjCritico.asignaGanador(getName());
                JOptionPane.showMessageDialog(null, "¡Felicidades " + getName() + " Ganaste! :3");
            }
            if ((btn_2 != -1) && (btn_5 != -1) && (btn_8 != -1)) {
                System.out.println("Partida > " + _ObjCritico.id_Partida + "   " + getName() + " - Gano con combinacion 4");
                _ObjCritico.asignaGanador(getName());
                JOptionPane.showMessageDialog(null, "¡Felicidades " + getName() + " Ganaste! :3");
            }
            if ((btn_3 != -1) && (btn_6 != -1) && (btn_9 != -1)) {
                System.out.println("Partida > " + _ObjCritico.id_Partida + "   " + getName() + " - Gano con combinacion 5");
                _ObjCritico.asignaGanador(getName());
                JOptionPane.showMessageDialog(null, "¡Felicidades " + getName() + " Ganaste! :3");
            }
            if ((btn_3 != -1) && (btn_5 != -1) && (btn_7 != -1)) {
                System.out.println("Partida > " + _ObjCritico.id_Partida + "   " + getName() + " - Gano con combinacion 6");
                _ObjCritico.asignaGanador(getName());
                JOptionPane.showMessageDialog(null, "¡Felicidades " + getName() + " Ganaste! :3");
            }
            if ((btn_4 != -1) && (btn_5 != -1) && (btn_6 != -1)) {
                System.out.println("Partida > " + _ObjCritico.id_Partida + "   " + getName() + " - Gano con combinacion 7");
                _ObjCritico.asignaGanador(getName());
                JOptionPane.showMessageDialog(null, "¡Felicidades " + getName() + " Ganaste! :3");
            }
            if ((btn_7 != -1) && (btn_8 != -1) && (btn_9 != -1)) {
                System.out.println("Partida > " + _ObjCritico.id_Partida + "   " + getName() + " - Gano con combinacion 8");
                _ObjCritico.asignaGanador(getName());
                JOptionPane.showMessageDialog(null, "¡Felicidades " + getName() + " Ganaste! :3");
            }
        }

    }

}
