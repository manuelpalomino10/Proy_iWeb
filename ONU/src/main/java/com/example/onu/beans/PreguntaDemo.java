package com.example.onu.beans;


import javax.persistence.*;

@Entity
@Table(name = "pregunta_demo")
public class PreguntaDemo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpregunta")
    private int idpregunta;

    @Column(name = "enunciado")
    private String enunciado;

    @ManyToOne
    @JoinColumn(name = "seccion_idseccion", referencedColumnName = "idseccion")
    private Seccion seccion;

    //-------------------------------------------------
    public PreguntaDemo() {
    }

    //-------------------------------------------------
    public PreguntaDemo(int idpregunta, String enunciado, Seccion seccion) {
        this.idpregunta = idpregunta;
        this.enunciado = enunciado;
        this.seccion = seccion;
    }

    //-------------------------------------------------
    public int getIdpregunta() {
        return idpregunta;
    }

    public void setIdpregunta(int idpregunta) {
        this.idpregunta = idpregunta;
    }
    //-------------------------------------------------
    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }
    //-------------------------------------------------
    public Seccion getSeccion() {
        return seccion;
    }

    public void setSeccion(Seccion seccion) {
        this.seccion = seccion;
    }


}

