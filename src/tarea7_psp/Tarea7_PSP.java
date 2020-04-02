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
    static SecretKey clavePass = null;
    static SecretKey clave = null;

    public static void main(String[] args){

        // GENERO UN FICHERO DE TEXTO DE CONTENIDO Y LONGITUD ALEATORIA
        // ENTRE 10 Y 20 CARACTERES
        longitudCadena = (int) (Math.random() * 10 + 10);

        for (int i = 0; i < longitudCadena; i++) {

            int n = (int) (Math.random() * (91 - 65)) + 65;
            letraCadena = (char) n;
            cadena += String.valueOf(letraCadena);
        }

        try {

            // CREO EL FICHERO DE TEXTO
            BufferedWriter bw = new BufferedWriter(new FileWriter("fichero.texto"));
            bw.write(" ");
            bw.write(cadena);
            bw.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }

        System.out.println("La cedena aleatoria formada y guardada: "+cadena+"\n");

        clave = crearClave();
        descifrarClave(clave);

    }

    private static SecretKey crearClave() {

        try {
            String nombreUsuario = JOptionPane.showInputDialog("introduce tu nombre de usuario");
            String passWord = JOptionPane.showInputDialog("introducir password");
            String claveString = nombreUsuario + passWord;
            System.out.println("La clve formada por usuario+pass: "+claveString+"\n");

            // CREACION DE LA SEMILLA CON LOS DATOS DE USUARIO
            // SHA-1 Pseudo-Random Number Generation es un algoritmo 
            // de generación de números pseudoaleatorios utilizados por SHA-1.
            // definicion sacada de wikipedia
            SecureRandom secureRandomGenerator = null;
            try {
                secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG");
            } catch (NoSuchAlgorithmException ex) {
                System.out.println(ex);
            }
            int seedByteCount = claveString.length();
            byte[] seed = secureRandomGenerator.generateSeed(seedByteCount);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(seed);

            // GENERO LA CLAVE CON EL ALGORITMO AES
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, secureRandom);
            SecretKey clavePass = kg.generateKey();
            System.out.println("Clave generada con "
                    + "el usuario + password: "
                    + Arrays.toString(clavePass.getEncoded()));
            System.out.println("\nSEMILLA secureRandom: " + secureRandom.nextInt());
            cifrado(clavePass);
            clave = clavePass;

        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex);
        }
        return clave;
    }

    private static void cifrado(SecretKey clavePass) {

        try {
            //iniciar cifrado
            Cipher cifrado = Cipher.getInstance("Rijndael/ECB/PKCS5Padding");
            // inicio el objeto cifrado en modo Encriptación
            cifrado.init(Cipher.ENCRYPT_MODE, clavePass);
            byte[] buffer = new byte[1000];
            byte[] bufferCifrado = null;

            // CREO EL FICHERO EN EL QUE SE
            FileInputStream leerFichero = new FileInputStream("fichero.texto");
            FileOutputStream grabarFichero = new FileOutputStream("fichero.cifrado");

//            BufferedReader br = new BufferedReader(new FileReader("fichero.descifrado"));
            int bytesLeidos = leerFichero.read(buffer, 0, 1000);
            while (bytesLeidos != -1) {

                bufferCifrado = cifrado.update(buffer, 0, bytesLeidos);
                grabarFichero.write(bufferCifrado);
                bytesLeidos = leerFichero.read(buffer, 0, 1000);
            }

            bufferCifrado = cifrado.doFinal();
            System.out.println("\nEl fichero cifrado es: "+new String(bufferCifrado,"UTF-8"));
            grabarFichero.write(bufferCifrado);
            grabarFichero.close();
            leerFichero.close();

        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | IOException
                | IllegalBlockSizeException | BadPaddingException ex) {
            System.out.println(ex);
        }
    }

    private static void descifrarClave(SecretKey clave) {

        try {
            //iniciar cifrado
            Cipher desCifrado = Cipher.getInstance("Rijndael/ECB/PKCS5Padding");
            // inicio el objeto cifrado en modo Encriptación
            desCifrado.init(Cipher.DECRYPT_MODE, clave);

            // CREO EL FICHERO EN EL QUE SE 
            FileInputStream ficheroEntrada = new FileInputStream("fichero.cifrado");
            FileOutputStream ficheroSalida = new FileOutputStream("fichero.descifrado");
            byte[] buffer = new byte[1000];
            byte[] bufferDescifrado;
            // LEER EL FICHERO CIFRADO DE 1K EN 1K Y ESTOS FRAGMENTOS LOS DECIFRA
            int bytesLeidos = ficheroEntrada.read(buffer, 0, 1000);

            while (bytesLeidos != -1) {
                // PASA TEXTO CIFRADO AL DESCIFRADOR
                // Y LO ASIGNA bufferDescifrado
                bufferDescifrado = desCifrado.update(buffer, 0, bytesLeidos);
                
                ficheroSalida.write(bufferDescifrado);
                bytesLeidos = ficheroEntrada.read(buffer, 0, 1000);
            }
            
            bufferDescifrado = desCifrado.doFinal();
            System.out.println("\nEl fichero descifrado es: "+new String(bufferDescifrado,"UTF-8"));
            ficheroSalida.write(bufferDescifrado);
//            System.out.println("el fichero descifrado es : " + bytesLeidos);
            ficheroSalida.close();
            ficheroEntrada.close();
            

        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | IOException
                | IllegalBlockSizeException | BadPaddingException ex) {
            System.out.println(ex);
        }
    }
}
