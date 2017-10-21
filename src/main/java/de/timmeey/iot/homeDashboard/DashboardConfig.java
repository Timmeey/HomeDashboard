package de.timmeey.iot.homeDashboard;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import de.timmeey.iot.homeDashboard.health.weigths.WeightsAggregator;
import de.timmeey.iot.homeDashboard.health.weigths.WeightsJooq;
import de.timmeey.iot.homeDashboard.lights.ColorSource;
import de.timmeey.iot.homeDashboard.lights.Light;
import de.timmeey.iot.homeDashboard.lights.LightsController;
import de.timmeey.iot.homeDashboard.lights.UDPLight;
import de.timmeey.iot.homeDashboard.sensors.Sensors;
import de.timmeey.iot.homeDashboard.sensors.SensorsJooq;
import de.timmeey.libTimmeey.observ.Observer;
import java.awt.Color;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

/**
 * DashboardConfig.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class DashboardConfig extends AbstractModule{
    private final DataSource datasource;

    public DashboardConfig(final DataSource dataSource) {
        this.datasource = dataSource;
    }

    @Override
    protected void configure() {
        bind(Sensors.class).to(SensorsJooq.class);
        bind(WeightsAggregator.class).to(WeightsJooq.class);
    }


    @Provides
    private DataSource dataSourceProvider(){
        return this.datasource;
    }

    @Provides
    private LightsController initLightsController() throws SocketException {
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
}
