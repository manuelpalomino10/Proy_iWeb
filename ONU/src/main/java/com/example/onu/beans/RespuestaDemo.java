package com.example.onu.beans;

import javax.persistence.*;

@Entity
@Table(name = "respuesta_demo")
public class RespuestaDemo {

    @Id
    @Column(name = "idrespuesta")
    private int idrespuesta;

    @Column(name = "respuesta")
    private String respuesta;

    @Column(name = "reg_respuestas_idregistro_respuestas")
    private int reg_respuestas_idregistro_respuestas;

    @Column(name = "pregunta_copy1_idpregunta")
    private int pregunta_copy1_idpregunta;

    //-------------------------------------------------
    public RespuestaDemo() {}

    //-------------------------------------------------
    public int getIdrespuesta() {
        return idrespuesta;
    }

    public void setIdrespuesta(int idrespuesta) {
        this.idrespuesta = idrespuesta;
    }
    //-------------------------------------------------
    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
    //-------------------------------------------------
    public int getReg_respuestas_idregistro_respuestas() {
        return reg_respuestas_idregistro_respuestas;
    }

    public void setReg_respuestas_idregistro_respuestas(int reg_respuestas_idregistro_respuestas) {
        this.reg_respuestas_idregistro_respuestas = reg_respuestas_idregistro_respuestas;
    }
    //-------------------------------------------------
    public int getPregunta_copy1_idpregunta() {
        return pregunta_copy1_idpregunta;
    }

    public void setPregunta_copy1_idpregunta(int pregunta_copy1_idpregunta) {
        this.pregunta_copy1_idpregunta = pregunta_copy1_idpregunta;
    }
}


