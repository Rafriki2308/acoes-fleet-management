package com.acoes.fleetmanagement.shared.validation;

import lombok.NoArgsConstructor;

/**
 * Utilidad para normalizar datos especificos de garajes.
 */
@NoArgsConstructor
public final class GarageNormalizatedUtil {

    /**
     * Normaliza un telefono hondureno al formato internacional con prefijo +504 cuando es posible.
     *
     * @param phone telefono recibido.
     * @return telefono normalizado o el valor recortado si no cumple el formato esperado.
     */
    public static String normalizeHondurasPhone(String phone) {

        if (phone == null) {
            return null;
        }

        String digitsOnly = phone.replaceAll("[^0-9]", "");

        if (digitsOnly.length() == 8) {
            return "+504" + digitsOnly;
        }

        if (digitsOnly.length() == 11 && digitsOnly.startsWith("504")) {
            return "+" + digitsOnly;
        }

        return phone.trim();
    }
}
