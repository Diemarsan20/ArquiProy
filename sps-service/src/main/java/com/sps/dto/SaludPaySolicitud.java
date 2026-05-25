package com.sps.dto;

public class SaludPaySolicitud {
    private String cedulaCliente;
    private String numeroCompra;
    private Double valorTotal;

    public SaludPaySolicitud() {}

    public SaludPaySolicitud(String cedulaCliente, String numeroCompra, Double valorTotal) {
        this.cedulaCliente = cedulaCliente;
        this.numeroCompra  = numeroCompra;
        this.valorTotal    = valorTotal;
    }

    public String getCedulaCliente() { return cedulaCliente; }
    public void setCedulaCliente(String cedulaCliente) { this.cedulaCliente = cedulaCliente; }

    public String getNumeroCompra() { return numeroCompra; }
    public void setNumeroCompra(String numeroCompra) { this.numeroCompra = numeroCompra; }

    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }
}
