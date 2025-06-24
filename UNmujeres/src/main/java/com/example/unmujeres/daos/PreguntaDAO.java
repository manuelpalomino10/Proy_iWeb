package com.example.unmujeres.daos;

import java.sql.*;
import java.util.*;

import com.example.unmujeres.beans.*;

public class PreguntaDAO extends BaseDAO{
    public ArrayList<Pregunta> getPreguntasConOpcionesPorFormulario(int idFormulario) {
        ArrayList<Pregunta> preguntas = new ArrayList<>();

        String sql = "SELECT f.idformulario, p.idpregunta, p.enunciado, p.tipo_dato, p.requerido," +
                "s.idseccion, s.nombre_sec " +
                "FROM pregunta p " +
                "INNER JOIN seccion s ON p.idseccion = s.idseccion " +
                "LEFT JOIN formulario f ON s.idformulario = f.idformulario " +
                "WHERE f.idformulario = ?"; // Sin ";"


        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idFormulario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Crear la pregunta
                    Pregunta pregunta = new Pregunta();
                    pregunta.setIdPregunta(rs.getInt("idpregunta"));
                    pregunta.setEnunciado(rs.getString("enunciado"));
                    pregunta.setTipoDato(rs.getString("tipo_dato"));
                    pregunta.setRequerido(rs.getBoolean("requerido"));

                    // Configurar la sección
                    Seccion seccion = new Seccion();
                    seccion.setIdSeccion(rs.getInt("idseccion"));
                    seccion.setNombreSec(rs.getString("nombre_sec"));
                    pregunta.setSeccion(seccion);
                    preguntas.add(pregunta);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener preguntas del formulario", e);
        }

        return preguntas;
    }

    public int crearPregunta(Connection conn, Pregunta pregunta) throws SQLException {

        String sql = "INSERT INTO pregunta (enunciado, tipo_dato, idseccion, requerido) VALUES (?, ?, ?, ?)";

        try (Connection con = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, pregunta.getEnunciado());
            ps.setString(2, pregunta.getTipoDato());
            ps.setInt(3, pregunta.getSeccion().getIdSeccion());

            boolean requerido = pregunta.getRequerido();
            int req = requerido ? 1 : 0;
            ps.setInt(4, req);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("No se pudo crear nueva pregunta");
    }



}

