package vistas;

import java.util.List;

public class PanelClientes extends CrudPanel {
    public PanelClientes() {
        this("admin");
    }

    public PanelClientes(String rol) {
        super(
                rol,
                "Gestion de huespedes",
                "huespedes",
                "id_huesped",
                new String[]{"id_huesped", "nombre", "telefono", "email", "fecha_registro"},
                "nombre",
                "fecha_registro",
                "huespedes",
                List.of(
                        Field.text("nombre", "Nombre completo", true),
                        Field.nullableText("telefono", "Telefono"),
                        Field.nullableText("email", "Correo electronico"),
                        Field.blob("foto", "Foto")
                )
        );
    }
}
