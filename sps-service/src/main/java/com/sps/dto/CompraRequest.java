package com.sps.dto;

import java.util.List;

public class CompraRequest {
    private String cedulaCliente;
    private List<Long> planIds;

    public String getCedulaCliente() { return cedulaCliente; }
    public void setCedulaCliente(String cedulaCliente) { this.cedulaCliente = cedulaCliente; }

    public List<Long> getPlanIds() { return planIds; }
    public void setPlanIds(List<Long> planIds) { this.planIds = planIds; }
}
