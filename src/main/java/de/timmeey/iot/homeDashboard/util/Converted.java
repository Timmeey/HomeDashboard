package de.timmeey.iot.homeDashboard.util;

/**
 * Converted.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 */
public interface Converted<FROM, TO> {

    FROM from();
    TO to();
}
