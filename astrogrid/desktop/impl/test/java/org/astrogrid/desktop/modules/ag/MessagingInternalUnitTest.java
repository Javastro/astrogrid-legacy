/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import junit.framework.TestCase;

import org.astrogrid.desktop.modules.ag.MessagingInternal.MessageListener;
import org.astrogrid.desktop.modules.ag.MessagingInternal.SourcedExecutionMessage;
import org.easymock.MockControl;

/**
 * @author Noel Winstanley
 * @since Jun 13, 20065:19:21 PM
 */
public class MessagingInternalUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		m = new MessagingImpl();
		listenerControl = MockControl.createControl(MessageListener.class);
		listener = (MessageListener)listenerControl.getMock();
		msg = new SourcedExecutionMessage(null,null,null,null,null);
		
	}
	protected void tearDown() throws Exception {
		super.tearDown();
		m = null;
		listenerControl = null;
		listener = null;
		msg = null;
	}
	protected MessagingImpl m;
	protected MockControl listenerControl;
	protected MessageListener listener;
	protected SourcedExecutionMessage msg;
	
	public void testAddRemoveListener() {
		listenerControl.replay();
		m.addEventProcessor(listener);
		m.removeEventProcessor(listener);
	}
	
	public void testInjectMessage() throws InterruptedException {
		listener.onMessage(msg);
		listenerControl.setVoidCallable();
		listenerControl.replay();
		m.addEventProcessor(listener);
		m.injectMessage(msg);
		Thread.sleep(1000);		
		m.removeEventProcessor(listener);
		m.injectMessage(msg);
		Thread.sleep(1000);
		listenerControl.verify();
	}
	/** test that if one of the listeners gets indigestion, the rest of the system continues uninterrupted 
	 * @throws InterruptedException */
	public void testInjectMessageThrowsNasty() throws InterruptedException {
		listener.onMessage(msg);
		listenerControl.setThrowable(new RuntimeException("designed to fail"),1);
		listenerControl.setVoidCallable(1);
		listenerControl.replay();
		m.addEventProcessor(listener);
		m.injectMessage(msg);
		Thread.sleep(1000);		
		m.injectMessage(msg);
		Thread.sleep(1000);
		listenerControl.verify();		
	}
	
	
	public void testInjectFiltersRubbish() throws InterruptedException {
		// not expecting to see anything.
		listenerControl.replay();
		m.addEventProcessor(listener);
		m.injectMessage(null);
		Thread.sleep(1000);		
		listenerControl.verify();		
	}

}
