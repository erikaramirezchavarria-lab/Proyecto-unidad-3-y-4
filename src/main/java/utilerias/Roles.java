package utilerias;

public final class Roles {
    public static final String ADMIN = "admin";
    public static final String OPERADOR = "operador";
    public static final String CONSULTOR = "consultor";

    private Roles() {
    }

    public static boolean esConsultor(String rol) {
        return CONSULTOR.equalsIgnoreCase(rol);
    }

    public static boolean esAdmin(String rol) {
        return ADMIN.equalsIgnoreCase(rol);
    }
}
