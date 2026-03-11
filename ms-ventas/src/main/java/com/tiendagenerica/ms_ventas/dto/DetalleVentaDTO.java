package com.tiendagenerica.ms_ventas.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DetalleVentaDTO {

    @NotNull(message = "El código del producto es obligatorio")
    private Long codigoProducto;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima es 1")
    private Integer cantidad;
}