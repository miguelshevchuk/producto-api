package com.hackerrank.productoapi.domain.port;

import com.hackerrank.productoapi.domain.model.Producto;

import java.util.List;

public interface ProductoRepository {

    Iterable<Producto> findAll();
    Iterable<Producto> findByIds(List<Integer> ids);
}
