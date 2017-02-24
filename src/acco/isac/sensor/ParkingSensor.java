package acco.isac.sensor;

import acco.isac.environment.Position;

/**
 * 
 * A simple virtual parking sensor.
 * 
 * @author acco
 *
 */
public class ParkingSensor implements ISensor {

	private Position position;

	/**
	 * The constructor.
	 * 
	 * @param x
	 *            x-position in a 2d grid-world
	 * @param y
	 *            y-position in a 2d grid-world
	 */
	public ParkingSensor(int x, int y) {
		this.position = new Position(x, y);
	}

	@Override
	public void sense() {

	}

	@Override
	public Position getPosition() {
		return this.position;
	}

}
