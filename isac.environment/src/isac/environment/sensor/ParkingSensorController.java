package isac.environment.sensor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import isac.core.message.SensorMessage;

public class ParkingSensorController extends AbstractSensorController {

	private ParkingSensor sensor;
	private LocalInteractionHandler localInteractionHandler;

	public ParkingSensorController(ParkingSensor sensor) {
		this.sensor = sensor;
		this.sensor.setController(this);
		this.localInteractionHandler = new LocalInteractionHandler(this.sensor);
		this.localInteractionHandler.start();

	}

	@Override
	protected Object sense() {
		// retrieve the value from the sensor
		return this.sensor.sense();
	}

	@Override
	protected byte[] process(Object busy) {
		// create a json message

		boolean booleanValue = !(boolean) busy;

		SensorMessage msg = new SensorMessage(this.sensor.getId(), this.sensor.getPosition(), booleanValue);
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(msg);

		return json.getBytes();
	}

}
