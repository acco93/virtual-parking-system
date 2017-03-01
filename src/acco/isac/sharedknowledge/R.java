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
	public static int ENV_COLUMNS = 0;

	/**
	 * Environment height.
	 */
	public static int ENV_ROWS = 0;

	/**
	 * Parking server queue name.
	 */
	public final static String SENSOR_TO_SERVER_QUEUE = "sensorsToServer";

	/**
	 * Max reception delay (in ms) after which the server consider the sensor
	 * dead.
	 */
	public static final int MAX_SENSOR_DELAY = 10000;

	/**
	 * Parking server queue name.
	 */
	public static final String CLIENT_TO_SERVER_QUEUE = "customerToServer";

	public static final String EXCHANGE_NAME = "serverToClientExchange";

	public static final int INF = 100000;

}
