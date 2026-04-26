-- init_aceros.sql
-- Script de inicialización mínimo para la base `acerosytrefilados`
-- Ejecutar desde la raíz del proyecto: mysql -u root -p1234 < db/init_aceros.sql

DROP DATABASE IF EXISTS acerosytrefilados;
CREATE DATABASE IF NOT EXISTS acerosytrefilados CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE acerosytrefilados;

-- Tabla usuarios
CREATE TABLE IF NOT EXISTS usuarios (
  usuario_id INT PRIMARY KEY AUTO_INCREMENT,
  password VARCHAR(255) NOT NULL,
  nombre VARCHAR(100),
  apellido_paterno VARCHAR(100),
  apellido_materno VARCHAR(100),
  edad INT,
  sueldo VARCHAR(50),
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_nacimiento DATE,
  fecha_contratacion DATE,
  curp VARCHAR(50),
  rfc VARCHAR(50),
  nss VARCHAR(50),
  email VARCHAR(100),
  genero VARCHAR(50),
  tipo_empleado VARCHAR(100),
  metodo_pago VARCHAR(50),
  banco VARCHAR(100),
  numero_cuenta VARCHAR(100),
  periodo_pago VARCHAR(50),
  tipo_contrato VARCHAR(50),
  pais VARCHAR(100),
  estado VARCHAR(100),
  localidad VARCHAR(100),
  colonia VARCHAR(100),
  numero_exterior VARCHAR(50),
  ciudad VARCHAR(100),
  calle VARCHAR(200),
  codigo_postal VARCHAR(20),
  numero_interior VARCHAR(50),
  pimera_sesion VARCHAR(10) DEFAULT '1',
  imagen LONGBLOB
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO usuarios (usuario_id, password, nombre, apellido_paterno, apellido_materno, edad, sueldo, tipo_empleado, pimera_sesion)
VALUES (1, '1234', 'Admin', 'Sistema', 'Root', 30, '0', 'GERENTE', '1');

-- Tabla materiales
CREATE TABLE IF NOT EXISTS materiales (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO materiales (nombre) VALUES ('Acero'), ('Aluminio');

-- Tabla alturas
CREATE TABLE IF NOT EXISTS alturas (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100),
  altura VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO alturas (nombre, altura) VALUES ('Altura1','10'), ('Altura2','20');

-- Tabla calibres
CREATE TABLE IF NOT EXISTS calibres (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100),
  calibre VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO calibres (nombre, calibre) VALUES ('Calibre1','C1'), ('Calibre2','C2');

-- Tabla rombos
CREATE TABLE IF NOT EXISTS rombos (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100),
  rombo VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO rombos (nombre, rombo) VALUES ('Rombo1','R1'), ('Rombo2','R2');

-- Tabla produccion
CREATE TABLE IF NOT EXISTS produccion (
  id INT PRIMARY KEY AUTO_INCREMENT,
  dia VARCHAR(50),
  material VARCHAR(100),
  calibre VARCHAR(100),
  altura VARCHAR(100),
  rombos VARCHAR(100),
  metros VARCHAR(50),
  cantidad VARCHAR(50),
  autor_id INT,
  fecha_registro DATE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO produccion (dia, material, calibre, altura, rombos, metros, cantidad, autor_id, fecha_registro)
VALUES ('LUNES','Acero','C1','10','R1','100','2',1, CURDATE());

-- Tablas para combos y selects
CREATE TABLE IF NOT EXISTS genero (genero VARCHAR(50) PRIMARY KEY) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO genero (genero) VALUES ('MASCULINO'), ('FEMENINO');

CREATE TABLE IF NOT EXISTS tipo_usuario (puesto VARCHAR(100) PRIMARY KEY) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO tipo_usuario (puesto) VALUES ('EMPLEADO GENERAL'), ('SUPERVISOR'), ('GERENTE');

CREATE TABLE IF NOT EXISTS tipo_pago (metodo VARCHAR(100) PRIMARY KEY) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO tipo_pago (metodo) VALUES ('EFECTIVO'), ('TRANSFERENCIA');

CREATE TABLE IF NOT EXISTS bancos (nombre VARCHAR(100) PRIMARY KEY) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO bancos (nombre) VALUES ('BANCO1'), ('BANCO2');

CREATE TABLE IF NOT EXISTS periodicidad_pago (periodo VARCHAR(50) PRIMARY KEY) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO periodicidad_pago (periodo) VALUES ('QUINCENAL'), ('MENSUAL');

CREATE TABLE IF NOT EXISTS tipo_contratos (contrato VARCHAR(100) PRIMARY KEY) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO tipo_contratos (contrato) VALUES ('INDEFINIDO'), ('TEMPORAL');

-- Paises / estados / ciudades
CREATE TABLE IF NOT EXISTS paises (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO paises (name) VALUES ('Mexico');

CREATE TABLE IF NOT EXISTS estados (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100), country_id INT) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO estados (name, country_id) VALUES ('Sinaloa', 1);

CREATE TABLE IF NOT EXISTS ciudades (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100), state_id INT) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO ciudades (name, state_id) VALUES ('Culiacan', 1);

-- Fin del script
