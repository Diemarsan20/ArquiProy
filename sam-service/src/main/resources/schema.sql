-- schema.sql SAM (basado en 05_textos_h2)
DROP TABLE IF EXISTS agenda_item;

CREATE TABLE agenda_item (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo_compra    VARCHAR(50),
    cedula_cliente   VARCHAR(20),
    nombre_servicio  VARCHAR(100),
    tipo_servicio    VARCHAR(50),
    nombre_plan      VARCHAR(100),
    fecha_recepcion  TIMESTAMP
);
