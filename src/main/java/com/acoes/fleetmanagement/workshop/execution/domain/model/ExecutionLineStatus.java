package com.acoes.fleetmanagement.workshop.execution.domain.model;

/**
 * Estados admitidos para una linea de ejecucion de taller.
 */
public enum ExecutionLineStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}
