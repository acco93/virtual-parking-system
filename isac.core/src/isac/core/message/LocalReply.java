package isac.core.message;

import isac.core.data.Position;

/**
 * 
 * Sensor to client reply.
 * 
 * @author acco
 *
 */
public class LocalReply {

	private Position destination;
	private int hops;
	private String id;

	public LocalReply(String id, Position destination, int hops) {
		this.id = id;
		this.destination = destination;
		this.hops = hops;
	}

	public Position getDestination() {
		return destination;
	}

	public int getHops() {
		return hops;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "LocalReply [destination=" + destination + ", hops=" + hops + ", id=" + id + "]";
	}

}
