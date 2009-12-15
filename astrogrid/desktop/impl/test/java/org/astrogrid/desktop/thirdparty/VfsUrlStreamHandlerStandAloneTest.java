/**
 * 
 */
package org.astrogrid.desktop.thirdparty;

import static org.astrogrid.Fixture.createVFS;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;
/**Tests the problems we're getting with vfs integration with url stream handler.
 * 
 * has to be a stand-alone test - muckiong about with the stream handler in this way causes all the other tests in a suite / build
 * to foul up. 
 * 
 * NEVER RUN THIS TEST AS PART OF THE AUTOMATED BUILD!
 *  unless you want hte build to fail in interesting ways,.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 21, 20089:45:38 AM
 */
public class VfsUrlStreamHandlerStandAloneTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
//can only have one test active at a time, as can oinly set stream handler once - shite!!
  public void testStandardVFS() throws Exception {
      final FileSystemManager vfs = VFS.getManager();
      URL.setURLStreamHandlerFactory(vfs.getURLStreamHandlerFactory());
      try {
          final URL u = new URL("http://www.slashdot.org"); // fails with standard VFS
          fail("problem fixed?");
      } catch (final MalformedURLException e) {
          //expected
      }
//      try {
//          // problem with ftp only. odd.
//          final URL f = new URL("ftp://ftp.roe.ac.uk/pub/al/atlas-test/atlas-list.xml");
//          fail("problem fixed?");
//      } catch (final MalformedURLException e) {
//          //e.printStackTrace();
//          // expected - this is th eproblem
//      }
//      try {
//          final URL sf = new URL("sftp://sftp.server.com/path");
//          fail("problem fixed?");
//      } catch (final MalformedURLException e) {
//          //e.printStackTrace();
//          // expected - this is th eproblem
//      }     

  }

  /** test thje customised configuration of vfs we're using insiode vod */
  public void notestCustomVFS() throws Exception {
      final FileSystemManager vfs = createVFS();
      URL.setURLStreamHandlerFactory(vfs.getURLStreamHandlerFactory());
     
      // these two are ok, as we avoid the bugggy vfs handler in these cases.
      final URL u = new URL("http://www.slashdot.org"); //passes with our vfs, as it comments out the vfs HTTP handler.
      final URL f = new URL("ftp://ftp.roe.ac.uk/pub/al/atlas-test/atlas-list.xml");

      try { // stil have to pass to vfs handler, as there's no otehr impl.
          final URL sf = new URL("sftp://sftp.server.com/path");
          fail("problem fixed?");
      } catch (final MalformedURLException e) {
          //e.printStackTrace();
          // expected - this is th eproblem
      }     
      
      // vos - works? - dunno - not present in the test facade.
      //final URL vo = new URL("vos://foo.bar/com");

  }
    
    
}
