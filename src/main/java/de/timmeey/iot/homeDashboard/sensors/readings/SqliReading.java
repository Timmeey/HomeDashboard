package de.timmeey.iot.homeDashboard.sensors.readings;

import de.timmeey.libTimmeey.persistence.UniqueIdentifier;
import de.timmeey.libTimmeey.sensor.reading.Reading;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;

/**
 * SqlReading.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@RequiredArgsConstructor
public class SqliReading implements Reading {
    private static final String getValue = String.format("SELECT %s FROM %s WHERE %s=?",
        SqliReadingTable.valueColumn.name(),
        SqliReadingTable.table.name(),
        SqliReadingTable.table.primaryKey().get().name()
        );
    private static final String getDatetime = String.format("SELECT %s FROM %s WHERE %s=?",
        SqliReadingTable.datetimeColumn.name(),
        SqliReadingTable.table.name(),
        SqliReadingTable.table.primaryKey().get().name()
    );
    private final UniqueIdentifier<String> id;
    private final Connection conn;

    @Override
    public UniqueIdentifier id() {
        return this.id;
    }

    @Override
    public double value() {
        try (PreparedStatement stmnt = conn.prepareStatement(getValue)) {
            stmnt.setString(1,this.id.id());
            return stmnt.executeQuery().getDouble(1);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public ZonedDateTime datetime() {
        try (PreparedStatement stmnt = conn.prepareStatement(getDatetime)) {
            stmnt.setString(1,this.id.id());
            return ZonedDateTime.ofInstant(stmnt.executeQuery()
                .getTimestamp(1)
                .toInstant(), ZoneId.of("UTC")
            );
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
