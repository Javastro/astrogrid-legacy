/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs;

import junit.framework.TestCase;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.UserAuthenticationData;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.util.UserAuthenticatorUtils;
import org.astrogrid.desktop.modules.system.VfsSystemTest;

/** Library tests for commons VFS.
 * Checks my understanding about the behaviour of this library.
 * 
 * See {@link VfsSystemTest} for tests of VFS's integration in AR. The tests in _this_
 * class just check some of the behaviours of the library itself.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 3, 20071:00:22 PM
 */
public class VfsLibraryTest extends TestCase {

	private FileSystemManager vfs;

	protected void setUp() throws Exception {
		super.setUp();
		this.vfs = VFS.getManager();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	// work with the temp file system as a testbed.
	public void testBasicCRUD() throws Exception {
		FileObject fo = vfs.resolveFile("tmp://foo");
		assertNotNull(fo);
	}
	
	public void testAuthentication() throws Exception {
		FileObject fo = vfs.resolveFile("tmp://bar");
		FileSystem fileSystem = fo.getFileSystem();
		System.out.println(fileSystem);
		FileSystemOptions o = fileSystem.getFileSystemOptions();
		System.out.println(o);
		UserAuthenticationData data = UserAuthenticatorUtils.authenticate(o,new UserAuthenticationData.Type[]{UserAuthenticationData.USERNAME});
		System.out.println(data);
	}

	
	
}
