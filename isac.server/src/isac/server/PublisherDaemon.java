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

public class PublisherDaemon extends Thread {

	private static final int EVENT_SEMAPHORE = 0;
	private Channel channel;
	private Semaphore semaphore;

	public PublisherDaemon() {

		Logger.getInstance().info("started");

		semaphore = new Semaphore(EVENT_SEMAPHORE);

		this.setupMqtt();
	}

	private void setupMqtt() {

		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare(R.EXCHANGE_NAME, "fanout");
		} catch (IOException | TimeoutException e) {
			Logger.getInstance().error(e.getMessage());
		}

	}

	@Override
	public void run() {

		while (true) {

			try {
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

			channel.basicPublish(R.EXCHANGE_NAME, "", null, json.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void notifyClients() {
		this.semaphore.release();
	}

}
