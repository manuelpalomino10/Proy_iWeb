package com.example.unmujeres.daos;
import com.example.unmujeres.beans.Distritos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DistritoDAO extends BaseDAO {

    // Obtener todos los distritos
    public List<Distritos> obtenerTodosDistritos() throws SQLException {
        List<Distritos> distritos = new ArrayList<>();
        String sql = "SELECT iddistritos, nombre FROM distritos"; // Asegúrate de incluir idzona si es necesario

        try (Connection con = this.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Distritos distrito = new Distritos();
                distrito.setIddistritos(rs.getInt("iddistritos"));
                distrito.setNombre(rs.getString("nombre"));
                distritos.add(distrito);
            }
        }

        return distritos;
    }

    // Obtener Distrito por ID (Opcional, depende de tus necesidades)
    public Distritos obtenerDistritoPorId(int idDistrito) throws SQLException {
        Distritos distrito = null;
        String sql = "SELECT iddistritos, nombre, zona_idzona FROM distritos WHERE iddistritos = ?"; // Added zona_idzona

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);) {

            ps.setInt(1, idDistrito);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                distrito = new Distritos();
                distrito.setIddistritos(rs.getInt("iddistritos"));
                distrito.setNombre(rs.getString("nombre"));
                distrito.setZona_idzona(rs.getInt("zona_idzona")); // Added zona_idzona
            }

        }
        return distrito;
    }

    public List<Distritos> listarDistritos() throws SQLException {
        String sql = "SELECT iddistritos, nombre, idzona FROM distritos ORDER BY nombre";
        List<Distritos> lista = new ArrayList<>();
        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet r = ps.executeQuery()) {

            while (r.next()) {
                Distritos d = new Distritos();
                d.setIddistritos(r.getInt("iddistritos"));
                d.setNombre(r.getString("nombre"));
                d.setZona_idzona(r.getInt("idzona"));
                lista.add(d);
            }
        }
        return lista;
    }



}