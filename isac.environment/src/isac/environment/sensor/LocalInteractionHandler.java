package isac.environment.sensor;

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
import isac.core.data.Position;
import isac.core.log.Logger;
import isac.core.message.LocalRequest;
import isac.core.sharedknowledge.R;

public class LocalInteractionHandler extends EventLoop<LocalRequest> {

	private Channel channel;
	private ParkingSensor sensor;
	private LocalInteractionProcessor localInteractionProcessor;
	private String queueName;

	public LocalInteractionHandler(ParkingSensor sensor) {
		this.sensor = sensor;
		this.localInteractionProcessor = new LocalInteractionProcessor(this.sensor);
		this.setupRabbitMQ();
	}

	private void setupRabbitMQ() {

		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection;
			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.exchangeDeclare(R.LOCAL_INTERACTIONS_CHANNEL, "fanout");
			queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, R.LOCAL_INTERACTIONS_CHANNEL, "");
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {

				String message = new String(body, "UTF-8");
				Gson gson = new GsonBuilder().create();
				LocalRequest request = gson.fromJson(message, LocalRequest.class);
				append(request);
			}

		};

		// Register to it
		try {
			channel.basicConsume(this.queueName, true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Logger.getInstance().info("waiting for client messages ...");

	}

	@Override
	protected void process(LocalRequest request) {

		Position sensorPosition = this.sensor.getPosition();
		Position requestPosition = request.getPosition();
		// discard too distante requests
		int manhattanDistance = Math.abs(sensorPosition.getRow() - requestPosition.getRow())
				+ Math.abs(sensorPosition.getColumn() - requestPosition.getColumn());

		if (manhattanDistance > 1) {
			return;
		}

		System.out.println("Request received from sensor " + this.sensor.getId());
		this.localInteractionProcessor.process(request);

	}

}
