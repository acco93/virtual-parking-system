package isac.environment.sensor;

import java.util.HashMap;
import java.util.HashSet;

import isac.core.data.Position;
import isac.core.message.InternalReply;
import isac.core.message.InternalRequest;
import isac.core.message.LocalRequest;

public class LocalInteractionProcessor {

	private ParkingSensor sensor;
	private int requestIndex;
	private HashSet<String> processedRequestsId;
	private InternalRequestHandler internalRequestsHandler;
	private InternalReplyHandler internalReplyHandler;
	private HashMap<String, LocalRequest> clientDirectRequest;

	public LocalInteractionProcessor(ParkingSensor sensor) {
		this.sensor = sensor;
		this.requestIndex = 0;
		this.processedRequestsId = new HashSet<>();
		this.clientDirectRequest = new HashMap<>();
		this.internalRequestsHandler = new InternalRequestHandler(this);
		this.internalReplyHandler = new InternalReplyHandler(this);

		this.internalRequestsHandler.start();
		this.internalReplyHandler.start();
	}

	public void process(InternalRequest iRequest) {

		System.out.println("Internal request received from " + this.sensor.getId());

		if (isTooFarMessage(this.sensor.getPosition(), iRequest.getFrom())) {
			return;
		}

		System.out.println("Passed. Internal request received from " + this.sensor.getId());

		String id = iRequest.getId();

		if (this.processedRequestsId.contains(id)) {
			// skip it!
			return;
		} else {
			this.processedRequestsId.add(id);

			boolean satisfied = this.tryToSatisfy(iRequest.getClientRequest());

			if (satisfied) {
				// ok spread the reply

				InternalReply iReply = new InternalReply(id, this.sensor.getPosition(), this.sensor.getPosition(), 0);

				this.process(iReply);

			} else {
				// no, spread the internal request

				// create a new request and increments the hops
				InternalRequest newRequest = new InternalRequest(iRequest.getId(), this.sensor.getPosition(),
						iRequest.getClientRequest(), iRequest.getHops() + 1);

				// spread it
				this.internalRequestsHandler.spread(newRequest);
			}
		}

	}

	public void process(InternalReply iReply) {

		if (isTooFarMessage(this.sensor.getPosition(), iReply.getFrom())) {
			return;
		}

		String id = iReply.getRequestId();

		if (this.processedRequestsId.contains(id)) {

			this.processedRequestsId.remove(id);

			// check if you have to reply to the customer

			LocalRequest clientRequest = this.clientDirectRequest.get(id);

			if (clientRequest == null) {

				this.internalReplyHandler.spread(iReply);

			} else {
				// reply to the customer!

				new AdHocCustomerHandler(clientRequest.getReplyChannelName()).send(iReply);

			}

		} else {
			// skip it
		}
	}

	private boolean isTooFarMessage(Position a, Position b) {

		int manhattanDistance = Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getColumn() - b.getColumn());
		if (manhattanDistance > 1) {
			return true;
		}

		return false;
	}

	public void process(LocalRequest request) {
		// translate a local request into an internal request
		String id = "s_" + this.sensor.getPosition().getRow() + "_" + this.sensor.getPosition().getColumn() + "_"
				+ this.requestIndex;
		this.requestIndex++;

		this.processedRequestsId.add(id);

		InternalRequest iRequest = new InternalRequest(id, this.sensor.getPosition(), request, 0);

		// store the client request so to understand that I have to reply to
		// the client

		this.clientDirectRequest.put(id, request);

		// check if you can satisfy it
		boolean satified = tryToSatisfy(request);

		if (satified) {
			// directly reply to the client since this is a LocalRequest
			System.out.println("Request satisfied by " + this.sensor.getId());
			InternalReply iReply = new InternalReply(id, this.sensor.getPosition(), this.sensor.getPosition(), 0);
			this.process(iReply);

		} else {

			System.out.println("Can't satisfy the request");

			// spread the request
			this.internalRequestsHandler.spread(iRequest);

		}

	}

	private boolean tryToSatisfy(LocalRequest request) {
		switch (request.getType()) {
		case LOCATE:
			if (request.getPosition().equals(sensor.getPosition())) {
				return true;
			}
			break;
		case PARK:
			return !sensor.sense();
		default:
			return false;

		}

		return false;
	}

}
