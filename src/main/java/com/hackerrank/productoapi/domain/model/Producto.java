package com.hackerrank.productoapi.domain.model;

import com.hackerrank.productoapi.domain.model.vo.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Entidad de dominio que representa un producto dentro del sistema.
 * <p>
 * Un {@code Producto} encapsula sus atributos principales como objetos de valor
 * ({@link Nombre}, {@link UrlImagen}, {@link Descripcion}, {@link Precio} y {@link Calificacion})
 * y mantiene además un mapa de {@code especificaciones} (pares clave/valor) que permiten
 * describir propiedades adicionales y variables entre distintos productos.
 * </p>
 * Esta clase no contiene lógica de persistencia ni de presentación; forma parte de la capa
 * de dominio y busca garantizar invarianzas mediante sus Value Objects.
 */
@Data
@AllArgsConstructor
public class Producto {

    /** Identificador técnico del producto (puede ser {@code null} antes de persistir). */
    private Integer id;
    /** Nombre del producto, modelado como Value Object. */
    private Nombre nombre;
    /** URL de la imagen representativa, como Value Object. */
    private UrlImagen urlImagen;
    /** Descripción breve o extendida del producto, como Value Object. */
    private Descripcion descripcion;
    /** Precio actual del producto, como Value Object. */
    private Precio precio;
    /** Calificación promedio del producto, como Value Object. */
    private Calificacion calificacion;
    /**
     * Especificaciones adicionales del producto expresadas como pares clave/valor.
     * Por ejemplo: color=rojo, peso=1kg, marca=XYZ, etc.
     */
    private Map<String, String> especificaciones;

    /**
     * Fábrica estática que crea un {@code Producto} sin especificar su {@code id}.
     * Útil para escenarios de alta/creación donde el identificador será asignado posteriormente.
     *
     * @param nombre nombre del producto (texto plano del Value Object)
     * @param urlImagen URL de la imagen del producto
     * @param descripcion descripción del producto
     * @param precio precio del producto
     * @param calificacion calificación promedio del producto
     * @param especificaciones mapa de especificaciones adicionales (puede ser {@code null} o vacío)
     * @return una nueva instancia de {@code Producto}
     */
    public static Producto create(String nombre,String urlImagen, String descripcion, BigDecimal precio, Double calificacion, Map<String, String> especificaciones){
        return create(null, nombre, urlImagen, descripcion, precio, calificacion, especificaciones);
    }

    /**
     * Fábrica estática que crea un {@code Producto} construyendo internamente los objetos de valor
     * a partir de los datos primitivos provistos.
     *
     * @param id identificador del producto (puede ser {@code null})
     * @param nombre nombre del producto (texto plano del Value Object)
     * @param urlImagen URL de la imagen del producto
     * @param descripcion descripción del producto
     * @param precio precio del producto
     * @param calificacion calificación promedio del producto
     * @param especificaciones mapa de especificaciones adicionales (puede ser {@code null} o vacío)
     * @return una nueva instancia de {@code Producto}
     */
    public static Producto create(Integer id, String nombre, String urlImagen, String descripcion, BigDecimal precio, Double calificacion, Map<String, String> especificaciones){
        var voNombre = new Nombre(nombre);
        var voUrlImagen = new UrlImagen(urlImagen);
        var voDescripcion = new Descripcion(descripcion);
        var voPrecio = new Precio(precio);
        var voCalificacion = new Calificacion(calificacion);

        return new Producto(id, voNombre, voUrlImagen, voDescripcion, voPrecio, voCalificacion, especificaciones);
    }

}
