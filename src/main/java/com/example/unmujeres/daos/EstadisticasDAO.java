package com.example.unmujeres.daos;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EstadisticasDAO extends BaseDAO {

    //------------------------ESTADISTICAS PARA ENCUESTADOR ------------------------------

    //--------------------- PRIMER CRUD ---------------------------------------
    public int contarFormulariosAsignados(int idusuario) throws SQLException {
        String sql = "SELECT COUNT(f.idformulario) " +
                "FROM iweb_proy.formulario f " +
                "JOIN iweb_proy.enc_has_formulario ehf ON f.idformulario = ehf.idformulario " +
                "JOIN iweb_proy.categoria c ON f.idcategoria = c.idcategoria " +
                "WHERE ehf.enc_idusuario = ?";

        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idusuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        }
    }

    //--------------------- SEGUNDO CRUD ---------------------------------------
    public Map<String, Object> calcularAvance(int idUsuario) throws SQLException {
        String sql = "SELECT f.nombre AS nombre_formulario, " +
                "SUM(CASE WHEN rr.fecha_registro >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) THEN 1 ELSE 0 END) AS completados_semana, " +
                "SUM(CASE WHEN rr.fecha_registro >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) THEN 1 ELSE 0 END) AS completados_mes " +
                "FROM iweb_proy.formulario f " +
                "JOIN iweb_proy.enc_has_formulario ehf ON ehf.idformulario = f.idformulario " +
                "JOIN iweb_proy.registro_respuestas rr ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "WHERE rr.estado = 'C' AND ehf.enc_idusuario = ? " +
                "GROUP BY f.idformulario, f.nombre";

        Map<String, int[]> datosPorFormulario = new HashMap<>();
        int totalCompletadosSemanaGeneral = 0;
        int totalCompletadosMesGeneral = 0;

        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String nombreFormulario = rs.getString("nombre_formulario");
                    int completadosSemana = rs.getInt("completados_semana");
                    int completadosMes = rs.getInt("completados_mes");

                    datosPorFormulario.put(nombreFormulario, new int[]{completadosSemana, completadosMes});
                    totalCompletadosSemanaGeneral += completadosSemana;
                    totalCompletadosMesGeneral += completadosMes;
                }
            }
        }

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("detallePorFormulario", datosPorFormulario);
        resultado.put("totalesGenerales", new int[]{totalCompletadosSemanaGeneral, totalCompletadosMesGeneral});
        return resultado;
    }

    //--------------------- TERCER CRUD ---------------------------------------
    public int contarBorradores(int idUsuario) throws SQLException {
        String sql = "SELECT COUNT(*) FROM iweb_proy.registro_respuestas rr " +
                "INNER JOIN iweb_proy.enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "WHERE estado = 'B' AND ehf.enc_idusuario = ?";;

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("Total borradores: " + count);
                    return count;
                }
            }
            return 0;
        }
    }

    //--------------------- CUARTO CRUD ---------------------------------------
    public int contarFormulariosCompletados(int idUsuario) throws SQLException {
        String sql = "SELECT COUNT(*) FROM iweb_proy.enc_has_formulario ehf " +
                "JOIN iweb_proy.registro_respuestas rr ON ehf.idenc_has_formulario = rr.idenc_has_formulario " +
                "WHERE rr.estado = 'C' AND ehf.enc_idusuario = ?" ;
        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    //--------------------- QUINTO CRUD ---------------------------------------
    public int contarCompletadosUltimos7Dias() throws SQLException {
        String sql = "SELECT COUNT(*) FROM iweb_proy.registro_respuestas " +
                "WHERE estado = 'C' AND fecha_registro >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)";
        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    //--------------------- SEXTO CRUD ---------------------------------------
    public Map<String, Integer> obtenerRespuestasCompletadasPorFormulario(int idusuario) throws SQLException {
        String sql = "SELECT f.nombre AS nombre_formulario, " +
                "COUNT(rr.idregistro_respuestas) AS respuestas_completadas " +
                "FROM iweb_proy.formulario f " +
                "JOIN iweb_proy.enc_has_formulario ehf ON ehf.idformulario = f.idformulario " +
                "JOIN iweb_proy.registro_respuestas rr ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "WHERE rr.estado = 'C' AND ehf.enc_idusuario = ? " +
                "GROUP BY f.idformulario, f.nombre";

        Map<String, Integer> resultados = new HashMap<>();

        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idusuario); // Corregido: Usar el parámetro idusuario
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String nombreFormulario = rs.getString("nombre_formulario");
                    int respuestasCompletadas = rs.getInt("respuestas_completadas");
                    System.out.println("DAO - Leyendo del ResultSet - Formulario: [" + nombreFormulario + "], Completados: [" + respuestasCompletadas + "]");
                    resultados.put(nombreFormulario, respuestasCompletadas);
                }
            }
            System.out.println("Total formularios con respuestas completadas para el usuario " + idusuario + ": " + resultados.size());
            return resultados;
        }
    }

    //--------------------- SÉPTIMO CRUD ---------------------------------------
    public String obtenerUltimoFormularioRegistrado() throws SQLException {
        String sql = "SELECT nombre FROM iweb_proy.formulario ORDER BY fecha_creacion DESC LIMIT 1";
        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("nombre");
            }
            return "Sin registros";
        }
    }

    //--------------------- OCTAVO CRUD ---------------------------------------
    public int contarFormulariosPorVencerPronto() throws SQLException {
        String sql = "SELECT COUNT(*) FROM iweb_proy.formulario WHERE fecha_limite BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 3 DAY)";
        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    //--------------------- NOVENO CRUD ---------------------------------------
    public int contarTotalRespuestasRegistradas(int encIdUsuario) throws SQLException {
        String sql = "SELECT COUNT(*) " +
                "FROM respuesta r " +
                "INNER JOIN registro_respuestas reg " +
                "    ON r.idregistro_respuestas = reg.idregistro_respuestas " +
                "INNER JOIN enc_has_formulario ehf " +
                "    ON reg.idenc_has_formulario = ehf.idenc_has_formulario " +
                "WHERE ehf.enc_idusuario = ?";

        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, encIdUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return 0;
        }
    }

    //--------------------- DÉCIMO CRUD ---------------------------------------
    public int contarFormulariosAsignadosHoy() throws SQLException {
        String sql = "SELECT COUNT(*) FROM iweb_proy.enc_has_formulario WHERE fecha_asignacion = CURDATE()";
        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
    //------------------ESTADISTICAS PARA ADMIN ---------------------------------------------------
    // Método para obtener total de usuarios (roles 1 y 2)
    public int getTotalUsuarios() throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE idroles IN (1,2)";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // Método para coordinadores activos (rol 1)
    public int getCoordinadoresActivos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE idroles = 2 AND estado = 1";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // Método para encuestadores activos (rol 2)
    public int getEncuestadoresActivos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE idroles = 3 AND estado = 1";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // Obtener respuestas por zona
    public Map<String, Integer> getRespuestasPorZona() throws SQLException {
        String sql = "SELECT z.nombre, COUNT(rr.idregistro_respuestas) AS total " +
                "FROM zona z " +
                "JOIN distritos d ON z.idzona = d.idzona " +
                "JOIN usuario u ON d.iddistritos = u.iddistritos " +
                "JOIN enc_has_formulario ehf ON u.idusuario = ehf.enc_idusuario " +
                "JOIN registro_respuestas rr ON ehf.idenc_has_formulario = rr.idenc_has_formulario " +
                "GROUP BY z.nombre";

        Map<String, Integer> resultados = new HashMap<>();
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultados.put(rs.getString("nombre"), rs.getInt("total"));
            }
        }
        return resultados;
    }

    // Distribución de usuarios (coordinadores vs encuestadores)
    public Map<String, Integer> getDistribucionUsuarios() throws SQLException {
        String sql = "SELECT " +
                "  SUM(CASE WHEN idroles = 2 THEN 1 ELSE 0 END) AS coordinadores, " +
                "  SUM(CASE WHEN idroles = 3 THEN 1 ELSE 0 END) AS encuestadores " +
                "FROM usuario WHERE estado = 1";

        Map<String, Integer> distribucion = new HashMap<>();
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                distribucion.put("coordinadores", rs.getInt("coordinadores"));
                distribucion.put("encuestadores", rs.getInt("encuestadores"));
            }
        }
        return distribucion;
    }


    // 1. Formularios activos
    public int getFormulariosActivos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM formulario WHERE estado = 1";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // 2. Promedio de respuestas por encuestador
    public double getPromedioRespuestasPorEncuestador() throws SQLException {
        String sql = "SELECT AVG(total_respuestas) FROM (" +
                "   SELECT ehf.enc_idusuario, COUNT(*) AS total_respuestas " +
                "   FROM registro_respuestas rr " +
                "   JOIN enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "   GROUP BY ehf.enc_idusuario" +
                ") AS conteo";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0.0;
        }
    }


    // 3. Usuarios desactivados
    public int getUsuariosDesactivados() throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE estado = 0 AND idroles IN (1,2)";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // 4. Zona con más respuestas (últimos 30 días)
    public String getZonaConMasRespuestas() throws SQLException {
        String sql = "SELECT z.nombre " +
                "FROM zona z " +
                "JOIN distritos d ON z.idzona = d.idzona " +
                "JOIN usuario u ON d.iddistritos = u.iddistritos " +
                "JOIN enc_has_formulario ehf ON u.idusuario = ehf.enc_idusuario " +
                "JOIN registro_respuestas rr ON ehf.idenc_has_formulario = rr.idenc_has_formulario " +
                "WHERE rr.fecha_registro >= CURDATE() - INTERVAL 30 DAY " +
                "GROUP BY z.nombre " +
                "ORDER BY COUNT(*) DESC " +
                "LIMIT 1";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getString(1) : "Ninguna";
        }
    }

    // 5. Respuestas por zona (últimos 30 días)
    public Map<String, Integer> getRespuestasPorZona30Dias() throws SQLException {
        String sql = "SELECT z.nombre, COUNT(rr.idregistro_respuestas) AS total " +
                "FROM zona z " +
                "JOIN distritos d ON z.idzona = d.idzona " +
                "JOIN usuario u ON d.iddistritos = u.iddistritos " +
                "JOIN enc_has_formulario ehf ON u.idusuario = ehf.enc_idusuario " +
                "JOIN registro_respuestas rr ON ehf.idenc_has_formulario = rr.idenc_has_formulario " +
                "WHERE rr.fecha_registro >= CURDATE() - INTERVAL 30 DAY " +
                "GROUP BY z.nombre";
        return ejecutarConsultaMapa(sql);
    }

    // 6. Respuestas por día (última semana)
    public Map<String, Integer> getRespuestasUltimaSemana() throws SQLException {
        String sql = "SELECT DATE_FORMAT(fecha_registro, '%Y-%m-%d') AS dia, COUNT(*) AS total " +
                "FROM registro_respuestas " +
                "WHERE fecha_registro >= CURDATE() - INTERVAL 7 DAY " +
                "GROUP BY dia " +
                "ORDER BY dia";
        return ejecutarConsultaMapa(sql);
    }

    // 7. Progreso de formularios por zona (%)
    public Map<String, Double> getProgresoFormulariosPorZona() throws SQLException {
        String sql = "SELECT z.nombre, " +
                "   (COUNT(rr.idregistro_respuestas) * 100.0 / COUNT(ehf.idenc_has_formulario)) AS porcentaje " +
                "FROM zona z " +
                "JOIN distritos d ON z.idzona = d.idzona " +
                "JOIN usuario u ON d.iddistritos = u.iddistritos " +
                "JOIN enc_has_formulario ehf ON u.idusuario = ehf.enc_idusuario " +
                "LEFT JOIN registro_respuestas rr ON ehf.idenc_has_formulario = rr.idenc_has_formulario " +
                "GROUP BY z.nombre";

        Map<String, Double> resultados = new HashMap<>();
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultados.put(rs.getString("nombre"), rs.getDouble("porcentaje"));
            }
        }
        return resultados;
    }

    // Método auxiliar para ejecutar consultas que devuelven mapas clave-valor
    private Map<String, Integer> ejecutarConsultaMapa(String sql) throws SQLException {
        Map<String, Integer> resultados = new HashMap<>();
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultados.put(rs.getString(1), rs.getInt(2));
            }
        }
        return resultados;
    }



    // 8. Total de respuestas registradas
    public int getTotalRespuestas() throws SQLException {
        String sql = "SELECT COUNT(*) FROM registro_respuestas";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    //-------------------------COORDINADOR --------------------------------------------------------

    // 1. Total de encuestadores en la zona del coordinador

    public int getTotalEncuestadoresCoordinador(int idCoordinador) throws SQLException {
        String sql = "SELECT COUNT(*) " +
                "FROM usuario u " +
                "WHERE u.idroles = 3 AND u.idzona = (SELECT idzona FROM usuario WHERE idusuario = ?)";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCoordinador);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("[DEBUG] getTotalEncuestadoresCoordinador - idCoordinador: " + idCoordinador + ", Total: " + count);
                    return count;
                }
                System.out.println("[DEBUG] getTotalEncuestadoresCoordinador - idCoordinador: " + idCoordinador + ", No results");
                return 0;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] getTotalEncuestadoresCoordinador - idCoordinador: " + idCoordinador + ", Error: " + e.getMessage());
            throw e;
        }
    }

    // 2. Encuestadores activos en la zona del coordinador
    public int getEncuestadoresActivosCoordinador(int idCoordinador) throws SQLException {
        String sql = "SELECT COUNT(*) " +
                "FROM usuario u " +
                "WHERE u.idroles = 3 AND u.estado = 1 AND u.idzona = (SELECT idzona FROM usuario WHERE idusuario = ?)";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCoordinador);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    // 3. Encuestadores baneados en la zona del coordinador
    public int getEncuestadoresBaneadosCoordinador(int idCoordinador) throws SQLException {
        String sql = "SELECT COUNT(*) " +
                "FROM usuario u " +
                "WHERE u.idroles = 3 AND u.estado = 0 AND u.idzona = (SELECT idzona FROM usuario WHERE idusuario = ?)";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCoordinador);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    // 4. Formularios asignados a encuestadores en la zona del coordinador
    public int getFormulariosAsignadosCoordinador(int idCoordinador) throws SQLException {
        String sql = "SELECT COUNT(*) " +
                "FROM enc_has_formulario ehf " +
                "JOIN usuario u ON ehf.enc_idusuario = u.idusuario " +
                "WHERE u.idroles = 3 AND u.idzona = (SELECT idzona FROM usuario WHERE idusuario = ?)";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCoordinador);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    // 5. Respuestas por zona para el coordinador (Gráfico de Área)
    public Map<String, Integer> getRespuestasPorZonaCoordinador(int idCoordinador) throws SQLException {
        Map<String, Integer> resultados = new HashMap<>();
        String query = "SELECT z.nombre, COUNT(DISTINCT ehf.idenc_has_formulario) AS total " +
                "FROM zona z " +
                "JOIN distritos d ON z.idzona = d.idzona " +
                "JOIN usuario u ON d.iddistritos = u.iddistritos " +
                "JOIN enc_has_formulario ehf ON u.idusuario = ehf.enc_idusuario " +
                "JOIN registro_respuestas rr ON ehf.idenc_has_formulario = rr.idenc_has_formulario " +
                "WHERE rr.estado = 'C' AND u.idroles = 3 " +
                "GROUP BY z.nombre";

        try (Connection conn = this.getConnection();PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                String zona = resultSet.getString("nombre");
                int total = resultSet.getInt("total");
                resultados.put(zona, total);
            }
        }
        System.out.println("[DEBUG] getRespuestasPorZonaCoordinador - idCoordinador: " + idCoordinador + ", Resultados: " + resultados);
        return resultados;
    }

    // 6. Porcentaje de encuestadores activos vs baneados para el coordinador (Gráfico de Torta)
    public Map<String, Double> getPorcentajeEncuestadoresActivosVsBaneadosCoordinador(int idCoordinador) throws SQLException {
        String sql = "SELECT " +
                "  SUM(CASE WHEN u.estado = 1 THEN 1 ELSE 0 END) AS activos, " +
                "  SUM(CASE WHEN u.estado = 0 THEN 1 ELSE 0 END) AS baneados, " +
                "  COUNT(*) AS total " +
                "FROM usuario u " +
                "WHERE u.idroles = 3 AND u.idzona = (SELECT idzona FROM usuario WHERE idusuario = ?)";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCoordinador);
            try (ResultSet rs = stmt.executeQuery()) {
                Map<String, Double> resultados = new HashMap<>();
                if (rs.next()) {
                    int activos = rs.getInt("activos");
                    int baneados = rs.getInt("baneados");
                    int total = rs.getInt("total");
                    double porcentajeActivos = total > 0 ? (activos * 100.0 / total) : 0.0;
                    double porcentajeBaneados = total > 0 ? (baneados * 100.0 / total) : 0.0;
                    resultados.put("activos", porcentajeActivos);
                    resultados.put("baneados", porcentajeBaneados);
                }
                return resultados;
            }
        }
    }







}

