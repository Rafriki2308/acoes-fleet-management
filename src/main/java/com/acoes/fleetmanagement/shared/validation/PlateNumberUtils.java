package com.acoes.fleetmanagement.shared.validation;

public final class PlateNumberUtils {

    private PlateNumberUtils() {
    }

    public static String normalize(String plateNumber) {

        if (plateNumber == null) {
            return null;
        }

        return plateNumber
                .trim()                 // quita espacios inicio/fin
                .toUpperCase()         // mayúsculas
                .replaceAll("\\s+", "") // elimina TODOS los espacios
                .replace("-", "");     // elimina guiones (opcional)
    }
}
