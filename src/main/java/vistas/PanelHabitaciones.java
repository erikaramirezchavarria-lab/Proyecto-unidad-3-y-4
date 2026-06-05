package vistas;

import java.util.List;

public class PanelHabitaciones extends CrudPanel {
    public PanelHabitaciones() {
        this("admin");
    }

    public PanelHabitaciones(String rol) {
        super(
                rol,
                "Gestion de habitaciones",
                "habitaciones",
                "id_habitacion",
                new String[]{"id_habitacion", "numero", "tipo", "precio", "estado", "fecha_registro"},
                "numero",
                "fecha_registro",
                "habitaciones",
                List.of(
                        Field.text("numero", "Numero", true),
                        Field.combo("tipo", "Tipo", "sencilla", "doble", "suite"),
                        Field.decimal("precio", "Precio", true),
                        Field.combo("estado", "Estado", "libre", "ocupada", "reservada"),
                        Field.blob("foto", "Foto")
                )
        );
    }
}
