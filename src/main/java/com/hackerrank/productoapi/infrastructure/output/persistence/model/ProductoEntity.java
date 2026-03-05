package com.hackerrank.productoapi.infrastructure.output.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Data
@Entity
@Table(name = "productos")
@AllArgsConstructor
@NoArgsConstructor
public class ProductoEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    @Column(name = "url_imagen")
    private String urlImagen;
    private String descripcion;
    private BigDecimal precio;
    private Double calificacion;

    @ElementCollection
    @CollectionTable(
            name = "producto_especificaciones",
            joinColumns = @JoinColumn(name = "producto_id")
    )
    @MapKeyColumn(name = "clave")
    @Column(name = "valor")
    private Map<String, String> especificaciones;
}
