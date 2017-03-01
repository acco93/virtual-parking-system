package acco.isac.server;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import acco.isac.datastructures.Graph;
import acco.isac.datastructures.Vertex;
import acco.isac.environment.Position;
import acco.isac.server.inforepresentation.SensorRepresentation;
import acco.isac.server.inforepresentation.StreetRepresentation;

public class Storage {

	private static Storage instance = new Storage();

	private int worldColumns;
	private int worldRows;
	private ConcurrentHashMap<String, SensorRepresentation> sensors;
	private Graph map;

	private Storage() {
		this.worldRows = -1;
		this.worldColumns = -1;
		this.sensors = new ConcurrentHashMap<String, SensorRepresentation>();
		this.map = new Graph(new LinkedList<Vertex>());
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

	public synchronized Graph getMap() {
		return map;
	}

	public synchronized void setMap(Graph map) {
		this.map = map;
	}

	public static void setInstance(Storage instance) {
		Storage.instance = instance;
	}

}
