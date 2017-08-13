package de.timmeey.iot.homeDashboard.bvg.BVGMobileParsed;

import de.timmeey.iot.homeDashboard.bvg.Route;
import de.timmeey.iot.homeDashboard.bvg.Station;

/**
 * Routes.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public final class MobileParsedRoute implements Route {

    private final DocumentFromHtml routeWebsite;

    public MobileParsedRoute(final DocumentFromHtml routeWebsite) {
        this.routeWebsite = routeWebsite;
    }

    @Override
    public Iterable<Route.Stop> stops() {
        throw new UnsupportedOperationException("#stops()");
    }

    @Override
    public Iterable<Route.Stop> nextStops(final Station station) {
        throw new UnsupportedOperationException("#nextStops()");
    }
}
