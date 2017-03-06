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

public class InternalReplyHandler extends EventLoop<InternalReply> {

	private LocalInteractionProcessor localInteractionProcessor;
	private Channel channel;
	private String queueName;

	public InternalReplyHandler(LocalInteractionProcessor localInteractionProcessor) {
		this.localInteractionProcessor = localInteractionProcessor;
		this.setupRabbitMQ();
	}

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
				append(iReply);
			}

		};

		try {
			channel.basicConsume(this.queueName, true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void spread(InternalReply iReply) {
		try {

			Gson gson = new GsonBuilder().create();

			String json = gson.toJson(iReply);

			channel.basicPublish(R.INTERNAL_REPLIES_CHANNEL, "", null, json.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void process(InternalReply iReply) {
		this.localInteractionProcessor.process(iReply);
	}

}
