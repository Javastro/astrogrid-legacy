/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import static org.astrogrid.Fixture.*;
import static org.easymock.EasyMock.*;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.desktop.alternatives.HeadlessUIComponent;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.UIComponent;

/** Unit test for the bulk copy worker.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 18, 200810:52:14 AM
 * @todo prevent result dialog from being displayed.
 */
public class BulkCopyWorkerUnitTest extends TestCase {

    private UIComponent ui;
    FileSystemManager vfs;
    File saveDir;
    private UIContext cxt;
    private CopyCommand dirCommand;
    private CopyCommand fileCommand;
    private CopyCommand nonexistentCommand;
    private FileObject dirFO;
    private FileObject fileFO;
    private FileObject neFO;

    protected void setUp() throws Exception {
        System.setProperty("unit.testing","true"); // flag to prevent dialogue being displayed
        super.setUp();
        vfs = createVFS();
        cxt = createMockContext();
        replay(cxt);
        this.ui = new HeadlessUIComponent("test",cxt);
        
        saveDir = createTempDir(this.getClass());
        
        // different kinds of file to copy
        URL dir = this.getClass().getResource("test-resources");
        assertNotNull(dir);
        URL file = this.getClass().getResource("test-resources/something.txt");
        assertNotNull(file);
        URL nonexistent = new URL(dir,"nonexistent.txt");
        // create a set of commands
        this.dirCommand = new CopyCommand(dir);
        this.fileCommand = new CopyCommand(file);
        this.nonexistentCommand = new CopyCommand(nonexistent);
        
        // validate the file objects pointed to by these commands, and store for use in tests.
        dirFO = dirCommand.resolveSourceFileObject(vfs);        
        assertNotNull(dirFO);
        assertTrue(dirFO.exists());
        fileFO= fileCommand.resolveSourceFileObject(vfs);
        assertNotNull(fileFO);
        assertTrue(fileFO.exists());        
        neFO = nonexistentCommand.resolveSourceFileObject(vfs);
        assertNotNull(neFO);
        assertFalse(neFO.exists());        
    }
    

    protected void tearDown() throws Exception {
        super.tearDown();
        FileUtils.forceDelete(saveDir);
        vfs = null;
        ui = null;
        cxt = null;
        dirCommand = null;
        fileCommand = null;
        nonexistentCommand = null;
        fileFO = null;
        neFO = null;
        dirFO = null;
    }
    
    /** if nothing to copy, it still proceeds correctly */
    public void testNullCopy() throws Exception {
       BulkCopyWorker worker = new BulkCopyWorker(vfs,ui,saveDir,new CopyCommand[0]);
       worker.start();     
       assertTrue(saveDir.exists());
       assertEquals(0,saveDir.list().length);
    }
    
    /** if nothing to copy, it still proceeds correctly
     * but if dest destination doesn't exist, it's created*/    
    public void testNullCopyNonExistentDest() throws Exception {
        saveDir.delete();
        assertFalse(saveDir.exists());
        BulkCopyWorker worker = new BulkCopyWorker(vfs,ui,saveDir,new CopyCommand[0]);
        worker.start();  
        assertTrue(saveDir.exists());
     }
    
// single kinds of file.    
    /**single copy of a file.*/
    public void testSingleCopy() throws Exception {
       BulkCopyWorker worker = new BulkCopyWorker(vfs,ui,saveDir,new CopyCommand[]{
               fileCommand
       });
       worker.start();   
       assertTrue(saveDir.exists());
       assertEquals(1,saveDir.list().length);
       validateFileWritten();
    }


    /** check that fileCommand has succeeded
     * @throws IOException
     * @throws FileSystemException
     */
    private void validateFileWritten() throws IOException, FileSystemException {
           
           // check that the command records the correct info.
           assertFalse("file command reports as failed",fileCommand.failed());
           FileName destination = fileCommand.getDestination();
           assertNotNull("no destination",destination);
           assertEquals("reported destination different to what's on disk",saveDir.list()[0],destination.getBaseName());
        assertEquals("destination filename differs to source filename"
                   ,fileFO.getName().getBaseName()
                   ,destination.getBaseName()
                   );
           // check the content
           assertTrue("content differs",IOUtils.contentEquals(
                   fileFO.getContent().getInputStream()
                   ,vfs.resolveFile(destination.getURI()).getContent().getInputStream()
                   ));
    }
    /** test with non-folder destination - expect error */
    public void testSingleCopyFileDestination() throws Exception {
        saveDir.delete();
        FileUtils.touch(saveDir);
        assertTrue(saveDir.exists());
        assertTrue(saveDir.isFile());
        BulkCopyWorker worker = new BulkCopyWorker(vfs,ui,saveDir,new CopyCommand[]{
                fileCommand
        });
        worker.start();   
        assertTrue(saveDir.exists());
        assertTrue(saveDir.isFile());               
        // check that the command records the correct info.
        assertTrue("expected to fail",fileCommand.failed());        
     }    
    
    /** test with non-existent destination. - expect creation - i.e. same behaviour as single copy. */
    public void testSingleCopyNonExistentDest() throws Exception {
        saveDir.delete();
        assertFalse(saveDir.exists());
        testSingleCopy();      
     }

    /** test with file object destination */
    public void testSingleCopyFileObjectDestination() throws Exception {
        BulkCopyWorker worker = new BulkCopyWorker(vfs,ui,vfs.resolveFile(saveDir,"."),new CopyCommand[]{
                fileCommand
        });
        worker.start();   
        assertTrue(saveDir.exists());
        assertEquals(1,saveDir.list().length);        
        validateFileWritten();
     }    
    /**  test with url destination */
    public void testSingleCopyURIDestination() throws Exception {
        BulkCopyWorker worker = new BulkCopyWorker(vfs,ui,saveDir.toURI(),new CopyCommand[]{
                fileCommand
        });
        worker.start();   
        assertTrue(saveDir.exists());
        assertEquals(1,saveDir.list().length);        
        validateFileWritten();
     }    
    
    /** single copy of non-existent file */
    public void testSingleCopyNonExistentSource() throws Exception {
        BulkCopyWorker worker = new BulkCopyWorker(vfs,ui,saveDir,new CopyCommand[]{
                nonexistentCommand
        });
        worker.start();   
        assertTrue(saveDir.exists());
        assertEquals(0,saveDir.list().length);
        
        // check that the command records the correct info.
        assertTrue("expected as failed",nonexistentCommand.failed());     
     }    

        
    /** single copy of a folder - should copy contents too.
     * @FIXME alter impl of copy worker to make this test pass */
    public void testSingleCopyFolder() throws Exception {
        BulkCopyWorker worker = new BulkCopyWorker(vfs,ui,saveDir,new CopyCommand[]{
                dirCommand
        });
        worker.start();     
        assertTrue(saveDir.exists());
        assertEquals(1,saveDir.list().length);
        
        // check that the command records the correct info.
        assertFalse("dir command reports as failed",dirCommand.failed());
        FileName destination = dirCommand.getDestination();
        assertNotNull("no destination",destination);
        assertEquals("reported destination different to what's on disk",saveDir.list()[0],destination.getBaseName());
     assertEquals("destination filename differs to source filename"
                ,dirFO.getName().getBaseName()
                ,destination.getBaseName()
                );
     FileObject destinationFO = vfs.resolveFile(destination.getURI());
     assertNotNull(destinationFO);
     assertTrue(destinationFO.exists()); 
     assertTrue(destinationFO.getType().hasChildren());
     // now verify children of the file object.
     assertEquals(0,destinationFO.getChildren().length); //@fixme should copy all directory contents.
     assertEquals(fileFO,destinationFO.getChildren()[0]);     
        // check the content
        assertTrue("content differs",IOUtils.contentEquals(
                fileFO.getContent().getInputStream()
                ,destinationFO.getChildren()[0].getContent().getInputStream()
                ));
     }    
    
    // don't need to test with different ways of specifying a  source (url, uri, file, fileobject) 
    // as CommandUnitTest makes it clear we always get a fileObject back
    
    // test a list copy, with one non-existend
    // - expect rest to succed - no matter which order.
    
    public void testListContainingNonExistentA() throws Exception {
        BulkCopyWorker worker = new BulkCopyWorker(vfs,ui,saveDir,new CopyCommand[]{
                nonexistentCommand
                , fileCommand
        });
        worker.start();  
        assertTrue(saveDir.exists());
        assertEquals(1,saveDir.list().length);
        validateFileWritten();
     }
    public void testListContainingNonExistentB() throws Exception {
        BulkCopyWorker worker = new BulkCopyWorker(vfs,ui,saveDir,new CopyCommand[]{
                fileCommand
                ,nonexistentCommand
        });
        worker.start();  
        assertTrue(saveDir.exists());
        assertEquals(1,saveDir.list().length);
        validateFileWritten();
     }
    
    
    // test a list copy, with multiple commands to the same file
    // - expect multiple copies, with varied names.
    
    public void testListDuplicate() throws Exception {
        CopyCommand fileCommand1 = new CopyCommand(fileFO);
        BulkCopyWorker worker = new BulkCopyWorker(vfs,ui,saveDir,new CopyCommand[]{
                fileCommand
                , fileCommand1
        });
        worker.start();   
        assertTrue(saveDir.exists());
        assertEquals(2,saveDir.list().length);
        // check that the command records the correct info.
        assertFalse("file command reports as failed",fileCommand.failed());
        assertFalse("file command1 reports as failed",fileCommand1.failed());
        FileName destination = fileCommand.getDestination();
        assertNotNull("no destination",destination);
        FileName destination1 = fileCommand1.getDestination();
        assertNotNull("no destination",destination1);        
        assertFalse("both commands written to same destination",destination.equals(destination1));
        assertTrue("reported destination differs to what's on disk",ArrayUtils.contains(saveDir.list(),destination.getBaseName()));
        assertTrue("reported destination differs to what's on disk",ArrayUtils.contains(saveDir.list(),destination1.getBaseName()));
        // check the content
        assertTrue("content differs",IOUtils.contentEquals(
                fileFO.getContent().getInputStream()
                ,vfs.resolveFile(destination.getURI()).getContent().getInputStream()
                ));
        // check the content
        assertTrue("content differs",IOUtils.contentEquals(
                fileFO.getContent().getInputStream()
                ,vfs.resolveFile(destination1.getURI()).getContent().getInputStream()
                ));        
     }
    
    //test a list, containing a save-as.
    public void testListDuplicateSaveAs() throws Exception {
        final String savedAs = "savedAs.txt";
        CopyCommand fileCommand1 = new CopyAsCommand(fileFO,savedAs);
        BulkCopyWorker worker = new BulkCopyWorker(vfs,ui,saveDir,new CopyCommand[]{
                fileCommand
                , fileCommand1
        });
        worker.start();   
        assertTrue(saveDir.exists());
        assertEquals(2,saveDir.list().length);
        // check that the command records the correct info.
        assertFalse("file command reports as failed",fileCommand.failed());
        assertFalse("file command1 reports as failed",fileCommand1.failed());
        FileName destination = fileCommand.getDestination();
        assertNotNull("no destination",destination);
        FileName destination1 = fileCommand1.getDestination();        
        assertNotNull("no destination",destination1);        
        assertFalse("both commands written to same destination",destination.equals(destination1));
        assertTrue("reported destination differs to what's on disk",ArrayUtils.contains(saveDir.list(),destination.getBaseName()));
        assertTrue("reported destination differs to what's on disk",ArrayUtils.contains(saveDir.list(),destination1.getBaseName()));
        // destination should be same as original filename
        assertEquals(fileFO.getName().getBaseName(),destination.getBaseName());
        // destination1 should be the saved-as one.
        assertEquals(savedAs,destination1.getBaseName());
        // check the content
        assertTrue("content differs",IOUtils.contentEquals(
                fileFO.getContent().getInputStream()
                ,vfs.resolveFile(destination.getURI()).getContent().getInputStream()
                ));
        // check the content
        assertTrue("content differs",IOUtils.contentEquals(
                fileFO.getContent().getInputStream()
                ,vfs.resolveFile(destination1.getURI()).getContent().getInputStream()
                ));        
     }    

    

    

}