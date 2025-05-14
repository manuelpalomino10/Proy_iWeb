package com.example.onumujeres.beans;

import java.io.Serializable;

public class Distrito implements Serializable {

    private int iddistritos; // Updated attribute name
    private String nombre;    // Updated attribute name
    private int zona_idzona;  // Added attribute

    // Constructors
    public Distrito() {
    }

    public Distrito(int iddistritos, String nombre, int zona_idzona) {
        this.iddistritos = iddistritos;
        this.nombre = nombre;
        this.zona_idzona = zona_idzona;
    }

    // Getters y Setters
    public int getIddistritos() {
        return iddistritos;
    }

    public void setIddistritos(int iddistritos) {
        this.iddistritos = iddistritos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getZona_idzona() {
        return zona_idzona;
    }

    public void setZona_idzona(int zona_idzona) {
        this.zona_idzona = zona_idzona;
    }

    @Override
    public String toString() {
        return "Distrito{" +
                "iddistritos=" + iddistritos +
                ", nombre='" + nombre + '\'' +
                ", zona_idzona=" + zona_idzona +
                '}';
    }
}