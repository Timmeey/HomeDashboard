package de.timmeey.iot.homeDashboard.sensors.readings;

import de.timmeey.iot.homeDashboard.sensors.SqliSensorTable;
import de.timmeey.libTimmeey.sql.sqlite.SqliAttribute;
import de.timmeey.libTimmeey.sql.sqlite.SqliColumn;
import de.timmeey.libTimmeey.sql.sqlite.SqliTable;

/**
 * SqliReadingTable.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class SqliReadingTable {

    public static final SqliColumn valueColumn = new SqliColumn("value",
        SqliColumn.SqliteDataType.REAL, false,
        SqliAttribute.NOT_NULL);
    public static final SqliColumn datetimeColumn = new SqliColumn
        ("datetime", SqliColumn.SqliteDataType.INTEGER, false,
        SqliAttribute.NOT_NULL);
    public static final SqliColumn sensor_idColumn = new SqliColumn
        ("sensor_id", SqliColumn.SqliteDataType.TEXT,
        SqliSensorTable.table,
        SqliSensorTable.table.primaryKeyCoulmn().get(),
        SqliAttribute.NOT_NULL);
    public static final SqliTable table = new SqliTable("sensor_reading", new
        SqliColumn("id", SqliColumn.SqliteDataType.TEXT, false, SqliAttribute
        .PRIMARY_KEY),
        valueColumn,
        datetimeColumn,
        sensor_idColumn
    );
}
