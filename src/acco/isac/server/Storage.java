package acco.isac.server;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import acco.isac.datastructures.Edge;
import acco.isac.datastructures.Graph;
import acco.isac.datastructures.Vertex;

public class Storage {

	private static Storage instance = new Storage();

	private int maxWorldWidth;
	private int maxWorldHeight;
	private ConcurrentHashMap<String, SensorRepresentation> sensors;
	private Graph map;

	private Storage() {
		this.maxWorldHeight = 0;
		this.maxWorldWidth = 0;
		this.sensors = new ConcurrentHashMap<String, SensorRepresentation>();
		this.map = new Graph(new LinkedList<Vertex>(),new LinkedList<Edge>());
	}

	public static Storage getInstance() {
		return Storage.instance;
	}

	public int getMaxWorldWidth() {
		return maxWorldWidth;
	}

	public void setMaxWorldWidth(int maxWorldWidth) {
		this.maxWorldWidth = maxWorldWidth;
	}

	public int getMaxWorldHeight() {
		return maxWorldHeight;
	}

	public void setMaxWorldHeight(int maxWorldHeight) {
		this.maxWorldHeight = maxWorldHeight;
	}

	public ConcurrentHashMap<String, SensorRepresentation> getSensors() {
		return sensors;
	}

	public void setSensors(ConcurrentHashMap<String, SensorRepresentation> sensors) {
		this.sensors = sensors;
	}

	public Graph getMap() {
		return map;
	}

	public void setMap(Graph map) {
		this.map = map;
	}

	public static void setInstance(Storage instance) {
		Storage.instance = instance;
	}

}
