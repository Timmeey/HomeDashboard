package de.timmeey.iot.homeDashboard.sensors.controller.dto;

import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * ReadingRequest.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@Getter
@Accessors(fluent = true)
public class ReadingRequest {
    private double value;

    private ZonedDateTime datetime;
}
