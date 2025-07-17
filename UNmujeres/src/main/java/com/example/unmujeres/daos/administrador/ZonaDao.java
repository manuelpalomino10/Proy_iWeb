package com.example.unmujeres.daos.administrador;

import com.example.unmujeres.beans.Zona;
import com.example.unmujeres.daos.BaseDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ZonaDao extends BaseDAO {
    public ArrayList<Zona> obtenerZonas() throws SQLException{
        ArrayList<Zona> lista = new ArrayList<>();
        String sql = "SELECT idzona, nombre FROM zona";

        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Zona zona = new Zona();
                zona.setIdzona(rs.getInt("idzona"));
                zona.setNombre(rs.getString("nombre"));
                lista.add(zona);
            }

        } //catch (SQLException e) {
            //e.printStackTrace();
        //}

        return lista;
    }


    public boolean existeZona(int idZona) throws SQLException {
        String sql = "SELECT COUNT(*) FROM zona WHERE idZona = ?";
        try (Connection con = this.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idZona);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        // Este método ya estaba bien, ya que declara throws SQLException y no la captura internamente.
        return false;
    }
}
