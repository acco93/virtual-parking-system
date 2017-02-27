package acco.isac.algorithms;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import acco.isac.datastructures.Graph;
import acco.isac.datastructures.ShortestPathVertex;
import acco.isac.datastructures.Vertex;

public class DijkstraAlgorithm {

	private Graph<ShortestPathVertex> graph;
	private ShortestPathVertex source;
	private HashSet<ShortestPathVertex> set;
	private PriorityQueue<ShortestPathVertex> queue;

	public DijkstraAlgorithm(Graph<ShortestPathVertex> graph, ShortestPathVertex source) {
		this.graph = graph;
		this.source = source;
		this.set = new HashSet<ShortestPathVertex>();
		this.queue = new PriorityQueue<ShortestPathVertex>((u, v) -> {
			return u.getShortestPathEstimate() - v.getShortestPathEstimate();
		});
		this.initializeSingleSource();

		// add all nodes to the queue
		this.queue.addAll(this.graph.getNodes());

		while (!this.queue.isEmpty()) {
			ShortestPathVertex u = this.queue.remove();
			this.set.add(u);
			for (Vertex v : u.getAdjecents()) {
				System.out.println("qua");
				this.relax(u, (ShortestPathVertex) v);
			}
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
