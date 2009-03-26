/**
 * 
 */
package org.astrogrid.desktop.thirdparty;

import static org.astrogrid.Fixture.createVFS;

import java.io.File;

import junit.framework.TestCase;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
/** Tests OUR configuration of VFS - not the default VFS setup.
 * our configuration contains some bug fixes, which in time may not be needed.
 * compare a new version by in <tt>setup()</tt> replacing <tt>createVFS</tt> with
 * <tt>VFS.getManager</tt>
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 21, 20089:45:38 AM
 */
public class VfsUnitTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        vfs = createVFS();
    }
    
    private FileSystemManager vfs;

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        vfs = null;
    }
    
    public void testWorkingWithTmpFilesystem() throws Exception {
        FileObject fo = vfs.resolveFile("tmp:/");
        assertNotNull(fo);
        
    }
  
  
  public void testConvertingTmpFileToVFS() throws Exception {
      File f = File.createTempFile("foo","nar");
      assertNotNull(f);
      FileObject obj = vfs.resolveFile(f.toURI().toString());
      assertNotNull(obj);
      assertTrue(obj.exists());
      obj.createFile();
      assertTrue(obj.exists());
      
  }
  /** verify various ways of converting between files and file objects 
   * 
   * for a File f, 
   * vfs.resolveFile(f,"."), 
   * vfs.resolveFile(f.toURI().toString(),
   * vfs.resolveFile(f.toString()),
   * and vfs.resolveFile(f.toURL().toString()) are all equivalent
   * 
   * */
  public void testResolvingFileToFileObject() throws Exception {
    File f = File.createTempFile("foo","bar");
    assertNotNull(f);
    FileObject[] fo = new FileObject[]{
            vfs.resolveFile(f,".")
            ,vfs.resolveFile(f.toURI().toString())
            ,vfs.resolveFile(f.toString())
            ,vfs.resolveFile(f.toURL().toString())
        };
                    
    for (int i = 0; i < fo.length; i++) {
        FileObject o  = fo[i];
        assertNotNull(o);
        if (i > 0) {
            assertEquals("not equal",o,fo[i-1]);
            assertSame("not same",o,fo[i-1]);
            
        }        
    }
  }

    
    
}
