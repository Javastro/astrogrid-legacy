/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs;

import java.io.File;
import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.FileUtil;
import org.apache.commons.vfs.UserAuthenticationData;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.provider.DelegateFileObject;
import org.apache.commons.vfs.util.UserAuthenticatorUtils;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.desktop.modules.system.VfsSystemTest;

/** Library tests for commons VFS.
 * Checks my understanding about the behaviour of this library
 * and it's integration into AR.
 * 
 * @todo write a subset of these tests into the automated build.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 3, 20071:00:22 PM
 */
public class VfsLibraryTest extends InARTestCase {

	private FileSystemManager vfs;

	protected void setUp() throws Exception {
		super.setUp();
	    vfs=  (FileSystemManager)assertComponentExists(FileSystemManager.class, "system.vfs");
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		vfs = null;
	}
	
	public static Test suite() {
	    return new ARTestSetup(new TestSuite(VfsLibraryTest.class),false);
	}
	/*
	public void testWorkingWithTmpFilesystem() throws Exception {
	    FileObject fo = vfs.resolveFile("tmp:/");
	    assertNotNull(fo);
        
    }*/
	
	public void testVirtual() throws Exception {
	 //   FileObject root = vfs.resolveFile("ram:/");
	    FileSystem virt = vfs.createVirtualFileSystem("nigel://").getFileSystem(); /* '//' necessary here, otherwise wont work */
	    
	    FileObject fn = vfs.resolveFile("http://www2.astrogrid.org");
	    virt.addJunction("/foo/bar",fn); // absolute path necesary here.
	   //FileObject f = vfs.resolveFile("nigel://foo/bar");
	    FileObject f = virt.resolveFile("/foo/bar");
	    assertTrue(f.exists());
	    System.out.println(f);
	    byte[] content = FileUtil.getContent(f);
	    System.out.println(new String(content));
	    
	    // can we traverse.
	    FileObject foo = f.getParent();
	    assertTrue(foo.exists());
	    System.out.println(Arrays.asList(foo.getChildren()));                 
        
	    // and the rest.
	    FileObject root = foo.getParent();
	    assertTrue(root.exists());
        System.out.println(Arrays.asList(root.getChildren()));   
        System.out.println(root);
        
        System.out.println("Root:" +  virt.getRootName());
        root = virt.getRoot();
        System.out.println(Arrays.asList(root.getChildren()));    
        
        
        // now can we resolve stuff from vfs itself??
        assertFalse(Arrays.asList(vfs.getSchemes()).contains("nigel")); // vfs not here :( - how can we add it?
        //FileObject sus = vfs.resolveFile("nigel:///");
        //System.out.println(Arrays.asList(sus.getChildren()));
        // throws - can't resolve it, as vfs not know.
        
        // can we create files in a virtual system??
        assertFalse(root.isWriteable()); // nope. can just mount.
        //root.resolveFile("nigel").createFolder();
      
        // can I mount a folder, and then manipulate things in this folder.
        FileObject tmp = vfs.resolveFile("tmp:/foo");
        tmp.createFolder();
        tmp.resolveFile("child").createFile();
        virt.addJunction("/foo/temporary",tmp);
        
        foo = virt.resolveFile("/foo");
        System.out.println(Arrays.asList(foo.getChildren()));  
        FileObject temporary = foo.getChild("temporary");
        System.out.println(temporary);
        System.out.println(temporary.isWriteable());
        System.out.println(Arrays.asList(temporary.getChildren()));
        temporary.resolveFile("child").delete();
        temporary.resolveFile("new child").createFile();
        System.out.println(Arrays.asList(temporary.getChildren()));   
        
        // no attibe
        System.out.println(Arrays.asList(temporary.resolveFile("new child").getContent().getAttributeNames()));
     //   System.out.println(Arrays.asList(temporary.getFileOperations().getOperations()));
        System.out.println(temporary.getName().getURI());
        // must be a new method - it's in the sources, but not in the lib.
       // not available in 1.0 - only in 1.1
        System.out.println(((DelegateFileObject)f).getDelegateFile().getName());
         // yep!! that's good.
        /*
         * conclusion - so we can create a file system - name of which isn't important,
         * bt must follow with //
         * can then mount files and folders in it, all good.
         * can traverse and navigate - however, can't resolve globally (which is a bit of a pain).
         * 
         * think I should be able to use this.would be easy to knock up a few virtual file systems
         * for astroscope results.
         *  - likewise, I can synthesize a virtual file system on top of the 'tasks' internal file system
         *      - which would allow me to add indirect results and direct results.
         *      
         */
        System.out.println(temporary.getClass().getName());
    }
	

    public void testSameScheme() throws Exception {
        // can we create two different & distinct virtfs with the same scheme.
        //   FileObject root = vfs.resolveFile("ram:/");
           FileSystem virt = vfs.createVirtualFileSystem("nigel://").getFileSystem(); /* '//' necessary here, otherwise wont work */
           assertNotNull(virt);
           FileSystem virt1 = vfs.createVirtualFileSystem("nigell://").getFileSystem();
           assertNotNull(virt1);
           assertNotSame(virt,virt1);
           assertFalse(virt1.equals(virt));
           
           // can we mount the same file in 2 vfs??
           FileObject fn = vfs.resolveFile("http://www2.astrogrid.org");
           virt.addJunction("/foo",fn); // absolute path necesary here.
           virt1.addJunction("/bar",fn);
           
           FileObject f = virt.resolveFile("/foo");
           assertTrue(f.exists());
           f = virt.resolveFile("/bar");
           assertFalse(f.exists());           
           
           f = virt1.resolveFile("/bar");
           assertTrue(f.exists());

           f = virt1.resolveFile("/foo");
           assertFalse(f.exists());
           
           
           
    }	
	
	   public void testMountWithinMountl() throws Exception {
	       // can we mount one virt fs wihin another one - even though they've got the same scheme..
           final FileSystem virt = vfs.createVirtualFileSystem("nigel://").getFileSystem(); /* '//' necessary here, otherwise wont work */
           assertNotNull(virt);
           final FileObject virt1Root = vfs.createVirtualFileSystem("nigell://");
           final FileSystem virt1 = virt1Root.getFileSystem();
           assertNotNull(virt1);
           assertNotSame(virt,virt1);
           assertFalse(virt1.equals(virt));
           
           
           virt.addJunction("/foo",virt1Root);
           
           FileObject fn = vfs.resolveFile("http://www2.astrogrid.org");
           virt1.addJunction("/bar",fn); // absolute path necesary here.
           
	          FileObject f = virt.resolveFile("/foo/bar");
	          assertTrue(f.exists());
	          System.out.println(f);
	          byte[] content = FileUtil.getContent(f);
	          System.out.println(new String(content));
	   }
	   
	   public void testGetRootFromVirtualFileSystem() throws Exception {
           final FileObject virt1Root = vfs.createVirtualFileSystem("nigell://");
           final FileSystem virt1 = virt1Root.getFileSystem();
           FileObject a = virt1.getRoot();
           assertSame(virt1Root,a);

        
    }
	   
       public void testVirtualRootIsFolder() throws Exception {
           // this test shows that a virtual root is imaginary until something is mounted in it.
           final FileObject a = vfs.createVirtualFileSystem("nigell://");
           final FileSystem virt1 = a.getFileSystem();

           assertEquals(FileType.IMAGINARY,a.getType());
           assertFalse(a.exists());
           // mount something
           virt1.addJunction("/fred",vfs.resolveFile("ram://foo/bar"));
                          
           assertEquals(FileType.FOLDER,a.getType());
           assertTrue(a.exists());
    }
	
	/*
	public void testConvertingTmpFileToVFS() throws Exception {
	    File f = File.createTempFile("foo","nar");
	    assertNotNull(f);
	    FileObject obj = vfs.resolveFile(f.toURI().toString());
	    assertNotNull(obj);
	    assertTrue(obj.exists());
	    obj.createFile();
	    assertTrue(obj.exists());
	    
    }
	
	// work with the temp file system as a testbed.
	public void testBasicCRUD() throws Exception {
		FileObject fo = vfs.resolveFile("tmp://foo");
		assertNotNull(fo);
	}
	
	public void testAuthentication() throws Exception {
		FileObject fo = vfs.resolveFile("tmp://bar");
		FileSystem fileSystem = fo.getFileSystem();
		System.out.println(fileSystem);
		FileSystemOptions o = fileSystem.getFileSystemOptions();
		System.out.println(o);
		UserAuthenticationData data = UserAuthenticatorUtils.authenticate(o,new UserAuthenticationData.Type[]{UserAuthenticationData.USERNAME});
		System.out.println(data);
	}

	*/
	
}
