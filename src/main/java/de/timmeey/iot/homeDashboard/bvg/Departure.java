package de.timmeey.iot.homeDashboard.bvg;

import de.timmeey.libTimmeey.printable.Printable;
import java.time.ZonedDateTime;

/**
 * Departure.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1

 */
public interface Departure extends Printable {

    ZonedDateTime plannedDepartureTime();
    String destination();
    String name();
    Vehicle vehicle();
}
