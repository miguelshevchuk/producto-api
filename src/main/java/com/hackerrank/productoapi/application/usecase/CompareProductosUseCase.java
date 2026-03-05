package com.hackerrank.productoapi.application.usecase;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.hackerrank.productoapi.domain.command.CompareProductosCommand;
import com.hackerrank.productoapi.domain.model.Producto;
import com.hackerrank.productoapi.domain.port.ProductoRepository;

/**
 * Caso de uso para comparar productos a partir de un conjunto de IDs y un subconjunto de
 * especificaciones.
 * <p>
 * Dado un {@link CompareProductosCommand}, obtiene los productos desde el repositorio y
 * retorna nuevas instancias que solo incluyen las especificaciones solicitadas, preservando
 * el resto de atributos (id, nombre, urlImagen, descripcion, precio y calificacion).
 * </p>
 * Esta clase pertenece a la capa de aplicación y actúa como orquestador; no contiene lógica de
 * persistencia.
 */
@Service
@AllArgsConstructor
public class CompareProductosUseCase {

    /**
     * Puerto de acceso al repositorio de productos.
     */
    private ProductoRepository productoRepository;

    /**
     * Obtiene los productos por sus identificadores y reduce sus especificaciones al conjunto
     * de claves indicado en el comando. Los datos originales del dominio no se modifican; se
     * construyen nuevas instancias con las especificaciones filtradas.
     *
     * @param command comando que contiene la lista de IDs de productos y las claves de
     *                especificaciones a incluir en el resultado
     * @return colección de productos con las especificaciones filtradas (puede estar vacía si
     *         no se encuentran productos)
     */
    public Iterable<Producto> findProductos(CompareProductosCommand command){

        Iterable<Producto> productos = productoRepository.findByIds(command.ids());

        var filtrados = StreamSupport.stream(productos.spliterator(), false)
                .map(p ->
                        Producto.create(
                                p.getId(),
                                p.getNombre().value(),
                                p.getUrlImagen().value(),
                                p.getDescripcion().value(),
                                p.getPrecio().value(),
                                p.getCalificacion().value(),
                                this.filtrarEspecificaciones(command.especificaciones(), p))
                )
                .toList();

        return filtrados;
    }

    /**
     * Filtra el mapa de especificaciones del producto dejando únicamente aquellas cuya clave
     * esté presente en la lista proporcionada.
     *
     * @param especificaciones lista de claves de especificación a conservar
     * @param producto producto fuente cuyas especificaciones serán filtradas
     * @return mapa de especificaciones filtrado (posiblemente vacío si ninguna clave coincide)
     */
    private Map<String, String> filtrarEspecificaciones(List<String> especificaciones, Producto producto){

        return producto.getEspecificaciones()
                .entrySet()
                .stream()
                .filter(e -> especificaciones.contains(e.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue
                ));


    }


}
