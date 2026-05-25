package com.sps.dto;

import java.util.List;

public class CompraNotificacionSAM {
    private String codigoCompra;
    private String cedulaCliente;
    private List<ServicioInfo> servicios;

    public static class ServicioInfo {
        private String nombreServicio;
        private String tipo;
        private String nombrePlan;

        public ServicioInfo() {}

        public ServicioInfo(String nombreServicio, String tipo, String nombrePlan) {
            this.nombreServicio = nombreServicio;
            this.tipo           = tipo;
            this.nombrePlan     = nombrePlan;
        }

        public String getNombreServicio() { return nombreServicio; }
        public void setNombreServicio(String nombreServicio) { this.nombreServicio = nombreServicio; }

        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }

        public String getNombrePlan() { return nombrePlan; }
        public void setNombrePlan(String nombrePlan) { this.nombrePlan = nombrePlan; }
    }

    public String getCodigoCompra() { return codigoCompra; }
    public void setCodigoCompra(String codigoCompra) { this.codigoCompra = codigoCompra; }

    public String getCedulaCliente() { return cedulaCliente; }
    public void setCedulaCliente(String cedulaCliente) { this.cedulaCliente = cedulaCliente; }

    public List<ServicioInfo> getServicios() { return servicios; }
    public void setServicios(List<ServicioInfo> servicios) { this.servicios = servicios; }
}
