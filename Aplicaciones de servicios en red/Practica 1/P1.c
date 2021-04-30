/*
	---------- Instituto Politecnico Nacional ----------
	------------ Escuela Superior de Computo -----------
	------ Aplicaciones para comunicaciones de red -----

	#Practica 1: Algoritmo Bucketsort
	#Autor: Munoz Primero Elias

	#Descripcion: Programa realizado en lenguaje C ANSI, 
	Consiste en crear 'n' hilos (canastas/buckets) las cuales 
	Ordenaran numeros aleatorios previamente calculados	a traves
	de diferentes rangos los cuales tambien son calculados.
	Se ordenaran internamente cada canasta y al final todos
	los hilos pondran de manera ordenada los resultados en un
	archivo de texto, en este caso llamado "Resultado.txt"

	#Ejecucion: gcc P1.c -o P1 -lpthread
*/
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <semaphore.h>
#define RESET_COLOR    "\x1b[0m"
#define ROJO_T     "\x1b[31m"
#define AMARILLO_T     "\x1b[33m"

/* Proponemos una estructura para cada hilo */
typedef struct Bucket{
	long unsigned idBucket;
	int start;
	int finish;
	int range;
	int elements;
} Bucket_t;

/* Prototipos del programa */
void Bienvenida();
void AdminHilos();
void ValidaEjecucion(char *argc);
void *AccionCanasta(void *idSer);
void sortNumb(Bucket_t *Canasta);

/* Se implementa el uso de semaforos */
static sem_t SemServidores;
/* Variables globales que seran nuestras secciones criticas */
static int Numbers[3500], NumServidores, Rango, Counter;

/*
	#Main: Funcion principal del programa, se inicia 
	validando que el formato para llamar al programa sea correcto,
	despues, se elimina el archivo resultante para no genenerar
	conflictos, seguido se inicializa nuestro semaforo y se calculan
	los numeros de servidores (hilos) a utilizar y el rango de 
	numeros que pueden aceptar, por ultimo se da la bienvenida al
	programa y se mandan a llamar a los hilos.
*/
int main(char argc, char *argv[]){
	ValidaEjecucion(&argc);
	remove("Resultado.txt");
	sem_init(&SemServidores,0,1);
	NumServidores = atoi(argv[1]);
	Rango = 1000/NumServidores; // Numeros del 0 al 999
	Bienvenida();
	AdminHilos();	
}

/*
	#ValidaEjecucion: Funcion pensada para que el usuario conozca el
	formato para ejecutar el programa.
	#Entradas:
		-char *argc: caracter que indica el numero de instrucciones
		ingresadas mediante el shell para ejecutar el programa.
	#Salidas:
		Funcion void no devuelve nada.
*/
void ValidaEjecucion(char *argc){
	if(*argc != 2){
		printf("\n \t ------------ Error en los argumentos ------------ \n");
		printf(" \t ------ Formato Correcto:  ./a  NumeroHilos ------\n\n");
		/* Formato Correcto: argv[0] = ./a    argv[1] = NumeroHilos */
		exit(0);
	}
}

/*
	#Bienvenida: Funcion pensada para hacer agradable la ejecucion
	del programa, tambien es en donde se calculan los numeros
	pseudo aleatorios.
	#Entradas:
		No tiene
	#Salidas:
		Funcion void no devuelve nada.
*/
void Bienvenida(){	
	if(NumServidores < 1){
		printf("\n\t *** El numero de hilos debe de ser mayor a cero ***");
		printf("\n\t\t *** HAZ MATADO A MI PROGRAMA :c *** \n\n");
		exit(0);
	}
	printf("\n\t ---- Practica 1: Algoritmo BucketSort ----");
	printf(AMARILLO_T"\n\t ------ Alumno: Munoz Primero Elias -------"RESET_COLOR);
	printf("\n\t ------------ Hilos a crear: %d ------------",NumServidores);
	printf("\n\t ------------ Tamanio Rango: %d ----------- \n\n",Rango);

	/* Llenamos la seccion critica con los numeros pseudo aleatorios */
	srand(time(NULL));
	for(int i=0; i<3500;i++)
		Numbers[i] = rand() % (1000); //Numeros del 0 al 999

}

/*
	#AdminHilos: Funcion pensada para crear los hilos dependientes 
	que serviran como canastas (buckets) para ordenar numeros 
	con base en un rango calculado.
	#Entradas:
		No tiene
	#Salidas:
		Funcion void no devuelve nada.
*/
void AdminHilos(){	
	int Recibe_Estado_Hilo;

	pthread_t Canastas[NumServidores];	

	for(int i=0;i<(NumServidores);i++)	
		if( Recibe_Estado_Hilo = pthread_create(&Canastas[i],NULL,AccionCanasta,(void*)&i) ) //Sirve para verificar que todo este bien con los hilos, sino, termina el programa
			exit(Recibe_Estado_Hilo);

	for(int i=0;i<(NumServidores);i++)
		pthread_join(Canastas[i],NULL);
}

/*
	#AccionCanasta: Funcion pensada agregar funcionalidad a los hilos 
	asignandoles una estructura con sus identificadores y rangos correspondientes
	#Entradas:
		- void *idSer: Identificador del hilo
	#Salidas:
		Funcion void no devuelve nada.
*/
void *AccionCanasta(void *idSer){
	/* Uso una seccion critica para que los hilos se creen correctamente en funcion de counter*/
	sem_wait(&SemServidores);
		Counter++;
		Bucket_t Canasta;
		Canasta.idBucket = pthread_self();
		Canasta.range = Counter;
		Canasta.elements=0;
		if(Counter == 1){/*Caso 1 se inicializa en cero*/
			Canasta.start = 0;
			Canasta.finish = Counter*Rango - 1;
		}else if(Counter == NumServidores){/*Caso 2 obligo que termine en 999 por si el rango se come decimales y reinicio counter*/
			Canasta.start = (Counter-1)*Rango;
			Canasta.finish = 999;
			Counter=0;
		}else{/*Caso 3 patrones intermedios*/
			Canasta.start = (Counter-1)*Rango;
			Canasta.finish = Counter*Rango - 1;
		}					
		sortNumb(&Canasta);
	sem_post(&SemServidores);		
}

/*
	#sortNumb: Funcion que sirve para ordenar las canastas de manera interna
	a traves de un algoritmo de inserccion.
	#Entradas:
		- Bucket_t *Canasta: Estructura de cada canasta la cual contiene
		todo lo relacionado a cada una. Sus datos son mostrados en pantalla
	#Salidas:
		Funcion void no devuelve nada.
*/
void sortNumb(Bucket_t *Canasta){
	int a[3500];

	/* Llemanos nuestra canasca con el rango establecido */
	for(int i=0,j=0;i<3500;i++){
		if( (Numbers[i] >= Canasta->start) && (Numbers[i] <= Canasta->finish) ){
			a[j]=Numbers[i];
			j++;
		}
		if(i==3499)
			Canasta->elements=j;
	}	

	/* Ordenamiento Por Inserccion */
	int i,pos,aux;
	for(i=0;i<Canasta->elements;i++){
		pos = i;
		aux = a[i];
		while((pos>0)&&(aux < a[pos-1])){
			a[pos] = a[pos-1];
			pos--;
		}
		a[pos] = aux;
	}

	/* Llenado a un archivo */ 
	FILE *archivo;
	archivo = fopen("Resultado.txt","a");
	for(i=0;i<Canasta->elements;i++)
		fprintf(archivo," %d ",a[i]);
	fclose(archivo);

	/* Descripcion de cada Hilo*/
	printf("\n Hi Everyone :) I am the bucket: %lu ",Canasta->idBucket);
	printf("\n Start: %d ",Canasta->start);
	printf("\n Finish: %d ",Canasta->finish);
	printf("\n Range: %d ",Canasta->range);
	printf("\n Elements: "ROJO_T"%d"RESET_COLOR" \n\n",Canasta->elements);
}