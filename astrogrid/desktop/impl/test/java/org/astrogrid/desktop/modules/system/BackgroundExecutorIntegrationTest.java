/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.easymock.MockControl;

/** test the basic functionality of background executor - that we can run, timeout and interrupt processes.
 * Also verifies behaviour of BackgroundWorker - as both classes cooperate quite closely.
 * In particular, this test checks that doAlways(), doError(), doFinished() are called correctly.
 * @author Noel Winstanley
 * @since Jun 9, 20062:34:21 PM
 */
public class BackgroundExecutorIntegrationTest extends InARTestCase {

	/**
	 * @author Noel Winstanley
	 * @since Jun 9, 20062:49:48 PM
	 */
	public static class TestWorker extends BackgroundWorker {
		/**
		 * @param parent
		 * @param msg
		 * @param msecs
		 */
		public TestWorker(UIContext parent, long msecs) {
			super(parent, "test worker", msecs);
		}
		public int construct;
		public int always;
		public int error;
		public Throwable ex;
		public int finished;
		public Object result;
		

		protected Object construct() throws Exception {
			construct++;
			Thread.sleep(10000);
			return null;
		}
		protected void doAlways() {
			always++;
			super.doAlways();
		}
		protected void doError(Throwable ex) {
			error++;
			this.ex =ex;
			
			//super.doError(ex);
		}
		protected void doFinished(Object result) {
			finished++;
			this.result = result;
			super.doFinished(result);
		}
		
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		exec = (BackgroundExecutor)assertComponentExists(BackgroundExecutor.class,"system.executor");
		ui = (UIContext)assertComponentExists(UIContext.class,"system.ui");
		assertNotNull(ui);
	}
	protected BackgroundExecutor exec;
	protected UIContext ui;
	protected void tearDown() throws Exception {
		super.tearDown();
		exec = null;
		ui = null;
	}
	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.BackgroundExecutorImpl.executeWorker(BackgroundWorker)'
	 */
	public void testExecuteWorker() throws InterruptedException {
		final Object expectedResult = new Object();
		TestWorker tw = new TestWorker(ui,1000L) {
			protected Object construct() throws Exception {
				construct++;
				return expectedResult;
			}
		};
		exec.executeWorker(tw);
		Thread.sleep(1000);
		assertEquals(1,tw.construct);
		assertEquals(0,tw.error);
		assertEquals(1,tw.always);
		assertEquals(1,tw.finished);
		assertEquals(expectedResult,tw.result);
	}

	public void testExecuteWorkerException() throws InterruptedException {
		final Exception e = new Exception();
		TestWorker tw = new TestWorker(ui,1000L) {
			protected Object construct() throws Exception {
				construct++;
				throw e;
			}
		};
		exec.executeWorker(tw);
		Thread.sleep(1000);
		assertEquals(1,tw.construct);
		assertEquals(1,tw.error);
		assertEquals(1,tw.always);
		assertEquals(0,tw.finished);
		assertNull(tw.result);
		assertEquals(e,tw.ex);
	}
	
	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.BackgroundExecutorImpl.execute(Runnable)'
	 */
	public void testExecute() throws InterruptedException {
		MockControl rControl = MockControl.createControl(Runnable.class);
		Runnable r = (Runnable)rControl.getMock();
		r.run();
		rControl.replay();
		
		exec.execute(r);
		Thread.sleep(1000);
		rControl.verify();

	}
	
	public void testTimeOut() throws InterruptedException {
		// set timeout to 1s, worker will sleep for 10s, test sleeps for 2s.
		// timeout fails silently - no error, no result., but always is still run.
		TestWorker tw = new TestWorker(ui, 1000L);
		exec.executeWorker(tw);
		Thread.sleep(2000);	
		assertEquals(1,tw.construct);
		assertEquals(0,tw.error);
		assertEquals(1,tw.always);
		assertEquals(0,tw.finished);
		assertNull(tw.result);
		assertNull(tw.ex);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.BackgroundExecutorImpl.interrupt(Runnable)'
	 */
	public void testInterrupt() throws InterruptedException {
		// set timeout to 10s, worker will sleep for 10s, test sleeps for 2s.
		// interrupt - same result as a timeout.
		TestWorker tw = new TestWorker(ui, 10000L);
		exec.executeWorker(tw);
		Thread.sleep(2000);	
		exec.interrupt(tw);
		Thread.sleep(1000);
		assertEquals(1,tw.construct);
		assertEquals(0,tw.error);
		assertEquals(1,tw.always);
		assertEquals(0,tw.finished);
		assertNull(tw.result);
		assertNull(tw.ex);
		
	}
	
	
	
	public static Test suite() {
		return new ARTestSetup(new TestSuite(BackgroundExecutorIntegrationTest.class));
	}

}
