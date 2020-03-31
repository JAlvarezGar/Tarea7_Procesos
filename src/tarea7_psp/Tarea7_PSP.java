package tarea7_psp;

import java.security.*;
import javax.crypto.*;
import java.io.*;
import java.util.Arrays;
import javax.swing.JOptionPane;

/**
 *
 * @author jjalv
 */
public class Tarea7_PSP {

    static String cadena = "";
    static int longitudCadena;
    static char letraCadena;

    static SecretKey clave = null;

    public static void main(String[] args)
            throws NoSuchAlgorithmException, NoSuchPaddingException {

        // genero una longitud de cadena aleatoria entre 10 y 20 caracteres
        longitudCadena = (int) (Math.random() * 10 + 10);

        for (int i = 0; i < longitudCadena; i++) {

            int n = (int) (Math.random() * (91 - 65)) + 65;

            letraCadena = (char) n;
            cadena += String.valueOf(letraCadena);
        }

        try {
            // crear fichero

            BufferedWriter bw = new BufferedWriter(new FileWriter("fichero.cifrado", true));
            bw.write(" ");
            bw.write(cadena);
            bw.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
        SecretKey clave = cifrarFichero("fichero.cifrado");

        Cipher cifrado = Cipher.getInstance("AES");

        System.out.println(cadena);

        crearClave();
        almacenarClave();

    }

    private static void crearClave() {

        try {
            String nombreUsuario = JOptionPane.showInputDialog("introduce tu nombre de usuario");
            String passWord = JOptionPane.showInputDialog("introducir password");
            String claveString = nombreUsuario + passWord;
            System.out.println(claveString);

            // CREACION DE LA SEMILLA CON LOS DATOS DE USUARIO
            // SHA-1 Pseudo-Random Number Generation es un algoritmo 
            // de generación de números pseudoaleatorios utilizados por SHA-1.
            // definicion sacada de wikipedia
            SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG");
            int seedByteCount = claveString.length();
            byte[] seed = secureRandomGenerator.generateSeed(seedByteCount);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(claveString.getBytes());

            // GENERO LA CLAVE CON EL ALGORITMO AES
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, secureRandom);
            SecretKey clavePass = kg.generateKey();
            System.out.println("Clave generada con "
                    + "el usuario + password: "
                    + Arrays.toString(clavePass.getEncoded()));
            System.out.println("numero secureRandom: " + secureRandom.nextInt());
          
            //iniciar cifrado
            Cipher cifrado = Cipher.getInstance("Rijndael/ECB/PKCS5Padding");
            // inicio el objeto cifrado en modo Encriptación
            cifrado.init(Cipher.ENCRYPT_MODE, clavePass);
            
            System.out.println("el cifrado de la contraseña es: " + cifrado);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException ex) {
            System.out.println(ex);
        }

    }

    private static SecretKey cifrarFichero(String criptofichero) {

        return null;
    }

    private static void almacenarClave() {
    }

}
