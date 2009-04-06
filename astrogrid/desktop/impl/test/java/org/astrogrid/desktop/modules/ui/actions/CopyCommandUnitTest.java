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
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;

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
        final CopyCommand command = new CopyCommand(home);
        assertNull(command.getDestination());
        assertFalse(command.failed());
        final FileName n = createMock(FileName.class);
        command.recordSuccess(n);
        assertSame(n,command.getDestination());
        
        assertNotNull(command.formatResult());
        assertFalse(command.failed());
    }
    
    public void testError() throws Exception {
        final CopyCommand command = new CopyCommand(home);
        assertNull(command.getDestination());
        assertFalse(command.failed());
        final FileSystemException e= new FileSystemException("*!*");
        command.recordError(e);
        assertTrue(command.failed());
        assertNotNull(command.formatResult());
        assertTrue(command.formatResult().contains("*!*"));
    }


    public void testFromFile() throws Exception {
        final CopyCommand command = new CopyCommand(home);
        final FileObject fo = command.resolveSourceFileObject(vfs);
        assertNotNull(fo);
        assertEquals(homeObject,fo);
    }

    public void testFromFileObject() throws Exception {
        final FileObjectView v = new FileObjectView(homeObject,null);
        final CopyCommand command = new CopyCommand(v);
        final FileObject fo = command.resolveSourceFileObject(vfs);
        assertNotNull(fo);
        assertEquals(homeObject,fo);        
    }
    
    public void testFromURI() throws Exception {
        final CopyCommand command = new CopyCommand(home.toURI());
        final FileObject fo = command.resolveSourceFileObject(vfs);
        assertNotNull(fo);
        assertEquals(homeObject,fo);
        
    }
    
    public void testFromURL() throws Exception {
        final CopyCommand command = new CopyCommand(home.toURL());
        final FileObject fo = command.resolveSourceFileObject(vfs);
        assertNotNull(fo);
        assertEquals(homeObject,fo);
    }
    
}
