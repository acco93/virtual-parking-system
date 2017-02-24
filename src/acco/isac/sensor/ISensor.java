package acco.isac.sensor;

import acco.isac.environment.Position;

/**
 * 
 * A generic sensor interface.
 * 
 * @author acco
 *
 */
public interface ISensor {
	
	/**
	 * 
	 */
	Position getPosition();
	
	/**
	 * Sense action.
	 */
	void sense();

}
