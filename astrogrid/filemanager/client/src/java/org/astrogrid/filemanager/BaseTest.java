/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/BaseTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:37:05 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: BaseTest.java,v $
 *   Revision 1.2  2005/03/11 13:37:05  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.1.2.3  2005/03/11 09:42:30  nw
 *   forgot to check this in.
 *
 *   Revision 1.1.2.2  2005/03/10 14:05:23  nw
 *   opened up visibility of setupKeys
 *
 *   Revision 1.1.2.1  2005/03/01 23:41:14  nw
 *   split code inito client and server projoects again.
 *
 *   Revision 1.1.2.1  2005/03/01 15:07:35  nw
 *   close to finished now.
 *
 *   Revision 1.1.2.5  2005/02/27 23:03:12  nw
 *   first cut of talking to filestore
 *
 *   Revision 1.1.2.4  2005/02/25 12:33:27  nw
 *   finished transactional store
 *
 *   Revision 1.1.2.3  2005/02/18 15:50:14  nw
 *   lots of changes.
 *   introduced new schema-driven soap binding, got soap-based unit tests
 *   working again (still some failures)
 *
 *   Revision 1.1.2.2  2005/02/11 14:29:02  nw
 *   in the middle of refactoring these.
 *
 *   Revision 1.1.2.1  2005/02/10 18:01:06  jdt
 *   Moved common into client.
 *
 *   Revision 1.5.4.3  2005/02/10 17:55:44  nw
 *   gets config from ag system.
 *
 *   Revision 1.5.4.2  2005/02/10 17:10:36  nw
 *   added test helper method to get url of a local resource.
 *
 *   Revision 1.5.4.1  2005/02/10 16:23:14  nw
 *   formatted code
 *
 *   Revision 1.5  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.4.2.1  2005/01/25 08:01:16  dave
 *   Added tests for FileManagerClientFactory ....
 *
 *   Revision 1.4  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.3.4.1  2005/01/12 13:16:27  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.3  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.5  2004/12/10 05:21:25  dave
 *   Added node and iterator to client API ...
 *
 *   Revision 1.1.2.4  2004/11/24 16:15:08  dave
 *   Added node functions to client ...
 *
 *   Revision 1.1.2.3  2004/11/16 03:25:37  dave
 *   Updated API to use full ivorn rather than ident ...
 *
 *   Revision 1.1.2.2  2004/11/13 01:39:03  dave
 *   Modifications to support the new client API ...
 *
 *   Revision 1.1.2.1  2004/10/13 06:33:17  dave
 *   Refactored exceptions ...
 *   Refactored the container API
 *   Added placeholder file interface ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager;

import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.filemanager.common.AccountIdent;
import org.astrogrid.io.Piper;
import org.astrogrid.store.Ivorn;

import org.apache.axis.client.Call;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;

import junit.framework.TestCase;

/**
 * A common base class for file manager tests.
 * @todo some of this isn't used now.
 *  @modified nww moved all config for tests out of maven and into code. makes it runnable in an ide, and more amebable to refactoring.
 */
public class BaseTest extends TestCase {

    // initializer to set up necessary config properties.
    public static final String FILEMANAGER_IVORN_STRING =  "ivo://org.astrogrid.test/filemanager";
    public static final String LOCAL_FILEMANAGER = "local:///FileManagerPort";

    protected void setUp() throws Exception {
        //
        // Initialise our base class.
        super.setUp();
        setupKeys();
        //
        // Create our test accounts.
        accountIdent = new AccountIdent(
                CommunityAccountIvornFactory.createLocal("test-"
                + String.valueOf(System.currentTimeMillis())).toString());
        unknownIvorn = new AccountIdent(
            CommunityAccountIvornFactory.createLocal("unknown-"
                + String.valueOf(System.currentTimeMillis())).toString());        
        //
        // Create our test account names.
        accountName = accountIdent.toString();
        accountIvorn = new Ivorn(accountName);
        unknownName = unknownIvorn.toString();
        
        // set up axis stuff.
        // Initialise the Axis 'local:' URL protocol.
        Call.initialize();        
        

    }
    
    protected void setupKeys() throws IOException { 

        SimpleConfig.setProperty("org.astrogrid.community.ident",LOCAL_COMMUNITY);
        SimpleConfig.setProperty("org.astrogrid.filemanager.service.ivorn",FILEMANAGER_IVORN_STRING);
        SimpleConfig.setProperty(TEST_PROPERTY_PREFIX + ".data.http.html","http://www.astrogrid.org/maven/");
        SimpleConfig.setProperty(TEST_PROPERTY_PREFIX + ".data.http.jar","http://www.astrogrid.org/maven/org.astrogrid/jars/astrogrid-common-SNAPSHOT.jar");

        SimpleConfig.setProperty(TEST_PROPERTY_PREFIX + ".ivorn",FILEMANAGER_IVORN_STRING);
        SimpleConfig.setProperty(TEST_PROPERTY_PREFIX + ".service.ivorn",FILEMANAGER_IVORN_STRING);        
        SimpleConfig.setProperty(TEST_PROPERTY_PREFIX + ".resolver.ivorn",FILEMANAGER_IVORN_STRING);
        SimpleConfig.setProperty(TEST_PROPERTY_PREFIX + ".resolver.endpoint",LOCAL_FILEMANAGER);
        
        // registry entries..
        Reader regResource = new InputStreamReader(this.getClass().getResourceAsStream("/service.entry.xml"));
        assertNotNull("registry resource not available from classpath",regResource);
        File tmpResource =File.createTempFile("filemanager-service-reg-entry","xml"); 
      //  tmpResource.deleteOnExit();
        Writer out = new FileWriter(tmpResource);
        Piper.pipe(regResource,out);
        out.close();
        // dunno which one I need to set..
        SimpleConfig.setProperty("ivo://org.astrogrid.test/filemanager",tmpResource.toURL().toString());
        SimpleConfig.setProperty("org.astrogrid.test/filemanager",tmpResource.toURL().toString());               
    }
    
    /**
     * Test properties prefix.
     *  
     */
   public static final String TEST_PROPERTY_PREFIX = "org.astrogrid.filemanager.test";

   public static final String LOCAL_COMMUNITY = "org.astrogrid.mock";
   
    /**
     * Helper method to get a local property.
     *  @modified nww - uses ag config classes now .
     */
    public static String getTestProperty(String name) {
        return SimpleConfig.getProperty(TEST_PROPERTY_PREFIX + "." + name);    
    }
    
    /** helper method to get a url of a local file */
    public URL getTestURL() {
            URL resource =  this.getClass().getResource("/text.txt");
            assertNotNull(resource);
            return resource;
    }

    /**
     * A test string. "A short test string ...."
     *  
     */
    public static final String TEST_STRING = "A short test string ....";

    /**
     * A test string. " plus a bit more ...."
     *  
     */
    public static final String EXTRA_STRING = " plus a bit more ....";

    /**
     * A test byte array. "A short byte array ...."
     *  
     */
    public static final byte[] TEST_BYTES = { 0x41, 0x20, 0x73, 0x68, 0x6f,
            0x72, 0x74, 0x20, 0x62, 0x79, 0x74, 0x65, 0x20, 0x61, 0x72, 0x72,
            0x61, 0x79, 0x20, 0x2e, 0x2e, 0x2e, 0x2e };

    /**
     * A test byte array. " plus a few more ...."
     *  
     */
    public static final byte[] EXTRA_BYTES = { 0x20, 0x70, 0x6c, 0x75, 0x73,
            0x20, 0x61, 0x20, 0x66, 0x65, 0x77, 0x20, 0x6d, 0x6f, 0x72, 0x65,
            0x20, 0x2e, 0x2e, 0x2e, 0x2e };

    /**
     * Test utility to compare two arrays of bytes.
     *  
     */
    public static void assertEquals(byte[] left, byte[] right) {
        assertEquals("Different array length", left.length, right.length);
        for (int i = 0; i < left.length; i++) {
            assertEquals("Wrong value for byte[" + i + "]", left[i], right[i]);
        }
    }

    /**
     * Our test account ivorn.
     *  
     */
    protected AccountIdent accountIdent;
    protected Ivorn accountIvorn;

    /**
     * Our test account name.
     *  
     */
    protected String accountName;

    /**
     * Our test account ivorn.
     *  
     */
    protected AccountIdent unknownIvorn;

    /**
     * Our test account name.
     *  
     */
    protected String unknownName;

    /**
     * A set of ivorn identifiers for our target file stores.
     *  
     */
    public static Ivorn FILESTORE_1;
    public static Ivorn FILESTORE_2;
    static {
        try {
        FILESTORE_1 = new Ivorn("ivo://filestore/alpha");
        FILESTORE_2 =  new Ivorn("ivo://filestore/beta");
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not initialize statics",e);
        }        
    }
    protected static  Ivorn[] filestores = new Ivorn[]{
           FILESTORE_1, FILESTORE_2
    };
    /**
     * Setup our test. This may need to be changed for the integration tests.
     *  
     */


    /**
     * Compare two ivorns.
     *  
     */
    public boolean compare(Ivorn frog, Ivorn toad) {
        return frog.toString().equals(toad.toString());
    }

    /**
     * Compare two Ivorns.
     *  
     */
    public void assertEquals(Ivorn frog, Ivorn toad) {
        assertEquals(frog != null ? frog.toString() : null, toad != null ? toad.toString() : null);
    }
}