# Proyecto-unidad-3-y-4
# Sistema de Gestión Hotelera (King Hotel)

Proyecto de sistema de escritorio para la administración de un hotel, desarrollado en Java con conexión a MySQL.

## REQUISITOS
Java JDK 17 o superior

MySQL Server (XAMPP o instalado directamente)

MySQL Workbench (o phpMyAdmin) para importar la base de datos

## Instrucciones de Instalación
1. Clonar este repositorio.
2. Importar el script SQL ubicado en `db_kinghotel` a MySQL usando phpMyAdmin o Workbench.
3. Configurar la cadena de conexión en `utilerias/ConexionBD.java` con tus credenciales de base de datos.
4. Compilar y ejecutar el proyecto con Maven: `mvn clean install`.

## Características Principales
- Login con 3 niveles de acceso (Administrador, Operador, Auditor).
- CRUD completo de servicios y gestión de pagos.
- Exportación de reportes a PDF.
- Interfaz gráfica intuitiva con validaciones de datos.

CONFIGURACIÓN DE LA BASE DE DATOS
Abrir MySQL Workbench o phpMyAdmin.

Crear un nuevo esquema/base de datos con el nombre: hotel_db.

Importar el archivo ubicado en la carpeta:
database/script_hotel.sql

Ejecutar el script para crear las tablas, relaciones (FK) y cargar los 10 registros de prueba por tabla.

CONFIGURACIÓN DE CONEXIÓN
Abrir el proyecto en tu IDE (NetBeans, IntelliJ o Eclipse).

Localizar la clase de conexión en: src/utilerias/ConexionBD.java.

Ajustar los datos de conexión según tu configuración local:

URL: jdbc:mysql://localhost:3306/hotel_db

Usuario: root

Contraseña: [Tu contraseña de MySQL]

ESTRUCTURA DEL PROYECTO
src/ → Código fuente completo (Vistas, DAO, Modelos, Utilerías).

database/ → Script SQL (script_hotel.sql).

doc/ → Manual de Usuario en formato PDF.

README.md → Instrucciones de instalación.

NOTAS IMPORTANTES
Asegúrate de que MySQL esté en ejecución antes de abrir el sistema.

Verifica que el usuario de base de datos tenga permisos para realizar operaciones (CRUD).

Si utilizas otra base de datos, no olvides actualizar la cadena de conexión en ConexionBD.java.

##INTEGRANTES
Garcia Contreras Genesis Nerina 22580080
Contreras Juarez Ezry Alexandra  22580067
Isidro Sanchez Lisset 21580044
Ramirez Chavarria Erika 21580059
