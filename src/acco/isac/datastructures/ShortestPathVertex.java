package acco.isac.datastructures;

public class ShortestPathVertex extends Vertex {

	private int shortestPathEstimate;
	private ShortestPathVertex predecessor;

	public ShortestPathVertex(String id) {
		super(id);
		reset();
	}

	public int getShortestPathEstimate() {
		return this.shortestPathEstimate;
	}

	public void setShortestPathEstimate(int value) {
		this.shortestPathEstimate = value;
	}

	public void setPredecessor(ShortestPathVertex vertex) {
		this.predecessor = vertex;
	}

	public void reset() {
		this.shortestPathEstimate = 1000;
		this.predecessor = null;
	}

	public int distanceFrom(ShortestPathVertex vertex) {
		return this.weightBetween(vertex);
	}

	public ShortestPathVertex getPredecessor() {
		return this.predecessor;
	}

	@Override
	public String toString() {
		return super.toString() + ": ShortestPathVertex [shortestPathEstimate=" + shortestPathEstimate+"]";
	}

}
