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
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.io.Piper;

import uk.ac.starlink.util.IOUtils;

/** test that behaviour reported in bz 2157 isn't still the case.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 24, 200711:35:54 AM
 */
public class Myspace2157SystemTest extends InARTestCase {

    /**
     * 
     */
    private static final String INITIAL_CONTENT = "should be overwritten";
    private Myspace myspace;
    private Siap siap;
    private URL url;
    

    protected void setUp() throws Exception {
        super.setUp();
        myspace = (Myspace)assertServiceExists(Myspace.class,"astrogrid.myspace");
        siap = (Siap)assertServiceExists(Siap.class,"ivoa.siap");
        url = new URL("http://casjobs.sdss.org/vo/DR4SIAP/SIAP.asmx/getSiapInfo?BANDPASS=ugriz&POS=180.0%2C2.0&SIZE=1.0&FORMAT=ALL");
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        myspace = null;
        siap = null;
        url = null;
    }
    
    
    public static Test suite() {
        return new ARTestSetup(new TestSuite(Myspace2157SystemTest.class),true); // login.
    }    
    
    public void testSaveToRoot() throws Exception {
        // create a save location in the root directory.
        URI home = myspace.getHome();
        assertNotNull(home);
        URI rootSave = new URI(home + "/test" + System.currentTimeMillis() + ".vot");
        System.out.println("Will save to " + rootSave);
        assertFalse(myspace.exists(rootSave));
        
        siap.executeAndSave(url,rootSave);
        assertTrue(myspace.exists(rootSave));
        String content = myspace.read(rootSave);
        assertNotNull(content);
        StringWriter sw = new StringWriter();
        Reader r = new BufferedReader(new InputStreamReader(url.openStream()));
        Piper.pipe(r,sw);
        assertEquals(content,sw.toString());
        
        myspace.delete(rootSave);
        assertFalse(myspace.exists(rootSave));
    }

    public void testSaveToSubdir() throws Exception {
        // create a save location in a subfolder.
        URI home = myspace.getHome();
        assertNotNull(home);
        URI folder = new URI(home + "/" + System.currentTimeMillis());
        assertFalse(myspace.exists(folder));
        myspace.createFolder(folder);
        assertTrue(myspace.exists(folder));
        URI file = new URI(folder + "/test" + System.currentTimeMillis() + ".vot");
        System.out.println("Will save to " + file);
        assertFalse(myspace.exists(file));
        
        siap.executeAndSave(url,file);
        assertTrue(myspace.exists(file));
        String content = myspace.read(file);
        assertNotNull(content);
        StringWriter sw = new StringWriter();
        Reader r = new BufferedReader(new InputStreamReader(url.openStream()));
        Piper.pipe(r,sw);
        assertEquals(content,sw.toString());
        
        // clean up.
        myspace.delete(file);
        assertFalse(myspace.exists(file));
        myspace.delete(folder);
        assertFalse(myspace.exists(folder));
    }
    

    public void testSaveToNonexistentSubdir() throws Exception {
        // create a save location in a directory that doesn't exist,
        // and then just write to that location.
        URI home = myspace.getHome();
        assertNotNull(home);
        URI folder = new URI(home + "/" + System.currentTimeMillis());
        assertFalse(myspace.exists(folder));
        URI file = new URI(folder + "/test" + System.currentTimeMillis() + ".vot");
        System.out.println("Will save to " + file);
        assertFalse(myspace.exists(file));
        
        siap.executeAndSave(url,file);
        assertTrue(myspace.exists(file));
        String content = myspace.read(file);
        assertNotNull(content);
        StringWriter sw = new StringWriter();
        Reader r = new BufferedReader(new InputStreamReader(url.openStream()));
        Piper.pipe(r,sw);
        assertEquals(content,sw.toString());
        
        // clean up.
        myspace.delete(file);
        assertFalse(myspace.exists(file));
        myspace.delete(folder);
        assertFalse(myspace.exists(folder));
    }    
//    
//    public void testOverwriteInNonexistentSubdir() throws Exception {
//        // create a save location, populate it with data, and then try to overwrite it.
//        URI home = myspace.getHome();
//        assertNotNull(home);
//        URI folder = new URI(home + "/" + System.currentTimeMillis());
//        assertFalse(myspace.exists(folder));
//        URI file = new URI(folder + "/test" + System.currentTimeMillis() + ".vot");
//        System.out.println("Will save to " + file);
//        assertFalse(myspace.exists(file));
//        myspace.write(file,INITIAL_CONTENT);
//        assertTrue(myspace.exists(file));
//        assertEquals(INITIAL_CONTENT,myspace.read(file));
//        
//        // now overwrite the file
//        siap.executeAndSave(url,file);
//        String content = myspace.read(file);
//        assertNotNull(content);
//        assertFalse("initial content not overwritten by url",INITIAL_CONTENT.equals(content));
//        StringWriter sw = new StringWriter();
//        Reader r = new BufferedReader(new InputStreamReader(url.openStream()));
//        Piper.pipe(r,sw);
//        assertEquals(content,sw.toString());
//        
//        // clean up.
//        myspace.delete(file);
//        assertFalse(myspace.exists(file));
//        myspace.delete(folder);
//        assertFalse(myspace.exists(folder));
//    }   

}
