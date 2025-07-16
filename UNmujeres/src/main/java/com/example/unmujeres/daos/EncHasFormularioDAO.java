package com.example.unmujeres.daos;

import java.util.ArrayList;

import com.example.unmujeres.beans.EncHasFormulario;
import com.example.unmujeres.beans.Formulario;
import com.example.unmujeres.beans.Usuario;
import com.example.unmujeres.dtos.AsignacionDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EncHasFormularioDAO extends BaseDAO {

    public ArrayList<AsignacionDTO> getByUser(int idUsuario) {
        ArrayList<AsignacionDTO> listaAsig = new ArrayList<>();

        String sql = "SELECT ehf.idenc_has_formulario, ehf.fecha_asignacion, f.idformulario, f.nombre, COUNT(DISTINCT reg.idregistro_respuestas) AS total_respuestas, " +
                "    f.registros_esperados DIV NULLIF(enc_count.encuestadores, 0) AS reg_esperados_enc, " +
                "    f.fecha_limite, f.registros_esperados " +
                "FROM formulario f " +
                "INNER JOIN enc_has_formulario ehf ON f.idformulario = ehf.idformulario " +
                "LEFT JOIN registro_respuestas reg ON ehf.idenc_has_formulario = reg.idenc_has_formulario " +
                "INNER JOIN ( " +
                "    SELECT ehf2.idformulario, COUNT(DISTINCT ehf2.enc_idusuario) AS encuestadores " +
                "    FROM enc_has_formulario ehf2 " +
                "    INNER JOIN usuario u " +
                "        ON ehf2.enc_idusuario = u.idusuario " +
                "        AND u.idroles = 3 " +
                "    GROUP BY ehf2.idformulario " +
                ") enc_count ON f.idformulario = enc_count.idformulario " +
                "WHERE ehf.enc_idusuario = ? AND f.estado = 1 " +
                "GROUP BY ehf.idenc_has_formulario, f.idformulario, ehf.fecha_asignacion;";

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AsignacionDTO a = new AsignacionDTO();

                    a.setIdAsignacion(rs.getInt("idenc_has_formulario"));
                    a.setFechaAsignacion(rs.getDate("fecha_asignacion"));
                    //a.setCodigo(rs.getString("codigo"));

                    Formulario formulario = new Formulario();
                        formulario.setIdFormulario(rs.getInt("idformulario"));
                        formulario.setNombre(rs.getString("nombre"));
                        formulario.setFechaLimite(rs.getDate("fecha_limite"));
                        formulario.setRegistrosEsperados(rs.getInt("registros_esperados"));
                    a.setFormulario(formulario);
                    a.setTotalRegistros(rs.getInt("total_respuestas"));
                    a.setEsperadosEnc(rs.getInt("reg_esperados_enc"));

                    listaAsig.add(a);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaAsig;
    }


    public EncHasFormulario getById(int id) {
        EncHasFormulario asig = null;
        String sql = "SELECT * FROM enc_has_formulario ehf " +
                "INNER JOIN formulario f ON ehf.idformulario=f.idformulario " +
                "WHERE idenc_has_formulario = ? AND f.estado=1 ";

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    asig = new EncHasFormulario();

                    asig.setIdEncHasFormulario(rs.getInt("idenc_has_formulario"));
                    asig.setFechaAsignacion(rs.getDate("fecha_asignacion"));
                    asig.setCodigo(rs.getString("codigo"));

                    Formulario formulario = new Formulario();
                    formulario.setIdFormulario(rs.getInt("idformulario"));
                    asig.setFormulario(formulario);
//                    Obtener formulario por id
//                    FormularioDAO formularioDAO = new FormularioDAO();
//                    asig.setFormulario(formularioDAO.getById(rs.getInt("idformulario")));

                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("enc_idusuario"));
                    asig.setUsuario(usuario);
//                     Obtener encuestador por id
//                    UsuarioDAO usuarioDAO = new UsuarioDAO();
//                    asig.setUsuario(usuarioDAO.getById(rs.getInt("enc_idusuario")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return asig;
    }

    public Formulario getAsigByForm(int idFormulario, int idUser) {
        Formulario formAsig = new Formulario();
        String sql = "SELECT f.idformulario, f.nombre, ehf.idenc_has_formulario " +
                "FROM formulario f " +
                "INNER JOIN enc_has_formulario ehf ON f.idformulario=ehf.idformulario " +
                "WHERE ehf.enc_idusuario=? AND f.idformulario=? AND f.estado=1 ";

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);) {

            ps.setInt(1, idUser);
            ps.setInt(2, idFormulario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    formAsig.setIdFormulario(rs.getInt("idformulario"));
                    formAsig.setNombre(rs.getString("nombre"));
                    System.out.println("Form asignado y existe/ con id: " + rs.getInt("idformulario"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return formAsig;
    }
}
