package com.example.unmujeres.beans;

import com.example.unmujeres.beans.Categoria;

import java.util.Date;

public class Formulario {
    private int idFormulario;
    private String nombre;
    private Date fechaCreacion;
    private Date fechaLimite;
    private boolean estado;
    private int registrosEsperados;
    // cantidad de respuestas registradas para este formulario cuando se consultaAdd commentMore actions
    // asignaciones de encuestadores. Usado para determinar si puede desasignarse
    private int respuestasCount;
    Categoria categoria;

    public Formulario(){
    }


    // Nuevo constructor con parámetros
    public Formulario(int idFormulario, String nombre) {
        this.idFormulario = idFormulario;
        this.nombre = nombre;
    }


    public int getIdFormulario() {
        return idFormulario;
    }
    public void setIdFormulario(int idFormulario) {
        this.idFormulario = idFormulario;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }
    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public int getRegistrosEsperados() {
        return registrosEsperados;
    }
    public void setRegistrosEsperados(int registrosEsperados) {
        this.registrosEsperados = registrosEsperados;
    }

    public boolean isEstado() {
        return estado;
    }
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Categoria getCategoria() {
        return categoria;
    }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    public int getRespuestasCount() {
        return respuestasCount;
    }

    public void setRespuestasCount(int respuestasCount) {
        this.respuestasCount = respuestasCount;
    }

}
