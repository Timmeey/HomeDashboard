package de.timmeey.iot.homeDashboard;

import java.io.IOException;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;

/**
 * SetupDatabase.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@SuppressWarnings({"JDBCExecuteWithNonConstantString", "StringConcatenation"})
@RequiredArgsConstructor
class SetupDatabase {

    private final DataSource src;

    final void ensureDatabase() throws SQLException, IOException {
        Flyway flyway = new Flyway();
        flyway.setCleanDisabled(true);
        flyway.setDataSource(this.src);
        flyway.migrate();
    }

}
