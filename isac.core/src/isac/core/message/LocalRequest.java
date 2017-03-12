package isac.core.message;

import isac.core.data.Position;

public class LocalRequest {

	private LocalRequestType type;
	private Position userPosition;
	private Position carPosition;
	private String replyChannelName;
	private String id;

	public LocalRequest(String id, LocalRequestType type, Position userPosition, Position carPosition,
			String replyChannelName) {
		this.id = id;
		this.type = type;
		this.userPosition = userPosition;
		this.carPosition = carPosition;
		this.replyChannelName = replyChannelName;
	}

	public LocalRequestType getType() {
		return type;
	}

	public String getReplyChannelName() {
		return replyChannelName;
	}

	public String getId() {
		return id;
	}

	public Position getUserPosition() {
		return userPosition;
	}

	public Position getCarPosition() {
		return carPosition;
	}

}
