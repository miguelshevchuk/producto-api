package com.hackerrank.productoapi.domain.model.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NombreTest {

    @Test
    @DisplayName("Crea Nombre válido y trimea valor")
    void createsValidNombre() {
        Nombre n = new Nombre("  Telefono  ");
        assertEquals("Telefono", n.value());
        assertEquals("Telefono", n.toString());
    }

    @Test
    @DisplayName("Nombre nulo o en blanco lanza IllegalArgumentException")
    void nullOrBlankThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Nombre(null));
        assertThrows(IllegalArgumentException.class, () -> new Nombre("   "));
    }
}
