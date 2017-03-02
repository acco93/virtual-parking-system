package isac.client.model;

import java.util.LinkedList;
import java.util.Optional;

import isac.core.data.Position;
import isac.core.datastructures.Graph;
import isac.core.datastructures.Vertex;

public class Storage {

	private static Storage instance = new Storage();
	private Optional<Position> carPosition;
	private Position userPosition;
	private Graph map;
	private int worldColumns;
	private int worldRows;
	private long time;

	private Storage() {
		this.carPosition = Optional.empty();
		this.userPosition = new Position(0, 0);
		this.map = new Graph(new LinkedList<Vertex>());
		this.worldColumns = 0;
		this.worldRows = 0;
	}

	public static Storage getInstance() {
		return Storage.instance;
	}

	public Optional<Position> getCarPosition() {
		return carPosition;
	}

	public void setCarPosition(Optional<Position> position) {
		this.carPosition = position;
	}

	public Position getUserPosition() {
		return userPosition;
	}

	public void setUserPosition(Position userPosition) {
		this.userPosition = userPosition;
	}

	public Graph getMap() {
		return map;
	}

	public void setMap(Graph map) {
		this.map = map;
	}

	public int getWorldColumns() {
		return worldColumns;
	}

	public void setWorldColumns(int worldColumns) {
		this.worldColumns = worldColumns;
	}

	public int getWorldRows() {
		return worldRows;
	}

	public void setWorldRows(int worldRows) {
		this.worldRows = worldRows;
	}

	public synchronized void setLastServerHeartbeat(long time) {
		this.time = time;

	}

	public long getLastServerHeartbeat() {
		return this.time;

	}

}
