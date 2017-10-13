package de.timmeey.iot.homeDashboard.sensors;

import de.timmeey.iot.homeDashboard.sensors.readings.SqliReading;
import de.timmeey.iot.homeDashboard.sensors.readings.SqliReadingTable;
import de.timmeey.libTimmeey.persistence.UUIDUniqueIdentifier;
import de.timmeey.libTimmeey.persistence.UniqueIdentifier;
import de.timmeey.libTimmeey.sensor.Sensor;
import de.timmeey.libTimmeey.sensor.reading.Reading;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

/**
 * SqlWeightSensor.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@SuppressWarnings("AbstractClassWithOnlyOneDirectInheritor")
public class SqliSensor implements Sensor {
    private static final String getAllIdsForSensor = String.format("SELECT %s FROM %s WHERE %s=?",
        SqliReadingTable.table.primaryKey().get().name(),
        SqliReadingTable.table.name(),
        SqliReadingTable.sensor_idColumn.name()
        );
    private final Connection conn;

    @Getter
    @Accessors(fluent = true)
    private final UniqueIdentifier<String> id;

    public SqliSensor(final Connection conn, final
    UniqueIdentifier<String> sensorId) {
        this.conn = conn;
        this.id = sensorId;
    }



    @SuppressWarnings("StringConcatenation")
    @Override
    public final Iterable<Reading> readings() throws Exception {
        final Collection<Reading> result = new LinkedList<>();
        final PreparedStatement stmnt = this.conn.prepareStatement(getAllIdsForSensor);
        stmnt.setString(1,this.id.id());
        final ResultSet rs = stmnt.executeQuery();
        while (rs.next()) {
            result.add(new SqliReading(new UUIDUniqueIdentifier(rs.getString
                (1)), conn));
        }
        return result;
    }

    @Override
    public final Reading addReading(final double value, final ZonedDateTime datetime)
        throws Exception {
        final PreparedStatement prepStmnt = this.conn
            .prepareStatement(SqliReadingTable.table.insertQuery());
        val id = new UUIDUniqueIdentifier();
        prepStmnt.setString(1, id.id());
        prepStmnt.setDouble(2, value);
        prepStmnt.setTimestamp(3, Timestamp.from(datetime.toInstant()));
        prepStmnt.setString(4,this.id.id());
        prepStmnt.execute();
        return new SqliReading(id,conn);
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

}
