package acco.isac.sensor;

import acco.isac.environment.Position;

public class SensorMessage {

	private String sensorId;
	private Position position;
	private boolean value;

	public SensorMessage(String sensorId, Position position, boolean value){
		this.sensorId = sensorId;
		this.position = position;
		this.value = value;
	}
	
}
