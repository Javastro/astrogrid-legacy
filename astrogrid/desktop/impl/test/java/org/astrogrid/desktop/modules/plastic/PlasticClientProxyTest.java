package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.astrogrid.common.namegen.InMemoryNameGen;

public class PlasticClientProxyTest extends TestCase {

	/**
	 * Test that clients aren't affected by any unexpected changes to their parameters.
	 * Test method for 'org.astrogrid.desktop.modules.plastic.PlasticClientProxy.PlasticClientProxy(NameGen, String, List)'
	 */
	public void testPlasticClientProxyNameGenStringList() {
		List messages = new ArrayList();
		messages.add(URI.create("ivo://test"));
		PlasticClientProxy client = new PollingPlasticClient(new InMemoryNameGen(),"test", messages );
		messages.clear();
		List storedMessages = client.getMessages();
		assertEquals(1, storedMessages.size());
	}

}
