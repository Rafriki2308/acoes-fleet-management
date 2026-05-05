package com.acoes.fleetmanagement.shared.validation;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class NormalizatedTextUtil {

    public static String normalizeUpper(String name) {

        if (name == null) {
            return null;
        }


        return name
                .trim()
                .toUpperCase()          // 👈 ahora en mayúsculas
                .replaceAll("\\s+", " "); // espacios simples
    }
}
