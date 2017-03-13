package isac.server;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import isac.core.log.Logger;
import isac.core.sharedknowledge.R;

/**
 * 
 * Send sensors info to the client.
 * 
 * @author acco
 *
 */
public class PublisherDaemon extends Thread {

	private static final int EVENT_SEMAPHORE = 0;
	private Channel channel;
	private Semaphore semaphore;
	private String momIp;

	public PublisherDaemon(String momIp) {

		this.momIp = momIp;
		Logger.getInstance().info("started");

		this.semaphore = new Semaphore(EVENT_SEMAPHORE);

		this.setupMqtt();
	}

	private void setupMqtt() {

		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(momIp);
			Connection connection = factory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare(R.SERVER_TO_CLIENTS_CHANNEL, "fanout");
		} catch (IOException | TimeoutException e) {
			Logger.getInstance().error(e.getMessage());
		}

	}

	@Override
	public void run() {

		while (true) {

			try {

				/*
				 * Send update message just when necessary
				 */
				semaphore.acquire();

			} catch (InterruptedException e) {
				Logger.getInstance().error(e.getMessage());
			}

			this.publish();

		}

	}

	private void publish() {
		try {

			Gson gson = new GsonBuilder().create();

			String json = gson.toJson(Storage.getInstance().getSensors());

			channel.basicPublish(R.SERVER_TO_CLIENTS_CHANNEL, "", null, json.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void notifyClients() {
		this.semaphore.release();
	}

}
