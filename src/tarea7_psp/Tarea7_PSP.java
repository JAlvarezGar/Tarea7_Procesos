package tarea7_psp;

/**
 *
 * @author jjalv
 */
public class Tarea7_PSP {

    static String cadena = "";
    static int longitudCadena;
    static char letraCadena;

    public static void main(String[] args) {

        // genero una longitud de cadena aleatoria entre 10 y 20 caracteres
        longitudCadena = (int) (Math.random() * 10 + 10);

        for (int i = 0; i < longitudCadena; i++) {

            int n = (int) (Math.random() * (91 - 65)) + 65;

            letraCadena = (char) n;
            cadena += String.valueOf(letraCadena);
        }
        System.out.println(cadena);
    }

}
