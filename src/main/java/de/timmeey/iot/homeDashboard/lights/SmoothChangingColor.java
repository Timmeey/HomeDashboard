package de.timmeey.iot.homeDashboard.lights;

import de.timmeey.libTimmeey.Func.UncheckedBiFunc;
import de.timmeey.libTimmeey.Iterable.EntryTransformingIterator;
import de.timmeey.libTimmeey.color.RGBColor;
import de.timmeey.libTimmeey.color.TColor;
import de.timmeey.libTimmeey.math.LinearSteppingTransform;
import java.awt.Color;
import java.util.Arrays;
import java.util.Iterator;
import org.cactoos.iterator.Mapped;
import org.cactoos.scalar.UncheckedScalar;

/**
 * SmoothChangingColor.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class SmoothChangingColor implements Iterator<Color> {
    private final Iterator<Color> colors;

    public SmoothChangingColor(final UncheckedScalar<TColor> start, final
    UncheckedScalar<TColor> end, final long
        steps) {

        this.colors = new Mapped<>(new EntryTransformingIterator<>(
            Arrays.stream(start.value().asRGB()).iterator(),
            Arrays.stream(end.value().asRGB()).iterator(),
            new UncheckedBiFunc<>(
                (startSingleValue, targetSingleValue) ->
                    new Mapped<>(
                        new LinearSteppingTransform(startSingleValue,
                            targetSingleValue,
                            steps > 0 ? steps : 1),
                        Double::intValue))), rgbArr ->
            new RGBColor(rgbArr.next(), rgbArr.next(), rgbArr.next())
                .asColor());

    }

    @Override
    synchronized public boolean hasNext() {
        return colors.hasNext();
    }

    @Override
    synchronized public Color next() {
        return colors.next();
    }
}
