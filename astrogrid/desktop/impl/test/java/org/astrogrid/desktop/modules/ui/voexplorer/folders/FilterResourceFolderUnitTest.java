/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.folders;

import org.apache.commons.lang.StringUtils;

/** unit test for filter resource folder. also test resourceFolder too.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 27, 20077:10:54 PM
 */
public class FilterResourceFolderUnitTest extends FolderUnitTest {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public static final String filter = "foo";
	public static final int sz = 42;
	protected Folder createFolder() {
		return new FilterResourceFolder(name,icon,filter,sz);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testFixed() throws Exception {
		FilterResourceFolder g = (FilterResourceFolder)f;
		assertFalse(g.isFixed());
		
		g.setFixed(true);
		assertTrue(g.isFixed());	
		
		// fixed folders have a formatted name
		String formatted = g.getName();
		assertTrue(StringUtils.contains(formatted,"<html>"));
		
	}
	
	public void testSize() throws Exception {
		FilterResourceFolder g = (FilterResourceFolder)f;
		assertEquals(sz,g.getSize());
		g.setSize(22);
		assertEquals(22,g.getSize());
	}



	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ui.voexplorer.folders.FilterResourceFolder#setFilter(java.lang.String)}.
	 */
	public void testSetFilter() {
		FilterResourceFolder g = (FilterResourceFolder)f;
		assertEquals(filter,g.getFilter());
		
		g.setFilter("bar");
		assertEquals("bar",g.getFilter());
		
	}

}
