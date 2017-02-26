package acco.isac.server.inforepresentation;

import java.util.Date;

import acco.isac.environment.Position;
import acco.isac.sharedknowledge.R;

/**
 * 
 * A simple server-side sensor representation.
 * 
 * @author acco
 *
 */
public class SensorRepresentation implements EnvironmentInfo{

	private String name;
	private Position position;
	private long lastHeartbeat;
	private boolean free;

	public SensorRepresentation(String name, Position position, boolean free) {
		this.name = name;
		this.position = position;
		this.free = free;
		this.lastHeartbeat = new Date().getTime();
	}

	public String getName() {
		return name;
	}

	public Position getPosition() {
		return position;
	}

	public boolean isDead() {

		long currentDate = new Date().getTime();

		if (currentDate > lastHeartbeat + R.MAX_SENSOR_DELAY) {
			return true;
		}

		return false;
	}

	public long remainingTime(){
		
		long currentDate = new Date().getTime();
		
		return (currentDate - lastHeartbeat);
		
	}
	
	public boolean isAlive() {
		return !isDead();
	}

	public boolean isFree() {
		return this.free;
	}

	public void updateState(boolean free) {
		this.free=free;
		this.lastHeartbeat = new Date().getTime();
		
	}

	@Override
	public InfoType getType() {
		return InfoType.SENSOR;
	}
	
	
}
