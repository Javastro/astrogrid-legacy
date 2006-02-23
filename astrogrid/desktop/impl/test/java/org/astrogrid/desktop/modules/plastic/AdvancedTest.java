package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.votech.plastic.CommonMessageConstants;

/**
 * Tests things that don't fit neatly elsewhere
 * @author jdt
 *
 */
public class AdvancedTest extends PresetupHub {
	private URI m1 = URI.create("ivo://mess1");
	private URI m2 = URI.create("ivo://mess2");
	private URI m3 = URI.create("ivo://mess3");
	
	/**
	 * Clients that can't respond shouldn't have an entry in the results map.
	 * ie, not polling, or no callback apps
	 */
	public void testOnlyResponsiveClientsReturnAResult() {
		
		URI p1 = hub.registerNoCallBack("deaf");
		URI p2 = hub.registerRMI("rmi", CommonMessageConstants.EMPTY, new TestListener("test"));
		URI p3 = hub.registerPolling("polling", CommonMessageConstants.EMPTY);
		
		Map results = hub.request(SENDER_ID, m1, CommonMessageConstants.EMPTY);
		
		assertTrue(results.containsKey(p2));
		assertFalse(results.containsKey(p1));
		assertFalse(results.containsKey(p3));
	}
	
	/**
	 * Get clients by message
	 */
	public void testGetClientsByMessage() {
		List messages1 = new ArrayList();
		List messages2 = new ArrayList();
		messages1.add(m1);
		messages1.add(m2);
		messages1.add(m3);
		messages2.add(m1);
		
		URI p1 = hub.registerPolling("tart", CommonMessageConstants.EMPTY);
		URI p2 = hub.registerPolling("polygamous", messages1);
		URI p3 = hub.registerPolling("monogamous", messages2);
		
		List interestedInm1 = hub.getMessageRegisteredIds(m1);
		List interestedInm2 = hub.getMessageRegisteredIds(m2);
		List interestedInm3 = hub.getMessageRegisteredIds(m3);
		
		//Only those that are explicitly registered should respond
		assertFalse(interestedInm1.contains(p1));
		assertFalse(interestedInm2.contains(p1));
		assertFalse(interestedInm3.contains(p1));
		
		assertTrue(interestedInm1.contains(p2));
		assertTrue(interestedInm1.contains(p3));
		
		assertTrue(interestedInm2.contains(p2));
		assertFalse(interestedInm2.contains(p3));
	
		assertTrue(interestedInm3.contains(p2));
		assertFalse(interestedInm3.contains(p3));

	}
	
	
}
