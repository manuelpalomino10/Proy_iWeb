USE iweb_proy;

-- VISTA: GESTION DE ENCUESTADORES

-- listar encuestadores de su zona
SELECT
  u.idusuario,
  u.nombres                      AS Nombre,
  u.apellidos                    AS Apellido,
  u.correo                       AS Email,
  d.nombre                       AS Distrito,
  CASE WHEN u.estado = 1 THEN 'Activo' ELSE 'Baneado' END AS Estado
FROM usuario u
JOIN distritos d
  ON u.distritos_iddistritos = d.iddistritos
WHERE u.roles_idroles = 3
  AND d.zona_idzona = (
      SELECT zona_idzona
      FROM usuario
      WHERE idusuario = ?   -- coordinador
  )
ORDER BY u.nombres, u.apellidos;

-- banear/reactivar encuestador
UPDATE usuario
SET estado = ?                                -- 0 = baneado, 1 = activo
WHERE idusuario = ?                          -- Parámetro: ID del encuestador
  AND roles_idroles = 3;                     -- Asegura que sea encuestador
  
-- asignar formulario a un encuestador
INSERT INTO usuario_has_formulario (
  usuario_idusuario,
  formulario_idformulario,
  codigo,
  asignado_por
) VALUES (
  ?,      -- ID del encuestador
  ?,      -- ID del formulario
  UUID(), -- código único
  ?       -- ID del coordinador
);

-- Listar formularios activos que el encuestador aún no tiene asignados
SELECT
  f.idformulario,
  f.nombre           AS nombre_formulario,
  f.fecha_creacion,
  f.fecha_limite
FROM formulario f
LEFT JOIN usuario_has_formulario uhf
  ON f.idformulario = uhf.formulario_idformulario
  AND uhf.usuario_idusuario = ?   -- Parámetro: ID del encuestador
WHERE f.estado = 1                -- activo
  AND uhf.formulario_idformulario IS NULL
ORDER BY f.fecha_limite;

-- Desasignar formulario de un encuestador (sin más condiciones)
DELETE FROM usuario_has_formulario
WHERE usuario_idusuario       = ?    -- Parámetro: ID del encuestador
  AND formulario_idformulario = ?;   -- Parámetro: ID del formulario

-- VISTA: GESTION DE FORMULARIOS

-- listar todos los formularios con Nombre, Fecha (creación) y Estado
SELECT
  f.idformulario,
  f.nombre                                   AS nombre_formulario,
  DATE_FORMAT(f.fecha_creacion, '%d/%m/%Y')  AS fecha_creacion_formateada,
  DATE_FORMAT(f.fecha_limite,  '%d/%m/%Y')   AS fecha_limite_formateada,
  CASE WHEN f.estado = 1 THEN 'Activo' ELSE 'Inactivo' END AS estado
FROM formulario f
ORDER BY f.fecha_creacion DESC;
  
-- Activar / Desactivar Formularios
UPDATE formulario f
SET f.estado = ?    -- 0 = inactivo, 1 = activo
WHERE f.idformulario = ?    -- Parámetro: ID del formulario
  AND EXISTS (
        SELECT 1
        FROM usuario_has_formulario uhf
        JOIN usuario u2
          ON uhf.usuario_idusuario = u2.idusuario
        JOIN distritos d2
          ON u2.distritos_iddistritos = d2.iddistritos
        WHERE uhf.formulario_idformulario = f.idformulario
          AND d2.zona_idzona = (
              SELECT zona_idzona
              FROM usuario
              WHERE idusuario = ?    -- Parámetro: ID del coordinador
          )
  );

-- Eliminar formulario (solo si no hay respuestas asociadas)
DELETE f
FROM formulario f
WHERE f.idformulario = ?    -- Parámetro: ID del formulario
  AND NOT EXISTS (
        SELECT 1
        FROM usuario_has_formulario uhf
        JOIN respuesta r 
          ON uhf.respuesta_idrespuesta = r.idrespuesta
        WHERE uhf.formulario_idformulario = f.idformulario
  )
  AND EXISTS (
        SELECT 1
        FROM usuario_has_formulario uhf2
        JOIN usuario u2
          ON uhf2.usuario_idusuario = u2.idusuario
        JOIN distritos d2
          ON u2.distritos_iddistritos = d2.iddistritos
        WHERE uhf2.formulario_idformulario = f.idformulario
          AND d2.zona_idzona = (
              SELECT zona_idzona
              FROM usuario
              WHERE idusuario = ?    -- Parámetro: ID del coordinador
          )
  );

-- VISTA: DASHBOARD

-- Total de encuestadores en la zona
SELECT 
    COUNT(*) AS total_encuestadores
FROM usuario u
JOIN distritos d ON u.distritos_iddistritos = d.iddistritos
WHERE u.roles_idroles = 3
  AND d.zona_idzona = ?;                -- Parámetro: ID del coordinador

-- Encuestadores activos en la zona
SELECT 
    COUNT(*) AS encuestadores_activos
FROM usuario u
JOIN distritos d ON u.distritos_iddistritos = d.iddistritos
WHERE u.roles_idroles = 3
  AND u.estado = 1
  AND d.zona_idzona = ?;                -- Parámetro: ID del coordinador

-- Encuestadores baneados en la zona
SELECT 
    COUNT(*) AS encuestadores_baneados
FROM usuario u
JOIN distritos d ON u.distritos_iddistritos = d.iddistritos
WHERE u.roles_idroles = 3
  AND u.estado = 0
  AND d.zona_idzona = ?;                -- Parámetro: ID del coordinador

-- Formularios asignados (total) en la zona
SELECT 
    COUNT(*) AS formularios_asignados
FROM usuario_has_formulario uhf
JOIN usuario u ON uhf.usuario_idusuario = u.idusuario
JOIN distritos d ON u.distritos_iddistritos = d.iddistritos
WHERE d.zona_idzona = ?;                -- Parámetro: ID del coordinador

-- Porcentaje de encuestadores activos vs. baneados
SELECT
    ROUND(
      SUM(CASE WHEN u.estado = 1 THEN 1 ELSE 0 END) 
      / COUNT(*) * 100
    ,2) AS porcentaje_activos,
    ROUND(
      SUM(CASE WHEN u.estado = 0 THEN 1 ELSE 0 END)
      / COUNT(*) * 100
    ,2) AS porcentaje_baneados
FROM usuario u
JOIN distritos d ON u.distritos_iddistritos = d.iddistritos
WHERE u.roles_idroles = 3
  AND d.zona_idzona = ?;                -- Parámetro: ID del coordinador

-- Cantidad de respuestas por encuestador (barra)
SELECT
    u.idusuario,
    CONCAT(u.nombres,' ',u.apellidos) AS nombre_encuestador,
    COUNT(r.idrespuesta)              AS total_respuestas
FROM usuario u
JOIN usuario_has_formulario uhf
  ON u.idusuario = uhf.usuario_idusuario
JOIN respuesta r
  ON uhf.respuesta_idrespuesta = r.idrespuesta
JOIN distritos d
  ON u.distritos_iddistritos = d.iddistritos
WHERE u.roles_idroles = 3
  AND d.zona_idzona = ?                -- ID del coordinador
GROUP BY u.idusuario, nombre_encuestador
ORDER BY total_respuestas DESC;

-- Respuestas por formulario (puede servir para otro gráfico o tabla)
SELECT
    f.idformulario,
    f.nombre                     AS nombre_formulario,
    COUNT(r.idrespuesta)         AS total_respuestas
FROM formulario f
JOIN usuario_has_formulario uhf
  ON f.idformulario = uhf.formulario_idformulario
JOIN respuesta r
  ON uhf.respuesta_idrespuesta = r.idrespuesta
JOIN usuario u
  ON uhf.usuario_idusuario = u.idusuario
JOIN distritos d
  ON u.distritos_iddistritos = d.iddistritos
WHERE f.estado = 1
  AND d.zona_idzona = ?                -- ID del coordinador
GROUP BY f.idformulario, nombre_formulario
ORDER BY total_respuestas DESC;

-- Respuestas por distrito dentro de la zona (barra)
SELECT
    d.iddistritos,
    d.nombre                     AS nombre_distrito,
    COUNT(r.idrespuesta)         AS total_respuestas
FROM distritos d
JOIN usuario u
  ON d.iddistritos = u.distritos_iddistritos
JOIN usuario_has_formulario uhf
  ON u.idusuario = uhf.usuario_idusuario
JOIN respuesta r
  ON uhf.respuesta_idrespuesta = r.idrespuesta
WHERE d.zona_idzona = (SELECT zona_idzona FROM usuario WHERE idusuario = ?)
GROUP BY d.iddistritos, nombre_distrito
ORDER BY total_respuestas DESC;
