-- ============================================================
--  Aceros y Trefilados — Schema v2
--  Mejoras respecto a v1:
--    - Tipos de datos correctos (DATE, DECIMAL, INT, TINYINT)
--    - FKs reales en `usuarios` y `produccion`
--    - Sin columna `edad` (se calcula de fecha_nacimiento)
--    - Typo corregido: pimera_sesion → primera_sesion
--    - create_time separado: created_at (inmutable) en todas las tablas
--    - Columnas de catálogo en `usuarios` usan IDs (no strings)
--    - Columnas de catálogo en `produccion` usan IDs (no strings)
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ─────────────────────────────────────────────
--  CATÁLOGOS GEOGRÁFICOS (sin FK outgoing)
-- ─────────────────────────────────────────────

DROP TABLE IF EXISTS `paises`;
CREATE TABLE `paises` (
  `id`        INT          NOT NULL AUTO_INCREMENT,
  `sortname`  VARCHAR(3)   NOT NULL,
  `name`      VARCHAR(150) NOT NULL,
  `phonecode` INT          NOT NULL DEFAULT 0,
  `created_at` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `estados`;
CREATE TABLE `estados` (
  `id`         INT          NOT NULL AUTO_INCREMENT,
  `name`       VARCHAR(100) NOT NULL,
  `country_id` INT          NOT NULL,
  `created_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_estados_country` (`country_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `ciudades`;
CREATE TABLE `ciudades` (
  `id`         INT         NOT NULL AUTO_INCREMENT,
  `name`       VARCHAR(100) NOT NULL,
  `state_id`   INT          NOT NULL,
  `created_at` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_ciudades_state` (`state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
--  CATÁLOGOS DE EMPRESA
-- ─────────────────────────────────────────────

DROP TABLE IF EXISTS `genero`;
CREATE TABLE `genero` (
  `id`         INT         NOT NULL AUTO_INCREMENT,
  `genero`     VARCHAR(45) NOT NULL,
  `created_at` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `genero` (`genero`) VALUES ('MASCULINO'), ('FEMENINO'), ('OTRO');

DROP TABLE IF EXISTS `tipo_usuario`;
CREATE TABLE `tipo_usuario` (
  `id`         INT         NOT NULL AUTO_INCREMENT,
  `puesto`     VARCHAR(45) NOT NULL,
  `created_at` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `tipo_usuario` (`puesto`) VALUES ('EMPLEADO GENERAL'), ('SUPERVISOR'), ('GERENTE');

DROP TABLE IF EXISTS `tipo_pago`;
CREATE TABLE `tipo_pago` (
  `id`         INT         NOT NULL AUTO_INCREMENT,
  `metodo`     VARCHAR(45) NOT NULL,
  `created_at` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `tipo_pago` (`metodo`) VALUES ('EFECTIVO'), ('TARJETA DE DEBITO'), ('CHEQUE'), ('OTRO');

DROP TABLE IF EXISTS `bancos`;
CREATE TABLE `bancos` (
  `id`         INT         NOT NULL AUTO_INCREMENT,
  `nombre`     VARCHAR(100) NOT NULL,
  `created_at` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `bancos` (`nombre`) VALUES
  ('Banamex'), ('BBVA'), ('HSBC'), ('Santander'), ('Scotiabank'),
  ('Banco Azteca'), ('BanBajío'), ('Inbursa'), ('BanRegio'), ('Banorte'), ('BanCoppel');

DROP TABLE IF EXISTS `periodicidad_pago`;
CREATE TABLE `periodicidad_pago` (
  `id`         INT         NOT NULL AUTO_INCREMENT,
  `periodo`    VARCHAR(45) NOT NULL,
  `dias`       INT,
  `created_at` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `periodicidad_pago` (`periodo`, `dias`) VALUES
  ('SEMANAL', 7), ('QUINCENAL', 15), ('MENSUAL', 30), ('OTRO', NULL);

DROP TABLE IF EXISTS `tipo_contratos`;
CREATE TABLE `tipo_contratos` (
  `id`         INT         NOT NULL AUTO_INCREMENT,
  `contrato`   VARCHAR(45) NOT NULL,
  `meses`      INT,
  `created_at` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `tipo_contratos` (`contrato`, `meses`) VALUES
  ('1 MES', 1), ('3 MESES', 3), ('6 MESES', 6), ('1 AÑO', 12), ('INDEFINIDO', NULL);

-- ─────────────────────────────────────────────
--  CATÁLOGOS DE PRODUCCIÓN
-- ─────────────────────────────────────────────

DROP TABLE IF EXISTS `materiales`;
CREATE TABLE `materiales` (
  `id`         INT          NOT NULL AUTO_INCREMENT,
  `nombre`     VARCHAR(100) NOT NULL,
  `created_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Materiales más comunes en trefilado / malla
INSERT INTO `materiales` (`nombre`) VALUES
  ('ALAMBRE LISO'),
  ('ALAMBRE TORCIDO'),
  ('ALAMBRE GALVANIZADO'),
  ('VARILLA CORRUGADA'),
  ('VARILLA LISA'),
  ('MALLA ELECTROSOLDADA'),
  ('MALLA HEXAGONAL'),
  ('CABLE DE ACERO'),
  ('CLAVO'),
  ('TREFILADO');

DROP TABLE IF EXISTS `alturas`;
CREATE TABLE `alturas` (
  `id`         INT          NOT NULL AUTO_INCREMENT,
  `nombre`     VARCHAR(100) NOT NULL,
  `altura`     DECIMAL(10,2),
  `created_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Alturas/longitudes estándar en metros
INSERT INTO `alturas` (`nombre`, `altura`) VALUES
  ('0.60 m',  0.60),
  ('0.90 m',  0.90),
  ('1.00 m',  1.00),
  ('1.20 m',  1.20),
  ('1.50 m',  1.50),
  ('1.80 m',  1.80),
  ('2.00 m',  2.00),
  ('2.40 m',  2.40),
  ('3.00 m',  3.00),
  ('4.00 m',  4.00),
  ('6.00 m',  6.00);

DROP TABLE IF EXISTS `calibres`;
CREATE TABLE `calibres` (
  `id`         INT          NOT NULL AUTO_INCREMENT,
  `nombre`     VARCHAR(100) NOT NULL,
  `calibre`    DECIMAL(10,2),
  `created_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Calibres (diámetros en mm) comunes en trefilado de acero
INSERT INTO `calibres` (`nombre`, `calibre`) VALUES
  ('Cal 2.5 mm',  2.50),
  ('Cal 3.0 mm',  3.00),
  ('Cal 3.5 mm',  3.50),
  ('Cal 4.0 mm',  4.00),
  ('Cal 4.5 mm',  4.50),
  ('Cal 5.0 mm',  5.00),
  ('Cal 5.5 mm',  5.50),
  ('Cal 6.0 mm',  6.00),
  ('Cal 6.5 mm',  6.50),
  ('Cal 7.0 mm',  7.00),
  ('Cal 8.0 mm',  8.00),
  ('Cal 9.0 mm',  9.00),
  ('Cal 10.0 mm', 10.00),
  ('Cal 12.0 mm', 12.00);

DROP TABLE IF EXISTS `rombos`;
CREATE TABLE `rombos` (
  `id`         INT          NOT NULL AUTO_INCREMENT,
  `nombre`     VARCHAR(100) NOT NULL,
  `rombo`      DECIMAL(10,2),
  `created_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tamaño de rombo (abertura de malla) en cm
INSERT INTO `rombos` (`nombre`, `rombo`) VALUES
  ('2.5 cm',   2.50),
  ('5.0 cm',   5.00),
  ('7.5 cm',   7.50),
  ('10.0 cm', 10.00),
  ('12.5 cm', 12.50),
  ('15.0 cm', 15.00),
  ('20.0 cm', 20.00),
  ('25.0 cm', 25.00);

-- ─────────────────────────────────────────────
--  USUARIOS
-- ─────────────────────────────────────────────

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE `usuarios` (
  `usuario_id`         INT           NOT NULL AUTO_INCREMENT,
  `username`           VARCHAR(45),
  `nombre`             VARCHAR(100)  NOT NULL,
  `apellido_paterno`   VARCHAR(100)  NOT NULL,
  `apellido_materno`   VARCHAR(100)  NOT NULL,
  `curp`               VARCHAR(18),
  `rfc`                VARCHAR(13),
  `nss`                VARCHAR(11),
  `fecha_nacimiento`   DATE,
  `fecha_contratacion` DATE,
  `email`              VARCHAR(255),
  `genero_id`          INT,
  `sueldo`             DECIMAL(10,2),
  `tipo_pago_id`       INT,
  `banco_id`           INT,
  `numero_cuenta`      VARCHAR(50),
  `periodo_pago_id`    INT,
  `tipo_contrato_id`   INT,
  `pais_id`            INT,
  `estado_id`          INT,
  `ciudad_id`          INT,
  `localidad`          VARCHAR(100),
  `colonia`            VARCHAR(100),
  `calle`              VARCHAR(100),
  `numero_exterior`    VARCHAR(20),
  `numero_interior`    VARCHAR(20),
  `codigo_postal`      VARCHAR(10),
  `tipo_usuario_id`    INT,
  `password`           VARCHAR(255)  NOT NULL,
  `primera_sesion`     TINYINT(1)    NOT NULL DEFAULT 0,
  `imagen`             MEDIUMBLOB,
  `created_at`         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`usuario_id`),
  CONSTRAINT `fk_usr_genero`       FOREIGN KEY (`genero_id`)       REFERENCES `genero`(`id`)            ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT `fk_usr_tipo_pago`    FOREIGN KEY (`tipo_pago_id`)    REFERENCES `tipo_pago`(`id`)         ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT `fk_usr_banco`        FOREIGN KEY (`banco_id`)        REFERENCES `bancos`(`id`)            ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT `fk_usr_periodo`      FOREIGN KEY (`periodo_pago_id`) REFERENCES `periodicidad_pago`(`id`) ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT `fk_usr_contrato`     FOREIGN KEY (`tipo_contrato_id`)REFERENCES `tipo_contratos`(`id`)    ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT `fk_usr_tipo_usuario` FOREIGN KEY (`tipo_usuario_id`) REFERENCES `tipo_usuario`(`id`)     ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────
--  SEED: USUARIO ADMINISTRADOR INICIAL
--  usuario_id = 1
--  password   = '1234'  (debe cambiar en primer inicio de sesión)
--  primera_sesion = 0   → la app obliga a cambiar la contraseña al primer login
-- ─────────────────────────────────────────────
INSERT INTO `usuarios`
  (nombre, apellido_paterno, apellido_materno, password, tipo_usuario_id, primera_sesion)
VALUES
  ('Admin', 'Aceros', 'Sistema', '1234', 3, 0);

-- ─────────────────────────────────────────────
--  PRODUCCIÓN
-- ─────────────────────────────────────────────

DROP TABLE IF EXISTS `produccion`;
CREATE TABLE `produccion` (
  `id`             INT           NOT NULL AUTO_INCREMENT,
  `material_id`    INT,
  `calibre_id`     INT,
  `altura_id`      INT,
  `rombo_id`       INT,
  `metros`         DECIMAL(10,2),
  `cantidad`       INT,
  `autor_id`       INT,
  `fecha_registro` DATE,
  `dia`            VARCHAR(20),
  `created_at`     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_prod_material` FOREIGN KEY (`material_id`) REFERENCES `materiales`(`id`) ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT `fk_prod_calibre`  FOREIGN KEY (`calibre_id`)  REFERENCES `calibres`(`id`)   ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT `fk_prod_altura`   FOREIGN KEY (`altura_id`)   REFERENCES `alturas`(`id`)    ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT `fk_prod_rombo`    FOREIGN KEY (`rombo_id`)    REFERENCES `rombos`(`id`)     ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT `fk_prod_autor`    FOREIGN KEY (`autor_id`)    REFERENCES `usuarios`(`usuario_id`) ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
