package acco.isac.sensor;

import acco.isac.environment.Position;

public class SensorMessage {

	private String sensorId;
	private Position position;
	private boolean free;

	public SensorMessage(String sensorId, Position position, boolean free){
		this.sensorId = sensorId;
		this.position = position;
		this.free = free;
	}

	public String getSensorId() {
		return sensorId;
	}

	public Position getPosition() {
		return position;
	}

	public boolean isFree() {
		return free;
	}
	
	
	
}
