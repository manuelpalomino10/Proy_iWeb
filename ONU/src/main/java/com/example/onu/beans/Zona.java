package com.example.onu.beans;

public class Zona {
    private int idzona;
    private String nombre;

    /**
     * Constructor vacío necesario para que DAO pueda instanciar via new Zona()
     */
    public Zona() {
    }

    /**
     * Constructor de conveniencia
     */
    public Zona(int idzona, String nombre) {
        this.idzona = idzona;
        this.nombre = nombre;
    }

    public int getIdzona() {
        return idzona;
    }

    public void setIdzona(int idzona) {
        this.idzona = idzona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Útil para imprimir directamente la zona (por ejemplo en un <c:out> o toString)
     */
    @Override
    public String toString() {
        return nombre;
    }
}
