package de.timmeey.iot.homeDashboard.bvg.adapter;

import de.timmeey.iot.homeDashboard.bvg.Departure;
import de.timmeey.iot.homeDashboard.bvg.Vehicle;
import de.timmeey.libTimmeey.printable.Printed;
import de.timmeey.oeffiwatch.line.Line;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * BVGOeffiDeparture.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class BVGOeffiDeparture implements Departure {

    private final Line source;

    public BVGOeffiDeparture(final Line source) {
        this.source = source;
    }

    @Override
    public ZonedDateTime plannedDepartureTime() {
        return ZonedDateTime.of(this.source.estimatedDepartureTime(), ZoneId.of("Europe/Paris"));
    }

    @Override
    public String destination() {
        return this.source.lineEnd();
    }

    @Override
    public String name() {
        return this.source.lineName();
    }

    @Override
    public Vehicle vehicle() {
        return Vehicle.byShortcode(this.source.vehicleType()
            .getShortcode());
    }

    @Override
    public Printed print(final Printed printed) {
        return printed
            .with("name", this.source.lineName())
            .with("destination", this.source.lineEnd())
            .with("plannedDeparture", this.plannedDepartureTime().format(DateTimeFormatter
                    .ISO_ZONED_DATE_TIME))
            .with("vehicle", this.source.vehicleType().getName())
            .with("departure", this.source.estimatedDepartureTimefromNow());

    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BVGOeffiDeparture{");
        sb.append("name=").append(this.name());
        sb.append("destination=").append(this.destination());
        sb.append("vehicle=").append(this.vehicle());
        sb.append("plannedDeparture=").append(this.plannedDepartureTime());

        sb.append('}');
        return sb.toString();
    }
}
