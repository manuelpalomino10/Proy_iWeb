package com.example.onu.daos;

import com.example.onu.DatabaseConnection;
import com.example.onu.beans.Distritos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DistritoDAO {
    public List<Distritos> listarDistritos() throws SQLException {
        String sql = "SELECT iddistritos, nombre, idzona FROM distritos ORDER BY nombre";
        List<Distritos> lista = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
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
