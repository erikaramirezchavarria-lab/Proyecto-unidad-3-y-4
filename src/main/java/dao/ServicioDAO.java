package dao;

import java.sql.*;
import utilerias.ConexionBD;
import modelos.Servicio;

public class ServicioDAO {
    public boolean guardarServicio(Servicio s) {
        String sql = "INSERT INTO servicios (nombre, descripcion, precio) VALUES (?, ?, ?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getNombre());
            ps.setString(2, s.getDescripcion());
            ps.setDouble(3, s.getPrecio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}