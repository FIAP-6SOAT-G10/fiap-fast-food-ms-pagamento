package br.com.fiap.techchallenge.infra.presenters;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateMapper {
    public static String fromLocalDateTimeToStringWithFormat(LocalDateTime localDateTime, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        if (localDateTime == null) {
            return "";
        }

        return dateTimeFormatter.format(localDateTime);
    }
}
