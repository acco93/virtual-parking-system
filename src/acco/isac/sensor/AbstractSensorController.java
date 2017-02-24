package acco.isac.sensor;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import acco.isac.sharedknowledge.R;

public abstract class AbstractSensorController extends Thread {

	private int delayTime;
	private Channel channel;

	public AbstractSensorController(int delayTime) {
		this.delayTime = delayTime;

		// setup rabbitmq
		this.rabbitMQSetup();
	}

	@Override
	public void run() {

		while (true) {

			Object value = this.sense();

			byte[] processedValue = this.process(value);

			this.send(processedValue);

			this.delay();

		}

	}

	protected void send(byte[] bytes) {

		try {
			channel.basicPublish("", R.QUEUE_NAME, null, bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Sending: " + bytes);

	}

	protected abstract byte[] process(Object value);

	protected abstract Object sense();

	private void delay() {
		try {
			Thread.sleep(this.delayTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void rabbitMQSetup() {

		try {

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection;
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(R.QUEUE_NAME, false, false, false, null);

		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
