/**
 * 
 */
package org.astrogrid.desktop.thirdparty;

import static org.astrogrid.Fixture.createVFS;
import junit.framework.TestCase;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.provider.URLFileName;
import org.apache.commons.vfs.provider.URLFileNameParser;
import org.apache.commons.vfs.provider.url.UrlFileObject;
/** 
 * Tests our fix of the VFS http-query ignoring bug.
 * 
 * Tests OUR configuration of VFS - not the default VFS setup.
 * our configuration contains some bug fixes, which in time may not be needed.
 * compare a new version by in <tt>setup()</tt> replacing <tt>createVFS</tt> with
 * <tt>VFS.getManager</tt>
 * 
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 21, 20089:45:38 AM
 */
public class VfsHttpResolvingUnitTest extends TestCase {

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
    public void testFileNameRespectsQuery() throws Exception {
        FileName a = vfs.resolveURI(s);
        FileName b = vfs.resolveURI(s1);
        assertFalse("a and b are same",a.equals(b)); // in vanilla VFS, a.equals(b)
    }
    
    public void testFileObjectRespectsQuery() throws Exception {
        FileObject a = vfs.resolveFile(s);
        FileObject b =  vfs.resolveFile(s1);
        assertFalse("a and b are same",a.equals(b));     //in vanilla VFS, a.equals(b)   
    }
    
    /** exposes a bug in http treatment in vfs - the query portion of urls is dropped 
     * 
     * this test now verifies we've fixed this bug.*/
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
        FileName fn = p.parseUri(null,null,s);
         assertNotNull(fn);
         assertTrue(fn instanceof URLFileName);
         URLFileName ufn = (URLFileName)fn;
         assertNotNull(ufn.getQueryString());
         assertNotNull(ufn.getPath());
         assertEquals(s,ufn.getURIEncoded(null));
     }
     

    
    
}
