/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.folders;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/** unit test for the dumb resource folder.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 27, 20077:34:18 PM
 */
public class DumbResourceFolderUnitTest extends FolderUnitTest {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	protected Folder createFolder() {
		return new DumbResourceFolder(name,icon);
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ui.voexplorer.folders.DumbResourceFolder#getSize()}.
	 */
	public void testGetSize() {
		DumbResourceFolder d = (DumbResourceFolder)f;
		assertEquals(0,d.getSize());
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ui.voexplorer.folders.DumbResourceFolder#setResourceArray(java.lang.String[])}.
	 */
	public void testSetResourceArray() {
		DumbResourceFolder d = (DumbResourceFolder)f;
		String[] resourceArray = d.getResourceArray();
		assertNotNull(resourceArray);
		assertEquals(0,resourceArray.length);
		
		String[] arr = new String[] {"foo","bar","choo"};
		d.setResourceArray(arr);
		assertNotNull(d.getResourceArray());
		assertTrue(Arrays.equals(arr,d.getResourceArray()));
		// check size and link to Collection is working.
		assertEquals(arr.length, d.getSize());
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ui.voexplorer.folders.DumbResourceFolder#setResourceSet(java.util.Collection)}.
	 */
	public void testSetResourceSet() throws Exception{
		DumbResourceFolder d = (DumbResourceFolder)f;
		Set s = d.getResourceSet();
		assertNotNull(s);
		assertEquals(0,s.size());
		
		
		List l = new ArrayList();
		l.add(new URI("foo"));
		l.add(new URI("bar"));
		l.add(new URI("choo"));
		d.setResourceSet(l);
		assertNotNull(d.getResourceSet());
		assertEquals(new TreeSet(l),new TreeSet(d.getResourceSet()));
		// check size are working.
		assertEquals(l.size(),d.getResourceSet().size());
		
	}

}
