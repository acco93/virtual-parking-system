package isac.environment;

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

import isac.core.constructs.EventLoop;
import isac.core.data.EnvironmentMessage;
import isac.core.log.Logger;
import isac.core.sharedknowledge.R;

public class EnvironmentDaemon extends EventLoop<EnvironmentMessage> {

	private Channel channel;

	public EnvironmentDaemon() {
		this.setupRabbitMQ();
	}

	private void setupRabbitMQ() {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection;
			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.queueDeclare(R.ENVIRONMENT_CHANNEL, false, false, false, null);

		} catch (IOException | TimeoutException e) {

			e.printStackTrace();
		}

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {

				String message = new String(body, "UTF-8");
				Gson gson = new GsonBuilder().create();
				EnvironmentMessage envMessage = gson.fromJson(message, EnvironmentMessage.class);
				append(envMessage);
			}

		};

		// Register to it
		try {
			channel.basicConsume(R.ENVIRONMENT_CHANNEL, true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Logger.getInstance().info("waiting for client messages ...");

	}

	@Override
	protected void process(EnvironmentMessage message) {

		if (message.isPark()) {
			Environment.getInstance().setCar(message.getPosition());
		} else {
			Environment.getInstance().removeCar(message.getPosition());
		}

	}

}
