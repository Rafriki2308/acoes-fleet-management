package com.acoes.fleetmanagement.shared.exception;
/**
 * Excepcion de dominio lanzada cuando se incumple una regla de negocio.
 */

public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
