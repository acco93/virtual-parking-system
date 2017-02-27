package acco.isac.datastructures;

import java.util.List;

/**
 * 
 * A graph is a set of nodes and edges. The edges are implicit in the Vertex
 * definition.
 * 
 * @author acco
 *
 */
public class Graph<T extends Vertex> {

	private List<T> nodes;

	public Graph(List<T> nodes) {
		this.nodes = nodes;
	}

	public List<T> getNodes() {
		return this.nodes;
	}

}
