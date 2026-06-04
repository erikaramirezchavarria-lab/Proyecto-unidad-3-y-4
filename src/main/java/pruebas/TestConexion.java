/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pruebas;
import utilerias.ConexionBD;
import java.sql.Connection;

/**
 *
 * @author LISSET ISIDRO SANCHEZ
 */

public class TestConexion {
    public static void main(String[] args) {
        Connection con = ConexionBD.conectar();
        if (con != null) {
            System.out.println("✅ Proyecto conectado correctamente a la base de datos hotel_db.");
        } else {
            System.out.println("❌ No se pudo conectar.");
        }
    }
}

