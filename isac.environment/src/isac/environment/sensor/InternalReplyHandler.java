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
import isac.core.message.InternalReply;
import isac.core.sharedknowledge.R;

/**
 * 
 * Daemon that handles internal replies in interactions with neighbors.
 * 
 * @author acco
 *
 */
public class InternalReplyHandler extends EventLoop<InternalReply> {

	/*
	 * The message processor.
	 */
	private LocalInteractionProcessor localInteractionProcessor;
	/*
	 * The channel where are published internal replies.
	 */
	private Channel channel;
	/*
	 * The queue where are published internal replies.
	 */
	private String queueName;

	public InternalReplyHandler(LocalInteractionProcessor localInteractionProcessor) {
		this.localInteractionProcessor = localInteractionProcessor;
		this.setupRabbitMQ();
	}

	/**
	 * Setup some RabbitMQ settings ...
	 */
	private void setupRabbitMQ() {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection;
			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.exchangeDeclare(R.INTERNAL_REPLIES_CHANNEL, "fanout");
			queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, R.INTERNAL_REPLIES_CHANNEL, "");
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");

				Gson gson = new GsonBuilder().create();
				InternalReply iReply = gson.fromJson(message, InternalReply.class);
				/*
				 * Process one reply at a time
				 */
				append(iReply);
			}

		};

		try {
			channel.basicConsume(this.queueName, true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void process(InternalReply iReply) {
		/*
		 * Forward the reply to the processor.
		 */
		this.localInteractionProcessor.process(iReply);
	}

	/**
	 * Publish the internal reply to the internal replies channel.
	 * 
	 * @param iReply
	 *            the reply
	 */
	public void spread(InternalReply iReply) {
		try {

			Gson gson = new GsonBuilder().create();
			String json = gson.toJson(iReply);
			channel.basicPublish(R.INTERNAL_REPLIES_CHANNEL, "", null, json.getBytes());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
