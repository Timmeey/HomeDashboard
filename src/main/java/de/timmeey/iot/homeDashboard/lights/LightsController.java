package de.timmeey.iot.homeDashboard.lights;

import java.awt.Color;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.MetaInfServices;
import ro.pippo.controller.Consumes;
import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.POST;
import ro.pippo.controller.Path;
import ro.pippo.controller.Produces;
import ro.pippo.controller.extractor.Body;
import ro.pippo.controller.extractor.Param;

/**
 * LightsController.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@MetaInfServices
@Slf4j
@Path("/lights")
public class LightsController extends Controller {

    private final Map<Long, Light> lights;
    

    public LightsController(@NonNull final Map<Long, Light> lights) {
        this.lights = lights;
        Math.random();
    }

    @GET("/")
    @Produces(Produces.JSON)
    public Map<Long, Light> allLights() {
        return this.lights;
    }

    @GET("/{id}")
    @Produces(Produces.JSON)
    public Light light(@Param("id") long id) {
        return Optional.ofNullable(this.lights.get(id)).orElseThrow(() -> new
            NoSuchElementException(String.format("No Light with id %s found",
            id)));
    }

    @POST("/{id}")
    @Consumes(Consumes.JSON)
    @Produces(Produces.JSON)
    public boolean setColor(@Param("id") long id, @Body ColorBean newColor) {
        log.debug("Color {} {} {}", newColor.getRed(), newColor.getGreen(), newColor
            .getBlue());
        System.out.println(newColor);
        Optional.ofNullable(this.lights.get(id)).ifPresent(light -> {
            light
                .colorSource(new SmoothChangingColorSource(
                    new StaticColor(new Color(
                        newColor.getRed(), newColor.getGreen(),newColor.getBlue())),
                    new StaticColor(light
                        .currentColor().orElse(Color.BLACK)),
                    newColor.getFading() >0 ? newColor.getFading() : 250 )
                );
            light.turnOn();
        });
        return true;

    }

}
