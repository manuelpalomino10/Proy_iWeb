package com.example.unmujeres.dtos;

public class ReporteDTO {
    private int idFormulario;
    private String nombreFormulario;
    private int idZona;
    private int idRol;
    private String fechaInicio;
    private String fechaFin;
    private int totalRegistros;

    public int getIdFormulario() {
        return idFormulario;
    }
    public void setIdFormulario(int idFormulario) {
        this.idFormulario = idFormulario;
    }

    public String getNombreFormulario() {
        return nombreFormulario;
    }
    public void setNombreFormulario(String nombreFormulario) {
        this.nombreFormulario = nombreFormulario;
    }

    public int getIdZona() {
        return idZona;
    }
    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public int getIdRol() {
        return idRol;
    }
    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }
    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getTotalRegistros() {
        return totalRegistros;
    }
    public void setTotalRegistros(int totalRegistros) {
        this.totalRegistros = totalRegistros;
    }
}

