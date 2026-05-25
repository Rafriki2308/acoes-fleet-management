package com.acoes.fleetmanagement.shared.exception;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * DTO de salida usado para devolver errores de API de forma uniforme.
 */
@Builder
public record ResponseException(
        LocalDateTime timestamp,
        int status,
        String exception,
        String message
) {}
