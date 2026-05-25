package com.sps.dto;

public class PagoNotificacion {
    private String cedulaCliente;
    private String numeroCompra;
    private Double valorPagado;

    public String getCedulaCliente() { return cedulaCliente; }
    public void setCedulaCliente(String cedulaCliente) { this.cedulaCliente = cedulaCliente; }

    public String getNumeroCompra() { return numeroCompra; }
    public void setNumeroCompra(String numeroCompra) { this.numeroCompra = numeroCompra; }

    public Double getValorPagado() { return valorPagado; }
    public void setValorPagado(Double valorPagado) { this.valorPagado = valorPagado; }
}
