package de.timmeey.iot.homeDashboard.bvg.BVGMobileParsed;

import de.timmeey.iot.homeDashboard.bvg.Departure;
import de.timmeey.iot.homeDashboard.bvg.Station;
import de.timmeey.libTimmeey.printable.Printed;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

/**
 * Station.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@Slf4j
public final class MobileParsedStation implements Station {

    private final MobileDepartureWebsite website;
    private final Iterable<Departure> departures;

    public MobileParsedStation(final MobileDepartureWebsite website, final
    MobileParsedRoutes routes) {
        this.website = website;
        this.departures= new MobileParsedDepartures(website,routes, this);
    }

    @Override
    public Printed print(final Printed printed) {
        return printed.with("StationName", this.name());
    }

    @Override
    public Iterable<Departure> departures() {
        return this.departures;
    }

    @Override
    public String name() {
        Element queryStationName = this.website.html().getElementById("ivu_overview_input");
        if (queryStationName != null && queryStationName.hasText()) {
            String result = queryStationName.getElementsByTag("strong").text();
            MobileParsedStation.log.trace("Found station Name: {}",result);
            return result;
        }
        MobileParsedStation.log.debug("Could not find stationName");
        throw new RuntimeException("Station name not found");
    }
}
