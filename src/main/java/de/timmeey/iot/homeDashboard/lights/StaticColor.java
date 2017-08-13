package de.timmeey.iot.homeDashboard.lights;

import de.timmeey.libTimmeey.observ.Observer;
import java.awt.Color;

/**
 * StaticColor.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class StaticColor implements ColorSource{
    private final Color color;

    public StaticColor(final Color color) {
        this.color = color;
    }

    @Override
    public void register(final Observer<Color> observer) {
    }

    @Override
    public void unregister(final Observer observer) {
    }

    @Override
    public Color currentColor() {
        return this.color;
    }
}
