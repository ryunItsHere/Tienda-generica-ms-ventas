package com.tiendagenerica.ms_ventas.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VentaResponseDTO {
    private Long id;
    private String cedulaCliente;
    private LocalDateTime fecha;
    private Double total;
    private List<DetalleResponseDTO> detalles;

    @Data
    public static class DetalleResponseDTO {
        private Long codigoProducto;
        private String nombreProducto;
        private Integer cantidad;
        private Double precioUnitario;
        private Double iva;
        private Double subtotal;
    }
}