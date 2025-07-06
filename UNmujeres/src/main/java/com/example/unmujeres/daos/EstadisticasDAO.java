package com.example.unmujeres.daos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class EstadisticasDAO extends BaseDAO {

    //------------------------ESTADISTICAS PARA ENCUESTADOR ------------------------------

    //--------------------- PRIMER CARD - FORMULARIOS DISPONIBLES PARA TI ---------------------------------------
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

    //--------------------- SEGUNDO GRAFICO  ---------------------------------------
    public Map<String, Integer> obtenerRespuestasCompletadasPorDia(int idUsuario) throws SQLException {
        String sql = "SELECT DATE(rr.fecha_registro) AS fecha, COUNT(*) AS cantidad " +
                "FROM iweb_proy.registro_respuestas rr " +
                "JOIN iweb_proy.enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "WHERE rr.estado = 'C' AND ehf.enc_idusuario = ? " +
                "AND rr.fecha_registro BETWEEN DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY) " +
                "AND DATE_ADD(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 6 DAY) " +
                "GROUP BY DATE(rr.fecha_registro) " +
                "ORDER BY fecha ASC";

        Map<String, Integer> resultados = new LinkedHashMap<>();
        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String fecha = rs.getString("fecha");
                    int cantidad = rs.getInt("cantidad");
                    resultados.put(fecha, cantidad);
                }
            }
        }
        return resultados;
    }



    //--------------------- TERCER CARD -  FORMULARIOS GUARDADOS COMO BORRADOR ---------------------------------------
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

    //--------------------- SEGUNDO CARD - FORMULARIOS COMPLETADOS ESTA SEMANA  ---------------------------------------
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

    //--------------------- PRIMER GRAFICO  : CANTIDAD DE RESPUESTAS POR FORMULARIO  ---------------------------------------
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
            stmt.setInt(1, idusuario);
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

    //--------------------- CUARTO CARD - ULTIMO FORMULARIO ASIGNADO ---------------------------------------
    public String obtenerUltimoFormularioRegistrado(int idusuario) throws SQLException {
        String sql = "SELECT f.nombre \n" +
                "                 FROM iweb_proy.formulario f \n" +
                "                 JOIN iweb_proy.enc_has_formulario ehf ON f.idformulario = ehf.idformulario \n" +
                "                 JOIN iweb_proy.registro_respuestas rr ON rr.idenc_has_formulario = ehf.idenc_has_formulario \n" +
                "                 WHERE ehf.enc_idusuario = ? AND rr.estado = 'C' \n" +
                "                 ORDER BY rr.fecha_registro DESC \n" +
                "                 LIMIT 1";
        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idusuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nombre");
                } else {
                    return "Sin registros";
                }
            }
        }
    }

    //--------------------- QUINTO CARD - FORMULARIOS POR VENCER PRONTO ---------------------------------------
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

    //--------------------- SEXTO CARD - TOTAL DE RESPUESTAS REGISTRADAS  ---------------------------------------
    public int contarTotalRespuestasRegistradas(int encIdUsuario) throws SQLException {
        String sql = "SELECT COUNT(*)\n" +
                "FROM registro_respuestas reg\n" +
                "INNER JOIN enc_has_formulario ehf\n" +
                "  ON reg.idenc_has_formulario = ehf.idenc_has_formulario\n" +
                "WHERE ehf.enc_idusuario = ?\n";

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

    //--------------------- SEPTIMO CARD - RESPUESTAS COMPLETADAS HOY  ---------------------------------------
    public int contarRespuestasCompletadasHoy(int idUsuario) throws SQLException {
        String sql = "SELECT COUNT(*) " +
                "FROM iweb_proy.registro_respuestas rr " +
                "JOIN iweb_proy.enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "WHERE rr.estado = 'C' AND ehf.enc_idusuario = ? AND DATE(rr.fecha_registro) = CURDATE()";

        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }


    // GRAFICOS  DE  FORMULARIOS POR ZONA
    public Map<String, Integer> obtenerFormulariosPorZona(int idUsuario) throws SQLException {
        Map<String, Integer> resultado = new LinkedHashMap<>();
        String sql = """
        SELECT\s
            z.nombre AS nombre_zona,
            COUNT(rr.idregistro_respuestas) AS total
        FROM iweb_proy.registro_respuestas rr
        JOIN iweb_proy.enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario
        JOIN iweb_proy.usuario e ON ehf.enc_idusuario = e.idusuario
        JOIN iweb_proy.zona z ON e.idzona = z.idzona
        WHERE e.idusuario = ?
        GROUP BY z.nombre
        ORDER BY total DESC;
        
    """;

        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String zona = rs.getString("nombre_zona");
                    int cantidad = rs.getInt("total");
                    resultado.put(zona, cantidad);
                }
            }
        }
        return resultado;
    }

    // GRAFICO PARA FORMULAIROS COMPLETADAS VS BORRADORES
    public Map<String, Integer> obtenerCantidadPorEstado(int idEncuestador) {
        Map<String, Integer> resultado = new HashMap<>();

        String sql = "SELECT rr.estado, COUNT(*) AS total " +
                "FROM registro_respuestas rr " +
                "JOIN enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "WHERE ehf.enc_idusuario = ? " +
                "GROUP BY rr.estado";

        try  (Connection con = this.getConnection();
              PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idEncuestador);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String estado = rs.getString("estado");
                int total = rs.getInt("total");
                resultado.put(estado, total);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }



    //------------------ESTADISTICAS PARA ADMIN ---------------------------------------------------
    // Metodo para obtener total de usuarios (roles 1 y 2)
    public int getTotalUsuarios() throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE idroles IN (3,2)";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // Metodo para coordinadores activos (rol 1)
    public int getCoordinadoresActivos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE idroles = 2 AND estado = 1";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // Metodo para encuestadores activos (rol 2)
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
        String sql = "SELECT COUNT(*) FROM usuario WHERE estado = 0 AND idroles IN (3,2)";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // 4. Zona con más respuestas (últimos 30 días)
    public String getZonaConMasRespuestas() throws SQLException {
        String sql = "SELECT z.nombre\n" +
                "FROM zona z\n" +
                "JOIN distritos d ON z.idzona = d.idzona \n" +
                "JOIN usuario u ON d.iddistritos = u.iddistritos \n" +
                "JOIN enc_has_formulario ehf ON u.idusuario = ehf.enc_idusuario \n" +
                "JOIN registro_respuestas rr ON ehf.idenc_has_formulario = rr.idenc_has_formulario \n" +
                "GROUP BY z.nombre \n" +
                "ORDER BY COUNT(*) DESC \n" +
                "LIMIT 1;\n";
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

    // Metodo auxiliar para ejecutar consultas que devuelven mapas clave-valor
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


    public Map<String, Map<String, Integer>> getRespuestasPorZonaEstado() {
        Map<String, Map<String, Integer>> resultados = new HashMap<>();
        String sql = "SELECT z.nombre AS zona, rr.estado, COUNT(*) AS total " +
                "FROM registro_respuestas rr " +
                "JOIN enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "JOIN usuario u ON ehf.enc_idusuario = u.idusuario " +
                "JOIN zona z ON u.idzona = z.idzona " +
                "GROUP BY z.nombre, rr.estado";
        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String zona = rs.getString("zona");
                String estado = rs.getString("estado"); // 'C' o 'B'
                int total = rs.getInt("total");

                resultados.computeIfAbsent(zona, k -> new HashMap<>()).put(estado, total);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultados;
    }



    // 2. Gráfico: Distribución de formularios por categoría
    public Map<String, Integer> getTopEncuestadores() throws SQLException {
        String sql = "SELECT CONCAT(u.nombres, ' ', u.apellidos) AS encuestador, " +
                "COUNT(rr.idregistro_respuestas) AS total_respuestas " +
                "FROM usuario u " +
                "JOIN enc_has_formulario ehf ON u.idusuario = ehf.enc_idusuario " +
                "JOIN registro_respuestas rr ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "WHERE u.idroles = 3 AND rr.estado = 'C' " +
                "GROUP BY u.idusuario " +
                "ORDER BY total_respuestas DESC " +
                "LIMIT 5";

        Map<String, Integer> resultados = new LinkedHashMap<>();
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultados.put(rs.getString("encuestador"), rs.getInt("total_respuestas"));
            }
        }
        return resultados;
    }








    //-------------------------COORDINADOR --------------------------------------------------------


    // PRIMERA CARD:  Total de encuestadores en la zona del coordinador
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

    // SEGUNDA CARD: Encuestadores activos en la zona del coordinador
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

    // TERCERA CARD: Encuestadores baneados en la zona del coordinador
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

    // CUARTA CARD:Formularios asignados a encuestadores en la zona del coordinador
    public int getFormulariosAsignadosCoordinador(int idCoordinador) throws SQLException {
        String sql = "SELECT COUNT(DISTINCT ehf.idformulario)\n" +
                "FROM enc_has_formulario ehf\n" +
                "JOIN usuario u ON ehf.enc_idusuario = u.idusuario\n" +
                "WHERE u.idroles = 3\n" +
                "  AND u.idzona = (SELECT idzona FROM usuario WHERE idusuario = ?)\n";
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

    public Map<String, Integer> obtenerCantidadRespuestasPorDistrito(int idZona) {
        Map<String, Integer> resultado = new HashMap<>();

        String sql = "SELECT d.nombre AS distrito, COUNT(*) AS total " +
                "FROM registro_respuestas rr " +
                "JOIN enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "JOIN usuario u ON ehf.enc_idusuario = u.idusuario " +
                "JOIN distritos d ON u.iddistritos = d.iddistritos " +
                "WHERE u.idzona = ? " +
                "GROUP BY d.nombre";

        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idZona);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                resultado.put(rs.getString("distrito"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public Map<String, Integer> obtenerRespuestasUltimos7Dias(int idZona) {
        Map<String, Integer> resultado = new LinkedHashMap<>();

        String sql = "SELECT rr.fecha_registro, COUNT(*) AS total " +
                "FROM registro_respuestas rr " +
                "JOIN enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "JOIN usuario u ON ehf.enc_idusuario = u.idusuario " +
                "WHERE rr.estado = 'C' AND u.idzona = ? AND rr.fecha_registro >= CURDATE() - INTERVAL 7 DAY " +
                "GROUP BY rr.fecha_registro " +
                "ORDER BY rr.fecha_registro";

        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idZona);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                resultado.put(rs.getString("fecha_registro"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public Map<String, Integer> obtenerConteoFormulariosPorEstado(int idZona) {
        Map<String, Integer> resultado = new HashMap<>();

        String sql = "SELECT rr.estado, COUNT(*) AS total " +
                "FROM registro_respuestas rr " +
                "JOIN enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "JOIN usuario u ON ehf.enc_idusuario = u.idusuario " +
                "WHERE u.idzona = ? " +
                "GROUP BY rr.estado";

        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idZona);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                resultado.put(rs.getString("estado"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }


    public double obtenerPromedioRespuestasPorEncuestador(int idZona) {
        double promedio = 0.0;

        String sql = "SELECT COUNT(rr.idregistro_respuestas) / COUNT(DISTINCT u.idusuario) AS promedio " +
                "FROM registro_respuestas rr " +
                "JOIN enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "JOIN usuario u ON ehf.enc_idusuario = u.idusuario " +
                "WHERE u.idzona = ?";

        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idZona);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                promedio = rs.getDouble("promedio");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promedio;
    }


    public Map<String, Integer> obtenerCantidadRespuestasPorFormulario(int idZona) {
        Map<String, Integer> resultado = new HashMap<>();

        String sql = "SELECT f.nombre AS formulario, COUNT(*) AS total " +
                "FROM registro_respuestas rr " +
                "JOIN enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "JOIN formulario f ON ehf.idformulario = f.idformulario " +
                "JOIN usuario u ON ehf.enc_idusuario = u.idusuario " +
                "WHERE u.idzona = ? " +
                "GROUP BY f.nombre";

        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idZona);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                resultado.put(rs.getString("formulario"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }


    public int contarFormulariosCompletadosPorZona(int idZona) {
        int total = 0;
        String sql = "SELECT COUNT(*) AS total " +
                "FROM registro_respuestas rr " +
                "JOIN enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "JOIN usuario u ON ehf.enc_idusuario = u.idusuario " +
                "WHERE rr.estado = 'C' AND u.idzona = ?";

        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idZona);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public int contarFormulariosBorradorPorZona(int idZona) {
        int total = 0;
        String sql = "SELECT COUNT(*) AS total " +
                "FROM registro_respuestas rr " +
                "JOIN enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "JOIN usuario u ON ehf.enc_idusuario = u.idusuario " +
                "WHERE rr.estado = 'B' AND u.idzona = ?";

        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idZona);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public double calcularTasaAvancePorZona(int idZona) {
        double tasa = 0.0;
        String sql = "SELECT " +
                "  (SELECT COUNT(*) FROM registro_respuestas rr " +
                "   JOIN enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "   JOIN usuario u ON ehf.enc_idusuario = u.idusuario " +
                "   WHERE rr.estado = 'C' AND u.idzona = ?) AS completados, " +
                "  (SELECT COUNT(*) FROM enc_has_formulario ehf " +
                "   JOIN usuario u ON ehf.enc_idusuario = u.idusuario " +
                "   WHERE u.idzona = ?) AS asignados";

        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idZona);
            stmt.setInt(2, idZona);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int completados = rs.getInt("completados");
                int asignados = rs.getInt("asignados");
                if (asignados > 0) {
                    tasa = (double) completados / asignados * 100.0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasa;
    }

    public String obtenerDistritoMasActivo(int idZona) {
        String distrito = "Sin datos";
        String sql = "SELECT d.nombre, COUNT(*) AS total " +
                "FROM registro_respuestas rr " +
                "JOIN enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "JOIN usuario u ON ehf.enc_idusuario = u.idusuario " +
                "JOIN distritos d ON u.iddistritos = d.iddistritos " +
                "WHERE rr.estado = 'C' AND u.idzona = ? " +
                "GROUP BY d.nombre " +
                "ORDER BY total DESC LIMIT 1";

        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idZona);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                distrito = rs.getString("nombre");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return distrito;
    }

    public Map<String, Map<String, Integer>> obtenerRespuestasPorFormularioYEstado(int idZona) {
        Map<String, Map<String, Integer>> resultado = new HashMap<>();

        String sql = "SELECT f.nombre AS formulario, rr.estado, COUNT(*) AS total " +
                "FROM registro_respuestas rr " +
                "JOIN enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "JOIN formulario f ON ehf.idformulario = f.idformulario " +
                "JOIN usuario u ON ehf.enc_idusuario = u.idusuario " +
                "WHERE u.idzona = ? " +
                "GROUP BY f.nombre, rr.estado";

        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idZona);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String nombreFormulario = rs.getString("formulario");
                String estado = rs.getString("estado");
                int total = rs.getInt("total");

                resultado.computeIfAbsent(nombreFormulario, k -> new HashMap<>()).put(estado, total);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }




    public int obtenerZonaPorCoordinador(int idCoordinador) {
        String sql = "SELECT idzona FROM usuario WHERE idusuario = ? AND idroles = 2";
        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idCoordinador);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("idzona");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Map<String, Integer> getRespuestasPorDistrito(int idCoordinador) {
        String sql = "SELECT d.nombre AS distrito, COUNT(*) AS total " +
                "FROM registro_respuestas rr " +
                "JOIN enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "JOIN usuario u ON u.idusuario = ehf.enc_idusuario " +
                "JOIN distritos d ON u.iddistritos = d.iddistritos " +
                "WHERE u.idzona = (SELECT idzona FROM usuario WHERE idusuario = ?) " +
                "AND rr.estado = 'C' " +
                "GROUP BY d.nombre";

        Map<String, Integer> resultado = new LinkedHashMap<>();
        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idCoordinador);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                resultado.put(rs.getString("distrito"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }
    public Map<String, Integer> getRespuestasUltimos7Dias(int idCoordinador) {
        String sql = "SELECT rr.fecha_registro, COUNT(*) AS total " +
                "FROM registro_respuestas rr " +
                "JOIN enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "JOIN usuario u ON u.idusuario = ehf.enc_idusuario " +
                "WHERE u.idzona = (SELECT idzona FROM usuario WHERE idusuario = ?) " +
                "AND rr.estado = 'C' AND rr.fecha_registro >= CURDATE() - INTERVAL 7 DAY " +
                "GROUP BY rr.fecha_registro ORDER BY rr.fecha_registro";

        Map<String, Integer> resultado = new LinkedHashMap<>();
        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idCoordinador);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                resultado.put(rs.getDate("fecha_registro").toString(), rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public Map<String, Integer> getFormulariosPorEstado(int idCoordinador) {
        String sql = "SELECT rr.estado, COUNT(*) AS total " +
                "FROM registro_respuestas rr " +
                "JOIN enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "JOIN usuario u ON u.idusuario = ehf.enc_idusuario " +
                "WHERE u.idzona = (SELECT idzona FROM usuario WHERE idusuario = ?) " +
                "GROUP BY rr.estado";

        Map<String, Integer> resultado = new LinkedHashMap<>();
        resultado.put("Completado", 0);
        resultado.put("Borrador", 0);
        resultado.put("No iniciado", 0);

        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idCoordinador);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String estado = rs.getString("estado");
                if ("C".equals(estado)) resultado.put("Completado", rs.getInt("total"));
                else if ("B".equals(estado)) resultado.put("Borrador", rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public Map<String, Map<String, Integer>> getRespuestasPorFormularioEstado(int idCoordinador) {
        String sql = "SELECT f.nombre, rr.estado, COUNT(*) AS total " +
                "FROM registro_respuestas rr " +
                "JOIN enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "JOIN formulario f ON ehf.idformulario = f.idformulario " +
                "JOIN usuario u ON u.idusuario = ehf.enc_idusuario " +
                "WHERE u.idzona = (SELECT idzona FROM usuario WHERE idusuario = ?) " +
                "GROUP BY f.nombre, rr.estado";

        Map<String, Map<String, Integer>> resultado = new LinkedHashMap<>();
        try (Connection con = this.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idCoordinador);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String estado = rs.getString("estado");
                int total = rs.getInt("total");

                resultado.putIfAbsent(nombre, new HashMap<>());
                resultado.get(nombre).put(estado, total);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }


}



