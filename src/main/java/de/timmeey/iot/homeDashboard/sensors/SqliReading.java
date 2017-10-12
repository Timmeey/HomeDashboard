package de.timmeey.iot.homeDashboard.sensors;

import de.timmeey.libTimmeey.persistence.UniqueIdentifier;
import de.timmeey.libTimmeey.sensor.reading.Reading;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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
    public static final String READING_TABLE_NAME = "sensor_readings";
    private final UniqueIdentifier<String> id;
    private final Connection conn;

    @Override
    public UniqueIdentifier id() {
        return this.id;
    }

    @Override
    public double value() {
        try (Statement stmnt = conn.createStatement()) {
            return stmnt.executeQuery(String.format(
                "SELECT value FROM %s WHERE id = \"%s\"",
                READING_TABLE_NAME,
                id().id())
            ).getDouble(1);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public ZonedDateTime datetime() {
        try (Statement stmnt = conn.createStatement()) {
            return ZonedDateTime.ofInstant(stmnt.executeQuery(String.format(
                "SELECT datetime FROM %s WHERE id = \"%s\"",
                READING_TABLE_NAME,
                id().id()))
                .getTimestamp(1)
                .toInstant(), ZoneId.of("UTC")
            );
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
