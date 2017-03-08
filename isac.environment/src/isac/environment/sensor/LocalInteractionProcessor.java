package isac.environment.sensor;

import java.util.HashMap;
import java.util.HashSet;

import isac.core.data.Position;
import isac.core.message.InternalReply;
import isac.core.message.InternalRequest;
import isac.core.message.LocalRequest;

/**
 * 
 * Each sensor has a local interaction processor to handle local interactions.
 * For local interaction it is meant: LocalRequest, LocalReply, InternalReply
 * and InternalRequest.
 * 
 * @author acco
 *
 */
public class LocalInteractionProcessor {

	private static final int MAX_LOCAL_INTERACTION_GRID_DISTANCE = 1;
	/*
	 * The sensor, in order to know the position and if it is possible to
	 * satisfy the request.
	 */
	private ParkingSensor sensor;
	/*
	 * Each sensor maintains a progressive request counter.
	 */
	private int requestIndex;
	/*
	 * Store requests id in order to know if you have already received a
	 * request.
	 */
	private HashSet<String> processedRequestsId;
	/*
	 * Store requests id and relative LocalRequest if you've been directly
	 * contacted by the client.
	 */
	private HashMap<String, LocalRequest> clientDirectRequests;
	/*
	 * Receives and spread InternalRequests messages.
	 */
	private InternalRequestHandler internalRequestsHandler;
	/*
	 * Receives and spread InternalReply messages.
	 */
	private InternalReplyHandler internalReplyHandler;

	public LocalInteractionProcessor(ParkingSensor sensor) {

		this.sensor = sensor;
		this.requestIndex = 0;

		this.processedRequestsId = new HashSet<>();
		this.clientDirectRequests = new HashMap<>();

		this.internalRequestsHandler = new InternalRequestHandler(this);
		this.internalReplyHandler = new InternalReplyHandler(this);

		this.internalRequestsHandler.start();
		this.internalReplyHandler.start();
	}

	/**
	 * Process an internal request, that is a request that comes from another
	 * sensor.
	 * 
	 * @param iRequest
	 *            the request
	 */
	public synchronized void process(InternalRequest iRequest) {

		/*
		 * Discard internal request messages that are too distant
		 */
		if (isTooFarMessage(this.sensor.getPosition(), iRequest.getFrom())) {
			return;
		}

		/*
		 * Retrieve the unique request identifier
		 */
		String id = iRequest.getId();

		if (this.processedRequestsId.contains(id)) {
			/*
			 * It was a request that I had already processed. Skip it to avoid
			 * infinity requests circulation.
			 */
			return;
		} else {
			/*
			 * It's a new request, add it to the processed requests
			 */
			this.processedRequestsId.add(id);

			/*
			 * Can I satisfy it?
			 */
			boolean satisfied = this.tryToSatisfy(iRequest.getClientRequest());

			if (satisfied) {

				/*
				 * Yes, construct a reply and process it.
				 */
				InternalReply iReply = new InternalReply(id, this.sensor.getPosition(), this.sensor.getPosition(), 0);
				this.process(iReply);

			} else {

				/*
				 * No, spread the internal request. Create a new request and
				 * increment the hops.
				 */
				InternalRequest newRequest = new InternalRequest(iRequest.getId(), this.sensor.getPosition(),
						iRequest.getClientRequest(), iRequest.getHops() + 1);

				/*
				 * Spread it.
				 */
				this.internalRequestsHandler.spread(newRequest);
			}
		}

	}

	/**
	 * Process an internal reply, that is a reply that comes from another
	 * sensor.
	 * 
	 * @param iReply
	 *            the reply
	 */
	public synchronized void process(InternalReply iReply) {

		/*
		 * Check not to be too far.
		 */
		if (isTooFarMessage(this.sensor.getPosition(), iReply.getFrom())) {
			return;
		}

		String id = iReply.getRequestId();

		/*
		 * Handle reply for which you've received the request.
		 */
		if (this.processedRequestsId.contains(id)) {

			/*
			 * Remove it ...
			 */
			this.processedRequestsId.remove(id);

			/*
			 * Check if the client directly contacted you
			 */
			LocalRequest clientRequest = this.clientDirectRequests.get(id);

			if (clientRequest == null) {
				/*
				 * No, spread the reply ... Someone else will contact the client
				 */
				this.internalReplyHandler.spread(iReply);

			} else {
				/*
				 * Yes, I've to reply to the client
				 */
				new AdHocCustomerHandler(clientRequest.getId(),clientRequest.getReplyChannelName()).send(iReply);
			}

		} else {
			/*
			 * That is a reply for which I've not previously received a request
			 * ... Skip it
			 */
		}
	}

	/**
	 * Check if two positions are too distant or not.
	 * 
	 * @param a
	 *            first position
	 * @param b
	 *            second position
	 * @return true, if too distant
	 */
	private boolean isTooFarMessage(Position a, Position b) {

		int manhattanDistance = Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getColumn() - b.getColumn());
		if (manhattanDistance > MAX_LOCAL_INTERACTION_GRID_DISTANCE) {
			return true;
		}

		return false;
	}

	/**
	 * Process a local request, that is a request that directly comes from a
	 * near client.
	 * 
	 * @param request
	 *            the request
	 */
	public synchronized void process(LocalRequest request) {
		/*
		 * Define an unique id
		 */
		String id = "s_" + this.sensor.getPosition().getRow() + "_" + this.sensor.getPosition().getColumn() + "_"
				+ this.requestIndex;
		/*
		 * Increment the requests counter
		 */
		this.requestIndex++;

		/*
		 * Add the request to the set of processed ones.
		 */
		this.processedRequestsId.add(id);

		/*
		 * Define the distance between the user and the sensor, it may be: 0 if
		 * the user is on the sensor, 1 otherwise. Requests with a distance
		 * greater than 1 are discarded.
		 */
		int distance = 1;
		if (this.sensor.getPosition().equals(request.getPosition())) {
			distance = 0;
		}

		/*
		 * Translate the LocalRequest into an InternalRequest
		 */
		InternalRequest iRequest = new InternalRequest(id, this.sensor.getPosition(), request, distance);

		/*
		 * Add an the entry to the structure that maintains the requests that
		 * started from me.
		 */
		this.clientDirectRequests.put(id, request);

		/*
		 * Check if you can satisfy it
		 */
		boolean satified = tryToSatisfy(request);

		if (satified) {
			/*
			 * Yes, create the reply and process it, that is directly contact
			 * the client.
			 */
			InternalReply iReply = new InternalReply(id, this.sensor.getPosition(), this.sensor.getPosition(),
					distance);
			this.process(iReply);

		} else {
			/*
			 * No, spread the request to neighbor sensors.
			 */
			this.internalRequestsHandler.spread(iRequest);

		}

	}

	/**
	 * Check if a request can be satisfied by this sensor.
	 * 
	 * @param request
	 *            the request
	 * @return true, if it can
	 */
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
