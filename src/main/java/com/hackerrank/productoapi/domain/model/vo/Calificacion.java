package com.hackerrank.productoapi.domain.model.vo;

import java.util.Objects;

public record Calificacion(Double value) {
    public Calificacion {

        if(Objects.isNull(value)){
            throw new IllegalArgumentException("Tamaño requerido");
        }

        if(value < 0){
            throw new IllegalArgumentException("El tamaño debe ser mayor a 0");
        }
    }

    @Override
    public String toString(){return value.toString();}
}
