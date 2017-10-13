package de.timmeey.iot.homeDashboard.health.weigth;

import de.timmeey.iot.homeDashboard.health.weigth.controller.dto
    .MetricWeightRequest;
import de.timmeey.iot.homeDashboard.sensors.Sensors;
import de.timmeey.libTimmeey.persistence.UUIDUniqueIdentifier;
import de.timmeey.libTimmeey.persistence.UniqueIdentifier;
import de.timmeey.libTimmeey.sensor.Sensor;
import de.timmeey.libTimmeey.sensor.reading.Reading;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.val;

/**
 * FkWeights.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
public final class SqliWeights implements Weights {
    private static final String getCstomSensorStmnt = "SELECT %s FROM "+ SqliWeightsTable.table.name()+" WHERE id = ?";
    private final Connection conn;
    private final Sensors sensors;
    private final UniqueIdentifier<String> id;

    @Override
    public Iterable<MetricWeight> allWeights() throws Exception {
        final Iterator<Reading> weights = this.weightSensor().readings()
            .iterator();
        final Iterator<Reading> fats = this.fatSensor().readings().iterator();
        final Iterator<Reading> waters = this.waterSensor().readings()
            .iterator();
        final Iterator<Reading> bones = this.boneSensor().readings().iterator();
        final Iterator<Reading> muscles = this.muscleSensor().readings()
            .iterator();

        final Deque<MetricWeight> result = new LinkedList<>();
        while (weights.hasNext() && fats.hasNext() && waters.hasNext() &&
            bones.hasNext() && muscles.hasNext()) {
            result.add(new WeightFromReading(weights.next(), fats.next
                (), waters.next(), bones.next(), muscles.next()));
        }
        return result;

    }

    @Override
    public MetricWeight addWeight(ZonedDateTime dt, MetricWeightRequest mwr)
        throws Exception {
        return new WeightFromReading(
            this.weightSensor().addReading(mwr.weight(), dt),
            this.fatSensor().addReading(mwr.bodyFat(), dt),
            this.waterSensor().addReading(mwr.bodyWater(), dt),
            this.boneSensor().addReading(mwr.boneMass(), dt),
            this.muscleSensor().addReading(mwr.muscle(), dt)
        );
    }

    private Sensor weightSensor() {
        return this.sensor(SqliWeightsTable.weightSensor.name());
    }

    private Sensor fatSensor() {
        return this.sensor(SqliWeightsTable.fatSensor.name());
    }

    private Sensor waterSensor() {
        return this.sensor(SqliWeightsTable.waterSensor.name());
    }

    private Sensor boneSensor() {
        return this.sensor(SqliWeightsTable.boneSensor.name());
    }

    private Sensor muscleSensor() {
        return this.sensor(SqliWeightsTable.muscleSensor.name());
    }

    private Sensor sensor(String sensorIdColumnName) {
        try (PreparedStatement stmnt = conn.prepareStatement(String.format(getCstomSensorStmnt,sensorIdColumnName))) {
            stmnt.setString(1, this.id.id());
            val sensorId = new UUIDUniqueIdentifier(stmnt
                .executeQuery().getString(1));
            return this.sensors.sensor(sensorId).orElseThrow(() -> new
                NullPointerException(String.format("Sensor with id: %s, does " +
                "not exist", sensorId.id())));
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public UniqueIdentifier<String> getId() {
        return this.id;
    }
}
