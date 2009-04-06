/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileUtil;
import org.apache.commons.vfs.Selectors;
import org.apache.commons.vfs.VFS;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;
import org.astrogrid.desktop.modules.ui.fileexplorer.FilesList;

import junit.framework.TestCase;

/** Test of the attach-delaying behaviour of astroscopeFileobject
 * create a lazy astroscope file object, and then test each of the rendering
 * codes that presents the file object information in the ui.
 * if all is well, the object should still be unattached at the end of the rendering.
 * also tests my assumptions about how vfs works.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 6, 20079:44:42 AM
 */
public class AstroscopeFileObjectUnitTest extends TestCase {

    private FileSystemManager vfs;
    private File f;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.vfs = VFS.getManager();
        f = File.createTempFile("AstroscopeFileObjectUnitTest",".txt");
        f.deleteOnExit();
        assertTrue(f.exists());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        vfs  = null;
    }

    /** check usual behaviour for file objects
     * - start off unattached
     * @throws Exception
     */
    public void testUsualBehaviour() throws Exception {
        final FileObject fo = vfs.resolveFile(f.toURI().toString());
        assertNotNull(fo);
        // not attached on creation
        assertFalse(fo.isAttached());
        // checking type forces attach
       assertTrue(fo.getType().hasContent());
       assertTrue(fo.isAttached());
    }
    public void testBasics() throws Exception {
        final FileObject fo = vfs.resolveFile(f.toURI().toString());
        assertNotNull(fo);
        final AstroscopeFileObject afo = new AstroscopeFileObject(fo,10,100,"text/html");
        // not attached on creation
        assertFalse(fo.isAttached());
        assertFalse(afo.isAttached());
        // checking type still doesn't attach - as the result is hard-coded
       assertTrue(afo.getType().hasContent());
       assertEquals(10,afo.getContent().getSize());
       assertEquals(100,afo.getContent().getLastModifiedTime());
       assertEquals("text/html",afo.getContent().getContentInfo().getContentType());
       assertFalse(afo.isAttached());
      
    }
    public void testReadContent() throws Exception {
        final String msg = "hello world";
        final long len = msg.getBytes().length;
        FileUtils.writeStringToFile(f,msg);
        
        final FileObject fo = vfs.resolveFile(f.toURI().toString());
        assertNotNull(fo);
        final AstroscopeFileObject afo = new AstroscopeFileObject(fo,len,100,"text/plain");
        // not attached on creation
        assertFalse(fo.isAttached());
        assertFalse(afo.isAttached());
        // checking type still doesn't attach - as the result is hard-coded
       assertTrue(afo.getType().hasContent());
       assertEquals(len,afo.getContent().getSize());
       assertEquals(100,afo.getContent().getLastModifiedTime());
       assertEquals("text/plain",afo.getContent().getContentInfo().getContentType());
       assertFalse(afo.isAttached());
       // ok. now attempt to read the file.
       final byte[] arr = FileUtil.getContent(afo);
       assertEquals(msg,new String(arr));
      
       // try reading the file manually.
       assertEquals(msg,IOUtils.toString(afo.getContent().getInputStream()));
    } 
    /** if size is incoprrect, only part of the content is read */
    public void testReadContentWrongSize() throws Exception {
        final String msg = "hello world";
        final long len = msg.getBytes().length -3;
        FileUtils.writeStringToFile(f,msg);
        
        final FileObject fo = vfs.resolveFile(f.toURI().toString());
        assertNotNull(fo);
        final AstroscopeFileObject afo = new AstroscopeFileObject(fo,len,100,"text/plain");
        // not attached on creation
        assertFalse(fo.isAttached());
        assertFalse(afo.isAttached());
        // checking type still doesn't attach - as the result is hard-coded
       assertTrue(afo.getType().hasContent());
       assertEquals(len,afo.getContent().getSize());
       assertEquals(100,afo.getContent().getLastModifiedTime());
       assertEquals("text/plain",afo.getContent().getContentInfo().getContentType());
       assertFalse(afo.isAttached());
       // ok. now attempt to read the file.
       final byte[] arr = FileUtil.getContent(afo);
       assertEquals(msg.substring(0,(int)len),new String(arr)); // we've read in only a portion of the file.
       // ==> file Utils respects 'size'
       
       // what happens we read it manally
       assertEquals(msg,IOUtils.toString(afo.getContent().getInputStream()));   
       //===> reading the stream gives us all the content, no matter what sz says
       // ---> don't use fileUtils.
    }
   
    public void testCopyContent() throws Exception {
        final String msg = "hello world";
        final long len = msg.getBytes().length;        
        FileUtils.writeStringToFile(f,msg);
        
        final FileObject fo = vfs.resolveFile(f.toURI().toString());
        assertNotNull(fo);
        final AstroscopeFileObject afo = new AstroscopeFileObject(fo,len,100,"text/plain");
        // not attached on creation
        assertFalse(fo.isAttached());
        assertFalse(afo.isAttached());

        // check we can copy the content.
        final File targetFile = File.createTempFile("AstroscopeFileObjectTestTarget",".txt");
        assertTrue(targetFile.exists());
        final FileObject target = vfs.resolveFile(targetFile.toURI().toString());
        target.copyFrom(afo,Selectors.SELECT_ALL);
        assertTrue(target.exists());
        // ok. now attempt to read the file.
        // ok. now attempt to read the file.
        final byte[] arr = FileUtil.getContent(afo);
        assertEquals(msg,new String(arr)); 
        // try reading the file manually.
        assertEquals(msg,IOUtils.toString(afo.getContent().getInputStream()));        
    }
    
    public void testCopyContentWrongSize() throws Exception {
        final String msg = "hello world";
        final long len = msg.getBytes().length -3;        
        FileUtils.writeStringToFile(f,msg);
        
        final FileObject fo = vfs.resolveFile(f.toURI().toString());
        assertNotNull(fo);
        final AstroscopeFileObject afo = new AstroscopeFileObject(fo,len,100,"text/plain");
        // not attached on creation
        assertFalse(fo.isAttached());
        assertFalse(afo.isAttached());

        // check we can copy the content.
        final File targetFile = File.createTempFile("AstroscopeFileObjectTestTarget",".txt");
        assertTrue(targetFile.exists());
        final FileObject target = vfs.resolveFile(targetFile.toURI().toString());
        target.copyFrom(afo,Selectors.SELECT_ALL);
        assertTrue(target.exists());
        // ok. now attempt to read the file.
        // ok. now attempt to read the file.
        final byte[] arr = FileUtil.getContent(afo);
        
        assertEquals(msg.substring(0,(int)len),new String(arr)); // we've read in only a portion of the file.
        // ==> file Utils respects 'size'
        
        // what happens we read it manally
        assertEquals(msg,IOUtils.toString(afo.getContent().getInputStream()));   
        //===> reading the stream gives us all the content, no matter what sz says
        // ---> don't use fileUtils.
    }
    
    public void testFilesList() throws Exception {
        final FileObject fo = vfs.resolveFile(f.toURI().toString());
        assertNotNull(fo);
        final AstroscopeFileObject afo = new AstroscopeFileObject(fo,10,100,"text/html");
        // not attached on creation
        assertFalse(fo.isAttached());
        assertFalse(afo.isAttached());

       //calls used in FilesList...
       afo.getName().getBaseName();
       assertFalse(afo.isAttached());       
       FilesList.createToolTipFromFileObject(new FileObjectView(afo, null));
       assertFalse(afo.isAttached());
  
    }
    public void testFilesTable() throws Exception {
        final FileObject fo = vfs.resolveFile(f.toURI().toString());
        assertNotNull(fo);
        final AstroscopeFileObject afo = new AstroscopeFileObject(fo,10,100,"text/html");
        // not attached on creation
        assertFalse(fo.isAttached());
        assertFalse(afo.isAttached());
        
        // calls used in filestable.
        afo.getName().getBaseName();
        afo.getContent();
        assertFalse("content causes attach",afo.isAttached());        
        afo.getContent().getLastModifiedTime();
        afo.getContent().getSize();
        afo.getContent().getContentInfo().getContentType();

        assertFalse(afo.isAttached());
     
    }
    public void testIconFinder() throws Exception {
        final FileObject fo = vfs.resolveFile(f.toURI().toString());
        assertNotNull(fo);
        final AstroscopeFileObject afo = new AstroscopeFileObject(fo,10,100,"text/html");
        // not attached on creation
        assertFalse(fo.isAttached());

       // used in incon finder.
       afo.getContent();
       assertFalse("content forces attach",afo.isAttached());
       afo.getContent().getSize();
       assertFalse(afo.isAttached());
       afo.getContent().getContentInfo();
       assertFalse(afo.isAttached());       
       afo.getContent().getContentInfo().getContentType();
       assertFalse(afo.isAttached());       
    }
}
