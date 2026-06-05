package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import modelos.Reservacion; 
import utilerias.ConexionBD;

public class ReservacionDAO {

    public boolean guardarReservacion(Reservacion r) {
        // Ajustado a los nombres reales de tus columnas
        String sql = "INSERT INTO reservas (id_huesped, id_habitacion, fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?)";
        
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, r.getIdHuesped());
            ps.setInt(2, r.getIdHabitacion());
            ps.setString(3, r.getFechaInicio());
            ps.setString(4, r.getFechaFin());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ ERROR AL GUARDAR RESERVACIÓN: " + e.getMessage());
            return false;
        }
    }
}