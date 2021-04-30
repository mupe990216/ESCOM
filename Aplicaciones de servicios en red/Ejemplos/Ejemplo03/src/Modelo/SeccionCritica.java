package Modelo;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import javax.swing.JOptionPane;

public class SeccionCritica {

    Semaphore semaforo;
    boolean ArchivoEncontrado, ListaLlena, TodosEstanRecorridos, TodosEstanAvisados;
    int HilosUsados, PosicionRecorrida, MaximoHilos;
    ArrayList<String> NombreHilos;
    String HiloSeleccionado;
    String RespuestaFinal;

    /*
        #Constructor del objeto SeccionCritica
        - Se inicializa el semaforo, la lista de ejecucion asi como variables booleanas y enteras.
        - Se solicita el numero maximo de hilos que pueden estar dentro del ArrayList, esto con el fin
           de tener un mejor control de los hilos que van a competir por la seccion critica del objeto
           que se instancia desde esta clase, el cual sera el objetoCritico del problema.
     */
    public SeccionCritica(int tamanio) {
        semaforo = new Semaphore(1);
        ArchivoEncontrado = false;
        ListaLlena = false;
        TodosEstanRecorridos = false;
        TodosEstanAvisados = false;
        HilosUsados = 0;
        PosicionRecorrida = 0;
        MaximoHilos = tamanio;
        NombreHilos = new ArrayList<>();
        HiloSeleccionado = null;
        RespuestaFinal = null;
    }

    /*
        #Funcion HiloEstaEn_ListaEjecucion: Creada con el fin de verificar si el hilo que corra este codigo ya
          se encuentre dentro de la lista de ejecucion.
        #Entrada: nombreHilo, el cual es el ID del hilo en cuesiton
        #Salida: Regresa falso si el elemento no esta en la lista de ejecucion, caso contrario true
     */
    public boolean HiloEstaEn_ListaEjecucion(String nombreHilo) {
        boolean existe;
        Object hilo = nombreHilo;
        if ((NombreHilos.indexOf(hilo)) == -1) {
            existe = false; //El elemento no existe dentro del arrayList
        } else {
            existe = true; //El elemento Si existe dentro del arrayList
        }
        return existe;
    }

    /*
        #Funcion AlmacenaNombres: Creada con el fin de ir llenando la lista que sera el orden en el cual
          se ejecutara cada hilo para la siguiente etapa, es decir, inicializamos la lista y la llenamos.
        #Entrada: nombreHilo, el cual es el ID del hilo en cuesiton
        #Salida: void - no regresa nada
     */
    public void AlmacenaNombres(String nombreHilo) {
        try {
            //Bloqueamos seccion critica
            semaforo.acquire();

            //1) Si la Lista esta vacia la inicializamos
            if (NombreHilos.isEmpty()) {
                if (!HiloEstaEn_ListaEjecucion(nombreHilo)) {//Agregamos el hilo a la lista de ejecucion
                    NombreHilos.add(nombreHilo);
                    PosicionRecorrida++;
                    //System.out.println("Primer elemento: " + nombreHilo + " Posicion recorrida: " + PosicionRecorrida + " \t HilosMaximos: " + MaximoHilos);
                }
                HiloSeleccionado = nombreHilo;
                HilosUsados = 0;
            }

            //2) Si hay elementos por agregar en la ejecucion intermedia se agregan
            if (PosicionRecorrida > 0 && PosicionRecorrida < MaximoHilos - 1) {
                if (!HiloEstaEn_ListaEjecucion(nombreHilo)) {//Agregamos el hilo a la lista de ejecucion
                    NombreHilos.add(nombreHilo);
                    PosicionRecorrida++;
                    //System.out.println("\nElemento anterior: " + NombreHilos.get(PosicionRecorrida - 2));
                    //System.out.println("Elemento Intermedio: " + nombreHilo + " Posicion recorrida: " + PosicionRecorrida + " \t HilosMaximos: " + MaximoHilos);
                }
            }

            //3) Si ya solo es el ultimo elemento volvemos la lista Circular
            if (PosicionRecorrida == MaximoHilos - 1) {
                if (!HiloEstaEn_ListaEjecucion(nombreHilo)) {//Agregamos el hilo a la lista de ejecucion
                    NombreHilos.add(nombreHilo);
                    PosicionRecorrida++;
                    //System.out.println("\nElemento anterior: " + NombreHilos.get(PosicionRecorrida - 2));
                    //System.out.println("Ultimo elemento: " + nombreHilo + " Posicion recorrida: " + PosicionRecorrida + " \t HilosMaximos: " + MaximoHilos);
                    ListaLlena = true;
                    System.out.println("\n\t --- ListaEjecucion Llenada: " + ListaLlena + " ---");
                    //Imprimimos el orden de ejecucion de los hilos
                    System.out.println("Orden de ejecucion: \n");
                    for (String elemento : NombreHilos) {
                        System.out.print(elemento + " ");
                    }
                    System.out.println("\n");
                }
            }

            //Desbloqueamos seccion critica
            semaforo.release();
        } catch (InterruptedException error) {
            System.out.println("Error al llenar la lista de ejecucion: " + error.getMessage());
        }
    }

    /*
        #Funcion BuscaArchivo: Creada con el fin de ir buscando el archivo solicitado pero de manera ordenada
          y con base en el orden que nos genera la lista circular.
        #Entrada: 
            - nombreHilo: ID del hilo en cuesiton
            - nombreArchivo: nombre del archivo que se busca en cuesiton
        #Salida: void - no regresa nada
     */
    public void BuscaArchivo(String nombreHilo, String nombreArchivo) {
        try {
            //Bloqueamos seccion critica
            semaforo.acquire();

            //Creamos ruta para buscar el archivo
            String rutaArchivoTemporal = "";
            rutaArchivoTemporal += "./01/";
            rutaArchivoTemporal += nombreHilo;
            rutaArchivoTemporal += "/";
            rutaArchivoTemporal += nombreArchivo;
            System.out.println("Nodo" + HilosUsados + " - " + nombreHilo + " > " + "Buscando el archivo en:  " + rutaArchivoTemporal);

            //Creamos objeto del tipo archivo y validamos si existe
            File archivito = new File(rutaArchivoTemporal);
            if (archivito.exists()) {//Si el archivo existe entonces paramos el bucle
                System.out.println("\t\t" + nombreHilo + " - " + "Ya lo encontre, ¡PAREN DE BUSCAR! \n");
                RespuestaFinal = " > Path Correcto: " + rutaArchivoTemporal;
                ArchivoEncontrado = true;
                if (HilosUsados != 0) { //Como ya se encontro, se le avisa a los nodos anteriores, pero si el nodo es 0 significa que nadie antes de el hizo la busqueda
                    HiloSeleccionado = NombreHilos.get(HilosUsados - 1);
                }
                TodosEstanRecorridos = true;
            } else {//Si el archivo no se encuentra se sigue un bucle para que pasen todos los hilos
                //System.out.println("Holi Soy el hilo: " + _IdHilo + " y el archivo NO existe :c");

                if (HilosUsados == MaximoHilos - 1) {//Si se va a llegar al final de los hilos disponibles hacemos lo siguiente
                    //Inicializamos HilosUsados en cero otra vez debido a que la lista es circular y el ultimo conecta con el primero
                    HilosUsados = 0;

                    //Le damos la opcion al usuario de realizar la busqueda otra vez
                    int respuestas = JOptionPane.showConfirmDialog(null, "¿Quieres Reiniciar La Busqueda?", "No se encontro el archivo", JOptionPane.YES_NO_OPTION);
                    if (respuestas == JOptionPane.YES_OPTION) {//Reiniciamos la busqueda
                        RespuestaFinal = null;
                        TodosEstanRecorridos = false;
                    } else if (respuestas == JOptionPane.NO_OPTION) {//Salimos del bucle padre
                        RespuestaFinal = " > Mensaje: No se encontro el archivo ";
                        TodosEstanRecorridos = true;
                    }

                } else {//Si aun no llegamos al final seguimos sumando
                    HilosUsados++;
                }

                //Mostramos el recorrido de cada hilo y su respectivo nodo, al igual que apuntamos al siguiente hilo que le toque buscar
                System.out.println("\t\t" + nombreHilo + " - " + "No lo encontre, vas tu: Nodo" + HilosUsados + " - " + NombreHilos.get(HilosUsados) + "\n");
                if (TodosEstanRecorridos && (!ArchivoEncontrado)) {//Si ya se va a salir del bucle y no se encontro el archivo entonces
                    HilosUsados = MaximoHilos - 1;//El apuntador seguire al ultimo elemento
                    HiloSeleccionado = NombreHilos.get(HilosUsados);//Ya que el avisara a los demas nodos de que no se encontro el archivo
                } else {//Caso contrario seguimos con el calculo de redireccionamiento
                    HiloSeleccionado = NombreHilos.get(HilosUsados);
                }

            }

            //Liberamos seccion critica
            semaforo.release();
        } catch (InterruptedException error) {
            System.out.println("Error al llenar la lista de ejecucion: " + error.getMessage());
        }
    }

    /*
        #Funcion Avisa_Encontrado: Creada con el fin de ir avisando a los nodos que ya realizaron una busqueda
          previa del archivo en cuestion, ir de manera regresiva hasta que el primer nodo que inicio la busqueda
          se entere que ya se encontro el archivo.
        #Entrada: 
            - nombreHilo: ID del hilo en cuesiton            
        #Salida: void - no regresa nada
     */
    public void Avisa_Encontrado(String nombreHilo) {
        try {
            //Bloqueamos seccion critica
            semaforo.acquire();

            //Vamos avisando a los nodos anteriores, por lo cual 'HiloSeleccionado' va a apuntar a un nodo atras
            int posicionFinal = HilosUsados - 1;
            if (posicionFinal >= 0) {

                System.out.println("¡EXITO! > Nodo" + HilosUsados + " - " + NombreHilos.get(HilosUsados) + " Aviso a Nodo" + posicionFinal + " - " + NombreHilos.get(posicionFinal) + RespuestaFinal);
                if (HilosUsados != 1) {//Mientras no estemos en el nodo de la primera posicion entonces podemos restar, sino se saldra del rango ya que no existe la posicion -1
                    HilosUsados--; //Si vale 1 esta variable valdra cero
                    HiloSeleccionado = NombreHilos.get(HilosUsados - 1);//Y aqui no existe la posicion -1
                } else {
                    //En ese caso perticular mandamos a que apunte al final de la lista
                    HiloSeleccionado = NombreHilos.get(MaximoHilos - 1);
                    System.out.println("¡EXITO!" + RespuestaFinal);
                    //Como ya vuelve a apuntar al final de la lista entonces ya se aviso a todos los antecesores, entonces podemos salir del bucle
                    TodosEstanAvisados = true;
                }

            } else {//Si la posicionFinal da -1 significa que ya se considero al nodo cero, por lo tanto podemos salir del bucle
                HiloSeleccionado = NombreHilos.get(MaximoHilos - 1);
                TodosEstanAvisados = true;
            }

            //Liberamos seccion critica
            semaforo.release();
        } catch (InterruptedException error) {
            System.out.println("Error al avisar exito obtenido a los nodos: " + error.getMessage());
        }
    }

    /*
        #Funcion Avisa_NO_Encontrado: Creada con el fin de ir avisando a todos los nodos que no se encontro el archivo,
          partiendo desde el ultimo nodo que no logro encontrar el archivo.
        #Entrada:
            - nombreHilo: ID del hilo en cuesiton
        #Salida: void - no regresa nada
     */
    public void Avisa_NO_Encontrado(String nombreHilo) {
        try {
            //Bloqueamos seccion critica
            semaforo.acquire();

            //Vamos avisando a los nodos anteriores, por lo cual 'HiloSeleccionado' va a apuntar a un nodo atras
            int posicionFinal = HilosUsados - 1;
            if (posicionFinal >= 0) {
                
                System.out.println("¡FRACASO! > Nodo" + HilosUsados + " - " + NombreHilos.get(HilosUsados) + " Aviso a Nodo" + posicionFinal + " - " + NombreHilos.get(posicionFinal) + RespuestaFinal);
                if (HilosUsados != 1) {//Mientras no estemos en el nodo de la primera posicion entonces podemos restar, sino se saldra del rango ya que no existe la posicion -1
                    HilosUsados--; //Si vale 1 esta variable valdra cero
                    HiloSeleccionado = NombreHilos.get(HilosUsados - 1);//Y aqui no existe la posicion -1
                } else {
                    //En ese caso perticular mandamos a que apunte al final de la lista
                    HiloSeleccionado = NombreHilos.get(MaximoHilos - 1);
                    System.out.println("¡FRACASO!" + RespuestaFinal);
                    //Como ya vuelve a apuntar al final de la lista entonces ya se aviso a todos los antecesores, entonces podemos salir del bucle
                    TodosEstanAvisados = true;
                }
                
            } else {//Si la posicionFinal da -1 significa que ya se considero al nodo cero, por lo tanto podemos salir del bucle
                HiloSeleccionado = NombreHilos.get(MaximoHilos - 1);
                TodosEstanAvisados = true;
            }

            //Liberamos seccion critica
            semaforo.release();
        } catch (InterruptedException error) {
            System.out.println("Error al avisar exito obtenido a los nodos: " + error.getMessage());
        }
    }

}
