package com.shc.model;

// Basado en patrón de entidades de 05_textos_h2
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "shc_registro")
public class ShcRegistro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoCompra;
    private String cedulaCliente;
    private String nombreCliente;
    private String correoCliente;
    private LocalDateTime fechaRecepcion;

    @OneToMany(mappedBy = "registro", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ShcPlan> planes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoCompra() { return codigoCompra; }
    public void setCodigoCompra(String codigoCompra) { this.codigoCompra = codigoCompra; }

    public String getCedulaCliente() { return cedulaCliente; }
    public void setCedulaCliente(String cedulaCliente) { this.cedulaCliente = cedulaCliente; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getCorreoCliente() { return correoCliente; }
    public void setCorreoCliente(String correoCliente) { this.correoCliente = correoCliente; }

    public LocalDateTime getFechaRecepcion() { return fechaRecepcion; }
    public void setFechaRecepcion(LocalDateTime fechaRecepcion) { this.fechaRecepcion = fechaRecepcion; }

    public List<ShcPlan> getPlanes() { return planes; }
    public void setPlanes(List<ShcPlan> planes) { this.planes = planes; }
}
