/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.acr.astrogrid.RemoteProcessManager;
import org.astrogrid.desktop.modules.system.SnitchInternal;
import  static org.easymock.EasyMock.*;
import org.w3c.dom.Document;


import static org.astrogrid.Fixture.*;
/** 
 * @author Noel Winstanley
 * @since Jun 13, 20067:01:00 PM
 * @TEST extend to covermore of abstract process monitor.
 */
public class RemoteProcessManagerUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	@Override
    protected void setUp() throws Exception {
		super.setUp();
		strats = new ArrayList();
		strats.add(new TestRemoteProcessStrategy());
		SnitchInternal snitch = createNiceMock("snitch",SnitchInternal.class);
		rpm = new RemoteProcessManagerImpl(strats,createVFS(),snitch);
		
		mocdoc = createMock("document",Document.class);
		replay(snitch,mocdoc);
	}
 @Override
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


        public ProcessMonitor create(Document doc)
                throws InvalidArgumentException, ServiceException {
            try {
                return new MonitorMapUnitTest.TestRemoteProcessMonitor(VFS.getManager());
            } catch (FileSystemException x) {
                throw new ServiceException(x);
            }
        }
	}
	
}
