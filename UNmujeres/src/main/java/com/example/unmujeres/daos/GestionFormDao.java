package com.example.unmujeres.daos;
import com.example.unmujeres.dtos.FormularioDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GestionFormDao extends BaseDAO {
    public ArrayList<FormularioDto> listar() {
        return listar(0);
    }

    public ArrayList<FormularioDto> listar(int idCategoria) {
        ArrayList<FormularioDto> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder("select idFormulario, f.nombre as NombreForm, fecha_creacion as creacion, estado, c.nombre as NombreCat\n" +
                "from formulario f\n" +
                "join categoria c on f.idcategoria = c.idcategoria");
        if (idCategoria != 0) {
            sql.append(" WHERE f.idcategoria = ?");
        }

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            if (idCategoria != 0) {
                ps.setInt(1, idCategoria);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    FormularioDto formularioDto = new FormularioDto();
                    formularioDto.setId(rs.getInt("idFormulario"));
                    formularioDto.setNombreForm(rs.getString("NombreForm"));
                    formularioDto.setFechaCreacion(rs.getDate("creacion"));
                    formularioDto.setEstado(rs.getBoolean("estado"));
                    formularioDto.setNombreCat(rs.getString("NombreCat"));
                    lista.add(formularioDto);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean cambiarEstado(int idFormulario, boolean estado) {
        boolean actualizado = false;
        String sql = "UPDATE formulario SET estado = ? WHERE idFormulario = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, estado);
            ps.setInt(2, idFormulario);

            actualizado = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actualizado;
    }
}
