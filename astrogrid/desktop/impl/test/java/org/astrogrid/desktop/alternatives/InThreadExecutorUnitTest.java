/**
 * 
 */
package org.astrogrid.desktop.alternatives;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import static org.easymock.EasyMock.*;
import static org.astrogrid.Fixture.*;

/** test for the in thread executor.
 * test it within the mock ui context - want to ensure threads and workers
 * are behaving before going on to test anything else.
 * @author Noel Winstanley
 * @since Jun 6, 20061:42:01 PM
 */
public class InThreadExecutorUnitTest extends TestCase {

	private UIContext cxt;

    /** test class that records the threads that each step of the worker is executed upon.
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Apr 24, 20087:24:54 AM
     */
    private final class ThreadRecordingBackgroundWorker extends
            BackgroundWorker {
        public Thread constructThread;
        public Thread finishedThread;
        public Thread alwaysThread;

        /**
         * @param parent
         * @param msg
         */
        private ThreadRecordingBackgroundWorker(UIComponent parent, String msg) {
            super(parent, msg);
        }

        @Override
        protected Object construct() throws Exception {
            
            this.constructThread = Thread.currentThread();
            return null;
        }

        @Override
        protected void doFinished(Object result) {
            this.finishedThread = Thread.currentThread();
        }

        @Override
        protected void doAlways() {
            this.alwaysThread = Thread.currentThread();
        }
    }

    /*
	 * @see TestCase#setUp()
	 */
	@Override
    protected void setUp() throws Exception {
		super.setUp();
		this.cxt = createMockContext();
		r = createMock("runnable",Runnable.class);
	}
	
	protected Runnable r;
	
	
	/* create a headless ui to assist us with constructing a background worker
	 * Test method for 'org.astrogrid.desktop.alternatives.InThreadExecutor.executeWorker(BackgroundWorker)'
	 */
	public void testExecuteWorker()  throws Exception{
		r.run();
		replay(cxt,r);
		UIComponent parent = new HeadlessUIComponent("testing",cxt);
		BackgroundWorker bw = new BackgroundWorker(parent,"test") {

			@Override
            protected Object construct() throws Exception {
				r.run();
				return null;
			}
		};
		bw.start();
		verify(cxt,r);
	}

	/** test to try to get threads all in a line
	 * want to have all parts of the background worker executed on the invoking thread
	 * 
	 * @throws Exception
	 */
	public void testAllPartsOfBackgroundWorkerExecuteOnSameThreadFromStart() throws Exception {
        replay(cxt);
        UIComponent parent = new HeadlessUIComponent("testing",cxt);
        ThreadRecordingBackgroundWorker bw = new ThreadRecordingBackgroundWorker(parent, "test");
        bw.start();
        assertNotNull(bw.constructThread);
        // with start, fails here - construct happens on EDT
        assertSame("construct not on lanunching thread",Thread.currentThread(),bw.constructThread);
        assertNotNull(bw.finishedThread);
        assertNotNull(bw.alwaysThread);
        assertSame("always and finished not on same thread",bw.alwaysThread,bw.finishedThread);
        assertSame("construct and finished not on same thread",bw.constructThread,bw.finishedThread);
        verify(cxt);        
    }
	
	/** any different when we call teh executor directly?? */
	   public void testAllPartsOfBackgroundWorkerExecuteOnSameThreadFromExecute() throws Exception {
	        replay(cxt);
	        UIComponent parent = new HeadlessUIComponent("testing",cxt);
	        ThreadRecordingBackgroundWorker bw = new ThreadRecordingBackgroundWorker(parent, "test");
	        cxt.getExecutor().executeWorker(bw);
	        assertNotNull(bw.constructThread);
	        assertSame("construct not on lanunching thread",Thread.currentThread(),bw.constructThread);
	        assertNotNull(bw.finishedThread);
	        assertNotNull(bw.alwaysThread);
	        assertSame("construct and finished not on same thread",bw.constructThread,bw.finishedThread);
	        // with execute, we get further, but finished is then run on edt.
	        assertSame("always and finished not on same thread",bw.alwaysThread,bw.finishedThread);
	        verify(cxt);        
	    }
	
	   /** I've altered background worker so that assertionErrors
	    * and junitAssertsions 
	    * thrown in construct are not trapped - but propagate upwards.
	    * this is only useful in testing, when running witrh
	    * in-thread exec.
	    * 
	    *  */
	    public void testAssertionErrorInConstructShouldFailTest()  throws Exception{	      
	        replay(cxt);
	        UIComponent parent = new HeadlessUIComponent("testing",cxt);
	        BackgroundWorker bw = new BackgroundWorker(parent,"test") {

	            @Override
                protected Object construct() throws Exception {
	              throw new AssertionError("expected to propagage");
	            }
	        };
	        try {
	            bw.start();
	            fail("failure didn't propagate");
	        } catch (AssertionError e) {
	            // expected
	        }	        
	        verify(cxt);
	    }
        public void testAssertionFailedErrorInConstructShouldFailTest()  throws Exception{         
            replay(cxt);
            UIComponent parent = new HeadlessUIComponent("testing",cxt);
            BackgroundWorker bw = new BackgroundWorker(parent,"test") {

                @Override
                protected Object construct() throws Exception {
                    fail("expected");
                    return null;
                }
            };
            try {
                bw.start();
                fail("failure didn't propagate");
            } catch (AssertionFailedError e) {
                // expected
            }           
            verify(cxt);
        }
        public void testAssertionErrorInDoFinishedShouldFailTest()  throws Exception{       
            replay(cxt);
            UIComponent parent = new HeadlessUIComponent("testing",cxt);
            BackgroundWorker bw = new BackgroundWorker(parent,"test") {

                @Override
                protected Object construct() throws Exception {
                    return null;
                }
                @Override
                protected void doFinished(Object result) {
                    throw new AssertionError("expected to propagage");
                }
            };
            try {
                bw.start();
                fail("failure didn't propagate");
            } catch (AssertionError e) {
                // expected
            }           
            verify(cxt);
        }
        public void testAssertionFailedErrorInDoFinishedShouldFailTest()  throws Exception{         
            replay(cxt);
            UIComponent parent = new HeadlessUIComponent("testing",cxt);
            BackgroundWorker bw = new BackgroundWorker(parent,"test") {

                @Override
                protected Object construct() throws Exception {
                    return null;
                }
                @Override
                protected void doFinished(Object result) {
                    fail("expected");
                }
            };
            try {
                bw.start();
                fail("failure didn't propagate");
            } catch (AssertionFailedError e) {
                // expected
            }           
            verify(cxt);
        }        
        
	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.InThreadExecutor.interrupt(Runnable)'
	 */
	public void testInterrupt() {
	    replay(r,cxt);		
		cxt.getExecutor().interrupt(r);
		verify(r,cxt);
		
	}

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.InThreadExecutor.execute(Runnable)'
	 */
	public void testExecute() throws InterruptedException {
		r.run();
		replay(r,cxt);		
		cxt.getExecutor().execute(r);
		verify(r,cxt);
	}
	
	public void testExecuteThrows() throws InterruptedException {
		r.run();
		expectLastCall().andThrow(new RuntimeException("designed to fail"));
		replay(r,cxt);			
        cxt.getExecutor().execute(r);
		verify(r,cxt);
	}
	


}
