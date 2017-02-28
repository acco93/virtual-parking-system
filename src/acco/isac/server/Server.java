package acco.isac.server;

import acco.isac.log.Logger;
import acco.isac.server.ui.ServerUserInterface;

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
	private ServerUserInterface ui;
	
	public Server() {

		Logger.getInstance().info("started");
		
		sensorsHandler = new SensorHandler();
		requestsHandler = new RequestsHandler();
		this.ui = new ServerUserInterface();
	}

	public void start() {

		this.sensorsHandler.start();
		this.requestsHandler.start();

	}

}
