package com.tiendagenerica.ms_ventas.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cedula_cliente", nullable = false)
    private String cedulaCliente;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private Double total;

    // Una venta tiene muchos detalles
    @OneToMany(mappedBy = "venta",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<DetalleVenta> detalles;
}