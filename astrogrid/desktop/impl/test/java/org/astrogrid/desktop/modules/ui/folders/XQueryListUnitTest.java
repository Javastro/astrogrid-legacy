/**
 * 
 */
package org.astrogrid.desktop.modules.ui.folders;

import org.astrogrid.desktop.modules.ui.folders.Folder;
import org.astrogrid.desktop.modules.ui.folders.SmartList;
import org.astrogrid.desktop.modules.ui.folders.XQueryList;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 27, 20077:31:27 PM
 */
public class XQueryListUnitTest extends FolderUnitTest {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
	}
	public static final String query = "query";
	@Override
    protected Folder createFolder() {
		return new XQueryList(name,query);
	}
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ui.folders.XQueryList#setQuery(java.lang.String)}.
	 */
	public void testSetQuery() {
		XQueryList q = (XQueryList)f;
		assertEquals(query,q.getQuery());
		
		q.setQuery("bar");
		assertEquals("bar",q.getQuery());
	}
	
	@Override
    public void testFolderStringString() {
		assertEquals(name,f.getName());
		assertEquals(SmartList.defaultIcon,f.getIconName());
		assertNotNull(f.getIcon());
	}

}
