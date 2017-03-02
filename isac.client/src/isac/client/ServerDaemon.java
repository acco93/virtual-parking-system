package isac.client;

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

import isac.core.log.ILogger;
import isac.core.log.Logger;
import isac.core.sharedknowledge.R;

public class ServerDaemon extends Thread {

	private Channel channel;
	private ILogger gLog;

	public ServerDaemon(ILogger gLog) {
		Logger.getInstance().info("started");
		this.gLog = gLog;
		this.setupRabbitMQ();
	}

	@Override
	public void run() {

	}

	private void setupRabbitMQ() {

		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection;
			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.queueDeclare(R.CLIENT_TO_SERVER_QUEUE, false, false, false, null);

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
				//SensorMessage msg = gson.fromJson(message, SensorMessage.class);


			}

		};

		// Register to it
		try {
			channel.basicConsume(R.CLIENT_TO_SERVER_QUEUE, true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Logger.getInstance().info("waiting for sensor messages ...");
		
	}
	
}
