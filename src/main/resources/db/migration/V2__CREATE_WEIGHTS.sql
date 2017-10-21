CREATE TABLE weight(
  id TEXT PRIMARY KEY,
  weightSensor_id TEXT ,
  fatSensor_id TEXT ,
  waterSensor_id TEXT ,
  boneSensor_id TEXT ,
  muscleSensor_id TEXT ,
  FOREIGN KEY(weightSensor_id) REFERENCES sensor(id),
  FOREIGN KEY(fatSensor_id) REFERENCES sensor(id),
  FOREIGN KEY(waterSensor_id) REFERENCES sensor(id),
  FOREIGN KEY(boneSensor_id) REFERENCES sensor(id),
  FOREIGN KEY(muscleSensor_id) REFERENCES sensor(id)
);
