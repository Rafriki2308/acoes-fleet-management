package com.acoes.fleetmanagement.shared.validation;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class GarageNormalizatedUtil {

    public static String normalizeHondurasPhone(String phone) {

        if (phone == null) {
            return null;
        }

        // Quitar todo lo que no sea número
        String digitsOnly = phone.replaceAll("[^0-9]", "");

        // Si viene sin prefijo
        if (digitsOnly.length() == 8) {
            return "+504" + digitsOnly;
        }

        // Si ya viene con prefijo (504XXXXXXXX)
        if (digitsOnly.length() == 11 && digitsOnly.startsWith("504")) {
            return "+" + digitsOnly;
        }

        // Si no cumple, devolvemos tal cual (la validación lo rechazará)
        return phone.trim();
    }
}
