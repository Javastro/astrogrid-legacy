/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.operations.FileOperations;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 5, 200711:38:49 AM
 */
public class MyspaceVFSSystemTest extends InARTestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		vfs=  assertComponentExists(FileSystemManager.class, "system.vfs");
		home = vfs.resolveFile("ivo://" + getCommunity() + "/" + getUsername());
	}

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		vfs = null;
		home = null;
	}
	
	protected FileSystemManager vfs;
	protected FileObject home;

    public static Test suite() {
        return new ARTestSetup(new TestSuite(MyspaceVFSSystemTest.class),true); // login.
    }    	
	
	public void testHome() throws Exception {
		assertNotNull(home);
		assertIsFolder(home);
		assertNotNull(home.getName());
		assertNotNull(home.getURL());
		assertFalse(home.isHidden());
		assertTrue(home.isWriteable());
		assertTrue(home.isReadable());
		assertTrue(home.isAttached());
		assertFalse(home.isContentOpen());
		assertNull(home.getParent());
		
		
		final FileObject[] chillun = home.getChildren();
		assertNotNull(chillun);
		assertTrue(chillun.length > 0);
		
		final FileObject o = home.getChild(chillun[0].getName().getBaseName());
		assertEquals(o.getName(),chillun[0].getName());
	// not true - how odd.	assertSame(o,chillun[0]); // should be same object if vfs caching is working.
	
	}
	
	public void testFileSystem() throws Exception {
		final FileSystem fileSystem = home.getFileSystem();
		assertNotNull(fileSystem);
		
		final FileSystemOptions opts = fileSystem.getFileSystemOptions();
		assertNull(opts);
		
		assertNotNull(fileSystem.getRoot());
		assertEquals(home.getName(),fileSystem.getRoot().getName());
		assertEquals(fileSystem.getRoot().getName(),fileSystem.getRootName());
		// could exercise the listener interfaces here too.
	}
	
	public void testFileOperations() throws Exception {
		final FileOperations ops = home.getFileOperations();
		assertNotNull(ops);
		assertNull(ops.getOperations());
		
	}
	
	public void testFindFiles() throws Exception {
		
	}
	
	// todo resolveFile
	// moveTo
	// content - read / write / metadata
	//delete
	//createFile, createFolder
	//copyFrom
	//canRenameTo
	// do most of tests for home with a subfolder.
	// 
	private void assertIsFolder(final FileObject home) throws FileSystemException {
		assertTrue(home.exists());
		assertTrue(home.getType().equals(FileType.FOLDER));
	}
	
	
	
}
