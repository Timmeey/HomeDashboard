package de.timmeey.iot.homeDashboard.lights;

import de.timmeey.libTimmeey.observ.Observable;
import java.awt.Color;

/**
 * ColorSource.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public interface ColorSource extends Observable<Color>{

    Color currentColor();
}
