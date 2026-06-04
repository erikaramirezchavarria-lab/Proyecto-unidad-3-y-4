package utilerias;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encriptador {

    /**
     * Convierte una contraseña en texto plano a un hash SHA-256 de 64 caracteres hexadecimales.
     * Coincide exactamente con la función SHA2('texto', 256) de MySQL.
     */
    public static String encriptarSHA256(String passwordPlana) {
        if (passwordPlana == null) {
            return null;
        }
        try {
            // Instanciamos el algoritmo SHA-256 estándar de Java
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // Obtenemos los bytes del texto usando UTF-8
            byte[] hashBytes = digest.digest(passwordPlana.getBytes(StandardCharsets.UTF_8));
            
            // Convertimos los bytes a formato Hexadecimal (64 caracteres)
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0'); // Mantiene el formato de dos dígitos por byte
                }
                hexString.append(hex);
            }
            
            return hexString.toString(); // Retorna la cadena de 64 caracteres
            
        } catch (NoSuchAlgorithmException e) {
            // Error crítico si el sistema operativo o entorno no soporta SHA-256
            System.err.println("Error al encriptar: Algoritmo no encontrado. " + e.getMessage());
            return null;
        }
    }
}