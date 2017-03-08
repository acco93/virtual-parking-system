package isac.core.message;

import isac.core.data.Position;

public class LocalRequest {

	private LocalRequestType type;
	private Position userPosition;
	private String replyChannelName;
	private String id;

	public LocalRequest(String id, LocalRequestType type, Position userPosition, String replyChannelName) {
		this.id = id;
		this.type = type;
		this.userPosition = userPosition;
		this.replyChannelName = replyChannelName;
	}

	public LocalRequestType getType() {
		return type;
	}

	public Position getPosition() {
		return userPosition;
	}

	public String getReplyChannelName() {
		return replyChannelName;
	}

	public String getId() {
		return id;
	}

}
