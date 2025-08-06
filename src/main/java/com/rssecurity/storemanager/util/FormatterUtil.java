package com.rssecurity.storemanager.util;

import org.springframework.stereotype.Component;

@Component
public class FormatterUtil {
    public static String applyMask(String value, String maskPattern) {
        String digits;
        StringBuilder formatted = new StringBuilder();
        int digitIndex = 0;

        if (value == null || maskPattern == null) {
            return "";
        }

        digits = value.trim().replaceAll("\\D", "");

        for (int i = 0; i < maskPattern.length(); i++) {
            if (digitIndex >= digits.length()) break;

            if (maskPattern.charAt(i) == 'N') {
                formatted.append(digits.charAt(digitIndex++));
            } else {
                formatted.append(maskPattern.charAt(i));
            }
        }

        return formatted.toString();
    }

    public static String formatCpf(String value) {
        return applyMask(value, "NNN.NNN.NNN-NN");
    }

    public static String formatCnpj(String value) {
        return applyMask(value, "NN.NNN.NNN/NNNN-NN");
    }

    public static String formatPhone(String value) {
        String digits = value.trim().replaceAll("\\D", "");

        if (digits.length() > 10) {
            return applyMask(value, "(NN) NNNNN-NNNN");
        } else {
            return applyMask(value, "(NN) NNNN-NNNN");
        }
    }
}
