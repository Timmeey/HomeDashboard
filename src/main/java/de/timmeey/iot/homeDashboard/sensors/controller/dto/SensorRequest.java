package de.timmeey.iot.homeDashboard.sensors.controller.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * SensorRequest.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@XmlRootElement
@Getter
@Accessors(fluent = true)
public class SensorRequest {
    @XmlAttribute
    private String unit;
}
