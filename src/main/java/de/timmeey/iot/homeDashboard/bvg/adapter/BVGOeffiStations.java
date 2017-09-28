package de.timmeey.iot.homeDashboard.bvg.adapter;

import de.timmeey.iot.homeDashboard.bvg.Station;
import de.timmeey.iot.homeDashboard.bvg.Stations;
import de.timmeey.oeffiwatch.Grabber;
import de.timmeey.oeffiwatch.exception.AmbigiuousStationNameException;
import de.timmeey.oeffiwatch.exception.ParseException;
import java.io.IOException;

/**
 * BVGStations.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class BVGOeffiStations implements Stations {
    private final Grabber bvgGrabber;

    public BVGOeffiStations(final Grabber bvgGrabber) {
        this.bvgGrabber = bvgGrabber;
    }

    @Override
    public Station get(final String name) throws ParseException, IOException, AmbigiuousStationNameException {
        return new BVGOeffiStation(this.bvgGrabber.getStation(name));

    }
}
