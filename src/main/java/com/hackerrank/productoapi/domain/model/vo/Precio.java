package com.hackerrank.productoapi.domain.model.vo;

import java.math.BigDecimal;
import java.util.Objects;

public record Precio(BigDecimal value) {
    public Precio{

        if(Objects.isNull(value)){
            throw new IllegalArgumentException("Precio requerido");
        }

        if(value.compareTo(new BigDecimal(0)) == -1){
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
    }

    @Override
    public String toString(){return value.toString();}
}
