-- schema.sql SHC (basado en 05_textos_h2)
DROP TABLE IF EXISTS shc_plan;
DROP TABLE IF EXISTS shc_registro;

CREATE TABLE shc_registro (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo_compra   VARCHAR(50),
    cedula_cliente  VARCHAR(20),
    nombre_cliente  VARCHAR(100),
    correo_cliente  VARCHAR(150),
    fecha_recepcion TIMESTAMP
);

CREATE TABLE shc_plan (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    registro_id     BIGINT,
    codigo_plan     VARCHAR(50),
    nombre_plan     VARCHAR(100),
    precio          DOUBLE,
    FOREIGN KEY (registro_id) REFERENCES shc_registro(id)
);
