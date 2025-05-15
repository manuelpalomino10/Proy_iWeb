package com.example.onu.beans;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "registro_respuestas")
public class RegistroRespuestas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idregistro_respuestas")
    private int idRegistroRespuestas;

    @Column(name = "fecha_registro")
    private Date fechaRegistro;

    @Column(name = "estado")
    private String estado;

    @ManyToOne
    @JoinColumn(name = "enc_has_formulario_idenc_has_formulario", referencedColumnName = "idenc_has_formulario", insertable = false, updatable = false)
    private EncHasFormulario encHasFormulario;

    //-------------------------------------------------

    public int getIdRegistroRespuestas() {
        return idRegistroRespuestas;
    }

    public void setIdRegistroRespuestas(int idRegistroRespuestas) {
        this.idRegistroRespuestas = idRegistroRespuestas;
    }
    //-------------------------------------------------
    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    //-------------------------------------------------
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    //-------------------------------------------------
    public EncHasFormulario getEncHasFormulario() {
        return encHasFormulario;
    }

    public void setEncHasFormulario(EncHasFormulario encHasFormulario) {
        this.encHasFormulario = encHasFormulario;
    }
}

