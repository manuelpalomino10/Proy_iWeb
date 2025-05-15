package com.example.onu.beans;
import javax.persistence.*;

@Entity
@Table(name = "opciones_pregunta")
public class OpcionesPregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idopciones_pregunta")
    private int idOpcionesPregunta;

    @Column(name = "opciones")
    private String opciones;

    @Column(name = "pregunta_idpregunta")
    private int preguntaIdPregunta;

    //-------------------------------------------------

    public int getIdOpcionesPregunta() {
        return idOpcionesPregunta;
    }

    public void setIdOpcionesPregunta(int idOpcionesPregunta) {
        this.idOpcionesPregunta = idOpcionesPregunta;
    }
    //-------------------------------------------------
    public String getOpciones() {
        return opciones;
    }

    public void setOpciones(String opciones) {
        this.opciones = opciones;
    }
    //-------------------------------------------------
    public int getPreguntaIdPregunta() {
        return preguntaIdPregunta;
    }

    public void setPreguntaIdPregunta(int preguntaIdPregunta) {
        this.preguntaIdPregunta = preguntaIdPregunta;
    }
}

