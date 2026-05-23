package com.group1.productcatalogsystem.util;

import com.group1.productcatalogsystem.exception.BadRequestException;

public final class StringValidationUtils {

    private StringValidationUtils() {
    }

    public static String requireNonBlankName(String name, String fieldLabel) {
        if (name == null) {
            throw new BadRequestException(fieldLabel + " is required");
        }
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            throw new BadRequestException(fieldLabel + " must not be blank");
        }
        return trimmed;
    }
}
