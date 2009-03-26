/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.io.Piper;

/** test suspected bug in copyURLToContent.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 24, 200711:35:54 AM
 */
public class Myspace2381SystemTest extends InARTestCase {

    /**
     * 
     */
    private static final String INITIAL_CONTENT = "should be overwritten";
    private Myspace myspace;
    private URL url;
    

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myspace = assertServiceExists(Myspace.class,"astrogrid.myspace");
        url = new URL("http://casjobs.sdss.org/vo/DR4SIAP/SIAP.asmx/getSiapInfo?BANDPASS=ugriz&POS=180.0%2C2.0&SIZE=1.0&FORMAT=ALL");
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        myspace = null;
        url = null;
    }
    
    
    public static Test suite() {
        return new ARTestSetup(new TestSuite(Myspace2381SystemTest.class),true); // login.
    }    
    
public void testCopyContentToOverwriteFile() throws Exception {
    URI home = myspace.getHome();
    assertNotNull(home);
    URI folder = new URI(home + "/" + System.currentTimeMillis());
    assertFalse(myspace.exists(folder));
    URI file = new URI(folder + "/test" + System.currentTimeMillis() + ".vot");
    System.out.println("Will save to " + file);
    assertFalse(myspace.exists(file));
    myspace.write(file,INITIAL_CONTENT);
    assertTrue("writen to file, but does not exist",myspace.exists(file));
    assertEquals("expected content not in file",INITIAL_CONTENT,myspace.read(file));
    
    // now overwrite the file
    myspace.copyURLToContent(url,file);
    String newContent = myspace.read(file);
    assertNotNull("content is now null",newContent);
    assertFalse("initial content not overwritten by url",INITIAL_CONTENT.equals(newContent));
    // read in the URL ourselves, and compare the data.
    StringWriter sw = new StringWriter();
    Reader r = new BufferedReader(new InputStreamReader(url.openStream()));
    Piper.pipe(r,sw);
    assertEquals("file does not contain content of URL",newContent,sw.toString());
    
    // if the test has gotten this far, clean up the files.
    myspace.delete(file);
    assertFalse(myspace.exists(file));
    myspace.delete(folder);
    assertFalse(myspace.exists(folder));
}   
}
