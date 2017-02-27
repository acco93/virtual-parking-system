package acco.isac.algorithms;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import acco.isac.datastructures.Graph;
import acco.isac.datastructures.ShortestPathVertex;
import acco.isac.datastructures.Vertex;

/**
 * 
 * Simple homemade Dijkstra algorithm implementated as described in
 * "Introduction to algorithms, 3rd edition".
 * 
 * @author acco
 *
 */

public class DijkstraAlgorithm {

	private Graph<ShortestPathVertex> graph;
	private ShortestPathVertex source;
	private HashSet<ShortestPathVertex> set;
	private List<ShortestPathVertex> queue;

	public DijkstraAlgorithm(Graph<ShortestPathVertex> graph, ShortestPathVertex source) {
		this.graph = graph;
		this.source = source;
		this.set = new HashSet<ShortestPathVertex>();
		this.queue = new LinkedList<ShortestPathVertex>();
		this.initializeSingleSource();

		// add all nodes to the queue
		this.queue.addAll(this.graph.getNodes());

		while (!this.queue.isEmpty()) {
			// sorting each time is not very efficient ... maybe an heap?
			Collections.sort(this.queue, (u, v) -> {
				return u.getShortestPathEstimate() - v.getShortestPathEstimate();
			});
			ShortestPathVertex u = this.queue.remove(0);
			this.set.add(u);
			for (Vertex v : u.getAdjecents()) {
				this.relax(u, (ShortestPathVertex) v);
			}
		}
	}

	/**
	 * Set all nodes shortest-path estimate to Inf and source node shortest-path
	 * estimate to 0.
	 */
	private void initializeSingleSource() {
		for (ShortestPathVertex vertex : this.graph.getNodes()) {
			vertex.reset();
		}
		source.setShortestPathEstimate(0);
	}

	/**
	 * Relax operation.
	 * 
	 * @param u
	 *            node
	 * @param v
	 *            node
	 */
	private void relax(ShortestPathVertex u, ShortestPathVertex v) {

		if (v.getShortestPathEstimate() > u.getShortestPathEstimate() + u.distanceFrom(v)) {
			v.setShortestPathEstimate(u.getShortestPathEstimate() + u.distanceFrom(v));
			v.setPredecessor(u);
		}
	}

	public List<ShortestPathVertex> getPath(ShortestPathVertex destination) {
		List<ShortestPathVertex> path = new LinkedList<>();

		ShortestPathVertex node = destination;

		while (node.getPredecessor() != null) {
			path.add(node);
			node = node.getPredecessor();
		}

		// append the source node
		path.add(source);
		Collections.reverse(path);
		return path;
	}

}
