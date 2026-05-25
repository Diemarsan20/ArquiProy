package com.sps.model;

// Entidad basada en 05_textos_h2 — adaptada para SPS (añade cedula y password)
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String correo;

    @Column(unique = true)
    private String cedula;

    private String password;
    private LocalDateTime fechaRegistro;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
