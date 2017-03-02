package acco.isac.algorithms;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import isac.core.data.InfoType;
import isac.core.data.SensorRepresentation;
import isac.core.datastructures.BFSColor;
import isac.core.datastructures.Graph;
import isac.core.datastructures.Vertex;
import isac.core.sharedknowledge.R;

public class NearestFreePark {

	private Graph graph;
	private Vertex source;

	public NearestFreePark(Graph graph, Vertex source) {

		this.graph = graph;
		this.source = source;

	}

	public List<Vertex> find() {

		List<Vertex> path = new LinkedList<>();

		Vertex node = this.customBFS();

		while (node != null && node.getPredecessor() != null) {
			path.add(node);
			node = node.getPredecessor();
		}

		// append the source node
		path.add(source);
		Collections.reverse(path);
		return path;

	}

	private Vertex customBFS() {
		for (Vertex node : graph.getNodes()) {
			node.setColor(BFSColor.WHITE);
			node.setDistance(R.INF);
			node.setPredecessor(null);
		}

		source.setColor(BFSColor.GRAY);
		source.setDistance(0);
		source.setPredecessor(null);

		PriorityQueue<Vertex> queue = new PriorityQueue<>((a, b) -> {
			return a.getDistance() - b.getDistance();
		});
		queue.add(source);

		while (!queue.isEmpty()) {
			Vertex u = queue.poll();

			if (u.getInfo().getType() == InfoType.SENSOR) {

				SensorRepresentation sensor = (SensorRepresentation) u.getInfo();

				if (sensor.isFree()) {
					return u;
				}

			}

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

		return null;

	}

}
