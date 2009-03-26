/**
 * 
 */
package org.astrogrid.desktop.modules.ui.folders;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.astrogrid.desktop.modules.ui.folders.StaticList;
import org.astrogrid.desktop.modules.ui.folders.Folder;

/** unit test for the dumb resource folder.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 27, 20077:34:18 PM
 */
public class StaticListUnitTest extends FolderUnitTest {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Override
    protected Folder createFolder() {
		return new StaticList(name,icon);
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ui.folders.StaticList#getSize()}.
	 */
	public void testGetSize() {
		StaticList d = (StaticList)f;
		assertEquals(0,d.getSize());
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ui.folders.StaticList#setResourceSet(java.util.Collection)}.
	 */
	public void testSetResourceSet() throws Exception{
		StaticList d = (StaticList)f;
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
