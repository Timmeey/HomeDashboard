package de.timmeey.iot.homeDashboard.bvg;

/**
 * Vehicle.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public enum Vehicle {

    Tram("Tra","Tram"), UBahn("U","U-Bahn"), SBahn("S","S-Bahn"), Faehre("F","Fähre"), Bus("Bus","Bus"), RegionalExpress(// NOSONAR
        "RE","Regional Express"), RegionalBahn("RB","Regional Bahn"), InterCityExpress("ICE","Intercity Express"), InterCity("IC","Intercity"), Unkown("unkown","Unbekannt"); // NOSONAR

    private final String shortCode;
    private final String name;

    Vehicle(String shortCode, String name) {
        this.shortCode = shortCode;
        this.name = name;
    }

    public String shortcode() {
        return this.shortCode;
    }

    public String fullname(){
        return this.name;
    }

    public static Vehicle byShortcode(String shortCode) {
        for (Vehicle vehicle : Vehicle.values()) {
            if (vehicle.shortcode().equals(shortCode)) {
                return vehicle;
            }
        }
        return Vehicle.Unkown;

    }

    public static Vehicle getVehicleByName(String name) {
        for (Vehicle vehicle : Vehicle.values()) {
            if (vehicle.shortcode().equals(name)) {
                return vehicle;
            }
        }
        return Vehicle.Unkown;

    }
}
