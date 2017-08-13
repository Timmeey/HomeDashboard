package de.timmeey.iot.homeDashboard.bvg.BVGMobileParsed;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * MobileRouteWebsite.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@Slf4j
public final class MobileRouteWebsite implements DocumentFromHtml {

    private final URL routeWebsiteUrl;

    MobileRouteWebsite(final URL routeWebsiteUrl) {
        this.routeWebsiteUrl = routeWebsiteUrl;
    }

    @Override
    public Document html(){
        try {
            MobileRouteWebsite.log.trace("Fetching website at URL: {}, with data: {}", this
                .routeWebsiteUrl);
            return Jsoup.parse(this.routeWebsiteUrl,500);

        } catch (final IOException e) {
            MobileRouteWebsite.log.warn("Could not get {}", this.routeWebsiteUrl,e);
            throw new UncheckedIOException(e);
        }
    }
}
