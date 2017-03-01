package acco.isac.client;

import acco.isac.client.ui.ClientUserInterface;
import acco.isac.core.ActiveEntity;

public class Client extends ActiveEntity {

	private ClientUserInterface ui;
	private ServerDaemon serverDaemon;
	private SubscriberDaemon subscriberDaemon;

	public Client() {
		this.ui = new ClientUserInterface();
		this.serverDaemon = new ServerDaemon();
		this.subscriberDaemon = new SubscriberDaemon();
	}

	@Override
	protected void work() {

		this.serverDaemon.start();
		this.subscriberDaemon.start();
	}

}
