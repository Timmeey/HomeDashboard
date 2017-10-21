package de.timmeey.iot.homeDashboard.util;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * SqlZonedDateTime.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class SqlZonedDateTime implements Converted<ZonedDateTime, Timestamp> {
    private final Timestamp src;

    public SqlZonedDateTime(final Timestamp datetime) {
        this.src = datetime;
    }

    public SqlZonedDateTime(final ZonedDateTime datetime) {
        this.src = Timestamp.from(datetime.toInstant());
    }

    @Override
    public ZonedDateTime from() {
        return ZonedDateTime.ofInstant(src.toInstant(),
            ZoneId.of("UTC"));
    }

    @Override
    public Timestamp to() {
        return src;
    }

}
