package de.timmeey.iot.homeDashboard.bvg.BVGMobileParsed;

import de.timmeey.iot.homeDashboard.bvg.Station;
import de.timmeey.iot.homeDashboard.bvg.Stations;

/**
 * Stations.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public final class MobileParsedStations implements Stations {
    @Override
    public Station get(final String name) {
        final MobileDepartureWebsite website = new MobileDepartureWebsite(name);
        return new MobileParsedStation(website,null);
    }
}
