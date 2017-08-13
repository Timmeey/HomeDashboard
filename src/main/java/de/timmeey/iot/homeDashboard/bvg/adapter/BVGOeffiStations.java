package de.timmeey.iot.homeDashboard.bvg.adapter;

import de.timmeey.iot.homeDashboard.bvg.Station;
import de.timmeey.iot.homeDashboard.bvg.Stations;
import de.timmeey.oeffiwatch.Grabber;

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
    public Station get(final String name) {
        try {
            return new BVGOeffiStation(this.bvgGrabber.getStation(name));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
