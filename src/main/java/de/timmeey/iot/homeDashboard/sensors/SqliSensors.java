package de.timmeey.iot.homeDashboard.sensors;

import de.timmeey.libTimmeey.persistence.UUIDUniqueIdentifier;
import de.timmeey.libTimmeey.persistence.UniqueIdentifier;
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
public class SqliSensors implements Sensors {
    private final Connection conn;

    @Override
    public Iterable<SqliSensors> sensors() {
        throw new UnsupportedOperationException("#sensors()");
    }

    @Override
    public SqliSensor add(final String unit) throws SQLException {
        try (final PreparedStatement stmnt = this.conn.prepareStatement
            (String.format("INSERT INTO %s (id, unit) VALUES(?,?)", SqliSensor
                .TABLE_NAME))) {
            val id = new UUIDUniqueIdentifier();
            stmnt.setString(1,id.id());
            stmnt.setString(2,unit);
            stmnt.execute();
            return new SqliSensor(this.conn,id);
        }
    }

    @Override
    public Optional<SqliSensor> sensor(final UniqueIdentifier<String> id) {
        return Optional.of(new SqliSensor(this.conn,id));
    }
}
