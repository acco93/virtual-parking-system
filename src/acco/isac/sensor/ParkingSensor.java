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
	 * @param row
	 *            row-position in a 2d grid-world
	 * @param column
	 *            column-position in a 2d grid-world
	 */
	public ParkingSensor(int row, int column, String uid) {
		this.position = new Position(row, column);
		this.uid = uid;
	}

	@Override
	public Boolean sense() {
		return Environment.getInstance().getCarsLayer()[this.position.getRow()][this.position.getColumn()];
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
