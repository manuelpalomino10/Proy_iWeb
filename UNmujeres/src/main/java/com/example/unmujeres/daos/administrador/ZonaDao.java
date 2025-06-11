package com.example.unmujeres.daos.administrador;

import com.example.unmujeres.beans.Zona;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ZonaDao extends BaseDao {
    public ArrayList<Zona> obtenerZonas() {
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

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
