package isac.core.data;

import java.util.Date;

import isac.core.sharedknowledge.R;

/**
 * 
 * A simple server-side sensor representation.
 * 
 * @author acco
 *
 */
public class SensorRepresentation implements EnvironmentInfo {

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

	public long remainingTime() {

		long currentDate = new Date().getTime();

		return (currentDate - lastHeartbeat);

	}

	public boolean isAlive() {
		return !isDead();
	}

	public boolean isFree() {
		return this.free;
	}

	public boolean updateState(boolean free) {
		boolean changed = false;
		if (this.free != free) {
			changed = true;
		}
		this.free = free;
		this.lastHeartbeat = new Date().getTime();
		return changed;
	}

	@Override
	public InfoType getType() {
		return InfoType.SENSOR;
	}

}
