package de.timmeey.iot.homeDashboard.bvg.BVGMobileParsed;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * MobileDepartureWebsite.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@Slf4j
public final class MobileDepartureWebsite implements DocumentFromHtml{
    private final String stationName;
    private final Map<String, String> data;

    private static final String BASE_URL = "http://mobil.bvg" +
        ".de/Fahrinfo/bin/stboard.bin/dox?ld=0.1&";

    public MobileDepartureWebsite(final String stationName) {
        this.stationName = stationName;
        this.data = new HashMap<String,String>();
    }

    @Override
    public Document html() {
        this.data.put("input", this.stationName);
        this.data.put("start", "suchen");
        this.data.put("boardType","depRT");

        try {
            MobileDepartureWebsite.log.trace("Fetching website at URL: {}, with data: {}", MobileDepartureWebsite.BASE_URL, this.data);
            return Jsoup.parse(Jsoup.connect(MobileDepartureWebsite.BASE_URL).data(this.data).post().html());

        } catch (IOException e) {
            MobileDepartureWebsite.log.warn("Could not get {}", e, MobileDepartureWebsite.BASE_URL);
            throw new UncheckedIOException(e);
        }
    }
}
