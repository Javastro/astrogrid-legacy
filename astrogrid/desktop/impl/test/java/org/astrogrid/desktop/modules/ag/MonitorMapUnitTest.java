/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.acr.astrogrid.RemoteProcessListener;
import org.astrogrid.desktop.modules.ag.RemoteProcessManagerImpl.MonitorMap;
import static org.easymock.EasyMock.*;

import junit.framework.TestCase;
import static org.astrogrid.Fixture.*;
/** Test for the monitor map - datastructure within the remote process manager.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 12, 20073:08:55 PM
 */
public class MonitorMapUnitTest extends TestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		this.mm = new RemoteProcessManagerImpl.MonitorMap();
		FileSystemManager vfs = createVFS();
		this.rpm1 = new TestRemoteProcessMonitor(vfs);
		this.rpm2 = new TestRemoteProcessMonitor(vfs);
		rpm1.start();
		rpm2.start();
	}

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		mm = null;
		rpm1 = null;
		rpm2 = null;
	}
	
	protected MonitorMap mm;
	protected AbstractProcessMonitor rpm1;
	protected AbstractProcessMonitor rpm2;
	public void testListKeysEmpty() throws Exception {
		java.net.URI[] u = mm.listKeys();
		assertNotNull(u);
		assertEquals(0,u.length);
	}
	
	public void testListKeysSingleton() throws Exception {
		mm.add(rpm1);
		URI[] u = mm.listKeys();
		assertNotNull(u);
		assertEquals(1,u.length);
		assertEquals(rpm1.getId(),u[0]);
	}
	
	public void testListKeysMany() throws Exception {
		mm.add(rpm1);
		mm.add(rpm2);
		URI[] u = mm.listKeys();
		assertNotNull(u);
		assertEquals(2,u.length);
		assertEquals(rpm1.getId(),u[0]); // order of insertion is preserved.
		assertEquals(rpm2.getId(),u[1]);
	}
	
	public void testRemove() throws Exception {
		mm.add(rpm1);
		mm.remove(rpm1.getId());
		assertNull(mm.get(rpm1.getId()));
		testListKeysEmpty();
	}
	
	public void testRemove1() throws Exception {
		mm.add(rpm1);
		mm.add(rpm2);
		mm.remove(rpm1.getId());
		assertNull(mm.get(rpm1.getId()));
	}
	
	public void testGet() throws Exception {
		mm.add(rpm1);
		mm.add(rpm2);
		assertSame(rpm1,mm.get(rpm1.getId()));
		assertSame(rpm2,mm.get(rpm2.getId()));
		
	}
	
	public void testWildcardListening() throws Exception {
		// first, create a mock ...
		RemoteProcessListener rpl =createMock(RemoteProcessListener.class);
		// specify what methods we expect to be called, with what parameters.
		rpl.statusChanged(rpm1.getId(),"hi");
		rpl.resultsReceived(rpm2.getId(),new HashMap());
		replay(rpl);
		// test listening to existing processes.
		mm.add(rpm1);
		mm.addWildcardListener(rpl);
		rpm1.fireStatusChanged("hi");
		
		// test listening to new processes.
		mm.add(rpm2);
		rpm2.fireResultsReceived(new HashMap());
		
		// test removing a global listener.
		mm.removeWildcardListener(rpl);
		rpm1.fireResultsReceived(null); // not expected
		rpm2.fireStatusChanged("ello");
		
		verify(rpl);
	}
	
	
	// while we're here, test RemoteProcessMonitor too.
	public void testRemoteProcessMonitor() throws Exception {
		// first, create a mock ...
	    
		RemoteProcessListener rpl = createMock(RemoteProcessListener.class);
		// specify what methods we expect to be called, with what parameters.
		rpl.statusChanged(rpm1.getId(),"hi");
		rpl.resultsReceived(rpm1.getId(),new HashMap());
		ExecutionMessage execMessage = new ExecutionMessage("foo","bar","stat",new Date(),"wibble");
		rpl.messageReceived(rpm1.getId(),execMessage);
		replay(rpl);
		
		// now do the test.
		
		rpm1.addRemoteProcessListener(rpl);
		rpm1.fireStatusChanged("hi");
		rpm1.removeRemoteProcessListener(rpl);
		rpm1.fireStatusChanged("boo"); // not expected.
		
		rpm1.addRemoteProcessListener(rpl);
		rpm1.fireResultsReceived(new HashMap());
		rpm1.fireMessageReceived(execMessage);
		
		rpm1.cleanUp();
		rpm1.fireStatusChanged("ugh"); // not expected.
		
		
		verify(rpl);
		
	}
	
	public static class TestRemoteProcessMonitor extends AbstractProcessMonitor {


        /**
         * @param vfs
         */
        public TestRemoteProcessMonitor(FileSystemManager vfs) {
            super(vfs);
        }

        private static int count = 0;


		@Override
        public ExecutionInformation getExecutionInformation()
				throws NotFoundException, InvalidArgumentException,
				ServiceException, SecurityException {
			return new ExecutionInformation(null,null,null,null,null,null);
		}

		@Override
        public ExecutionMessage[] getMessages() throws NotFoundException,
				ServiceException {
			return new ExecutionMessage[]{};
		}

		@Override
        public Map getResults() throws ServiceException, SecurityException,
				NotFoundException, InvalidArgumentException {
			return new HashMap();
		}

		public void halt() throws NotFoundException, InvalidArgumentException,
				ServiceException, SecurityException {
		}

        public void start(URI serviceId) {
            this.start();
        }

        public void start() {
            setId(URI.create("testmonitor://" + (count++)));
        }

        public void refresh() throws ServiceException {
        }
	}

}
