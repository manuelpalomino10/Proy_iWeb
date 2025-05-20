package com.example.onumujeres.daos;

import com.example.onumujeres.beans.OpcionPregunta;
import com.example.onumujeres.beans.Pregunta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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


}
