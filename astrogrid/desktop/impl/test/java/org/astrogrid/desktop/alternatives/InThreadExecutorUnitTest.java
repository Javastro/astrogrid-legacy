/**
 * 
 */
package org.astrogrid.desktop.alternatives;

import junit.framework.TestCase;

import org.astrogrid.desktop.modules.system.BackgroundExecutor;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.easymock.MockControl;

/** test for the in thread executor.
 * @author Noel Winstanley
 * @since Jun 6, 20061:42:01 PM
 */
public class InThreadExecutorUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		this.be = new InThreadExecutor();
		rControl = MockControl.createControl(Runnable.class);
		r = (Runnable)rControl.getMock();
	}
	
	protected BackgroundExecutor be;
	protected MockControl rControl;
	protected Runnable r;
	
	
	/* create a headless ui to assist us with constructing a background worker
	 * Test method for 'org.astrogrid.desktop.alternatives.InThreadExecutor.executeWorker(BackgroundWorker)'
	 */
	public void testExecuteWorker() {
		r.run();
		rControl.replay();
		UIInternal parent = new HeadlessUI("testing",be);
		BackgroundWorker bw = parent.wrap(r);
		be.executeWorker(bw);
		rControl.verify();
	}

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.InThreadExecutor.interrupt(Runnable)'
	 */
	public void testInterrupt() {
		rControl.replay();		
		be.interrupt(r);
		rControl.verify();
		
	}

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.InThreadExecutor.execute(Runnable)'
	 */
	public void testExecute() throws InterruptedException {
		r.run();
		rControl.replay();			
		be.execute(r);
		rControl.verify();
	}
	
	public void testExecuteThrows() throws InterruptedException {
		r.run();
		rControl.setThrowable(new RuntimeException("designed to fail"));
		rControl.replay();			
		be.execute(r);
		rControl.verify();
	}
	


}
