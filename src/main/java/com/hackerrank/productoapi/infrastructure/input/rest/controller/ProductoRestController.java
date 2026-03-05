package com.hackerrank.productoapi.infrastructure.input.rest.controller;

import org.springframework.web.bind.annotation.RestController;

import com.hackerrank.productoapi.application.usecase.CompareProductosUseCase;
import com.hackerrank.productoapi.application.usecase.FindProductosUseCase;
import com.hackerrank.productoapi.infrastructure.input.rest.mapper.ProductoApiMapper;
import com.hackerrank.productoapi.infrastructure.input.rest.model.CompareRequest;
import com.hackerrank.productoapi.infrastructure.input.rest.model.ProductoResponse;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/productos")
@AllArgsConstructor
@Tag(name = "Productos", description = "Operaciones para listar y comparar productos")
public class ProductoRestController {

    private FindProductosUseCase findProductosUseCase;
    private CompareProductosUseCase compareProductosUseCase;

    @GetMapping()
    @Operation(
            summary = "Listar productos",
            description = "Devuelve todos los productos o filtra por una lista de IDs separados por coma (ej: '1,3')."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de productos",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductoResponse.class)))) ,
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Error interno",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<Iterable<ProductoResponse>> getProductos(
            @Parameter(description = "IDs separados por coma para filtrar (ej: 1,3)", example = "1,3")
            @RequestParam(name= "ids", required = false) String ids
    ){
        var productos = findProductosUseCase.findProductos(ids);
        var body = ProductoApiMapper.toResponse(productos);
        return ResponseEntity.ok(body);
    }


    @PostMapping("/compare")
    @Operation(
            summary = "Comparar productos",
            description = "Dado un conjunto de IDs y de claves de especificaciones, devuelve los productos con sus especificaciones filtradas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comparación realizada",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductoResponse.class)))) ,
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Error interno",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<Iterable<ProductoResponse>> compare(
            @Valid @RequestBody /* OpenAPI infiere el schema desde la clase */ CompareRequest request
    ){
        var productos = compareProductosUseCase
                .findProductos(ProductoApiMapper.toCommand(request));
        var body = ProductoApiMapper.toResponse(productos);
        return ResponseEntity.ok(body);
    }

}
