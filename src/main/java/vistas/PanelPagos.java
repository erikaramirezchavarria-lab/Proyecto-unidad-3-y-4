package vistas;

import java.util.List;

public class PanelPagos extends CrudPanel {
    public PanelPagos() {
        this("admin");
    }

    public PanelPagos(String rol) {
        super(
                rol,
                "Gestion de pagos",
                "pagos",
                "id_pago",
                new String[]{
                        "pagos.id_pago",
                        "pagos.id_reserva",
                        "pagos.id_huesped",
                        "huespedes.nombre AS huesped",
                        "pagos.monto",
                        "pagos.metodo_pago",
                        "pagos.fecha_pago",
                        "pagos.estado"
                },
                "huespedes.nombre",
                "pagos.fecha_pago",
                "pagos LEFT JOIN huespedes ON pagos.id_huesped = huespedes.id_huesped "
                        + "LEFT JOIN reservas ON pagos.id_reserva = reservas.id_reserva",
                List.of(
                        Field.integer("id_reserva", "ID reserva", false, true),
                        Field.integer("id_huesped", "ID huesped", true, false),
                        Field.decimal("monto", "Monto", true),
                        Field.combo("metodo_pago", "Metodo de pago", "efectivo", "tarjeta", "transferencia"),
                        Field.nullableText("fecha_pago", "Fecha pago yyyy-MM-dd HH:mm:ss"),
                        Field.combo("estado", "Estado", "pendiente", "pagado", "cancelado")
                )
        );
    }
}
