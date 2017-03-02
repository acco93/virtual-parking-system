package isac.server;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import isac.core.constructs.EventLoop;
import isac.core.log.Logger;
import isac.core.sharedknowledge.R;

public class RequestsHandler extends EventLoop<Boolean> {

	private PublisherDaemon publisherDaemon;
	private Channel channel;

	public RequestsHandler(PublisherDaemon publisherDaemon) {
		Logger.getInstance().info("started");
		this.publisherDaemon = publisherDaemon;

		this.setupRabbitMQ();
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
		
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {

				String message = new String(body, "UTF-8");

				

				append(true);

			}

		};

		// Register to it
		try {
			channel.basicConsume(R.CLIENT_TO_SERVER_QUEUE, true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Logger.getInstance().info("waiting for client messages ...");

		
		
	}


	@Override
	protected void process(Boolean event) {

		System.out.println("qua");
		this.publisherDaemon.notifyClients();

	}

}
