package com.hackerrank.productoapi.infrastructure.input.rest.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "CompareRequest", description = "Payload para comparar productos filtrando por especificaciones")
public class CompareRequest {

    @NotNull
    @NotEmpty
    @Schema(description = "IDs de productos a comparar", example = "[1,3]", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Integer> ids;

    @NotNull
    @NotEmpty
    @Schema(description = "Claves de especificaciones a devolver por cada producto", example = "[\"color\",\"marca\"]", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<@NotBlank String> especificaciones;

}
