package com.tiendagenerica.ms_ventas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "detalle_venta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Venta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_venta", nullable = false)
    @JsonIgnore  // evita recursión infinita en JSON
    private Venta venta;

    @Column(name = "codigo_producto", nullable = false)
    private Long codigoProducto;

    // Guardamos el nombre para historial
    @Column(name = "nombre_producto", nullable = false)
    private String nombreProducto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @Column(nullable = false)
    private Double iva;

    @Column(nullable = false)
    private Double subtotal;
}