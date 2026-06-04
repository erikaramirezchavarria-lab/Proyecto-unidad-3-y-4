package dao;

import java.sql.*;
import utilerias.ConexionBD;
import modelos.Pago;

public class PagoDAO {
    public boolean registrarPago(Pago p) {
        // Ajustado a tu tabla: id_reserva, id_huesped, monto, metodo_pago
        String sql = "INSERT INTO pagos (id_reserva, id_huesped, monto, metodo_pago) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, p.getIdReserva());
            ps.setInt(2, p.getIdHuesped());
            ps.setDouble(3, p.getMonto());
            // Convertimos a minúsculas porque tu ENUM usa 'efectivo', 'tarjeta', 'transferencia'
            ps.setString(4, p.getMetodo().toLowerCase().replace("tarjeta de crédito/débito", "tarjeta"));
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}