package de.timmeey.iot.homeDashboard.lights;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 * ColorBean.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@XmlRootElement
@Data
public class ColorBean {
    @XmlAttribute
    private int red;
    @XmlAttribute
    private int green;
    @XmlAttribute
    private int blue;

    @XmlAttribute
    private long fading;
}
