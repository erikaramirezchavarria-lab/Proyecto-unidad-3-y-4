package vistas;

import java.util.List;

public class PanelUsuarios extends CrudPanel {
    public PanelUsuarios() {
        this("admin");
    }

    public PanelUsuarios(String rol) {
        super(
                rol,
                "Gestion de usuarios",
                "usuarios",
                "id_usuario",
                new String[]{"id_usuario", "usuario", "correo", "rol", "activo", "fecha_registro"},
                "usuario",
                "fecha_registro",
                "usuarios",
                List.of(
                        Field.text("usuario", "Usuario", true),
                        Field.text("correo", "Correo electronico", true),
                        Field.text("password", "Contrasena", true),
                        Field.combo("rol", "Rol", "admin", "operador", "consultor"),
                        Field.combo("activo", "Activo", "1", "0")
                )
        );
    }
}
