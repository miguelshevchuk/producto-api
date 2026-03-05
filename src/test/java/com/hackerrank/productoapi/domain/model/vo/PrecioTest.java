package com.hackerrank.productoapi.domain.model.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PrecioTest {

    @Test
    @DisplayName("Crea Precio válido")
    void createsValidPrecio() {
        Precio p = new Precio(new BigDecimal("10.50"));
        assertEquals(new BigDecimal("10.50"), p.value());
        assertEquals("10.50", p.toString());
    }

    @Test
    @DisplayName("Precio nulo o negativo lanza IllegalArgumentException")
    void nullOrNegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Precio(null));
        assertThrows(IllegalArgumentException.class, () -> new Precio(new BigDecimal("-0.01")));
    }
}
