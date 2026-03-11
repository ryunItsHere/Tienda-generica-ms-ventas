package com.tiendagenerica.ms_ventas.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ClienteClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${ms.clientes.url}")
    private String clientesUrl;

    public boolean existeCliente(String cedula) {
        try {
            String url = clientesUrl
                    + "/clientes/existe/" + cedula;
            Boolean resultado = restTemplate
                    .getForObject(url, Boolean.class);
            return Boolean.TRUE.equals(resultado);
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            System.err.println(
                    ">>> Error al contactar MS-Clientes: "
                            + e.getMessage());
            return true;
        }
    }
}