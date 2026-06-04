package modelos;

public class Pago {
    private int idReserva;
    private int idHuesped;
    private double monto;
    private String metodo;

    public Pago(int idReserva, int idHuesped, double monto, String metodo) {
        this.idReserva = idReserva;
        this.idHuesped = idHuesped;
        this.monto = monto;
        this.metodo = metodo;
    }
    // Getters...
    public int getIdReserva() { return idReserva; }
    public int getIdHuesped() { return idHuesped; }
    public double getMonto() { return monto; }
    public String getMetodo() { return metodo; }
}