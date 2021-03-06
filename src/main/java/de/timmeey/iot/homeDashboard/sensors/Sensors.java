package de.timmeey.iot.homeDashboard.sensors;

import de.timmeey.libTimmeey.persistence.UniqueIdentifier;
import de.timmeey.libTimmeey.sensor.Sensor;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Sensors.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public interface Sensors {

    Iterable<? extends Sensors> sensors();
    Sensor add(String unit) throws SQLException;
    Optional<? extends Sensor> sensor(UniqueIdentifier<String> id);


}
