package isac.client;

import isac.client.ui.ClientUserInterface;
import isac.core.constructs.ActiveEntity;
import isac.core.log.ILogger;

public class Client extends ActiveEntity {

	private ClientUserInterface ui;
	private ServerDaemon serverDaemon;
	private SubscriberDaemon subscriberDaemon;

	public Client() {
		this.ui = new ClientUserInterface();
		ILogger gLog = this.ui.getGraphicalLogger();
		this.serverDaemon = new ServerDaemon(gLog);
		this.subscriberDaemon = new SubscriberDaemon(gLog);
	}

	@Override
	protected void work() {

		this.serverDaemon.start();
		this.subscriberDaemon.start();
	}

}
