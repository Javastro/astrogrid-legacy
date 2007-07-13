/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.acr.astrogrid.RemoteProcessManager;
import org.astrogrid.desktop.modules.system.SnitchInternal;
import org.easymock.MockControl;
import org.w3c.dom.Document;

import junit.framework.TestCase;

/** @implement
 * @author Noel Winstanley
 * @since Jun 13, 20067:01:00 PM
 */
public class RemoteProcessManagerUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		strats = new ArrayList();
		strats.add(new TestRemoteProcessStrategy());
		MockControl msControl = MockControl.createNiceControl(MyspaceInternal.class);
		MyspaceInternal ms = (MyspaceInternal)msControl.getMock();
		MockControl snitchControl = MockControl.createNiceControl(SnitchInternal.class);
		SnitchInternal snitch = (SnitchInternal)snitchControl.getMock();
		rpm = new RemoteProcessManagerImpl(strats,ms,snitch);
		
		snitchControl.replay();
		msControl.replay();
		
		MockControl docControl = MockControl.createControl(Document.class);
		mocdoc = (Document)docControl.getMock();
		docControl.replay();
	}
 protected void tearDown() throws Exception {
	super.tearDown();
	strats = null;
	rpm = null;
}
 	protected Document mocdoc;
	protected java.util.List strats;
	protected RemoteProcessManager rpm;
	
	
	public void testList() throws Exception {
		URI[] u = rpm.list();
		assertNotNull(u);
		assertEquals(0,u.length);
	}

	public void testSubmit() throws Exception {
		URI uri = rpm.submit(mocdoc);
		assertNotNull(uri);
		URI[] list = rpm.list();
		assertEquals(1,list.length);
		assertEquals(uri,list[0]);
		rpm.halt(uri);

		ExecutionMessage[] arr = rpm.getMessages(uri);
		assertNotNull(arr);
		assertEquals(0,arr.length);
		
		Map results = rpm.getResults(uri);
		assertNotNull(results);
		assertEquals(0,results.size());
		
		assertNotNull(rpm.getExecutionInformation(uri));		
		
		rpm.delete(uri);
		// verify it's gone.
		try {
			rpm.getMessages(uri);
		} catch (NotFoundException e) {
		}		
	}
	
	public void testSubmitTo() throws Exception {
		URI uri = rpm.submitTo(mocdoc,new URI("test://foo"));
		assertNotNull(uri);
		URI[] list = rpm.list();
		assertEquals(1,list.length);
		assertEquals(uri,list[0]);
		rpm.halt(uri);
		
		ExecutionMessage[] arr = rpm.getMessages(uri);
		assertNotNull(arr);
		assertEquals(0,arr.length);
		
		Map results = rpm.getResults(uri);
		assertNotNull(results);
		assertEquals(0,results.size());
		
		assertNotNull(rpm.getExecutionInformation(uri));
		
		
		rpm.delete(uri);
		
		// verify it's gone.
		try {
			rpm.getMessages(uri);
		} catch (NotFoundException e) {
		}
	}
	
	public void testUnknownHalt() throws Exception {
		URI u = new URI("test://foo");
		try {
			rpm.halt(u);
			fail("expected to chuck");
		} catch(NotFoundException e) {
			// expected
		}
	}
	
	public void testUnknownGetMessages() throws Exception {
		URI u = new URI("test://foo");
		try {
			rpm.getMessages(u);
			fail("expected to chuck");
		} catch(NotFoundException e) {
			// expected
		}
	}
	
	public void testUnknownGetExecutionInformation() throws Exception {
		URI u = new URI("test://foo");
		try {
			rpm.getExecutionInformation(u);
			fail("expected to chuck");
		} catch(NotFoundException e) {
			// expected
		}
	}
	
	public void testUnknownGetResults() throws Exception {
		URI u = new URI("test://foo");
		try {
			rpm.getResults(u);
			fail("expected to chuck");
		} catch(NotFoundException e) {
			// expected
		}
	}
	
	public static class TestRemoteProcessStrategy implements RemoteProcessStrategy {

		public boolean canProcess(URI execId) {
			return true;
		}

		public String canProcess(Document doc) {
			return "ok";
		}

		public ProcessMonitor submit(Document doc)
				throws ServiceException, SecurityException, NotFoundException,
				InvalidArgumentException {
			return new MonitorMapUnitTest.TestRemoteProcessMonitor();
		}

		public ProcessMonitor submitTo(Document doc, URI service)
				throws ServiceException, SecurityException, NotFoundException,
				InvalidArgumentException {
			return new MonitorMapUnitTest.TestRemoteProcessMonitor();
		}
	}
	
}
