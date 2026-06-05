CREATE DATABASE IF NOT EXISTS hotel_db
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_unicode_ci;

USE hotel_db;

DROP TABLE IF EXISTS pagos;
DROP TABLE IF EXISTS servicios;
DROP TABLE IF EXISTS reservas;
DROP TABLE IF EXISTS habitaciones;
DROP TABLE IF EXISTS huespedes;
DROP TABLE IF EXISTS usuarios;

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    correo VARCHAR(100) NOT NULL,
    password VARCHAR(64) NOT NULL,
    rol ENUM('admin','operador','consultor') NOT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE huespedes (
    id_huesped INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100),
    foto MEDIUMBLOB,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE habitaciones (
    id_habitacion INT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(10) NOT NULL UNIQUE,
    tipo ENUM('sencilla','doble','suite') NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    estado ENUM('libre','ocupada','reservada') NOT NULL DEFAULT 'libre',
    foto MEDIUMBLOB,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reservas (
    id_reserva INT AUTO_INCREMENT PRIMARY KEY,
    id_huesped INT NOT NULL,
    id_habitacion INT NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    estado ENUM('activa','finalizada','cancelada') NOT NULL DEFAULT 'activa',
    CONSTRAINT fk_reservas_huespedes
        FOREIGN KEY (id_huesped) REFERENCES huespedes(id_huesped)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_reservas_habitaciones
        FOREIGN KEY (id_habitacion) REFERENCES habitaciones(id_habitacion)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE servicios (
    id_servicio INT AUTO_INCREMENT PRIMARY KEY,
    id_reserva INT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    precio DECIMAL(10,2) NOT NULL,
    estado ENUM('activo','inactivo') NOT NULL DEFAULT 'activo',
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_servicios_reservas
        FOREIGN KEY (id_reserva) REFERENCES reservas(id_reserva)
        ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE pagos (
    id_pago INT AUTO_INCREMENT PRIMARY KEY,
    id_reserva INT NULL,
    id_huesped INT NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    metodo_pago ENUM('efectivo','tarjeta','transferencia') NOT NULL,
    fecha_pago DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('pendiente','pagado','cancelado') NOT NULL DEFAULT 'pagado',
    CONSTRAINT fk_pagos_reservas
        FOREIGN KEY (id_reserva) REFERENCES reservas(id_reserva)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_pagos_huespedes
        FOREIGN KEY (id_huesped) REFERENCES huespedes(id_huesped)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

INSERT INTO usuarios (usuario, correo, password, rol, activo) VALUES
('admin', 'admin@kinghotel.com', SHA2('Admin123', 256), 'admin', 1),
('operador', 'operador@kinghotel.com', SHA2('Operador123', 256), 'operador', 1),
('consultor', 'consultor@kinghotel.com', SHA2('Consultor123', 256), 'consultor', 1),
('recepcion1', 'recepcion1@kinghotel.com', SHA2('Recepcion123', 256), 'operador', 1),
('recepcion2', 'recepcion2@kinghotel.com', SHA2('Recepcion124', 256), 'operador', 1),
('ventas1', 'ventas1@kinghotel.com', SHA2('Ventas123', 256), 'operador', 1),
('consulta1', 'consulta1@kinghotel.com', SHA2('Consulta123', 256), 'consultor', 1),
('consulta2', 'consulta2@kinghotel.com', SHA2('Consulta124', 256), 'consultor', 1),
('gerencia', 'gerencia@kinghotel.com', SHA2('Gerencia123', 256), 'admin', 1),
('soporte', 'soporte@kinghotel.com', SHA2('Soporte123', 256), 'admin', 1);

INSERT INTO huespedes (nombre, telefono, email) VALUES
('Juan Perez', '8991234501', 'juan.perez@example.com'),
('Maria Lopez', '8991234502', 'maria.lopez@example.com'),
('Carlos Ramirez', '8991234503', 'carlos.ramirez@example.com'),
('Ana Torres', '8991234504', 'ana.torres@example.com'),
('Luis Hernandez', '8991234505', 'luis.hernandez@example.com'),
('Sofia Martinez', '8991234506', 'sofia.martinez@example.com'),
('Miguel Garcia', '8991234507', 'miguel.garcia@example.com'),
('Laura Sanchez', '8991234508', 'laura.sanchez@example.com'),
('Diego Flores', '8991234509', 'diego.flores@example.com'),
('Valeria Castro', '8991234510', 'valeria.castro@example.com');

INSERT INTO habitaciones (numero, tipo, precio, estado) VALUES
('101', 'sencilla', 800.00, 'libre'),
('102', 'sencilla', 850.00, 'ocupada'),
('103', 'doble', 1200.00, 'reservada'),
('104', 'doble', 1250.00, 'libre'),
('201', 'suite', 2400.00, 'ocupada'),
('202', 'suite', 2500.00, 'reservada'),
('203', 'sencilla', 900.00, 'libre'),
('204', 'doble', 1350.00, 'libre'),
('301', 'suite', 2700.00, 'reservada'),
('302', 'doble', 1400.00, 'ocupada');

INSERT INTO reservas (id_huesped, id_habitacion, fecha_inicio, fecha_fin, estado) VALUES
(1, 2, '2026-06-01', '2026-06-05', 'activa'),
(2, 3, '2026-06-02', '2026-06-06', 'activa'),
(3, 5, '2026-06-03', '2026-06-07', 'finalizada'),
(4, 6, '2026-06-04', '2026-06-08', 'activa'),
(5, 9, '2026-06-05', '2026-06-10', 'activa'),
(6, 10, '2026-06-06', '2026-06-09', 'cancelada'),
(7, 1, '2026-06-07', '2026-06-11', 'activa'),
(8, 4, '2026-06-08', '2026-06-12', 'activa'),
(9, 7, '2026-06-09', '2026-06-13', 'finalizada'),
(10, 8, '2026-06-10', '2026-06-14', 'activa');

INSERT INTO servicios (id_reserva, nombre, descripcion, precio, estado) VALUES
(1, 'Restaurante', 'Cena para dos personas', 650.00, 'activo'),
(2, 'Spa', 'Masaje relajante', 900.00, 'activo'),
(3, 'Lavanderia', 'Lavado de ropa', 180.00, 'activo'),
(4, 'Minibar', 'Consumo minibar', 320.00, 'activo'),
(5, 'Tour', 'Tour local guiado', 1200.00, 'activo'),
(6, 'Restaurante', 'Desayuno buffet', 300.00, 'activo'),
(7, 'Transporte', 'Traslado aeropuerto', 500.00, 'activo'),
(8, 'Spa', 'Circuito termal', 700.00, 'activo'),
(9, 'Lavanderia', 'Planchado express', 150.00, 'inactivo'),
(10, 'Room service', 'Servicio a habitacion', 250.00, 'activo');

INSERT INTO pagos (id_reserva, id_huesped, monto, metodo_pago, fecha_pago, estado) VALUES
(1, 1, 3850.00, 'tarjeta', '2026-06-01 10:10:00', 'pagado'),
(2, 2, 5100.00, 'efectivo', '2026-06-02 11:15:00', 'pagado'),
(3, 3, 7380.00, 'transferencia', '2026-06-03 12:20:00', 'pagado'),
(4, 4, 10900.00, 'tarjeta', '2026-06-04 13:25:00', 'pagado'),
(5, 5, 14700.00, 'transferencia', '2026-06-05 14:30:00', 'pendiente'),
(6, 6, 4200.00, 'efectivo', '2026-06-06 15:35:00', 'cancelado'),
(7, 7, 3700.00, 'tarjeta', '2026-06-07 16:40:00', 'pagado'),
(8, 8, 5700.00, 'efectivo', '2026-06-08 17:45:00', 'pagado'),
(9, 9, 3750.00, 'transferencia', '2026-06-09 18:50:00', 'pagado'),
(10, 10, 5850.00, 'tarjeta', '2026-06-10 19:55:00', 'pendiente');
