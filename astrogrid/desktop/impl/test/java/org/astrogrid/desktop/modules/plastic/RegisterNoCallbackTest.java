package org.astrogrid.desktop.modules.plastic;


import java.net.URI;
import java.util.Collection;
import java.util.List;

import org.votech.plastic.CommonMessageConstants;


public class RegisterNoCallbackTest  extends PresetupHub {

	public void testRegisterGoodNames() {
		URI id = hub.registerNoCallBack("Application1");
		assertNotNull(id);
		Collection registered = hub.getRegisteredIds();
		assertTrue("Registered IDs should contain this application", registered.contains(id));
		
		URI id2 = hub.registerNoCallBack("Application2");
		assertNotNull(id2);
		Collection registered2 = hub.getRegisteredIds();
		assertTrue("Registered IDs should contain this application", registered2.contains(id));
		assertTrue("Registered IDs should contain this application", registered2.contains(id2));
	}
	
	public void testGetName() {
		URI id1 = hub.registerNoCallBack("Application1");
		URI id2 = hub.registerNoCallBack("Application2");
		assertEquals("Application1", hub.getName(id1));
		assertEquals("Application2", hub.getName(id2));
		String invalidName = hub.getName(URI.create("http://news.bbc.co.uk"));
		assertEquals(CommonMessageConstants.RPCNULL, invalidName);
	}
	
	public void testUnregister() {
		URI id1 = hub.registerNoCallBack("Application1");
		URI id2 = hub.registerNoCallBack("Application2");

		hub.unregister(id1);
		List stillRegistered = hub.getRegisteredIds();
		assertTrue(stillRegistered.contains(id2));
		assertFalse(stillRegistered.contains(id1));
		
	}
	
    /*
     * 
     * No longer true, since the hub doesn't self register, as such
	public void testHubRegistration() {
		hub.registerNoCallBack("Application1");

		List registered = hub.getRegisteredIds();
		assertEquals("Expect two applications, the hub and application 1", 2, registered.size());
		assertTrue(registered.contains(hub.getHubId()));
		
	}
	*/
	/**
	 * This used to fail since the name became part of the URI.  Now should be OK...
	 *
	 */
	public void testRegisterBadName() {
		URI id = hub.registerNoCallBack("Application Name With Spaces");
		assertNotNull(id);
		Collection registered = hub.getRegisteredIds();
		assertTrue("Registered IDs should contain this application", registered.contains(id));
	}

}
