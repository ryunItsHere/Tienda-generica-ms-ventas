package com.tiendagenerica.ms_ventas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VentaResponseDTO {
    private Long id;
    private String cedulaCliente;
    private LocalDateTime fecha;
    private Double total;
    private List<DetalleResponseDTO> detalles;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DetalleResponseDTO {
        private Long codigoProducto;
        private String nombreProducto;
        private Integer cantidad;
        private Double precioUnitario;
        private Double iva;
        private Double subtotal;
    }
}