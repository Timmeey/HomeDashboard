package de.timmeey.iot.homeDashboard;

import de.timmeey.iot.homeDashboard.sensors.SqlReading;
import de.timmeey.iot.homeDashboard.sensors.SqlSensor;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import org.cactoos.Text;
import org.cactoos.text.JoinedText;
import org.cactoos.text.TextOf;

/**
 * SetupDatabase.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@SuppressWarnings({"JDBCExecuteWithNonConstantString", "StringConcatenation"})
class SetupDatabase {

    private final Connection conn;

    SetupDatabase(final Connection conn) {
        this.conn = conn;
    }

    final void setup() throws SQLException, IOException {
        final boolean oldCommitState = conn.getAutoCommit();
        try {
            conn.setAutoCommit(false);
            StringBuilder sb = new StringBuilder(2);
            sb.append(sensorTable().asString()).append(readingTable()
                .asString());
            System.out.println(sb.toString());
            conn.createStatement().executeUpdate(sb.toString());
        } finally {
            conn.commit();
            conn.setAutoCommit(oldCommitState);
        }

    }

    private Table readingTable() {
        String appendix = "FOREIGN KEY(sensor_id) REFERENCES sensors(id)";
        return new Table(SqlReading.READING_TABLE_NAME, appendix,
            new Column("id", "TEXT", "NOT NULL", "PRIMARY KEY"),
            new Column("value", "DECIMAL", "NOT NULL"),
            new Column("datetime", "DATE", "NOT NULL"),
            new Column("sensor_id", "TEXT", true, "NOT NULL")
        );
    }

    private Table sensorTable() {
        return new Table(SqlSensor.TABLE_NAME, "",
            new Column("id", "TEXT", "PRIMARY KEY"),
            new Column("unit", "TEXT", "NOT NULL")
        );
    }

    private static class Column implements Text {

        private final String name;
        private final String type;
        private final String[] attributes;
        private final boolean isIndex;

        public Column(final String name,
            final String type,
            final boolean isIndex,
            final String... attributes) {

            this.name = name;
            this.type = type;
            this.attributes = attributes;
            this.isIndex = isIndex;
        }

        public Column(final String name,
            final String type,
            final String... attributes) {

            this.name = name;
            this.type = type;
            this.attributes = attributes;
            this.isIndex = false;
        }

        @Override
        public String asString() throws IOException {
            return String.format("%s %s %s", name, type, new JoinedText("" +
                " ", attributes).asString());
        }

        @Override
        public int compareTo(final Text o) {
            throw new UnsupportedOperationException("#compareTo()");
        }

        public boolean isIndex() {
            return isIndex;
        }
    }

    private static class Table implements Text {
        private final String name;
        private final String appendix;
        private final SetupDatabase.Column[] columns;

        public Table(final String name, final String appendix,
            final Column... columns) {
            this.name = name;
            this.appendix = appendix;
            this.columns = columns;
        }

        public String asString() throws IOException {
            final StringBuilder sb = new StringBuilder();
            if (this.appendix.isEmpty()) {
                sb.append(String.format("CREATE TABLE %s(\n%s\n);\n", name, new
                    JoinedText(new TextOf(",\n"), columns).asString())
                );
            } else {
                sb.append(String.format("CREATE TABLE %s(\n%s,\n%s\n);\n",
                    name,
                    new JoinedText(new TextOf(",\n"), columns).asString(),
                    appendix
                    )
                );
            }
            Arrays.stream(columns).filter(c -> c.isIndex()).forEach(c -> sb
                .append(String.format(
                "CREATE INDEX %sindex ON %s(%s)\n",
                c.name,
                this.name,
                c.name
            )));
            return sb.toString();
        }

        @Override
        public int compareTo(final Text o) {
            throw new UnsupportedOperationException("#compareTo()");
        }
    }
}
