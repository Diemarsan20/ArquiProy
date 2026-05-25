package com.sps.dto;

import java.util.List;

public class CompraNotificacionSHC {
    private String codigoCompra;
    private String cedulaCliente;
    private String nombreCliente;
    private String correoCliente;
    private List<PlanInfo> planes;

    public static class PlanInfo {
        private String codigoPlan;
        private String nombrePlan;
        private Double precio;

        public PlanInfo() {}

        public PlanInfo(String codigoPlan, String nombrePlan, Double precio) {
            this.codigoPlan  = codigoPlan;
            this.nombrePlan  = nombrePlan;
            this.precio      = precio;
        }

        public String getCodigoPlan() { return codigoPlan; }
        public void setCodigoPlan(String codigoPlan) { this.codigoPlan = codigoPlan; }

        public String getNombrePlan() { return nombrePlan; }
        public void setNombrePlan(String nombrePlan) { this.nombrePlan = nombrePlan; }

        public Double getPrecio() { return precio; }
        public void setPrecio(Double precio) { this.precio = precio; }
    }

    public String getCodigoCompra() { return codigoCompra; }
    public void setCodigoCompra(String codigoCompra) { this.codigoCompra = codigoCompra; }

    public String getCedulaCliente() { return cedulaCliente; }
    public void setCedulaCliente(String cedulaCliente) { this.cedulaCliente = cedulaCliente; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getCorreoCliente() { return correoCliente; }
    public void setCorreoCliente(String correoCliente) { this.correoCliente = correoCliente; }

    public List<PlanInfo> getPlanes() { return planes; }
    public void setPlanes(List<PlanInfo> planes) { this.planes = planes; }
}
