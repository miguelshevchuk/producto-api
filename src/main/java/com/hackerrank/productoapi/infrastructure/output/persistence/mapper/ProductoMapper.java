package com.hackerrank.productoapi.infrastructure.output.persistence.mapper;

import java.util.stream.StreamSupport;

import com.hackerrank.productoapi.domain.model.Producto;
import com.hackerrank.productoapi.infrastructure.output.persistence.model.ProductoEntity;

//Lo ideal seria tener un Mapper de mapStruct, pero este IDE da error al usarlo!
public class ProductoMapper {

    public static Producto toDomain(ProductoEntity entity){

        Producto producto = Producto.create(
                entity.getId(),
                entity.getNombre(),
                entity.getUrlImagen(),
                entity.getDescripcion(),
                entity.getPrecio(),
                entity.getCalificacion(),
                entity.getEspecificaciones()
        );

        return producto;
    }

    public static ProductoEntity toEntity(Producto producto){

        ProductoEntity entity = new ProductoEntity(
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

    public static Iterable<Producto> toDomain(Iterable<ProductoEntity> entityList){
        return StreamSupport
                .stream(entityList.spliterator(), false)
                .map(entity -> toDomain(entity))
                .toList();
    }

    public static Iterable<ProductoEntity> toEntity(Iterable<Producto> modelList){
        return StreamSupport
                .stream(modelList.spliterator(), false)
                .map(model -> toEntity(model))
                .toList();
    }

}
