package de.timmeey.iot.homeDashboard.sensors.controller;

import de.timmeey.iot.homeDashboard.sensors.Sensors;
import de.timmeey.iot.homeDashboard.sensors.controller.dto.ReadingRequest;
import de.timmeey.iot.homeDashboard.sensors.controller.dto.SensorRequest;
import de.timmeey.libTimmeey.persistence.UUIDUniqueIdentifier;
import de.timmeey.libTimmeey.sensor.Sensor;
import de.timmeey.libTimmeey.sensor.reading.Reading;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import ro.pippo.controller.Consumes;
import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.POST;
import ro.pippo.controller.Path;
import ro.pippo.controller.Produces;
import ro.pippo.controller.extractor.Body;
import ro.pippo.controller.extractor.Param;

/**
 * SensorsController.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@Path("/sensors")
@RequiredArgsConstructor
public class SensorsController extends Controller {
    private final Sensors sensors;

    @GET("/")
    @Produces(Produces.JSON)
    public Iterable<? extends Sensor> sensors() {
        return sensors.sensors();
    }

    @GET("/{sensorId}")
    @Produces(Produces.JSON)
    public Sensor sensor(@Param("sensorId") String sensorId) {
        return sensors.sensor(new UUIDUniqueIdentifier(sensorId))
            .orElseThrow(() ->
                new NoSuchElementException(
                    String.format(
                        "Sensor with id %s not found", sensorId)
                )
            );
    }

    @Consumes(Consumes.JSON)
    @Produces(Produces.JSON)
    @POST("/")
    public Sensor add(@Body SensorRequest sensorRequest) throws
        SQLException {
        return this.sensors.add(sensorRequest.unit());
    }

    @GET("/{sensorId}/readings")
    @Produces(Produces.JSON)
    public Iterable<Reading> readings(@Param("sensorId") String
        sensorId) throws Exception {
        Optional<? extends Sensor> sensor = sensors.sensor(new
            UUIDUniqueIdentifier(sensorId));
        if (sensor.isPresent()) {
            System.out.println("Retunring");
            return sensor.get().readings();
        } else {
            throw new NoSuchElementException(
                String.format(
                    "Sensor with id %s not found", sensorId)
            );
        }
    }

        @Consumes(Consumes.JSON)
        @Produces(Produces.JSON)
        @POST("/{sensorId}/readings")
        public Reading addReading (
            @Param("sensorId") String sensorId,
            @Body ReadingRequest readingRequest) throws SQLException {
            return this.sensors.sensor(
                new UUIDUniqueIdentifier(sensorId)
            ).map(
                sensor -> {
                    try {
                        return sensor.addReading(readingRequest.value(),
                            Optional.ofNullable(readingRequest.datetime())
                                .orElse(ZonedDateTime.now())
                        );
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }
            ).orElseThrow(() -> new NoSuchElementException(
                String.format(
                    "Sensor with id %s not found", sensorId))
            );
        }
    }
