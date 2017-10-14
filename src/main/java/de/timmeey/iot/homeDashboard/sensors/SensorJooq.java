package de.timmeey.iot.homeDashboard.sensors;

import de.timmeey.iot.homeDashboard.sensors.readings.ReadingConst;
import de.timmeey.iot.homeDashboard.sensors.readings.ReadingJooq;
import de.timmeey.iot.jooq.sqlite.Tables;
import de.timmeey.libTimmeey.persistence.UUIDUniqueIdentifier;
import de.timmeey.libTimmeey.persistence.UniqueIdentifier;
import de.timmeey.libTimmeey.sensor.Sensor;
import de.timmeey.libTimmeey.sensor.reading.Reading;
import java.math.BigDecimal;
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
import org.jooq.Record1;

/**
 * SqlWeightSensor.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@SuppressWarnings("AbstractClassWithOnlyOneDirectInheritor")
@RequiredArgsConstructor
public class SensorJooq implements Sensor {
    @Getter
    @Accessors(fluent = true)
    private final UniqueIdentifier<String> id;
    private final DSLContext jooq;
    private static final de.timmeey.iot.jooq.sqlite.tables.Sensor table =
        Tables.SENSOR;
    private static final de.timmeey.iot.jooq.sqlite.tables.SensorReading
        readingTable = Tables.SENSOR_READING;

    @SuppressWarnings("StringConcatenation")
    @Override
    public final Iterable<Reading> readings() throws Exception {
        final List<Reading> result = new LinkedList<>();

        double modUlorFactor = count() / 80.0d;
        if (modUlorFactor <=1) {
            for (Record1<String> record : jooq.select(readingTable.ID)
                .from(readingTable)
                .where(readingTable.SENSOR_ID.eq(this.id.id()))
                .fetch()) {
                result.add(new ReadingJooq(new UUIDUniqueIdentifier(record
                    .component1()), jooq));
            }
        } else {
            val chance = new BigDecimal((1.0d - (1.0d /
                modUlorFactor))).multiply(new BigDecimal(9223372036854775807.0));

            for (val record : jooq.selectFrom(readingTable)
                .where(readingTable.SENSOR_ID.eq(this.id.id())
                    .and("random() >= "+chance.toString()))//no idea why, but jooq query building failed here
                .fetch()) {
                result.add(new ReadingConst(new ReadingJooq(new UUIDUniqueIdentifier(record
                    .component1()), jooq),record.getDatetime(),record.getValue()));
            }
        }

        return result;
    }

    @Override
    public final Reading addReading(final double value, final ZonedDateTime
        datetime) throws Exception {
        UniqueIdentifier<String> readingId = new UUIDUniqueIdentifier();
        jooq.insertInto(readingTable)
            .set(readingTable.ID, readingId.id())
            .set(readingTable.VALUE, value)
            .set(readingTable.DATETIME, Timestamp.from(datetime.toInstant
                ()))
            .set(readingTable.SENSOR_ID, this.id.id())
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
        throw new UnsupportedOperationException("#unit()");
    }

    private long count() {
        return jooq.selectCount().from(readingTable).where(readingTable
            .SENSOR_ID.eq(this.id.id())).fetchAny().component1();
    }

}
