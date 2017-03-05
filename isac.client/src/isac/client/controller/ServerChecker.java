package isac.client.controller;

import java.util.Date;

import isac.client.model.Storage;

public class ServerChecker extends Thread {

	private static final int MAX_SERVER_DELAY = 1500;
	private boolean onLocalInteractions;
	private Client client;

	public ServerChecker(Client client) {
		this.client = client;
	}

	@Override
	public void run() {

		while (true) {

			long time = Storage.getInstance().getServerHeartbeat();

			long currentTime = new Date().getTime();

			if (currentTime - time > MAX_SERVER_DELAY) {

				if (!onLocalInteractions) {
					onLocalInteractions = true;
					this.client.setStatus("Server unreachable");
				}

			} else {

				if (onLocalInteractions) {
					onLocalInteractions = false;
					this.client.setStatus("Server ON");
				}

			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

}
