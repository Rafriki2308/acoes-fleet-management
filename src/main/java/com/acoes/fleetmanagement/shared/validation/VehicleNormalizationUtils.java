package com.acoes.fleetmanagement.shared.validation;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class VehicleNormalizationUtils {

    public static String normalizePlateNumber(String plateNumber) {

        if (plateNumber == null) {
            return null;
        }

        return plateNumber
                .trim()                 // quita espacios inicio/fin
                .toUpperCase()         // mayúsculas
                .replaceAll("\\s+", "") // elimina TODOS los espacios
                .replace("-", "");     // elimina guiones (opcional)
    }

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
