package dao;

import java.sql.*;
import utilerias.ConexionBD;
import modelos.Servicio;

public class ServicioDAO {
    public boolean guardarServicio(Servicio s) {
        String sql = "INSERT INTO servicios (id_reserva, nombre, descripcion, precio) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, s.getIdReserva());
            ps.setString(2, s.getNombre());
            ps.setString(3, s.getDescripcion());
            ps.setDouble(4, s.getPrecio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
