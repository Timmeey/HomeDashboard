package de.timmeey.iot.homeDashboard.bvg.adapter;

import de.timmeey.iot.homeDashboard.bvg.Station;
import de.timmeey.libTimmeey.printable.Printed;
import java.time.format.DateTimeFormatter;

/**
 * BVGStation.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class BVGOeffiStation implements Station {
    private final de.timmeey.oeffiwatch.station.Station source;

    public BVGOeffiStation(de.timmeey.oeffiwatch.station.Station source) {
        this.source = source;
    }

    @Override
    public BVGOeffiDepartures departures() {
        return new BVGOeffiDepartures(this.source);
    }

    @Override
    public String name() {
        throw new UnsupportedOperationException("#name()");
    }

    @Override
    public Printed print(final Printed printed) {
        printed.with("name", this.source.name());
        printed.with("updated", this.source.lastUpdated().format(DateTimeFormatter.ISO_DATE_TIME));
        printed.withList("departures", this.departures());
        return printed;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BVGOeffiStation{");
        sb.append("name=").append(this.source.name());
        sb.append("departures=").append(this.departures());

        sb.append('}');
        return sb.toString();
    }
}
