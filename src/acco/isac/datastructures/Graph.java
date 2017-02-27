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

	/**
	 * Retrieve a vertex from its name.
	 * 
	 * @param id
	 * @return
	 */
	public Vertex getVertexFromId(String id) {
		// should be implemented in a more efficient way
		// maybe storing all nodes in map String->Vertex
		for (T vertex : nodes) {
			if (vertex.getId().equals(id)) {
				return vertex;
			}
		}
		return null;
	}

}
