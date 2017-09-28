package de.timmeey.iot.homeDashboard;

import ro.pippo.core.Application;
import ro.pippo.core.DefaultErrorHandler;
import ro.pippo.core.PippoRuntimeException;
import ro.pippo.core.route.RouteContext;

/**
 * NewDefaultErrorHandler.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class NewDefaultErrorHandler extends DefaultErrorHandler {
    public NewDefaultErrorHandler(final Application application) {
        super(application);
    }

    @Override
    public void handle(final Exception exception, final RouteContext
        routeContext) {
        if (exception instanceof PippoRuntimeException && exception.getCause
            () instanceof Exception) {
            this.handle((Exception) exception.getCause(), routeContext);

        }else{
            super.handle(exception,routeContext);
        }
    }
}
