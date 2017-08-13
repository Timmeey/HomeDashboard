package de.timmeey.iot.homeDashboard.bvg.BVGMobileParsed;

import de.timmeey.iot.homeDashboard.bvg.Route;
import java.net.URL;

/**
 * MobileParsedRoutes.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public final class MobileParsedRoutes {

    Route getRoute(URL url) {
        return new MobileParsedRoute(new MobileRouteWebsite(url));
    }
}
