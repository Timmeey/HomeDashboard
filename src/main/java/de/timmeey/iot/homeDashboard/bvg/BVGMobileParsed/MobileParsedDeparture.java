package de.timmeey.iot.homeDashboard.bvg.BVGMobileParsed;

import de.timmeey.iot.homeDashboard.bvg.Departure;
import de.timmeey.iot.homeDashboard.bvg.Station;
import de.timmeey.iot.homeDashboard.bvg.Vehicle;
import de.timmeey.libTimmeey.printable.Printed;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.jsoup.nodes.Element;

/**
 * MobileParsedDeparture.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 */
public final class MobileParsedDeparture implements Departure {

    private final Station station;
    private final MobileParsedRoutes routes;
    private final Element element;

    public MobileParsedDeparture(final Station station, final MobileParsedRoutes routes,
        final Element element) {
        this.station = station;
        this.routes = routes;
        this.element = element;
    }

    @Override
    public Printed print(final Printed printed) {
        throw new UnsupportedOperationException("#print()");
    }

    @Override
    public ZonedDateTime plannedDepartureTime() {
        LocalTime internalDepartureTime = LocalTime.parse(this.element.child(0).text().replace("*","").trim());
        LocalTime nowTime = LocalTime.now();
        LocalDateTime departureDateTime;
        // Determines if the departure time means (probably) the next day.
        // Departure time 00:19, now 23:42. In that case an extra day is added
        // during the conversion
        // to a DateTime for the departure time, because it is probably the next
        // day.
        if (Math.abs(internalDepartureTime.until(nowTime, ChronoUnit.HOURS)) > 12) {
            departureDateTime = internalDepartureTime.atDate(LocalDateTime.now().toLocalDate());
            departureDateTime = departureDateTime.plus(1, ChronoUnit.DAYS);

        } else {
            departureDateTime = internalDepartureTime.atDate(LocalDateTime.now().toLocalDate());
        }

        return ZonedDateTime.of(departureDateTime, ZoneId.of("Europe/Paris"));
    }

    @Override
    public String destination() {
        return this.element.child(2).text();
    }

    @Override
    public String name() {
        String workingName;
        workingName = this.element.child(1).text().replace("Bus", "");
        workingName = workingName.replace("Tra", "");
        workingName = workingName.trim();
        if (workingName.contains(" ")) {
            workingName = workingName.split(" ")[0];
        }
        String regex = "[0-9]+";
        if (workingName.matches(regex)) {
            // Name only contains digits, appending Vehicle vehicle to name
            workingName = this.vehicle().name().concat(" ").concat(workingName);
        }
        return workingName;
    }

    @Override
    public Vehicle vehicle() {
        // try simple
        Vehicle tmpType = Vehicle.byShortcode(this.element.child(1).text().split(" ")[0]);
        if (!tmpType.equals(Vehicle.Unkown)) {
            return tmpType;
        }
        tmpType = Vehicle.byShortcode(this.element.child(1).text().substring(0, 0));
        if (!tmpType.equals(Vehicle.Unkown)) {
            return tmpType;
        }

        tmpType = Vehicle.byShortcode(this.element.child(1).text().substring(0, 1));
        if (!tmpType.equals(Vehicle.Unkown)) {
            return tmpType;
        }
        // Fallthrough
        return Vehicle.Unkown;
    }
}
