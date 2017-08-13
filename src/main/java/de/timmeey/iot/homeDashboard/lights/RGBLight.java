package de.timmeey.iot.homeDashboard.lights;

import de.timmeey.libTimmeey.observ.Observer;
import de.timmeey.libTimmeey.printable.Printed;
import java.awt.Color;
import java.util.Optional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * UDPLight.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@Slf4j
public abstract class RGBLight implements Light {

    private boolean turnedOn;
    private Color color;
    private ColorSource colorSource;
    private final Observer<Color> updateListener;

    public RGBLight(@NonNull ColorSource colorSource) {
        this.updateListener = this::setColor;
        this.colorSource = colorSource;
    }

    private void setColor(@NonNull Color color){
        this.color = color;
        this.showColor(color);
    }

    @Override
    public final void turnOff() {
        if (this.turnedOn) {
            log.debug("Turning light off");
            this.colorSource.unregister(this.updateListener);
            this.turnedOn = false;
            this.switchOff();
        }
    }

    @Override
    public final void turnOn() {
        if (!this.turnedOn) {
            log.debug("Turning Light on");
            this.setColor(this.colorSource.currentColor());
            this.turnedOn = true;
            this.switchOn();
            this.colorSource.register(this.updateListener);
        }
    }

    @Override
    synchronized public final void colorSource(@NonNull final ColorSource src) {
        log.debug("Setting new color source");
        this.colorSource.unregister(this.updateListener);
        this.colorSource = src;
        this.setColor(this.colorSource.currentColor());
        this.colorSource.register(this.updateListener);
    }

    @Override
    public final Optional<Color> currentColor() {
        if (this.turnedOn) {
            return Optional.of(this.color);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public final boolean isTurnedOn() {
        return this.turnedOn;
    }

    Color getLastColor() {
        return this.color;
    }

    @Override
    public Printed print(final Printed printed) {
        printed.with("turnedOn", this.turnedOn);
        if (this.turnedOn) {
            printed.with("color", this.color.getRGB());
        } else {
            printed.with("color", "");
        }
        return this.enrich(printed);
    }

    abstract void switchOff();

    abstract void switchOn();

    abstract void showColor(Color color);

    /**
     * Add lightType specific information
     * @param printed the printed object
     * @return the enriched printed object
     */
    abstract Printed enrich(Printed printed);
}
