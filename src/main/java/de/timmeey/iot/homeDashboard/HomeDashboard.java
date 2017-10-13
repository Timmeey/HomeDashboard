package de.timmeey.iot.homeDashboard;

import de.timmeey.iot.homeDashboard.bvg.OeffiController;
import de.timmeey.iot.homeDashboard.bvg.adapter.BVGOeffiStations;
import de.timmeey.iot.homeDashboard.date.DateController;
import de.timmeey.iot.homeDashboard.health.weigth.SqliWeightsAggregator;
import de.timmeey.iot.homeDashboard.health.weigth.controller.WeightController;
import de.timmeey.iot.homeDashboard.lights.ColorSource;
import de.timmeey.iot.homeDashboard.lights.Light;
import de.timmeey.iot.homeDashboard.lights.LightsController;
import de.timmeey.iot.homeDashboard.lights.UDPLight;
import de.timmeey.iot.homeDashboard.sensors.SqliSensors;
import de.timmeey.libTimmeey.observ.Observer;
import de.timmeey.oeffiwatch.Grabber;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import lombok.val;
import org.kohsuke.MetaInfServices;
import org.sqlite.SQLiteConfig;
import ro.pippo.controller.Controller;
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
    public static final String CONFIG_DIR_NAME = ".iotDashboard";
    public static final String CONFIG_DIR_PATH = System.getProperty("user" +
        ".home") + File
        .separator + CONFIG_DIR_NAME + File.separator;
    private static Connection conn;

    public static void main(final String[] args) throws Exception {
        System.setProperty("pippo.mode", "dev");
        Class.forName("org.sqlite.JDBC");
        conn = getConn();
        if (!HomeDashboard.dbExists()) {
            new SetupDatabase(conn).setup();
        }

        final Pippo pippo = new Pippo(new HomeDashboard());
        pippo.start(8081);
    }

    private static boolean configExists() {
        File configDir = new File(CONFIG_DIR_PATH);
        return configDir.isDirectory() && configDir.exists();
    }

    private static boolean dbExists() {
        File dbFile = new File(CONFIG_DIR_PATH + "iot.db");
        return dbFile.isFile() && dbFile.exists();
    }

    private static Connection getConn() throws Exception {
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        if (configExists()) {
            System.out.println("Using Production environment. Db: " +
                CONFIG_DIR_PATH + "iot.db");
            return DriverManager.getConnection("jdbc:sqlite:" +
                CONFIG_DIR_PATH + "iot.db", config.toProperties());
        } else {
            File tmpFile = File.createTempFile("dashboardDevDb", ".db");
            System.out.println("Using DEV environment. DB: " + tmpFile
                .getAbsolutePath());
            return DriverManager.getConnection("jdbc:sqlite::memory:", config.toProperties());
        }
    }

    @Override
    protected final void onInit() {
        try {
            super.onInit();
            this.setErrorHandler(new NewDefaultErrorHandler(this));

            this.ANY("/.*", routeContext -> {
                routeContext.getResponse().header
                    ("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "*")
                    .header("Access-Control-Allow-Headers", "Content-Type");
                routeContext.next();
            });

            this.ANY("/.*", routeContext -> {
                if ("OPTIONS".equalsIgnoreCase(routeContext.getRequestMethod
                    ())) {

                    routeContext.getResponse().status(200);
                    return;
                }
                routeContext.next();
            });
            this.registerContentTypeEngine(JsonPrintableEngine.class);
            this.addResourceRoute(new PublicResourceHandler());
            this.addControllers(new DateController());
            this.addControllers(new OeffiController(new BVGOeffiStations(Grabber
                .getVBBInstance()), getErrorHandler()));
            this.addControllers(initWeightsController());
            this.addControllers(this.initLightsController());
        } catch (Exception e) {
            System.out.println("Could not start pippo: " + e.getMessage());
            System.exit(1);
        }
    }

    private Controller initLightsController() throws SocketException {
        final Map<Long, Light> lights = new HashMap<>(1);
        lights.put(0L,
            new UDPLight(new ColorSource() {
                @Override
                public Color currentColor() {
                    return Color.WHITE;
                }

                @Override
                public void register(final Observer<Color> observer) {
                }

                @Override
                public void unregister(final Observer observer) {
                }
            }, new DatagramSocket())
        );
        return new LightsController(lights);

    }

    private Controller initWeightsController() throws IOException, SQLException {
        val wa = new SqliWeightsAggregator(new SqliSensors(conn),conn);
        wa.add();
        return new WeightController(wa);
    }
}
