/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.folders;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 27, 20077:31:27 PM
 */
public class QueryResourceFolderUnitTest extends FolderUnitTest {

	protected void setUp() throws Exception {
		super.setUp();
	}
	public static final String query = "query";
	protected Folder createFolder() {
		return new QueryResourceFolder(name,icon,query);
	}
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ui.voexplorer.folders.QueryResourceFolder#setQuery(java.lang.String)}.
	 */
	public void testSetQuery() {
		QueryResourceFolder q = (QueryResourceFolder)f;
		assertEquals(query,q.getQuery());
		
		q.setQuery("bar");
		assertEquals("bar",q.getQuery());
	}

}
