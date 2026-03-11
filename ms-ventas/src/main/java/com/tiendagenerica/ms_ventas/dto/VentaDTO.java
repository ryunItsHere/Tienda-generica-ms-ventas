package com.tiendagenerica.ms_ventas.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class VentaDTO {

    @NotBlank(message = "La cédula del cliente es obligatoria")
    private String cedulaCliente;

    @NotEmpty(message = "La venta debe tener al menos un producto")
    private List<DetalleVentaDTO> detalles;
}