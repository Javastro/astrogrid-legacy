/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs;

import junit.framework.TestCase;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.NameScope;
import org.astrogrid.desktop.modules.ag.vfs.myspace.MyspaceFileName;

/** Test that the myspace filename behaves as expected.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 3, 20074:44:15 PM
 */
public class MyspaceFileNameUnitTest extends TestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		uri = "ivo://uk.ac.le.star/noelwinstanley#/foo/bar.txt";
		fn = new MyspaceFileName("uk.ac.le.star","noelwinstanley","/foo/bar.txt",FileType.FILE);
		root = new MyspaceFileName("uk.ac.le.star","noelwinstanley","/",FileType.FOLDER);
		parent = new MyspaceFileName("uk.ac.le.star","noelwinstanley","/foo",FileType.FOLDER);
		
	}
	
	String uri;
	MyspaceFileName fn;
	MyspaceFileName root;
	MyspaceFileName parent;
	
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		fn = null;
		uri = null;
		root = null;
		parent =null;
	}
	
	public void testBaseName() throws Exception {
		assertEquals("bar.txt",fn.getBaseName());
	}
	
	public void testPath() throws Exception {
		assertEquals("/foo/bar.txt",fn.getPath());
	}
	
	public void testPathDecoded() throws Exception {
		assertEquals("/foo/bar.txt",fn.getPathDecoded());
	}
	
	public void testDepth() throws Exception {
		assertEquals(3,fn.getDepth()); 
	}
	
	public void testScheme() throws Exception {
		assertEquals("ivo",fn.getScheme());
	}
	
	public void testURI() throws Exception {
		assertEquals(uri,fn.getURI());
	}
	
	public void testRootURI() throws Exception {
		assertEquals("ivo://uk.ac.le.star/noelwinstanley#/",fn.getRootURI());
	}
	
	public void testRoot() throws Exception {
			assertEquals(root,fn.getRoot());
	}

	public void testParent() throws Exception {
			assertEquals(parent,fn.getParent());
	}
	
	public void testRelativeName() throws Exception {
		assertEquals("foo/bar.txt",root.getRelativeName(fn));
		assertEquals("bar.txt",parent.getRelativeName(fn));
		assertEquals("..",fn.getRelativeName(parent));
		assertEquals("../..",fn.getRelativeName(root));
	}
	
	public void testAncestor() throws Exception {
		assertTrue(fn.isAncestor(parent)); // poorly named function, IMHO
		assertTrue(fn.isAncestor(root));
		assertTrue(parent.isAncestor(root));
		assertFalse(root.isAncestor(parent));
		assertFalse(parent.isAncestor(fn));
		assertFalse(fn.isDescendent(fn));
	}
	
	public void testDescendent() throws Exception {
		assertFalse(fn.isDescendent(parent)); 
		assertFalse(fn.isDescendent(root));
		assertFalse(parent.isDescendent(root));
		assertTrue(root.isDescendent(parent));
		assertTrue(parent.isDescendent(fn));
		assertFalse(fn.isDescendent(fn));
		
	}
	
	public void testDescendentNameScope() throws Exception {
		assertTrue(root.isDescendent(fn,NameScope.FILE_SYSTEM));
		assertFalse(root.isDescendent(fn,NameScope.CHILD));
	}
	
	public void testType() throws Exception {
		assertEquals(FileType.FILE,fn.getType());
		assertEquals(FileType.FOLDER,parent.getType());
		assertEquals(FileType.FOLDER,root.getType());
	}
	
	public void testFriendlyURI() throws Exception {
		// should be the same, as we've no password in the uris.
		assertEquals(uri,fn.getFriendlyURI());
	}
	
	public void testCreateName() throws Exception {
		FileName fn1 = fn.createName(fn.getPath(),FileType.FILE);
		assertEquals(fn,fn1);
	}
}
