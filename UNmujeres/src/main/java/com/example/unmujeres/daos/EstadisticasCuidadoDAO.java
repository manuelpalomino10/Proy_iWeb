package com.example.unmujeres.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EstadisticasCuidadoDAO extends BaseDAO {

    public Map<Integer, Map<String, Integer>> obtenerTodasRespuestasSiNo() throws SQLException {
        // Lista de ID de preguntas con respuestas tipo Sí/No (puedes extenderla)
        int[] preguntas = {9, 11, 12, 13, 16, 17, 20, 21, 23, 24, 26, 27, 28};

        Map<Integer, Map<String, Integer>> respuestasPorPregunta = new HashMap<>();

        try (Connection con = getConnection()) {
            for (int idPregunta : preguntas) {
                String sql = "SELECT respuesta, COUNT(*) AS total " +
                        "FROM respuesta WHERE idpregunta = ? AND respuesta IN ('Sí', 'No') GROUP BY respuesta";

                try (PreparedStatement stmt = con.prepareStatement(sql)) {
                    stmt.setInt(1, idPregunta);
                    try (ResultSet rs = stmt.executeQuery()) {
                        Map<String, Integer> conteo = new HashMap<>();
                        while (rs.next()) {
                            conteo.put(rs.getString("respuesta"), rs.getInt("total"));
                        }
                        respuestasPorPregunta.put(idPregunta, conteo);
                    }
                }
            }
        }

        return respuestasPorPregunta;
    }

}
