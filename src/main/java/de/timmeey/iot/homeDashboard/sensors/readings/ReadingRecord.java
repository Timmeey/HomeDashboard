package de.timmeey.iot.homeDashboard.sensors.readings;

import de.timmeey.iot.homeDashboard.util.SqlZonedDateTime;
import de.timmeey.iot.jooq.sqlite.tables.records.SensorReadingRecord;
import de.timmeey.libTimmeey.persistence.UUIDUniqueIdentifier;
import de.timmeey.libTimmeey.persistence.UniqueIdentifier;
import de.timmeey.libTimmeey.sensor.reading.Reading;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;

/**
 * ReadingRecord.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 */
@RequiredArgsConstructor
public class ReadingRecord implements Reading {

    private final Reading sot;
    private final SensorReadingRecord src;

    @Override
    public UniqueIdentifier id() {
        return new UUIDUniqueIdentifier(src.getId());
    }

    @Override
    public double value() {
        return src.getValue();
    }

    @Override
    public ZonedDateTime datetime() {
        return new SqlZonedDateTime(src.getDatetime()).from();
    }
}
