package com.sam.model;

// Basado en patrón de entidades de 05_textos_h2
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agenda_item")
public class AgendaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoCompra;
    private String cedulaCliente;
    private String nombreServicio;
    private String tipoServicio;
    private String nombrePlan;
    private LocalDateTime fechaRecepcion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoCompra() { return codigoCompra; }
    public void setCodigoCompra(String codigoCompra) { this.codigoCompra = codigoCompra; }

    public String getCedulaCliente() { return cedulaCliente; }
    public void setCedulaCliente(String cedulaCliente) { this.cedulaCliente = cedulaCliente; }

    public String getNombreServicio() { return nombreServicio; }
    public void setNombreServicio(String nombreServicio) { this.nombreServicio = nombreServicio; }

    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }

    public String getNombrePlan() { return nombrePlan; }
    public void setNombrePlan(String nombrePlan) { this.nombrePlan = nombrePlan; }

    public LocalDateTime getFechaRecepcion() { return fechaRecepcion; }
    public void setFechaRecepcion(LocalDateTime fechaRecepcion) { this.fechaRecepcion = fechaRecepcion; }
}
