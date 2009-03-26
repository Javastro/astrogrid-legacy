/**
 * 
 */
package org.astrogrid.desktop.modules.file;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.net.URI;
import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.lang.SystemUtils;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.file.Info;
import org.astrogrid.acr.file.Manager;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** Integration test for manager.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 30, 20083:45:26 PM
 */
public class ManagerImplIntegrationTest extends InARTestCase {

    private Manager m;
    private Info i;

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
    
    private final static URI ROOT = URI.create("/");
    
    public static Test suite() {
        return new ARTestSetup(new TestSuite(ManagerImplIntegrationTest.class));
    }
    
    public void testListChildren() throws Exception {
        final String[] c = m.listChildren(ROOT);
        assertNotNull(c);              
        assertThat(c.length,greaterThan(0));
        for (final String n : c) {
            assertThat(n,notNullValue());
        }
    }
    
    public void testListChildUris() throws Exception {
        final URI[] c = m.listChildUris(ROOT);
        assertNotNull(c);
        assertThat(c.length,greaterThan(0));
        for (final URI uri : c) {
            assertThat(uri,notNullValue());
        }
    }
    
    public void testListChildrenOfNonExistent() throws Exception {
        final URI f = new URI("tmp://testListChilrenOfNonExistent");
        try {
            m.listChildren(f);
            fail("expected to chuck");
        } catch (final ACRException e) {
            // ok.
        }
    }

    
    public void testListChildrenOfEmpty() throws Exception {
        final URI f = new URI("tmp://testListChilrenOfEmpty");
        m.createFolder(f);
        final String[] children = m.listChildren(f);
        assertNotNull(children);
        assertThat(children.length,is(0));
    }
    
    public void testListChildrenOfFile() throws Exception {
        final URI f = new URI("tmp://testListChilrenOfFile/file.txt");
        m.createFile(f);
        try {
            m.listChildren(f);
            fail("expected to chuck");
        } catch (final ACRException e) {
            // ok.
        }
    }
    
    /** assert that relative uris are resolved relative to user.home */
    public void testRelativeResolvedToHome() throws Exception {       
        final URI dot = new URI(".");
        assertFalse(dot.isAbsolute());
        // only way to test for equivalence within manager is to list children..
        // Uris should be identical.
        
        final URI home = new File(SystemUtils.USER_HOME).toURI();
        assertTrue(home.isAbsolute());
        final URI[] homeChildren = m.listChildUris(home);
        final URI[] dotChildren = m.listChildUris(dot);
        assertThat(dotChildren.length,is(homeChildren.length));
        Arrays.sort(homeChildren);
        Arrays.sort(dotChildren);
        assertThat(dotChildren,equalTo(homeChildren));
    }
    /** test we can create a temporary file */
    public void testCreation() throws Exception {
        final URI tmpFile = new URI("tmp://foo/bar/choo");
        m.createFile(tmpFile);
        final URI tmpFolder = new URI("tmp://foo/frog/toad");
        m.createFolder(tmpFolder);
        
        // check we can 'ls' these new files and folders
        assertThat(m.listChildren(new URI("tmp://foo"))
                ,allOf(
                    hasItemInArray("bar")
                    ,hasItemInArray("frog")
                        ));
        
        // now can create at same type again, but at different type will throw..
        m.createFile(tmpFile);      
        m.createFolder(tmpFolder);
        try {
            m.createFile(tmpFolder);
            fail("expected to barf");
        } catch (final ACRException e) {
            // ok
        }
        try {
            m.createFolder(tmpFile);
            fail("expected to barf");
        } catch (final ACRException e) {
            // ok.
        }
        
        // likewise, trying to create a file at somewhere we've already created a folder witll fail.
        try {
            m.createFile(new URI("tmp:///foo/bar"));  
            /* note that // is equivalent to /// */
            fail("expected to barf");
        } catch (final ACRException e) {
            // ok.
        }        
    }
// delete    
    public void testDeleteFile() throws Exception {
        final URI d = new URI("tmp://deleteFile/");
        final URI f = d.resolve("file");
        m.createFile(f);
        assertThat(m.listChildren(d),hasItemInArray("file"));
        assertTrue(m.delete(f));
        assertEquals(0,m.listChildren(d).length);
        
        
    }
    
    public void testDeleteFolder() throws Exception {
        final URI d = new URI("tmp://deleteFolder/");
        final URI f = d.resolve("folder");
        m.createFolder(f);
        assertThat(m.listChildren(d),hasItemInArray("folder"));
        assertTrue(m.delete(f));
        assertEquals(0,m.listChildren(d).length);                        
    }
    
    public void testDeleteNonEmptyFolder() throws Exception {
        final URI d = new URI("tmp://deleteNonEmpty/");
        final URI f = d.resolve("file");
        m.createFile(f);
        assertThat(m.listChildren(d),hasItemInArray("file"));
        assertTrue("failed to delete non-empty folder",m.delete(d));                               
    }
    
//Copy
    public void testCopyFile() throws Exception {
        final URI src = new URI("tmp://copyFile/file.txt");
        final String content = "some file content";
        m.write(src,content);
        assertTrue(i.exists(src));
        // neither the destination file, or the destinatio foldr exist..
        final URI dest = new URI("tmp://copyfiledest/filedest.txt");
        m.copy(src,dest);
        assertTrue(i.exists(src));
        assertTrue(i.exists(dest));
        assertEquals(content,m.read(src));
        assertEquals(content,m.read(dest));
    }
    
    public void testCopyOverwritesExistingFile() throws Exception {
        final URI src = new URI("tmp://copyFileOverwrite/file.txt");
        final String content = "some file content";
        m.write(src,content);
        assertTrue(i.exists(src));
        final URI dest = new URI("tmp://copyfiledOverwritedest/filedest.txt");
        m.write(dest,"some other content");
        assertTrue(i.exists(dest));
        m.copy(src,dest);
        assertTrue(i.exists(src));
        assertTrue(i.exists(dest));
        assertEquals(content,m.read(src));
        assertEquals(content,m.read(dest));        
    }
    
    public void testCopyConvertsFileToFolder() throws Exception {
        final URI srcFolder = new URI("tmp://copyConvert/");
        final URI src = srcFolder.resolve("file.txt");
        final String content = "some file content";
        m.write(src,content);
        assertTrue(i.exists(src));
        assertTrue(i.isFolder(srcFolder));
        
        final URI dest = new URI("tmp://copyConvertFile/filedest.txt");
        m.write(dest,"some other content");
        assertTrue(i.exists(dest));
        assertTrue(i.isFile(dest));
        m.copy(srcFolder,dest);
        assertTrue(i.exists(src));
        assertTrue(i.exists(dest));
        assertTrue(i.isFolder(dest));       
    }
    
    
    public void testCopyEmptyFolder() throws Exception {
        final URI srcFolder = new URI("tmp://copyEmpty/");
        m.createFolder(srcFolder);
        assertTrue(i.isFolder(srcFolder));
        
        final URI dest = new URI("tmp://copyConvertFile/filedest.txt");
        m.copy(srcFolder,dest);
        assertTrue(i.exists(srcFolder));
        assertTrue(i.exists(dest));
        assertTrue(i.isFolder(dest));           
    }
    
    public void testCopyNonexistentFile() throws Exception {
        final URI src = new URI("tmp://copyNonExistent/file.txt");
        assertFalse(i.exists(src));
        final URI dest = new URI("tmp://copyNonExistent/filedest.txt");
        try {
            m.copy(src,dest);
            fail("expected to throw");
        } catch (final ACRException e) {
            // ok
        }
    }
    
// Move   
    public void testMoveFileWithinSameDir() throws Exception {
        final URI src = new URI("tmp://moveFile/a");
        final String content = "some file content";
        m.write(src,content);
        assertTrue(i.exists(src));
        // neither the destination file, or the destinatio foldr exist..
        final URI dest = new URI("tmp://moveFile/b");
        m.move(src,dest);
        assertFalse(i.exists(src));
        assertTrue(i.exists(dest));      
        assertEquals(content,m.read(dest));
    }    
    
    public void testMoveIntoDifferentDir() throws Exception {
        final URI src = new URI("tmp://moveFileDifferent/file.txt");
        final String content = "some file content";
        m.write(src,content);
        assertTrue(i.exists(src));
        // neither the destination file, or the destinatio foldr exist..
        final URI destDir = new URI("tmp://movefileAnotherDir/");
        final URI dest = destDir.resolve("anotherfile.txt");
        m.createFolder(destDir);
        m.move(src,dest);
        assertFalse(i.exists(src));
        assertTrue(i.exists(dest));      
        assertEquals(content,m.read(dest));
    }    
    public void testMoveIntoNonExistentDir() throws Exception {
        final URI src = new URI("tmp://moveFile42/file.txt");
        final String content = "some file content";
        m.write(src,content);
        assertTrue(i.exists(src));
        // neither the destination file, or the destinatio foldr exist..
        final URI dest = new URI("tmp://movefileNonExistent/filedest.txt");
        m.move(src,dest);
        assertFalse(i.exists(src));
        assertTrue(i.exists(dest));      
        assertEquals(content,m.read(dest));
    }
    
    
    public void testMoveOverwritesExistingFile() throws Exception {
        final URI src = new URI("tmp://moveFileOverwrite/file.txt");
        final String content = "some file content";
        m.write(src,content);
        assertTrue(i.exists(src));
        final URI dest = new URI("tmp://movefiledOverwritedest/filedest.txt");
        m.write(dest,"some other content");
        assertTrue(i.exists(dest));
        m.move(src,dest);
        assertFalse(i.exists(src));
        assertTrue(i.exists(dest));
        assertEquals(content,m.read(dest));        
    }
    
    public void testMoveConvertsFileToFolder() throws Exception {
        final URI srcFolder = new URI("tmp://moveConvert/");
        final URI src = srcFolder.resolve("file.txt");
        final String content = "some file content";
        m.write(src,content);
        assertTrue(i.exists(src));
        assertTrue(i.isFolder(srcFolder));
        
        final URI dest = new URI("tmp://moveConvertFile/filedest.txt");
        m.write(dest,"some other content");
        assertTrue(i.exists(dest));
        assertTrue(i.isFile(dest));
        m.move(srcFolder,dest);
        assertTrue(i.exists(dest));
        assertTrue(i.isFolder(dest));   
        m.refresh(src); // seems to be necessary, and has to be src, not srcFolder.
        assertFalse(i.exists(src));
    }
    
    
    public void testMoveEmptyFolder() throws Exception {
        final URI srcFolder = new URI("tmp://moveEmpty/");
        m.createFolder(srcFolder);
        assertTrue(i.isFolder(srcFolder));
        
        final URI dest = new URI("tmp://moveConvertFile/filedest.txt");
        m.move(srcFolder,dest);
        assertFalse(i.exists(srcFolder));
        assertTrue(i.exists(dest));
        assertTrue(i.isFolder(dest));           
    }
    
    public void testMoveNonexistentFile() throws Exception {
        final URI src = new URI("tmp://moveNonExistent/file.txt");
        assertFalse(i.exists(src));
        final URI dest = new URI("tmp://moveNonExistent/filedest.txt");
        try {
            m.move(src,dest);
            fail("expected to throw");
        } catch (final ACRException e) {
            // ok
        }
    }


    //Read    
    public void testReadNonExistent() throws Exception {
        final URI f = new URI("tmp://readWNonExistent/file.txt");
        try {
            m.read(f);
            fail("expected to chuck");
        } catch (final ACRException e) {
            // ok
        }
    }

    public void testReadFolder() throws Exception {
        final URI f = new URI("tmp://readFolder/folder.txt");
        m.createFolder(f);
        try {
            m.read(f);
            fail("expected to chuck");
        } catch (final ACRException e) {
            // ok
        }
    }
    
    public void testWriteFolder() throws Exception {
        final URI f = new URI("tmp://writeFoler/file.txt");
        m.createFolder(f);
        try {
            m.write(f,"summat");
            fail("expected to chuck");
        } catch (final ACRException e) {
            // ok
        }
    }
    
    
    public void testReadEmpty() throws Exception {
        final URI f = new URI("tmp://readEmpty/file.txt");
        m.createFile(f);
        assertEquals("",m.read(f));
    }
    
    public void testWriteNonExistent() throws Exception {
        final URI f = new URI("tmp://writeNonExistent/file.txt");
        final String s= "some text\n and some more";
        m.write(f,s);
        assertEquals(s,m.read(f));
    }
    
    public void testWriteEmpty() throws Exception {
        final URI f = new URI("tmp://writeEmpty/file.txt");
        m.createFile(f);
        final String s= "some text\n and some more";
        m.write(f,s);
        assertEquals(s,m.read(f));       
    }
    
    public void testOverwrite() throws Exception {
        final URI f = new URI("tmp://overwrite/file.txt");
        m.createFile(f);
        m.write(f,"initial text");
        final String s= "some text\n and some more";
        m.write(f,s);
        assertEquals(s,m.read(f));            
       
    }
    
    public void testReadBinary() throws Exception {
        final URI f = new URI("tmp://readBinaryfile.txt");
        m.createFile(f);
        final String s= "some text\n and some more";
        m.write(f,s);
        assertThat(m.readBinary(f),is(s.getBytes()));
    }

    public void testReadEmptyBinary() throws Exception {
        final URI f = new URI("tmp://readEmptyBinaryfile.txt");
        m.createFile(f);
        assertThat(m.readBinary(f),is(new byte[0]));
    }
    
    public void testReadWriteBinary() throws Exception {
        final URI f = new URI("tmp://readwriteBinaryfile.txt");
        m.createFile(f);
        final byte[] s= "some text\n and some more".getBytes();
        m.writeBinary(f,s);
        assertThat(m.readBinary(f),is(s));
    }
    
    public void testWriteBinary() throws Exception {
        final URI f = new URI("tmp://writeBinaryfile.txt");
        m.createFile(f);
        final String s= "some text\n and some more";
        m.writeBinary(f,s.getBytes());
        assertThat(m.read(f),is(s));
    }
    
    public void testAppendNonExistent() throws Exception {
        final URI f = new URI("tmp://testAppendNonExisting.txt");
        m.createFile(f);
        final String s= "some text\n and some more";
        m.append(f,s);
        assertThat(m.read(f),is(s));     
    }
    
    public void testApppedExisting() throws Exception {
        final URI f = new URI("tmp://testAppendExisting.txt");
        m.createFile(f);
        final String s= "some text\n and some more";
        m.write(f,s);
        assertThat(m.read(f),is(s));  
        
        // now append some more.
        final String extra = "and \n some more";
        m.append(f,extra);
        assertThat(m.read(f),is(s+extra));  
    }

    public void testAppendNonExistentBinary() throws Exception {
        final URI f = new URI("tmp://testAppendNonExistingBinary.txt");
        m.createFile(f);
        final String s= "some text\n and some more";
        m.appendBinary(f,s.getBytes());
        assertThat(m.read(f),is(s));            
    }
    
    public void testApppedExistingBinary() throws Exception {
        final URI f = new URI("tmp://testAppendExistingBinary.txt");
        m.createFile(f);
        final String s= "some text\n and some more";
        m.writeBinary(f,s.getBytes());
        assertThat(m.read(f),is(s));  
        
        // now append some more.
        final String extra = "and \n some more";
        m.appendBinary(f,extra.getBytes());
        assertThat(m.read(f),is(s+extra));        
    }

    

}
