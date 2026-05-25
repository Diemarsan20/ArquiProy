package com.sps.model;

import jakarta.persistence.*;

@Entity
@Table(name = "servicio_medico")
public class ServicioMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String tipo;
    private Double precio;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Plan plan;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Plan getPlan() { return plan; }
    public void setPlan(Plan plan) { this.plan = plan; }
}
