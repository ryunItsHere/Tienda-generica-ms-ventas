package com.tiendagenerica.ms_ventas.controller;

import com.tiendagenerica.ms_ventas.dto.VentaDTO;
import com.tiendagenerica.ms_ventas.dto.VentaResponseDTO;
import com.tiendagenerica.ms_ventas.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    // Registrar nueva venta
    @PostMapping("/registrar")
    public ResponseEntity<VentaResponseDTO> registrar(
            @Valid @RequestBody VentaDTO dto) {
        return ResponseEntity.ok(
                ventaService.registrarVenta(dto));
    }

    // Listar todas las ventas
    @GetMapping("/listar")
    public ResponseEntity<List<VentaResponseDTO>> listar() {
        return ResponseEntity.ok(
                ventaService.listarVentas());
    }

    // Buscar venta por ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<VentaResponseDTO> buscar(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ventaService.buscarPorId(id));
    }

    // Listar ventas por cliente → usado por MS-Reportes
    @GetMapping("/cliente/{cedula}")
    public ResponseEntity<List<VentaResponseDTO>> porCliente(
            @PathVariable String cedula) {
        return ResponseEntity.ok(
                ventaService.listarPorCliente(cedula));
    }
}