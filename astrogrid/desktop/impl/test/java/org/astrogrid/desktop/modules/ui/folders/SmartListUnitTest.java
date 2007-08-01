/**
 * 
 */
package org.astrogrid.desktop.modules.ui.folders;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.desktop.modules.ui.folders.SmartList;
import org.astrogrid.desktop.modules.ui.folders.Folder;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.PhraseSRQL;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQL;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.TermSRQL;

/** unit test for filter resource folder. also test resourceFolder too.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 27, 20077:10:54 PM
 */
public class SmartListUnitTest extends FolderUnitTest {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public static final SRQL q = new TermSRQL();
	protected Folder createFolder() {
		return new SmartList(name,q);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testFixed() throws Exception {
		SmartList g = (SmartList)f;
		assertFalse(g.isFixed());
		
		g.setFixed(true);
		assertTrue(g.isFixed());	

		
	}
	// overridden
	public void testFolderStringString() {
		assertEquals(name,f.getName());
		assertEquals(SmartList.defaultIcon,f.getIconName());
		assertNotNull(f.getIcon());
	}
	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ui.folders.SmartList#setFilter(java.lang.String)}.
	 */
	public void testQuery() {
		SmartList g = (SmartList)f;
		assertEquals(q,g.getQuery());
		
		SRQL nu = new PhraseSRQL();
		g.setQuery(nu);
		assertEquals(nu,g.getQuery());
		
	}

}
