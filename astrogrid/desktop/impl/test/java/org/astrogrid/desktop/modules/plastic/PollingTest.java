/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.votech.plastic.CommonMessageConstants;

/**
 * @author jdt
 *
 */
public class PollingTest extends PresetupHub {
	private URI sender = URI.create("ivo://junit");

	public void testPollingAll() {
		URI id = hub.registerPolling("polling", CommonMessageConstants.EMPTY);
		hub.pollForMessages(id); //just to clear out
		
		populate(id, 100);
		List nomessage = hub.pollForMessages(sender);
		assertEquals(0, nomessage.size());
		List messages = hub.pollForMessages(id);
		assertEquals(100, messages.size());
		
		//pick one at random
		List message = (List) messages.get(50);
		assertEquals(sender, message.get(0));
		assertEquals(URI.create("ivo://message50"), message.get(1));
		List argsback = (List) message.get(2);
		assertEquals(51,argsback.size());
		
		//now should be empty
		List messages2 = hub.pollForMessages(id);
		assertEquals(0, messages2.size());
	}
	/** 
	 * I happen to know that this implementation only stores 1000 messages
	 *
	 */
	public void testPollingOverFlow() {
		URI id = hub.registerPolling("polling", CommonMessageConstants.EMPTY);
		hub.pollForMessages(id); //just to clear out
		
		populate(id, 1200);
		List nomessage = hub.pollForMessages(sender);
		assertEquals(0, nomessage.size());
		List messages = hub.pollForMessages(id);
		assertEquals(1000, messages.size());
		
		//pick one at random
		List message = (List) messages.get(0);
		assertEquals(sender, message.get(0));
		assertEquals(URI.create("ivo://message200"), message.get(1));
		
		//now should be empty
		List messages2 = hub.pollForMessages(id);
		assertEquals(0, messages2.size());
	}

	private void populate(URI id,  int num) {
		List args = new ArrayList();
		for (int m=0;m<num;++m) {
			URI message = URI.create("ivo://message"+m);
			args.add("arg"+m);
			Map results = (Map) hub.request(sender , message, args);
			assertEquals(CommonMessageConstants.RPCNULL, results.get(id));
		}
	}

}
