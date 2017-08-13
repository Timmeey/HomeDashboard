package de.timmeey.iot.homeDashboard.bvg;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.cactoos.iterable.Filtered;
import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.Path;
import ro.pippo.controller.Produces;
import ro.pippo.controller.extractor.Param;

/**
 * OeffiController.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@Path("/oeffi")
public final class OeffiController extends Controller {
    private final Stations stations;

    public OeffiController(final Stations stations) {
        super();
        this.stations = stations;
    }

    @GET("/stations/{station}")
    @Produces(Produces.JSON)
    public Station getStation(@Param("station") final String station) {
        return this.stations.get(station);
    }

    @GET("/stations/{station}/departures")
    @Produces(Produces.JSON)
    public Iterable<Departure> getLines(@Param("station") String station) {
        final List<String> filter = Lists.newArrayList
            (this.getRouteContext().getRequest()
                .getQueryParameter
                    ("vehicle").getValues());
        final Set<Vehicle> vehicles = Arrays.stream(Vehicle.values()).filter
            (v -> filter.contains(v.fullname()) || filter.contains(v
                .shortcode()))
            .collect(Collectors.toSet());
        Station stop = this.getStation(station);

        if (!vehicles.isEmpty()) {
            return new Filtered<>(stop.departures(), departure ->
                vehicles.contains(departure.vehicle()));
        }
        return stop.departures();

    }
}
