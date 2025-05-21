USE iweb_proy;
INSERT INTO roles (idroles,nombre) VALUES
(1, 'Administrador'),
(2, 'Coordinador Interno'),
(3, 'Encuestador');

INSERT INTO categoria (idcategoria,nombre) VALUES
(101, 'cat1'),
(102, 'cat2'),
(103, 'cat3'),
(104, 'cat4');

INSERT INTO zona (idzona,nombre) VALUES
(1, 'NORTE'),
(2, 'SUR'),
(3, 'ESTE'),
(4, 'OESTE');

-- distritos completos según proyecto
INSERT INTO distritos(iddistritos,nombre,idzona) VALUES
-- NORTE
(1,'Ancon',1),(2,'Santa Rosa',1),(3,'Carabayllo',1),(4,'Puente Piedra',1),
(5,'Comas',1),(6,'Los Olivos',1),(7,'San Martin de Porres',1),(8,'Independencia',1),
-- SUR
(9,'San Juan de Miraflores',2),(10,'Villa Maria del Triunfo',2),(11,'Villa el Salvador',2),
(12,'Pachacamac',2),(13,'Lurin',2),(14,'Punta Hermosa',2),(15,'Punta Negra',2),
(16,'San Bartolo',2),(17,'Santa Maria del Mar',2),(18,'Pucusana',2),
-- ESTE
(19,'San Juan de Lurigancho',3),(20,'Lurigancho/Chosica',3),(21,'Ate',3),
(22,'El Agustino',3),(23,'Santa Anita',3),(24,'La Molina',3),(25,'Cieneguilla',3),
-- OESTE
(26,'Rimac',4),(27,'Cercado de Lima',4),(28,'Brena',4),(29,'Pueblo Libre',4),
(30,'Magdalena',4),(31,'Jesus Maria',4),(32,'La Victoria',4),(33,'Lince',4),
(34,'San Isidro',4),(35,'San Miguel',4),(36,'Surquillo',4),(37,'San Borja',4),
(38,'Santiago de Surco',4),(39,'Barranco',4),(40,'Chorrillos',4),(41,'San Luis',4),
(42,'Miraflores',4);

INSERT INTO usuario (idusuario,nombres,apellidos,contraseña,DNI,correo,direccion,estado,idroles,idzona,iddistritos,fecha_incorporacion,foto,cod_enc) VALUES
-- admins
(1, 'Ana María', 'García Cornejo', SHA2('AnaSecure#1', 256), '12345678', 'ana.garcia@unmujeres.org', NULL, 1, 1, 1, NULL, '2023-07-24', NULL, NULL),
(2, 'Lucía Yreva', 'Martínez Díaz', SHA2('AdminUN2025!', 256), '34567890', 'lucia.martinez@unmujeres.org', NULL, 1, 1, 2, NULL, '2023-09-05', NULL, NULL),
-- cordis
(3, 'Pedro Pablo', 'López García', SHA2('C0ordipdr', 256), '09876543', 'pedro.lopez@unmujeres.org', NULL, 1, 2, 1, 1, '2024-09-12', NULL, NULL),
(4, 'Luisa Fernanda', 'Torres Méndez', SHA2('Lt0rres2024', 256), '23456789', 'luisa.torres@unmujeres.org', NULL, 1, 2, 3, NULL, '2024-01-10', NULL, NULL),
(5, 'Roberto Carlos', 'Silva Castro', SHA2('Pv3ga2@UN', 256), '34567891', 'roberto.silva@unmujeres.org', NULL, 0, 2, 2, 5, '2024-03-15', NULL, NULL),
(6, 'Frank', 'Vega Leiva', SHA2('Fqv3g4q', 256), 99001122, 'frank.vega@unmujeres.org', NULL, 1, 2, 3, 7, '2025-04-01', NULL, NULL),
-- encuestadores
(7, 'Juan Carlos', 'Pérez López', SHA2('JuanP*2025', 256), '87654321', 'juan.perez@unmujeres.org', 'Av. Principal 123', 1, 3, 1, 1, '2024-01-15', NULL, 'ENC-001'),
(8, 'María Elena', 'Gómez Sánchez', SHA2('ABrDFW255', 256), '15935746', 'maria.gomez@unmujeres.org', 'Av. Primavera 456', 1, 3, 1, 2, '2024-03-10', NULL, 'ENC-002'),
(9, 'Carlos Alberto', 'Rodríguez Vargas', SHA2('CARV*enc!', 256), '321456987', 'carlos.rodriguez@unmujeres.org', 'Calle Los Olivos 789', 1, 3, 2, 5, '2024-05-20', NULL, 'ENC-003'),
(10, 'Rosa', 'Díaz Condezo', SHA2('R0s4D1az!', 256), '56789012', 'rosa.diaz@unmujeres.org', 'Jr. San Martín 159', 0, 3, 2, 8, '2024-11-25', NULL, 'ENC-004'),
(11, 'Jose Roberto', 'Perez Caceres', SHA2('jOrO*0', 256), '123654789', 'jose.perez@unmujeres.org', 'Calle Lima 357', 1, 3, 3, 9, '2024-08-03', NULL, 'ENC-005');

INSERT INTO formulario (idformulario,nombre,fecha_creacion,fecha_limite,estado,registros_esperados,idcategoria) VALUES
(1, 'Formulario 1', '2025-01-15', '2025-03-15', 1, 40, 101),
(2, 'Formulario 2', '2025-01-20', '2025-03-20', 1, 50, 102),
(3, 'Formulario 3', '2025-03-10', '2025-04-10', 0, 200, 101),
(4, 'Formulario 4', '2025-04-05', '2025-05-05', 1, 100, 103),
(5, 'Formulario 5', '2025-05-01', '2025-07-01', 1, 50, 102);

INSERT INTO enc_has_formulario (idenc_has_formulario,enc_idusuario,idformulario,codigo,fecha_asignacion) VALUES
-- encuestador 7 con 3 formularios asignados
(1, 7, 1, '75318469', '2025-01-20'),
(2, 7, 2, '15864923', '2025-01-30'),
(3, 7, 4, '45983125', '2025-05-09'),
-- encuestador 8 con 2 formularios asignados
(4, 8, 2, '56789135', '2025-01-30'),
(5, 8, 3, '48593167', '2025-03-10'),
-- encuestador 9 con 2 formulario asignado
(6, 9, 1, '78631597', '2025-05-01'),
(7, 9, 4, '59357891', '2025-05-10'),
-- encuestador 10 con 2 formulario asignado
(8, 10, 3, '45932183', '2025-03-11'),
(9, 10, 5, '45641238', '2025-05-05'),
-- encuestador 11 con 1 formulario asignado
(10, 11, 5, '95637815', '2025-05-02');

INSERT INTO registro_respuestas (idregistro_respuestas, fecha_registro, estado, idenc_has_formulario) VALUES
-- encuestador 7 con 2 registros por formulario asignado
(1, '2025-01-23', 'B', 1),
(2, '2025-01-24', 'B', 1),

(3, '2025-02-07', 'C', 2),
(4, '2025-02-08', 'C', 2),

(5, '2025-05-15', 'C', 3),
(6, '2025-05-17', 'B', 3),
-- encuestador 8 con 2 registros por formulario asignado
(7, '2025-02-03', 'C', 4),
(8, '2025-02-06', 'B', 4),

(9, '2025-03-17', 'B', 5),
(10, '2025-03-20', 'C', 5),
-- encuestador 9 con 1 registro por formulario asignado
(11, '2025-05-14', 'C', 6),
(12, '2025-05-14', 'C', 7),
-- encuestador 10 con 1 registro por formulario asignado
(13, '2025-05-17', 'B', 9),
(14, '2025-03-12', 'C', 8),
-- encuestador 11 con 3 registros por formulario asignado
(15, '2025-05-07', 'C', 10),
(16, '2025-05-08', 'C', 10),
(17, '2025-05-09', 'B', 10);

INSERT INTO seccion (idseccion, nombre_sec,idformulario) VALUES
-- form 1 con 4 secciones
(1, 'seccion A', 1),
(2, 'seccion B', 1),
(3, 'seccion C', 1),
(4, 'seccion D', 1),
-- forms con 3 secciones
(5, 'seccion A', 2),
(6, 'seccion B', 2),
(7, 'seccion C', 2),
(8, 'seccion A', 3),
(9, 'seccion B', 3),
(10, 'seccion C', 3),
(11, 'seccion A', 4),
(12, 'seccion B', 4),
(13, 'seccion C', 4),
-- form 5 con 2 secciones
(14, 'seccion A', 5),
(15, 'seccion B', 5);

INSERT INTO pregunta (idpregunta, enunciado, tipo_dato, idseccion) VALUES
-- form 1
(1, 'pregunta1_1', DEFAULT, 1),
(2, 'pregunta2_1', 'date', 1),
(3, 'pregunta3_1', DEFAULT, 1),
(4, 'pregunta1_1', DEFAULT, 2),
(5, 'pregunta2_1', 'combobox', 2),
(6, 'pregunta1_1', 'int', 3),
(7, 'pregunta2_1', DEFAULT, 3),
(8, 'pregunta1_1', 'combobox', 4),
(9, 'pregunta2_1', DEFAULT, 4),
-- form 2
(10, 'pregunta1_2', 'int', 5),
(11, 'pregunta2_2', 'date', 5),
(12, 'pregunta1_2', DEFAULT, 6),
(13, 'pregunta2_2', 'combobox', 6),
(14, 'pregunta1_2', 'combobox', 7),
(15, 'pregunta2_2', DEFAULT, 7),
(16, 'pregunta3_2', DEFAULT, 7),
-- form 3
(17, 'pregunta1_3', 'combobox', 8),
(18, 'pregunta2_3', DEFAULT, 8),
(19, 'pregunta1_3', 'int', 9),
(20, 'pregunta2_3', DEFAULT, 9),
(21, 'pregunta1_3', 'date', 10),
(22, 'pregunta2_3', DEFAULT, 10), --
-- form 4
(23, 'pregunta1_4', DEFAULT, 11),
(24, 'pregunta2_4', DEFAULT, 12),
(25, 'pregunta1_4', 'combobox', 12),
(26, 'pregunta2_4', 'int', 13),
-- form 5
(27, 'pregunta1_5', DEFAULT, 11),
(28, 'pregunta2_5', DEFAULT, 11),
(29, 'pregunta1_5', 'combobox', 12),
(30, 'pregunta2_5', 'int', 12),
(31, 'Fecha de la entrevista',               'date',     1),
(32, 'Nombres y apellidos de la persona que encuesta', 'char', 1),
(33, 'Nombre del asentamiento humano',       'char',     2),
(34, 'Sector',                               'char',     2),
(35, 'Nombres y apellidos',                  'char',     3),
(36, 'Edad',                                 'int',      3),
(37, 'Dirección',                            'char',     3),
(38, 'Celular (opcional)',                   'char',     3),
(39, '¿Hay niños/niñas de 0 a 5 años en el hogar?',       'combobox', 4),
(40, '¿Cuántos niños/niñas de 0 a 5 años hay en el hogar?', 'int',    4),
(41, '¿Asisten a una guardería o preescolar?',           'combobox', 4),
(42, '¿Por qué no usa guarderías o centros de cuidado?',  'combobox', 4);


INSERT INTO opcion_pregunta (idopcion_pregunta, opcion, idpregunta) VALUES
(1,  'opcion1_p5', 5),
(2,  'opcion2_p5', 5),
(3,  'opcion3_p5', 5),
(4,  'opcion1_p8', 8),
(5,  'opcion2_p8', 8),
(6,  'opcion3_p8', 8),
(7,  'opcion1_p13', 13),
(8,  'opcion2_p13', 13),
(9,  'opcion1_p14', 14),
(10, 'opcion2_p14', 14),
(11, 'opcion1_p17', 17),
(12, 'opcion2_p17', 17),
(13, 'opcion1_p25', 25),
(14, 'opcion2_p25', 25),
(15, 'opcion1_p29', 29),
(16, 'opcion2_p29', 29),

-- Opciones Sí/No para pregunta 39
(17, 'Sí', 39),
(18, 'No', 39),

-- Opciones Sí/No para pregunta 41
(19, 'Sí', 41),
(20, 'No', 41),

-- Motivos para pregunta 42
(21, 'No hay guardería cerca',             42),
(22, 'Prefiere cuidador familiar',          42),
(23, 'No puedo costear',                    42),
(24, 'No confío en guarderías',             42),
(25, 'Cuidador en casa',                    42),
(26, 'Otro motivo',                         42);


INSERT INTO respuesta (idrespuesta,respuesta,idpregunta,idregistro_respuestas) VALUES
-- form 1 con 3 registros
(1, 'f1_in1_respuesta1', 1, 1),		-- borrador de enc 7
(2, '07/01/2025', 2, 1), -- 'f1_in1_respuesta2'  tipo date
(3, 'f1_in1_respuesta3', 3, 1),
(4, 'f1_in1_respuesta4', 4, 1),
(5, 'opcion1_p5', 5, 1), -- 'f1_in1_respuesta5' tipo combobox
(6, 116, 6, 1), -- 'f1_in1_respuesta6' tipo int
(7, 'f1_in1_respuesta7', 7, 1),
(8, null, 8, 1), -- (8, 'f1_in1_respuesta8', 8, 1),	tipo combobox 
(9, null, 9, 1), -- (9, 'f1_in1_respuesta9', 9, 1),	-- respuesta vacia
(10, 'f1_in2_respuesta1', 1, 2),		-- borrador de enc 7
(11, '2025-02-07', 2, 2), -- 'f1_in2_respuesta2' tipo date
(12, 'f1_in2_respuesta3', 3, 2),
(13, 'f1_in2_respuesta4', 4, 2),
(14, null, 5, 2), -- (14, 'f1_in2_respuesta5', 5, 2),  tipo combobox
(15, null, 6, 2), -- (15, 'f1_in2_respuesta6', 6, 2),  tipo int 
(16, 'f1_in2_respuesta7', 7, 2),
(17, null, 8, 2), -- (17, 'f1_in2_respuesta8', 8, 2),  tipo combobox
(18, 'f1_in2_respuesta9', 9, 2),
(19, 'f1_in3_respuesta1', 1, 11),		-- completado de enc 9
(20, '2025-01-09', 2, 11), -- 'f1_in3_respuesta2' tipo date
(21, 'f1_in3_respuesta3', 3, 11),
(22, 'f1_in3_respuesta4', 4, 11),
(23, 'opcion1_p5', 5, 11), -- tipo combobox
(24, 136, 6, 11), -- 'f1_in3_respuesta6'  tipo int
(25, 'f1_in3_respuesta7', 7, 11),
(26, 'opcion1_p8', 8, 11), --  tipo combobox
(27, 'f1_in3_respuesta9', 9, 11),
-- form 2 con 4 registros
(28, 211, 10, 3), --  tipo int 	-- completado de enc 7
(29, '2024-01-07', 11, 3), --  tipo date
(30, 'f2_in1_respuesta3', 12, 3),
(31, 'opcion2_p13', 13, 3),	--  tipo combobox
(32, 'opcion2_p14', 14, 3),	--  tipo combobox
(33, 'f2_in1_respuesta6', 15, 3),
(34, 'f2_in1_respuesta7', 16, 3),
(35, 221, 10, 4), --  tipo int	-- completado de enc 7
(36, '2024-02-07', 11, 4), --  tipo date
(37, 'f2_in2_respuesta3', 12, 4),
(38, 'opcion2_p13', 13, 4),	--  tipo combobox
(39, 'opcion1_p14', 14, 4),	--  tipo combobox
(40, 'f2_in2_respuesta6', 15, 4),
(41, 'f2_in2_respuesta7', 16, 4),
(42, 231, 10, 7), --  tipo int	-- completado de enc 8
(43, '2024-01-08', 11, 7), --  tipo date
(44, 'f2_in3_respuesta3', 12, 7),
(45, 'opcion1_p13', 13, 7),	--  tipo combobox
(46, 'opcion1_p14', 14, 7),	--  tipo combobox
(47, 'f2_in3_respuesta6', 15, 7),
(48, 'f2_in3_respuesta7', 16, 7),
(49, 241, 10, 8), --  tipo int		-- borrador de enc 8
(50, null, 11, 8),	-- 'f2_in4_respuesta2' respuesta vacia --  tipo date
(51, 'f2_in4_respuesta3', 12, 8),
(52, 'f2_in4_respuesta4', 13, 8),
(53, 'f2_in4_respuesta5', 14, 8),	
(54, null, 15, 8),	-- 'f2_in4_respuesta6'  respuesta vacia
(55, 'f2_in4_respuesta7', 16, 8),
-- form 3 con 3 registros
(56, 'opcion1_p17', 17, 9),		-- borrador de enc 8
(57, 'f3_in1_respuesta2', 18, 9),
(58, 313, 19, 9), --  tipo int
(59, 'f3_in1_respuesta4', 20, 9),
(60, '2023-01-08', 21, 9), --  tipo date
(61, null, 22, 9),	--  'f3_in1_respuesta6'  respuesta vacia
(62, 'opcion1_p17', 17, 10),		-- completado de enc 8
(63, 'f3_in2_respuesta2', 18, 10),
(64, 323, 19, 10),
(65, 'f3_in2_respuesta4', 20, 10),
(66, '2023-02-08', 21, 10), --  tipo date
(67, 'f3_in2_respuesta6', 22, 10),
(68, 'opcion1_p17', 17, 14),		-- completado de enc 10
(69, 'f3_in3_respuesta2', 18, 14),
(70, 333, 19, 14),
(71, 'f3_in3_respuesta4', 20, 14),
(72, '2023-01-10', 21, 14), --  tipo date
(73, 'f3_in3_respuesta6', 22, 14),
-- form 4 con 3 registros
(74, 'f4_in1_respuesta1', 23, 5),		-- completado de enc 7
(75, 'f4_in1_respuesta2', 24, 5),
(76, 'opcion1_p25', 25, 5), --  tipo combobox
(77, 414, 26, 5), --  tipo int
(78, 'f4_in2_respuesta1', 23, 6),		-- borrador de enc 7
(79, 'f4_in2_respuesta2', 24, 6),
(80, null, 25, 6),	-- 'f4_in2_respuesta3'  respuesta vacia --  tipo combobox
(81, null, 26, 6),	-- 'f4_in2_respuesta4'  respuesta vacia
(82, 'f4_in3_respuesta1', 23, 12),		-- completado de enc 7
(83, 'f4_in3_respuesta2', 24, 12),
(84, 'opcion1_p25', 25, 12), --  tipo combobox
(85, 434, 26, 12), --  tipo int
-- form 5 con 4 registros
(86, 'f5_in1_respuesta1', 27, 13),		-- completado de enc 10
(87, 'f5_in1_respuesta2', 28, 13),
(88, 'opcion2_p29', 29, 13), --  tipo combobox
(89, 514, 30, 13), --  tipo int
(90, 'f5_in2_respuesta1', 27, 15),		-- completado de enc 11
(91, 'f5_in2_respuesta2', 28, 15),
(92, 'opcion1_p29', 29, 15), --  tipo combobox
(93, 524, 30, 15), --  tipo int
(94, 'f5_in3_respuesta1', 27, 16),		-- completado de enc 11
(95, 'f5_in3_respuesta2', 28, 16),
(96, 'opcion2_p29', 29, 16), --  tipo combobox
(97, 534, 30, 16), --  tipo int
(98, 'f5_in4_respuesta1', 27, 17),		-- borrador de enc 11
(99, 'f5_in4_respuesta2', 28, 17),
(100, null, 29, 17),  -- 'f5_in4_respuesta3'  respuesta vacia  tipo combobox
(101, 544, 30, 17); --  tipo int


