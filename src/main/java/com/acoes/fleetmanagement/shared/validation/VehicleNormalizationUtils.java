package com.acoes.fleetmanagement.shared.validation;

import lombok.NoArgsConstructor;

/**
 * Utilidad para normalizar identificadores propios de vehiculos.
 */
@NoArgsConstructor
public final class VehicleNormalizationUtils {

    /**
     * Normaliza una matricula eliminando espacios y guiones, y convirtiendo a mayusculas.
     *
     * @param plateNumber matricula recibida.
     * @return matricula normalizada o {@code null} si la entrada es nula.
     */
    public static String normalizePlateNumber(String plateNumber) {

        if (plateNumber == null) {
            return null;
        }

        return plateNumber
                .trim()
                .toUpperCase()
                .replaceAll("\\s+", "")
                .replace("-", "");
    }

    /**
     * Normaliza un VIN eliminando espacios y convirtiendo a mayusculas.
     *
     * @param vin VIN recibido.
     * @return VIN normalizado o {@code null} si la entrada es nula.
     */
    public static String normalizeVin(String vin) {
        if (vin == null) {
            return null;
        }

        return vin
                .trim()
                .toUpperCase()
                .replaceAll("\\s+", "");
    }
}
