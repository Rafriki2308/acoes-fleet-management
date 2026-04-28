package com.acoes.fleetmanagement.shared.exception;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ResponseException(
        LocalDateTime timestamp,
        int status,
        String exception,
        String message
) {}
