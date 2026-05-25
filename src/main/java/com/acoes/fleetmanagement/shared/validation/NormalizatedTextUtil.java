package com.acoes.fleetmanagement.shared.validation;

import lombok.NoArgsConstructor;

/**
 * Utilidad para normalizar texto libre usado en el dominio.
 */
@NoArgsConstructor
public final class NormalizatedTextUtil {

    /**
     * Normaliza texto eliminando espacios extremos, compactando espacios internos y convirtiendo a mayusculas.
     *
     * @param name texto recibido.
     * @return texto normalizado o {@code null} si la entrada es nula.
     */
    public static String normalizeUpper(String name) {

        if (name == null) {
            return null;
        }

        return name
                .trim()
                .toUpperCase()
                .replaceAll("\\s+", " ");
    }
}
