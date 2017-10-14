package de.timmeey.iot.homeDashboard.health.weigth;

import de.timmeey.iot.homeDashboard.sensors.SqnsorsJooq;
import de.timmeey.iot.jooq.sqlite.Tables;
import de.timmeey.libTimmeey.persistence.UUIDUniqueIdentifier;
import de.timmeey.libTimmeey.persistence.UniqueIdentifier;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jooq.DSLContext;
import org.jooq.Record1;

/**
 * SqliWeightsAggregator.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 01.
 */
@RequiredArgsConstructor
public class WeightsAggregatorJooq implements WeightsAggregator {

    private final SqnsorsJooq sensors;
    private final DSLContext jooq;
    private final de.timmeey.iot.jooq.sqlite.tables.Weights table = Tables
        .WEIGHTS;

    @Override
    public Iterable<WeightsJooq> getAll() throws SQLException {
        final List<WeightsJooq> result = new LinkedList<>();
        for (Record1<String> record : jooq.select(table.ID)
            .from(table)
            .fetch()) {
            result.add(new WeightsJooq(new UUIDUniqueIdentifier(record
                .component1()), jooq, sensors));
        }
        return result;
    }

    @Override
    public Optional<WeightsJooq> get(final UniqueIdentifier<String> id) throws
        SQLException {
        return StreamSupport.stream(getAll().spliterator(), false).filter(w
            -> w.getId().equals(id)).findAny();
    }

    @Override
    public Weights add() throws IOException, SQLException {
        val id = new UUIDUniqueIdentifier();
        jooq.insertInto(table).
            set(table.ID, id.id()).
            set(table.WEIGHTSENSOR_ID, this.sensors.add("kg").id().id()).
            set(table.WATERSENSOR_ID, this.sensors.add("%").id().id()).
            set(table.BONESENSOR_ID, this.sensors.add("%").id().id()).
            set(table.FATSENSOR_ID, this.sensors.add("%").id().id()).
            set(table.MUSCLESENSOR_ID, this.sensors.add("%").id().id())
            .execute();
        return new WeightsJooq(id, jooq, this.sensors);
    }
}

