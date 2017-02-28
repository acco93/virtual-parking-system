package acco.isac.algorithms;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import acco.isac.datastructures.Graph;
import acco.isac.datastructures.Vertex;
import acco.isac.sharedknowledge.R;

/**
 * 
 * Simple homemade Dijkstra algorithm implementated as described in
 * "Introduction to algorithms, 3rd edition".
 * 
 * @author acco
 *
 */

public class DijkstraAlgorithm {

	private Graph graph;
	private Vertex source;
	private HashSet<Vertex> set;
	private List<Vertex> queue;

	public DijkstraAlgorithm(Graph graph, Vertex source) {
		this.graph = graph;
		this.source = source;
		this.set = new HashSet<Vertex>();
		this.queue = new LinkedList<Vertex>();
		this.initializeSingleSource();

		// add all nodes to the queue
		this.queue.addAll(this.graph.getNodes());

		while (!this.queue.isEmpty()) {
			// sorting each time is not very efficient ... maybe an heap?
			Collections.sort(this.queue, (u, v) -> {
				return u.getShortestPathEstimate() - v.getShortestPathEstimate();
			});
			Vertex u = this.queue.remove(0);
			this.set.add(u);
			for (Vertex v : u.getAdjecent()) {
				this.relax(u, v);
			}
		}
	}

	/**
	 * Set all nodes shortest-path estimate to Inf and source node shortest-path
	 * estimate to 0.
	 */
	private void initializeSingleSource() {
		for (Vertex vertex : this.graph.getNodes()) {
			vertex.setShortestPathEstimate(R.INF);
			vertex.setPredecessor(null);
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
	private void relax(Vertex u, Vertex v) {

		if (v.getShortestPathEstimate() > u.getShortestPathEstimate() + u.weightBetween(v)) {
			v.setShortestPathEstimate(u.getShortestPathEstimate() + u.weightBetween(v));
			v.setPredecessor(u);
		}
	}

	public List<Vertex> getPath(Vertex destination) {
		List<Vertex> path = new LinkedList<>();

		Vertex node = destination;

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
