package de.timmeey.iot.homeDashboard.health.weigth.controller.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * ColorBean.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@XmlRootElement
@Getter
@Accessors(fluent = true)
public class MetricWeightRequest {
    @XmlAttribute
    private double weight;
    @XmlAttribute
    private double bodyFat;
    @XmlAttribute
    private double bodyWater;
    @XmlAttribute
    private double boneMass;
    @XmlAttribute
    private double muscle;
}
