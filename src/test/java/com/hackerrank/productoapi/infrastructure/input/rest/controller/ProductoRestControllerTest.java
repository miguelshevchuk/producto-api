package com.hackerrank.productoapi.infrastructure.input.rest.controller;

import com.hackerrank.productoapi.application.usecase.CompareProductosUseCase;
import com.hackerrank.productoapi.application.usecase.FindProductosUseCase;
import com.hackerrank.productoapi.domain.model.Producto;
import com.hackerrank.productoapi.infrastructure.input.rest.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductoRestControllerTest {

    private MockMvc mockMvc;
    private FindProductosUseCase findProductosUseCase;
    private CompareProductosUseCase compareProductosUseCase;

    @BeforeEach
    void setup() {
        findProductosUseCase = Mockito.mock(FindProductosUseCase.class);
        compareProductosUseCase = Mockito.mock(CompareProductosUseCase.class);
        ProductoRestController controller = new ProductoRestController(findProductosUseCase, compareProductosUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private Producto p(Integer id, String nombre) {
        return Producto.create(id, nombre, "testImg", "Descripcion", new BigDecimal("10.00"), Double.valueOf("4"), Map.of("color", "rojo", "marca", "X"));
    }

    @Test
    @DisplayName("GET /productos sin ids devuelve todos los productos (200)")
    void getAllProducts_ok() throws Exception {
        when(findProductosUseCase.findProductos(null)).thenReturn(List.of(p(1, "A"), p(2, "B")));

        mockMvc.perform(get("/productos").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("A"))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    @DisplayName("GET /productos?ids=1,3 devuelve solo los solicitados (200)")
    void getProducts_byIds_ok() throws Exception {
        when(findProductosUseCase.findProductos("1,3")).thenReturn(List.of(p(1, "A"), p(3, "C")));

        mockMvc.perform(get("/productos").param("ids", "1,3").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("GET /productos?ids=a,b produce error de parseo → 400 ProblemDetail")
    void getProducts_invalidIds_error() throws Exception {
        when(findProductosUseCase.findProductos("a,b")).thenThrow(new NumberFormatException("For input string: 'a'"));

        mockMvc.perform(get("/productos").param("ids", "a,b").accept(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Argumento inválido"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.instance").value("/productos"));
    }

    @Test
    @DisplayName("POST /productos/compare con cuerpo válido devuelve 200 y productos filtrados")
    void compare_ok() throws Exception {
        var out = List.of(
                Producto.create(1, "A", "img", "desc", new BigDecimal("10.00"), 4.0, Map.of("color", "rojo")),
                Producto.create(3, "C", "img", "desc", new BigDecimal("12.00"), 3.5, Map.of("marca", "Y"))
        );
        when(compareProductosUseCase.findProductos(Mockito.any())).thenReturn(out);

        String body = "{" +
                "\"ids\":[1,3]," +
                "\"especificaciones\":[\"color\",\"marca\"]" +
                "}";

        mockMvc.perform(post("/productos/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].especificaciones.color").value("rojo"))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].especificaciones.marca").value("Y"));
    }

    @Test
    @DisplayName("POST /productos/compare con JSON inválido → 400 ProblemDetail")
    void compare_invalidJson_badRequest() throws Exception {
        String invalidBody = "{ ids: [1,3], especificaciones: [color, marca]"; // faltan comillas y llave de cierre

        mockMvc.perform(post("/productos/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_PROBLEM_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Body inválido"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.instance").value("/productos/compare"));
    }

    @Test
    @DisplayName("POST /productos/compare con error de negocio → 400 ProblemDetail")
    void compare_businessError_badRequest() throws Exception {
        when(compareProductosUseCase.findProductos(Mockito.any())).thenThrow(new IllegalArgumentException("Datos inválidos para comparar"));

        String body = "{" +
                "\"ids\":[1,3]," +
                "\"especificaciones\":[\"color\",\"marca\"]" +
                "}";

        mockMvc.perform(post("/productos/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_PROBLEM_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Argumento inválido"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Datos inválidos para comparar"));
    }
}
