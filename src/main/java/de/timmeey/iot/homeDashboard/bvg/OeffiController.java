package de.timmeey.iot.homeDashboard.bvg;

import com.google.common.collect.Lists;
import de.timmeey.oeffiwatch.exception.AmbigiuousStationNameException;
import de.timmeey.oeffiwatch.exception.ParseException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.cactoos.iterable.Filtered;
import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.Path;
import ro.pippo.controller.Produces;
import ro.pippo.controller.extractor.Param;
import ro.pippo.core.ErrorHandler;

/**
 * OeffiController.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@Path("/oeffi")
public final class OeffiController extends Controller {
    private final Stations stations;

    public OeffiController(final Stations stations, ErrorHandler errorHandler) {
        super();
        this.stations = stations;

        errorHandler.setExceptionHandler(AmbigiuousStationNameException
            .class, (exception, routeContext) -> {
            AmbigiuousStationNameException castedException = (
                (AmbigiuousStationNameException) exception);
            getRouteContext().setLocal("message", Arrays.toString
                (castedException.getAlternativeNames()));
            errorHandler.handle(404, routeContext);
        });
    }

    @GET("/stations/{station}")
    @Produces(Produces.JSON)
    public Station getStation(@Param("station") final String station) throws
        ParseException, IOException, AmbigiuousStationNameException {
        return this.stations.get(station);
    }

    @GET("/stations/{station}/departures")
    @Produces(Produces.JSON)
    public Iterable<Departure> getLines(@Param("station") String station)
        throws AmbigiuousStationNameException, IOException, ParseException {
        final List<String> filter = Lists.newArrayList
            (this.getRouteContext().getRequest()
                .getQueryParameter
                    ("vehicle").getValues());
        final Set<Vehicle> vehicles = Arrays.stream(Vehicle.values()).filter
            (v -> filter.contains(v.fullname()) || filter.contains(v
                .shortcode()))
            .collect(Collectors.toSet());
        Station stop = this.getStation(station);
        Iterable<Departure> departures = stop.departures();
        if (!vehicles.isEmpty()) {
            return new Filtered<>(departures, departure ->
                vehicles.contains(departure.vehicle()));
        }
        return departures;

    }

    @GET("/stations/{station}/smartDepartures")
    @Produces(Produces.JSON)
    public Map<String, List<Departure>> getSmartDepartures(@Param("station")
        String
        station) throws ParseException, IOException,
        AmbigiuousStationNameException {
        List<Departure> deps = StreamSupport.stream(getLines(station)
            .spliterator(), false).collect(Collectors.toList());

        return deps.stream().collect(Collectors.groupingBy(Departure
            ::name));

    }
}
