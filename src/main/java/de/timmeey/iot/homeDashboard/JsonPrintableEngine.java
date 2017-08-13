package de.timmeey.iot.homeDashboard;

import com.google.gson.Gson;
import de.timmeey.libTimmeey.printable.JsonPrettyPrinted;
import de.timmeey.libTimmeey.printable.Printable;
import de.timmeey.libTimmeey.printable.Printed;
import java.util.Map;
import org.cactoos.iterable.ListOf;
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
        if (o instanceof Printable) {
            return ((Printable) o).print(new JsonPrettyPrinted()).asString();
        } else if (o instanceof Iterable && new ListOf<>((Iterable)o).stream()
            .allMatch(p -> p instanceof Printable)) {
            return new JsonPrettyPrinted().onlyList((Iterable<Printable>) o)
                .asString();
        } else if(o instanceof Map && ((Map) o).values().stream().allMatch(p -> p instanceof Printable)){

            final Printed printed = new JsonPrettyPrinted();
            ((Map) o).forEach((key,value) -> printed.with(key.toString(),(Printable)value));
            return printed.asString();
        }
        return o.toString();
    }

    @Override
    public <T> T fromString(final String s, final Class<T> aClass) {
        return gson.fromJson(s,aClass);
    }
}
