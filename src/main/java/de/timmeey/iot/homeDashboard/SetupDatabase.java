package de.timmeey.iot.homeDashboard;

import de.timmeey.iot.homeDashboard.health.weigth.SqliWeightsTable;
import de.timmeey.iot.homeDashboard.sensors.readings.SqliReadingTable;
import de.timmeey.iot.homeDashboard.sensors.SqliSensorTable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * SetupDatabase.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@SuppressWarnings({"JDBCExecuteWithNonConstantString", "StringConcatenation"})
class SetupDatabase {

    private final Connection conn;

    SetupDatabase(final Connection conn) {
        this.conn = conn;
    }

    final void setup() throws SQLException, IOException {
        final boolean oldCommitState = conn.getAutoCommit();
        try {
            conn.setAutoCommit(false);
            StringBuilder sb = new StringBuilder(2);
            sb.append(SqliSensorTable.table.createTableQuery()).append(SqliReadingTable.table.createTableQuery());
            sb.append(SqliWeightsTable.table.createTableQuery());
            System.out.println(sb.toString());
            conn.createStatement().executeUpdate(sb.toString());
        } finally {
            conn.commit();
            conn.setAutoCommit(oldCommitState);
        }

    }

}
