/**
 * 
 */
package org.astrogrid.desktop.modules.ag.recorder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.collections.ComparatorUtils;
import org.astrogrid.WorkbenchAssert;
import org.astrogrid.acr.astrogrid.ExecutionInformation;

/** unit test for folder impl
 * @author Noel Winstanley
 * @since Jun 13, 20063:44:54 PM
 */
public class FolderImplUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		id = new URI("id:me");
		info = new ExecutionInformation(id,null,null,null,null,null);
		parent = new URI("id:parent");
		f = new FolderImpl(info,parent);
	}
	protected ExecutionInformation info;
	protected FolderImpl f;
	protected URI id;
	protected URI parent;
	
	public void testSerializable() throws IOException, ClassNotFoundException{
		// note, this is just testing serialization of folderImpl - as subordinate info object is mostly null.
		WorkbenchAssert.assertSerializable(f);
	}
	

	
	
	/*
	 * Test method for 'org.astrogrid.desktop.modules.ag.recorder.FolderImpl.setInformation(ExecutionInformation)'
	 */
	public void testSetInformation() {
		assertEquals(info,f.getInformation());
		ExecutionInformation e1 = new ExecutionInformation(id,null,"wibble",null,null,null);
		f.setInformation(e1);
		assertEquals(e1,f.getInformation());
	}

	public void testSetNullInformation() {
		try {
			f.setInformation(null);
			fail("expected to fail");
		} catch (IllegalArgumentException e) {
			//ok
		}
	}
	
	public void testSetDifferentInformation() throws URISyntaxException {
			URI id1 = new  URI("id:other");
		ExecutionInformation e1 = new ExecutionInformation(id1,null,null,null,null,null);
		try {
			f.setInformation(e1);
			fail("expected to fail");
		} catch (IllegalArgumentException e) {
			// ok
		}
	}
	
	/*
	 * Test method for 'org.astrogrid.desktop.modules.ag.recorder.FolderImpl.setDeleted(boolean)'
	 */
	public void testSetDeleted() {
		assertFalse(f.isDeleted());
		f.setDeleted(true);
		assertTrue(f.isDeleted());
		f.setDeleted(false);
		assertFalse(f.isDeleted());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ag.recorder.FolderImpl.setUnreadCount(int)'
	 */
	public void testSetUnreadCount() {
		assertEquals(0,f.getUnreadCount());
		f.setUnreadCount(10);
		assertEquals(10,f.getUnreadCount());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ag.recorder.FolderImpl.getChildKeyList()'
	 */
	public void testGetChildKeyList() {
		List l = f.getChildKeyList();
		assertNotNull(l);
		assertEquals(0,l.size());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ag.recorder.FolderImpl.getParentKey()'
	 */
	public void testGetParentKey() {
		assertEquals(parent,f.getParentKey());

	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ag.recorder.FolderImpl.getKey()'
	 */
	public void testGetKey() {
		assertEquals(id,f.getKey());

	}

}
