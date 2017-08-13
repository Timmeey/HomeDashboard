package de.timmeey.iot.homeDashboard.bvg.BVGMobileParsed;

import de.timmeey.iot.homeDashboard.bvg.Departure;
import de.timmeey.iot.homeDashboard.bvg.Station;
import java.util.Iterator;
import org.cactoos.scalar.StickyScalar;
import org.cactoos.scalar.UncheckedScalar;
import org.jsoup.nodes.Element;

/**
 * MobileParsedDepartureIterator.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public final class MobileParsedDepartureIterator implements Iterator<Departure> {

    private final MobileParsedRoutes routes;
    private final MobileDepartureWebsite website;
    private final Station station;
    private int pointer;
    private final UncheckedScalar<Iterator<Element>> entries;

    public MobileParsedDepartureIterator(final MobileParsedRoutes routes, final
    MobileDepartureWebsite website, final Station station) {
        this.routes = routes;
        this.website = website;
        this.station = station;
        this.entries = new UncheckedScalar<>(
            new StickyScalar<>(() -> website.html()
                .getElementsByClass
                    ("ivu_table").first().child(1).getElementsByTag("tr")
                .iterator()
            )
        );
    }

    @Override
    public boolean hasNext() {
        return this.entries.value().hasNext();
    }

    @Override
    public Departure next() {
       return new MobileParsedDeparture(this.station, this.routes, this.entries.value().next());
    }
}
