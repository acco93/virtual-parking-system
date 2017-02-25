package acco.isac.server;

/**
 * 
 * The server knows everything but may be offline!
 * 
 * @author acco
 *
 */
public class Server {

	private SensorHandler sensorsHandler;
	private RequestsHandler requestsHandler;

	public Server() {

		sensorsHandler = new SensorHandler();
		requestsHandler = new RequestsHandler();
	}

	public void start() {

		this.sensorsHandler.start();
		this.requestsHandler.start();

	}

}
