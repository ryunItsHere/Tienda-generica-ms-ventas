package com.tiendagenerica.ms_ventas.service;

import com.tiendagenerica.ms_ventas.client.ClienteClient;
import com.tiendagenerica.ms_ventas.client.ProductoClient;
import com.tiendagenerica.ms_ventas.dto.*;
import com.tiendagenerica.ms_ventas.model.DetalleVenta;
import com.tiendagenerica.ms_ventas.model.Venta;
import com.tiendagenerica.ms_ventas.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteClient clienteClient;

    @Autowired
    private ProductoClient productoClient;

    // ─────────────────────────────────────────
    // Registrar venta
    // ─────────────────────────────────────────
    public VentaResponseDTO registrarVenta(VentaDTO dto) {

        // 1. Verificar que el cliente existe
        if (!clienteClient.existeCliente(
                dto.getCedulaCliente())) {
            throw new RuntimeException(
                    "El cliente con cédula "
                            + dto.getCedulaCliente()
                            + " no existe en el sistema");
        }

        // 2. Verificar stock y obtener datos de productos
        List<DetalleVenta> detalles = new ArrayList<>();
        double total = 0.0;

        for (DetalleVentaDTO detalleDTO : dto.getDetalles()) {

            // Verificar stock disponible
            if (!productoClient.hayStock(
                    detalleDTO.getCodigoProducto(),
                    detalleDTO.getCantidad())) {
                throw new RuntimeException(
                        "Stock insuficiente para el producto: "
                                + detalleDTO.getCodigoProducto());
            }

            // Obtener datos del producto
            Map<String, Object> producto = productoClient
                    .obtenerProducto(
                            detalleDTO.getCodigoProducto());

            if (producto == null) {
                throw new RuntimeException(
                        "Producto no encontrado: "
                                + detalleDTO.getCodigoProducto());
            }

            // Calcular valores
            Double precioVenta = Double.valueOf(
                    producto.get("precioVenta").toString());
            Double ivaCompra = Double.valueOf(
                    producto.get("ivacompra").toString());
            String nombreProducto = producto
                    .get("nombreProducto").toString();

            double subtotal = precioVenta
                    * detalleDTO.getCantidad();
            double ivaValor = subtotal * ivaCompra;
            double totalLinea = subtotal + ivaValor;

            // Crear detalle
            DetalleVenta detalle = new DetalleVenta();
            detalle.setCodigoProducto(
                    detalleDTO.getCodigoProducto());
            detalle.setNombreProducto(nombreProducto);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(precioVenta);
            detalle.setIva(ivaValor);
            detalle.setSubtotal(totalLinea);

            detalles.add(detalle);
            total += totalLinea;
        }

        // 3. Crear y guardar la venta
        Venta venta = new Venta();
        venta.setCedulaCliente(dto.getCedulaCliente());
        venta.setFecha(LocalDateTime.now());
        venta.setTotal(total);
        venta.setDetalles(detalles);

        // Asignar la venta a cada detalle
        detalles.forEach(d -> d.setVenta(venta));

        Venta ventaGuardada = ventaRepository.save(venta);

        // 4. Descontar stock de cada producto
        for (DetalleVentaDTO detalleDTO : dto.getDetalles()) {
            productoClient.descontarStock(
                    detalleDTO.getCodigoProducto(),
                    detalleDTO.getCantidad());
        }

        return convertirAResponse(ventaGuardada);
    }

    // ─────────────────────────────────────────
    // Listar todas las ventas
    // ─────────────────────────────────────────
    public List<VentaResponseDTO> listarVentas() {
        return ventaRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────
    // Buscar venta por ID
    // ─────────────────────────────────────────
    public VentaResponseDTO buscarPorId(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Venta no encontrada con id: " + id));
        return convertirAResponse(venta);
    }

    // ─────────────────────────────────────────
    // Listar ventas por cliente
    // → usado por MS-Reportes
    // ─────────────────────────────────────────
    public List<VentaResponseDTO> listarPorCliente(
            String cedula) {
        return ventaRepository
                .findByCedulaCliente(cedula)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────
    // Convertir Venta a VentaResponseDTO
    // ─────────────────────────────────────────
    private VentaResponseDTO convertirAResponse(Venta venta) {
        VentaResponseDTO response = new VentaResponseDTO();
        response.setId(venta.getId());
        response.setCedulaCliente(venta.getCedulaCliente());
        response.setFecha(venta.getFecha());
        response.setTotal(venta.getTotal());

        List<VentaResponseDTO.DetalleResponseDTO> detallesDTO =
                venta.getDetalles().stream().map(d -> {
                    VentaResponseDTO.DetalleResponseDTO det =
                            new VentaResponseDTO.DetalleResponseDTO();
                    det.setCodigoProducto(d.getCodigoProducto());
                    det.setNombreProducto(d.getNombreProducto());
                    det.setCantidad(d.getCantidad());
                    det.setPrecioUnitario(d.getPrecioUnitario());
                    det.setIva(d.getIva());
                    det.setSubtotal(d.getSubtotal());
                    return det;
                }).collect(Collectors.toList());

        response.setDetalles(detallesDTO);
        return response;
    }
}