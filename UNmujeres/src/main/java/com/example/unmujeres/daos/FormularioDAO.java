package com.example.unmujeres.daos;

import java.util.ArrayList;

import com.example.unmujeres.beans.EncHasFormulario;
import com.example.unmujeres.beans.Formulario;
import com.example.unmujeres.beans.RegistroRespuestas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FormularioDAO extends BaseDAO {

    public Formulario getById(int id) {
        Formulario formulario = null;
        String sql = "SELECT * FROM formulario WHERE idformulario = ?";

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);){
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    formulario = new Formulario();

                    formulario.setIdFormulario(rs.getInt("idformulario"));
                    formulario.setNombre(rs.getString("nombre"));
                    formulario.setFechaLimite(rs.getDate("fecha_limite"));
                    formulario.setEstado(rs.getBoolean("estado"));
                    formulario.setRegistrosEsperados(rs.getInt("registros_esperados"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return formulario;

    }
}
