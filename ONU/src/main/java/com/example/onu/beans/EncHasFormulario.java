package com.example.onu.beans;
import java.util.Date;
import javax.persistence.*;


@Entity
@Table(name = "enc_has_formulario")
public class EncHasFormulario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idenc_has_formulario")
    private int idEncHasFormulario;

    @Column(name = "enc_idusuario")
    private int encIdUsuario;

    @ManyToOne
    @JoinColumn(name = "formulario_idformulario", referencedColumnName = "idformulario", insertable = false, updatable = false)
    private Formulario formulario;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "fecha_asignacion")
    private Date fechaAsignacion;

    //-------------------------------------------------
    public int getIdEncHasFormulario() {
        return idEncHasFormulario;
    }

    public void setIdEncHasFormulario(int idEncHasFormulario) {
        this.idEncHasFormulario = idEncHasFormulario;
    }
    //-------------------------------------------------

    public int getEncIdUsuario() {
        return encIdUsuario;
    }

    public void setEncIdUsuario(int encIdUsuario) {
        this.encIdUsuario = encIdUsuario;
    }

    //-------------------------------------------------

    public Formulario getFormulario() {
        return formulario;
    }

    public void setFormulario(Formulario formulario) {
        this.formulario = formulario;
    }
    //-------------------------------------------------

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }


    //-------------------------------------------------
    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }
}



