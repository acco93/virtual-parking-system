package acco.isac.sensor;

import acco.isac.environment.Environment;
import acco.isac.environment.IEnvironmentElement;
import acco.isac.environment.Position;

/**
 * 
 * A simple virtual parking sensor. Each sensor is deployed in a (x, y) position
 * of the grid.
 * 
 * @author acco
 *
 */
public class ParkingSensor implements ISensor<Boolean>, IEnvironmentElement {

	private Position position;
	private String uid;

	/**
	 * The constructor.
	 * 
	 * @param x
	 *            x-position in a 2d grid-world
	 * @param y
	 *            y-position in a 2d grid-world
	 */
	public ParkingSensor(int x, int y, String uid) {
		this.position = new Position(x, y);
		this.uid = uid;
	}

	@Override
	public Boolean sense() {
		return Environment.getInstance().getCarsLayer()[this.position.getX()][this.position.getY()];
	}

	@Override
	public Position getPosition() {
		return this.position;
	}

	@Override
	public String getId() {
		return this.uid;
	}

}
