import java.util.InputMismatchException;
import java.util.Locale;
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

    public static void main(String[] args) {

        menuInicio();

    }

    private static void juego(){
        imprimirTablero();
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
                    System.out.print(" c ");
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
                    System.out.println("hola 1");
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

            if (cantidadPremios>20){
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
            cantidadPremios = entrada.nextInt();

            if (cantidadPremios>20){
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
