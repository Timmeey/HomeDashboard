package de.timmeey.iot.homeDashboard.sensors.readings;

import de.timmeey.libTimmeey.persistence.UniqueIdentifier;
import de.timmeey.libTimmeey.sensor.reading.Reading;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * ReadingConst.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class ReadingConst implements Reading{

    private final Reading src;
    private final java.sql.Timestamp datetime;
    private final double value;

    public ReadingConst(final Reading src, final Timestamp datetime, final
    double value) {
        this.src = src;
        this.datetime = datetime;
        this.value = value;
    }

    public ZonedDateTime datetime() {
        return ZonedDateTime.ofInstant(datetime.toInstant(),
            ZoneId.of("UTC"));
    }

    @Override
    public UniqueIdentifier id() {
        return this.src.id();
    }

    public double value() {
        return this.value;
    }


}
