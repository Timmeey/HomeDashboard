package de.timmeey.iot.homeDashboard.sensors.readings;

import de.timmeey.iot.jooq.sqlite.Tables;
import de.timmeey.iot.jooq.sqlite.tables.SensorReading;
import de.timmeey.libTimmeey.persistence.UniqueIdentifier;
import de.timmeey.libTimmeey.sensor.reading.Reading;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

/**
 * SqlReading.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@RequiredArgsConstructor
public class ReadingJooq implements Reading {
    private final UniqueIdentifier<String> id;
    private final DSLContext jooq;
    private static final SensorReading table = Tables.SENSOR_READING;

    @Override
    public UniqueIdentifier id() {
        return this.id;
    }

    @Override
    public double value() {
        return jooq.select(table.VALUE)
            .from(table)
            .where(table.ID.eq(this.id.id()))
            .fetchAny().component1();
    }

    @Override
    public ZonedDateTime datetime() {
        return ZonedDateTime.ofInstant(
            jooq.select(table.DATETIME)
                .from(table)
                .where(table.ID.eq(this.id.id()))
                .fetchAny().component1()
                .toInstant(),
            ZoneId.of("UTC"));
    }
}
