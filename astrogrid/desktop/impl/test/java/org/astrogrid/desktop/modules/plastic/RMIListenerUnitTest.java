package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.RmiServer;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.common.namegen.InMemoryNameGen;
import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.HubMessageConstants;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.PlasticListener;
import org.votech.plastic.incoming.handlers.MessageHandler;
import org.votech.plastic.incoming.handlers.StandardHandler;

import EDU.oswego.cs.dl.util.concurrent.DirectExecutor;
// TODO refactor this
public class RMIListenerUnitTest extends AbstractPlasticBase {

    protected PlasticHubListener hub1;

    public void setUp() throws Exception {
        
        
        hub1 = createHubWithMocks();
        ((PlasticHubImpl)hub1).start(); 
        
        sampleArgs = new ArrayList();
        sampleArgs.add("a");
        sampleArgs.add(new Object());
        oneMessage = new ArrayList();
        oneMessage.add(message);
        super.setUp();
    }
    
    public void tearDown() throws Exception {
        super.tearDown();
        ((PlasticHubImpl)hub1).halting();
        hub1 = null;
    }
    
    
    // TODO stick this somewhere else
    private PlasticHubListener createHubWithMocks() {
        DirectExecutor executor = new DirectExecutor();
        InMemoryNameGen idGenerator = new InMemoryNameGen();
        WebServer web = new WebServer() {

            public String getUrlRoot() {
                return "http://127.0.0.1:" + getPort() + "/" + getKey() + "/";
            }

            public String getKey() {
                return "foobar";
            }

            public int getPort() {
                return 8001;
            }

        };
        RmiServer rmi = new RmiServer() {

            public int getPort() {
                return 1234;
            }

        };

        Configuration config = new Configuration() {
            Map store = new HashMap();
            public boolean setKey(String arg0, String arg1) {
                store.put(arg0,arg1);
                return true;
            }

            public String getKey(String arg0) {
                return (String) store.get(arg0);
            }

            public String[] listKeys() throws ACRException {
                return (String[]) store.keySet().toArray(new String[]{});
            }

            public Map list() throws ACRException {
                return store;
            }

            public void removeKey(String arg0) {
                store.remove(arg0);
            }
            
        };
        
        Shutdown shutdown = new Shutdown() {

            public void halt() {
                // TODO Auto-generated method stub
                
            }

            public void reallyHalt() {
                // TODO Auto-generated method stub
                
            }

            public void addShutdownListener(ShutdownListener arg0) {
                // TODO Auto-generated method stub
                
            }

            public void removeShutdownListener(ShutdownListener arg0) {
                // TODO Auto-generated method stub
                
            }
            
        };
        PlasticHubImpl impl = new PlasticHubImpl(executor , idGenerator,   rmi, web, new PrettyPrinterImpl(null), config);
        final MessageHandler internalhandler = new StandardHandler("Astro Runtime","description","ivo://foo","http://news.bbc.co.uk",PlasticListener.CURRENT_VERSION);
        impl.registerSelf("Astro Runtime", internalhandler.getHandledMessages(), new PlasticListener() {
           public Object perform(URI arg0, URI arg1, List arg2) {
                return internalhandler.perform(arg0,arg1,arg2);
            }
            
        });
        
        return impl;
    }
    
    
    private List sampleArgs;
	private URI message = URI.create("ivo://message");
	private URI test = URI.create("ivo://junit");
	private List oneMessage;


	public void testSend() {

		
		PlasticListener listener = new TestListener("listener1");

		URI listenerId = hub1.registerRMI("listener1", oneMessage, listener);
		

		Map results = (Map) hub1.request(test, message, sampleArgs);
		Object result = results.get(listenerId);
		assertEquals(result,"listener1"+test+message+"2");
		
		//test new getMessages method
		assertEquals(oneMessage, hub1.getUnderstoodMessages(listenerId));
	}
	
	public void testSelectiveListeners() {

		URI message1 = URI.create("ivo://message1");
		URI message2 = URI.create("ivo://message2");
		
		PlasticListener listener1 = new TestListener("listener1");
		URI listenerId1 = hub1.registerRMI("listener1", CommonMessageConstants.EMPTY, listener1);
		PlasticListener listener2 = new TestListener("listener2");
		List messages = new ArrayList();
		messages.add(message1);
		URI listenerId2 = hub1.registerRMI("listener2", messages, listener2);		
		
		//test new getMessages method
		assertEquals(messages, hub1.getUnderstoodMessages(listenerId2));

		Map results = (Map) hub1.request(test, message1, sampleArgs);
		assertEquals(1, results.size());
		Object result1 = results.get(listenerId1);
		assertNull(result1);
		Object result2 = results.get(listenerId2);
		assertEquals(result2,"listener2"+test+message1+"2");
		
		Map results2 = (Map) hub1.request(test, message2, sampleArgs);
		assertEquals(0, results2.size());
		assertFalse(results2.containsKey(listenerId2));
	}
	
	public void testGetMessagesForUnknownId() {
		//Expect an empty list back if the app isn't registered
		URI listenerId = URI.create("ivo://imadethisup");
		assertEquals(CommonMessageConstants.EMPTY, hub1.getUnderstoodMessages(listenerId ));
	}
	
	public void testSendToSubset() {
		
		PlasticListener listener1 = new TestListener("listener1");
		URI listenerId1 = hub1.registerRMI("listener1", oneMessage, listener1);
		PlasticListener listener2 = new TestListener("listener2");
		URI listenerId2 = hub1.registerRMI("listener2", oneMessage, listener2);		
		

		List recipients = new ArrayList();
		recipients.add(listenerId1);
		
		Map results = (Map) hub1.requestToSubset(test, message, sampleArgs, recipients);
		assertEquals(1, results.size());
		Object result1 = results.get(listenerId1);
		assertEquals(result1,"listener1"+test+message+"2");
		Object result2 = results.get(listenerId2);
		assertNull(result2);
	}
	
	public void testSendAsynch() {

		
		TestListener2 listener = new TestListener2();
		URI listenerId = hub1.registerRMI("listener2", oneMessage, listener);
		
		assertEquals("listener2", hub1.getName(listenerId));
		

		hub1.requestAsynch(test, message, sampleArgs);

		assertEquals(message, listener.getMessage());
	}
	
	public void testSendToSubsetAsynch() {

		
		TestListener2 listener1 = new TestListener2();
		hub1.registerRMI("listener1", oneMessage, listener1);
		
		TestListener2 listener2 = new TestListener2();
		URI listenerId2 = hub1.registerRMI("listener2", oneMessage, listener2);
		
		List subset = new ArrayList();
		subset.add(listenerId2);
		hub1.requestToSubsetAsynch(test, message, sampleArgs, subset);

		assertNotSame(message, listener1.getMessage());
		assertEquals(message, listener2.getMessage());
	}
	

	
	public void testGetRegUnregMessage() {
		TestListener2 listener1 = new TestListener2();
		TestListener2 listener2 = new TestListener2();
		
		List messages = new ArrayList();
		messages.add(HubMessageConstants.APPLICATION_REGISTERED_EVENT);
		messages.add(HubMessageConstants.APPLICATION_UNREGISTERED_EVENT);
		
		
		URI id1 = hub1.registerRMI("listener1", messages, listener1);
		URI id2 = hub1.registerRMI("listener2", messages, listener2);
		
		assertEquals(HubMessageConstants.APPLICATION_REGISTERED_EVENT, listener1.getMessage());
		assertEquals(id2.toString(), listener1.getArgs().get(0));
		assertEquals(hub1.getHubId(), listener1.getSender());
		
		hub1.unregister(id1);
		
		assertEquals(HubMessageConstants.APPLICATION_UNREGISTERED_EVENT, listener2.getMessage());
		assertEquals(id1.toString(), listener2.getArgs().get(0));
		assertEquals(hub1.getHubId(), listener2.getSender());
	}

    protected PlasticHubListener getHub() throws Exception {
        return hub1;
    }

    protected TestPlasticApplication getApplication(Properties appData) {
        return new TestListenerRMI(appData);
    }
	
	

}
