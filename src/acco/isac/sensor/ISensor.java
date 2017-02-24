package acco.isac.sensor;

/**
 * 
 * A generic sensor interface.
 * 
 * @author acco
 * @param <T>
 *
 */
public interface ISensor<T> {

	/**
	 * Retrieve the sensor unique id.
	 * @return the uid.
	 */
	String getId();
	
	/**
	 * Sense action.
	 * @return the sensed information.
	 */
	T sense();

}
