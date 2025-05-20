package com.example.onu.daos;

import com.example.onu.DatabaseConnection;
import com.example.onu.beans.Zona;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ZonaDAO {

    /**
     * Lee todas las zonas de la tabla `zona` y las devuelve como lista de beans Zona.
     */
    public List<Zona> listarZonas() throws SQLException {
        String sql = "SELECT idzona, nombre FROM zona ORDER BY idzona";
        List<Zona> zonas = new ArrayList<>();

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                // Aquí usamos el constructor Zona(int idzona, String nombre)
                Zona z = new Zona(
                        rs.getInt("idzona"),
                        rs.getString("nombre")
                );
                zonas.add(z);
            }
        }

        return zonas;
    }
}
