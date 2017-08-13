package de.timmeey.iot.homeDashboard.lights;

import com.google.common.base.Stopwatch;
import de.timmeey.libTimmeey.color.RGBColor;
import de.timmeey.libTimmeey.color.TColor;
import de.timmeey.libTimmeey.observ.Observer;
import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.cactoos.scalar.StickyScalar;
import org.cactoos.scalar.UncheckedScalar;

/**
 * SmoothChangingColorSource.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@Slf4j
public class SmoothChangingColorSource implements ColorSource {
    private static final long DEFAULT_FADING_TIME = 100l;
    private static final long STEP_LENGTH_IN_MS = 20l;

    private final ColorSource newSrc;
    private final List<Observer<Color>> observer = new LinkedList<>();
    private final Iterator<Color> smoothChangedColors;
    private final Runnable changer;
    private Color buffer;
    private final AtomicBoolean finished = new AtomicBoolean(false);
    private final AtomicBoolean started = new AtomicBoolean(false);

    public SmoothChangingColorSource(@NonNull final ColorSource newColorSrc,
        @NonNull final UncheckedScalar<TColor> oldColor,
        final long fadingTime) {
        this.smoothChangedColors = new SmoothChangingColor(oldColor,
            new UncheckedScalar<>(new StickyScalar<>(() -> new RGBColor
                (newColorSrc.currentColor()))),
            fadingTime / SmoothChangingColorSource.STEP_LENGTH_IN_MS);
        this.newSrc = newColorSrc;

        this.changer = () -> {
            try {
                Color nextColor=null;
                Color lastColor=null;
                while (this.smoothChangedColors.hasNext()) {
                    try {
                        long time = System.currentTimeMillis();
                        lastColor=nextColor;
                        nextColor = this.smoothChangedColors.next();
                        if(!nextColor.equals(lastColor)) {
                            Stopwatch watch1 = Stopwatch.createStarted();
                            this.updateColor(nextColor);
                            if(watch1.elapsed(TimeUnit.MILLISECONDS)>1)
                                System.out.println("Updating took: " +watch1.stop().elapsed(TimeUnit.MILLISECONDS));
                        }

                        Thread.sleep(System.currentTimeMillis()-time+SmoothChangingColorSource
                            .STEP_LENGTH_IN_MS);

                    } catch (InterruptedException e) {
                        this.updateColor(newColorSrc.currentColor());
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            } finally {
                this.finished.set(true);
                this.observer.forEach(newColorSrc::register);
            }
        };
        SmoothChangingColorSource.log.debug("TargetColor {}, start Color {}",
            newColorSrc.currentColor(), oldColor);
    }

    public SmoothChangingColorSource(@NonNull final ColorSource newColorSrc,
        @NonNull final ColorSource oldColor,
        final long fadingTime) {
        this(newColorSrc, new UncheckedScalar<>(new StickyScalar<>(()
            -> new RGBColor(oldColor.currentColor()))),fadingTime);
    }

    private void updateColor(@NonNull Color color) {
        this.buffer = color;
        this.observer.forEach(o -> o.update(color));
    }

    @Override
    public Color currentColor() {
        synchronized (this) {
            this.ensureStarted();
            if (this.buffer == null) {
                this.updateColor(this.smoothChangedColors.next());
            }
            return this.buffer;
        }
    }

    @Override
    public void register(final Observer<Color> observer) {
        this.ensureStarted();
        if (this.finished.get()) {
            this.newSrc.register(observer);
        } else {
            this.observer.add(observer);
        }
    }

    private void ensureStarted() {
        if (!this.started.getAndSet(true)) {
            new Thread(this.changer).start();
        }
    }

    @Override
    public void unregister(final Observer observer) {
        this.newSrc.unregister(observer);
        this.observer.remove(observer);
    }
}
