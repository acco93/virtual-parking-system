package isac.server;

import isac.core.constructs.ActiveEntity;
import isac.core.log.Logger;
import isac.server.ui.ServerUserInterface;

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
	private PublisherDaemon publisherDaemon;
	private OnlineDaemon onlineDaemon;

	public Server() {

		Logger.getInstance().info("started");

		this.onlineDaemon = new OnlineDaemon();
		this.publisherDaemon = new PublisherDaemon();
		this.sensorsHandler = new SensorHandler(this.publisherDaemon);
		this.requestsHandler = new RequestsHandler(this.publisherDaemon);

		@SuppressWarnings("unused")
		ServerUserInterface ui = new ServerUserInterface();
	}

	@Override
	protected void work() {
		this.onlineDaemon.start();
		this.sensorsHandler.start();
		this.requestsHandler.start();
		this.publisherDaemon.start();
	}

}
