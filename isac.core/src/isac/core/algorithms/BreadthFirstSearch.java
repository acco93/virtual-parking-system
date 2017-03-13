package isac.core.algorithms;

import java.util.PriorityQueue;

import isac.core.datastructures.BFSColor;
import isac.core.datastructures.Graph;
import isac.core.datastructures.Vertex;
import isac.core.sharedknowledge.R;

/**
 * 
 * BFS implementation in a graph.
 * 
 * @author acco
 *
 */
public class BreadthFirstSearch {

	public BreadthFirstSearch(Graph graph, Vertex source) {

		for (Vertex node : graph.getNodes()) {
			node.setColor(BFSColor.WHITE);
			node.setDistance(R.INF);
			node.setPredecessor(null);
		}

		source.setColor(BFSColor.GRAY);
		source.setDistance(0);
		source.setPredecessor(null);

		PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>();
		queue.add(source);

		while (!queue.isEmpty()) {
			Vertex u = queue.poll();
			for (Vertex v : u.getAdjecent()) {

				if (v.getColor() == BFSColor.WHITE) {
					v.setColor(BFSColor.GRAY);
					v.setDistance(u.getDistance() + 1);
					v.setPredecessor(u);
					queue.add(v);
				}
			}
			u.setColor(BFSColor.BLACK);
		}
	}

}
