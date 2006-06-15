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
public class AdvancedUnitTest extends PresetupHub {
	private URI m1 = URI.create("ivo://mess1");
	private URI m2 = URI.create("ivo://mess2");
	private URI m3 = URI.create("ivo://mess3");
	private List messages1;
	private List messages2;
	
	/**
	 * Clients that can't respond shouldn't have an entry in the results map.
	 * ie, not polling, or no callback apps
	 */
	public void testOnlyResponsiveClientsReturnAResult() {
		
		URI p1 = hub.registerNoCallBack("deaf");
		URI p2 = hub.registerRMI("rmi", messages1, new TestListener("test"));
		URI p3 = hub.registerPolling("polling", messages1);
		
		Map results = hub.request(SENDER_ID, m1, CommonMessageConstants.EMPTY);
		
		assertTrue(results.containsKey(p2));
		assertFalse(results.containsKey(p1));
		assertFalse(results.containsKey(p3));
	}
	
	
	public void setUp() {
		super.setUp();
		messages1 = new ArrayList();
		messages2 = new ArrayList();
		messages1.add(m1);
		messages1.add(m2);
		messages1.add(m3);
		messages2.add(m1);
	}
	/**
	 * Get clients by message
	 */
	public void testGetClientsByMessage() {

		
		URI p1 = hub.registerPolling("glaswegian", CommonMessageConstants.EMPTY);
		URI p2 = hub.registerPolling("bilingual", messages1);
		URI p3 = hub.registerPolling("monolingual", messages2);
		
		List interestedInm1 = hub.getMessageRegisteredIds(m1);
		List interestedInm2 = hub.getMessageRegisteredIds(m2);
		List interestedInm3 = hub.getMessageRegisteredIds(m3);
		
		//Only those that are have registered should respond
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
