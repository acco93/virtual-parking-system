package acco.isac.sharedknowledge;

/**
 * 
 * Shared resources publicly accessible.
 * 
 * @author acco
 *
 */
public class R {

	/**
	 * Environment width.
	 */
	public static int ENV_WIDTH = 14;
	
	/**
	 * Environment height.
	 */
	public static int ENV_HEIGHT = 10;
	
	/**
	 * Parking server queue name.
	 */
	public final static String QUEUE_NAME = "parkingServer";

	/**
	 * Max reception delay (in ms) after which the server consider the sensor dead.
	 */
	public static final long MAX_SENSOR_DELAY = 5000;
	
}
