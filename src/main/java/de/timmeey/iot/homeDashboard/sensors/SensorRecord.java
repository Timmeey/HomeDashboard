package de.timmeey.iot.homeDashboard.sensors;

import de.timmeey.iot.homeDashboard.sensors.readings.ReadingJooq;
import de.timmeey.iot.homeDashboard.sensors.readings.ReadingRecord;
import static de.timmeey.iot.jooq.sqlite.Keys.FK_SENSOR_READING_SENSOR_1;
import de.timmeey.libTimmeey.persistence.UUIDUniqueIdentifier;
import de.timmeey.libTimmeey.sensor.Sensor;
import de.timmeey.libTimmeey.sensor.reading.Reading;
import java.time.ZonedDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.cactoos.iterable.Mapped;
import org.jooq.DSLContext;

/**
 * SensorRecord.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@RequiredArgsConstructor
public class SensorRecord implements Sensor {

    private final Sensor sot;
    private final de.timmeey.iot.jooq.sqlite.tables.records.SensorRecord src;
    private final DSLContext jooq;

    @Override
    public Iterable<Reading> readings() throws Exception {
        return new Mapped<>(
            src.fetchChildren(FK_SENSOR_READING_SENSOR_1), r ->
            new ReadingRecord(
                new ReadingJooq(
                    new UUIDUniqueIdentifier(r.getId()),
                    jooq),
                r)
        );
    }

    @Override
    public Optional<Reading> last() throws Exception {
        return sot.last();
    }

    @Override
    public Reading addReading(final double value, final ZonedDateTime
        datetime) throws Exception {
        return sot.addReading(value,datetime);
    }

    @Override
    public void delete(final Reading reading) throws Exception {
        sot.delete(reading);
    }

    @Override
    public String unit() {
        return src.getUnit();
    }
}
