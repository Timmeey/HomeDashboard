package de.timmeey.iot.homeDashboard.health.weigths.weight;

import de.timmeey.libTimmeey.printable.Printable;
import de.timmeey.libTimmeey.printable.Printed;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Weight.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public interface MetricWeight extends Printable{
    double weight();

    double bodyFat();

    double bodyWater();

    double boneMass();

    double muscle();

    ZonedDateTime datetime();

    @SuppressWarnings("unused")
    default double bmi(final int heightInCm){
        return this.weight()/ Math.pow((double) heightInCm / 100.0d, 2.0);
    }


    @Override
    default Printed print(final Printed printed) {
        return printed.with("weight", this.weight())
            .with("bodyFat", this.bodyFat())
            .with("bodyWater", this.bodyWater())
            .with("boneMass", this.boneMass())
            .with("muscle", this.muscle())
            .with("dateTime", this.datetime().format(DateTimeFormatter.ISO_DATE_TIME));
    }
}
