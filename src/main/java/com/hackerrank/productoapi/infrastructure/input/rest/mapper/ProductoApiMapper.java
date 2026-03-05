package com.hackerrank.productoapi.infrastructure.input.rest.mapper;

import java.util.stream.StreamSupport;

import com.hackerrank.productoapi.domain.command.CompareProductosCommand;
import com.hackerrank.productoapi.domain.model.Producto;
import com.hackerrank.productoapi.infrastructure.input.rest.model.CompareRequest;
import com.hackerrank.productoapi.infrastructure.input.rest.model.ProductoResponse;

public class ProductoApiMapper {


    public static Producto toDomain(ProductoResponse response){

        Producto producto = Producto.create(
                response.getId(),
                response.getNombre(),
                response.getUrlImagen(),
                response.getDescripcion(),
                response.getPrecio(),
                response.getCalificacion(),
                response.getEspecificaciones()
        );

        return producto;
    }

    public static ProductoResponse toResponse(Producto producto){

        ProductoResponse entity = new ProductoResponse(
                producto.getId(),
                producto.getNombre().value(),
                producto.getUrlImagen().value(),
                producto.getDescripcion().value(),
                producto.getPrecio().value(),
                producto.getCalificacion().value(),
                producto.getEspecificaciones()
        );

        return entity;
    }

    public static Iterable<Producto> toDomain(Iterable<ProductoResponse> entityList){
        return StreamSupport
                .stream(entityList.spliterator(), false)
                .map(entity -> toDomain(entity))
                .toList();
    }

    public static Iterable<ProductoResponse> toResponse(Iterable<Producto> modelList){
        return StreamSupport
                .stream(modelList.spliterator(), false)
                .map(model -> toResponse(model))
                .toList();
    }

    public static CompareProductosCommand toCommand(CompareRequest request){
        return new CompareProductosCommand(request.getIds(), request.getEspecificaciones());
    }

}
