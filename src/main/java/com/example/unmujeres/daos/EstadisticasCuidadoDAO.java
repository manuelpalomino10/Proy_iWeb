package com.example.unmujeres.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EstadisticasCuidadoDAO extends BaseDAO {

    // 1. Distribución de niños que asisten a guarderías
    public Map<String, Integer> obtenerAsistenciaGuarderia() throws SQLException {
        String sql = "SELECT r.respuesta, COUNT(*) AS total "
                + "FROM respuesta r "
                + "WHERE r.idpregunta = 11 "  // Pregunta
                + "GROUP BY r.respuesta";

        Map<String, Integer> resultados = new HashMap<>();
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                resultados.put(rs.getString("respuesta"), rs.getInt("total"));
            }
        }
        return resultados;
    }

    // 2. Razones para no usar guarderías
    public Map<String, Integer> obtenerMotivosNoGuarderia() throws SQLException {
        String sql = "SELECT r.respuesta, COUNT(*) AS total "
                + "FROM respuesta r "
                + "WHERE r.idpregunta = 12 "  // Pregunta
                + "GROUP BY r.respuesta";

        Map<String, Integer> resultados = new HashMap<>();
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String motivo = rs.getString("respuesta");

                if (motivo.contains("familiar") || motivo.contains("personal")) {
                    motivo = "Cuidado personal/familiar";
                } else if (motivo.contains("confío") || motivo.contains("confia")) {
                    motivo = "Desconfianza";
                }
                resultados.merge(motivo, rs.getInt("total"), Integer::sum);
            }
        }
        return resultados;
    }

    // 3. Presencia de adultos mayores con enfermedades
    public Map<String, Integer> obtenerAdultosMayoresEnfermos() throws SQLException {
        String sql = "SELECT r.respuesta, COUNT(*) AS total "
                + "FROM respuesta r "
                + "WHERE r.idpregunta = 15 "  // Pregunta
                + "AND r.respuesta <> 'Ninguna' "
                + "GROUP BY r.respuesta";

        Map<String, Integer> resultados = new HashMap<>();
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                resultados.put(rs.getString("respuesta"), rs.getInt("total"));
            }
        }
        return resultados;
    }

    // 4. Relación entre niños y adultos mayores
    public Map<String, Integer> obtenerHogaresConNinosYAncianos() throws SQLException {
        String sql = "SELECT "
                + "  SUM(CASE WHEN ninos.respuesta = 'Sí' AND ancianos.respuesta = 'Sí' THEN 1 ELSE 0 END) AS ambos, "
                + "  SUM(CASE WHEN ninos.respuesta = 'Sí' AND ancianos.respuesta = 'No' THEN 1 ELSE 0 END) AS solo_ninos, "
                + "  SUM(CASE WHEN ninos.respuesta = 'No' AND ancianos.respuesta = 'Sí' THEN 1 ELSE 0 END) AS solo_ancianos "
                + "FROM ("
                + "  SELECT idregistro_respuestas, respuesta "
                + "  FROM respuesta WHERE idpregunta = 9" // PREGUNTA
                + ") ninos "
                + "JOIN ("
                + "  SELECT idregistro_respuestas, respuesta "
                + "  FROM respuesta WHERE idpregunta = 13"  // pregunta
                + ") ancianos ON ninos.idregistro_respuestas = ancianos.idregistro_respuestas";

        Map<String, Integer> resultados = new HashMap<>();
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                resultados.put("Ambos", rs.getInt("ambos"));
                resultados.put("Solo niños", rs.getInt("solo_ninos"));
                resultados.put("Solo ancianos", rs.getInt("solo_ancianos"));
            }
        }
        return resultados;
    }

    // 5. Tiempo promedio de cuidado por hogar
    public double obtenerTiempoPromedioCuidado() throws SQLException {

        String sql = "SELECT AVG(CAST(r.respuesta AS DECIMAL)) AS promedio "
                + "FROM respuesta r "
                + "WHERE r.idpregunta = 17";  // Pregunta

        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("promedio");
            }
        }
        return 0.0;
    }
}
