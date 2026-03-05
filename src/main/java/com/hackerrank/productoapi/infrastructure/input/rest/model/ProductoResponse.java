package com.hackerrank.productoapi.infrastructure.input.rest.model;

import java.math.BigDecimal;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.AllArgsConstructor;

/**
 * Modelo de salida para exponer productos vía API REST.
 */
@Data
@AllArgsConstructor
@Schema(name = "ProductoResponse", description = "Producto expuesto por la API")
public class ProductoResponse {

    @Schema(description = "Identificador del producto", example = "1")
    private Integer id;

    @Schema(description = "Nombre legible del producto", example = "Producto 1")
    private String nombre;

    @Schema(description = "URL de la imagen del producto", example = "https://example.com/img/p1.png")
    private String urlImagen;

    @Schema(description = "Descripción corta del producto", example = "Producto de prueba 1")
    private String descripcion;

    @Schema(description = "Precio actual del producto", example = "5.50")
    private BigDecimal precio;

    @Schema(description = "Calificación promedio (0..5)", example = "4.5")
    private Double calificacion;

    @Schema(description = "Mapa de especificaciones del producto (clave → valor)",
            example = "{\"color\":\"rojo\",\"marca\":\"IOS\"}")
    private Map<String, String> especificaciones;

}
