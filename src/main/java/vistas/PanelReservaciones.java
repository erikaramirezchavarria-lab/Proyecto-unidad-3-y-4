package vistas;

import java.util.List;

public class PanelReservaciones extends CrudPanel {
    public PanelReservaciones() {
        this("admin");
    }

    public PanelReservaciones(String rol) {
        super(
                rol,
                "Gestion de reservas",
                "reservas",
                "id_reserva",
                new String[]{
                        "reservas.id_reserva",
                        "reservas.id_huesped",
                        "huespedes.nombre AS huesped",
                        "reservas.id_habitacion",
                        "habitaciones.numero AS habitacion",
                        "reservas.fecha_inicio",
                        "reservas.fecha_fin",
                        "reservas.estado"
                },
                "huespedes.nombre",
                "reservas.fecha_inicio",
                "reservas LEFT JOIN huespedes ON reservas.id_huesped = huespedes.id_huesped "
                        + "LEFT JOIN habitaciones ON reservas.id_habitacion = habitaciones.id_habitacion",
                List.of(
                        Field.integer("id_huesped", "ID huesped", true, false),
                        Field.integer("id_habitacion", "ID habitacion", true, false),
                        Field.text("fecha_inicio", "Fecha inicio yyyy-MM-dd", true),
                        Field.text("fecha_fin", "Fecha fin yyyy-MM-dd", true),
                        Field.combo("estado", "Estado", "activa", "finalizada", "cancelada")
                )
        );
    }
}
