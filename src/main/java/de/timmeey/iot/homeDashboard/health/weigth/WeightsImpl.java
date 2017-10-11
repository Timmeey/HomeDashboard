package de.timmeey.iot.homeDashboard.health.weigth;

import de.timmeey.iot.homeDashboard.health.weigth.controller.dto
    .MetricWeightRequest;
import de.timmeey.libTimmeey.sensor.Sensor;
import de.timmeey.libTimmeey.sensor.reading.Reading;
import java.time.ZonedDateTime;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import lombok.RequiredArgsConstructor;

/**
 * FkWeights.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@RequiredArgsConstructor
public final class WeightsImpl implements Weights {
    private final Sensor weightSensor;
    private final Sensor fatSensor;
    private final Sensor waterSensor;
    private final Sensor boneSensor;
    private final Sensor muscleSensor;

    @Override
    public Iterable<MetricWeight> allWeights() throws Exception {
        final Iterator<Reading> weights = this.weightSensor.readings().iterator();
        final Iterator<Reading> fats = this.fatSensor.readings().iterator();
        final Iterator<Reading> waters = this.waterSensor.readings().iterator();
        final Iterator<Reading> bones = this.boneSensor.readings().iterator();
        final Iterator<Reading> muscles = this.muscleSensor.readings().iterator();

        final Deque<MetricWeight> result = new LinkedList<>();
        while (weights.hasNext() && fats.hasNext() && waters.hasNext() &&
            bones.hasNext() && muscles.hasNext()) {
            result.add(new MetricWeightFromReadings(weights.next(), fats.next
                (), waters.next(), bones.next(), muscles.next()));
        }
        return result;

    }

    @Override
    public MetricWeight addWeight(ZonedDateTime dt, MetricWeightRequest mwr)
        throws Exception {
        return new MetricWeightFromReadings(
            this.weightSensor.addReading(mwr.weight(), dt),
            this.fatSensor.addReading(mwr.bodyFat(), dt),
            this.waterSensor.addReading(mwr.bodyWater(), dt),
            this.boneSensor.addReading(mwr.boneMass(), dt),
            this.muscleSensor.addReading(mwr.muscle(), dt)
        );
    }
}
