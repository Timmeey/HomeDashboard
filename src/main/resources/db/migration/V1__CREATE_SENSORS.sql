CREATE TABLE sensor(
   id TEXT PRIMARY KEY,
   unit TEXT
);

CREATE TABLE sensor_reading(
   id TEXT PRIMARY KEY,
   value DOUBLE NOT NULL,
   datetime DATETIME NOT NULL,
   sensor_id TEXT NOT NULL,
   FOREIGN KEY(sensor_id) REFERENCES sensor(id)
);
