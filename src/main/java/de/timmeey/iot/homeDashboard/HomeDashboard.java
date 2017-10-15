package de.timmeey.iot.homeDashboard;

import de.timmeey.iot.homeDashboard.bvg.OeffiController;
import de.timmeey.iot.homeDashboard.bvg.adapter.BVGOeffiStations;
import de.timmeey.iot.homeDashboard.date.DateController;
import de.timmeey.iot.homeDashboard.health.weigth.WeightsJooq;
import de.timmeey.iot.homeDashboard.health.weigth.controller.WeightController;
import de.timmeey.iot.homeDashboard.lights.ColorSource;
import de.timmeey.iot.homeDashboard.lights.Light;
import de.timmeey.iot.homeDashboard.lights.LightsController;
import de.timmeey.iot.homeDashboard.lights.UDPLight;
import de.timmeey.iot.homeDashboard.sensors.SensorsJooq;
import de.timmeey.iot.homeDashboard.util.JsonPrintableEngine;
import de.timmeey.iot.homeDashboard.util.NewDefaultErrorHandler;
import de.timmeey.iot.homeDashboard.util.ThreadLocalRequestProvider;
import de.timmeey.libTimmeey.observ.Observer;
import de.timmeey.oeffiwatch.Grabber;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.sql.DataSource;
import lombok.Synchronized;
import lombok.val;
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

public class HomeDashboard extends ControllerApplication {
    private static Logger log;
    private static final ThreadLocal<Connection> requestConnection = new
        ThreadLocal<>();
    private static final ConcurrentLinkedQueue<Connection> connectionPool =
        new ConcurrentLinkedQueue();
    public static final String CONFIG_DIR_NAME = ".iotDashboard";
    public static final String CONFIG_DIR_PATH = System.getProperty("user" +
        ".home") + File
        .separator + CONFIG_DIR_NAME + File.separator;
    private static DataSource dataSource;

    private static DSLContext jooq;

    public static void main(final String[] args) throws Exception {
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");
        log = LoggerFactory.getLogger("main");

        System.setProperty("pippo.mode", "dev");
        Class.forName("org.sqlite.JDBC");
        dataSource = getDataSource();
        if (!HomeDashboard.dbExists()) {
            new SetupDatabase(dataSource).ensureDatabase();
        }
        DefaultConfiguration jooqConfig = new DefaultConfiguration();
        jooqConfig.setSQLDialect(SQLDialect.SQLITE);
        jooqConfig.setConnectionProvider(new ThreadLocalRequestProvider(requestConnection,dataSource));
        jooq = new DefaultDSLContext(jooqConfig);
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

    private static DataSource getDataSource() throws Exception {
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        config.setSharedCache(true);
        SQLiteDataSource src = new SQLiteDataSource(config);
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

    @Override
    protected final void onInit() {
        try {
            super.onInit();
            this.setErrorHandler(new NewDefaultErrorHandler(this));

            this.ANY("/.*", routeContext -> {
                try {
                    Connection tmp = getConnection();
                    tmp.setAutoCommit(false);
                    requestConnection.set(tmp);
                    routeContext.next();
                } catch (SQLException e) {
                    routeContext.getResponse().internalError().commit();
                    log.error("could not get new connection");
                }
            });

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


            this.ANY("/.*", routeContext -> {
                Connection tmp = requestConnection.get();
                requestConnection.set(null);
                try {
                    if (routeContext.getResponse().getStatus() < 400) {
                        tmp.commit();
                    } else {
                        tmp.rollback();
                    }
                    tmp.setAutoCommit(true);
                    if(connectionPool.size()>3){
                        log.warn("ConnectionPoolSize is {}",connectionPool.size());

                        tmp.close();
                    }else {
                        connectionPool.add(tmp);
                    }
                } catch (SQLException e) {
                    log.error("Something went wrong while committing " +
                        "transaction",e);
                }
            }).runAsFinally();

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

    private Controller initWeightsController() throws IOException,
        SQLException {
        val wa = new WeightsJooq(new SensorsJooq(jooq), jooq);
        if (!wa.getAll().iterator().hasNext()) {
            wa.add();
        }
        return new WeightController(wa);
    }

    @Synchronized
    private Connection getConnection() throws SQLException {
        Connection conn;
        if ((conn = connectionPool.poll()) != null) {
            return conn;
        } else {
            return dataSource.getConnection();
        }
    }
}
