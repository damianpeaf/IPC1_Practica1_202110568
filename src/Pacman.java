import javax.swing.plaf.TreeUI;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Pacman {

    private static Scanner entrada = new Scanner(System.in);
    private static String nombreUsuario = "";
    private static String[][] tablero;
    private static int cantidadPremios = 40;
    private static int cantidadParedes = 20;
    private static int cantidadTrampas = 20;
    private static int puntaje = 0;
    private static  int vidas = 3;
    private static Random posicionesAleatorias = new Random();

    private final static String FANTASMA = "@";
    private final static String PREMIOSIMPLE = "0";
    private final static String PREMIOESPECIAL = "$";
    private final static String PARED = "X";
    private final static String PACMAN = "<";
    private static int posYPacman, posXPacman;

    private static boolean partidaTerminada;
    private static int elementosMaximos = 0;
    private static int elementosRecogidos = 0;

    public static void main(String[] args) {

        menuInicio();

    }

    private static void juego(){
        generarTablero();
        imprimirTablero();
        posicionIncial();

        System.out.println("Que comience el juego !!!");
        imprimirTablero();

        partidaTerminada = false;

        decisionMovimiento();

    }

    private static void decisionMovimiento(){

        if (vidas != 0 && !partidaTerminada && elementosRecogidos<elementosMaximos) {

            imprimirTablero();

            try {
                imprimirMenuMovimiento();
                switch (entrada.next()) {
                    case "8":
                        mover("arriba");
                        break;
                    case "5":
                        mover("abajo");
                        break;
                    case "6":
                        mover("derecha");
                        break;
                    case "4":
                        mover("izquierda");
                        break;
                    case "F":
                        menuPausa();
                        break;
                    default:
                        System.out.println("Ingresa un valor valido");
                        decisionMovimiento();
                }
            } catch (InputMismatchException exception) {
                System.out.println("Ingresa un valor valido");
                entrada.nextLine();
                decisionMovimiento();
            }
        }else{
            //terminar y guarda partida
            System.out.println("Juego terminado");

            //¿Por que termino la partida?
            String razon = "";

            if (vidas == 0) {
                razon = "DERROTA";
            }

            if (partidaTerminada == true) {
                razon = "RENUNCIA";
            }

            if (elementosRecogidos >= elementosMaximos){
                razon = "VICTORIA";
            }

            String registroIndividual = nombreUsuario + " | " + puntaje + " | "+ razon + "\n";

            System.out.println(registroIndividual);
        }

    }


    private static void mover(String tipoMovimiento){

        int posXPacmanAntigua = posXPacman;
        int posYPacmanAntigua = posYPacman;


        tablero[posYPacman][posXPacman] = null;
        //desplazamiento horizontal
        if (tipoMovimiento.equals("derecha")) {
            posXPacman+=1;
        }
        if (tipoMovimiento.equals("izquierda")) {
            posXPacman-=1;
        }


        //desplazamiento vertical
        if (tipoMovimiento.equals("arriba")) {
            posYPacman-=1;
        }
        if (tipoMovimiento.equals("abajo")) {
            posYPacman+=1;
        }

        //validacion reapacer por otro lado
        if (posYPacman >= tablero.length) {
            posYPacman=0;
        }
        if (posYPacman < 0) {
            posYPacman=tablero.length-1;
        }

        if (posXPacman >= tablero[0].length) {
            posXPacman=0;
        }
        if (posXPacman < 0) {
            posXPacman=tablero[0].length-1;
        }

        //mover y colisiones:

        //siempre que haya un objeto, es decir, hay colision
        if (tablero[posYPacman][posXPacman] != null) {

            //toca fantasma
            if (tablero[posYPacman][posXPacman].equals(FANTASMA)){
                vidas--;
                tablero[posYPacman][posXPacman]=PACMAN;
            }

            //toca premio
            if (tablero[posYPacman][posXPacman].equals(PREMIOSIMPLE) || tablero[posYPacman][posXPacman].equals(PREMIOESPECIAL)) {
                if (tablero[posYPacman][posXPacman].equals(PREMIOSIMPLE)) {
                    puntaje+=10;
                }

                if (tablero[posYPacman][posXPacman].equals(PREMIOESPECIAL)) {
                    puntaje+=15;
                }
                elementosRecogidos++;
                tablero[posYPacman][posXPacman]=PACMAN;
            }


            //toca pared
            if (tablero[posYPacman][posXPacman].equals(PARED)) {
                //Lo regresa a la posicion anterior

                posXPacman=posXPacmanAntigua;
                posYPacman=posYPacmanAntigua;
                tablero[posYPacman][posXPacman] = PACMAN;

                System.out.println("No puedes avanzar ahi, hay una pared");
            }

        }else{
            //simplemente mueve al personaje (ya que no hay problema)

            tablero[posYPacman][posXPacman] = PACMAN;
        }

        //regresar a decidir
        decisionMovimiento();
    }

    private static void menuPausa() {
    }

        private static void imprimirMenuMovimiento(){
        System.out.println("| Arriba: 8 | Abajo: 5 | Derecha: 6 | Izquierda: 4 | Menu pausa: F |");
    }

    private static void posicionIncial(){

        try {

            System.out.println("Ingresar la posicion inicla del juego");
            System.out.println("Fila: ");
            int posY = entrada.nextInt() -1;
            System.out.println("Columna: ");
            int posX = entrada.nextInt() -1;

            if (tablero[posY][posX] == null) {
                tablero[posY][posX]=PACMAN;
                posXPacman=posX;
                posYPacman=posY;
            }else{
                System.out.println("Posicion invalida");
                posicionIncial();
            }

        }catch (InputMismatchException exception){
            System.out.println("Ingresa un valor valido");
            entrada.nextLine();
            posicionIncial();
        }


    }

    private static  void generarTablero(){

        generarElemento(cantidadPremios, PREMIOSIMPLE, PREMIOESPECIAL);
        generarElemento(cantidadParedes, PARED, null);
        generarElemento(cantidadTrampas, FANTASMA, null);


    }

    private static void generarElemento(int cantidadElementos, String simboloPrincipal, String simboloEspecial){
        int casillas = tablero.length*tablero[0].length;

        String simbolo;


        //Para generar por lo menos un elemento
        int elementosRepresentados = 1;
        int posiblesElementos = Math.round(casillas*cantidadElementos/100);
        if (posiblesElementos>1) {
            elementosRepresentados = posiblesElementos;
        }

        //para pruebas
        //int elementosRepresentados = 0;


        System.out.println("Cantidad de " + simboloPrincipal + " generados = " + elementosRepresentados);

        if (simboloPrincipal.equals(PREMIOSIMPLE)) {
            elementosMaximos = elementosRepresentados;
        }

        for (int i = 0; i < elementosRepresentados; i++) {
            boolean creado = false;

            //Escoger cual de los 2 simbolos usar (si es que hay signo especial)
            if (simboloEspecial == null) {
                simbolo = simboloPrincipal;
            }else{
                if (posicionesAleatorias.nextInt(3)==0) {
                    simbolo = simboloEspecial;
                }else {
                    simbolo = simboloPrincipal;
                }
            }

            //Genera posiciones hasta que logra insertar el simbolo en alguna
            while (!creado){

                int posY = posicionesAleatorias.nextInt(tablero.length);
                int posX = posicionesAleatorias.nextInt(tablero[0].length);

                if (tablero[posY][posX] == null) {
                    tablero[posY][posX] = simbolo;
                    creado=true;
                }
            }
        }
    }

    private static void imprimirTablero(){

        String encabezado = "";

        for (int j=0; j<tablero[0].length; j++) {
            encabezado+= " " + (j+1) + " ";
        }

        System.out.println("---"+encabezado+"---");

        for (int i=0; i<tablero.length; i++){

            if (i==9){
                System.out.print((i+1) + "|");

            }else{
                System.out.print((i+1) + " |");
            }

            for (int j=0; j<tablero[0].length; j++){

                String caracter = tablero[i][j];

                if (caracter!=null){
                    System.out.print(" " + caracter + " ");
                }else{
                    System.out.print("   ");
                }

            }
            System.out.println("|");
        }

        String pie = "";

        for (int j=0; j<tablero[0].length; j++) {
            pie+= "---";
        }
        System.out.println("---"+pie+"---");

        System.out.println("| Nombre usuario: " +nombreUsuario + " | Puntos: " + puntaje + " | Vidas: "+ vidas+ " |");

    }

    private static void menuInicio(){
        try{

            System.out.println("=== Menu de inicio ===");
            System.out.println("1. Iniciar Juego");
            System.out.println("2. Historial de partidas");
            System.out.println("3. salir");

            switch (entrada.nextInt()){
                case 1:
                    nombreUsuario();
                    break;
                case 2:
                    System.out.println("LISTADO");
                    break;
                case 3:
                    System.out.println("Hasta pronto");
                    System.exit(0);
                default:
                    System.out.println("Ingresa un valor valido");
                    menuInicio();
            }
        }catch (InputMismatchException exception){
            System.out.println("Ingresa un valor valido");
            entrada.nextLine();
            menuInicio();
        }
    }

    private static void nombreUsuario(){
        try{
            System.out.println("¿Nombre de usuario?");
            nombreUsuario = entrada.next();
            dimensionesTablero();
        }catch (InputMismatchException exception){
            System.out.println("Ingresa un valor valido");
            entrada.nextLine();
            nombreUsuario();
        }

    }

    private static void dimensionesTablero() {
        try{
            System.out.println("¿Tipo de tablero?");
            System.out.println("p -> pequeño");
            System.out.println("g -> grande");

            switch (entrada.next().toLowerCase()) {
                case "p":
                    tablero = new String[5][6];
                    cantidadPremios();
                    break;
                case "g":
                    tablero = new String[10][10];
                    cantidadPremios();
                    break;
                default:
                    System.out.println("Ingresa un valor valido");
                    dimensionesTablero();

            }
        }catch (InputMismatchException exception){
            System.out.println("Ingresa un valor valido");
            entrada.nextLine();
            dimensionesTablero();
        }
    }

    private static void cantidadPremios(){
        try {
            System.out.println("¿Cantidad de premios? [0-40]");

            cantidadPremios = entrada.nextInt();

            if (cantidadPremios > 40) {
                cantidadPremios();
            } else {
                cantidadParedes();
            }
        }catch (InputMismatchException exception){
            System.out.println("Ingresa un valor valido");
            entrada.nextLine();
            cantidadPremios();
        }

    }

    private static void cantidadParedes(){
        try {
            System.out.println("¿Cantidad de paredes? [0-20]");
            cantidadParedes = entrada.nextInt();

            if (cantidadParedes>20){
                cantidadParedes();

            }else{
                cantidadTrampas();

            }
        }catch (InputMismatchException exception){
            System.out.println("Ingresa un valor valido");
            entrada.nextLine();
            cantidadParedes();
        }
    }

    private static void cantidadTrampas(){
        try {
            System.out.println("¿Cantidad de trampas? [0-20]");
            cantidadTrampas = entrada.nextInt();

            if (cantidadTrampas>20){
                cantidadTrampas();
            }else {

                juego();
            }
        }catch (InputMismatchException exception){
            System.out.println("Ingresa un valor valido");
            entrada.nextLine();
            cantidadTrampas();
        }

    }


}
