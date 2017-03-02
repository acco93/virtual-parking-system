package isac.environment;

import java.util.Optional;

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
	
	public boolean isEmpty(){
		return !this.element.isPresent();
	}

	@Override
	public String toString() {
		return "Cell [component=" + element + "]";
	}
	
	

}
