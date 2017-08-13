package de.timmeey.iot.homeDashboard.lights;

import de.timmeey.libTimmeey.printable.Printable;
import de.timmeey.libTimmeey.printable.Printed;
import java.awt.Color;
import java.util.Optional;

/**
 * PrintableLight.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class PrintableLight implements Light, Printable{

    private final Light src;

    public PrintableLight(final Light src) {
        this.src = src;
    }

    @Override
    public void turnOff() {
        src.turnOff();
    }

    @Override
    public void turnOn() {
        src.turnOn();
    }

    @Override
    public void colorSource(final ColorSource src) {
        this.src.colorSource(src);
    }

    @Override
    public boolean isTurnedOn() {
        return src.isTurnedOn();
    }

    @Override
    public Optional<Color> currentColor() {
        return src.currentColor();
    }

    @Override
    public Printed print(final Printed printed) {
        throw new UnsupportedOperationException("#print()");
    }
}
