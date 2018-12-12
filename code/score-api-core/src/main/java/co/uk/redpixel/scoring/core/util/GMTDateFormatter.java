package co.uk.redpixel.scoring.core.util;

import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.util.TimeZone.getTimeZone;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class GMTDateFormatter {

    private static final DateFormat gmtDateFormat;

    static {
        gmtDateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        gmtDateFormat.setTimeZone(getTimeZone("GMT"));
    }

    public static String format(Date date) {
        return gmtDateFormat.format(date);
    }
}
