package com.example.onu.beans;

import java.util.Date;

public class Formulario {
    private int idformulario;
    private String nombre;
    private Date fecha_creacion;
    private Date fecha_limite;
    private byte estado;
    private Integer registros_esperados;
    private int categoria_idcategoria;
    //-------------------------------------------------
    public Formulario() {
    }
    //-------------------------------------------------
    public Formulario(int idformulario, String nombre, Date fecha_creacion, Date fecha_limite, byte estado, Integer registros_esperados, int categoria_idcategoria) {
        this.idformulario = idformulario;
        this.nombre = nombre;
        this.fecha_creacion = fecha_creacion;
        this.fecha_limite = fecha_limite;
        this.estado = estado;
        this.registros_esperados = registros_esperados;
        this.categoria_idcategoria = categoria_idcategoria;
    }
    //-------------------------------------------------
    public int getIdformulario() {
        return idformulario;
    }

    public void setIdformulario(int idformulario) {
        this.idformulario = idformulario;
    }
    //-------------------------------------------------
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    //-------------------------------------------------
    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }
    //-------------------------------------------------
    public Date getFecha_limite() {
        return fecha_limite;
    }

    public void setFecha_limite(Date fecha_limite) {
        this.fecha_limite = fecha_limite;
    }
    //-------------------------------------------------
    public byte getEstado() {
        return estado;
    }

    public void setEstado(byte estado) {
        this.estado = estado;
    }
    //-------------------------------------------------
    public Integer getRegistros_esperados() {
        return registros_esperados;
    }

    public void setRegistros_esperados(Integer registros_esperados) {
        this.registros_esperados = registros_esperados;
    }
    //-------------------------------------------------
    public int getCategoria_idcategoria() {
        return categoria_idcategoria;
    }

    public void setCategoria_idcategoria(int categoria_idcategoria) {
        this.categoria_idcategoria = categoria_idcategoria;
    }
}

