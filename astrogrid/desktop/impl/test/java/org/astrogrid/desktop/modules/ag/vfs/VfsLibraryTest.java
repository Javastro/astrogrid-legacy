/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.FileUtil;
import org.apache.commons.vfs.UserAuthenticationData;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.provider.DelegateFileObject;
import org.apache.commons.vfs.provider.URLFileName;
import org.apache.commons.vfs.provider.URLFileNameParser;
import org.apache.commons.vfs.provider.UriParser;
import org.apache.commons.vfs.provider.url.UrlFileObject;
import org.apache.commons.vfs.provider.url.UrlFileSystem;
import org.apache.commons.vfs.util.UserAuthenticatorUtils;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.desktop.modules.system.VfsSystemTest;
import org.astrogrid.io.Piper;

/** Library tests for commons VFS.
 * Checks my understanding about the behaviour of this library
 * and it's integration into AR.
 * 
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
	
	private static class MyUrlFileObject extends UrlFileObject {
	/**
         * @param fs
         * @param fileName
         */
        public MyUrlFileObject(UrlFileSystem fs, FileName fileName) {
            super(fs, fileName);
        }
    }
    final static  String s = "http://vizier.u-strasbg.fr/viz-bin/votable/-dtd/-A?-source=J/A+A/382/60/table4";
    final static  String s1 = "http://vizier.u-strasbg.fr/viz-bin/votable/-dtd/-A?-source=J/A+A/382/60/table5";

    /** so resolving a http url, we get a correct file object */
	public void testResolvingHttpFileName() throws Exception {
	    // verify that the parsed file name object still contains the query portion.
	    FileName fn = vfs.resolveURI(s);
	    assertTrue(fn instanceof URLFileName);
	    
	    URLFileName urlName = (URLFileName)fn;
        assertEquals(s,urlName.getURIEncoded(null));
        assertEquals(s,urlName.toString());
        assertEquals(s,urlName.toString());
        assertNotNull(urlName.getQueryString()); // most important.
        
	}
	/** fixed bug - vfs doesn't consider query when resolving files - probably down to equals on filename not respecting these */
	public void testFileNameQueryIgnoredInEquals() throws Exception {
        FileName a = vfs.resolveURI(s);
        FileName b = vfs.resolveURI(s1);
        assertFalse("a and b are same",a.equals(b));
    }
	
	public void testFileObjectQueryIgnoredInEquals() throws Exception {
	    FileObject a = vfs.resolveFile(s);
	    FileObject b =  vfs.resolveFile(s1);
        assertFalse("a and b are same",a.equals(b));        
    }
	/** exposes a bug in http treatment in vfs - the query portion of urls is dropped */
	   public void testResolvingHttpFileObject() throws Exception {
	        FileName fn = vfs.resolveURI(s);
	        // try creating a file object.
	        FileObject fo = vfs.resolveFile(s);
	        assertTrue(fo instanceof UrlFileObject);
	        // get the filename from this fileobject.
	        final FileName fon = fo.getName();
	        assertTrue(fon instanceof URLFileName);
	        final URLFileName fileName = ((URLFileName)fon);
	        assertNotNull(fileName.getPath());
            assertEquals(fn,fon); 

	        // but here's the bug - geetQueryString() is null, and this breaks verything else.     
	        // the cause of this is that getQueryString() returns null;
	        /*assertNull(fileName.getQueryString()); // bug - should be non-null
	        // due to this, all the following is wrong.
	        assertFalse(s.equals(fileName.getURIEncoded(null)));
	        assertFalse(s.equals(fon.toString())); // so if you toString the filename, you get the Query.
	        assertFalse(s.equals(fon.getURI())); // and same for getURI
	        */
            // now working due to overrided default url provider.
            assertNotNull(fileName.getQueryString());
            assertEquals(s,fileName.getURIEncoded(null));
            assertEquals(s,fileName.getURI());
            assertEquals(s,fileName.toString());
	    }
	   
	   /** investigating the causes of the bug spotted in the test above 
	     * - but this passes - so it must be something else that is creating my urlFilename.
	     * gues this figures - as when a filename is resolved, it's correct.
	     * it's just when a fileobject is resolved that it's incorrect.
	     * */
	    public void testURLFileNameParser() throws Exception {
	        URLFileNameParser p = new URLFileNameParser(80);
	        final String s = "http://vizier.u-strasbg.fr/viz-bin/votable/-dtd/-A?-source=J/A+A/382/60/table4";
	        FileName fn = p.parseUri(null,null,s);
	        assertNotNull(fn);
	        assertTrue(fn instanceof URLFileName);
	        URLFileName ufn = (URLFileName)fn;
	        assertNotNull(ufn.getQueryString());
	        assertNotNull(ufn.getPath());
	        assertEquals(s,ufn.getURIEncoded(null));
	    }
	   
	    /** we can work around the parser error by creating 
	     * a UrlFileObject manually - but it's hardly practical,
	     * as need to cathc all possible occasions.
	     * @throws Exception
	     */
	public void testResolvingHttpWorkAround() throws Exception {
        FileObject fo = vfs.resolveFile(s);
        FileName fn = vfs.resolveURI(s);
        // is it possible to create a URLFileObject by hand?
        UrlFileObject hacked = new MyUrlFileObject((UrlFileSystem)fo.getFileSystem(),fn);
        // this should be true.
        assertTrue(s.equals(((URLFileName)hacked.getName()).getURIEncoded(null)));        
        
       //byte[] content = FileUtil.getContent(fo);
       // System.out.println(new String(content));
       // InputStream is = new URL(urlName.toString()).openStream();
        InputStream is = hacked.getInputStream();
        Piper.pipe(is,System.out);
	}
	
// tests related to virtual.	
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
	 //   System.out.println(new String(content));
	    
	    // can we traverse.
	    
	    // ok - this is a problem in vfs - it's a bug.
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
