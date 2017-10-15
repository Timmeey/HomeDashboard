package de.timmeey.iot.homeDashboard.sensors.readings;

import de.timmeey.iot.homeDashboard.util.SqlZonedDateTime;
import static de.timmeey.iot.jooq.sqlite.tables.SensorReading.SENSOR_READING;
import de.timmeey.libTimmeey.persistence.UniqueIdentifier;
import de.timmeey.libTimmeey.sensor.reading.Reading;
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
    private final DSLContext jooq;;

    @Override
    public UniqueIdentifier id() {
        return this.id;
    }

    @Override
    public double value() {
        return jooq.select(SENSOR_READING.VALUE)
            .from(SENSOR_READING)
            .where(SENSOR_READING.ID.eq(this.id.id()))
            .fetchAny().component1();
    }

    @Override
    public ZonedDateTime datetime() {
        return
            new SqlZonedDateTime(
                jooq.select(SENSOR_READING.DATETIME)
                .from(SENSOR_READING)
                .where(SENSOR_READING.ID
                    .eq(this.id.id()))
                .fetchAny().component1()).from();

    }
}
