package com.hackerrank.productoapi.domain.model.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlImagenTest {

    @Test
    @DisplayName("Crea UrlImagen válida y trimea valor")
    void createsValidUrlImagen() {
        UrlImagen u = new UrlImagen("  https://img.example/a.png  ");
        assertEquals("https://img.example/a.png", u.value());
        assertEquals("https://img.example/a.png", u.toString());
    }

    @Test
    @DisplayName("UrlImagen nula o en blanco lanza IllegalArgumentException")
    void nullOrBlankThrows() {
        assertThrows(IllegalArgumentException.class, () -> new UrlImagen(null));
        assertThrows(IllegalArgumentException.class, () -> new UrlImagen("   "));
    }
}
