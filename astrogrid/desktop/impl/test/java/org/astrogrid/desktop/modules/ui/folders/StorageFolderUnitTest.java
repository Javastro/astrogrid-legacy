/**
 * 
 */
package org.astrogrid.desktop.modules.ui.folders;

import static org.easymock.EasyMock.*;

import java.net.URI;

import org.apache.commons.vfs.FileObject;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;

/** unit test for storage folder.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 27, 20077:23:37 PM
 * @TEST removal of links on logout.
 */
public class StorageFolderUnitTest extends FolderUnitTest {

	@Override
    protected void setUp() throws Exception {
		uri = new URI("http://www.astrogrid.org");
		super.setUp();
	}
	URI uri;
	@Override
    protected Folder createFolder() {
		return new StorageFolder(name,icon,uri);
	}
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		uri = null;
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ui.folders.StorageFolder#setUriString(java.lang.String)}.
	 */
	public void testSetUriString() throws Exception {
		final StorageFolder s = (StorageFolder)f;
		assertEquals(uri,s.getUri());
		assertEquals(uri.toString(),s.getUriString());
		
		final String u2 = "http://www.slashdot.org";
		s.setUriString(u2);
		assertEquals(u2,s.getUriString());
		assertEquals(new URI(u2),s.getUri());
		
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ui.folders.StorageFolder#setFile(org.apache.commons.vfs.FileObject)}.
	 */
	public void testSetFile() throws Exception {
		final StorageFolder s = (StorageFolder)f;
		assertNull(s.getFile());
		final FileObject fo = createNiceMock(FileObject.class);
		replay(fo);
		final FileObjectView fov = new FileObjectView(fo,null);
		s.setFile(fov);
		assertSame(fov,s.getFile());
	}

}
