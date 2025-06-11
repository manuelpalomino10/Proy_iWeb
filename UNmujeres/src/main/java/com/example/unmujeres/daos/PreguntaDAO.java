package com.example.unmujeres.daos;

import java.util.*;

import com.example.unmujeres.beans.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PreguntaDAO extends BaseDAO{
    public ArrayList<Pregunta> getPreguntasConOpcionesPorFormulario(int idFormulario) {
        ArrayList<Pregunta> preguntas = new ArrayList<>();

        String sql = "SELECT f.idformulario, p.idpregunta, p.enunciado, p.tipo_dato, " +
                "s.idseccion, s.nombre_sec " +
                "FROM pregunta p " +
                "INNER JOIN seccion s ON p.idseccion = s.idseccion " +
                "LEFT JOIN formulario f ON s.idformulario = f.idformulario " +
                "WHERE f.idformulario = ?";


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

}

