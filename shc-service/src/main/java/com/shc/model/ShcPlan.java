package com.shc.model;

import jakarta.persistence.*;

@Entity
@Table(name = "shc_plan")
public class ShcPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "registro_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private ShcRegistro registro;

    private String codigoPlan;
    private String nombrePlan;
    private Double precio;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ShcRegistro getRegistro() { return registro; }
    public void setRegistro(ShcRegistro registro) { this.registro = registro; }

    public String getCodigoPlan() { return codigoPlan; }
    public void setCodigoPlan(String codigoPlan) { this.codigoPlan = codigoPlan; }

    public String getNombrePlan() { return nombrePlan; }
    public void setNombrePlan(String nombrePlan) { this.nombrePlan = nombrePlan; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
}
