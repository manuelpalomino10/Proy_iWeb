package com.example.unmujeres.daos;

import com.example.unmujeres.beans.OpcionPregunta;
import com.example.unmujeres.beans.Pregunta;

import java.sql.*;
import java.util.ArrayList;

public class OpcionPreguntaDAO extends BaseDAO{

    public ArrayList<OpcionPregunta> getByReg(int idReg) {
        ArrayList<OpcionPregunta> opciones = new ArrayList<>();

        String sql = "SELECT " +
                "    opc.idopcion_pregunta, " +
                "    opc.opcion, " +
                "    p.idpregunta " +
                "FROM " +
                "    opcion_pregunta opc " +
                "LEFT JOIN " +
                "    pregunta p ON opc.idpregunta = p.idpregunta " +
                "INNER JOIN " +
                "    respuesta r ON p.idpregunta = r.idpregunta " +
                "INNER JOIN " +
                "    registro_respuestas reg ON r.idregistro_respuestas = reg.idregistro_respuestas " +
                "WHERE " +
                "    reg.idregistro_respuestas = ?";

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, idReg);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OpcionPregunta opc = new OpcionPregunta();

                    opc.setIdOpcionPregunta(rs.getInt("idopcion_pregunta"));
                    opc.setOpcion(rs.getString("opcion"));

                        Pregunta preg = new Pregunta();
                        preg.setIdPregunta(rs.getInt("idpregunta"));
                    opc.setPregunta(preg);

                    opciones.add(opc);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return opciones;
    }
    public ArrayList<OpcionPregunta> getByForm(int idForm) {
        ArrayList<OpcionPregunta> opciones = new ArrayList<>();

        String sql = "SELECT " +
                "    opc.idopcion_pregunta, " +
                "    opc.opcion, " +
                "    p.idpregunta " +
                "FROM " +
                "    opcion_pregunta opc " +
                "LEFT JOIN " +
                "    pregunta p ON opc.idpregunta = p.idpregunta " +
                "LEFT JOIN " +
                "    seccion s ON p.idseccion = s.idseccion " +
                "LEFT JOIN " +
                "    formulario f ON s.idformulario = f.idformulario " +
                "WHERE " +
                "    f.idformulario = ? " +
                "    OR f.idformulario IS NULL";

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, idForm);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OpcionPregunta opc = new OpcionPregunta();

                    opc.setIdOpcionPregunta(rs.getInt("idopcion_pregunta"));
                    opc.setOpcion(rs.getString("opcion"));

                    Pregunta preg = new Pregunta();
                    preg.setIdPregunta(rs.getInt("idpregunta"));
                    opc.setPregunta(preg);

                    opciones.add(opc);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return opciones;
    }

    public ArrayList<String> getByPreguntaToString (int idPregunta) {
        ArrayList<OpcionPregunta> opciones = new ArrayList<>();
        ArrayList<String> opcionesStr = new ArrayList<>();

        String sql = " SELECT opcion FROM opcion_pregunta " +
                "WHERE idpregunta = ? ";

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, idPregunta);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String opc = rs.getString("opcion");

                    opcionesStr.add(opc);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return opcionesStr;
    }


    public int crearOpcion(Connection conn, OpcionPregunta opcion) throws SQLException {

        String sql = "INSERT INTO opcion_pregunta (opcion, idpregunta) VALUES (?, ?)";

        try (Connection con = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, opcion.getOpcion());
            ps.setInt(2, opcion.getPregunta().getIdPregunta());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("No se pudo crear nueva opcion");
    }

}
