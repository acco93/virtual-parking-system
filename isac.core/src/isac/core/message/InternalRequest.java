package isac.core.message;

import isac.core.data.Position;

public class InternalRequest {

	private Position from;
	private LocalRequest request;
	private String id;
	private int hops;

	public InternalRequest(String id, Position from, LocalRequest request, int hops) {
		this.id = id;
		this.from = from;
		this.request = request;
		this.hops = hops;
	}

	public Position getFrom() {
		return from;
	}

	public LocalRequest getClientRequest() {
		return request;
	}

	public String getId() {
		return id;
	}

	public int getHops() {
		return hops;
	}

}
