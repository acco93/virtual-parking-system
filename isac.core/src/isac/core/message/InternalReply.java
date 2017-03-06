package isac.core.message;

import isac.core.data.Position;

public class InternalReply {


	private String requestId;
	private Position from;
	private Position destination;
	private int hops;

	public InternalReply(String requestId, Position from, Position destination, int hops) {
		this.requestId = requestId;
		this.from = from;
		this.destination = destination;
		this.hops = hops;
	}

	public String getRequestId() {
		return requestId;
	}

	public Position getFrom() {
		return from;
	}

	public Position getDestination() {
		return destination;
	}

	public int getHops() {
		return hops;
	}

	


}
