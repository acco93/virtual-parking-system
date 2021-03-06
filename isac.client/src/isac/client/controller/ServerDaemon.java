package isac.client.controller;

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

import isac.client.model.Storage;
import isac.core.log.Logger;
import isac.core.sharedknowledge.R;

/**
 * It receives pings from the server when the latter is correctly working and
 * updates a variables that maintains the last reception time.
 * 
 * @author acco
 *
 */
public class ServerDaemon {

	private Channel channel;
	private String queueName;
	private String momIp;

	public ServerDaemon(String momIp) {
		this.momIp = momIp;
		Logger.getInstance().info("started");
		this.setupRabbitMQ();
		this.requestParkInfo();
	}

	private void requestParkInfo() {
		try {

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(momIp);
			Connection connection;
			connection = factory.newConnection();
			Channel requestChannel = connection.createChannel();
			requestChannel.queueDeclare(R.CLIENT_TO_SERVER_QUEUE, false, false, false, null);
			requestChannel.basicPublish("", R.CLIENT_TO_SERVER_QUEUE, null, "1".getBytes());
		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setupRabbitMQ() {

		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(momIp);
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
				Storage.getInstance().setServerHeartbeat(time);

			}

		};

		// Register to it
		try {
			channel.basicConsume(this.queueName, true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
