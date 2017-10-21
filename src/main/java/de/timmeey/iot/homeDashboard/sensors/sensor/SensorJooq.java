package de.timmeey.iot.homeDashboard.sensors.sensor;

import de.timmeey.iot.homeDashboard.sensors.readings.ReadingJooq;
import de.timmeey.iot.homeDashboard.sensors.readings.ReadingJooqConst;
import static de.timmeey.iot.jooq.sqlite.Tables.SENSOR;
import static de.timmeey.iot.jooq.sqlite.Tables.SENSOR_READING;
import de.timmeey.iot.jooq.sqlite.tables.records.SensorReadingRecord;
import de.timmeey.libTimmeey.persistence.UUIDUniqueIdentifier;
import de.timmeey.libTimmeey.printable.Printed;
import de.timmeey.libTimmeey.sensor.Sensor;
import de.timmeey.libTimmeey.sensor.reading.Reading;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.val;
import org.jooq.DSLContext;

/**
 * SqlWeightSensor.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@SuppressWarnings("AbstractClassWithOnlyOneDirectInheritor")
@RequiredArgsConstructor
public final class SensorJooq implements Sensor {
    @Getter
    @Accessors(fluent = true)
    private final UUIDUniqueIdentifier id;
    private final DSLContext jooq;

    @SuppressWarnings("StringConcatenation")
    @Override
    public final Iterable<Reading> readings() throws Exception {
        final List<Reading> result = new LinkedList<>();

        for (val record : jooq.selectFrom(SENSOR_READING)
            .where(SENSOR_READING.SENSOR_ID
                .eq(this.id.id()))
            ) {
            result.add(mapToReading(record));
        }
        return result;
    }

    @Override
    public final Reading addReading(final double value, final ZonedDateTime
        datetime) throws Exception {
        UUIDUniqueIdentifier readingId = new UUIDUniqueIdentifier();
        jooq.insertInto(SENSOR_READING)
            .set(SENSOR_READING.ID, readingId.id())
            .set(SENSOR_READING.VALUE, value)
            .set(SENSOR_READING.DATETIME, Timestamp.from(datetime.toInstant
                ()))
            .set(SENSOR_READING.SENSOR_ID, this.id.id())
            .execute();

        return new ReadingJooq(readingId, jooq);
    }

    @Override
    public Optional<Reading> last() throws Exception {
        throw new UnsupportedOperationException("#last()");
    }

    @Override
    public void delete(final Reading reading) throws Exception {
        throw new UnsupportedOperationException("#delete()");
    }

    @Override
    public String unit() {
        return jooq.select(SENSOR.UNIT).from(SENSOR).where(SENSOR.ID.eq(this.id
            .id())).fetchOne().component1();
    }

    @Override
    public UUIDUniqueIdentifier id(){
        return this.id;
    }

    private long count() {
        return jooq.selectCount().from(SENSOR_READING)
            .where(SENSOR_READING.SENSOR_ID.eq(this.id.id()))
            .fetchAny().component1();
    }

    private Reading mapToReading(SensorReadingRecord record) {
        return new ReadingJooqConst(new ReadingJooq(
            new UUIDUniqueIdentifier(
                record.component1()
            ),
            jooq),
            record
        );
    }

    @Override
    public Printed print(final Printed printed) {
        return printed.with("id",this.id().id())
            .with("unit",this.unit());
    }
}
