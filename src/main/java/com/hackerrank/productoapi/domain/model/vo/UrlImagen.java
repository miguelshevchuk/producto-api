package com.hackerrank.productoapi.domain.model.vo;

import java.util.Objects;

public record UrlImagen(String value) {
    public UrlImagen {

        if(Objects.isNull(value) || value.trim().isBlank()){
            throw new IllegalArgumentException("URL Imagen requerida");
        }


        value = value.trim();
    }

    @Override
    public String toString(){return value;}
}
