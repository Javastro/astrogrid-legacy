/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.buffer.BoundedFifoBuffer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.common.namegen.NameGen;
import org.votech.plastic.outgoing.PlasticException;

/**
 * A plastic client that stores messages for later
 * retrieval by polling.  
 * @author jdt
 *
 */
public class PollingPlasticClient extends PlasticClientProxy {
	private static final int BUFFERSIZE = 1000;
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(PollingPlasticClient.class);

	/**
	 * @param gen
	 * @param name
	 * @param supportedMessages
	 */
	public PollingPlasticClient(NameGen gen, String name, List supportedMessages) {
		super(gen, name, supportedMessages);
		logger.debug("Creating a Polling Client: "+name);
	}

	/**
	 * @param gen
	 * @param name
	 */
	public PollingPlasticClient(NameGen gen, String name) {
		super(gen, name);
		logger.debug("Creating a Polling Client: "+name);
	}

	/**
	 * 
	 *  Note that nothing is returned, since we cannot wait.
	 *  Messages are queued up to the max buffer size, then
	 *  the first in are dropped.
	 * @see org.astrogrid.desktop.modules.plastic.PlasticClientProxy#perform(java.net.URI, java.net.URI, java.util.List)
	 */
	public synchronized Object perform(URI sender, URI message, List args)
			throws PlasticException {
		
		List messageDetails = new ArrayList();
		messageDetails.add(sender);
		messageDetails.add(message);
		messageDetails.add(new ArrayList(args)); //necessary to create a new one, in case the old list is reused by the client
		if (buffer.isFull()) {
			List thrownAwayMessage = (List) buffer.remove();
			URI tamSender = (URI) thrownAwayMessage.get(0);
			URI tamMessage = (URI) thrownAwayMessage.get(1);
			logger.warn("Buffer overflow - discarding message "+tamMessage+" from "+tamSender);
		}
		buffer.add(messageDetails);
		return null;
	}
	BoundedFifoBuffer buffer = new BoundedFifoBuffer(BUFFERSIZE);

	public List getStoredMessages() {
		return new ArrayList(buffer);
	}

	/**
	 * Discard any messages.
	 * TODO thing about synch issues - this might be too heavy handed
	 */
	public synchronized void flush() {
		buffer.clear();		
	}

}
