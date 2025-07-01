package com.example.unmujeres.beans;

import java.util.List;
import java.util.Map;

public class EstadisticasFormulario {
    private Map<String, Map<String, List<RespuestaDetallada>>> respuestasAgrupadas;
    private Map<String, Map<String, Long>> datosGraficos;

    // Getters y Setters
    public Map<String, Map<String, List<RespuestaDetallada>>> getRespuestasAgrupadas() {
        return respuestasAgrupadas;
    }

    public void setRespuestasAgrupadas(Map<String, Map<String, List<RespuestaDetallada>>> respuestasAgrupadas) {
        this.respuestasAgrupadas = respuestasAgrupadas;
    }

    public Map<String, Map<String, Long>> getDatosGraficos() {
        return datosGraficos;
    }

    public void setDatosGraficos(Map<String, Map<String, Long>> datosGraficos) {
        this.datosGraficos = datosGraficos;
    }
}
