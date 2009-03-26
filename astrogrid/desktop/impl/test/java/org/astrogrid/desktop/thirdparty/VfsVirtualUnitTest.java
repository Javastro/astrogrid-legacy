/**
 * 
 */
package org.astrogrid.desktop.thirdparty;

import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.provider.DelegateFileObject;
import org.astrogrid.desktop.modules.ui.actions.BulkCopyWorkerUnitTest;
import junit.framework.TestCase;
import static org.astrogrid.Fixture.*;
/** 
 * Tests the behaviour of VFS virtual filesystems.
 * 
 * Tests OUR configuration of VFS - not the default VFS setup.
 * our configuration contains some bug fixes, which in time may not be needed.
 * compare a new version by in <tt>setup()</tt> replacing <tt>createVFS</tt> with
 * <tt>VFS.getManager</tt>
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 21, 20089:45:38 AM
 */
public class VfsVirtualUnitTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        vfs = createVFS();
        URL file = BulkCopyWorkerUnitTest.class.getResource("test-resources/something.txt");
        assertNotNull(file);
        existingFileObject = vfs.resolveFile(file.toString());
        assertNotNull(existingFileObject);
        assertTrue(existingFileObject.exists());
        assertTrue(existingFileObject.getType().hasContent());
    }
    
    private FileSystemManager vfs;
    private FileObject existingFileObject;

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        vfs = null;
        existingFileObject = null;
    }
    
//tests related to virtual.
    
    /** virtual filesystem must be created in form 'scheme://' 
       * name isn't important, but must follow with //
       * can then mount files and folders in it, all good.
       * can traverse and navigate - however, can't resolve globally (which is a bit of a pain).
       * and can access the actual files by casting.
     * */
  public void testVirtual() throws Exception {
      FileSystem virt = vfs.createVirtualFileSystem("nigel://").getFileSystem(); /* '//' necessary here, otherwise wont work */
     
      // splice a file into the virtual file system.
      virt.addJunction("/foo/bar",existingFileObject); // absolute path necesary here.
      
      // reslve the file we've just spliced
      FileObject virtualFileObject = virt.resolveFile("/foo/bar");
      
      exists(virtualFileObject);
      isFile(virtualFileObject);
      // and it's scheme should be the virtual one.
      assertTrue(virtualFileObject.getName().toString().startsWith("nigel://"));
     
      // check the content is the same.
      contentSameAsExistingFile(virtualFileObject);      

      // can we traverse.      
      final FileObject virtualParent = virtualFileObject.getParent();
      exists(virtualParent);
      isFolder(virtualParent);      
      // and it's scheme should be the virtual one.
      assertTrue(virtualParent.getName().toString().startsWith("nigel://"));    
      
      // and the virtual file object we started from should be in it's children.
      parentContainsChild(virtualParent, virtualFileObject);
                    
      
      // and now go higher.
      final FileObject virtualRoot = virtualParent.getParent();
      exists(virtualRoot);
      isFolder(virtualRoot);      
      // and it's scheme should be the virtual one.
      assertTrue(virtualRoot.getName().toString().startsWith("nigel://"));    
      
      parentContainsChild(virtualRoot, virtualParent);

      // and it's the root.
      assertEquals("nigel:///",virt.getRootName().toString());
      assertEquals(virt.getRootName(),virtualRoot.getName());
      assertEquals(virt.getRoot(),virtualRoot);
      assertSame(virt.getRoot(),virtualRoot);
      
      
      // now can we resolve virtual filesystems  from vfs itself??
      
      // the virtual filesystem is not listed among the known schemes
      assertFalse(ArrayUtils.contains(vfs.getSchemes(),"nigel"));
      
      // cant resolve virtual files by name either.
      try {
          FileObject sus = vfs.resolveFile("nigel:///");
          fail("Expected to fail");
      } catch (FileSystemException e) {
          // expected
      }
            
      // can we create files in a virtual system??
      assertFalse(virtualRoot.isWriteable()); // nope. can just mount.
    
      // can I mount a folder, and then manipulate things in this folder.
      // create a folder first.
      FileObject tmpFolder = vfs.resolveFile("tmp:/foo");
          assertNotNull(tmpFolder);
          tmpFolder.createFolder();
          exists(tmpFolder);
          tmpFolder.resolveFile("child").createFile();
          assertEquals(1,tmpFolder.getChildren().length);
      // mount it.
      virt.addJunction("/foo/temporary",tmpFolder);
      
      FileObject virtualFolder = virt.resolveFile("/foo/temporary");
      assertNotNull(virtualFolder);
      parentContainsChild(virtualParent,virtualFolder);
      assertTrue(virtualFolder.isWriteable());
      assertEquals(1,virtualFolder.getChildren().length);
      
      // try a deletion.
      virtualFolder.resolveFile("child").delete();
      assertEquals(0,virtualFolder.getChildren().length);
      // and does this map onto actual file system? - YES
      assertEquals(0,tmpFolder.getChildren().length);

      // and a creation.
      virtualFolder.resolveFile("new child").createFile();
      assertEquals(1,virtualFolder.getChildren().length);
      assertEquals(1,tmpFolder.getChildren().length);
      
      // no attributes
      assertEquals(0,virtualFolder.resolveFile("new child").getContent().getAttributeNames().length);
        
      // access the concrete object from virtual one.
      assertTrue(virtualFileObject instanceof DelegateFileObject);
      assertTrue(virtualFolder instanceof DelegateFileObject);
      
      assertSame(existingFileObject,((DelegateFileObject)virtualFileObject).getDelegateFile());
      assertSame(tmpFolder,((DelegateFileObject)virtualFolder).getDelegateFile());
       
  }

    /**
     * @param fo
     * @throws IOException
     * @throws FileSystemException
     */
    private void contentSameAsExistingFile(FileObject fo) throws IOException,
            FileSystemException {
        assertTrue("content differs",IOUtils.contentEquals(
                  existingFileObject.getContent().getInputStream()
                  ,fo.getContent().getInputStream()
                  ));
    }

    /**
     * @param parent
     * @param child
     * @throws FileSystemException
     */
    private void parentContainsChild(FileObject parent, FileObject child)
            throws FileSystemException {
        assertTrue(ArrayUtils.contains(parent.getChildren(),child));
    }

    /**
     * @param fo
     * @throws FileSystemException
     */
    private void isFolder(FileObject fo) throws FileSystemException {
        assertTrue(fo.getType().hasChildren());
    }

    /**
     * @param fo
     * @throws FileSystemException
     */
    private void isFile(FileObject fo) throws FileSystemException {
        assertTrue(fo.getType().hasContent());
    }

    /**
     * @param fo
     * @throws FileSystemException
     */
    private void exists(FileObject fo) throws FileSystemException {
        assertTrue(fo.exists());
    }
  

    /** Q:can we create two different & distinct virtual fs with the same scheme?
     * A: Yes.
     * 
     * @throws Exception
     */
  public void testSameScheme() throws Exception {
         FileSystem virt = vfs.createVirtualFileSystem("nigel://").getFileSystem(); /* '//' necessary here, otherwise wont work */           
         assertNotNull(virt);
         FileSystem virt1 = vfs.createVirtualFileSystem("nigell://").getFileSystem();
         assertNotNull(virt1);
         assertNotSame(virt,virt1);
         assertFalse(virt1.equals(virt));
         
         // can we mount the same file in 2 vfs??
         virt.addJunction("/foo",existingFileObject); // absolute path necesary here.
         virt1.addJunction("/bar",existingFileObject);
         
         FileObject f = virt.resolveFile("/foo");
         exists(f);
         f = virt.resolveFile("/bar");
         assertFalse(f.exists());           
         
         f = virt1.resolveFile("/bar");
         exists(f);

         f = virt1.resolveFile("/foo");
         assertFalse(f.exists());                           
  }   
  
  /** Q:can we mount one virt fs wihin another one - even though they've got the same scheme
   * A: Yes
   */
     public void testMountWithinMountl() throws Exception {
         final FileSystem virt = vfs.createVirtualFileSystem("nigel://").getFileSystem(); /* '//' necessary here, otherwise wont work */
         assertNotNull(virt);
         final FileObject virt1Root = vfs.createVirtualFileSystem("nigell://");
         final FileSystem virt1 = virt1Root.getFileSystem();
         assertNotNull(virt1);
         assertNotSame(virt,virt1);
         assertFalse(virt1.equals(virt));
         
         
         virt.addJunction("/foo",virt1Root);
         
         virt1.addJunction("/bar",existingFileObject); // absolute path necesary here.
         
            FileObject f = virt.resolveFile("/foo/bar");
            exists(f);
            contentSameAsExistingFile(f);
     }
     
     public void testGetRootFromVirtualFileSystem() throws Exception {
         final FileObject virt1Root = vfs.createVirtualFileSystem("nigell://");
         final FileSystem virt1 = virt1Root.getFileSystem();
         FileObject a = virt1.getRoot();
         assertSame(virt1Root,a);
      
  }
     
     /** this test shows that a virtual root is imaginary until something is mounted in it.
      * 
      * @throws Exception
      */
     public void testVirtualRootIsFolder() throws Exception {
         final FileObject a = vfs.createVirtualFileSystem("nigell://");
         final FileSystem virt1 = a.getFileSystem();

         assertEquals(FileType.IMAGINARY,a.getType());
         assertFalse(a.exists());
         // mount something
         virt1.addJunction("/fred",existingFileObject);
                        
         assertEquals(FileType.FOLDER,a.getType());
         exists(a);
  }
  
    
}
