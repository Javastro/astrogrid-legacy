/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.file;

import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.easymock.MockControl;

import uk.ac.starlink.connect.Node;

/**test for a myspace branch.
 * 
 * inherits all tests for baseclass too - to verify that these still behave unchanged.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 25, 200711:56:51 AM
 */
public class MyspaceBranchUnitTest extends MyspaceNodeUnitTest {

	protected void setUp() throws Exception {
		super.setUp();  
		assertTrue(msnode instanceof MyspaceBranch);
		msbranch = (MyspaceBranch)msnode;
		childControls = new MockControl[3];
		children = new FileManagerNode[3];
		for (int i = 0; i < childControls.length; i++) {
			childControls[i] = MockControl.createControl(FileManagerNode.class);
			children[i] = (FileManagerNode)childControls[i].getMock();
		}
	}

	protected MockControl[] childControls;
	protected FileManagerNode[] children;
	
	protected MyspaceBranch msbranch;
	
	protected void tearDown() throws Exception {
		super.tearDown();
		msbranch = null;
		childControls = null;
		children = null;
	}
protected MyspaceNode createMyspaceNode() {
	return new MyspaceBranch(node,branch);
}

	public void testCreateNodeNull() {
		try {	
			msbranch.createNode(null);
			fail("expected to chuck");
		} catch (IllegalArgumentException e) {
		                // ok
		}
		
	}

	public void testCreateNodeNew() throws Exception {
		node.children();
		nodeControl.setReturnValue(enumerateChildren(1));
		
		children[0].isFile();
		childControls[0].setReturnValue(true);
		children[0].getName();
		childControls[0].setReturnValue("node");
		
		node.addFile("new");
		nodeControl.setReturnValue(children[1]);
		
		replay();
		Node newNode = msbranch.createNode("new");
		assertNotNull(newNode);
		assertTrue(newNode instanceof PotentialChildLeaf);
		// get a bit of quick testing of potentialChildLeaf in here too.
		MyspaceLeaf msLeaf = (MyspaceLeaf)newNode;
		
		assertEquals("new",msLeaf.getName());
		assertNotNull(msLeaf.getNode());
		
		verify();				
		
	}
	
	public void testCreateNodeExists() throws Exception {
		node.children();
		nodeControl.setReturnValue(enumerateChildren(1));
		
		children[0].isFile();
		childControls[0].setReturnValue(true);
		children[0].getName();
		childControls[0].setReturnValue("node");
		
		
		replay();
		Node existingNode = msbranch.createNode("node");
		assertNotNull(existingNode);
		assertTrue(existingNode instanceof MyspaceLeaf);
		assertEquals(children[0],((MyspaceLeaf)existingNode).getNode());

		verify();			
	}
	
	private Enumeration enumerateChildren(int count) {
		ArrayList a = new ArrayList();
		for (int i = 0; i < count && i < children.length; i++) {
			a.add(children[i]);
		}
		return new IteratorEnumeration(a.iterator());
	}

	public void testGetChildrenOneFolder() {
		node.children();
		nodeControl.setReturnValue(enumerateChildren(1));
		
		children[0].isFile();
		childControls[0].setReturnValue(false);
		children[0].isFolder();
		childControls[0].setReturnValue(true);		
		replay();
		Node[] c = msbranch.getChildren();
		assertNotNull(c);
		assertEquals(1,c.length);
		assertTrue(c[0] instanceof MyspaceBranch);
		verify();
	}
	
	public void testGetChildrenOneFile() {
		node.children();
		nodeControl.setReturnValue(enumerateChildren(1));
		
		children[0].isFile();
		childControls[0].setReturnValue(true);
		replay();
		Node[] c = msbranch.getChildren();
		assertNotNull(c);
		assertEquals(1,c.length);
		assertTrue(c[0] instanceof MyspaceLeaf);
		verify();
	}
	
	public void testGetChildrenOneUnknown() {
		node.children();
		nodeControl.setReturnValue(enumerateChildren(1));
		
		children[0].isFile();
		childControls[0].setReturnValue(false);
		children[0].isFolder();
		childControls[0].setReturnValue(false);				
		replay();
		Node[] c = msbranch.getChildren();
		assertNotNull(c);
		assertEquals(0,c.length);
		verify();
	}
	
	public void testGetChildrenSome() {
		node.children();
		nodeControl.setReturnValue(enumerateChildren(3));
		
		children[0].isFile();
		childControls[0].setReturnValue(false);
		children[0].isFolder();
		childControls[0].setReturnValue(true);
		
		children[1].isFile();
		childControls[1].setReturnValue(true);
		
		children[2].isFile();
		childControls[2].setReturnValue(false);
		children[2].isFolder();
		childControls[2].setReturnValue(true);
		
		replay();
		Node[] c = msbranch.getChildren();
		assertNotNull(c);
		assertEquals(3,c.length);
		assertTrue(c[0] instanceof MyspaceBranch);
		assertTrue(c[1] instanceof MyspaceLeaf);
		assertTrue(c[2] instanceof MyspaceBranch);
		verify();		
	}
	
	public void testGetChildrenNone() {
		node.children();
		nodeControl.setReturnValue(enumerateChildren(0));
		
		replay();
		Node[] c = msbranch.getChildren();
		assertNotNull(c);
		assertEquals(0,c.length);
		//temporary debugging.
	//	assertNotNull(msbranch.getNode());
	//	assertSame(node,msbranch.getNode());
		verify();		
	}
	
	public void testGetChildrenNull() {
		node.children();
		nodeControl.setReturnValue(null);
		
		replay();
		Node[] c = msbranch.getChildren();
		assertNotNull(c);
		assertEquals(0,c.length);
		verify();				
	}
	
	public void testGetChildrenNullNode() {
		msbranch.setNode(null);
		
		replay();
		Node[] c = msbranch.getChildren();
		assertNotNull(c);
		assertEquals(0,c.length);
		verify();				
	}

	protected void replay() {
		super.replay();
		for (int i = 0; i < childControls.length; i++) {
			childControls[i].replay();
		}		
	}
	
	protected void verify() {
		super.verify();
		for (int i = 0; i < childControls.length; i++) {
			childControls[i].verify();
		}
	}
	
}
