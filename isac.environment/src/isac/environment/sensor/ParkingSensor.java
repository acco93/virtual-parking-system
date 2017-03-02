package isac.environment.sensor;

import isac.core.data.Position;
import isac.environment.Environment;
import isac.environment.IEnvironmentElement;

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
	private ParkingSensorController sensorController;

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

	public void setController(ParkingSensorController controller) {
		this.sensorController = controller;

	}

	public void wake() {
		this.sensorController.wake();
	}

}
