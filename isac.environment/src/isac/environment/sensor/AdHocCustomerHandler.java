package isac.environment.sensor;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import isac.core.message.InternalReply;
import isac.core.message.LocalReply;

public class AdHocCustomerHandler {

	private Channel channel;
	private String replyChannelName;

	public AdHocCustomerHandler(String replyChannelName) {

		this.replyChannelName = replyChannelName;

		this.setupRabbitMQ();

	}

	private void setupRabbitMQ() {
		try {

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection;
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(this.replyChannelName, false, false, false, null);

		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void send(InternalReply iReply) {

		LocalReply reply = this.translateReply(iReply);

		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(reply);
		try {
			channel.basicPublish("", this.replyChannelName, null, json.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private LocalReply translateReply(InternalReply iReply) {

		LocalReply reply = new LocalReply(iReply);

		return reply;
	}

}
