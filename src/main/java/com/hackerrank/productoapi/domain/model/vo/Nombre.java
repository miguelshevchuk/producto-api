package com.hackerrank.productoapi.domain.model.vo;

import java.util.Objects;

public record Nombre(String value) {
    public Nombre{

        if(Objects.isNull(value) || value.trim().isBlank()){
            throw new IllegalArgumentException("Nombre requerido");
        }

        value = value.trim();
    }

    @Override
    public String toString(){return value;}
}
