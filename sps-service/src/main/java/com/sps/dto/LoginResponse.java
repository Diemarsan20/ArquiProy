package com.sps.dto;

public class LoginResponse {
    private String token;
    private String cedula;
    private String nombre;

    public LoginResponse(String token, String cedula, String nombre) {
        this.token  = token;
        this.cedula = cedula;
        this.nombre = nombre;
    }

    public String getToken()  { return token; }
    public String getCedula() { return cedula; }
    public String getNombre() { return nombre; }
}
