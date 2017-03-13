package isac.client.controller;

import java.util.Date;

import isac.client.model.Storage;
import isac.core.log.Logger;

/**
 * It periodically checks the amount of time between the last receipt server
 * ping and the current time.
 * 
 * @author acco
 *
 */
public class ServerChecker extends Thread {

	private static final int MAX_SERVER_DELAY = 1500;
	private Client client;
	private Storage storage;

	public ServerChecker(Client client) {
		this.client = client;
		this.storage = Storage.getInstance();
	}

	@Override
	public void run() {

		Logger.getInstance().info("started");

		while (true) {

			long time = Storage.getInstance().getServerHeartbeat();

			long currentTime = new Date().getTime();

			if (currentTime - time > MAX_SERVER_DELAY) {
				this.storage.setServerOn(false);
				this.client.setStatus("Server unreachable");

			} else {
				this.storage.setServerOn(true);
				this.client.setStatus("Server ON");

			}

			this.client.repaintMap();

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

}
