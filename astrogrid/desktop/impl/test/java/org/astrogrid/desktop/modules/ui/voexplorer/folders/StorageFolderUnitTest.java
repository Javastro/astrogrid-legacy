/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.folders;

import java.net.URI;

import org.apache.commons.vfs.FileObject;
import org.easymock.MockControl;

/** unit test for storage folder.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 27, 20077:23:37 PM
 */
public class StorageFolderUnitTest extends FolderUnitTest {

	protected void setUp() throws Exception {
		uri = new URI("http://www.astrogrid.org");
		super.setUp();
	}
	URI uri;
	protected Folder createFolder() {
		return new StorageFolder(name,icon,uri);
	}
	protected void tearDown() throws Exception {
		super.tearDown();
		uri = null;
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ui.voexplorer.folders.StorageFolder#setUriString(java.lang.String)}.
	 */
	public void testSetUriString() throws Exception {
		StorageFolder s = (StorageFolder)f;
		assertEquals(uri,s.getUri());
		assertEquals(uri.toString(),s.getUriString());
		
		String u2 = "http://www.slashdot.org";
		s.setUriString(u2);
		assertEquals(u2,s.getUriString());
		assertEquals(new URI(u2),s.getUri());
		
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ui.voexplorer.folders.StorageFolder#setFile(org.apache.commons.vfs.FileObject)}.
	 */
	public void testSetFile() {
		StorageFolder s = (StorageFolder)f;
		assertNull(s.getFile());
		MockControl m = MockControl.createNiceControl(FileObject.class);
		FileObject fo = (FileObject)m.getMock();
		s.setFile(fo);
		assertSame(fo,s.getFile());
	}

}