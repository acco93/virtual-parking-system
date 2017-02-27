package acco.isac.datastructures;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * Simple Vertex class of an indirect graph.
 * 
 * @author acco
 *
 */
public class Vertex {

	private String id;
	private List<Vertex> adjacents;
	private Map<String, Integer> weights;

	public Vertex(String id) {
		this.id = id;
		this.adjacents = new LinkedList<Vertex>();
		this.weights = new HashMap<>();
	}

	public void addAdjacent(Vertex vertex, int weight) {
		System.out.println("adding");
		this.adjacents.add(vertex);
		this.weights.put(vertex.getId(), weight);
	}

	public String getId() {
		return id;
	}

	public List<Vertex> getAdjecents() {
		return adjacents;
	}

	public Integer weightBetween(Vertex vertex) {
		return this.weights.get(vertex.getId());
	}

	@Override
	public String toString() {
		return "Vertex [id=" + id + "]";
	}
	
	
}
