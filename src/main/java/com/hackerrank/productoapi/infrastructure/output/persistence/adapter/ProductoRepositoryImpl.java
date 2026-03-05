package com.hackerrank.productoapi.infrastructure.output.persistence.adapter;

import org.springframework.stereotype.Repository;

import com.hackerrank.productoapi.domain.model.Producto;
import com.hackerrank.productoapi.domain.port.ProductoRepository;
import com.hackerrank.productoapi.infrastructure.output.persistence.mapper.ProductoMapper;

import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Implementación del puerto {@link ProductoRepository} basada en la infraestructura de persistencia.
 * <p>
 * Actúa como adaptador entre la capa de dominio y la capa de datos: delega en el repositorio
 * CRUD de Spring Data para obtener entidades de persistencia y luego utiliza el {@link ProductoMapper}
 * para convertirlas a objetos de dominio {@link Producto}.
 * </p>
 */
@Repository
@AllArgsConstructor
public class ProductoRepositoryImpl implements ProductoRepository{

    /** Repositorio CRUD de Spring Data que opera sobre entidades de persistencia. */
    private final ProductoCRUDRepository productoCRUDRepository;

    /**
     * Recupera todos los productos disponibles en la base de datos y los mapea al modelo de dominio.
     *
     * @return colección iterable de {@link Producto}
     */
    @Override
    public Iterable<Producto> findAll() {
        return ProductoMapper.toDomain(productoCRUDRepository.findAll());
    }

    /**
     * Recupera los productos cuyos identificadores coinciden con la lista provista y los mapea al
     * modelo de dominio.
     *
     * @param ids lista de identificadores a buscar
     * @return colección iterable de {@link Producto} (posiblemente vacía si no hay coincidencias)
     */
    @Override
    public Iterable<Producto> findByIds(List<Integer> ids) {
        return ProductoMapper.toDomain(productoCRUDRepository.findAllById(ids));
    }

}
