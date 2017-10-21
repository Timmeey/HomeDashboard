package de.timmeey.iot.homeDashboard.health.weigth;

import de.timmeey.iot.homeDashboard.sensors.SensorsJooq;
import static de.timmeey.iot.jooq.sqlite.Tables.WEIGHT;
import de.timmeey.libTimmeey.persistence.UUIDUniqueIdentifier;
import de.timmeey.libTimmeey.persistence.UniqueIdentifier;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.cactoos.iterable.Mapped;
import org.jooq.DSLContext;

/**
 * SqliWeightsAggregator.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 01.
 */
@RequiredArgsConstructor
public class WeightsJooq implements WeightsAggregator {

    private final SensorsJooq sensors;
    private final DSLContext jooq;

    @Override
    public Iterable<WeightRecord> getAll() throws SQLException {
        return new Mapped<>(
            jooq.selectFrom(WEIGHT)
                .fetch(), r -> new WeightRecord(r, jooq, sensors)
        );
    }

    @Override
    public Optional<WeightRecord> get(final UniqueIdentifier<String> id) throws
        SQLException {
        return StreamSupport.stream(getAll().spliterator(), false).filter(w
            -> w.getId().equals(id)).findAny();
    }

    @Override
    public Weights add() throws IOException, SQLException {
        return new WeightRecord(
            jooq.insertInto(WEIGHT).
                set(WEIGHT.ID, new UUIDUniqueIdentifier().id()).
                set(WEIGHT.WEIGHTSENSOR_ID, this.sensors.add("kg").id()
                    .id()).
                set(WEIGHT.WATERSENSOR_ID, this.sensors.add("%").id().id()).
                set(WEIGHT.BONESENSOR_ID, this.sensors.add("%").id().id()).
                set(WEIGHT.FATSENSOR_ID, this.sensors.add("%").id().id()).
                set(WEIGHT.MUSCLESENSOR_ID, this.sensors.add("%").id().id())
                .returning().fetchOne(),
            this.jooq,
            this.sensors
        );

    }
}

