package isac.core.sharedknowledge;

/**
 * 
 * Shared resources publicly accessible.
 * 
 * @author acco
 *
 */
public class R {

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
	public static final String CLIENT_TO_SERVER_QUEUE = "clientRequestsQueue";

	/**
	 * Server to clients channel where the servers publishes updated world
	 * versions.
	 */
	public static final String SERVER_TO_CLIENTS_CHANNEL = "serverToClientsChannel";

	/**
	 * Positive infinity.
	 */
	public static final int INF = 100000;

	/**
	 * Channel name where the server continuously sends bit indicating that it's
	 * alive.
	 */
	public static final String SERVER_HEARTBEAT_CHANNEL = "serverHeartbeatChannel";

	/**
	 * Clients to environment channel.
	 */
	public static final String ENVIRONMENT_CHANNEL = "environmentChannel";

	/**
	 * Client to sensors channel.
	 */
	public static final String LOCAL_INTERACTIONS_CHANNEL = "localInteractionsChannel";

	/**
	 * Sensor to sensors internal requests channel.
	 */
	public static final String INTERNAL_REQUESTS_CHANNEL = "internalRequestsChannel";

	/**
	 * Sensor to sensors internal replies channel.
	 */
	public static final String INTERNAL_REPLIES_CHANNEL = "internalRepliesChannel";

}
