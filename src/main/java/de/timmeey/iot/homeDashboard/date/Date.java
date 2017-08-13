package de.timmeey.iot.homeDashboard.date;

import de.timmeey.libTimmeey.printable.Printed;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * date.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class Date implements IDate {
    @Override
    public Printed print(final Printed printed) {
        return printed.with("datetime", ZonedDateTime.now(ZoneId.of
            ("Europe/Berlin")).format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
    }
}
