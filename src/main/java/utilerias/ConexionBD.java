/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilerias;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    
    // 🔥 CORRECCIÓN 1: Cambiado "Hotel" por "hotel_db" que es el nombre real en tu script de MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/hotel_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    
    // 🔥 CORRECCIÓN 2: Como usas XAMPP, la contraseña va COMPLETAMENTE VACÍA ("")
    private static final String PASSWORD = ""; 

    // Método para obtener conexión
    public static Connection conectar() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // cargar driver
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conexión exitosa a hotel_db");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver JDBC no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con MySQL: " + e.getMessage());
        }
        return con;
    }
}