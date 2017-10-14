package de.timmeey.iot.homeDashboard.health.weigth;

import de.timmeey.iot.homeDashboard.health.weigth.controller.dto
    .MetricWeightRequest;
import de.timmeey.iot.homeDashboard.sensors.Sensors;
import de.timmeey.iot.jooq.sqlite.Tables;
import de.timmeey.iot.jooq.sqlite.tables.records.WeightsRecord;
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
import org.jooq.TableField;

/**
 * FkWeights.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
public final class WeightsJooq implements Weights {
    private final UniqueIdentifier<String> id;
    private final DSLContext jooq;
    private final de.timmeey.iot.jooq.sqlite.tables.Weights table = Tables
        .WEIGHTS;
    private final Sensors sensors;

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
        return this.sensor(table.WEIGHTSENSOR_ID);
    }

    private Sensor fatSensor() {
        return this.sensor(table.FATSENSOR_ID);
    }

    private Sensor waterSensor() {
        return this.sensor(table.WATERSENSOR_ID);
    }

    private Sensor boneSensor() {
        return this.sensor(table.BONESENSOR_ID);
    }

    private Sensor muscleSensor() {
        return this.sensor(table.MUSCLESENSOR_ID);
    }

    private Sensor sensor(TableField<WeightsRecord, String>
        sensorIdColumnName) {
        return sensors.sensor(new UUIDUniqueIdentifier(jooq.select
            (sensorIdColumnName).from(table).where(table.ID.eq(this.id.id())).fetchOne().component1())).get();
    }

    public UniqueIdentifier<String> getId() {
        return this.id;
    }
}
