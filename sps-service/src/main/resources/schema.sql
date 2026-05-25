-- schema.sql (basado en estructura de 05_textos_h2)

DROP TABLE IF EXISTS item_compra;
DROP TABLE IF EXISTS compra;
DROP TABLE IF EXISTS servicio_medico;
DROP TABLE IF EXISTS plan;
DROP TABLE IF EXISTS cliente;

CREATE TABLE cliente (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre     VARCHAR(100),
    correo     VARCHAR(150),
    cedula     VARCHAR(20) UNIQUE,
    password   VARCHAR(255),
    fecha_registro TIMESTAMP
);

CREATE TABLE plan (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo      VARCHAR(50) UNIQUE,
    nombre      VARCHAR(100),
    descripcion VARCHAR(500),
    precio      DOUBLE
);

CREATE TABLE servicio_medico (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(100),
    tipo    VARCHAR(50),
    precio  DOUBLE,
    plan_id BIGINT,
    FOREIGN KEY (plan_id) REFERENCES plan(id)
);

CREATE TABLE compra (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo              VARCHAR(50) UNIQUE,
    cliente_id          BIGINT,
    estado_compra       VARCHAR(50),
    valor_total         DOUBLE,
    fecha_creacion      TIMESTAMP,
    fecha_actualizacion TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE item_compra (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    compra_id             BIGINT,
    plan_id               BIGINT,
    estado_validacion_sns VARCHAR(50),
    precio                DOUBLE,
    FOREIGN KEY (compra_id) REFERENCES compra(id),
    FOREIGN KEY (plan_id)   REFERENCES plan(id)
);
