package isac.client;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

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

public class ServerDaemon {

	private Channel channel;
	private ILogger gLog;
	private String queueName;

	public ServerDaemon(ILogger gLog) {
		Logger.getInstance().info("started");
		this.gLog = gLog;
		this.setupRabbitMQ();
		this.requestParkInfo();
	}

	private void requestParkInfo() {
		try {

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection;
			connection = factory.newConnection();
			Channel requestChannel = connection.createChannel();
			requestChannel.queueDeclare(R.CLIENT_TO_SERVER_QUEUE, false, false, false, null);
			channel.basicPublish("", R.CLIENT_TO_SERVER_QUEUE, null, "1".getBytes());
		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setupRabbitMQ() {

		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection;
			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.exchangeDeclare(R.SERVER_HEARTBEAT_CHANNEL, "fanout");
			queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, R.SERVER_HEARTBEAT_CHANNEL, "");
		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void start() {
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {

				long time = new Date().getTime();
				ClientStorage.getInstance().setLastServerHeartbeat(time);
				gLog.error("qua");
			}

		};

		// Register to it
		try {
			channel.basicConsume(this.queueName, true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Logger.getInstance().info("waiting for server heartbeat ...");
	}

}
