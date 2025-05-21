package com.example.unmujeres.daos;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EstadisticasDAO extends BaseDAO {

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
    public int contarBorradores() throws SQLException {
        String sql = "SELECT COUNT(*) FROM iweb_proy.registro_respuestas WHERE estado = 'B'";
        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Total borradores: " + count);
                return count;
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
}

