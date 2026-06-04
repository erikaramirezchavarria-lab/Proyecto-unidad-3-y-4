package modelos;

/**
 * @author Gilberto
 */
public class Habitacion {
    private int idHabitacion;
    private String numero;
    private String tipo;   // sencilla, doble, suite
    private double precio;
    private String estado; // libre, ocupada, reservada
    private byte[] foto;

    public Habitacion() {}

    // Constructor para insertar (sin ID porque es Auto_Increment en MySQL)
    public Habitacion(String numero, String tipo, double precio, String estado, byte[] foto) {
        this.numero = numero;
        this.tipo = tipo;
        this.precio = precio;
        this.estado = estado;
        this.foto = foto;
    }

    // Constructor completo para listar
    public Habitacion(int idHabitacion, String numero, String tipo, double precio, String estado, byte[] foto) {
        this.idHabitacion = idHabitacion;
        this.numero = numero;
        this.tipo = tipo;
        this.precio = precio;
        this.estado = estado;
        this.foto = foto;
    }

    // Getters y Setters
    public int getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(int idHabitacion) { this.idHabitacion = idHabitacion; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public byte[] getFoto() { return foto; }
    public void setFoto(byte[] foto) { this.foto = foto; }
}