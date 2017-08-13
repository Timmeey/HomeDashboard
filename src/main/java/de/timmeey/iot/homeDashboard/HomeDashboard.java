package de.timmeey.iot.homeDashboard;

import de.timmeey.iot.homeDashboard.bvg.OeffiController;
import de.timmeey.iot.homeDashboard.bvg.adapter.BVGOeffiStations;
import de.timmeey.iot.homeDashboard.date.DateController;
import de.timmeey.iot.homeDashboard.lights.ColorSource;
import de.timmeey.iot.homeDashboard.lights.Light;
import de.timmeey.iot.homeDashboard.lights.LightsController;
import de.timmeey.iot.homeDashboard.lights.UDPLight;
import de.timmeey.libTimmeey.observ.Observer;
import de.timmeey.oeffiwatch.Grabber;
import java.awt.Color;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import org.kohsuke.MetaInfServices;
import ro.pippo.controller.ControllerApplication;
import ro.pippo.core.Pippo;
import ro.pippo.core.route.PublicResourceHandler;

/**
 * HomeDashboard.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@MetaInfServices
public class HomeDashboard extends ControllerApplication {

    public static void main(String[] args) {

        System.setProperty("pippo.mode", "dev");
        Pippo pippo = new Pippo(new HomeDashboard());
        pippo.start(8081);
    }

    @Override
    protected void onInit() {
        this.ALL("/.*", routeContext -> {
            routeContext.getResponse().header
                ("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "*")
                .header("Access-Control-Allow-Headers", "Content-Type");
            routeContext.next();
        });

        this.ALL("/.*", routeContext -> {
            if (routeContext.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                routeContext.getResponse().status(200);
                return;
            }
            routeContext.next();
        });
        this.registerContentTypeEngine(JsonPrintableEngine.class);
        this.addResourceRoute(new PublicResourceHandler());
        this.addControllers(new DateController());
        this.addControllers(new OeffiController(new BVGOeffiStations(Grabber
            .getVBBInstance())));
        Map<Long, Light> lights = new HashMap<>();
        try {
            lights.put(0L,
                new UDPLight(new ColorSource() {
                    @Override
                    public Color currentColor() {
                        return Color.WHITE;
                    }

                    @Override
                    public void register(final Observer<Color> observer) {
                        return;
                    }

                    @Override
                    public void unregister(final Observer observer) {
                        return;
                    }
                }, new DatagramSocket())
            );
            this.addControllers(new LightsController(lights));
        } catch (SocketException e) {
            throw new IllegalStateException(e);
        }

    }
}