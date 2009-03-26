/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import static org.astrogrid.Fixture.createVFS;
import static org.easymock.EasyMock.createMock;

import java.io.File;

import junit.framework.TestCase;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;

/** unit test for the copy command.
 * 
 * main point is to test that no matter how the object is constructed, the accessible fileObject
 * is the ssame.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 18, 200812:08:29 PM
 */
public class CopyCommandUnitTest extends TestCase {

    private File home;
    private FileObject homeObject;
    private FileSystemManager vfs;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.home = SystemUtils.getUserHome();
        
        vfs = createVFS();
        this.homeObject = vfs.resolveFile(home,".");
    }
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testDestination() throws Exception {
        CopyCommand command = new CopyCommand(home);
        assertNull(command.getDestination());
        assertFalse(command.failed());
        FileName n = createMock(FileName.class);
        command.recordSuccess(n);
        assertSame(n,command.getDestination());
        
        assertNotNull(command.formatResult());
        assertFalse(command.failed());
    }
    
    public void testError() throws Exception {
        CopyCommand command = new CopyCommand(home);
        assertNull(command.getDestination());
        assertFalse(command.failed());
        FileSystemException e= new FileSystemException("*!*");
        command.recordError(e);
        assertTrue(command.failed());
        assertNotNull(command.formatResult());
        assertTrue(command.formatResult().contains("*!*"));
    }


    public void testFromFile() throws Exception {
        CopyCommand command = new CopyCommand(home);
        FileObject fo = command.resolveSourceFileObject(vfs);
        assertNotNull(fo);
        assertEquals(homeObject,fo);
    }

    public void testFromFileObject() throws Exception {
        CopyCommand command = new CopyCommand(homeObject);
        FileObject fo = command.resolveSourceFileObject(vfs);
        assertNotNull(fo);
        assertEquals(homeObject,fo);        
    }
    
    public void testFromURI() throws Exception {
        CopyCommand command = new CopyCommand(home.toURI());
        FileObject fo = command.resolveSourceFileObject(vfs);
        assertNotNull(fo);
        assertEquals(homeObject,fo);
        
    }
    
    public void testFromURL() throws Exception {
        CopyCommand command = new CopyCommand(home.toURL());
        FileObject fo = command.resolveSourceFileObject(vfs);
        assertNotNull(fo);
        assertEquals(homeObject,fo);
    }
    
}
