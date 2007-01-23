/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import junit.framework.TestCase;

import org.astrogrid.acr.ivoa.Registry;
import org.easymock.MockControl;

/**  @todo need a mock implementation of recordManager -
 *  an in memory one, or something?
 * 
 * @author Noel Winstanley
 * @since Jun 13, 20065:12:03 PM
 */
public class MessageRecorderImplUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		mControl = MockControl.createControl(MessagingInternal.class);
		m = (MessagingInternal)mControl.getMock();
		storeControl = MockControl.createControl(StoreInternal.class);
		store = (StoreInternal)storeControl.getMock();
		regControl = MockControl.createControl(Registry.class);
		reg = (Registry)regControl.getMock();
	//	mr = new MessageRecorderImpl(m,store,reg);
	}
	protected MockControl mControl;
	protected MessagingInternal m;
	protected MockControl storeControl;
	protected StoreInternal store;
	protected MockControl regControl;
	protected Registry reg;
	protected MessageRecorderImpl mr;
	
	public void testReminder() {
		System.err.println("Implement tests for " + this.getClass().getName());
	}

}
