package isac.client.model;

import java.util.LinkedList;
import java.util.Optional;

import isac.core.data.Position;
import isac.core.datastructures.Graph;
import isac.core.datastructures.Vertex;

/**
 * 
 * Client storage implemented as singleton and monitor.
 * 
 * @author acco
 *
 */
public class Storage {

	private static Storage instance = new Storage();
	private Optional<Position> carPosition;
	private Position userPosition;
	private Graph map;
	private int worldColumns;
	private int worldRows;
	private long serverHeartbeat;
	private boolean serverOn;

	private Storage() {
		this.carPosition = Optional.empty();
		/*
		 * Initial position.
		 */
		this.userPosition = new Position(0, 0);
		this.map = new Graph(new LinkedList<Vertex>());
		this.worldColumns = 0;
		this.worldRows = 0;
		this.serverOn = false;
	}

	/**
	 * Return the storage instance.
	 * 
	 * @return storage instance
	 */
	public static Storage getInstance() {
		return Storage.instance;
	}

	/**
	 * Return the car position, if the user didn't park the Optional is empty.
	 * 
	 * @return an Optional<Position>
	 */
	public synchronized Optional<Position> getCarPosition() {
		return carPosition;
	}

	/**
	 * Set the car position.
	 * 
	 * @param position
	 *            an Optional<Position>
	 */
	public synchronized void setCarPosition(Optional<Position> position) {
		this.carPosition = position;
	}

	/**
	 * Get the user position.
	 * 
	 * @return the position
	 */
	public synchronized Position getUserPosition() {
		return userPosition;
	}

	/**
	 * Set the user position.
	 * 
	 * @param userPosition
	 *            the position
	 */
	public synchronized void setUserPosition(Position userPosition) {
		this.userPosition = userPosition;
	}

	/**
	 * Return the most updated known map.
	 * 
	 * @return the map
	 */
	public synchronized Graph getMap() {
		return map;
	}

	/**
	 * Set a new map.
	 * 
	 * @param map
	 *            the map
	 */
	public synchronized void setMap(Graph map) {
		this.map = map;
	}

	/**
	 * Return the number of columns of the world.
	 * 
	 * @return # of columns
	 */
	public synchronized int getWorldColumns() {
		return worldColumns;
	}

	/**
	 * Set the number of columns of the world.
	 * 
	 * @param worldColumns
	 *            # of columns
	 */
	public synchronized void setWorldColumns(int worldColumns) {
		this.worldColumns = worldColumns;
	}

	/**
	 * Return the number of rows of the world.
	 * 
	 * @return # of rows
	 */
	public synchronized int getWorldRows() {
		return worldRows;
	}

	/**
	 * Set the number of rows of the world.
	 * 
	 * @param worldRows
	 *            # of rows
	 */
	public synchronized void setWorldRows(int worldRows) {
		this.worldRows = worldRows;
	}

	/**
	 * Set the last time in milliseconds in which the server has sent its
	 * heartbeat.
	 * 
	 * @param serverHeartbeat
	 */
	public synchronized void setServerHeartbeat(long serverHeartbeat) {
		this.serverHeartbeat = serverHeartbeat;

	}

	/**
	 * Get the last server heartbeat.
	 * 
	 * @return
	 */
	public synchronized long getServerHeartbeat() {
		return this.serverHeartbeat;

	}

	/**
	 * Set the serverOn flag.
	 * 
	 * @param isOn
	 */
	public synchronized void setServerOn(boolean isOn) {
		this.serverOn = isOn;
	}

	/**
	 * Retrieve the serverOn flag.
	 * 
	 * @return
	 */
	public synchronized boolean isServerOn() {
		return this.serverOn;
	}

}
