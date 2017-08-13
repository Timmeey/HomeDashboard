package de.timmeey.iot.homeDashboard;

import com.google.gson.Gson;
import org.kohsuke.MetaInfServices;
import ro.pippo.controller.MethodParameter;
import ro.pippo.controller.extractor.MethodParameterExtractor;
import ro.pippo.core.route.RouteContext;

/**
 * JsonBeanExtractor.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@MetaInfServices
public class JsonBeanExtractor implements MethodParameterExtractor {
    Gson gson = new Gson();

    @Override
    public boolean isApplicable(MethodParameter parameter) {
        return parameter.isAnnotationPresent(DTO.class);
    }

    @Override
    public Object extract(MethodParameter parameter, RouteContext routeContext) {
        Class<?> parameterType = parameter.getParameterType();

        return gson.fromJson(routeContext.getRequest().getBody(),parameterType);
    }

}
