package de.timmeey.iot.homeDashboard.health.weigth;

import de.timmeey.iot.homeDashboard.health.weigth.controller.dto
    .MetricWeightRequest;
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
