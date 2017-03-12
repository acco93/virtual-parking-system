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
import isac.core.message.LocalRequest;
import isac.core.sharedknowledge.R;

/**
 * 
 * Daemon that receives local messages from clients. It is implemented as
 * EventLoop to process a message at a time.
 * 
 * @author acco
 *
 */
public class LocalInteractionHandler extends EventLoop<LocalRequest> {

	private Channel channel;
	private ParkingSensor sensor;
	private LocalInteractionProcessor localInteractionProcessor;
	private String queueName;

	public LocalInteractionHandler(ParkingSensor sensor) {
		this.sensor = sensor;
		/*
		 * Setup a local interaction processor. It will process messages
		 * received from clients and other sensors.
		 */
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
				/*
				 * Process a message at a time.
				 */
				append(request);
			}

		};

		// Register to it
		try {
			channel.basicConsume(this.queueName, true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void process(LocalRequest request) {

		Position sensorPosition = this.sensor.getPosition();
		Position requestPosition = request.getUserPosition();

		/*
		 * All sensors receives all (local) client messages since a
		 * publish-subscribe schema is used. To simulate local interactions
		 * discard messages that comes from too distant locations. Compute the
		 * manhattan distance since we are working in a grid.
		 */
		int manhattanDistance = Math.abs(sensorPosition.getRow() - requestPosition.getRow())
				+ Math.abs(sensorPosition.getColumn() - requestPosition.getColumn());

		if (manhattanDistance > 1) {
			/*
			 * Discarded
			 */
			return;
		}

		/*
		 * Received
		 */
		this.localInteractionProcessor.process(request);
	}

	public void disable() {
		super.disable();
		this.localInteractionProcessor.disable();

	}

}
