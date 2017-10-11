package de.timmeey.iot.homeDashboard.sensors;

import de.timmeey.libTimmeey.persistence.UUIDUniqueIdentifier;
import de.timmeey.libTimmeey.persistence.UniqueIdentifier;
import de.timmeey.libTimmeey.sensor.Sensor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.val;

/**
 * SqlSensors.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@RequiredArgsConstructor
public class SqlSensors implements Sensors {
    private final Connection conn;

    @Override
    public Iterable<Sensors> sensors() {
        throw new UnsupportedOperationException("#sensors()");
    }

    @Override
    public Sensor add(final String unit) throws SQLException {
        try (final PreparedStatement stmnt = this.conn.prepareStatement
            (String.format("INSERT INTO %s (id, unit) VALUES(?,?)", SqlSensor
                .TABLE_NAME))) {
            val id = new UUIDUniqueIdentifier();
            stmnt.setString(1,id.id());
            stmnt.setString(2,unit);
            stmnt.execute();
            return new SqlSensor(this.conn,id);
        }
    }

    @Override
    public Optional<Sensor> sensor(final UniqueIdentifier<String> id) {
        throw new UnsupportedOperationException("#sensor()");
    }
}
