package com.sps.dto;

public class SnsRespuesta {
    private String codigoPlan;
    private String estado; // APROBADO | RECHAZADO | ENPROCESO

    public String getCodigoPlan() { return codigoPlan; }
    public void setCodigoPlan(String codigoPlan) { this.codigoPlan = codigoPlan; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
