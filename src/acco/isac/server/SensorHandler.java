package acco.isac.server;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import acco.isac.sensor.SensorMessage;
import acco.isac.sharedknowledge.R;
import acco.isac.ui.UserInterface;

public class SensorHandler {

	private Channel channel;
	private UserInterface ui;
	private ConcurrentHashMap<String, SensorRepresentation> storage;

	public SensorHandler(){
		ui = new UserInterface();

		this.setupRabbitMQ();

		storage = new ConcurrentHashMap<String, SensorRepresentation>();

	}

	private void setupRabbitMQ() {

		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection;
			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.queueDeclare(R.QUEUE_NAME, false, false, false, null);


		} catch (IOException | TimeoutException e) {

			e.printStackTrace();
		}

	}

	public void start() {

		// Define a consumer

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {

				String message = new String(body, "UTF-8");

				Gson gson = new GsonBuilder().create();
				SensorMessage msg = gson.fromJson(message, SensorMessage.class);

				SensorRepresentation rep = new SensorRepresentation(msg.getSensorId(), msg.getPosition(),
						msg.isFree());

				storage.put(msg.getSensorId(), rep);

				ui.updateSensors(storage);

			}
		};

		// Register to it
		try {
			channel.basicConsume(R.QUEUE_NAME, true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
