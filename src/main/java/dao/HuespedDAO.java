package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelos.Huesped;
import utilerias.ConexionBD;

/**
 * @author Gilberto
 */
public class HuespedDAO {

    // 1. LISTAR TODOS LOS HUÉSPEDES
    public List<Huesped> listarHuespedes() {
        List<Huesped> lista = new ArrayList<>();
        String sql = "SELECT id_huesped, nombre, telefono, email, foto FROM huespedes";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Huesped h = new Huesped();
                h.setIdHuesped(rs.getInt("id_huesped"));
                h.setNombre(rs.getString("nombre"));
                h.setTelefono(rs.getString("telefono"));
                h.setEmail(rs.getString("email"));
                h.setFoto(rs.getBytes("foto"));
                lista.add(h);
            }
        } catch (SQLException e) {
            System.err.println("Error en listarHuespedes: " + e.getMessage());
        }
        return lista;
    }

    // 2. REGISTRAR UN NUEVO HUÉSPED
    public boolean registrarHuesped(Huesped huesped) {
        String sql = "INSERT INTO huespedes (nombre, telefono, email, foto) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, huesped.getNombre());
            ps.setString(2, huesped.getTelefono());
            ps.setString(3, huesped.getEmail());
            ps.setBytes(4, huesped.getFoto()); // Puede ir null si no suben foto

            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("Error en registrarHuesped: " + e.getMessage());
            return false;
        }
    }
}