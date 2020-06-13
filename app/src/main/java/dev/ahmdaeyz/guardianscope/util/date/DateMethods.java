package dev.ahmdaeyz.guardianscope.util.date;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

public class DateMethods {
    public static String[] yesterdayAndToday() {
        Instant today = Instant.now();
        Instant aDayAgo = today.minusSeconds(60 * 60 * 24);
        LocalDateTime dateToday = LocalDateTime.ofInstant(today, ZoneId.of("UTC"));
        LocalDateTime dateYesterday = LocalDateTime.ofInstant(aDayAgo, ZoneId.of("UTC"));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new String[]{dateTimeFormatter.format(dateYesterday), dateTimeFormatter.format(dateToday)};
    }
}
