package acco.isac.server;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import acco.isac.datastructures.Graph;
import acco.isac.datastructures.ShortestPathVertex;
import acco.isac.server.inforepresentation.SensorRepresentation;

public class Storage {

	private static Storage instance = new Storage();

	private int worldColumns;
	private int worldRows;
	private ConcurrentHashMap<String, SensorRepresentation> sensors;
	private Graph<ShortestPathVertex> map;

	private Storage() {
		this.worldRows = -1;
		this.worldColumns = -1;
		this.sensors = new ConcurrentHashMap<String, SensorRepresentation>();
		this.map = new Graph<>(new LinkedList<ShortestPathVertex>());
	}

	public static Storage getInstance() {
		return Storage.instance;
	}

	public synchronized int getWorldColumns() {
		return worldColumns;
	}

	public synchronized void setWorldColumns(int worldColumns) {
		this.worldColumns = worldColumns;
	}

	public synchronized int getWorldRows() {
		return worldRows;
	}

	public synchronized void setWorldRows(int worldRows) {
		this.worldRows = worldRows;
	}

	public synchronized ConcurrentHashMap<String, SensorRepresentation> getSensors() {
		return sensors;
	}

	public synchronized void setSensors(ConcurrentHashMap<String, SensorRepresentation> sensors) {
		this.sensors = sensors;
	}

	public synchronized Graph<ShortestPathVertex> getMap() {
		return map;
	}

	public synchronized void setMap(Graph<ShortestPathVertex> map) {
		this.map = map;
	}

	public static void setInstance(Storage instance) {
		Storage.instance = instance;
	}

}
