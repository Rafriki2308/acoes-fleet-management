package com.acoes.fleetmanagement.workshop.order.domain.model;
/**
 * Estados admitidos para una orden de taller.
 */

public enum OrderStatus {
    OPEN,
    PENDING_PARTS,
    CLOSED,
    CANCELLED
}
