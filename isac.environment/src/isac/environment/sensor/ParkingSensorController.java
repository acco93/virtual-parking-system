package isac.environment.sensor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import isac.core.data.SensorMessage;

public class ParkingSensorController extends AbstractSensorController {

	private ParkingSensor sensor;

	public ParkingSensorController(ParkingSensor sensor) {
		this.sensor = sensor;
		this.sensor.setController(this);
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
