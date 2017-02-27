package acco.isac.algorithms;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import acco.isac.datastructures.Graph;
import acco.isac.datastructures.ShortestPathVertex;
import acco.isac.datastructures.Vertex;

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
			Collections.sort(this.queue, (u, v) -> {
				return u.getShortestPathEstimate() - v.getShortestPathEstimate();
			});
			ShortestPathVertex u = this.queue.remove(0);
			this.set.add(u);
			for (Vertex v : u.getAdjecents()) {
				this.relax(u, (ShortestPathVertex) v);
			}
			System.out.println("queue:");
			System.out.println(this.queue);
		}
	}

	private void initializeSingleSource() {
		for (ShortestPathVertex vertex : this.graph.getNodes()) {
			vertex.reset();
		}
		source.setShortestPathEstimate(0);
	}

	private void relax(ShortestPathVertex u, ShortestPathVertex v) {

		if (v.getShortestPathEstimate() > u.getShortestPathEstimate() + u.distanceFrom(v)) {
			v.setShortestPathEstimate(u.getShortestPathEstimate() + u.distanceFrom(v));
			v.setPredecessor(u);
		}
	}

	public List<ShortestPathVertex> getPath(ShortestPathVertex destination) {
		List<ShortestPathVertex> path = new LinkedList<>();

		ShortestPathVertex node = destination;

		// System.out.println(node.getAdjecents());

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
