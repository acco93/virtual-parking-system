package acco.isac.server;

import acco.isac.core.ActiveEntity;
import acco.isac.log.Logger;
import acco.isac.server.ui.ServerUserInterface;

/**
 * 
 * The server knows everything but may be offline!
 * 
 * @author acco
 *
 */
public class Server extends ActiveEntity {

	private SensorHandler sensorsHandler;
	private RequestsHandler requestsHandler;
	private ServerUserInterface ui;
	private PublisherDaemon publisherDaemon;

	public Server() {

		Logger.getInstance().info("started");

		this.sensorsHandler = new SensorHandler();
		this.requestsHandler = new RequestsHandler();
		this.publisherDaemon = new PublisherDaemon();
		this.ui = new ServerUserInterface();
	}

	@Override
	protected void work() {
		this.sensorsHandler.start();
		this.requestsHandler.start();
		this.publisherDaemon.start();
	}

}
