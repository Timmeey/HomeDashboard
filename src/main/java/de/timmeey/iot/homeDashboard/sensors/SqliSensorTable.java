package de.timmeey.iot.homeDashboard.sensors;

import de.timmeey.libTimmeey.sql.sqlite.SqliAttribute;
import de.timmeey.libTimmeey.sql.sqlite.SqliColumn;
import de.timmeey.libTimmeey.sql.sqlite.SqliTable;

/**
 * SqliSensorTable.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class SqliSensorTable {
    public static final SqliColumn unitColumn = new SqliColumn("unit",
        SqliColumn.SqliteDataType.TEXT, false);

    public static final SqliTable table = new SqliTable("sensor", new
        SqliColumn("id", SqliColumn.SqliteDataType.TEXT, false, SqliAttribute
        .PRIMARY_KEY),
        unitColumn
    );
}
