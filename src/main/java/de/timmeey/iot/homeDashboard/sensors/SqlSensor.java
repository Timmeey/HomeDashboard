package de.timmeey.iot.homeDashboard.sensors;

import de.timmeey.libTimmeey.persistence.UUIDUniqueIdentifier;
import de.timmeey.libTimmeey.persistence.UniqueIdentifier;
import de.timmeey.libTimmeey.sensor.Sensor;
import de.timmeey.libTimmeey.sensor.reading.Reading;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import lombok.val;

/**
 * SqlWeightSensor.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@SuppressWarnings("AbstractClassWithOnlyOneDirectInheritor")
public class SqlSensor implements Sensor {
    public static final String TABLE_NAME = "sensors";
    private final Connection conn;
    private final UniqueIdentifier<String> sensorId;

    SqlSensor(final Connection conn, final
    UniqueIdentifier<String> sensorId) {
        this.conn = conn;
        this.sensorId = sensorId;
    }

    @SuppressWarnings("StringConcatenation")
    @Override
    public final Iterable<Reading> readings() throws Exception {
        final Collection<Reading> result = new LinkedList<>();
        final Statement stmnt = this.conn.createStatement();
        final ResultSet rs = stmnt.executeQuery(
            String.format("Select id FROM %s WHERE sensor_id=\"%s\"",
                SqlReading.READING_TABLE_NAME, sensorId.id())
        );
        while (rs.next()) {
            result.add(new SqlReading(new UUIDUniqueIdentifier(rs.getString
                (1)), conn));
        }
        return result;
    }

    @Override
    public final Reading addReading(final double value, final ZonedDateTime datetime)
        throws Exception {
        final PreparedStatement prepStmnt = this.conn
            .prepareStatement(String.format("INSERT INTO %s (id, value, " +
                "datetime, sensor_id) VALUES(?,?,?,?)",SqlReading.READING_TABLE_NAME));
        val id = new UUIDUniqueIdentifier();
        prepStmnt.setString(1, id.id());
        prepStmnt.setDouble(2, value);
        prepStmnt.setTimestamp(3, Timestamp.from(datetime.toInstant()));
        prepStmnt.setString(4,this.sensorId.id());
        prepStmnt.execute();
        return new SqlReading(id,conn);
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
