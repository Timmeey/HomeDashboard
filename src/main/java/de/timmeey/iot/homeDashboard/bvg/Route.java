package de.timmeey.iot.homeDashboard.bvg;

import java.time.ZonedDateTime;

/**
 * Route.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public interface Route {
    Iterable<Route.Stop> stops();
    Iterable<Route.Stop> nextStops(Station station);

    class Stop {
        private final Station station;
        private final ZonedDateTime departure;

        public Stop(final Station station, final ZonedDateTime departure) {
            this.station = station;
            this.departure = departure;
        }

        Station station(){
            return this.station;
        }
        ZonedDateTime departureTime(){
            return this.departure;
        }
    }
}
