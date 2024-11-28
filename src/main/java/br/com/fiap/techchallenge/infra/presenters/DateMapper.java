package br.com.fiap.techchallenge.infra.presenters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateMapper {
    public static String fromLocalDateTimeToStringWithFormat(LocalDateTime localDateTime, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        if (localDateTime == null) {
            return "";
        }

        return dateTimeFormatter.format(localDateTime);
    }
}
