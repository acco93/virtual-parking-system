package isac.client.controller;

import java.util.Date;

import isac.client.model.Storage;
import isac.core.log.ILogger;

public class ServerChecker extends Thread {

	private static final int MAX_SERVER_DELAY = 1500;
	private boolean onLocalInteractions;
	private Client client;
	private ILogger gLog;

	public ServerChecker(Client client, ILogger gLog) {
		this.client = client;
		this.gLog = gLog;
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
