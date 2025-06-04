USE iweb_proy;
INSERT INTO roles (idroles,nombre) VALUES
(1, 'Administrador'), (2, 'Coordinador Interno'), (3, 'Encuestador');

INSERT INTO categoria (idcategoria,nombre) VALUES
(101, 'cat1'), (102, 'cat2'), (103, 'cat3'), (104, 'cat4');

INSERT INTO zona (idzona,nombre) VALUES
(1, 'NORTE'), (2, 'SUR'), (3, 'ESTE'), (4, 'OESTE');

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
(9, 'Carlos Alberto', 'Rodríguez Vargas', SHA2('CARV*enc3', 256), '321456987', 'carlos.rodriguez@unmujeres.org', 'Calle Los Olivos 789', 1, 3, 2, 5, '2024-05-20', NULL, 'ENC-003'),
(10, 'Rosa', 'Díaz Condezo', SHA2('R0s4D1az!', 256), '56789012', 'rosa.diaz@unmujeres.org', 'Jr. San Martín 159', 0, 3, 2, 8, '2024-11-25', NULL, 'ENC-004'),
(11, 'Jose Roberto', 'Perez Caceres', SHA2('jOrO*0', 256), '123654789', 'jose.perez@unmujeres.org', 'Calle Lima 357', 1, 3, 3, 9, '2024-08-03', NULL, 'ENC-005');

INSERT INTO formulario (idformulario,nombre,fecha_creacion,fecha_limite,estado,registros_esperados,idcategoria) VALUES
(1, 'Ficha de Necesidades Ciudadanas de Cuidados', 								'2025-01-01', '2025-08-01', 1, 150, 101),		-- ficha de registro REAL
(2, 'Encuesta de Necesidades Básicas y Servicios Esenciales para Mujeres', 		'2024-09-20', '2024-12-31', 1, 250, 101),
(3, 'Formulario de Acceso a Educación y Capacitación para Mujeres',				'2025-01-05', '2025-04-15', 1, 300, 102),
(4, 'Encuesta de Acceso Tecnológico para Mujeres', 								'2024-11-15', '2024-12-31', 1, 200, 103),
(5, 'Estudio de Necesidades Digitales en Comunidades de Mujeres', 				'2024-12-05', '2025-01-31', 1, 250,103),
(6, 'Diagnóstico de la Brecha Tecnológica en Mujeres', 							'2025-02-20', '2025-04-30', 1, 180, 103),
(7, 'Encuesta de Oportunidades Laborales Justas para Cuidadoras', 				'2024-12-06', '2025-07-15', 1, 220, 104),
(8, 'Diagnóstico de Condiciones de Vida y Trabajo para Trabajadoras del Hogar', '2025-02-01', '2025-05-01', 1, 180, 104);

INSERT INTO enc_has_formulario (idenc_has_formulario,enc_idusuario,idformulario,codigo,fecha_asignacion) VALUES
-- encuestador 7 con 7 formularios asignados
(1, 7, 1, '75318469', '2025-02-07'),
(2, 7, 2, '15864923', '2024-10-01'),
(3, 7, 3, '45983125', '2025-03-15'),
(4, 7, 4, '75318777', '2024-12-12'),
(5, 7, 5, '25697392', '2025-01-01'),
(6, 7, 6, '18971686', '2025-03-01'),
(7, 7, 7, '78461535', '2024-12-07'),
-- encuestador 8 con 5 formularios asignados
(8, 8, 1, '75318888', '2025-02-08'),
(9, 8, 2, '56789135', '2024-09-30'),
(10, 8, 3, '48593167', '2025-01-10'),
(11, 8, 7, '79516492', '2025-01-01'),
(12, 8, 8, '98416184', '2025-02-08'),
-- encuestador 9 con 4 formulario asignado
(13, 9, 1, '75318999', '2025-02-09'),
(14, 9, 4, '78631597', '2024-12-01'),
(15, 9, 5, '59357891', '2024-12-10'),
(16, 9, 6, '48618486', '2025-03-09'),
-- encuestador 10 con 3 formulario asignado
(17, 10, 1, '75318000', '2025-02-10'),
(18, 10, 2, '45932183', '2024-10-20'),
(19, 10, 3, '45641238', '2025-01-10'),
-- encuestador 11 con 3 formulario asignado
(20, 11, 1, '75318111', '2025-02-11'),
(21, 11, 7, '95637815', '2025-03-15'),
(22, 11, 8, '45664168', '2025-02-11');


INSERT INTO registro_respuestas (idregistro_respuestas, fecha_registro, estado, idenc_has_formulario) VALUES
-- encuestador 7
(1, '2025-02-11', 'C', 1), (2, '2025-02-12', 'C', 1),(3, '2025-02-13', 'B', 1), (4, '2025-02-13', 'B', 1),
(5, '2024-10-10', 'B', 2), (6, '2025-10-11', 'B', 2),
(7, '2025-04-01', 'B', 3), (8, '2025-04-04', 'B', 3),
(9, '2025-01-03', 'B', 4), (10, '2025-01-04', 'B', 4),
(11, '2025-02-01', 'B', 5), (12, '2025-02-02', 'B', 5),
(13, '2025-03-13', 'B', 6), (14, '2025-03-14', 'B', 6),
(15, '2024-12-21', 'B', 7), (16, '2024-12-22', 'B', 7),
-- encuestador 8
(17, '2025-02-16', 'C', 8), (18, '2025-02-16', 'B', 8),(19, '2025-02-17', 'B', 8),
(20, '2025-03-17', 'B', 9), (21, '2025-03-20', 'B', 9),
(22, '2025-03-17', 'B', 10), (23, '2025-03-20', 'B', 10),
(24, '2025-03-17', 'B', 11), (25, '2025-03-20', 'B', 11),
(26, '2025-03-17', 'B', 12), (27, '2025-03-20', 'B', 12),
-- encuestador 9
(28, '2025-03-01', 'C', 13), (29, '2025-03-02', 'B', 13),
(30, '2024-03-03', 'B', 14),
(31, '2024-12-26', 'B', 15),
-- encuestador 10
(32, '2025-03-10', 'C', 17),
(33, '2024-11-12', 'B', 18),
-- encuestador 11
(34, '2025-02-12', 'B', 20),
(35, '2025-03-19', 'B', 21);

INSERT INTO seccion (idseccion, nombre_sec,idformulario) VALUES
-- form 1 REAL con 5 secciones
(1, 'A. Datos de la entrevista', 1),
(2, 'B. Datos del territorio', 1),
(3, 'C. Datos de la persona entrevistada', 1),
(4, 'D. Datos sobre niñas y niños', 1),
(5, 'E. Datos sobre personas adultas mayores', 1),
-- form 2: Encuesta de Necesidades Básicas
(6, 'Información General y Demográfica', 2),
(7, 'Acceso a Servicios Básicos', 2),
(8, 'Vivienda y Saneamiento', 2),
(9, 'Prioridades y Necesidades Inmediatas', 2),
-- form 3: Acceso a Educación y Capacitación
(10, 'Datos Personales y Familiares', 3),
(11, 'Acceso a Educación Formal', 6),
(12, 'Capacitación y Habilidades', 3),
(13, 'Barreras y Oportunidades', 3),
-- form 4: Encuesta de Acceso Tecnológico
(14, 'Datos Personales', 4),
(15, 'Acceso Tecnológico', 4),
(16, 'Uso de Dispositivos', 4),
(17, 'Necesidades Específicas', 4),
-- form 5: Estudio de Necesidades Digitales
(18,  'Información Básica', 5),
(19, 'Educación y Capacitación', 5),
(20, 'Acceso a Internet', 5),
(21, 'Requerimientos Tecnológicos', 5),
-- form 6: Diagnóstico de Brecha Tecnológica
(22, 'Datos de Identidad', 6),
(23, 'Brecha Tecnológica', 6),
(24, 'Opiniones y Sugerencias', 6),
(25, 'Impacto Social', 6),
-- form 7: Oportunidades Laborales para Cuidadoras
(26, 'Perfil Laboral y Experiencia', 7),
(27, 'Condiciones de Empleo', 7),
(28, 'Derechos y Beneficios Laborales', 7),
(29, 'Expectativas y Sugerencias', 7),
-- form 8: Diagnóstico para Trabajadoras del Hogar
(30, 'Identidad y Datos Personales', 8),
(31, 'Condiciones de Trabajo', 8),
(32, 'Remuneración y Beneficios', 8),
(33, 'Satisfacción y Propuestas de Mejora', 8);

INSERT INTO pregunta (idpregunta, enunciado, tipo_dato, idseccion) VALUES
-- form 1 con 15 preguntas
(1, 'Fecha de la entrevista',               'date',     1),
(2, 'Nombres y apellidos de la persona que encuesta', 'char', 1),
	(3, 'Nombre del asentamiento humano',       'char',     2),
	(4, 'Sector',                               'char',     2),
		(5, 'Nombres y apellidos',                  'char',     3),
		(6, 'Edad',                                 'int',      3),
		(7, 'Dirección',                            'char',     3),
		(8, 'Celular (opcional)',                   'int',     3),
			(9, '¿Hay niños/niñas de 0 a 5 años en el hogar?',       'combobox', 4),
			(10, '¿Cuántos niños/niñas de 0 a 5 años hay en el hogar?', 'int',    4),
			(11, '¿Asisten a una guardería o preescolar?',           'combobox', 4),
			(12, '¿Por qué no usa guarderías o centros de cuidado?',  'combobox', 4),
				(13, '¿Hay personas adultas mayores en el hogar?', 'combobox', 5),
				(14, '¿Cuántas personas adultas mayores hay en el hogar? ', 'int', 5),
				(15, '¿Padecen alguna enfermedad? ', Default, 5),
-- form 2 con 20 preguntas
(16, 'Nombre completo', 'Default', 6),
(17, 'Edad', 'int', 6),
(18, 'Estado civil', 'combobox', 6),
(19, 'Fecha de nacimiento', 'date', 6),
(20, 'Número de dependientes', 'int', 6),
  (21, '¿Cuenta con acceso a agua potable?', 'combobox', 7),
  (22, 'Disponibilidad de electricidad', 'combobox', 7),
  (23, 'Confiabilidad del suministro eléctrico (1-10)', 'int', 7),
  (24, 'Fecha de corte del suministro', 'date', 7),
  (25, 'Calidad percibida del servicio público', 'Default', 7),
	  (26, 'Tipo de vivienda', 'combobox', 8),
	  (27, 'Condición general de la vivienda', 'Default', 8),
	  (28, 'Disponibilidad de saneamiento adecuado', 'combobox', 8),
	  (29, 'Año de construcción de la vivienda', 'int', 8),
	  (30, 'Necesidades de mejora en infraestructura', 'Default', 8),
		  (31, 'Prioridad en mejoras de servicios', 'combobox', 9),
		  (32, 'Necesidad de apoyo gubernamental', 'combobox', 9),
		  (33, 'Evaluación del nivel de riesgo en el hogar (1-10)', 'int', 9),
		  (34, 'Fecha de la última atención de servicios', 'date', 9),
		  (35, 'Comentarios adicionales sobre necesidades', 'Default', 9),
-- form 3 con 20 preguntas
(36, 'Nombre completo', 'Default', 10),
(37, 'Edad', 'int', 10),
(38, 'Número de miembros en el hogar', 'int', 10),
(39, 'Situación actual de empleo', 'combobox', 10),
(40, 'Nivel educativo alcanzado', 'combobox', 10),
  (41, '¿Está inscrita en alguna institución educativa?', 'combobox', 11),
  (42, 'Nivel de educación que cursa', 'combobox', 11),
  (43, 'Tipo de institución (pública/privada)', 'combobox', 11),
  (44, 'Año de ingreso a la institución', 'int', 11),
  (45, 'Localidad de la institución', 'Default', 11),
	  (46, '¿Ha recibido capacitación profesional?', 'combobox', 12),
	  (47, 'Área de la capacitación recibida', 'Default', 12),
	  (48, 'Duración del curso (horas)', 'int', 12),
	  (49, 'Fecha en que realizó la capacitación', 'date', 12),
	  (50, 'Nivel de utilidad de la capacitación', 'Default', 12),
		  (51, '¿Ha enfrentado barreras educativas?', 'combobox', 13),
		  (52, 'Tipo de barreras enfrentadas', 'Default', 13),
		  (53, '¿Necesita apoyo para superar barreras?', 'combobox', 13),
		  (54, 'Fecha del último apoyo educativo', 'date', 13),
		  (55, 'Observaciones sobre el acceso a educación', 'Default', 13),
-- form 4 con 20 preguntas
(56, 'Nombre completo', 'Default', 14),
(57, 'Número de Identificación', 'int', 14),
(58, 'Género', 'combobox', 14),
(59, 'Fecha de Nacimiento', 'date', 14),
(60, 'Número de Contacto', 'Default', 14),
  (61, '¿Tiene acceso a una computadora?', 'combobox', 15),
  (62, 'Tipo de conexión a internet', 'Default', 15),
  (63, 'Velocidad de internet (Mbps)', 'int', 15),
  (64, 'Fecha de última actualización del servicio', 'date', 15),
  (65, 'Nivel de satisfacción con la conectividad', 'Default', 15),
	  (66, 'Dispositivo principal utilizado', 'combobox', 16),
	  (67, 'Marca y modelo del dispositivo', 'Default', 16),
	  (68, 'Año de adquisición', 'int', 16),
	  (69, 'Fecha de compra del dispositivo', 'date', 16),
	  (70, 'Problemas técnicos frecuentes', 'Default', 16),
		  (71, '¿Necesita capacitación en tecnología?', 'combobox', 17),
		  (72, 'Tipo de capacitación solicitada', 'Default', 17),
		  (73, 'Duración preferida de la capacitación (horas)', 'int', 17),
		  (74, 'Fecha deseada para iniciar la capacitación', 'date', 17),
		  (75, 'Comentarios adicionales sobre necesidades', 'Default', 17),
-- form 5 con 20 preguntas
(76, 'Nombre completo', 'Default', 18),
(77, 'Nivel educativo', 'combobox', 18),
(78, 'Ocupación actual', 'Default', 18),
(79, 'Fecha de actualización de datos', 'date', 18),
(80, 'Dirección de residencia', 'Default', 18),
  (81, '¿Ha recibido capacitación en tecnología?', 'combobox', 19),
  (82, 'Tipo de capacitación recibida', 'Default', 19),
  (83, 'Duración del curso (horas)', 'int', 19),
  (84, 'Fecha de finalización del curso', 'date', 19),
  (85, 'Nivel de satisfacción con la capacitación', 'Default', 19),
	  (86, '¿Tiene acceso a internet en el hogar?', 'combobox', 20),
	  (87, 'Tipo de conexión (fibra, ADSL, etc.)', 'Default', 20),
	  (88, 'Velocidad de conexión (Mbps)', 'int', 20),
	  (89, 'Fecha de instalación del servicio', 'date', 20),
	  (90, 'Comentarios sobre la conectividad', 'Default', 20),
		  (91, '¿Interesada en adquirir nuevos dispositivos?', 'combobox', 21),
		  (92, 'Tipo de dispositivo requerido', 'Default', 21),
		  (93, 'Cantidad de dispositivos necesarios', 'int', 21),
		  (94, 'Fecha prevista para la adquisición', 'date', 21),
		  (95, 'Comentarios sobre requerimientos tecnológicos', 'Default', 21),
-- form 6 con 20 preguntas
(96, 'Nombre completo', 'Default', 22),
(97, 'Número de identificación', 'Default', 22),
(98, 'Nacionalidad', 'combobox', 22),
(99, 'Fecha de nacimiento', 'date', 22),
(100, 'Correo electrónico', 'Default', 22),
  (101, '¿Dispone de dispositivos tecnológicos adecuados?', 'combobox', 23),
  (102, 'Cantidad de dispositivos en uso', 'int', 23),
  (103, 'Fecha de la última actualización de dispositivos', 'date', 23),
  (104, 'Opinión sobre la brecha tecnológica', 'Default', 23),
  (105, 'Principales barreras al acceso a tecnología', 'Default', 23),
	  (106, 'Nivel de satisfacción con los servicios digitales', 'int', 24),
	  (107, 'Recomendaciones para mejoras', 'Default', 24),
	  (108, '¿Considera útil la implementación tecnológica?', 'combobox', 24),
	  (109, 'Fecha de evaluación', 'date', 24),
	  (110, 'Comentarios adicionales', 'Default', 24),
		  (111, 'Impacto del acceso digital en su vida', 'combobox', 25),
		  (112, 'Nivel de dependencia de la tecnología', 'int', 25),
		  (113, 'Fecha del último cambio tecnológico', 'date', 25),
		  (114, 'Percepción de cambios en la comunidad', 'Default', 25),
		  (115, 'Sugerencias para futuros proyectos', 'Default', 25),
-- form 7 con 20 preguntas
(116, 'Nombre completo', 'Default', 26),
(117, 'Años de experiencia laboral', 'int', 26),
(118, 'Sector de empleo principal', 'Default', 26),
(119, 'Tipo de empleo actual', 'combobox', 26),
(120, 'Capacitación laboral previa', 'combobox', 26),
  (121, 'Horas laborales semanales', 'int', 27),
  (122, 'Salario mensual aproximado', 'int', 27),
  (123, 'Descripción de las condiciones laborales', 'Default', 27),
  (124, 'Tipo de contrato laboral', 'combobox', 27),
  (125, 'Ingreso extra o compensaciones', 'Default', 27),
	  (126, '¿Cuenta con contrato formal?', 'combobox', 28),
	  (127, 'Acceso a seguridad social', 'combobox', 28),
	  (128, '¿Recibe beneficios adicionales?', 'combobox', 28),
	  (129, 'Fecha de inicio en el empleo actual', 'date', 28),
	  (130, 'Satisfacción con los derechos laborales', 'Default', 28),
		  (131, 'Expectativa salarial justa', 'int', 29),
		  (132, 'Oportunidades de crecimiento profesional', 'combobox', 29),
		  (133, '¿Necesita formación adicional?', 'combobox', 29),
		  (134, 'Fecha de evaluación de su situación laboral', 'date', 29),
		  (135, 'Sugerencias para mejorar las condiciones laborales', 'Default', 29),
-- form 8 con 20 preguntas
(136, 'Nombre completo', 'Default', 30),
(137, 'Número de identificación', 'Default', 30),
(138, 'Nacionalidad', 'combobox', 30),
(139, 'Fecha de nacimiento', 'date', 30),
(140, 'Correo electrónico', 'Default', 30),
  (141, 'Tipo de labor realizada', 'Default', 30),
  (142, 'Descripción del ambiente de trabajo', 'Default', 31),
  (143, '¿Existen jornadas excesivas?', 'combobox', 31),
  (144, 'Fecha de inicio en el trabajo actual', 'date', 31),
  (145, 'Comentarios sobre las condiciones de trabajo', 'Default', 31),
	  (146, 'Salario percibido', 'int', 32),
	  (147, 'Frecuencia de pago', 'combobox', 32),
	  (148, 'Beneficios laborales adicionales', 'Default', 32),
	  (149, 'Fecha de la última revisión salarial', 'date', 32),
	  (150, 'Satisfacción con la remuneración', 'Default', 32),
		  (151, 'Nivel de satisfacción laboral (1-10)', 'int', 33),
		  (152, '¿Considera justa la remuneración?', 'combobox', 33),
		  (153, 'Oportunidades de capacitación laboral', 'combobox', 33),
		  (154, 'Fecha de evaluación de satisfacción', 'date', 33),
		  (155, 'Propuestas para mejorar el entorno laboral', 'Default', 33);

INSERT INTO opcion_pregunta (idopcion_pregunta, opcion, idpregunta) VALUES
-- Opciones Sí/No para pregunta 9
(1, 'Sí', 9), (2, 'No', 9),
-- Opciones Sí/No para pregunta 11
(3, 'Sí', 11), (4, 'No', 11),
-- Motivos para pregunta 12
(5, 'No hay guardería cerca',     12),
(6, 'Prefiere cuidador familiar', 12),
(7, 'No puedo costear',           12),
(8, 'No confío en guarderías',    12),
(9, 'Cuidador en casa',           12),
(10, 'Otro motivo',               12),
-- Opciones Sí/No para pregunta 13
(119, 'Sí', 13), (120, 'No', 13),

-- Pregunta 18 (Estado civil en Form 2)
(11, 'Soltera', 18),
(12, 'Casada', 18),
(13, 'Divorciada', 18),
(14, 'Viuda', 18),
-- Pregunta 21 (¿Cuenta con acceso a agua potable? - Form 2)
(15, 'Sí', 21), (16, 'No', 21),
-- Pregunta 22 (Disponibilidad de electricidad - Form 2)
(17, 'Sí', 22),
(18, 'No', 22),
(19, 'Intermitente', 22),
-- Pregunta 26 (Tipo de vivienda - Form 2)
(20, 'Casa', 26),
(21, 'Departamento', 26),
(22, 'Otra', 26),
-- Pregunta 28 (Disponibilidad de saneamiento adecuado - Form 2)
(23, 'Sí', 28),
(24, 'No', 28),
(25, 'Parcialmente', 28),
-- Pregunta 31 (Prioridad en mejoras de servicios - Form 2)
(26, 'Alta', 31),
(27, 'Media', 31),
(28, 'Baja', 31),
-- Pregunta 32 (Necesidad de apoyo gubernamental - Form 2)
(29, 'Sí', 32), (30, 'No', 32),
-- Pregunta 39 (Situación actual de empleo - Form 3)
(31, 'Desempleada', 39),
(32, 'Empleada a tiempo parcial', 39),
(33, 'Empleada a tiempo completo', 39),
-- Pregunta 40 (Nivel educativo alcanzado - Form 3)
(34, 'Primaria', 40),
(35, 'Secundaria', 40),
(36, 'Preparatoria', 40),
(37, 'Universitaria', 40),
(38, 'Postgrado', 40),
-- Pregunta 41 (¿Está inscrita en alguna institución educativa? - Form 3)
(39, 'Sí', 41), (40, 'No', 41),
-- Pregunta 42 (Nivel de educación que cursa - Form 3)
(41, 'Básica', 42),
(42, 'Media', 42),
(43, 'Superior', 42),
-- Pregunta 43 (Tipo de institución (pública/privada) - Form 3)
(44, 'Pública', 43), (45, 'Privada', 43),
-- Pregunta 46 (¿Ha recibido capacitación profesional? - Form 3)
(46, 'Sí', 46), (47, 'No', 46),
-- Pregunta 51 (¿Ha enfrentado barreras educativas? - Form 3)
(48, 'Sí', 51), (49, 'No', 51),
-- Pregunta 53 (¿Necesita apoyo para superar barreras? - Form 3)
(50, 'Sí', 53), (51, 'No', 53),
-- Opciones para Formulario 4
	-- Pregunta 58: "Género"
	(52, 'Femenino', 58),
	(53, 'Masculino', 58),
	(54, 'Prefiero no decir', 58),
	-- Pregunta 61: "¿Tiene acceso a una computadora?"
	(55, 'Sí', 61),	(56, 'No', 61),
	-- Pregunta 66: "Dispositivo principal utilizado"
	(57, 'Smartphone', 66),
	(58, 'Laptop', 66),
	(59, 'Tablet', 66),
	(60, 'Otro', 66),
	-- Pregunta 71: "¿Necesita capacitación en tecnología?"
	(61, 'Sí', 71),	(62, 'No', 71),
-- Opciones para Formulario 5
	-- Pregunta 77: "Nivel educativo"
	(63, 'Primaria', 77),
	(64, 'Secundaria', 77),
	(65, 'Universitaria', 77),
	(66, 'Postgrado', 77),
	-- Pregunta 81: "¿Ha recibido capacitación en tecnología?"
	(67, 'Sí', 81),	(68, 'No', 81),
	-- Pregunta 86: "¿Tiene acceso a internet en el hogar?"
	(69, 'Sí', 86),	(70, 'No', 86),
	-- Pregunta 91: "¿Interesada en adquirir nuevos dispositivos?"
	(71, 'Sí', 91), (72, 'No', 91),
-- Opciones para Formulario 6
	-- Pregunta 98: "Nacionalidad"
	(73, 'Peruana', 98),
	(74, 'Extranjera', 98),
	(75, 'Otra', 98),
	-- Pregunta 101: "¿Dispone de dispositivos tecnológicos adecuados?"
	(76, 'Sí', 101),(77, 'No', 101),
	-- Pregunta 108: "¿Considera útil la implementación tecnológica?"
	(78, 'Muy útil', 108),
	(79, 'Útil', 108),
	(80, 'Poco útil', 108),
	(81, 'Nada útil', 108),
    -- Pregunta 111: "Impacto del acceso digital en su vida"
	(82, 'Muy alto', 111),
	(83, 'Alto', 111),
	(84, 'Moderado', 111),
	(85, 'Bajo', 111),
	(86, 'Ninguno', 111),
-- Opciones para Formulario 7
	-- Pregunta 119:
	(87, 'Tiempo completo', 119),
	(88, 'Tiempo parcial', 119),
	(89, 'Por horas', 119),
	(90, 'Eventual', 119),
	-- Pregunta 120:
    (91, 'Sí', 120), (92, 'No', 120),
	-- Pregunta 124:
    (93, 'Formal', 124),
	(94, 'Informal', 124),
	(95, 'Contrato temporal', 124),
	-- Pregunta 126:
    (96, 'Sí', 126), (97, 'No', 126),
	-- Pregunta 127:
    (98, 'Sí', 127), (99, 'No', 127),
	-- Pregunta 128:
    (100, 'Sí', 128), (101, 'No', 128),
    -- Pregunta 132:
    (121, 'Sí, cada año', 132), (122, 'Sí, pero con demasiado tiempo', 132), (123, 'No', 132),
	-- Pregunta 133:
    (102, 'Alta', 133),
	(103, 'Media', 133),
	(104, 'Baja', 133),
	-- Pregunta 134:
    (105, 'Sí', 134), (106, 'No', 134),
-- Opciones para Formulario 8
	-- Pregunta 139:
	(107, 'Peruana', 139),
	(108, 'Otra', 139),
	-- Pregunta 144:
    (109, 'Sí', 144),(110, 'No', 144),
	-- Pregunta 148:
    (111, 'Semanal', 148),
	(112, 'Quincenal', 148),
	(113, 'Mensual', 148),
	-- Pregunta 153:
    (114, 'Sí', 153), (115, 'No', 153),
	-- Pregunta 154:
    (116, 'Alta', 154),
	(117, 'Media', 154),
	(118, 'Baja', 154);

-- INSERT INTO respuesta (idrespuesta,respuesta,idpregunta,idregistro_respuestas) VALUES
-- se hace con el archivo respuestas_insert.sql

