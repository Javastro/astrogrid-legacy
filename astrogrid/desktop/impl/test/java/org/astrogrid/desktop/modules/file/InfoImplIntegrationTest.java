/**
 * 
 */
package org.astrogrid.desktop.modules.file;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import java.net.URI;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.file.Info;
import org.astrogrid.acr.file.Manager;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** Test for info component.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 30, 20087:03:54 PM
 */
public class InfoImplIntegrationTest extends InARTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        m = assertServiceExists(Manager.class,"file.manager");
        i = assertServiceExists(Info.class,"file.info");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        m = null;
        i = null;
    }
    
    private Manager m;
    private Info i;
    private static URI ROOT = URI.create("/");
    private static URI NON_EXISTENT = URI.create("tmp://non-exisitent.txt");
    
    public static Test suite() {
        return new ARTestSetup(new TestSuite(InfoImplIntegrationTest.class));
    }
    
    public void testExists() throws Exception {
        final URI u = new URI("tmp://exists/bar");
        assertFalse(i.exists(u));
        m.createFile(u);
        assertTrue(i.exists(u));
    }
    
    public void testAttributes() throws Exception {
        final Map map = i.getAttributes(ROOT);
        assertNotNull(map);
        // can't test any further
        try {
            i.getAttributes(NON_EXISTENT);
            fail("expected to chuck");
        } catch (final ACRException e) {
            // ok
        }
    }
    
    public void testContentType() throws Exception {
        final URI u = new URI("tmp://contentType/bar.html");
        assertEquals("text/html",i.getContentType(u));
        assertNull(i.getContentType(ROOT));
        assertEquals("text/plain",i.getContentType(NON_EXISTENT));
        
        // verify that content type goes on file suffix, not content.
        // by writing binary data to a html file, and see what is returned.
        final byte[] arr = new byte[10];
        for (int i =0; i < arr.length; i++) {
            arr[i] = (byte)i;
        }
        m.writeBinary(u,arr);
        assertEquals("text/html",i.getContentType(u));
    }
    
    public void testLastModifiedTime() throws Exception {
        assertThat(i.getLastModifiedTime(ROOT),greaterThan(0L));
        try {
            i.getLastModifiedTime(NON_EXISTENT);
            fail("expected to chuck");
        } catch (final ACRException e) {
            // ok
        } 
    }

    public void testSize() throws Exception {
        try {
            i.getSize(ROOT);
            fail("chucks");
        } catch (final ACRException e) {
            // ok
        }
        try {
            i.getSize(NON_EXISTENT);
            fail("chucks");
        } catch (final ACRException e) {
            // ok
        }        
        final URI u = new URI("tmp://size/foo.txt");
        m.createFile(u);
        assertEquals(0,i.getSize(u));
        m.write(u,"some text");
        assertThat(i.getSize(u),greaterThan(0L));
    }
    
    public void testIsFile() throws Exception {
        assertFalse(i.isFile(ROOT));
        final URI f = new URI("tmp://isFile/foo.txt");
        m.createFile(f);
        assertTrue(i.isFile(f));
        assertFalse(i.isFile(NON_EXISTENT));
    }
    
    public void testIsFolder() throws Exception {
        assertTrue(i.isFolder(ROOT));
        final URI f = new URI("tmp://isFile/foo.txt");
        m.createFile(f);
        assertFalse(i.isFolder(f));
        assertFalse(i.isFolder(NON_EXISTENT));
    }    
    
    public void testHidden() throws Exception {
        assertFalse(i.isHidden(ROOT));
        final URI u = new URI("tmp://foo/.hidden");
        assertFalse(i.isHidden(u));
        m.createFile(u);
        assertTrue(i.isHidden(u));
        assertFalse(i.isHidden(NON_EXISTENT));      
    }
    
    public void testReadable() throws Exception {
        assertTrue(i.isReadable(ROOT));
        assertFalse(i.isReadable(NON_EXISTENT));         
    }
    
    public void testWritable() throws Exception {
        // existing non-writable directory.
        assertFalse(i.isWritable(ROOT));
        // non-existent file, in non-writable directory.
        assertFalse(i.isWritable(ROOT.resolve("inroot.txt")));
        
        // non-existing writable file.
        final URI f = new URI("tmp://isWrtitable/foo.txt");
        assertTrue(i.isWritable(f));        
        m.createFile(f);
        // existing writable file.
        assertTrue(i.isWritable(f));
        // exist writing folder.
        final URI folder = new URI("tmp://isWritable/folder");
        m.createFolder(folder);
        assertTrue(i.isWritable(folder));
          
    }
    
    
}
