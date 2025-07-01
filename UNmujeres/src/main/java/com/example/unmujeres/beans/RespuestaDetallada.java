package com.example.unmujeres.beans;

public class RespuestaDetallada {
    private int idPregunta;
    private String enunciadoPregunta;
    private String tipoPregunta;
    private String respuesta;
    private String encuestador;
    private int idSeccion;
    private String nombreSeccion;

    // Constructor
    public RespuestaDetallada(int idPregunta, String enunciadoPregunta, String tipoPregunta,
                              String respuesta, String encuestador, int idSeccion, String nombreSeccion) {
        this.idPregunta = idPregunta;
        this.enunciadoPregunta = enunciadoPregunta;
        this.tipoPregunta = tipoPregunta;
        this.respuesta = respuesta;
        this.encuestador = encuestador;
        this.idSeccion = idSeccion;
        this.nombreSeccion = nombreSeccion;
    }

    // Getters
    public String getEnunciadoPregunta() {
        return enunciadoPregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public String getEncuestador() {
        return encuestador;
    }

    public String getNombreSeccion() {
        return nombreSeccion;
    }

    public String getTipoPregunta() {
        return tipoPregunta;
    }
}
