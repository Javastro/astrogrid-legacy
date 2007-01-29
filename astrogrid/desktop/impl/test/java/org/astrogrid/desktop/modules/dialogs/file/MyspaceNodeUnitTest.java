/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.file;

import java.net.URI;
import java.rmi.RemoteException;

import junit.framework.TestCase;

import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.client.NodeMetadata;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.store.Ivorn;
import org.easymock.MockControl;

import uk.ac.starlink.connect.Branch;

/** Unit test - verifies the most trivial functionality of this class.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 25, 200711:13:49 AM
 */
public class MyspaceNodeUnitTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		nodeControl = MockControl.createControl(FileManagerNode.class);
		node = (FileManagerNode)nodeControl.getMock();
		
		branchControl = MockControl.createControl(Branch.class);
		branch = (Branch)branchControl.getMock();
		
		msnode = createMyspaceNode();
	}

	/**
	 * factory method to create the node to test.
	 */
	protected MyspaceNode createMyspaceNode() {
		return new MyspaceNode(node,branch);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		nodeControl = null;
		node = null;
		
		branchControl = null;
		branch = null;
		
		msnode = null;
	}
	protected MockControl nodeControl;
	protected FileManagerNode node;

	
	protected MockControl branchControl;
	protected Branch branch;
	
	protected MyspaceNode msnode;

	public void testGetName() throws Exception {
		node.getName();
		nodeControl.setReturnValue("mock");
		replay();
		assertEquals("mock",msnode.getName());
		verify();
		
	}
	
	public void testGetNameNullNode() throws Exception {
		msnode.setNode(null);
		assertNull(msnode.getName());
	}
	
	public void testGetParent() throws Exception {
		replay();
		assertNotNull(msnode.getParent());
		assertSame(branch,msnode.getParent());
		
		verify();
	}
	
	public void testGetNode() throws Exception {
		replay();
		assertNotNull(msnode.getNode());
		assertSame(node,msnode.getNode());
	
		verify();
	}
	
	public void testSetNode() throws Exception {
		replay();
		msnode.setNode(null); //@todo should this be trapped?
		assertNull(msnode.getNode());
		msnode.setNode(node);
		assertSame(node,msnode.getNode());
		
		verify();
	}
	
	
	public void testGetURI() throws Exception {
		node.getIvorn();
		final String uri = "ivo://foo/bar";
		final Ivorn ivo = new Ivorn(uri);
		nodeControl.setReturnValue(ivo);
		replay();
		final URI uri2 = msnode.getURI();
		assertNotNull(uri2);
		assertEquals(uri,uri2.toString());
		
		verify();
	}
	
	public void testGetURINullNode() throws Exception {
		msnode.setNode(null);
		assertNull(msnode.getURI());
		
	
	}
	
	public void testGetURINodeException() throws Exception {
		node.getIvorn();
		Throwable t = new RemoteException();
		nodeControl.setThrowable(t);
		replay();
		try {
			msnode.getURI();
		} catch (RemoteException t1) {
			// throws same exception, without translation.
			assertSame(t,t1);
		}
		
		verify();		
	}
	
	// verify it returns the string form of the uri.
	public void testToString() throws Exception {
		node.getIvorn();
		final String uri = "ivo://foo/bar";
		final Ivorn ivo = new Ivorn(uri);
		nodeControl.setDefaultReturnValue(ivo);
		replay();
		assertEquals(msnode.getURI().toString(),msnode.toString());
		
		verify();		
		
	}

	public void testtoStringNullNode() throws Exception {
		msnode.setNode(null);
		assertNotNull(msnode.toString());
		assertEquals("no filemanager node set",msnode.toString());
		
	}
	
	public void testToStringNodeException() throws Exception {
		node.getIvorn();
		Throwable t = new RemoteException();
		nodeControl.setThrowable(t);
		MockControl metadataMock = MockControl.createControl(NodeMetadata.class);
		NodeMetadata metadata = (NodeMetadata)metadataMock.getMock();
		node.getMetadata();
		nodeControl.setReturnValue(metadata);
		metadata.getNodeIvorn();
		String uri = "ivo://foo/bar/choo";
		metadataMock.setReturnValue(new NodeIvorn(uri));
		
		replay();
		metadataMock.replay();
			String s = msnode.toString();
			assertNotNull(s);
			assertEquals(uri,s);

		
		verify();		
		metadataMock.verify();
	}
	
	/**
	 * 
	 */
	protected void replay() {
		branchControl.replay();
		nodeControl.replay();
	}

	/**
	 * 
	 */
	protected void verify() {
		branchControl.verify();
		nodeControl.verify();
	}
	
	
	
	
}
