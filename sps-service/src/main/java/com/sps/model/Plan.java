package com.sps.model;

// Entidad equivalente a Producto de 05_textos_h2, adaptada como Plan de Salud
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String codigo;

    private String nombre;
    private String descripcion;
    private Double precio;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ServicioMedico> servicios;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public List<ServicioMedico> getServicios() { return servicios; }
    public void setServicios(List<ServicioMedico> servicios) { this.servicios = servicios; }
}
