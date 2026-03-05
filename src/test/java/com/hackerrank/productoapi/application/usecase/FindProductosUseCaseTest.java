package com.hackerrank.productoapi.application.usecase;

import com.hackerrank.productoapi.domain.model.Producto;
import com.hackerrank.productoapi.domain.port.ProductoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindProductosUseCaseTest {

    private final ProductoRepository repo = Mockito.mock(ProductoRepository.class);
    private final FindProductosUseCase useCase = new FindProductosUseCase(repo);

    private Producto p(int id) {
        return Producto.create(id, "P"+id, "img", "desc", new BigDecimal("1.0"), 4.0, null);
    }

    @Test
    @DisplayName("Cuando ids es null o en blanco, delega en repository.findAll()")
    void nullOrBlankIdsCallsFindAll() {
        when(repo.findAll()).thenReturn(List.of(p(1), p(2)));

        var r1 = useCase.findProductos(null);
        assertNotNull(r1);
        verify(repo, times(1)).findAll();

        reset(repo);
        when(repo.findAll()).thenReturn(List.of(p(1)));
        var r2 = useCase.findProductos("   ");
        assertNotNull(r2);
        verify(repo, times(1)).findAll();
    }

    @Test
    @DisplayName("Cuando ids tiene valores, parsea y llama findByIds con la lista")
    void parseIdsAndCallFindByIds() {
        when(repo.findByIds(anyList())).thenReturn(List.of(p(1), p(3)));
        var result = useCase.findProductos("1,3");
        assertNotNull(result);

        ArgumentCaptor<List<Integer>> captor = ArgumentCaptor.forClass(List.class);
        verify(repo).findByIds(captor.capture());
        assertEquals(List.of(1,3), captor.getValue());
    }
}
