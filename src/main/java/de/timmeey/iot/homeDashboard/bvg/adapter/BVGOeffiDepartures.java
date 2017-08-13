package de.timmeey.iot.homeDashboard.bvg.adapter;

import de.timmeey.iot.homeDashboard.bvg.Departure;
import de.timmeey.libTimmeey.printable.Printable;
import de.timmeey.libTimmeey.printable.Printed;
import de.timmeey.oeffiwatch.station.Station;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import org.cactoos.iterator.Mapped;

/**
 * BVGOeffiDepartures.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 */
public final class BVGOeffiDepartures implements Iterable<Departure>,Printable {

    private final Station source;

    public BVGOeffiDepartures(final Station
        source) {
        this.source = source;
    }

    @Override
    public Iterator<Departure> iterator() {
        try {
            if (this.source.lastUpdated().isBefore(LocalDateTime.now().minus(10,
                ChronoUnit.SECONDS))) {
                this.source.update();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return new Mapped<>(this.source.lines().iterator(),
            BVGOeffiDeparture::new);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BVGOeffiDepartures{");
        sb.append("departures=[");
        this.iterator().forEachRemaining(sb::append);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public Printed print(final Printed printed) {
        return printed.onlyList(iterator());

    }
}
