package de.timmeey.iot.homeDashboard.health.weigth;

import de.timmeey.libTimmeey.persistence.UniqueIdentifier;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * WeightsAggregators.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public interface WeightsAggregator {

    Iterable<? extends Weights> getAll() throws SQLException;

    Optional<? extends Weights> get(UniqueIdentifier<String> id) throws
        SQLException;

    Weights add() throws SQLException, IOException;
}
