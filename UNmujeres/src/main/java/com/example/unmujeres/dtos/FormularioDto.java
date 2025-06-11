package com.example.unmujeres.dtos;

import java.util.Date;

public class FormularioDto {
    private int id;
    private String nombreForm;
    private Date fechaCreacion;
    private String nombreCat;
    private boolean estado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreForm() {
        return nombreForm;
    }

    public void setNombreForm(String nombreForm) {
        this.nombreForm = nombreForm;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getNombreCat() {
        return nombreCat;
    }

    public void setNombreCat(String nombreCat) {
        this.nombreCat = nombreCat;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
