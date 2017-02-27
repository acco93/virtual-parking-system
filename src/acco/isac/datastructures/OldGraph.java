package acco.isac.datastructures;

import java.util.List;

/**
*
* @author http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
*
*/

public class OldGraph {

	private final List<OldVertex> vertexes;
	private final List<Edge> edges;

	public OldGraph(List<OldVertex> vertexes, List<Edge> edges) {
		this.vertexes = vertexes;
		this.edges = edges;
	}

	public List<OldVertex> getVertexes() {
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
