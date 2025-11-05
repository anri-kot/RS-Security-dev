package com.rssecurity.storemanager.excel.reader;

import java.util.Set;

import com.rssecurity.storemanager.infra.exception.BadRequestException;
import com.rssecurity.storemanager.util.FormatterUtil;

import jakarta.validation.ConstraintViolation;

public class ReaderValidator {
    public static <T> void validate(Set<ConstraintViolation<T>> violations) {
        if (!violations.isEmpty()) {
            throw new BadRequestException(FormatterUtil.formatViolations(violations));
        }
    }
}