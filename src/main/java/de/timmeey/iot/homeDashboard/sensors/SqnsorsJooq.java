package de.timmeey.iot.homeDashboard.sensors;

import de.timmeey.iot.jooq.sqlite.Tables;
import de.timmeey.libTimmeey.persistence.UUIDUniqueIdentifier;
import de.timmeey.libTimmeey.persistence.UniqueIdentifier;
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
public class SqnsorsJooq implements Sensors {
    private final DSLContext jooq;
    private static final de.timmeey.iot.jooq.sqlite.tables.Sensor table =
        Tables.SENSOR;

    @Override
    public Iterable<SqnsorsJooq> sensors() {
        throw new UnsupportedOperationException("#sensors()");
    }

    @Override
    public SensorJooq add(final String unit) throws SQLException {
        val id = new UUIDUniqueIdentifier();
        jooq.insertInto(table)
            .set(table.ID, id.id())
            .set(table.UNIT, unit)
            .execute();
        return new SensorJooq(id, this.jooq);

    }

    @Override
    public Optional<SensorJooq> sensor(final UniqueIdentifier<String> id) {
        if(jooq.fetchExists(
            jooq.select()
                .from(table)
                .where(table.ID.eq(id.id()))
        )) {
            return Optional.of(new SensorJooq(id, this.jooq));
        }else{
            return Optional.empty();
        }
    }
}
