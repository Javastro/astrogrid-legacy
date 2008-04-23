/**
 * 
 */
package org.astrogrid.desktop.alternatives;

import junit.framework.TestCase;

import org.astrogrid.desktop.modules.system.BackgroundExecutor;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import static org.easymock.EasyMock.*;

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
		r = createMock("runnable",Runnable.class);
	}
	
	protected BackgroundExecutor be;
	protected Runnable r;
	
	
	/* create a headless ui to assist us with constructing a background worker
	 * Test method for 'org.astrogrid.desktop.alternatives.InThreadExecutor.executeWorker(BackgroundWorker)'
	 */
	public void testExecuteWorker() {
		r.run();
		UIContext cxt= createNiceMock("context",UIContext.class);
		replay(cxt,r);
		UIComponent parent = new HeadlessUIComponent("testing",cxt);
		BackgroundWorker bw = new BackgroundWorker(parent,"test") {

			protected Object construct() throws Exception {
				r.run();
				return null;
			}
		};
		be.executeWorker(bw);
		verify(cxt,r);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.InThreadExecutor.interrupt(Runnable)'
	 */
	public void testInterrupt() {
	    replay(r);		
		be.interrupt(r);
		verify(r);
		
	}

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.InThreadExecutor.execute(Runnable)'
	 */
	public void testExecute() throws InterruptedException {
		r.run();
		replay(r);		
		be.execute(r);
		verify(r);
	}
	
	public void testExecuteThrows() throws InterruptedException {
		r.run();
		expectLastCall().andThrow(new RuntimeException("designed to fail"));
		replay(r);			
		be.execute(r);
		verify(r);
	}
	


}
