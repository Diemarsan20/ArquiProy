package com.sps.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Compra {

    public static final String ESTADO_PENDIENTE_VALIDACION = "PENDIENTE_VALIDACION";
    public static final String ESTADO_VALIDADO             = "VALIDADO";
    public static final String ESTADO_PENDIENTE_PAGO       = "PENDIENTE_PAGO";
    public static final String ESTADO_PAGADO               = "PAGADO";
    public static final String ESTADO_TERMINADA            = "TERMINADA";
    public static final String ESTADO_RECHAZADO            = "RECHAZADO";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private String estadoCompra;
    private Double valorTotal;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ItemCompra> items;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public String getEstadoCompra() { return estadoCompra; }
    public void setEstadoCompra(String estadoCompra) { this.estadoCompra = estadoCompra; }

    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public List<ItemCompra> getItems() { return items; }
    public void setItems(List<ItemCompra> items) { this.items = items; }
}
