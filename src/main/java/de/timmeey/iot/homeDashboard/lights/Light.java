package de.timmeey.iot.homeDashboard.lights;

import de.timmeey.libTimmeey.printable.Printable;
import java.awt.Color;
import java.util.Optional;
import lombok.NonNull;

/**
 * Light.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public interface Light extends Printable {

    /**
     * Turns this light off
     */
    void turnOff();

    /**
     * Turns this light on with the last color
     */
    void turnOn();

    /**
     * Sets the new color source for this light
     * @param src
     */
    void colorSource(@NonNull ColorSource src);

    /**
     * Returns if the light is on or off
     * @return if the light is on or off
     */
    boolean isTurnedOn();

    /**
     * The currently shown color, or empty if turned off
     * @return THe currently shown color
     */
    Optional<Color> currentColor();


}
