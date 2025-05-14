package com.example.onumujeres.beans;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Date;

public class Usuario implements Serializable {

    private int idusuario;          // Changed from idUsuario
    private String nombres;
    private String apellidos;
    private String contraseña;      // Changed from clave
    private int dni;                // Changed from String to int
    private String correo;
    private String direccion;
    private byte estado;
    private int roles_idroles;
    private Integer zona_idzona;
    private Integer distritos_iddistritos; // Changed from int to Integer
    private Date fecha_incorporacion;
    private Blob foto;
    private String cod_enc;

    // Constructores
    public Usuario() {
    }

    public Usuario(int idusuario, String nombres, String apellidos, String contraseña, int dni, String correo, String direccion, byte estado, int roles_idroles, Integer zona_idzona, Integer distritos_iddistritos, Date fecha_incorporacion, Blob foto, String cod_enc) {
        this.idusuario = idusuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.contraseña = contraseña;
        this.dni = dni;
        this.correo = correo;
        this.direccion = direccion;
        this.estado = estado;
        this.roles_idroles = roles_idroles;
        this.zona_idzona = zona_idzona;
        this.distritos_iddistritos = distritos_iddistritos;
        this.fecha_incorporacion = fecha_incorporacion;
        this.foto = foto;
        this.cod_enc = cod_enc;
    }

    // Getters y Setters
    public int getIdusuario() {        // Changed from getIdUsuario
        return idusuario;
    }

    public void setIdusuario(int idusuario) { // Changed from setIdUsuario
        this.idusuario = idusuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getDni() {           // Changed from String to int getter
        return dni;
    }

    public void setDni(int dni) {   // Changed from String to int setter
        this.dni = dni;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getDistritos_iddistritos() {  // Changed getter name and return type
        return distritos_iddistritos;
    }

    public void setDistritos_iddistritos(Integer distritos_iddistritos) { // Changed setter name and parameter type
        this.distritos_iddistritos = distritos_iddistritos;
    }

    public String getContraseña() {   // Changed from getClave
        return contraseña;
    }

    public void setContraseña(String contraseña) {  // Changed from setClave
        this.contraseña = contraseña;
    }

    public Blob getFoto() {         // Changed from getFotoPerfil and return type
        return foto;
    }

    public void setFoto(Blob foto) {    // Changed from setFotoPerfil and parameter type
        this.foto = foto;
    }

    public byte getEstado() {
        return estado;
    }

    public void setEstado(byte estado) {
        this.estado = estado;
    }

    public int getRoles_idroles() {
        return roles_idroles;
    }

    public void setRoles_idroles(int roles_idroles) {
        this.roles_idroles = roles_idroles;
    }

    public Integer getZona_idzona() {
        return zona_idzona;
    }

    public void setZona_idzona(Integer zona_idzona) {
        this.zona_idzona = zona_idzona;
    }

    public Date getFecha_incorporacion() {
        return fecha_incorporacion;
    }

    public void setFecha_incorporacion(Date fecha_incorporacion) {
        this.fecha_incorporacion = fecha_incorporacion;
    }

    public String getCod_enc() {
        return cod_enc;
    }

    public void setCod_enc(String cod_enc) {
        this.cod_enc = cod_enc;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idusuario=" + idusuario +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", contraseña='" + contraseña + '\'' +
                ", dni=" + dni +
                ", correo='" + correo + '\'' +
                ", direccion='" + direccion + '\'' +
                ", estado=" + estado +
                ", roles_idroles=" + roles_idroles +
                ", zona_idzona=" + zona_idzona +
                ", distritos_iddistritos=" + distritos_iddistritos +
                ", fecha_incorporacion=" + fecha_incorporacion +
                ", foto=" + foto +
                ", cod_enc='" + cod_enc + '\'' +
                '}';
    }
}