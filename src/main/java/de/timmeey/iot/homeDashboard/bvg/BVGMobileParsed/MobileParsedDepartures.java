package de.timmeey.iot.homeDashboard.bvg.BVGMobileParsed;

import de.timmeey.iot.homeDashboard.bvg.Departure;
import de.timmeey.iot.homeDashboard.bvg.Station;
import de.timmeey.libTimmeey.printable.Printable;
import de.timmeey.libTimmeey.printable.Printed;
import java.util.Iterator;

/**
 * MobileParsedDepartures.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public final class MobileParsedDepartures implements Iterable<Departure>,Printable {

    private final MobileDepartureWebsite website;
    private final MobileParsedRoutes routes;
    private final Station station;

    public MobileParsedDepartures(final MobileDepartureWebsite website, final
    MobileParsedRoutes routes, final Station station) {
        this.website = website;
        this.routes = routes;
        this.station = station;
    }

    @Override
    public Iterator<Departure> iterator() {
        return new MobileParsedDepartureIterator(this.routes, this.website, this
            .station);
    }

    @Override
    public Printed print(final Printed printed) {
        return printed.onlyList(iterator());
    }
}
