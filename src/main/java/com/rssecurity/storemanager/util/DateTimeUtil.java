package com.rssecurity.storemanager.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import com.rssecurity.storemanager.exception.BadRequestException;

public class DateTimeUtil {
    
    public static LocalDateTime parseStartOfDay(String input) {
        System.out.println(input);
        try {
            return LocalDateTime.parse(input); // tenta como datetime
        } catch (DateTimeParseException e1) {
            try {
                return LocalDate.parse(input).atStartOfDay(); // tenta como date
            } catch (DateTimeParseException e2) {
                throw new BadRequestException("Formato de data inválido: " + input);
            }
        }
    }

    public static LocalDateTime parseEndOfDay(String input) {
        System.out.println(input);
        try {
            return LocalDateTime.parse(input);
        } catch (DateTimeParseException e1) {
            try {
                return LocalDate.parse(input).atTime(LocalTime.MAX);
            } catch (DateTimeParseException e2) {
                throw new BadRequestException("Formato de data inválido: " + input);
            }
        }
    }
}
