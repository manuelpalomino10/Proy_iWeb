USE iweb_proy;
INSERT INTO roles (idroles, nombre) VALUES
(1, 'admin'),
(2, 'coordinador'),
(3, 'encuestador');

INSERT INTO categoria (idcategoria, nombre) VALUES
(101, 'cat1'),
(102, 'cat2'),
(103, 'cat3'),
(104, 'cat4');

INSERT INTO zona (idzona, nombre) VALUES
(1, 'NORTE'),
(2, 'SUR'),
(3, 'ESTE'),
(4, 'OESTE');

INSERT INTO distritos (iddistritos, nombre, zona_idzona) VALUES
(1, 'Ancon', 1),
(2, 'Comas', 1),
(3, 'Los Olivos', 1),
(4, 'SJM', 2),
(5, 'VMT', 2),
(6, 'Lurin', 2),
(7, 'Ate', 3),
(8, 'SJL', 3),
(9, 'La Molina', 3),
(10, 'Rimac', 4),
(11, 'Breña', 4),
(12, 'Lince', 4);

INSERT INTO formulario (idformulario, fecha_creacion, fecha_limite, estado, categoria_idcategoria) VALUES
(1, '2023-01-15', '2023-02-15', 1, 101),
(2, '2023-02-01', '2023-03-01', 1, 102),
(3, '2023-03-10', '2023-04-10', 0, 101),
(4, '2023-04-05', '2023-05-05', 1, 103),
(5, '2023-05-20', '2023-06-20', 1, 102);

INSERT INTO preguntas (idPreguntas, enunciado, formulario_idformulario) VALUES
(1, 'pregunta1_1', 1),
(2, 'pregunta2_1', 1),
(3, 'pregunta3_1', 1),
(4, 'pregunta4_1', 1),
(5, 'pregunta5_1', 1),
(6, 'pregunta1_2', 2),
(7, 'pregunta2_2', 2),
(8, 'pregunta3_2', 2),
(9, 'pregunta4_2', 2),
(10, 'pregunta1_3', 3),
(11, 'pregunta2_3', 3),
(12, 'pregunta3_3', 3),
(13, 'pregunta1_4', 4),
(14, 'pregunta2_4', 4),
(15, 'pregunta1_5', 5);

INSERT INTO respuesta (idrespuesta,Preguntas_idPreguntas,respuesta) VALUES
(1, 1, 'respuesta_1'),
(2, 1, 'respuesta_1'),
(3, 2, 'respuesta_2'),
(4, 1, 'respuesta_1'),
(5, 1, 'respuesta_1'),
(6, 2, 'respuesta_2');

INSERT INTO usuario (idusuario,nombres,apellidos,contraseña,DNI,correo,direccion,estado,roles_idroles,zona_idzona,distritos_iddistritos,fecha_incorporacion,foto) VALUES
(1, 'Juan Carlos', 'Pérez López', '$2a$10$xJwL5vW18UzD7oNq9QYz0e', '12345678', 'juan.perez@empresa.com', 'Av. Principal 123', 1, 3, 3, 7, '2022-01-15', NULL),
(2, 'María Elena', 'Gómez Sánchez', '$2a$10$yH9vD4tR7sN2mKpL3QZx1f', '87654321', 'maria.gomez@empresa.com', 'Calle Secundaria 456', 1, 3, 1, 2, '2022-03-10', NULL),
(3, 'Carlos Alberto', 'Rodríguez Vargas', '$2a$10$zK8uB5wW2tP4nJ6mQ7Xy3g', '56789012', 'carlos.rodriguez@empresa.com', 'Jr. Uno 789', 1, 3, 2, 5, '2022-05-20', NULL),

(4, 'Ana Lucía', 'Martínez Díaz', '$2a$10$aB3cD6eF8hI9jK1L2M4n5o', '34567890', 'ana.martinez@empresa.com', NULL, 1, 1, 2, NULL, '2022-07-05', NULL),

(5, 'Pedro Pablo', 'López García', '$2a$10$bC4dE7fG9hJ0kL1M2N3o4p', '09876543', 'pedro.lopez@empresa.com', 'Calle Tres 654', 0, 2, 3, 8, '2022-09-12', NULL),
(6, 'Luisa Fernanda', 'Torres Méndez', '$2a$10$cD5eF8gH9iJ2kL3mN4o5pQ', '23456789', 'luisa.torres@empresa.com', NULL, 1, 2, 1, NULL, '2023-01-10', NULL),
(7, 'Roberto Carlos', 'Silva Castro', '$2a$10$dE6fG8hI1jK3lM4nO5pQr', '34567891', 'roberto.silva@empresa.com', 'Av. Central 456', 1, 2, 2, 4, '2023-03-15', NULL);

INSERT INTO usuario_has_formulario (usuario_idusuario, formulario_idformulario, respuesta_idrespuesta, codigo, fecha_asignacion, estado_rpta, fecha_registro) VALUES
(1, 1, 1, 'COD001', '2023-06-20', 'F', '2023-07-20'),
(1, 2, 2, 'COD001', '2023-05-20', 'F', '2023-09-22'),
(3, 2, 3, 'COD001', '2023-04-20', 'F', '2023-05-01'),
(4, 2, 4, 'COD002', '2023-03-20', 'F', '2023-07-20'),
(5, 3, 5, 'COD002', '2023-02-20', 'F', '2023-10-02'),
(6, 4, 6, 'COD003', '2023-01-20', 'F', '2023-03-15');


