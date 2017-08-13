package de.timmeey.iot.homeDashboard.bvg;

import de.timmeey.libTimmeey.printable.Printable;

/**
 * Station.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public interface Station extends Printable {
    Iterable<Departure> departures();
    String name();

}
