package acco.isac.environment;

import java.util.Optional;

import acco.isac.sensor.ISensor;

/**
 * 
 * A cell is an element of the grid-world-environment. It may contain a
 * component or not.
 * 
 * 
 * @author acco
 *
 */
public class Cell {

	private Optional<ISensor> sensor;

	/**
	 * Create an empty cell.
	 */
	public Cell() {
		this.sensor = Optional.empty();
	}

	/**
	 * 
	 * Inject the component if the cell is not already occupied.
	 * 
	 * @param newSensor
	 *            the component to inject.
	 * @return true if successfully injected, false otherwise.
	 */
	public boolean inject(ISensor newSensor) {

		if (this.sensor.isPresent()) {
			return false;
		} else {
			this.sensor = Optional.of(newSensor);
			return true;
		}

	}
	
	public boolean isEmpty(){
		return !this.sensor.isPresent();
	}

	@Override
	public String toString() {
		return "Cell [component=" + sensor + "]";
	}
	
	

}
