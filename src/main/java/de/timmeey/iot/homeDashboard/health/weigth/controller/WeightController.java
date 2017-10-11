package de.timmeey.iot.homeDashboard.health.weigth.controller;

import de.timmeey.iot.homeDashboard.EnhancedRequest;
import de.timmeey.iot.homeDashboard.health.weigth.MetricWeight;
import de.timmeey.iot.homeDashboard.health.weigth.Weights;
import de.timmeey.iot.homeDashboard.health.weigth.controller.dto.MetricWeightRequest;
import de.timmeey.libTimmeey.printable.Printable;
import de.timmeey.libTimmeey.printable.Printed;
import java.time.ZonedDateTime;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.cactoos.iterable.Mapped;
import ro.pippo.controller.Consumes;
import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.POST;
import ro.pippo.controller.Path;
import ro.pippo.controller.Produces;
import ro.pippo.controller.extractor.Body;

/**
 * WeightController.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@SuppressWarnings("ProhibitedExceptionDeclared")
@Slf4j
@Path("/health/weight")
public final class WeightController extends Controller {
    private final Weights weights;

    public WeightController(final Weights weights) {
        this.weights = weights;
    }

    @GET
    @Produces(Produces.JSON)
    public Iterable<MetricWeight> weight() throws Exception {
        val height = new EnhancedRequest(this.getRequest())
            .getQueryParamAsInt("height");
        val result = this.weights.allWeights();
        return height.<Iterable<MetricWeight>>map(integer -> new Mapped<>
            (result, metric -> new BMIEnabledWeight(metric, integer)))
            .orElse(result);

    }

    @POST("/")
    @Consumes(Consumes.JSON)
    @Produces(Produces.JSON)
    public MetricWeight addWeights(@Body final MetricWeightRequest weightRequest) throws
        Exception {
        return this.weights.addWeight(ZonedDateTime.now(),weightRequest);
    }

    private static final class BMIEnabledWeight implements MetricWeight {
        @Delegate(excludes = Printable.class)
        private final MetricWeight src;
        private final int height;

        private BMIEnabledWeight(final MetricWeight src, final int height) {
            this.src = src;
            this.height = height;
        }

        @Override
        public Printed print(final Printed printed) {
            return MetricWeight.super.print(printed).with("bmi", this.src.bmi
                (this.height));
        }
    }
}
