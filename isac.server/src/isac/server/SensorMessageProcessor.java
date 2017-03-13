package isac.server;

import java.util.concurrent.ConcurrentHashMap;

import isac.core.algorithms.GraphFromMap;
import isac.core.constructs.EventLoop;
import isac.core.data.Position;
import isac.core.data.SensorRepresentation;
import isac.core.datastructures.Graph;
import isac.core.message.SensorMessage;

/**
 * 
 * It translate sensor messages into useful aggregate information.
 * 
 * @author acco
 *
 */
public class SensorMessageProcessor extends EventLoop<SensorMessage> {

	private Storage storage;
	private PublisherDaemon publisherDaemon;

	public SensorMessageProcessor(PublisherDaemon publisherDaemon) {
		this.storage = Storage.getInstance();
		this.publisherDaemon = publisherDaemon;
	}

	/**
	 * Add the message to the processing queue. It will be process as soon as
	 * possible following a FIFO sematics.
	 * 
	 * @param msg
	 *            the sensor message
	 */
	public void appendSensorMessage(SensorMessage msg) {
		super.append(msg);
	}

	@Override
	protected void process(SensorMessage msg) {

		/*
		 * Retrieve the sensors stored in the server storage
		 */
		ConcurrentHashMap<String, SensorRepresentation> sensors = storage.getSensors();

		/*
		 * Check if the sensor associated with this message was already present
		 */
		SensorRepresentation rep = sensors.get(msg.getSensorId());

		boolean update = false;

		if (rep == null) {
			/*
			 * Unknown sensor
			 */
			rep = new SensorRepresentation(msg.getSensorId(), msg.getPosition(), msg.isFree());
			sensors.put(msg.getSensorId(), rep);
			updateMaxPosition(msg.getPosition());
			rebuildMap();
			update = true;
		} else {
			/*
			 * Known sensor
			 */
			update = rep.updateState(msg.isFree());
		}

		/*
		 * Notify clients if needed
		 */
		if (update) {
			publisherDaemon.notifyClients();
		}

	}

	/**
	 * Update the known world dimensions.
	 * 
	 * @param position
	 *            a sensor position
	 */
	private void updateMaxPosition(Position position) {

		int row = position.getRow();
		int column = position.getColumn();

		if (column > this.storage.getWorldColumns()) {
			this.storage.setWorldColumns(column);
		}

		if (row > this.storage.getWorldRows()) {
			this.storage.setWorldRows(row);
		}

	}

	/**
	 * Build a graph from the sensors representation.
	 */
	private void rebuildMap() {

		Graph map = GraphFromMap.build(Storage.getInstance().getSensors(), this.storage.getWorldRows(),
				this.storage.getWorldColumns());

		this.storage.setMap(map);

	}

}
