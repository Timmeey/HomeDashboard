package de.timmeey.iot.homeDashboard.sensors;

import static de.timmeey.iot.jooq.sqlite.Tables.SENSOR;
import de.timmeey.libTimmeey.persistence.UUIDUniqueIdentifier;
import de.timmeey.libTimmeey.persistence.UniqueIdentifier;
import de.timmeey.libTimmeey.sensor.Sensor;
import java.sql.SQLException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jooq.DSLContext;

/**
 * SqlSensors.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@RequiredArgsConstructor
public final class SensorsJooq implements Sensors {
    private final DSLContext jooq;

    @Override
    public Iterable<SensorsJooq> sensors() {
        throw new UnsupportedOperationException("#sensors()");
    }

    @Override
    public SensorJooq add(final String unit) throws SQLException {
        val id = new UUIDUniqueIdentifier();
        jooq.insertInto(SENSOR)
            .set(SENSOR.ID, id.id())
            .set(SENSOR.UNIT, unit)
            .execute();
        return new SensorJooq(id, this.jooq);

    }

    @Override
    public Optional<Sensor> sensor(final UniqueIdentifier<String> id) {
            return Optional.ofNullable(jooq.selectFrom(SENSOR)
                .where(SENSOR.ID.eq(id.id()))
                .fetchOne())
                .map(r ->
                    new SensorRecord(
                        new SensorJooq(
                            new UUIDUniqueIdentifier(
                                r.getId()
                            ),jooq
                        ),r,jooq
                    )
                );

    }
}
