package acco.isac.server;

import acco.isac.server.ui.UserInterface;

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
	private UserInterface ui;
	
	public Server() {

		sensorsHandler = new SensorHandler();
		requestsHandler = new RequestsHandler();
		this.ui = new UserInterface();
	}

	public void start() {

		this.sensorsHandler.start();
		this.requestsHandler.start();

	}

}
