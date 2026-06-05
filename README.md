# KING HOTEL

Sistema de escritorio para gestion hotelera desarrollado en Java Swing con MySQL. Permite administrar usuarios, huespedes, habitaciones, reservas, servicios y pagos con control de acceso por roles, manejo de imagenes BLOB y reportes PDF.

## Integrantes
Contreras Juarez Ezry Alexandra  22580067

Garcia Contreras Genesis Nerina 22580080

Isidro Sanchez Lisset 21580044

Ramirez Chavarria Erika 21580059



Requisitos

* Java JDK 17 o superior
* MySQL 8.x o MariaDB compatible
* NetBeans, IntelliJ IDEA o cualquier IDE Java
* Conector JDBC incluido por Maven: `mysql-connector-j`

## Base de datos

El script se encuentra en:

```text
database/hotel\_db.sql
```

Pasos para importar:

1. Abrir MySQL Workbench, phpMyAdmin o consola MySQL.
2. Ejecutar el archivo `database/hotel\_db.sql`.
3. Verificar que se cree la base `hotel\_db`.
4. Confirmar que existan las tablas `usuarios`, `huespedes`, `habitaciones`, `reservas`, `servicios` y `pagos`.

La conexion esta configurada en:

```text
src/main/java/utilerias/ConexionBD.java
```

Valores actuales:

```text
URL: jdbc:mysql://localhost:3306/hotel\_db
Usuario: root
Password: vacio
```

## Ejecutar el proyecto

Con Maven instalado:

```bash
mvn clean package
java -jar target/prac2-1.0.jar
```

Sin Maven, abrir el proyecto en NetBeans y ejecutar la clase:

```text
controladores.LoginController
```

## Usuarios de prueba

|Rol|Usuario|Contrasena|
|-|-|-|
|admin|admin|Admin123|
|operador|operador|Operador123|
|consultor|consultor|Consultor123|

El rol `consultor` es solo lectura: no puede guardar, editar ni eliminar registros.

## Modulos

* Login y registro de usuarios
* Gestion de usuarios
* Gestion de huespedes con foto BLOB
* Gestion de habitaciones con foto BLOB
* Gestion de reservas
* Gestion de servicios
* Gestion de pagos
* Reportes PDF por modulo
* Cierre de sesion

## Reportes PDF

Cada modulo incluye botones para:

* Listado general
* Reporte por rango de fechas
* Reporte por criterio de busqueda
* Reporte estadistico/resumen
* Detalle individual

Cada reporte incluye nombre del sistema, nombre del reporte, fecha y hora, usuario activo y numero de pagina.

## Capturas principales

Agregar capturas en una carpeta `docs/capturas/`:

* Login
* Menu principal
* CRUD de huespedes
* Vista previa BLOB
* Reporte PDF generado

## Diagrama ER
    usuarios {
        INT id\_usuario PK
        VARCHAR usuario
        VARCHAR correo
        VARCHAR password
        ENUM rol
        TINYINT activo
        DATETIME fecha\_registro
    }

    huespedes {
        INT id\_huesped PK
        VARCHAR nombre
        VARCHAR telefono
        VARCHAR email
        MEDIUMBLOB foto
        DATETIME fecha\_registro
    }

    habitaciones {
        INT id\_habitacion PK
        VARCHAR numero
        ENUM tipo
        DECIMAL precio
        ENUM estado
        MEDIUMBLOB foto
        DATETIME fecha\_registro
    }

    reservas {
        INT id\_reserva PK
        INT id\_huesped FK
        INT id\_habitacion FK
        DATE fecha\_inicio
        DATE fecha\_fin
        ENUM estado
    }

    servicios {
        INT id\_servicio PK
        INT id\_reserva FK
        VARCHAR nombre
        VARCHAR descripcion
        DECIMAL precio
        ENUM estado
        DATETIME fecha\_registro
    }

    pagos {
        INT id\_pago PK
        INT id\_reserva FK
        INT id\_huesped FK
        DECIMAL monto
        ENUM metodo\_pago
        DATETIME fecha\_pago
        ENUM estado
    }

    huespedes ||--o{ reservas : realiza
    habitaciones ||--o{ reservas : asigna
    reservas ||--o{ servicios : consume
    reservas ||--o{ pagos : genera
    huespedes ||--o{ pagos : paga
```

## Enlace al video

Pendiente de agregar por el equipo.

hola
