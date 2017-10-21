package de.timmeey.iot.homeDashboard;

import de.timmeey.iot.homeDashboard.bvg.OeffiController;
import de.timmeey.iot.homeDashboard.bvg.adapter.BVGOeffiStations;
import de.timmeey.iot.homeDashboard.date.DateController;
import de.timmeey.iot.homeDashboard.health.weigths.WeightsAggregator;
import de.timmeey.iot.homeDashboard.health.weigths.WeightsJooq;
import de.timmeey.iot.homeDashboard.health.weigths.controller.WeightController;
import de.timmeey.iot.homeDashboard.lights.ColorSource;
import de.timmeey.iot.homeDashboard.lights.Light;
import de.timmeey.iot.homeDashboard.lights.LightsController;
import de.timmeey.iot.homeDashboard.lights.UDPLight;
import de.timmeey.iot.homeDashboard.sensors.Sensors;
import de.timmeey.iot.homeDashboard.sensors.SensorsJooq;
import de.timmeey.iot.homeDashboard.sensors.controller.SensorsController;
import de.timmeey.iot.homeDashboard.util.JsonPrintableEngine;
import de.timmeey.iot.homeDashboard.util.NewDefaultErrorHandler;
import de.timmeey.libTimmeey.observ.Observer;
import de.timmeey.oeffiwatch.Grabber;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.simple.SimpleLogger;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
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

@RequiredArgsConstructor
public class HomeDashboard extends ControllerApplication {
    private static Logger log;
    public static final String CONFIG_DIR_NAME = ".iotDashboard";
    public static final String CONFIG_DIR_PATH = System.getProperty("user" +
        ".home") + File
        .separator + CONFIG_DIR_NAME + File.separator;

    public static void main(final String[] args) throws Exception {
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");
        log = LoggerFactory.getLogger("main");

        System.setProperty("pippo.mode", "dev");
        final DataSource dataSource = getDataSource();
        new SetupDatabase(dataSource).ensureDatabase();
        final DefaultConfiguration jooqConfig = new DefaultConfiguration();
        jooqConfig.setSQLDialect(SQLDialect.SQLITE);
        jooqConfig.setDataSource(dataSource);
        DSLContext jooq = new DefaultDSLContext(jooqConfig);
        new SetupDatabase(dataSource).ensureDatabase();

        final SensorsJooq sensors = new SensorsJooq(jooq);
        new Pippo(
            new HomeDashboard(
                jooq,
                sensors,
                new WeightsJooq(sensors, jooq),
                new BVGOeffiStations(
                    Grabber.getVBBInstance()
                )
            )
        ).start(8081);
    }

    private static boolean configExists() {
        final File configDir = new File(CONFIG_DIR_PATH);
        return configDir.isDirectory() && configDir.exists();
    }

    private static DataSource getDataSource() throws Exception {
        final SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        config.setSharedCache(true);
        final SQLiteDataSource src = new SQLiteDataSource(config);
        src.setLogWriter(new PrintWriter(System.out));

        if (configExists()) {
            System.out.println("Using Production environment. Db: " +
                CONFIG_DIR_PATH + "iot.db");
            src.setUrl("jdbc:sqlite:" +
                CONFIG_DIR_PATH + "iot.db");

            return src;
        } else {
            src.setUrl("jdbc:sqlite:dev.db");
            return src;
        }
    }

    private final DSLContext jooq;
    private final Sensors sensors;
    private final WeightsJooq weightsJooq;
    private final BVGOeffiStations stations;

    @Override
    protected final void onInit() {
        try {
            super.onInit();
            this.registerContentTypeEngine(JsonPrintableEngine.class);
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

            this.addResourceRoute(new PublicResourceHandler());
            this.addControllers(new DateController());
            this.addControllers(new OeffiController(stations, getErrorHandler
                ()));
            this.addControllers(
                new WeightController(
                    ensureWeights(weightsJooq)
                ));
            this.addControllers(initLightsController());
            this.addControllers(new SensorsController(sensors));

        } catch (final Exception e) {
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

    private static WeightsAggregator ensureWeights(
        final WeightsAggregator wa) throws IOException, SQLException {
        if (!wa.getAll().iterator().hasNext()) {
            wa.add();
        }
        return wa;
    }
}
