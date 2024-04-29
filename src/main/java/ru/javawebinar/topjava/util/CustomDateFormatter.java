package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomDateFormatter implements Formatter<LocalDate> {
    @Override
    public LocalDate parse(@Nullable String text, @Nullable Locale locale) {
        return StringUtils.hasLength(text) ? LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE) : null;
    }

    @Override
    public String print(@NotNull LocalDate localDate, @Nullable Locale locale) {
        return localDate.toString();
    }
}
