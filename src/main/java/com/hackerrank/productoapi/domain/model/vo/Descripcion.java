package com.hackerrank.productoapi.domain.model.vo;

import java.util.Objects;

public record Descripcion(String value) {
    public Descripcion{

        if(Objects.isNull(value) || value.trim().isBlank()){
            throw new IllegalArgumentException("Descripcion requerida");
        }


        value = value.trim();
    }

    @Override
    public String toString(){return value;}
}
