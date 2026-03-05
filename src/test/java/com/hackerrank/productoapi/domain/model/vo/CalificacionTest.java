package com.hackerrank.productoapi.domain.model.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalificacionTest {

    @Test
    @DisplayName("Crea Calificacion válida")
    void createsValidCalificacion() {
        Calificacion c = new Calificacion(4.0);
        assertEquals(4.0, c.value());
        assertEquals("4.0", c.toString());
    }

    @Test
    @DisplayName("Calificacion nula o negativa lanza IllegalArgumentException")
    void nullOrNegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Calificacion(null));
        assertThrows(IllegalArgumentException.class, () -> new Calificacion(-0.1));
    }
}
