package com.example.onu.daos;

import com.example.onu.DatabaseConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class EstadisticasDAO {
    //--------------------- PRIMER CRUD ---------------------------------------
    public int contarFormulariosAsignados(int idusuario) throws SQLException {

        String sql = "SELECT COUNT(f.idformulario) " +
                "FROM iweb_proy.formulario f " +
                "JOIN iweb_proy.enc_has_formulario ef ON f.idformulario = ef.formulario_idformulario " +
                "JOIN iweb_proy.categoria c ON f.categoria_idcategoria = c.idcategoria " +
                "WHERE ef.enc_idusuario = ?";

        // Establecemos la conexión con la base de datos y preparamos la consulta
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Establecemos el parámetro idusuario en la consulta
            stmt.setInt(1, idusuario);

            // Ejecutamos la consulta y obtenemos el resultado
            try (ResultSet rs = stmt.executeQuery()) {
                // Si se encuentran resultados, retornamos el conteo de formularios asignados
                if (rs.next()) {
                    return rs.getInt(1); // Retorna el número de formularios asignados
                }
                return 0; // Si no hay registros, retornamos 0
            }
        }
    }



    //--------------------- SEGUNDO CRUD ---------------------------------------
    //-------------------------------------------------
    public Map<String, Object> calcularAvance(int idUsuario) throws SQLException {

        String sql = "SELECT f.nombre AS nombre_formulario, "
                + "SUM(CASE WHEN rr.fecha_registro >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) THEN 1 ELSE 0 END) AS completados_semana, "
                + "SUM(CASE WHEN rr.fecha_registro >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) THEN 1 ELSE 0 END) AS completados_mes "
                + "FROM formulario f JOIN enc_has_formulario ehf ON ehf.formulario_idformulario = f.idformulario "
                + "JOIN registro_respuestas rr ON rr.enc_has_formulario_idenc_has_formulario = ehf.idenc_has_formulario "
                + "WHERE rr.estado = 'C' AND ehf.enc_idusuario = ? "
                + "GROUP BY f.idformulario, f.nombre";

        Map<String, int[]> datosPorFormulario = new HashMap<>();
        int totalCompletadosSemanaGeneral = 0;
        int totalCompletadosMesGeneral = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String nombreFormulario = rs.getString("nombre_formulario");
                int completadosSemana = rs.getInt("completados_semana");
                int completadosMes = rs.getInt("completados_mes");

                datosPorFormulario.put(nombreFormulario, new int[]{completadosSemana, completadosMes});
                totalCompletadosSemanaGeneral += completadosSemana;
                totalCompletadosMesGeneral += completadosMes;
            }
        }

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("detallePorFormulario", datosPorFormulario);
        resultado.put("totalesGenerales", new int[]{totalCompletadosSemanaGeneral, totalCompletadosMesGeneral});

        return resultado;
    }


    //--------------------- TERCER CRUD ---------------------------------------
    //-------------------------------------------------
    public int contarBorradores() throws SQLException {

        String sql = "SELECT COUNT(*) FROM registro_respuestas WHERE estado = 'B'";
        try (Connection conn = DatabaseConnection.getConnection();
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            int count = rs.next() ? rs.getInt(1) : 0;
            System.out.println("Total borradores: " + count);
            return count;
        }
    }

    //--------------------- CUARTO CRUD ---------------------------------------
    //-------------------------------------------------
    public int contarFormulariosCompletados() throws SQLException {

        String sql = "SELECT COUNT(*) FROM enc_has_formulario ehf " +
                "JOIN registro_respuestas rr ON ehf.idenc_has_formulario = rr.enc_has_formulario_idenc_has_formulario " +
                "WHERE rr.estado = 'C'";
        try (Connection conn = DatabaseConnection.getConnection();
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            int count = rs.next() ? rs.getInt(1) : 0;
            System.out.println("Formularios completados: " + count);
            return count;
        }
    }


    //--------------------- QUINTO CRUD ---------------------------------------
    //-------------------------------------------------
    public int contarCompletadosUltimos7Dias() throws SQLException {


        String sql = "SELECT COUNT(*) FROM registro_respuestas " +
                "WHERE estado = 'C' AND fecha_registro >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)";
        try (Connection conn = DatabaseConnection.getConnection();
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            int count = rs.next() ? rs.getInt(1) : 0;

            return count;
        }
    }

    //--------------------- SEXTO CRUD ---------------------------------------
    public Map<String, Integer> obtenerRespuestasCompletadasPorFormulario(int idusuario) throws SQLException {
        String sql = "SELECT " +
                "f.nombre AS nombre_formulario, " +
                "COUNT(rr.idregistro_respuestas) AS respuestas_completadas " +
                "FROM " +
                "formulario f " +
                "JOIN enc_has_formulario ehf ON ehf.formulario_idformulario = f.idformulario " +
                "JOIN registro_respuestas rr ON rr.enc_has_formulario_idenc_has_formulario = ehf.idenc_has_formulario " +
                "WHERE rr.estado = 'C' AND ehf.enc_idusuario = 5 " // Por el momento trabajamos con el usuario = 5"
                + "GROUP BY f.idformulario, f.nombre";

        Map<String, Integer> resultados = new HashMap<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String nombreFormulario = rs.getString("nombre_formulario");
                int respuestasCompletadas = rs.getInt("respuestas_completadas");
                System.out.println("DAO - Leyendo del ResultSet - Formulario: [" + nombreFormulario + "], Completados: [" + respuestasCompletadas + "]");
                resultados.put(nombreFormulario, respuestasCompletadas);
            }

            System.out.println("Total formularios con respuestas completadas para el usuario " + idusuario + ": " + resultados.size());
            return resultados;
        }
    }



}

