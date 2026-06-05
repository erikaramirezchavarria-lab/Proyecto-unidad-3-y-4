package modelos;

public class Servicio {
    private int idReserva;
    private String nombre;
    private String descripcion;
    private double precio;

    public Servicio() {
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }
}
