package de.timmeey.iot.homeDashboard;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DTO.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
public @interface DTO  {
}
