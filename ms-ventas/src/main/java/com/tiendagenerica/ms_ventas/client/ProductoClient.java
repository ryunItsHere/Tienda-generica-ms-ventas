package com.tiendagenerica.ms_ventas.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import java.util.Map;

@Component
public class ProductoClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${ms.productos.url}")
    private String productosUrl;

    // Verifica si hay stock suficiente
    public boolean hayStock(Long codigo, Integer cantidad) {
        try {
            String url = productosUrl
                    + "/productos/stock/" + codigo
                    + "/" + cantidad;
            Boolean resultado = restTemplate
                    .getForObject(url, Boolean.class);
            return Boolean.TRUE.equals(resultado);
        } catch (Exception e) {
            System.err.println(
                    ">>> Error al verificar stock: "
                            + e.getMessage());
            return true;
        }
    }

    // Obtiene precio y nombre del producto
    public Map<String, Object> obtenerProducto(Long codigo) {
        try {
            String url = productosUrl
                    + "/productos/buscar/" + codigo;
            return restTemplate.getForObject(
                    url, Map.class);
        } catch (Exception e) {
            System.err.println(
                    ">>> Error al obtener producto: "
                            + e.getMessage());
            return null;
        }
    }

    // Descuenta stock después de confirmar venta
    public void descontarStock(Long codigo, Integer cantidad) {
        try {
            String url = productosUrl
                    + "/productos/descontar-stock/"
                    + codigo + "/" + cantidad;
            restTemplate.put(url, null);
        } catch (Exception e) {
            System.err.println(
                    ">>> Error al descontar stock: "
                            + e.getMessage());
        }
    }
}