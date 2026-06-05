package vistas;

import java.util.List;

public class PanelServicios extends CrudPanel {
    public PanelServicios() {
        this("admin");
    }

    public PanelServicios(String rol) {
        super(
                rol,
                "Gestion de servicios",
                "servicios",
                "id_servicio",
                new String[]{
                        "servicios.id_servicio",
                        "servicios.id_reserva",
                        "huespedes.nombre AS huesped",
                        "servicios.nombre",
                        "servicios.descripcion",
                        "servicios.precio",
                        "servicios.estado",
                        "servicios.fecha_registro"
                },
                "servicios.nombre",
                "servicios.fecha_registro",
                "servicios LEFT JOIN reservas ON servicios.id_reserva = reservas.id_reserva "
                        + "LEFT JOIN huespedes ON reservas.id_huesped = huespedes.id_huesped",
                List.of(
                        Field.integer("id_reserva", "ID reserva", false, true),
                        Field.text("nombre", "Nombre", true),
                        Field.nullableText("descripcion", "Descripcion"),
                        Field.decimal("precio", "Precio", true),
                        Field.combo("estado", "Estado", "activo", "inactivo")
                )
        );
    }
}
