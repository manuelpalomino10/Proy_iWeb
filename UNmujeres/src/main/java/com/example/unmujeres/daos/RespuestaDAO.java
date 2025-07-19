package com.example.unmujeres.daos;

import com.example.unmujeres.beans.Pregunta;
import com.example.unmujeres.beans.Respuesta;
import com.example.unmujeres.beans.Seccion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RespuestaDAO extends BaseDAO{

    public ArrayList<Respuesta> listaRespuestas(int idRegistro) {

        ArrayList<Respuesta> respuestas  = new ArrayList<>();

        String sql = "SELECT " +
                "    p.idpregunta, " +
                "    p.enunciado, " +
                "    p.tipo_dato, " +
                "    p.requerido, " +
                "    r.idrespuesta, " +
                "    r.respuesta, " +
                "    s.idseccion, " +
                "    s.nombre_sec " +
                "FROM " +
                "    pregunta p " +
                "LEFT JOIN " +
                "    respuesta r ON p.idpregunta = r.idpregunta " +
                "INNER JOIN " +
                "    seccion s ON p.idseccion = s.idseccion " +
                "WHERE " +
                "    r.idregistro_respuestas = ? " +
                "ORDER BY s.idseccion ASC; ";
        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, idRegistro);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Respuesta respuesta = new Respuesta();
                    respuesta.setIdRespuesta(rs.getInt("idrespuesta"));
                    respuesta.setRespuesta(rs.getString("respuesta"));
                    //respuesta.setRegistro(idRegistro);
                        Pregunta pregunta = new Pregunta();
                        pregunta.setIdPregunta(rs.getInt("idpregunta"));
                        pregunta.setEnunciado(rs.getString("enunciado"));
                        pregunta.setTipoDato(rs.getString("tipo_dato"));
                        int req = rs.getInt("requerido");
                        boolean requerido = req==1 ? true : false;
                        pregunta.setRequerido(requerido);
                            Seccion seccion = new Seccion();
                            seccion.setIdSeccion(rs.getInt("idseccion"));
                            seccion.setNombreSec(rs.getString("nombre_sec"));

                        pregunta.setSeccion(seccion);

//                        OpcionPregunta opciones = new OpcionPregunta();
//                        opciones.setIdOpcionesPregunta(rs.getInt("idopciones_pregunta"));
//                        opciones.setOpcion(rs.getString("opciones"));
//                        opciones.setPregunta(pregunta);

                    respuesta.setPregunta(pregunta);

                    respuestas.add(respuesta);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return respuestas;
    }


    public void updateResponse(int idReg,int idRespuesta,String respuesta) {

        String sql="UPDATE respuesta SET respuesta= ? WHERE idrespuesta= ? AND idregistro_respuestas= ?";

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, respuesta);
            ps.setInt(2, idRespuesta);
            ps.setInt(3, idReg);

            ps.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public int crearRegistroRespuestas(int idEncHasFormulario) throws SQLException {
        String sql = "INSERT INTO registro_respuestas (fecha_registro, estado, idenc_has_formulario) " +
                "VALUES (CURDATE(), 'B', ?)";

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, idEncHasFormulario);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Retorna el ID generado
                }
            }
        }
        throw new SQLException("No se pudo crear el registro de respuestas");
    }

    public void guardarRespuestas(int idRegistro, Map<Integer, String> respuestas) throws SQLException {
        String sql = "INSERT INTO respuesta (respuesta, idpregunta, idregistro_respuestas) " +
                "VALUES (?, ?, ?)";

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            con.setAutoCommit(false); // Iniciar transacción

            for (Map.Entry<Integer, String> entry : respuestas.entrySet()) {
                if (entry.getValue() == null) {
                    ps.setNull(1, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(1, entry.getValue());
                }
                ps.setInt(2, entry.getKey());
                ps.setInt(3, idRegistro);
                ps.addBatch();
            }

            ps.executeBatch();
            con.commit();

        } catch (SQLException e) {
            throw e;
        }
    }

    public void guardarRespTran(Connection conn, int idRegistro, Map<Integer, String> respuestas) throws SQLException {
        String sql = "INSERT INTO respuesta (respuesta, idpregunta, idregistro_respuestas) " +
                "VALUES (?, ?, ?)";

        try (Connection con = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Map.Entry<Integer, String> entry : respuestas.entrySet()) {
                if (entry.getValue() == null) {
                    ps.setNull(1, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(1, entry.getValue());
                }
                ps.setInt(2, entry.getKey());
                ps.setInt(3, idRegistro);
                ps.addBatch();
            }

            ps.executeBatch();

        } catch (SQLException e) {
            throw e;
        }
    }
}
