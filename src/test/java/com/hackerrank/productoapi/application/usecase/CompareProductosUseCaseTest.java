package com.hackerrank.productoapi.application.usecase;

import com.hackerrank.productoapi.domain.command.CompareProductosCommand;
import com.hackerrank.productoapi.domain.model.Producto;
import com.hackerrank.productoapi.domain.port.ProductoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompareProductosUseCaseTest {

    private final ProductoRepository repo = Mockito.mock(ProductoRepository.class);
    private final CompareProductosUseCase useCase = new CompareProductosUseCase(repo);

    private Producto p(int id, Map<String,String> especs) {
        return Producto.create(id, "P"+id, "img", "desc", new BigDecimal("1.0"), 4.0, especs);
    }

    @Test
    @DisplayName("Filtra especificaciones según claves indicadas")
    void filtersSpecifications() {
        var especs1 = Map.of("color","rojo","marca","A","peso","1");
        var especs2 = Map.of("color","azul","tamanio","10");
        when(repo.findByIds(List.of(1,2))).thenReturn(List.of(p(1, especs1), p(2, especs2)));

        var cmd = new CompareProductosCommand(List.of(1,2), List.of("color","marca"));
        var result = useCase.findProductos(cmd);

        var list = (List<Producto>) result;
        assertEquals(2, list.size());
        assertEquals(Map.of("color","rojo","marca","A"), list.get(0).getEspecificaciones());
        assertEquals(Map.of("color","azul"), list.get(1).getEspecificaciones());
    }

    @Test
    @DisplayName("Cuando no encuentra productos devuelve lista vacía")
    void returnsEmptyWhenNoProducts() {
        when(repo.findByIds(List.of(99))).thenReturn(List.of());
        var cmd = new CompareProductosCommand(List.of(99), List.of("color"));
        var result = useCase.findProductos(cmd);
        assertTrue(((List<?>)result).isEmpty());
    }

    @Test
    @DisplayName("Cuando la lista de especificaciones está vacía devuelve mapas vacíos")
    void emptySpecsProduceEmptyMaps() {
        var especs1 = Map.of("color","rojo","marca","A");
        when(repo.findByIds(List.of(1))).thenReturn(List.of(p(1, especs1)));
        var cmd = new CompareProductosCommand(List.of(1), List.of());
        var result = (List<Producto>) useCase.findProductos(cmd);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getEspecificaciones().isEmpty());
    }
}
