package de.timmeey.iot.homeDashboard.health.weigths;

import de.timmeey.iot.homeDashboard.health.weigths.controller.dto
    .MetricWeightRequest;
import de.timmeey.iot.homeDashboard.health.weigths.weight.MetricWeight;
import java.time.ZonedDateTime;

/**
 * Weights.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 */
public interface Weights {

    Iterable<MetricWeight> allWeights() throws Exception;

    MetricWeight addWeight(ZonedDateTime datetime,MetricWeightRequest metricWeightRequest) throws Exception;
}
