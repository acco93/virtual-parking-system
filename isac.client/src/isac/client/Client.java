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
		this.subscriberDaemon = new SubscriberDaemon(gLog);
		this.serverDaemon = new ServerDaemon(gLog);

	}

	@Override
	protected void work() {
		this.subscriberDaemon.start();
		this.serverDaemon.start();

	}

}
