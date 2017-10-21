package de.timmeey.iot.homeDashboard.health.weigths.weight;

import de.timmeey.iot.homeDashboard.health.weigths.Weights;
import de.timmeey.iot.homeDashboard.health.weigths.controller.dto.MetricWeightRequest;
import de.timmeey.iot.homeDashboard.sensors.Sensors;
import static de.timmeey.iot.jooq.sqlite.Keys.FK_WEIGHT_SENSOR_1;
import static de.timmeey.iot.jooq.sqlite.Keys.FK_WEIGHT_SENSOR_2;
import static de.timmeey.iot.jooq.sqlite.Keys.FK_WEIGHT_SENSOR_3;
import static de.timmeey.iot.jooq.sqlite.Keys.FK_WEIGHT_SENSOR_4;
import static de.timmeey.iot.jooq.sqlite.Keys.FK_WEIGHT_SENSOR_5;
import de.timmeey.iot.jooq.sqlite.tables.records.SensorRecord;
import de.timmeey.libTimmeey.persistence.UUIDUniqueIdentifier;
import de.timmeey.libTimmeey.persistence.UniqueIdentifier;
import de.timmeey.libTimmeey.sensor.Sensor;
import de.timmeey.libTimmeey.sensor.reading.Reading;
import java.time.ZonedDateTime;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jooq.DSLContext;
import org.jooq.ForeignKey;

/**
 * FkWeights.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
public final class WeightRecord implements Weights {
    private final de.timmeey.iot.jooq.sqlite.tables.records.WeightRecord src;
    private final DSLContext jooq;
    private final Sensors sensors;

    @Override
    public Iterable<MetricWeight> allWeights() throws Exception {
        final Iterator<? extends Reading> weights = this.weightSensor()
            .readings().iterator();
        final Iterator<? extends Reading> fats = this.fatSensor()
            .readings().iterator();
        final Iterator<? extends Reading> waters = this.waterSensor()
            .readings().iterator();
        final Iterator<? extends Reading> bones = this.boneSensor()
            .readings().iterator();
        final Iterator<? extends Reading> muscles = this.muscleSensor()
            .readings().iterator();

        final Deque<MetricWeight> result = new LinkedList<>();
        while (weights.hasNext()
            && fats.hasNext()
            && waters.hasNext()
            && bones.hasNext()
            && muscles.hasNext()) {
            result.add(new WeightFromReadings(
                weights.next(),
                fats.next(),
                waters.next(),
                bones.next(),
                muscles.next())
            );
        }
        return result;

    }

    @Override
    public MetricWeight addWeight(ZonedDateTime dt, MetricWeightRequest mwr)
        throws Exception {
        return new WeightFromReadings(
            this.weightSensor().addReading(mwr.weight(), dt),
            this.fatSensor().addReading(mwr.bodyFat(), dt),
            this.waterSensor().addReading(mwr.bodyWater(), dt),
            this.boneSensor().addReading(mwr.boneMass(), dt),
            this.muscleSensor().addReading(mwr.muscle(), dt)
        );

    }

    private Sensor weightSensor() {
        return this.sensor(FK_WEIGHT_SENSOR_5);
    }

    private Sensor fatSensor() {
        return this.sensor(FK_WEIGHT_SENSOR_4);
    }

    private Sensor waterSensor() {
        return this.sensor(FK_WEIGHT_SENSOR_3);
    }

    private Sensor boneSensor() {
        return this.sensor(FK_WEIGHT_SENSOR_2);
    }

    private Sensor muscleSensor() {
        return this.sensor(FK_WEIGHT_SENSOR_1);
    }

    private Sensor sensor(ForeignKey<de.timmeey.iot.jooq.sqlite.tables
        .records.WeightRecord, SensorRecord>
        sensorKey) {
        return sensors.sensor(new UUIDUniqueIdentifier(sensorKey.fetchParent
            (this.src).getId())).get();
    }

    public UniqueIdentifier<String> getId() {
        return new UUIDUniqueIdentifier(this.src.getId());
    }
}
