package com.example.onumujeres.daos;

import com.example.onumujeres.beans.Distrito;
import com.example.onumujeres.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DistritoDAO {

    // Obtener todos los distritos
    public List<Distrito> obtenerTodosDistritos() throws SQLException {
        List<Distrito> distritos = new ArrayList<>();
        String sql = "SELECT iddistritos, nombre, zona_idzona FROM distritos"; // Added zona_idzona

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Distrito distrito = new Distrito();
                distrito.setIddistritos(rs.getInt("iddistritos"));
                distrito.setNombre(rs.getString("nombre"));
                distrito.setZona_idzona(rs.getInt("zona_idzona")); // Added zona_idzona
                distritos.add(distrito);
            }

        }
        return distritos;
    }

    // Obtener Distrito por ID (Opcional, depende de tus necesidades)
    public Distrito obtenerDistritoPorId(int idDistrito) throws SQLException {
        Distrito distrito = null;
        String sql = "SELECT iddistritos, nombre, zona_idzona FROM distritos WHERE iddistritos = ?"; // Added zona_idzona

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idDistrito);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                distrito = new Distrito();
                distrito.setIddistritos(rs.getInt("iddistritos"));
                distrito.setNombre(rs.getString("nombre"));
                distrito.setZona_idzona(rs.getInt("zona_idzona")); // Added zona_idzona
            }

        }
        return distrito;
    }
}