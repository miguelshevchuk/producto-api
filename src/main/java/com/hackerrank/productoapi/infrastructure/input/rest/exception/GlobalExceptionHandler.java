package com.hackerrank.productoapi.infrastructure.input.rest.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Gestor de excepciones para la capa REST.
 * <p>
 * Centraliza el tratamiento de errores comunes producidos durante el
 * procesamiento de peticiones HTTP y retorna respuestas estandarizadas
 * </p>
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Maneja errores de validación sobre el body de la petición anotado con {@code @Valid}.
     *
     * @param ex      excepción lanzada por el validador
     * @param request petición HTTP actual (para obtener el path)
     * @return {@link ProblemDetail} con estado 400 y el detalle de los campos inválidos
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                      HttpServletRequest request) {
        var fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> Map.of(
                        "field", err.getField(),
                        "rejectedValue", String.valueOf(err.getRejectedValue()),
                        "message", err.getDefaultMessage()))
                .toList();

        ProblemDetail pd = buildProblemDetail(HttpStatus.BAD_REQUEST,
                "Request invalido",
                "El request tiene datos invalidos.",
                request);
        pd.setProperty("errors", fieldErrors);

        log.error("Validaciones fallidas {}: {}", request.getRequestURI(), fieldErrors, ex);
        log.debug("Validaciones fallidas {}: {}", request.getRequestURI(), fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    /**
     * Maneja errores de validación para parámetros de consulta, path variables, etc.
     *
     * @param ex      excepción de binding
     * @param request petición HTTP
     * @return {@link ProblemDetail} con estado 400 y lista de errores
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(BindException ex, HttpServletRequest request) {
        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> Map.of(
                        "field", err.getField(),
                        "rejectedValue", String.valueOf(err.getRejectedValue()),
                        "message", err.getDefaultMessage()))
                .collect(Collectors.toList());

        ProblemDetail pd = buildProblemDetail(HttpStatus.BAD_REQUEST,
                "Parámetros inválidos",
                "Uno o más parámetros son inválidos.",
                request);
        pd.setProperty("errors", errors);
        log.error("Errores de binding en {}: {}", request.getRequestURI(), errors, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    /**
     * Maneja violaciones de restricciones bean validation para parámetros (p.ej. {@code @Size} en query params).
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(ConstraintViolationException ex,
                                                                   HttpServletRequest request) {
        List<Map<String, String>> errors = ex.getConstraintViolations().stream()
                .map(v -> Map.of(
                        "property", v.getPropertyPath().toString(),
                        "invalidValue", String.valueOf(v.getInvalidValue()),
                        "message", v.getMessage()))
                .toList();

        ProblemDetail pd = buildProblemDetail(HttpStatus.BAD_REQUEST,
                "Violación de restricciones",
                "Se detectaron violaciones de validación.",
                request);
        pd.setProperty("errors", errors);
        log.error("Violaciones de validación en {}: {}", request.getRequestURI(), errors, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    /** Maneja parámetros requeridos ausentes. */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ProblemDetail> handleMissingParam(MissingServletRequestParameterException ex,
                                                            HttpServletRequest request) {
        ProblemDetail pd = buildProblemDetail(HttpStatus.BAD_REQUEST,
                "Parámetro requerido ausente",
                String.format("Falta el parámetro requerido '%s'", ex.getParameterName()),
                request);
        log.error("Falta parámetro requerido en {}: {}", request.getRequestURI(), ex.getParameterName(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    /** Maneja errores de conversión de tipo en parámetros. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                            HttpServletRequest request) {
        String detail = String.format("El parámetro '%s' con valor '%s' no puede convertirse al tipo %s",
                ex.getName(), ex.getValue(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconocido");
        ProblemDetail pd = buildProblemDetail(HttpStatus.BAD_REQUEST,
                "Tipo de parámetro inválido",
                detail,
                request);
        log.error("Tipo de parámetro inválido en {}: {} -> requerido {}", request.getRequestURI(), ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconocido", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    /** Maneja cuerpos de petición mal formados (JSON inválido, etc.). */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpServletRequest request) {
        ProblemDetail pd = buildProblemDetail(HttpStatus.BAD_REQUEST,
                "Body inválido",
                "El body no es legible o tiene formato inválido.",
                request);
        log.error("Body no legible en {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    /** Maneja argumentos ilegales detectados por la lógica de negocio. */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgument(IllegalArgumentException ex,
                                                               HttpServletRequest request) {
        ProblemDetail pd = buildProblemDetail(HttpStatus.BAD_REQUEST,
                "Argumento inválido",
                ex.getMessage(),
                request);
        log.error("Argumento inválido en {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    /**
     * Captura cualquier excepción no contemplada explícitamente y retorna 500.
     * Evita filtrar detalles sensibles al cliente, pero registra el error para diagnóstico.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception at {}", request.getRequestURI(), ex);
        ProblemDetail pd = buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor",
                "Ha ocurrido un error inesperado. Intente nuevamente más tarde.",
                request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
    }

    /**
     * Construye un {@link ProblemDetail} estándar con metadatos comunes.
     *
     * @param status  código HTTP a retornar
     * @param title   título legible para humanos
     * @param detail  detalle del problema (mensaje)
     * @param request petición HTTP para obtener la instancia (path)
     * @return objeto {@link ProblemDetail} listo para serializarse
     */
    private ProblemDetail buildProblemDetail(HttpStatus status, String title, String detail, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("timestamp", Instant.now().toString());
        pd.setProperty("path", request.getRequestURI());
        return pd;
    }
}
