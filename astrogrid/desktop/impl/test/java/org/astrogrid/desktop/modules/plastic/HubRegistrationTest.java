package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.Map;

import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.PlasticListener;

public class HubRegistrationTest extends PresetupHub {
	public void testHubData() {
		URI hubId = hub.getHubId();
		String name = hub.getName(hubId);
		assertEquals("ACR-Plastic-Hub", name);
		URI test = URI.create("plastic://junit"); //Note that if we stop allowing "anon" messages, we might have to register first
		Map nameMap = (Map) hub.request(test, CommonMessageConstants.GET_NAME, CommonMessageConstants.EMPTY);
		Map ivornMap = hub.request(test, CommonMessageConstants.GET_IVORN, CommonMessageConstants.EMPTY);
		Map versionMap = hub.request(test, CommonMessageConstants.GET_VERSION, CommonMessageConstants.EMPTY);
		Map expectNothingMap = hub.request(test, CommonMessageConstants.VO_SHOW_OBJECTS, CommonMessageConstants.EMPTY);
		
		
		assertEquals(nameMap.get(hubId), name);
		assertEquals("ivo://org.astrogrid/acr/fixme", ivornMap.get(hubId));
		assertEquals(PlasticListener.CURRENT_VERSION, versionMap.get(hubId));
		assertNull(expectNothingMap.get(hubId));
		
		//TODO Object icon = hub.request(test, CommonMessageConstants.GET_NAME, CommonMessageConstants.EMPTY);
		
	}
	
	public void testCantDeregister() {
		URI hubId = hub.getHubId();
		hub.unregister(hubId);
		assertTrue("Hub not there! Shouldn't be able to deregister",hub.getRegisteredIds().contains(hubId)); 
		
	}
	/**
	 * The sender of a message should not receive it.
	 *
	 */
	public void testSenderShouldNotReceive() {
		TestListener2 listener = new TestListener2();
		URI id = hub.registerRMI("test", CommonMessageConstants.EMPTY, listener);
		URI m = URI.create("ivo://test");
		hub.requestAsynch(id, m , CommonMessageConstants.EMPTY);
		URI lastMessage = listener.getMessage();
		assertNotSame(m,lastMessage);
	}
}
