/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs;

import junit.framework.TestCase;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileType;
import org.astrogrid.desktop.modules.ag.vfs.myspace.MyspaceNameParser;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 3, 200712:50:05 PM
 */
public class MyspaceNameParserUnitTest extends TestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		parser = new MyspaceNameParser();
	}

	MyspaceNameParser parser;
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		parser = null; 
	}
	
	public void testFileParse() throws Exception {
		FileName fn = parser.parseUri(null,null,"ivo://uk.ac.le.star/noelwinstanley#/foo/bar/choo");
		assertNotNull(fn);
		assertEquals("/foo/bar/choo",fn.getPath());
		assertEquals(FileType.FILE,fn.getType());
		assertEquals("choo",fn.getBaseName());
	}

	public void testFolderParse() throws Exception {
		FileName fn = parser.parseUri(null,null,"ivo://uk.ac.le.star/noelwinstanley#/foo/bar/");
		assertNotNull(fn);
		assertEquals("/foo/bar",fn.getPath());
		assertEquals(FileType.FOLDER,fn.getType());
		assertEquals("bar",fn.getBaseName());
	}	
	
	public void testRootParse() throws Exception {
		FileName fn = parser.parseUri(null,null,"ivo://uk.ac.le.star/noelwinstanley#/");
		assertNotNull(fn);		
		assertNotNull(fn);
		assertEquals("/",fn.getPath());
		assertEquals(FileType.FOLDER,fn.getType());
		assertEquals(fn,fn.getRoot());
		assertEquals("",fn.getBaseName());
	}	
	
	public void testRootParseLazy() throws Exception {
		FileName fn = parser.parseUri(null,null,"ivo://uk.ac.le.star/noelwinstanley#");
		assertNotNull(fn);
		assertEquals("/",fn.getPath());
		assertEquals(FileType.FOLDER,fn.getType());
		assertEquals(fn,fn.getRoot());
		assertEquals("",fn.getBaseName());
	}	
	public void testRootParseLazier() throws Exception {
		FileName fn = parser.parseUri(null,null,"ivo://uk.ac.le.star/noelwinstanley");
		assertEquals("/",fn.getPath());
		assertEquals(FileType.FOLDER,fn.getType());
		assertEquals(fn,fn.getRoot());
		assertEquals("",fn.getBaseName());
	}		
	
}
