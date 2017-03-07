package isac.server;

import java.io.IOException;
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

import isac.core.log.Logger;
import isac.core.message.SensorMessage;
import isac.core.sharedknowledge.R;

/**
 * 
 * It receives messages from the sensors.
 * 
 * @author acco
 *
 */
public class SensorHandler {

	private Channel channel;
	private SensorMessageProcessor msgProcessor;

	public SensorHandler(PublisherDaemon publisherDaemon) {

		Logger.getInstance().info("started");

		this.msgProcessor = new SensorMessageProcessor(publisherDaemon);
		this.msgProcessor.start();

		this.setupRabbitMQ();

	}

	private void setupRabbitMQ() {

		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection;
			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.queueDeclare(R.SENSOR_TO_SERVER_QUEUE, false, false, false, null);

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

				msgProcessor.appendSensorMessage(msg);

			}

		};

		// Register to it
		try {
			channel.basicConsume(R.SENSOR_TO_SERVER_QUEUE, true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Logger.getInstance().info("waiting for sensor messages ...");

	}

}
