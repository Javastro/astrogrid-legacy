package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.HubMessageConstants;
import org.votech.plastic.PlasticListener;

public class RMIListenerTest extends PresetupHub {
	private List sampleArgs;
	private URI message = URI.create("ivo://message");
	private URI test = URI.create("ivo://junit");
	private List oneMessage;


	public void testSend() {

		
		PlasticListener listener = new TestListener("listener1");

		URI listenerId = hub.registerRMI("listener1", oneMessage, listener);
		

		Map results = (Map) hub.request(test, message, sampleArgs);
		Object result = results.get(listenerId);
		assertEquals(result,"listener1"+test+message+"2");
		
		//test new getMessages method
		assertEquals(oneMessage, hub.getUnderstoodMessages(listenerId));
	}
	
	public void testSelectiveListeners() {

		URI message1 = URI.create("ivo://message1");
		URI message2 = URI.create("ivo://message2");
		
		PlasticListener listener1 = new TestListener("listener1");
		URI listenerId1 = hub.registerRMI("listener1", CommonMessageConstants.EMPTY, listener1);
		PlasticListener listener2 = new TestListener("listener2");
		List messages = new ArrayList();
		messages.add(message1);
		URI listenerId2 = hub.registerRMI("listener2", messages, listener2);		
		
		//test new getMessages method
		assertEquals(messages, hub.getUnderstoodMessages(listenerId2));

		Map results = (Map) hub.request(test, message1, sampleArgs);
		assertEquals(1, results.size());
		Object result1 = results.get(listenerId1);
		assertNull(result1);
		Object result2 = results.get(listenerId2);
		assertEquals(result2,"listener2"+test+message1+"2");
		
		Map results2 = (Map) hub.request(test, message2, sampleArgs);
		assertEquals(0, results2.size());
		assertFalse(results2.containsKey(listenerId2));
	}
	
	public void testGetMessagesForUnknownId() {
		//Expect an empty list back if the app isn't registered
		URI listenerId = URI.create("ivo://imadethisup");
		assertEquals(CommonMessageConstants.EMPTY, hub.getUnderstoodMessages(listenerId ));
	}
	
	public void testSendToSubset() {
		
		PlasticListener listener1 = new TestListener("listener1");
		URI listenerId1 = hub.registerRMI("listener1", oneMessage, listener1);
		PlasticListener listener2 = new TestListener("listener2");
		URI listenerId2 = hub.registerRMI("listener2", oneMessage, listener2);		
		

		List recipients = new ArrayList();
		recipients.add(listenerId1);
		
		Map results = (Map) hub.requestToSubset(test, message, sampleArgs, recipients);
		assertEquals(1, results.size());
		Object result1 = results.get(listenerId1);
		assertEquals(result1,"listener1"+test+message+"2");
		Object result2 = results.get(listenerId2);
		assertNull(result2);
	}
	
	public void testSendAsynch() {

		
		TestListener2 listener = new TestListener2();
		URI listenerId = hub.registerRMI("listener2", oneMessage, listener);
		
		assertEquals("listener2", hub.getName(listenerId));
		

		hub.requestAsynch(test, message, sampleArgs);

		assertEquals(message, listener.getMessage());
	}
	
	public void testSendToSubsetAsynch() {

		
		TestListener2 listener1 = new TestListener2();
		hub.registerRMI("listener1", oneMessage, listener1);
		
		TestListener2 listener2 = new TestListener2();
		URI listenerId2 = hub.registerRMI("listener2", oneMessage, listener2);
		
		List subset = new ArrayList();
		subset.add(listenerId2);
		hub.requestToSubsetAsynch(test, message, sampleArgs, subset);

		assertNotSame(message, listener1.getMessage());
		assertEquals(message, listener2.getMessage());
	}
	
	public void setUp() {
		super.setUp();
		sampleArgs = new ArrayList();
		sampleArgs.add("a");
		sampleArgs.add(new Object());
		oneMessage = new ArrayList();
		oneMessage.add(message);
	}
	
	public void testGetRegUnregMessage() {
		TestListener2 listener1 = new TestListener2();
		TestListener2 listener2 = new TestListener2();
		
		List messages = new ArrayList();
		messages.add(HubMessageConstants.APPLICATION_REGISTERED_EVENT);
		messages.add(HubMessageConstants.APPLICATION_UNREGISTERED_EVENT);
		
		
		URI id1 = hub.registerRMI("listener1", messages, listener1);
		URI id2 = hub.registerRMI("listener2", messages, listener2);
		
		assertEquals(HubMessageConstants.APPLICATION_REGISTERED_EVENT, listener1.getMessage());
		assertEquals(id2.toString(), listener1.getArgs().get(0));
		assertEquals(hub.getHubId(), listener1.getSender());
		
		hub.unregister(id1);
		
		assertEquals(HubMessageConstants.APPLICATION_UNREGISTERED_EVENT, listener2.getMessage());
		assertEquals(id1.toString(), listener2.getArgs().get(0));
		assertEquals(hub.getHubId(), listener2.getSender());
	}
	
	

}
