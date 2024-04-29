package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomTimeFormatter implements Formatter<LocalTime> {
    @Override
    public LocalTime parse(@Nullable String text, @Nullable Locale locale) {
        return StringUtils.hasLength(text) ? LocalTime.parse(text, DateTimeFormatter.ISO_LOCAL_TIME) : null;
    }

    @Override
    public String print(@NotNull LocalTime localTime, @Nullable Locale locale) {
        return localTime.toString();
    }
}
