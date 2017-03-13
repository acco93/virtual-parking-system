package isac.environment.sensor;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import isac.core.log.Logger;
import isac.core.message.SensorMessage;
import isac.core.sharedknowledge.R;
import isac.environment.Environment;
import isac.environment.resources.ER;

/**
 * 
 * AbstractSensorController extension to handle a parking sensor.
 * 
 * @author acco
 *
 */
public class ParkingSensorController extends AbstractSensorController {

	/*
	 * The component that is able to sense.
	 */
	private ParkingSensor sensor;
	/*
	 * Thread that manage the interaction with other sensors and clients.
	 */
	private LocalInteractionHandler localInteractionHandler;
	/*
	 * The sensor to server channel.
	 */
	private Channel channel;

	public ParkingSensorController(ParkingSensor sensor) {

		this.sensor = sensor;
		this.sensor.setController(this);

		/*
		 * Server interaction
		 */
		this.rabbitMQSetup();

		/*
		 * Neighbors interaction
		 */
		this.localInteractionHandler = new LocalInteractionHandler(this.sensor);
		this.localInteractionHandler.start();
	}

	@Override
	protected Object sense() {
		/*
		 * Retrieve the value from the sensor.
		 */
		return this.sensor.sense();
	}

	@Override
	protected byte[] process(Object busy) {

		boolean freePlace = !(boolean) busy;

		SensorMessage msg = new SensorMessage(this.sensor.getId(), this.sensor.getPosition(), freePlace);
		/*
		 * Create the json message.
		 */
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(msg);

		return json.getBytes();
	}

	private void rabbitMQSetup() {

		try {

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(ER.MOM_IP);
			Connection connection;
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(R.SENSOR_TO_SERVER_QUEUE, false, false, false, null);

		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void act(byte[] bytes) {
		{

			try {
				channel.basicPublish("", R.SENSOR_TO_SERVER_QUEUE, null, bytes);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@Override
	protected void disable() {
		Environment.getInstance().removeSensor(this.sensor);
		Logger.getInstance().error(
				"Sensor " + this.sensor.getId() + " in position " + this.sensor.getPosition() + " stopped working ...");
		this.localInteractionHandler.disable();

	}

}
