package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types; // Importante para manejar valores NULL
import java.util.ArrayList;
import java.util.List;
import modelos.Habitacion;
import utilerias.ConexionBD;

/**
 * @author Gilberto
 */
public class HabitacionDAO {

    // Método para traer todas las habitaciones a la tabla de la interfaz
    public List<Habitacion> listarHabitaciones() {
        List<Habitacion> lista = new ArrayList<>();
        String sql = "SELECT id_habitacion, numero, tipo, precio, estado, foto FROM habitaciones";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Habitacion h = new Habitacion();
                h.setIdHabitacion(rs.getInt("id_habitacion"));
                h.setNumero(rs.getString("numero"));
                h.setTipo(rs.getString("tipo"));
                h.setPrecio(rs.getDouble("precio"));
                h.setEstado(rs.getString("estado"));
                h.setFoto(rs.getBytes("foto"));
                lista.add(h);
            }
        } catch (SQLException e) {
            System.err.println("Error en listarHabitaciones: " + e.getMessage());
        }
        return lista;
    }

    // Método para registrar un nuevo cuarto
    public boolean registrarHabitacion(Habitacion h) {
        String sql = "INSERT INTO habitaciones (numero, tipo, precio, estado, foto) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, h.getNumero());
            ps.setString(2, h.getTipo());
            ps.setDouble(3, h.getPrecio());
            ps.setString(4, h.getEstado());
            
            // CORRECCIÓN: Si la foto es null, enviamos un valor NULL de SQL correctamente
            if (h.getFoto() != null) {
                ps.setBytes(5, h.getFoto());
            } else {
                ps.setNull(5, Types.LONGVARBINARY);
            }
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ ERROR AL GUARDAR HABITACION: " + e.getMessage());
            e.printStackTrace(); 
            return false;
        }
    }

    // NUEVO MÉTODO: Para cambiar el estado cuando se haga una reserva
    public void actualizarEstadoHabitacion(String numero, String nuevoEstado) {
        String sql = "UPDATE habitaciones SET estado = ? WHERE numero = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setString(2, numero);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ ERROR AL ACTUALIZAR ESTADO: " + e.getMessage());
        }
    }
}