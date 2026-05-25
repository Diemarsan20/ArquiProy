-- data.sql (basado en estructura de 05_textos_h2)

INSERT INTO cliente (nombre, correo, cedula, password, fecha_registro) VALUES
('Juan Perez',   'juan@example.com',   '11111111', 'pass123', CURRENT_TIMESTAMP),
('Maria Gomez',  'maria@example.com',  '22222222', 'pass123', CURRENT_TIMESTAMP),
('Carlos Ruiz',  'carlos@example.com', '33333333', 'pass123', CURRENT_TIMESTAMP);

INSERT INTO plan (codigo, nombre, descripcion, precio) VALUES
('PLAN-001', 'Plan Básico',    'Consultas generales',                  350000.0),
('PLAN-002', 'Plan Familiar',  'Consultas + exámenes de laboratorio',  750000.0),
('PLAN-003', 'Plan Premium',   'Cobertura total incluida hospitalización', 1500000.0);

INSERT INTO servicio_medico (nombre, tipo, precio, plan_id) VALUES
('Consulta General',        'consulta',        100000.0, 1),
('Consulta Especialista',   'consulta',        250000.0, 1),
('Examen de Sangre',        'examen',          150000.0, 2),
('Rayos X',                 'examen',          200000.0, 2),
('Consulta Especialista',   'consulta',        250000.0, 2),
('Hospitalización',         'hospitalizacion', 800000.0, 3),
('Cirugía Programada',      'hospitalizacion', 500000.0, 3),
('Consulta Especialista',   'consulta',        200000.0, 3);
