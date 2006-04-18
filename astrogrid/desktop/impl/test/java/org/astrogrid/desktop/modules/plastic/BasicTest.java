package org.astrogrid.desktop.modules.plastic;


import java.io.File;
import java.net.URI;
import java.util.Collection;

import org.picocontainer.Startable;
import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.HubMessageConstants;
import org.votech.plastic.PlasticHubListener;


public class BasicTest  extends AbstractPlasticTestBase {
	public void testContructsOK() {
		File file = new File(System.getProperty("user.home"),".plastic");
		//System.out.println(file);
		assertFalse("Plastic file should not exist before test", file.exists());
		Startable listener = new PlasticHubImpl(executor , idGenerator, messenger, rmi, web, new PrettyPrinterImpl(browser), config, shutdown);
		listener.start();
		assertTrue("Plastic file should exist after test",file.exists());
		PlasticHubListener hub = (PlasticHubListener) listener;
		URI hubId = hub.getHubId();
		Collection allIds = hub.getRegisteredIds();
		assertNotNull("hub is registered", hubId);
		assertTrue("Hub is regisered", allIds.contains(hubId));
		assertEquals("Nothing else is registered", 1, allIds.size());
		listener.stop();
	}
	
	public void testShutDown() {
		PlasticHubImpl hub = new PlasticHubImpl(executor , idGenerator, messenger, rmi, web, new PrettyPrinterImpl(browser), config, shutdown);
		hub.start();
		TestListener2 client = new TestListener2();
		hub.registerRMI("client", CommonMessageConstants.EMPTY, client);
		hub.halting();
		assertEquals(HubMessageConstants.HUB_STOPPING_EVENT, client.getMessage());
		assertEquals(hub.getHubId(), client.getSender());
		
	}
	


}
