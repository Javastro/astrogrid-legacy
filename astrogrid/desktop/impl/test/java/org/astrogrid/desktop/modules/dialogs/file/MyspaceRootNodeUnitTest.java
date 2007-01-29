package org.astrogrid.desktop.modules.dialogs.file;

import java.net.URI;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.UserInformation;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.easymock.MockControl;

/** tests a myspace root node */
public class MyspaceRootNodeUnitTest extends MyspaceBranchUnitTest {

	protected void setUp() throws Exception {
		msiControl = MockControl.createControl(MyspaceInternal.class);
		msi = (MyspaceInternal)msiControl.getMock();
		
		clientControl = MockControl.createControl(FileManagerClient.class);
		client = (FileManagerClient)clientControl.getMock();
		commControl = MockControl.createControl(Community.class);
		comm= (Community)commControl.getMock();
		
		
		super.setUp();
		assertTrue(msbranch instanceof MyspaceRootNode);
		msroot = (MyspaceRootNode)msbranch;
	
		// setup default expectation.
		comm.isLoggedIn();
		commControl.setDefaultReturnValue(true);
		
		msi.getClient();
		msiControl.setReturnValue(client,0,1); // expect will be called no more than once per tests
	
		client.home();
		clientControl.setReturnValue(node,0,1);		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		msi = null;
		msiControl = null;
		comm= null;
		commControl = null;
		msroot = null;
		client = null;
		clientControl = null;
	}
	
	MyspaceInternal msi;
	MockControl msiControl;
	Community comm;
	MockControl commControl;
	MyspaceRootNode msroot;
	private MockControl clientControl;
	private FileManagerClient client;
	
	protected MyspaceNode createMyspaceNode() {
		return new MyspaceRootNode(msi,comm);
	}
	
	//////////////////////// overridden tests.
	
	// overrides.
	public void testSetNode() throws Exception {
		try {
			msroot.setNode(node);
			fail("expected to chuck");
		} catch (UnsupportedOperationException e) {
			// ok
		}
	}
	// overrides.
	public void testGetName() throws Exception{
		comm.getUserInformation();
		URI id = new URI("ivo://test/testing");
		UserInformation unfo = new UserInformation(id,"foo","bar","choo");
		commControl.setReturnValue(unfo);
		replay();
		String s = msroot.getName();
		assertNotNull(s);
		assertEquals("Myspace",s);
		
		// now do a 'login',
		msroot.getNode();
		s = msroot.getName();
		assertNotNull(s);
		//assertTrue(s.contains(id.toString())); 1.5 only.
		assertTrue(StringUtils.contains(s, id.toString()));
		verify();
	}


	
	public void testToString() throws Exception {
		comm.getUserInformation();
		URI id = new URI("ivo://test/testing");
		UserInformation unfo = new UserInformation(id,"foo","bar","choo");
		commControl.setDefaultReturnValue(unfo);
		replay();
		assertEquals(msroot.getName(),msroot.toString());
		verify();
	}
	// overridden
	public void testGetParent() throws Exception {
		replay();
		assertNull(msroot.getParent());
		verify();
	}
	
	/////////////////////////// disabled tests.
	public void testGetNameNullNode() throws Exception {
		// implementation ovverides getName - so this test isn'nt meaningful.
	}
	public void testToStringNodeException() throws Exception {
		// impldementation overrides the toString - this test isn't relevant.
	}
	 
	public void testtoStringNullNode() throws Exception {
		// impldementation overrides the toString - this test isn't relevant.
	}
	
	public void testGetChildrenNullNode() {
		// can't set node to null - so test isn't relevant.
	}
	
	public void testGetURINullNode() throws Exception {
		// can't set node to null - so test isn't relevant.
	}



	protected void verify() {
		super.verify();
		msiControl.verify();
		commControl.verify();
		clientControl.verify();
	}
	protected void replay() {
		super.replay();
		msiControl.replay();
		commControl.replay();
		clientControl.replay();
	}
}
