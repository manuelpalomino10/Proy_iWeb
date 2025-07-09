package com.example.unmujeres.dtos;

import com.example.unmujeres.beans.Formulario;

import java.sql.Date;

public class AsignacionDTO {
    private int idAsignacion;
    private Date fechaAsignacion;
    private Formulario formulario;
//    private int idFormulario;
//    private int nombreForm;
//    private Date fechaLimite;
    private int totalRegistros;
    private int esperadosEnc;


    public int getIdAsignacion() {
        return idAsignacion;
    }
    public void setIdAsignacion(int idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }
    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

//    public int getIdFormulario() {
//        return idFormulario;
//    }
//    public void setIdFormulario(int idFormulario) {
//        this.idFormulario = idFormulario;
//    }
//
//    public int getNombreForm() {
//        return nombreForm;
//    }
//    public void setNombreForm(int nombreForm) {
//        this.nombreForm = nombreForm;
//    }
//    public Date getFechaLimite() {
//    return fechaLimite;
//    }
//    public void setFechaLimite(Date fechaLimite) {
//        this.fechaLimite = fechaLimite;
//    }
    public Formulario getFormulario() {
        return formulario;
    }
    public void setFormulario(Formulario formulario) {
        this.formulario = formulario;
    }

    public int getTotalRegistros() {
        return totalRegistros;
    }
    public void setTotalRegistros(int totalRegistros) {
        this.totalRegistros = totalRegistros;
    }

    public int getEsperadosEnc() {
        return esperadosEnc;
    }
    public void setEsperadosEnc(int esperadosEnc) {
        this.esperadosEnc = esperadosEnc;
    }

}
