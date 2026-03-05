package com.hackerrank.productoapi.domain.model.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DescripcionTest {

    @Test
    @DisplayName("Crea Descripcion válida y trimea valor")
    void createsValidDescripcion() {
        Descripcion d = new Descripcion("  Un producto  ");
        assertEquals("Un producto", d.value());
        assertEquals("Un producto", d.toString());
    }

    @Test
    @DisplayName("Descripcion nula o en blanco lanza IllegalArgumentException")
    void nullOrBlankThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Descripcion(null));
        assertThrows(IllegalArgumentException.class, () -> new Descripcion("   "));
    }
}
