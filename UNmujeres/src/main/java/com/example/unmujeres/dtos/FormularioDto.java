package com.example.unmujeres.dtos;

import java.util.Date;

public class FormularioDto {
    private int id;
    private String nombreForm;
    private Date fechaCreacion;
    private String nombreCat;
    private boolean estado;

    private int NRegCompletados;
    private int regEsperados;
    private Date fechaLimite;
    private int idEncHasFormulario;

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


    public int getNRegCompletados() {
        return NRegCompletados;
    }
    public void setNRegCompletados(int NRegCompletados) {
        this.NRegCompletados = NRegCompletados;
    }

    public int getRegEsperados() {
        return regEsperados;
    }
    public void setRegEsperados(int regEsperados) {
        this.regEsperados = regEsperados;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }
    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public int getIdEncHasFormulario() {
        return idEncHasFormulario;
    }
    public void setIdEncHasFormulario(int idEncHasFormulario) {
        this.idEncHasFormulario = idEncHasFormulario;
    }
}
