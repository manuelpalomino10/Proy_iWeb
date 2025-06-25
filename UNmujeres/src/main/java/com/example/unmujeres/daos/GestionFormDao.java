package com.example.unmujeres.daos;
import com.example.unmujeres.dtos.FormularioDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GestionFormDao extends BaseDAO {
    public ArrayList<FormularioDto> listar() {
        ArrayList<FormularioDto> lista = new ArrayList<>();

        String sql = "select idFormulario, f.nombre as NombreForm, fecha_creacion as creacion, estado\n" +
                "from formulario f\n" +
                "join categoria c on f.idcategoria = c.idcategoria;";

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                FormularioDto formularioDto = new FormularioDto();

                formularioDto.setId(rs.getInt("idFormulario"));
                formularioDto.setNombreForm(rs.getString("NombreForm"));
                formularioDto.setFechaCreacion(rs.getDate("creacion"));
                formularioDto.setEstado(rs.getBoolean("estado"));

                lista.add(formularioDto);
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
