package acco.isac.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import acco.isac.datastructures.Edge;
import acco.isac.datastructures.OldGraph;
import acco.isac.datastructures.OldVertex;

/**
 *
 * @author http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
 *
 */

public class OldDijkstraAlgorithm {

	private final List<OldVertex> nodes;
	private final List<Edge> edges;
	private Set<OldVertex> settledNodes;
	private Set<OldVertex> unSettledNodes;
	private Map<OldVertex, OldVertex> predecessors;
	private Map<OldVertex, Integer> distance;

	public OldDijkstraAlgorithm(OldGraph graph) {
		// create a copy of the array so that we can operate on this array
		this.nodes = new ArrayList<OldVertex>(graph.getVertexes());
		this.edges = new ArrayList<Edge>(graph.getEdges());
	}

	public void execute(OldVertex source) {
		settledNodes = new HashSet<OldVertex>();
		unSettledNodes = new HashSet<OldVertex>();
		distance = new HashMap<OldVertex, Integer>();
		predecessors = new HashMap<OldVertex, OldVertex>();
		distance.put(source, 0);
		unSettledNodes.add(source);
		while (unSettledNodes.size() > 0) {
			OldVertex node = getMinimum(unSettledNodes);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}
	}

	private void findMinimalDistances(OldVertex node) {
		List<OldVertex> adjacentNodes = getNeighbors(node);
		for (OldVertex target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
				distance.put(target, getShortestDistance(node) + getDistance(node, target));
				predecessors.put(target, node);
				unSettledNodes.add(target);
			}
		}

	}

	private int getDistance(OldVertex node, OldVertex target) {
		for (Edge edge : edges) {
			if (edge.getSource().equals(node) && edge.getDestination().equals(target)) {
				return edge.getWeight();
			}
		}
		throw new RuntimeException("Should not happen");
	}

	private List<OldVertex> getNeighbors(OldVertex node) {
		List<OldVertex> neighbors = new ArrayList<OldVertex>();
		for (Edge edge : edges) {
			if (edge.getSource().equals(node) && !isSettled(edge.getDestination())) {
				neighbors.add(edge.getDestination());
			}
		}
		return neighbors;
	}

	private OldVertex getMinimum(Set<OldVertex> vertexes) {
		OldVertex minimum = null;
		for (OldVertex vertex : vertexes) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}
		return minimum;
	}

	private boolean isSettled(OldVertex vertex) {
		return settledNodes.contains(vertex);
	}

	private int getShortestDistance(OldVertex destination) {
		Integer d = distance.get(destination);
		if (d == null) {
			return Integer.MAX_VALUE;
		} else {
			return d;
		}
	}

	/*
	 * This method returns the path from the source to the selected target and
	 * NULL if no path exists
	 */
	public LinkedList<OldVertex> getPath(OldVertex target) {
		LinkedList<OldVertex> path = new LinkedList<OldVertex>();
		OldVertex step = target;
		// check if a path exists
		if (predecessors.get(step) == null) {
			return null;
		}
		path.add(step);
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}
		// Put it into the correct order
		Collections.reverse(path);
		return path;
	}

}
