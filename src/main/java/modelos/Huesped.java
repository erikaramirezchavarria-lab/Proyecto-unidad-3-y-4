package modelos;

/**
 * @author Gilberto
 */
public class Huesped {
    private int idHuesped;
    private String nombre;
    private String telefono;
    private String email;
    private byte[] foto; // Mapea el MEDIUMBLOB de MySQL

    // Constructor vacío
    public Huesped() {
    }

    // Constructor con datos (sin ID, útil para registrar)
    public Huesped(String nombre, String telefono, String email, byte[] foto) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.foto = foto;
    }

    // Constructor completo (con ID, útil para listar y actualizar)
    public Huesped(int idHuesped, String nombre, String telefono, String email, byte[] foto) {
        this.idHuesped = idHuesped;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.foto = foto;
    }

    // Getters y Setters
    public int getIdHuesped() { return idHuesped; }
    public void setIdHuesped(int idHuesped) { this.idHuesped = idHuesped; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public byte[] getFoto() { return foto; }
    public void setFoto(byte[] foto) { this.foto = foto; }
}