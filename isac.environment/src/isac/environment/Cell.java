package isac.environment;

import java.util.Optional;

import isac.environment.sensor.ParkingSensor;

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

	private Optional<IEnvironmentElement> element;

	/**
	 * Create an empty cell.
	 */
	public Cell() {
		this.element = Optional.empty();
	}

	/**
	 * 
	 * Inject the component if the cell is not already occupied.
	 * 
	 * @param newElement
	 *            the component to inject.
	 * @return true if successfully injected, false otherwise.
	 */
	public boolean inject(IEnvironmentElement newElement) {

		if (this.element.isPresent()) {
			return false;
		} else {
			this.element = Optional.of(newElement);
			return true;
		}

	}

	/**
	 * Return if there is something in the cell
	 * 
	 * @return true, if not empty
	 */
	public boolean isEmpty() {
		return !this.element.isPresent();
	}

	/**
	 * Return the element.
	 * 
	 * @return the element
	 */
	public Optional<IEnvironmentElement> getElement() {
		return this.element;
	}

	@Override
	public String toString() {
		return "Cell [component=" + element + "]";
	}

}
