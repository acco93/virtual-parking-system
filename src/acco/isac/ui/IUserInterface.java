package acco.isac.ui;

import java.util.concurrent.ConcurrentHashMap;

import acco.isac.sensor.SensorMessage;
import acco.isac.server.SensorRepresentation;

public interface IUserInterface {


	void updateSensors(ConcurrentHashMap<String, SensorRepresentation> storage);
}
