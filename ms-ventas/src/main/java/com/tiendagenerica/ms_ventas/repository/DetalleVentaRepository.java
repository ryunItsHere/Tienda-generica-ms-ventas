package com.tiendagenerica.ms_ventas.repository;

import com.tiendagenerica.ms_ventas.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleVentaRepository
        extends JpaRepository<DetalleVenta, Long> {
}