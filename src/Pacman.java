import javax.swing.plaf.TreeUI;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Pacman {

    //leer las entradas del usuario durante toda la ejucion
    private static Scanner entrada = new Scanner(System.in);

    //objeto para generar numeros aleatorios
    private static Random posicionesAleatorias = new Random();

    //tablero y posicion de pacman
    private static String[][] tablero;
    private static int posYPacman, posXPacman;

    //datos de la partida
    private static int cantidadPremios = 40;
    private static int cantidadParedes = 20;
    private static int cantidadTrampas = 20;
    private static boolean partidaTerminada;
    private static int elementosMaximos = 0;
    private static int elementosRecogidos = 0;

    //datos del usuario
    private static String nombreUsuario = "";
    private static int puntaje = 0;
    private static  int vidas = 3;

    //Simbolos de los items
    private final static String FANTASMA = "@";
    private final static String PREMIOSIMPLE = "0";
    private final static String PREMIOESPECIAL = "$";
    private final static String PARED = "X";
    private final static String PACMAN = "<";

    //Historial de partidas
    private static String registroGeneral= "";

    /*
    //variables ansi de escape (para colores) -> no sirve en CMD
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RESET = "\u001B[0m";
    */
    public static final String ANSI_RED = "";
    public static final String ANSI_GREEN = "";
    public static final String ANSI_YELLOW = "";
    public static final String ANSI_BLUE = "";
    public static final String ANSI_RESET = "";

    public static void main(String[] args) {
        menuInicio();
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
                    historialPartidas();
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

    private static void historialPartidas(){
        //crear arreglo para manejar el registro

        if (!registroGeneral.isEmpty()){
            String[] datosPartidas = registroGeneral.split(" | ");

            int ContadorAux = 1;

            System.out.println(ANSI_RED+"No. - "+ANSI_BLUE+"Usuario - " + ANSI_RED+"Punteo - " + ANSI_YELLOW+"Estado");

            for (int i = 0; i < datosPartidas.length; i+=6) {
                System.out.println(ANSI_RED+ ContadorAux+" - "+ANSI_BLUE+ datosPartidas[i] + " - " + ANSI_RED+ datosPartidas[i+2]+" - " + ANSI_YELLOW+datosPartidas[i+4] + ANSI_RESET);
                ContadorAux++;
            }
        }else{
            System.out.println("No hay registros");
        }

        boolean volverAlMenu = false;

        entrada.nextLine();
        String texto = entrada.nextLine();

        if (texto.isEmpty()) {
            volverAlMenu = true;
            System.out.println(texto);
        }

        if (volverAlMenu) {
            menuInicio();
        }else{
            historialPartidas();
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
            System.out.println("¿Cantidad de premios? [1-40]");

            cantidadPremios = entrada.nextInt();

            if (cantidadPremios > 40 || cantidadPremios<1) {
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
            System.out.println("¿Cantidad de paredes? [1-20]");
            cantidadParedes = entrada.nextInt();

            if (cantidadParedes>20 || cantidadParedes<1){
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
            System.out.println("¿Cantidad de trampas? [1-20]");
            cantidadTrampas = entrada.nextInt();

            if (cantidadTrampas>20 || cantidadTrampas<1){
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


    private static void juego(){
        generarTablero();
        imprimirTablero();
        posicionIncial();

        System.out.println("Que comience el juego !!!");
        partidaTerminada = false;
        decisionMovimiento();

    }


    private static  void generarTablero(){

        //renicia las variables por si hubo una partida antes
        elementosRecogidos=0;
        puntaje=0;
        vidas=3;

        //genera los items/elementos del tablero
        generarElemento(cantidadPremios, PREMIOSIMPLE, PREMIOESPECIAL);
        generarElemento(cantidadParedes, PARED, null);
        generarElemento(cantidadTrampas, FANTASMA, null);

    }

    private static void generarElemento(int cantidadElementos, String simboloPrincipal, String simboloEspecial){

        //total de casillas en el tablero
        int casillas = tablero.length*tablero[0].length;

        //Simbolo que se mostrara en el tablero
        String simbolo;

        //Para generar por lo menos un elemento o el porcentaje indicado al principio
        int elementosRepresentados = 1;
        int posiblesElementos = Math.round(casillas*cantidadElementos/100);
        if (posiblesElementos>1) {
            elementosRepresentados = posiblesElementos;
        }

        //para pruebas de movimiento
        //int elementosRepresentados = 0;


        System.out.println("Cantidad de " + simboloPrincipal + " generados = " + elementosRepresentados);

        //define la cantidad de premios maximos que recoger (para saber si ganó)
        if (simboloPrincipal.equals(PREMIOSIMPLE)) {
            elementosMaximos = elementosRepresentados;
        }

        //genera n elementos
        for (int i = 0; i < elementosRepresentados; i++) {
            boolean creado = false;

            //Escoger cual de los 2 simbolos usar (si es que hay signo especial)
            if (simboloEspecial == null) {
                simbolo = simboloPrincipal;
            }else{
                //si hay 2 (premios) genera uno especial o simple de manera aleatoria
                if (posicionesAleatorias.nextInt(3)==0) {
                    simbolo = simboloEspecial;
                }else {
                    simbolo = simboloPrincipal;
                }
            }

            //Genera posiciones en el tablero HASTA que logra insertar el simbolo en alguna
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

        //Encabezado (numero para las columnas)
        String encabezado = "";

        for (int j=0; j<tablero[0].length; j++) {
            encabezado+= " " + (j+1) + " ";
        }

        System.out.println("---"+encabezado+"---");

        for (int i=0; i<tablero.length; i++){
            //Si se escoge el tablero grande (10 filas) quita un espacio para cuadrar la ultima fila
            if (i>=9){
                System.out.print((i+1) + "|");

            }else{
                System.out.print((i+1) + " |");
            }

            for (int j=0; j<tablero[0].length; j++){

                //verifica que esta almacenado en el tablero
                String caracter = tablero[i][j];

                if (caracter!=null){
                    String color = "";

                    //cambiar color de los elementos
                    if (tablero[i][j].equals(FANTASMA)) {
                        color = ANSI_RED;
                    }else if (tablero[i][j].equals(PREMIOSIMPLE) || tablero[i][j].equals(PREMIOESPECIAL)){
                        color = ANSI_GREEN;
                    }else if (tablero[i][j].equals(PARED)) {
                        color = ANSI_BLUE;
                    }else if (tablero[i][j].equals(PACMAN)) {
                        color = ANSI_YELLOW;
                    }

                    //imprime el elemento
                    System.out.print( color +" " + caracter + " "+ ANSI_RESET);
                }else{
                    //si no hay nada coloca un espacio en blanco
                    System.out.print("   ");
                }

            }
            System.out.println("|");
        }

        //ciera el tablero

        String pie = "";

        for (int j=0; j<tablero[0].length; j++) {
            pie+= "---";
        }
        System.out.println("---"+pie+"---");

        //muestra los datos de la partida
        System.out.println("| Nombre usuario: " +nombreUsuario + " | Puntos: " + puntaje + " | Vidas: "+ vidas+ " |");

    }


    private static void posicionIncial(){

        try {

            System.out.println("Ingresar la posicion inicla del juego");
            System.out.println("Fila: ");
            int posY = entrada.nextInt() -1;
            System.out.println("Columna: ");
            int posX = entrada.nextInt() -1;

            //siempre que no haya nada en el donde se vaya a insertar a pacman
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


    private static void decisionMovimiento(){

        //3 condiciones para dar por finalizada una partida
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
                    case "f":
                        menuPausa();
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

            //Para guardar en el registro general
            String registroIndividual = nombreUsuario + " | " + puntaje + " | "+ razon + " | ";

            System.out.println(registroIndividual);

            registroGeneral = registroIndividual + registroGeneral;

            menuInicio();
        }

    }

    private static void menuPausa() {
        try {
            System.out.println("=== Menu de Pausa ===");
            System.out.println("1. Regresar");
            System.out.println("3. Terminar partida");
            switch (entrada.nextInt()) {
                case 1:
                    decisionMovimiento();
                    break;
                case 3:
                    partidaTerminada=true;
                    decisionMovimiento();
                    break;
                default:
                    menuPausa();
                    break;
            }

        } catch (InputMismatchException exception) {
            System.out.println("Ingresa un valor valido");
            entrada.nextLine();
            decisionMovimiento();
        }
    }

    private static void imprimirMenuMovimiento(){
        System.out.println("| Arriba: 8 | Abajo: 5 | Derecha: 6 | Izquierda: 4 | Menu pausa: f |");
    }


    private static void mover(String tipoMovimiento){

        //guarda la posicion anterior de pacman
        int posXPacmanAntigua = posXPacman;
        int posYPacmanAntigua = posYPacman;

        //borra al pacman de su posicion inicial

        tablero[posYPacman][posXPacman] = null;

        //Para desplazamiento horizontal
        if (tipoMovimiento.equals("derecha")) {
            posXPacman+=1;
        }
        if (tipoMovimiento.equals("izquierda")) {
            posXPacman-=1;
        }


        //Para desplazamiento vertical
        if (tipoMovimiento.equals("arriba")) {
            posYPacman-=1;
        }
        if (tipoMovimiento.equals("abajo")) {
            posYPacman+=1;
        }

        //validacion para reapacer por otro lado
        //vertical
        if (posYPacman >= tablero.length) {
            posYPacman=0;
        }
        if (posYPacman < 0) {
            posYPacman=tablero.length-1;
        }

        //horizontal
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
            //simplemente mueve al personaje (ya que no hay nada en esa casilla)

            tablero[posYPacman][posXPacman] = PACMAN;
        }

        //regresar a decidir
        decisionMovimiento();
    }

}
