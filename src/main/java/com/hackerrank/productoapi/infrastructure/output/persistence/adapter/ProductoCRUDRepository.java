package com.hackerrank.productoapi.infrastructure.output.persistence.adapter;

import org.springframework.data.repository.CrudRepository;

import com.hackerrank.productoapi.infrastructure.output.persistence.model.ProductoEntity;

public interface ProductoCRUDRepository extends CrudRepository<ProductoEntity, Integer> {

}
