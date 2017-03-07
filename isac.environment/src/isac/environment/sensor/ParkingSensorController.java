package isac.environment.sensor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import isac.core.message.SensorMessage;

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

	public ParkingSensorController(ParkingSensor sensor) {

		this.sensor = sensor;
		this.sensor.setController(this);

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

}
