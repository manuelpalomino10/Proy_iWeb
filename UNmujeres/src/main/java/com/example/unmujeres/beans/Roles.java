package com.example.unmujeres.beans;

public class Roles {
    private int idroles;
    private String nombre;

    public Roles(int idroles, String nombre) {
        this.idroles = idroles;
        this.nombre = nombre;
    }

    public Roles() {
    }

    public int getIdRoles() {
        return idroles;
    }
    public void setIdRoles(int idroles) {
        this.idroles = idroles;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

