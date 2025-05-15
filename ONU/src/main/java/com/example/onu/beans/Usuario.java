package com.example.onu.beans;

public class Usuario {

    private int idusuario;
    private String nombres;
    private String apellidos;
    private String contraseña;
    private int DNI;
    private String correo;
    private String direccion;
    private byte estado;
    private int roles_idroles;
    private Integer zona_idzona;
    private Integer distritos_iddistritos;
    private java.sql.Date fecha_incorporacion;
    private byte[] foto;
    private String cod_enc;

    //-------------------------------------------------
    public Usuario() {}

    //-------------------------------------------------
    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }
    //-------------------------------------------------
    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    //-------------------------------------------------
    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    //-------------------------------------------------
    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
    //-------------------------------------------------
    public int getDNI() {
        return DNI;
    }

    public void setDNI(int DNI) {
        this.DNI = DNI;
    }
    //-------------------------------------------------
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    //-------------------------------------------------
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    //-------------------------------------------------
    public byte getEstado() {
        return estado;
    }

    public void setEstado(byte estado) {
        this.estado = estado;
    }
    //-------------------------------------------------
    public int getRoles_idroles() {
        return roles_idroles;
    }

    public void setRoles_idroles(int roles_idroles) {
        this.roles_idroles = roles_idroles;
    }
    //-------------------------------------------------
    public Integer getZona_idzona() {
        return zona_idzona;
    }

    public void setZona_idzona(Integer zona_idzona) {
        this.zona_idzona = zona_idzona;
    }
    //-------------------------------------------------
    public Integer getDistritos_iddistritos() {
        return distritos_iddistritos;
    }

    public void setDistritos_iddistritos(Integer distritos_iddistritos) {
        this.distritos_iddistritos = distritos_iddistritos;
    }
    //-------------------------------------------------
    public java.sql.Date getFecha_incorporacion() {
        return fecha_incorporacion;
    }

    public void setFecha_incorporacion(java.sql.Date fecha_incorporacion) {
        this.fecha_incorporacion = fecha_incorporacion;
    }
    //-------------------------------------------------
    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
    //-------------------------------------------------
    public String getCod_enc() {
        return cod_enc;
    }

    public void setCod_enc(String cod_enc) {
        this.cod_enc = cod_enc;
    }
}

