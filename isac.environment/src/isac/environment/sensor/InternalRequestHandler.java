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
import isac.core.message.InternalRequest;
import isac.core.sharedknowledge.R;

public class InternalRequestHandler extends EventLoop<InternalRequest> {

	private LocalInteractionProcessor localInteractionProcessor;
	private Channel channel;
	private String queueName;

	public InternalRequestHandler(LocalInteractionProcessor localInteractionProcessor) {
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

			channel.exchangeDeclare(R.INTERNAL_REQUESTS_CHANNEL, "fanout");
			queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, R.INTERNAL_REQUESTS_CHANNEL, "");
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				
				String message = new String(body, "UTF-8");
				Gson gson = new GsonBuilder().create();
				InternalRequest iRequest = gson.fromJson(message, InternalRequest.class);
				append(iRequest);
				
			}

		};

		try {
			channel.basicConsume(this.queueName, true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void process(InternalRequest iRequest) {
		System.out.println("qua");
		this.localInteractionProcessor.process(iRequest);
	}

	public void spread(InternalRequest iRequest) {
		
		System.out.println("Spreading ...");
		
		try {

			Gson gson = new GsonBuilder().create();

			String json = gson.toJson(iRequest);

			channel.basicPublish(R.INTERNAL_REQUESTS_CHANNEL, "", null, json.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
