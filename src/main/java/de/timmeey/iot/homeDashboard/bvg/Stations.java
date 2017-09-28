package de.timmeey.iot.homeDashboard.bvg;

import de.timmeey.oeffiwatch.exception.AmbigiuousStationNameException;
import de.timmeey.oeffiwatch.exception.ParseException;
import java.io.IOException;

/**
 * Stations.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public interface Stations {
    Station get(String name) throws ParseException, IOException, AmbigiuousStationNameException;
}
