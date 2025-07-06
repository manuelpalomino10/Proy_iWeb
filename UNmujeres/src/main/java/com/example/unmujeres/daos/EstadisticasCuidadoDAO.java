package com.example.unmujeres.daos;

import com.example.unmujeres.beans.EstadisticasFormulario;
import com.example.unmujeres.beans.Formulario;
import com.example.unmujeres.beans.RespuestaDetallada;

import java.sql.*;
import java.util.*;

public class EstadisticasCuidadoDAO extends BaseDAO {


    //----------------------------------------------------------------------------------------------------------------

    public EstadisticasFormulario obtenerEstadisticasPorFormulario(int idFormulario) throws SQLException {
        EstadisticasFormulario estadisticas = new EstadisticasFormulario();


        Map<String, Map<String, Long>> datosGraficos = obtenerDatosParaGraficos(idFormulario);


        Map<String, Map<String, List<RespuestaDetallada>>> datosAgrupados = obtenerDatosParaTablas(idFormulario);

        estadisticas.setDatosGraficos(datosGraficos);
        estadisticas.setRespuestasAgrupadas(datosAgrupados);

        return estadisticas;
    }

    public Map<String, Map<String, Long>> obtenerDatosParaGraficos(int idFormulario) {
        Map<String, Map<String, Long>> datosGraficos = new LinkedHashMap<>();

        String sqlCombobox = "SELECT p.enunciado, r.respuesta " +
                "FROM respuesta r " +
                "JOIN pregunta p ON r.idpregunta = p.idpregunta " +
                "JOIN registro_respuestas rr ON r.idregistro_respuestas = rr.idregistro_respuestas " +
                "JOIN enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "JOIN seccion s ON p.idseccion = s.idseccion " +
                "WHERE ehf.idformulario = ? " +
                "AND s.idseccion >= 4 " +
                "AND p.tipo_dato = 'combobox' " +
                "AND r.respuesta IS NOT NULL " +
                "AND rr.estado = 'C'";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlCombobox)) {

            ps.setInt(1, idFormulario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String pregunta = rs.getString("enunciado");
                String respuesta = rs.getString("respuesta");

                datosGraficos
                        .computeIfAbsent(pregunta, k -> new LinkedHashMap<>())
                        .merge(respuesta, 1L, Long::sum);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return datosGraficos;
    }

    public EstadisticasFormulario obtenerEstadisticasDesdeSeccionD(int idFormulario) {
        EstadisticasFormulario estadisticas = new EstadisticasFormulario();
        Map<String, Map<String, List<RespuestaDetallada>>> datosAgrupados = new LinkedHashMap<>();


        String sqlTablas = "SELECT p.idpregunta, p.enunciado, p.tipo_dato, r.respuesta, " +
                "CONCAT(u.nombres, ' ', u.apellidos) AS encuestador, " +
                "s.idseccion, s.nombre_sec " +
                "FROM respuesta r " +
                "JOIN pregunta p ON r.idpregunta = p.idpregunta " +
                "JOIN registro_respuestas rr ON r.idregistro_respuestas = rr.idregistro_respuestas " +
                "JOIN enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "JOIN usuario u ON ehf.enc_idusuario = u.idusuario " +
                "JOIN seccion s ON p.idseccion = s.idseccion " +
                "WHERE ehf.idformulario = ? " +
                "AND s.idseccion >= 4 " +
                "AND r.respuesta IS NOT NULL " +
                "AND TRIM(r.respuesta) <> '' " +
                "AND p.tipo_dato NOT IN ('combobox')";  // Filtro clave

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlTablas)) {

            ps.setInt(1, idFormulario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RespuestaDetallada rd = new RespuestaDetallada(
                        rs.getInt("idpregunta"),
                        rs.getString("enunciado"),
                        rs.getString("tipo_dato"),
                        rs.getString("respuesta"),
                        rs.getString("encuestador"),
                        rs.getInt("idseccion"),
                        rs.getString("nombre_sec")
                );


                datosAgrupados
                        .computeIfAbsent(rd.getNombreSeccion(), k -> new LinkedHashMap<>())
                        .computeIfAbsent(rd.getEnunciadoPregunta(), k -> new ArrayList<>())
                        .add(rd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        Map<String, Map<String, Long>> datosGraficos = obtenerDatosParaGraficos(idFormulario);

        estadisticas.setRespuestasAgrupadas(datosAgrupados);
        estadisticas.setDatosGraficos(datosGraficos);

        return estadisticas;
    }

    public EstadisticasFormulario obtenerEstadisticasCuidado() throws SQLException {
        EstadisticasFormulario estadisticas = new EstadisticasFormulario();
        int idFormulario = 1;


        Map<String, Map<String, Long>> datosGraficos = obtenerDatosParaGraficos(idFormulario);


        Map<String, Map<String, List<RespuestaDetallada>>> datosAgrupados = obtenerDatosParaTablas(idFormulario);

        estadisticas.setDatosGraficos(datosGraficos);
        estadisticas.setRespuestasAgrupadas(datosAgrupados);

        return estadisticas;
    }
    private Map<String, Map<String, List<RespuestaDetallada>>> obtenerDatosParaTablas(int idFormulario) throws SQLException {
        Map<String, Map<String, List<RespuestaDetallada>>> datosAgrupados = new LinkedHashMap<>();

        String sqlTablas = "SELECT p.idpregunta, p.enunciado, p.tipo_dato, r.respuesta, " +
                "CONCAT(u.nombres, ' ', u.apellidos) AS encuestador, " +
                "s.idseccion, s.nombre_sec " +
                "FROM respuesta r " +
                "JOIN pregunta p ON r.idpregunta = p.idpregunta " +
                "JOIN registro_respuestas rr ON r.idregistro_respuestas = rr.idregistro_respuestas " +
                "JOIN enc_has_formulario ehf ON rr.idenc_has_formulario = ehf.idenc_has_formulario " +
                "JOIN usuario u ON ehf.enc_idusuario = u.idusuario " +
                "JOIN seccion s ON p.idseccion = s.idseccion " +
                "WHERE ehf.idformulario = ? " +
                "AND s.idseccion >= 4 " +
                "AND r.respuesta IS NOT NULL " +
                "AND TRIM(r.respuesta) <> '' " +
                "AND p.tipo_dato NOT IN ('combobox')";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlTablas)) {

            ps.setInt(1, idFormulario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RespuestaDetallada rd = new RespuestaDetallada(
                        rs.getInt("idpregunta"),
                        rs.getString("enunciado"),
                        rs.getString("tipo_dato"),
                        rs.getString("respuesta"),
                        rs.getString("encuestador"),
                        rs.getInt("idseccion"),
                        rs.getString("nombre_sec")
                );

                datosAgrupados
                        .computeIfAbsent(rd.getNombreSeccion(), k -> new LinkedHashMap<>())
                        .computeIfAbsent(rd.getEnunciadoPregunta(), k -> new ArrayList<>())
                        .add(rd);
            }
        }
        return datosAgrupados;
    }


    public List<Formulario> listarFormulariosActivos() throws SQLException {
        List<Formulario> formularios = new ArrayList<>();
        String sql = "SELECT idformulario, nombre FROM formulario WHERE estado = 1";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                formularios.add(new Formulario(
                        rs.getInt("idformulario"),
                        rs.getString("nombre")
                ));
            }
        }
        return formularios;
    }


}
