package de.timmeey.iot.homeDashboard.util;

import com.google.gson.Gson;
import de.timmeey.libTimmeey.printable.JsonPrettyPrinted;
import de.timmeey.libTimmeey.printable.Printed;
import ro.pippo.core.Application;
import ro.pippo.core.ContentTypeEngine;
import ro.pippo.core.HttpConstants;

/**
 * JsonPrintableEngine.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class JsonPrintableEngine implements ContentTypeEngine {
    Gson gson = new Gson();

    @Override
    public void init(final Application application) {
    }

    @Override
    public String getContentType() {
        return HttpConstants.ContentType.APPLICATION_JSON;
    }

    @Override
    public String toString(final Object o) {
        return new Printed.IsPrintable().print(o, new JsonPrettyPrinted());
    }

    @Override
    public <T> T fromString(final String s, final Class<T> aClass) {
        return gson.fromJson(s,aClass);
    }
}
