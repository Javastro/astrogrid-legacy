/**
 * 
 */
package org.astrogrid.desktop.modules.ui.folders;

import org.astrogrid.desktop.modules.ui.folders.Folder;

import junit.framework.TestCase;

/** basic unit test for the folder class.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 27, 20077:03:32 PM
 */
public class FolderUnitTest extends TestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		f = createFolder();
	}

	protected Folder createFolder() {
		return new Folder(name,icon);
	}
	public static final String name = "Folder";
	public static final String icon = "contents16.png";
	Folder f;
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testInitialized() throws Exception {
		f = new Folder();
		assertNull(f.getName());
		assertNull(f.getIconName());
		assertNull(f.getIcon());
	}
	
	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ui.folders.Folder#Folder(java.lang.String, java.lang.String)}.
	 */
	public void testFolderStringString() {
		assertEquals(name,f.getName());
		assertEquals(icon,f.getIconName());
		assertNotNull(f.getIcon());
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ui.folders.Folder#getIconName()}.
	 */
	public void testGetIconName() {
		f.setIconName("unknownIcon");
		assertEquals("unknownIcon",f.getIconName());
		assertNull(f.getIcon());
		
		f.setIconName("ascii16.png");
		assertEquals("ascii16.png",f.getIconName());
		assertNotNull(f.getIcon());
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ui.folders.Folder#getName()}.
	 */
	public void testGetName() {
		f.setName("foo");
		assertEquals("foo",f.getName());
	}

}
