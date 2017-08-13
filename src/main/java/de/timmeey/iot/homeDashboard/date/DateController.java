package de.timmeey.iot.homeDashboard.date;

import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.Path;
import ro.pippo.controller.Produces;

/**
 * DateController.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@Path("/date")
public class DateController extends Controller {
    public DateController() {
    }

    @GET("/")
    @Produces(Produces.JSON)
    public Date date(){
        return new Date();
    }
}
