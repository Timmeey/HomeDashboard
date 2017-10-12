package de.timmeey.iot.homeDashboard.health.weigth;

import de.timmeey.iot.homeDashboard.sensors.SqliSensors;
import de.timmeey.libTimmeey.persistence.UUIDUniqueIdentifier;
import de.timmeey.libTimmeey.persistence.UniqueIdentifier;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.val;

/**
 * SqliWeightsAggregator.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 01.
 */
@RequiredArgsConstructor
public class SqliWeightsAggregator implements WeightsAggregator {
    public static final String WEIGTHS_TABLE_NAME = "weights";
    private final SqliSensors sensors;
    private final Connection conn;

    @Override
    public Iterable<SqliWeights> getAll() throws SQLException {
        final Collection<SqliWeights> result = new LinkedList<>();
        final Statement stmnt = this.conn.createStatement();
        final ResultSet rs = stmnt.executeQuery(
            String.format("Select id FROM %s",
                WEIGTHS_TABLE_NAME)
        );
        while (rs.next()) {
            result.add(new SqliWeights(conn, sensors, new
                UUIDUniqueIdentifier(rs
                .getString
                    (1)))
            );
        }
        return result;
    }

    @Override
    public Optional<SqliWeights> get(final UniqueIdentifier<String> id) throws
        SQLException {
        return StreamSupport.stream(getAll().spliterator(), false).filter(w
            -> w.getId().equals(id)).findAny();
    }

    @Override
    public Weights add() throws IOException, SQLException {
        try (final PreparedStatement stmnt = this.conn.prepareStatement
            (String.format("INSERT INTO %s (id," +
                    "weightSensor_id," +
                    "fatSensor_id," +
                    "waterSensor_id," +
                    "boneSensor_id," +
                    "muscleSensor_id" +
                    ") VALUES(?,?,?,?,?,?)",
                SqliWeightsAggregator.WEIGTHS_TABLE_NAME))) {
            val id = new UUIDUniqueIdentifier();

            stmnt.setString(1, id.id());
            stmnt.setString(2, this.sensors.add("kg").id().id());
            stmnt.setString(3, this.sensors.add("%").id().id());
            stmnt.setString(4, this.sensors.add("%").id().id());
            stmnt.setString(5, this.sensors.add("%").id().id());
            stmnt.setString(6, this.sensors.add("%").id().id());
            stmnt.execute();

            return new SqliWeights(this.conn, this.sensors, id);
        }
    }
}
