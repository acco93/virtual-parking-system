package acco.isac.datastructures;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import acco.isac.server.inforepresentation.EnvironmentInfo;

/**
 * 
 * Simple Vertex class of an indirect graph, it contains all the information to
 * compute the needed algorithms.
 * 
 * @author acco
 *
 */
public class Vertex {

	/**
	 * Vertex unique id.
	 */
	private String id;
	/**
	 * List of adjacent nodes.
	 */
	private List<Vertex> adjacent;
	/**
	 * Edge weight from this node to adjacent.
	 */
	private Map<String, Integer> weights;

	// Single-source shortest path data.
	private int shortestPathEstimate;

	private Vertex predecessor;

	// Breadth search first data.
	private BFSColor color;
	private int distance;

	private EnvironmentInfo info;

	public Vertex(String id, EnvironmentInfo info) {
		this.id = id;
		this.adjacent = new LinkedList<>();
		this.weights = new HashMap<>();
		this.info = info;
	}

	public void addAdjacent(Vertex vertex, int weight) {
		this.adjacent.add(vertex);
		this.weights.put(vertex.getId(), weight);
	}

	public String getId() {
		return id;
	}

	public List<Vertex> getAdjecent() {
		return adjacent;
	}

	public Integer weightBetween(Vertex vertex) {
		return this.weights.get(vertex.getId());
	}

	public int getShortestPathEstimate() {
		return shortestPathEstimate;
	}

	public void setShortestPathEstimate(int shortestPathEstimate) {
		this.shortestPathEstimate = shortestPathEstimate;
	}

	public Vertex getPredecessor() {
		return predecessor;
	}

	public void setPredecessor(Vertex predecessor) {
		this.predecessor = predecessor;
	}

	public BFSColor getColor() {
		return color;
	}

	public void setColor(BFSColor color) {
		this.color = color;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public EnvironmentInfo getInfo() {
		return info;
	}

}
