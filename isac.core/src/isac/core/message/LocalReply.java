package isac.core.message;

import isac.core.data.Position;

public class LocalReply {

	private Position destination;
	private int hops;

	public LocalReply(InternalReply iReply) {
		this.destination = iReply.getDestination();
		this.hops = iReply.getHops();
	}

	public Position getDestination() {
		return destination;
	}

	public int getHops() {
		return hops;
	}

}
