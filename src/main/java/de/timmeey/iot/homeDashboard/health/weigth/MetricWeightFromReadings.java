package de.timmeey.iot.homeDashboard.health.weigth;

import de.timmeey.libTimmeey.sensor.reading.Reading;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * MuhMetricWeight.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@Getter
@Accessors(fluent = true)
public class MetricWeightFromReadings implements MetricWeight {
    private final ZonedDateTime datetime;
    private final double weight, bodyFat, bodyWater, boneMass, muscle;

    public MetricWeightFromReadings(final Reading weight, final Reading fat,
        final Reading water, final Reading boneMass, final Reading muscle) {
        this.datetime = weight.datetime();
        this.weight = weight.value();
        this.bodyFat = fat.value();
        this.bodyWater = water.value();
        this.boneMass = boneMass.value();
        this.muscle = muscle.value();
    }
}
