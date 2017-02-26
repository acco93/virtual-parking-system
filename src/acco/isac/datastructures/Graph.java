package acco.isac.datastructures;

import java.util.List;

/**
*
* @author http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
*
*/

public class Graph {

	private final List<Vertex> vertexes;
	private final List<Edge> edges;

	public Graph(List<Vertex> vertexes, List<Edge> edges) {
		this.vertexes = vertexes;
		this.edges = edges;
	}

	public List<Vertex> getVertexes() {
		return vertexes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	@Override
	public String toString() {
		return "Graph [edges=" + edges + "]";
	}
	
	

}
