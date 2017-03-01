package acco.isac.client;

import java.util.LinkedList;
import java.util.Optional;

import acco.isac.datastructures.Graph;
import acco.isac.datastructures.Vertex;
import acco.isac.environment.Position;

public class ClientStorage {

	private static ClientStorage instance = new ClientStorage();
	private Optional<Position> carPosition;
	private Optional<Position> userPosition;
	private Graph map;
	private int worldColumns;
	private int worldRows;

	private ClientStorage() {
		this.carPosition = Optional.empty();
		this.userPosition = Optional.empty();
		this.map = new Graph(new LinkedList<Vertex>());
		this.worldColumns = 0;
		this.worldRows = 0;
	}

	public static ClientStorage getInstance() {
		return ClientStorage.instance;
	}

	public Optional<Position> getCarPosition() {
		return carPosition;
	}

	public void setCarPosition(Optional<Position> carPosition) {
		this.carPosition = carPosition;
	}

	public Optional<Position> getUserPosition() {
		return userPosition;
	}

	public void setUserPosition(Optional<Position> userPosition) {
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

}