/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.net.URLStreamHandlerFactory;
import java.util.Collection;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.vfs.CacheStrategy;
import org.apache.commons.vfs.Capability;
import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileContentInfo;
import org.apache.commons.vfs.FileContentInfoFactory;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemConfigBuilder;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.FilesCache;
import org.apache.commons.vfs.operations.FileOperationProvider;
import org.apache.commons.vfs.operations.FileOperations;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** test that vfs, as integrated into AR, is doing what it says on the tin
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 26, 20075:11:16 PM
 * @TEST refactor into integration and system tests.
 */
public class VfsSystemTest extends InARTestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		ACR reg = getACR();
		vfs = assertComponentExists(FileSystemManager.class,"system.vfs");
	}

	protected FileSystemManager vfs;
	
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		vfs = null;
	}
	
    public static Test suite() {
        return new ARTestSetup(new TestSuite(VfsSystemTest.class));
    }

    public void testBaseFule() throws Exception {
    	FileObject base = vfs.getBaseFile();
    	assertNotNull(base);
	}
    
    public void testCacheStrategy() throws Exception {
		CacheStrategy cs = vfs.getCacheStrategy();
		assertNotNull(cs);
		// further testing here?
	}
    
    public void testFileContentInfoFactory() throws Exception {
		FileContentInfoFactory fac = vfs.getFileContentInfoFactory();
		assertNotNull(fac);
	}
    
    public void testFileObjectDecorator() throws Exception {
		Class clazz = vfs.getFileObjectDecorator();
		assertNull(clazz);
	}
    
    public void testFilesCache() throws Exception {
    	FilesCache cache = vfs.getFilesCache();
    	assertNotNull(cache);
    }
    
    public void testFileSystemConfigBuilder() throws Exception {
    	FileSystemConfigBuilder b = vfs.getFileSystemConfigBuilder("ftp");
    	assertNotNull(b);
    }
    
    public void testOperationProviders() throws Exception {
		FileOperationProvider[] operationProviders = vfs.getOperationProviders("ftp");
		assertNull(operationProviders);
	}
    
    public void testProviderCapabilities() throws Exception {
   // 	Collection caps = vfs.getProviderCapabilities("http");
    //	assertNotNull(caps);
    //	System.out.println(caps);
    	Collection caps = vfs.getProviderCapabilities("file");
    	assertNotNull(caps);
    	System.out.println(caps);    	
	}
    
    public void testSchemes() throws Exception {
		String[] s = vfs.getSchemes();
		assertNotNull(s);
		assertTrue(s.length > 0);
	}
    
    public void testStreamHandlerFacotry() throws Exception {
		URLStreamHandlerFactory h = vfs.getURLStreamHandlerFactory();
		assertNotNull(h);
		//@todo utilize this h somewhere.
	}
    
    public void testResolveURI() throws Exception {
		FileName fn = vfs.resolveURI("http://www.astrogrid.org/foo/");
		assertNotNull(fn);
		assertEquals(FileType.FOLDER,fn.getType());
		
		fn = vfs.resolveURI("http://www.astrogrid.org/foo");
		assertNotNull(fn);
		assertEquals(FileType.FILE,fn.getType());
	}
    
    public void testHttp() throws Exception {
		FileObject ag = vfs.resolveFile("http://www.astrogrid.org/");
		assertNotNull(ag);
		assertNotNull(ag.getType());
		assertEquals(FileType.FILE,ag.getType());
		FileOperations ops = ag.getFileOperations();
		assertNotNull(ops);
		assertNull(ops.getOperations());
		
		FileSystem fs = ag.getFileSystem();
		assertNotNull(fs);
		FileSystemOptions opts = fs.getFileSystemOptions();
		assertNull(opts);
		// can use this to detect what it's possible to do.
		assertFalse(fs.hasCapability(Capability.LIST_CHILDREN));

		FileContent content = ag.getContent();
		assertNotNull(content);
		assertTrue(content.getAttributes().isEmpty());
		FileContentInfo nfo = content.getContentInfo();
		/* don't work.
		assertNotNull(nfo.getContentType());
		assertEquals("text/html",nfo.getContentType());
		assertTrue(content.getSize() > 0);
		*/
		
		
	}
    
}
