package com.sps.model;

import jakarta.persistence.*;

@Entity
@Table(name = "item_compra")
public class ItemCompra {

    public static final String SNS_PENDIENTE  = "PENDIENTE";
    public static final String SNS_ENPROCESO  = "ENPROCESO";
    public static final String SNS_APROBADO   = "APROBADO";
    public static final String SNS_RECHAZADO  = "RECHAZADO";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "compra_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Compra compra;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    private String estadoValidacionSns;
    private Double precio;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Compra getCompra() { return compra; }
    public void setCompra(Compra compra) { this.compra = compra; }

    public Plan getPlan() { return plan; }
    public void setPlan(Plan plan) { this.plan = plan; }

    public String getEstadoValidacionSns() { return estadoValidacionSns; }
    public void setEstadoValidacionSns(String estadoValidacionSns) { this.estadoValidacionSns = estadoValidacionSns; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
}
