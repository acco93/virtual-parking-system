package isac.client.controller;

import java.io.IOException;
import java.util.UUID;
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

import isac.client.model.Storage;
import isac.client.view.UserInterface;
import isac.core.data.Position;
import isac.core.log.Logger;
import isac.core.message.LocalReply;
import isac.core.message.LocalRequest;
import isac.core.message.LocalRequestType;
import isac.core.sharedknowledge.R;

public class LocalInteraction {

	private UserInterface ui;
	private Channel channel;
	private String privateReplyChannel;

	public LocalInteraction(UserInterface ui) {

		this.ui = ui;

		this.privateReplyChannel = UUID.randomUUID().toString();

		this.setupRabbitMQ();
	}

	private void setupRabbitMQ() {

		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare(R.LOCAL_INTERACTIONS_CHANNEL, "fanout");

			channel.queueDeclare(this.privateReplyChannel, false, false, false, null);

		} catch (IOException | TimeoutException e) {
			Logger.getInstance().error(e.getMessage());
		}

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {

				String message = new String(body, "UTF-8");

				Gson gson = new GsonBuilder().create();
				LocalReply reply = gson.fromJson(message, LocalReply.class);

				ui.setAirDistanceString("" + reply.getHops());
				ui.setNearestParkPositionString("" + reply.getDestination());

				String airPath = computeAirPath(Storage.getInstance().getUserPosition(), reply.getDestination());

				ui.setAirPath("Estimated air path: " + airPath);
			}

			private String computeAirPath(Position source, Position destination) {

				boolean toRight = false;
				boolean toBottom = false;

				int xSteps = source.getColumn() - destination.getColumn();
				int ySteps = source.getRow() - destination.getRow();

				if (xSteps < 0) {
					toRight = true;
				}

				if (ySteps < 0) {
					toBottom = true;
				}

				String path = "";

				for (int i = 0; i < Math.abs(xSteps); i++) {
					if (toRight) {
						path += "right ";
					} else {
						path += "left ";
					}
				}

				for (int i = 0; i < Math.abs(ySteps); i++) {
					if (toBottom) {
						path += "bottom ";
					} else {
						path += "up ";
					}
				}

				if(xSteps+ySteps ==0){
					path+="in place";
				}
				
				return path;
			}

		};

		/*
		 * Replies will be received in this private channel
		 */
		try {
			channel.basicConsume(this.privateReplyChannel, true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void searchNearFreePark() {
		System.out.println("Trying to contact the sensors ...");
		this.generalRequest(LocalRequestType.PARK);
	}

	public void searchParkedCar() {
		this.generalRequest(LocalRequestType.LOCATE);
	}

	/**
	 * Contact the nearest sensor, if one is available.
	 * 
	 * @param type
	 *            request type
	 */
	private void generalRequest(LocalRequestType type) {
		LocalRequest request = new LocalRequest(type, Storage.getInstance().getUserPosition(),
				this.privateReplyChannel);

		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(request);
		try {
			channel.basicPublish(R.LOCAL_INTERACTIONS_CHANNEL, "", null, json.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
