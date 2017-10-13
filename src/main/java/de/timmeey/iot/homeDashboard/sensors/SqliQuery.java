package de.timmeey.iot.homeDashboard.sensors;

import de.timmeey.iot.homeDashboard.sensors.readings.SqliReadingTable;
import de.timmeey.libTimmeey.sql.sqlite.SqliColumn;
import de.timmeey.libTimmeey.sql.sqlite.SqliTable;

/**
 * SqliQuery.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class SqliQuery {
    StringBuilder select = new StringBuilder("SELECT ");
    StringBuilder where = new StringBuilder();
    private final SqliTable from;
    private boolean firstSelect = true;
    private boolean whereAlreadyStarted = false;

    public SqliQuery(final SqliTable from) {
        this.from = from;
    }

    public SqliQuery SELECT(final SqliColumn column) {
        if (this.firstSelect) {
            select.append(column.name());
            firstSelect = false;
        } else {
            select.append(", " + column.name());
        }
        return this;
    }

    public SqliQuery WHERE(final SqliColumn column, String operator) {
        if (whereAlreadyStarted) {
            throw new IllegalStateException("WHERE clause already started, " +
                "use AND/OR for further conditions");
        } else {
            where.append(String.format(" WHERE %s %s ? ", column.name(), operator));
            whereAlreadyStarted = true;
        }
        return this;
    }

    public SqliQuery AND(final SqliColumn column, String operator) {
        if (whereAlreadyStarted) {
            where.append(String.format("AND %s %s ?", column.name(), operator));
        } else {
            throw new IllegalStateException("WHERE clause not started, use " +
                "WHERE for first conditions");

        }
        return this;
    }

    public SqliQuery OR(final SqliColumn column, String operator) {
        if (whereAlreadyStarted) {
            where.append(String.format("AND %s %s ?", column.name(), operator));
        } else {
            throw new IllegalStateException("WHERE clause not started, use " +
                "WHERE for first conditions");

        }
        return this;
    }

    public String toString() {
        return new StringBuilder().append(this.select).append(" FROM ").append(from.name()).append(where).append(";").toString();
    }

    public static void main(String[] args) {
        System.out.println(new SqliQuery(SqliReadingTable.table)
            .SELECT(SqliReadingTable.valueColumn)
            .SELECT(SqliReadingTable.datetimeColumn)
            .toString());
    }

}
