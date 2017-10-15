package de.timmeey.iot.homeDashboard.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;
import org.jooq.ConnectionProvider;
import org.jooq.exception.DataAccessException;

/**
 * ThreadLocalRequestProvider.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 */
public class ThreadLocalRequestProvider implements ConnectionProvider {
    private final ThreadLocal<Connection> requestConnection;
    private final DataSource dataSource;

    public ThreadLocalRequestProvider(final ThreadLocal<Connection>
        requestConnection, final DataSource dataSource) {
        this.requestConnection = requestConnection;
        this.dataSource = dataSource;
    }

    @Override
    public Connection acquire() throws DataAccessException {
        return Optional.ofNullable(requestConnection.get()).orElseGet(() -> {
            try {
                return dataSource.getConnection();
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        });

    }

    @Override
    public void release(final Connection connection) throws
        DataAccessException {
    }
}
