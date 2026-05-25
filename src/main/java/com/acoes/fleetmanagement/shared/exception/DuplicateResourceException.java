package com.acoes.fleetmanagement.shared.exception;
/**
 * Excepcion de dominio lanzada cuando se intenta crear un recurso duplicado.
 */

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
