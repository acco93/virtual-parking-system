package isac.core.message;

import isac.core.data.Position;

public class LocalRequest {

	private LocalRequestType type;
	private Position userPosition;
	private String replyChannelName;

	public LocalRequest(LocalRequestType type, Position userPosition, String replyChannelName) {
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

}
