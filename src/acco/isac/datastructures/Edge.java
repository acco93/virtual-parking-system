package acco.isac.datastructures;

/**
*
* @author http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
*
*/

public class Edge {

	private final String id;
	private final OldVertex source;
	private final OldVertex destination;
	private final int weight;

	public Edge(String id, OldVertex source, OldVertex destination, int weight) {
		this.id = id;
		this.source = source;
		this.destination = destination;
		this.weight = weight;
	}

	public String getId() {
		return id;
	}

	public OldVertex getDestination() {
		return destination;
	}

	public OldVertex getSource() {
		return source;
	}

	public int getWeight() {
		return weight;
	}

	

	@Override
	public String toString() {
		return "Edge [id=" + id + ", source=" + source + ", destination=" + destination + ", weight=" + weight + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + weight;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}

}
