package de.timmeey.iot.homeDashboard.health.weigth;

import de.timmeey.iot.homeDashboard.sensors.SqliSensorTable;
import de.timmeey.libTimmeey.sql.sqlite.SqliAttribute;
import de.timmeey.libTimmeey.sql.sqlite.SqliColumn;
import de.timmeey.libTimmeey.sql.sqlite.SqliTable;

/**
 * SqliWeightsTable.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class SqliWeightsTable {
    private static final SqliTable sensorTable = SqliSensorTable.table;

    public static final SqliColumn weightSensor = new SqliColumn
        ("weightSensor_id", SqliColumn.SqliteDataType.TEXT,
        sensorTable, sensorTable.primaryKeyCoulmn().get());

    public static final SqliColumn fatSensor = new SqliColumn("fatSensor_id",
        SqliColumn.SqliteDataType.TEXT,
        sensorTable, sensorTable.primaryKeyCoulmn().get());
    public static final SqliColumn waterSensor = new SqliColumn
        ("waterSensor_id", SqliColumn.SqliteDataType.TEXT,
        sensorTable, sensorTable.primaryKeyCoulmn().get());
    public static final SqliColumn boneSensor = new SqliColumn
        ("boneSensor_id", SqliColumn.SqliteDataType.TEXT,
        sensorTable, sensorTable.primaryKeyCoulmn().get());
    public static final SqliColumn muscleSensor = new SqliColumn
        ("muscleSensor_id", SqliColumn.SqliteDataType.TEXT,
        sensorTable, sensorTable.primaryKeyCoulmn().get());

    public static final SqliTable table = new SqliTable("weights", new
        SqliColumn("id", SqliColumn.SqliteDataType.TEXT, false, SqliAttribute
        .PRIMARY_KEY),
        weightSensor,
        fatSensor,
        waterSensor,
        boneSensor,
        muscleSensor
    );
}
