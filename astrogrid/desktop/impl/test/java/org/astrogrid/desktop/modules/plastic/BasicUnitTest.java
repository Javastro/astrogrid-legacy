package org.astrogrid.desktop.modules.plastic;


import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.HubMessageConstants;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.PlasticListener;


public class BasicUnitTest  extends AbstractPlasticTestBase {
	private PlasticListener pretendHub = new PlasticListener() {
        
        public Object perform(URI arg0, URI arg1, List arg2) {
            return CommonMessageConstants.RPCNULL;
        }
        
        
    }; //The hub needs a "proxy" to receive messages from the outside world such as "getName" etc.  This is it.

    public void testContructsOK() {
		File file = new File(System.getProperty("user.home"),".plastic");
		assertFalse("Plastic file should not exist before test", file.exists());
		PlasticHubImpl listener = new PlasticHubImpl(executor , idGenerator,  rmi, web, new PrettyPrinterImpl(browser), config);
		listener.start();
        
        pretendHub = new PlasticListener() {
        
                    public Object perform(URI arg0, URI arg1, List arg2) {
                        return CommonMessageConstants.RPCNULL;
                    }
                    
                    
                };
        listener.registerSelf("hub", new Vector(), pretendHub);
        
		assertTrue("Plastic file should exist after test",file.exists());
		PlasticHubListener hub = (PlasticHubListener) listener;
		URI hubId = hub.getHubId();
		Collection allIds = hub.getRegisteredIds();
		assertNotNull("hub is registered", hubId);
		assertTrue("Hub is regisered", allIds.contains(hubId));
		assertEquals("Nothing else is registered", 1, allIds.size());
		listener.halting();
	}
	
	public void testShutDown() {
		PlasticHubImpl hub = new PlasticHubImpl(executor , idGenerator,  rmi, web, new PrettyPrinterImpl(browser), config);
		hub.start();
        hub.registerSelf("hub", new Vector(), pretendHub);
		TestListener2 client = new TestListener2();
		List messages = new ArrayList();
		messages.add(HubMessageConstants.HUB_STOPPING_EVENT);
		hub.registerRMI("client", messages, client);
		hub.halting();
		assertEquals(HubMessageConstants.HUB_STOPPING_EVENT, client.getMessage());
		assertEquals(hub.getHubId(), client.getSender());
		
	}
	


}
