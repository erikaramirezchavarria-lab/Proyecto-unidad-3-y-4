package dao;

import utilerias.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author LISSET ISIDRO SANCHEZ
 */
public class UsuarioDAO {

    // Validar login
    public String validarLogin(String usuario, String password) {
        String rol = null;
        // CORRECCIÓN: Quitamos SHA2(?,256) de SQL porque el LoginView ya debe mandar la contraseña encriptada o la procesamos aquí.
        // Para que funcione directo con lo que mande el Login, usamos '?' directo.
        String sql = "SELECT rol FROM usuarios WHERE usuario = ? AND password = ? AND activo = 1";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, password); // Recibe el hash de 64 caracteres

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rol = rs.getString("rol");
            }
        } catch (Exception e) {
            System.err.println("Error en validarLogin (DAO): " + e.getMessage());
            e.printStackTrace();
        }
        return rol;
    }

    // Registrar nuevo usuario
    public boolean registrarUsuario(String usuario, String password, String correo, String rol) {
        boolean registrado = false;
        // CORRECCIÓN: Quitamos el SHA2(?,256) de la consulta SQL. 
        // Ahora dejamos simplemente '?' porque el FrmRegistro ya le pasa la contraseña perfectamente encriptada desde Java.
        String sql = "INSERT INTO usuarios (usuario, correo, password, rol, activo) VALUES (?, ?, ?, ?, 1)";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario); // usuario único
            ps.setString(2, correo);   // correo electrónico
            ps.setString(3, password); // Ya viene encriptada como una cadena de 64 caracteres desde Java
            ps.setString(4, rol);

            int filas = ps.executeUpdate();
            registrado = (filas > 0);
        } catch (Exception e) {
            // Esto imprimirá en la consola el error exacto de MySQL si algo falla
            System.err.println("Error en registrarUsuario (DAO): " + e.getMessage());
            e.printStackTrace();
        }
        return registrado;
    }

    // Verificar si usuario ya existe
    public boolean existeUsuario(String usuario) {
        boolean existe = false;
        String sql = "SELECT id_usuario FROM usuarios WHERE usuario = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                existe = true;
            }
        } catch (Exception e) {
            System.err.println("Error en existeUsuario (DAO): " + e.getMessage());
            e.printStackTrace();
        }
        return existe;
    }
}