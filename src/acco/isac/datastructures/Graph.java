package acco.isac.datastructures;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * A graph is a set of nodes and edges. The edges are implicit in the Vertex
 * definition.
 * 
 * @author acco
 *
 */
public class Graph {

	private List<Vertex> nodes;

	public Graph(List<Vertex> nodes) {
		this.nodes = nodes;
	}

	public List<Vertex> getNodes() {
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
		for (Vertex vertex : nodes) {
			if (vertex.getId().equals(id)) {
				return vertex;
			}
		}
		return null;
	}

}
