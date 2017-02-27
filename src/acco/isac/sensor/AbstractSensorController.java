package acco.isac.sensor;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import acco.isac.sharedknowledge.R;

public abstract class AbstractSensorController extends Thread {

	private Channel channel;
	private Random random;
	private double serviceDisruptionProbability;
	private boolean working;

	public AbstractSensorController() {
		this.random = new Random();
		this.serviceDisruptionProbability = 0.001;
		this.working = true;
		// setup rabbitmq
		this.rabbitMQSetup();
	}

	@Override
	public void run() {

		while (working) {

			this.delay();

			Object value = this.sense();

			byte[] processedValue = this.process(value);

			this.send(processedValue);

			if (random.nextDouble() <= this.serviceDisruptionProbability) {
				this.working = false;
			}

		}

	}

	protected void send(byte[] bytes) {

		try {
			channel.basicPublish("", R.SENSOR_TO_SERVER_QUEUE, null, bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected abstract byte[] process(Object value);

	protected abstract Object sense();

	private void delay() {

		int randomDelay = random.nextInt(R.MAX_SENSOR_DELAY);

		try {
			Thread.sleep(randomDelay);
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
			channel.queueDeclare(R.SENSOR_TO_SERVER_QUEUE, false, false, false, null);

		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
