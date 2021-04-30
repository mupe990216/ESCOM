package Modelo;

public class Hilos extends Thread {

    private String _NombreArchivo;
    SeccionCritica _ObjetoCritico;

    /*
        #Constructor del objeto de la clase Hilos.
        Se utilizan tres parametros: 
        #IdHilo: es el ID de cada hilo y se hace llamada de su respectivo constructor de la
          clase 'Thread' la cual heradamos y a traves del metodo 'super()' inicializamos su nombre
          de tal forma que tenga la nomenclatura que queremos, en este caso seria ('hilo'+i).
        #NombreArchivo: cadena de texto que tendra el nombre del archivo que vamos a buscar.
        #objetoCritico: Objeto de la clase SeccionCritica, como el nombre lo indica, sera el objeto
          que contiene todos los elementos por los cuales los hilos competiran por modificar, asi mismo
          se hace el paso por referencia ya que todos los hilos tendran el mismo objetoCritico debido
          a que en el main solamente se inicializa un unico objetoCritico.
     */
    public Hilos(int IdHilo, String NombreArchivo, SeccionCritica objetoCritico) {
        super("hilo" + IdHilo);
        this._NombreArchivo = NombreArchivo;
        this._ObjetoCritico = objetoCritico;
    }

    /*
        #Metodo Run(): Este metodo se hereda de la clase 'Thread', se tiene que sobreescribir para 
          utilizarlo en este problema, debido a que este metodo indica como se comportaran TODOS
          los hilos u objetos instanciados desde esta claso 'Hilos', es decir, todos los objetos 
          del tipo 'Hilo' al momento de ser ejecutados (invocados con el metodo start en el main)
          se comportaran como lo indica el metodo run() que sobreescribimos.
        - Nota: Como se sobreescribe, antes de la declaracion del metodo se coloca la instruccion '@Override' 
            para que java entienda que este metodo run() sera diferente unicamente para los objetos
            del tipo 'Hilo'.
     */
    @Override
    public void run() {
        //System.out.println(getName()+" > "+"Primera Etapa iniciada - Llenar Lista de Orden de Ejecucion");
        //1) Primera etapa del problema: Llenar la lista y con ello saber el orden de ejecucion de los hilos
        while (!_ObjetoCritico.ListaLlena) {
            try {
                _ObjetoCritico.AlmacenaNombres(getName());
                sleep(1500);
            } catch (InterruptedException e) {
                System.out.println("Error al llenar la lista de ejecucion circular: " + e.getMessage());
            }
        }
        //System.out.println(getName()+" > "+"Segunda Etapa iniciada - Buscar Archivo");
        //2) Segunda etapa del problema: Hacer que cada hilo creado busque el archivo
        while (!_ObjetoCritico.TodosEstanRecorridos) {
            try {
                if (_ObjetoCritico.HiloSeleccionado.equals(getName())) {
                    //System.out.println(getName()+" > "+"Hora de buscar papa 7w7");
                    _ObjetoCritico.BuscaArchivo(getName(), _NombreArchivo);
                    sleep(500);
                } else {
                    //System.out.println(getName()+" > "+"No es mi turno me voy a dormir");
                    sleep(1000);
                }
            } catch (InterruptedException e) {
                System.out.println("Error al buscar el archivo: " + e.getMessage());
            }
        }
        //System.out.println(getName()+" > "+"Tercera Etapa iniciada - Informamos a todos los hilos el resultado");
        //3) Tercera etapa del problema: Si el archivo se encuentra, avisar solo a los hilos anteriores; si el archivo no se encuentra, avisar a todos los hilos anteriores y asegurar que todos se recorran        
        if (_ObjetoCritico.ArchivoEncontrado) {
            while (!_ObjetoCritico.TodosEstanAvisados) {//Archivo Encontrado
                //System.out.println("El archivo fue encontrado");
                try {
                    if (_ObjetoCritico.HiloSeleccionado.equals(getName())) {
                        _ObjetoCritico.Avisa_Encontrado(getName());
                        sleep(500);
                    } else {
                        sleep(1000);
                    }
                } catch (InterruptedException e) {
                    System.out.println("Error al avisar que el archivo fue encontrado: " + e.getMessage());
                }
            }
        } else {
            while (!_ObjetoCritico.TodosEstanAvisados) {//Archivo NO Encontrado
                //System.out.println("El archivo NO fue encontrado");
                try {
                    if (_ObjetoCritico.HiloSeleccionado.equals(getName())) {
                        _ObjetoCritico.Avisa_NO_Encontrado(getName());
                        sleep(500);
                    } else {
                        sleep(1000);
                    }
                } catch (InterruptedException e) {
                    System.out.println("Error al avisar que el archivo fue encontrado: " + e.getMessage());
                }
            }
        }

        //System.out.println(getName() + " Terminado Correctamente");

    }

}
