package com.acoes.fleetmanagement.shared.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores de validación de DTOs (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseException handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage(),
                        (existing, replacement) -> existing
                ))
                .entrySet()
                .stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));

        return buildResponseDto(HttpStatus.BAD_REQUEST, ex, message);
    }

    /**
     * Maneja validaciones de parámetros (PathVariable, RequestParam)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseException handleConstraintViolation(ConstraintViolationException ex) {
        return buildResponseDto(HttpStatus.BAD_REQUEST, ex);
    }

    /**
     * Maneja recursos no encontrados
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseException handleResourceNotFound(ResourceNotFoundException ex) {
        return buildResponseDto(HttpStatus.NOT_FOUND, ex);
    }

    /**
     * Maneja duplicados (matrícula, VIN, etc.)
     */
    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseException handleDuplicateResource(DuplicateResourceException ex) {
        return buildResponseDto(HttpStatus.CONFLICT, ex);
    }

    /**
     * Maneja reglas de negocio
     */
    @ExceptionHandler(BusinessRuleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseException handleBusinessRule(BusinessRuleException ex) {
        return buildResponseDto(HttpStatus.BAD_REQUEST, ex);
    }

    /**
     * Maneja cualquier excepción no controlada
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseException handleGenericException(Exception ex) {
        return buildResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    // =========================
    // HELPERS
    // =========================

    private ResponseException buildResponseDto(HttpStatus status, Exception ex) {
        return ResponseException.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .exception(ex.getClass().getSimpleName())
                .message(ex.getMessage())
                .build();
    }

    private ResponseException buildResponseDto(HttpStatus status, Exception ex, String customMessage) {
        return ResponseException.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .exception(ex.getClass().getSimpleName())
                .message(customMessage)
                .build();
    }
}
