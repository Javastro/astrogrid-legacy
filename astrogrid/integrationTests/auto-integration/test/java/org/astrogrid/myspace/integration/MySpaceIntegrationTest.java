/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/integrationTests/auto-integration/test/java/org/astrogrid/myspace/integration/Attic/MySpaceIntegrationTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:36:22 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 * <cvs:log>
 *   $Log: MySpaceIntegrationTest.java,v $
 *   Revision 1.7  2005/03/11 13:36:22  clq2
 *   with merges from filemanager
 *
 *   Revision 1.6.92.1  2005/03/10 19:33:16  nw
 *   marked tests for removal once myspace is replaced by filemanager.
 *
 *   Revision 1.6  2004/09/09 01:19:50  dave
 *   Updated MIME type handling in MySpace.
 *   Extended test coverage for MIME types in FileStore and MySpace.
 *   Added VM memory data to community ServiceStatusData.
 *
 *   Revision 1.5.6.5  2004/09/08 20:59:40  dave
 *   Added check for fake null in mime type ....
 *
 *   Revision 1.5.6.4  2004/09/08 14:29:31  dave
 *   Fixed broken {}
 *
 *   Revision 1.5.6.3  2004/09/08 14:27:19  dave
 *   Removed duplicate test ...
 *
 *   Revision 1.5.6.2  2004/09/08 14:20:28  dave
 *   Fixed typo ....
 *
 *   Revision 1.5.6.1  2004/09/08 13:20:53  dave
 *   Updated mime type tests ...
 *
 *   Revision 1.5  2004/09/02 10:25:41  dave
 *   Updated FileStore and MySpace to handle mime type and file size.
 *   Updated Community deployment script.
 *
 *   Revision 1.4.2.3  2004/09/02 00:29:05  dave
 *   Fixed typo and changed tabs to spaces.
 *
 *   Revision 1.4.2.2  2004/09/02 00:25:38  dave
 *   Fixed typo.
 *
 *   Revision 1.4.2.1  2004/09/02 00:12:47  dave
 *   Added tests for mime type.
 *
 *   Revision 1.4  2004/08/27 22:43:15  dave
 *   Updated filestore and myspace to report file size correctly.
 *
 *   Revision 1.3.6.6  2004/08/27 16:34:00  dave
 *   Dumb typo ...
 *
 *   Revision 1.3.6.5  2004/08/27 16:30:45  dave
 *   Removed extra stage to test ...
 *
 *   Revision 1.3.6.4  2004/08/27 15:34:13  dave
 *   Fixed wrong file path in test ...
 *
 *   Revision 1.3.6.3  2004/08/27 15:26:47  dave
 *   Dumb typo ...
 *
 *   Revision 1.3.6.2  2004/08/27 15:18:25  dave
 *   Added test for imported URL size.
 *
 *   Revision 1.3.6.1  2004/08/27 14:27:05  dave
 *   Added test for file size ....
 *
 *   Revision 1.3  2004/08/20 15:18:30  dave
 *   Patched myspace.xsp to use localhost:8080 (temp fix only).
 *   Modified myspace integration test to prevent name collision when repeated.
 *
 *   Revision 1.2.6.1  2004/08/20 13:56:06  dave
 *   Modified test to prevent repeat tests failing.
 *
 *   Revision 1.2  2004/08/18 19:00:01  dave
 *   Myspace manager modified to use remote filestore.
 *   Tested before checkin - integration tests at 91%.
 * </cvs:log>
 *
 */
package org.astrogrid.myspace.integration ;

import java.net.URL ;
import java.net.HttpURLConnection ;

import java.io.InputStream ;
import java.io.IOException ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.config.Config ;
import org.astrogrid.config.SimpleConfig ;

import org.astrogrid.store.Agsl;
import org.astrogrid.store.Msrl;

import org.astrogrid.store.delegate.StoreFile ;
import org.astrogrid.store.delegate.StoreException ;
import org.astrogrid.store.delegate.myspaceItn05.MySpaceIt05Delegate ;

import org.astrogrid.community.User;

import org.astrogrid.filestore.common.file.FileProperties ;

import junit.framework.TestCase;

/**
 * A JUnit test case for the myspace service.
 *@deprecated remove this package when we ditch myspace
 */
public class MySpaceIntegrationTest
    extends TestCase
    {

    /**
     * A test string.
     * "A short test string ...."
     *
     */
    public static final String TEST_STRING = "A short test string ...." ;

    /**
     * A test string.
     * " plus a bit more ...."
     *
     */
    public static final String EXTRA_STRING = " plus a bit more ...." ;

    /**
     * A test byte array.
     * "A short byte array ...."
     *
     */
    public static final byte[] TEST_BYTES = {
        0x41,
        0x20,
        0x73,
        0x68,
        0x6f,
        0x72,
        0x74,
        0x20,
        0x62,
        0x79,
        0x74,
        0x65,
        0x20,
        0x61,
        0x72,
        0x72,
        0x61,
        0x79,
        0x20,
        0x2e,
        0x2e,
        0x2e,
        0x2e
        } ;

    /**
     * A test byte array.
     * " plus a few more ...."
     *
     */
    public static final byte[] EXTRA_BYTES = {
        0x20,
        0x70,
        0x6c,
        0x75,
        0x73,
        0x20,
        0x61,
        0x20,
        0x66,
        0x65,
        0x77,
        0x20,
        0x6d,
        0x6f,
        0x72,
        0x65,
        0x20,
        0x2e,
        0x2e,
        0x2e,
        0x2e
        } ;

    /**
     * Current timestamp, used to generate a new user name for each test.
     *
     */
    private long timestamp = 0 ;

    /**
     * Setup our test (sets the test tiemstamp).
     *
     */
    public void setUp()
        throws Exception
        {
        this.timestamp = System.currentTimeMillis() ;
        }

    /**
     * Helper method to create a user name.
     * Creates a string based on the test count.
     * e.g. integration-test-4.
     *
     */
    public String getUserName()
        {
        //
        // Generate the user name.
        return "integration-test-" + String.valueOf(timestamp) ;
        }

    /**
     * Helper method to create a user object.
     * Creates a User object with the current user name.
     *
     */
    public User getUserObject()
        {
        //
        // Generate the user object.
        return new User(
            this.getUserName(),
            "community",
            "group",
            "token"
            ) ;
        }

    /**
     * Helper method to create a user path.
     * Creates a metadata path based on the current user name.
     * e.g '/integration-test-4/path/file'
     *
     */
    public String getUserPath(String path)
        {
        return new StringBuffer()
            .append("/")
            .append(
                this.getUserName()
                )
            .append("/")
            .append(path)
            .toString()
            ;
        }

    /**
     * Our AstroGrid configuration.
     */
    private static Config config = SimpleConfig.getSingleton();

    /**
     * Helper method to get a config property.
     *
     */
    public String getConfigProperty(String name)
        {
        return (String) config.getProperty(
            name
            ) ;
        }

    /**
     * Our test property key prefix.
     * Note, this uses the same properties as the filestore tests.
     *
     */
    public static final String TEST_PROPERTY_PREFIX = "org.astrogrid.filestore.test" ;

    /**
     * Helper method to get a test property.
     *
     */
    public String getTestProperty(String name)
        {
        return (String) config.getProperty(
            TEST_PROPERTY_PREFIX + "." + name
            ) ;
        }

    /**
     * Test utility to compare two arrays of bytes.
     *
     */
    public static void assertEquals(byte[] left, byte[] right)
        {
        System.out.println("--------") ;
        assertEquals(
            "Different array length",
            left.length,
            right.length
            ) ;
        for (int i = 0 ; i < left.length ; i++)
            {
            System.out.println(
                "[" + i + "] " + Integer.toHexString(left[i]) + ":" + Integer.toHexString(right[i])
                ) ;
            assertEquals(
                "Wrong value for byte[" + i + "]",
                left[i],
                right[i]
                ) ;
            }
        System.out.println("--------") ;
        }

    /**
     * Test utility to compare the contents of two input streams.
     *
     */
    public static void assertContentsEqual(InputStream aStream, InputStream bStream)
        throws Exception
        {
        int aByte = -1 ;
        int bByte = -1 ;
        do {
            aByte = aStream.read() ;
            bByte = bStream.read() ;
            assertEquals(
                aByte,
                bByte
                ) ;
            }
        while (aByte != -1) ;
        }

    /**
     * Helper method to create a delegate.
     *
     */
    public MySpaceIt05Delegate delegate()
        throws Exception
        {
        //
        // Get the service endpoint from the registry.
        // Shouldn't this be resolveable from the registry ?
        String endpoint = getConfigProperty(
            "org.astrogrid.myspace.endpoint"
            ) ;
        System.out.println("Endpoint : " + endpoint) ;
        //
        // Create our fake 'user'.
        User operator = new User(
            "someone",
            "somewhere",
            "group",
            "token"
            );
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = new MySpaceIt05Delegate(
            operator,
            new Msrl("myspace:" + endpoint).toString()
            );
        //
        // Configure the delegate to
        // (i) not generate test responses and
        // (ii) throw exceptions if errors are returned.
        myspace.setTest(false);
        myspace.setThrow(true);
        return myspace ;
        }

    /**
     * Test we can create a myspace delegate.
     *
     */
    public void testCreateDelegate()
        throws Exception
        {
        assertNotNull(
            "Failed to create delegate.",
            delegate()
            ) ;
        }

    /**
     * Test we can create a test account.
     *
     */
    public void testCreateUser()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        }

    /**
     * Test that we can import a string.
     *
     */
    public void testImportString()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Import some data.
        myspace.putString(
            TEST_STRING,
            this.getUserPath(
                "imported-string"
                ),
            false
            );
        }

    /**
     * Test that we can import and export a string.
     *
     */
    public void testExportString()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Import some data.
        myspace.putString(
            TEST_STRING,
            this.getUserPath(
                "imported-string"
                ),
            false
            );
        //
        // Export the same data as a string.
        assertEquals(
            TEST_STRING,
            myspace.getString(
                this.getUserPath(
                    "imported-string"
                    )
                )
            ) ;
        }

    /**
     * Test that we can import a byte array.
     *
     */
    public void testImportBytes()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Import some data.
        myspace.putBytes(
            TEST_BYTES,
            0,
            TEST_BYTES.length,
            this.getUserPath(
                "imported-bytes"
                ),
            false
            );
        }

    /**
     * Test that we can import and export a byte array.
     *
     */
    public void testExportBytes()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Import some data.
        myspace.putBytes(
            TEST_BYTES,
            0,
            TEST_BYTES.length,
            this.getUserPath(
                "imported-bytes"
                ),
            false
            );
        //
        // Export the same data as bytes.
        assertEquals(
            TEST_BYTES,
            myspace.getBytes(
                this.getUserPath(
                    "imported-bytes"
                    )
                )
            ) ;
        }

    /**
     * Test that we can import the contents of a URL.
     *
     */
    public void testImportHtml()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Get the URL from our config.
        URL url = new URL(
            getTestProperty(
                "data.http.html"
                )
            ) ;
        //
        // Import some data.
        myspace.putUrl(
            url,
            this.getUserPath(
                "imported-html"
                ),
            false
            );
        }

    /**
     * Test that we can import the contents of a URL.
     *
     */
    public void testImportJar()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Get the URL from our config.
        URL url = new URL(
            getTestProperty(
                "data.http.jar"
                )
            ) ;
        //
        // Import some data.
        myspace.putUrl(
            url,
            this.getUserPath(
                "imported-jar"
                ),
            false
            );
        }

    /**
     * Test the contents of an imported URL.
     *
     */
    public void testImportedUrlContents()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Get the URL from our config.
        URL source = new URL(
            getTestProperty(
                "data.http.jar"
                )
            ) ;
        //
        // Import some data.
        myspace.putUrl(
            source,
            this.getUserPath(
                "imported.jar"
                ),
            false
            );
        //
        // Get the file URL from the manager.
        URL stored = myspace.getUrl(
            this.getUserPath(
                "imported.jar"
                )
            ) ;
        //
        // Check the content are the same.
        assertContentsEqual(
            source.openStream(),
            stored.openStream()
            ) ;
        }

    /**
     * Test that we can import and append a string.
     *
     */
    public void testAppendString()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Import some data.
        myspace.putString(
            TEST_STRING,
            this.getUserPath(
                "imported-string"
                ),
            false
            );
        //
        // Append some data.
        myspace.putString(
            EXTRA_STRING,
            this.getUserPath(
                "imported-string"
                ),
            true
            );
        //
        // Export the same data as a string.
        assertEquals(
            (TEST_STRING + EXTRA_STRING),
            myspace.getString(
                this.getUserPath(
                    "imported-string"
                    )
                )
            ) ;
        }

    /**
     * Test that we can import and append a byte array.
     *
     */
    public void testAppendBytes()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Import some data.
        myspace.putBytes(
            TEST_BYTES,
            0,
            TEST_BYTES.length,
            this.getUserPath(
                "imported-string"
                ),
            false
            );
        //
        // Append some data.
        myspace.putBytes(
            EXTRA_BYTES,
            0,
            EXTRA_BYTES.length,
            this.getUserPath(
                "imported-string"
                ),
            true
            );
        //
        // Export the data as a string.
        assertEquals(
            (new String(TEST_BYTES) + new String(EXTRA_BYTES)),
            myspace.getString(
                this.getUserPath(
                    "imported-string"
                    )
                )
            ) ;
        }

    /**
     * Test that we can copy a file.
     *
     */
    public void testCopyLocal()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Create the source and destination paths.
        String source = this.getUserPath(
            "imported string"
            ) ;
        String destination = this.getUserPath(
            "copy of string"
            ) ;
        String endpoint = getConfigProperty(
            "org.astrogrid.myspace.endpoint"
            ) ;
        Msrl msrl = new Msrl(
            "myspace:" + endpoint
            ) ;
        Agsl agsl = new Agsl(
            msrl,
            destination
            );
        //
        // Import some data.
        myspace.putString(
            TEST_STRING,
            source,
            false
            );
        //
        // Copy the data within the manager.
        myspace.copy(
            source,
            agsl
            );
        //
        // Check the copy has the same data.
        assertEquals(
            TEST_STRING,
            myspace.getString(
                this.getUserPath(
                    "copy of string"
                    )
                )
            ) ;
        //
        // Modify the copy.
        myspace.putString(
            EXTRA_STRING,
            this.getUserPath(
                "copy of string"
                ),
            true
            );
        //
        // Check the copy has changed
        assertEquals(
            (TEST_STRING + EXTRA_STRING),
            myspace.getString(
                this.getUserPath(
                    "copy of string"
                    )
                )
            ) ;
        //
        // Check the original is unchanged
        assertEquals(
            TEST_STRING,
            myspace.getString(
                this.getUserPath(
                    "imported string"
                    )
                )
            ) ;
        //
        // Delete the original.
        myspace.delete(
            this.getUserPath(
                "imported string"
                )
            ) ;
        //
        // Check the copy is ok.
        assertEquals(
            (TEST_STRING + EXTRA_STRING),
            myspace.getString(
                this.getUserPath(
                    "copy of string"
                    )
                )
            ) ;
        }

    /**
     * Test that we can delete a file.
     *
     */
    public void testDelete()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Import some data.
        myspace.putString(
            TEST_STRING,
            this.getUserPath(
                "imported-string"
                ),
            false
            );
        //
        // Delete the file.
        myspace.delete(
            this.getUserPath(
                "imported-string"
                )
            ) ;
        //
        // Try to export the data.
        try {
            myspace.getString(
                this.getUserPath(
                    "imported-string"
                    )
                ) ;
            }
        catch(StoreException ouch)
            {
            return ;
            }
        fail("") ;
        }

    /**
     * Test accessing the URL for a deleted file won't work.
     *
     */
    public void testDeletedUrlFails()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Get the URL from our config.
        URL source = new URL(
            getTestProperty(
                "data.http.jar"
                )
            ) ;
        //
        // Import some data.
        myspace.putUrl(
            source,
            this.getUserPath(
                "imported.jar"
                ),
            false
            );
        //
        // Get the file URL from the manager.
        URL stored = myspace.getUrl(
            this.getUserPath(
                "imported.jar"
                )
            ) ;
        //
        // Delete the file.
        myspace.delete(
            this.getUserPath(
                "imported.jar"
                )
            ) ;
        //
        // Check that accessing the URL returns the right error.
        HttpURLConnection connection = (HttpURLConnection) stored.openConnection() ;
        assertEquals(
            404,
            connection.getResponseCode()
            ) ;
        }

    /**
     * Check that an imported string has the right size.
     *
     */
    public void testImportStringSize()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Import some data.
        myspace.putString(
            TEST_STRING,
            this.getUserPath(
                "imported-string"
                ),
            false
            );
        //
        // Get the file info.
        StoreFile file = myspace.getFile(
            this.getUserPath(
                "imported-string"
                )
            ) ;
        //
        // Check the file size.
        assertEquals(
            (long) TEST_STRING.length(),
            file.getSize()
            ) ;
        }

    /**
     * Check that an imported URL has the right size.
     *
     */
    public void testImportUrlSize()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Get the URL from our config.
        URL source = new URL(
            getTestProperty(
                "data.http.jar"
                )
            ) ;
        //
        // Import some data.
        myspace.putUrl(
            source,
            this.getUserPath(
                "imported.jar"
                ),
            false
            );
        //
        // Get the original file size.
        long size = source.openConnection().getContentLength() ;
        //
        // Get the file info.
        StoreFile file = myspace.getFile(
            this.getUserPath(
                "imported.jar"
                )
            ) ;
        //
        // Check the file info size.
        assertEquals(
            size,
            file.getSize()
            ) ;
        //
        // Get the file URL from the manager.
        URL stored = myspace.getUrl(
            this.getUserPath(
                "imported.jar"
                )
            ) ;
        //
        // Check the stored file size.
//        assertEquals(
//            size,
//            stored.openConnection().getContentLength()
//            ) ;
        }


    /**
     * Check that importing a string as 'imported.xml' has the right mime type.
     *
     */
    public void testImportStringAsXML()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Import some data.
        myspace.putString(
            TEST_STRING,
            this.getUserPath(
                "imported.xml"
                ),
            false
            );
        //
        // Get the file info.
        StoreFile file = myspace.getFile(
            this.getUserPath(
                "imported.xml"
                )
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_XML,
            file.getMimeType()
            ) ;
        }

    /**
     * Check that importing a string as 'imported.vot' has the right mime type.
     *
     */
    public void testImportStringAsVot()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Import some data.
        myspace.putString(
            TEST_STRING,
            this.getUserPath(
                "imported.vot"
                ),
            false
            );
        //
        // Get the file info.
        StoreFile file = myspace.getFile(
            this.getUserPath(
                "imported.vot"
                )
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_VOTABLE,
            file.getMimeType()
            ) ;
        }

    /**
     * Check that importing a string as 'imported.votable' has the right mime type.
     *
     */
    public void testImportStringAsVoTable()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Import some data.
        myspace.putString(
            TEST_STRING,
            this.getUserPath(
                "imported.votable"
                ),
            false
            );
        //
        // Get the file info.
        StoreFile file = myspace.getFile(
            this.getUserPath(
                "imported.votable"
                )
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_VOTABLE,
            file.getMimeType()
            ) ;
        }

    /**
     * Check that importing a string as 'imported.vol' has the right mime type.
     *
     */
    public void testImportStringAsVol()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Import some data.
        myspace.putString(
            TEST_STRING,
            this.getUserPath(
                "imported.vol"
                ),
            false
            );
        //
        // Get the file info.
        StoreFile file = myspace.getFile(
            this.getUserPath(
                "imported.vol"
                )
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_VOLIST,
            file.getMimeType()
            ) ;
        }

    /**
     * Check that importing a string as 'imported.volist' has the right mime type.
     *
     */
    public void testImportStringAsVoList()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Import some data.
        myspace.putString(
            TEST_STRING,
            this.getUserPath(
                "imported.volist"
                ),
            false
            );
        //
        // Get the file info.
        StoreFile file = myspace.getFile(
            this.getUserPath(
                "imported.volist"
                )
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_VOLIST,
            file.getMimeType()
            ) ;
        }

    /**
     * Check that importing a string as 'imported.work' has the right mime type.
     *
     */
    public void testImportStringAsWork()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Import some data.
        myspace.putString(
            TEST_STRING,
            this.getUserPath(
                "imported.work"
                ),
            false
            );
        //
        // Get the file info.
        StoreFile file = myspace.getFile(
            this.getUserPath(
                "imported.work"
                )
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_WORKFLOW,
            file.getMimeType()
            ) ;
        }

    /**
     * Check that importing a string as 'imported.flow' has the right mime type.
     *
     */
    public void testImportStringAsFlow()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Import some data.
        myspace.putString(
            TEST_STRING,
            this.getUserPath(
                "imported.flow"
                ),
            false
            );
        //
        // Get the file info.
        StoreFile file = myspace.getFile(
            this.getUserPath(
                "imported.flow"
                )
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_WORKFLOW,
            file.getMimeType()
            ) ;
        }

    /**
     * Check that importing a string as 'imported.workflow' has the right mime type.
     *
     */
    public void testImportStringAsWorkFlow()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Import some data.
        myspace.putString(
            TEST_STRING,
            this.getUserPath(
                "imported.workflow"
                ),
            false
            );
        //
        // Get the file info.
        StoreFile file = myspace.getFile(
            this.getUserPath(
                "imported.workflow"
                )
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_WORKFLOW,
            file.getMimeType()
            ) ;
        }

    /**
     * Check that importing a string as 'imported.job' has the right mime type.
     *
     */
    public void testImportStringAsJob()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Import some data.
        myspace.putString(
            TEST_STRING,
            this.getUserPath(
                "imported.job"
                ),
            false
            );
        //
        // Get the file info.
        StoreFile file = myspace.getFile(
            this.getUserPath(
                "imported.job"
                )
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_JOB,
            file.getMimeType()
            ) ;
        }

    /**
     * Check that importing a string as 'imported.adql' has the right mime type.
     *
     */
    public void testImportStringAsAdql()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Import some data.
        myspace.putString(
            TEST_STRING,
            this.getUserPath(
                "imported.adql"
                ),
            false
            );
        //
        // Get the file info.
        StoreFile file = myspace.getFile(
            this.getUserPath(
                "imported.adql"
                )
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_ADQL,
            file.getMimeType()
            ) ;
        }

    /**
     * Check that importing a URL as 'imported.xml' has the right mime type.
     *
     */
    public void testImportUrlAsXml()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Get the URL from our config.
        URL source = new URL(
            getTestProperty(
                "data.http.html"
                )
            ) ;
        //
        // Import some data.
        myspace.putUrl(
            source,
            this.getUserPath(
                "imported.xml"
                ),
            false
            );
        //
        // Get the file info.
        StoreFile file = myspace.getFile(
            this.getUserPath(
                "imported.xml"
                )
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_XML,
            file.getMimeType()
            ) ;
        //
        // Get the file URL from the manager.
        URL stored = myspace.getUrl(
            this.getUserPath(
                "imported.xml"
                )
            ) ;
        //
        // Check the stored mime type.
        assertEquals(
            FileProperties.MIME_TYPE_XML,
            stored.openConnection().getContentType()
            ) ;
        }

    /**
     * Check that importing a URL as 'imported.vot' has the right mime type.
     *
     */
    public void testImportUrlAsVot()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Get the URL from our config.
        URL source = new URL(
            getTestProperty(
                "data.http.html"
                )
            ) ;
        //
        // Import some data.
        myspace.putUrl(
            source,
            this.getUserPath(
                "imported.vot"
                ),
            false
            );
        //
        // Get the file info.
        StoreFile file = myspace.getFile(
            this.getUserPath(
                "imported.vot"
                )
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_VOTABLE,
            file.getMimeType()
            ) ;
        //
        // Get the file URL from the manager.
        URL stored = myspace.getUrl(
            this.getUserPath(
                "imported.vot"
                )
            ) ;
        //
        // Check the stored mime type.
        assertEquals(
            FileProperties.MIME_TYPE_VOTABLE,
            stored.openConnection().getContentType()
            ) ;
        }

    /**
     * Check that importing a URL as 'imported.votable' has the right mime type.
     *
     */
    public void testImportUrlAsVoTable()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Get the URL from our config.
        URL source = new URL(
            getTestProperty(
                "data.http.html"
                )
            ) ;
        //
        // Import some data.
        myspace.putUrl(
            source,
            this.getUserPath(
                "imported.votable"
                ),
            false
            );
        //
        // Get the file info.
        StoreFile file = myspace.getFile(
            this.getUserPath(
                "imported.votable"
                )
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_VOTABLE,
            file.getMimeType()
            ) ;
        //
        // Get the file URL from the manager.
        URL stored = myspace.getUrl(
            this.getUserPath(
                "imported.votable"
                )
            ) ;
        //
        // Check the stored mime type.
        assertEquals(
            FileProperties.MIME_TYPE_VOTABLE,
            stored.openConnection().getContentType()
            ) ;
        }

    /**
     * Check that importing a URL as 'imported.vol' has the right mime type.
     *
     */
    public void testImportUrlAsVol()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Get the URL from our config.
        URL source = new URL(
            getTestProperty(
                "data.http.html"
                )
            ) ;
        //
        // Import some data.
        myspace.putUrl(
            source,
            this.getUserPath(
                "imported.vol"
                ),
            false
            );
        //
        // Get the file info.
        StoreFile file = myspace.getFile(
            this.getUserPath(
                "imported.vol"
                )
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_VOLIST,
            file.getMimeType()
            ) ;
        //
        // Get the file URL from the manager.
        URL stored = myspace.getUrl(
            this.getUserPath(
                "imported.vol"
                )
            ) ;
        //
        // Check the stored mime type.
        assertEquals(
            FileProperties.MIME_TYPE_VOLIST,
            stored.openConnection().getContentType()
            ) ;
        }

    /**
     * Check that importing a URL as 'imported.volist' has the right mime type.
     *
     */
    public void testImportUrlAsVoList()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Get the URL from our config.
        URL source = new URL(
            getTestProperty(
                "data.http.html"
                )
            ) ;
        //
        // Import some data.
        myspace.putUrl(
            source,
            this.getUserPath(
                "imported.volist"
                ),
            false
            );
        //
        // Get the file info.
        StoreFile file = myspace.getFile(
            this.getUserPath(
                "imported.volist"
                )
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_VOLIST,
            file.getMimeType()
            ) ;
        //
        // Get the file URL from the manager.
        URL stored = myspace.getUrl(
            this.getUserPath(
                "imported.volist"
                )
            ) ;
        //
        // Check the stored mime type.
        assertEquals(
            FileProperties.MIME_TYPE_VOLIST,
            stored.openConnection().getContentType()
            ) ;
        }

    /**
     * Check that importing a URL as 'imported.work' has the right mime type.
     *
     */
    public void testImportUrlAsWork()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Get the URL from our config.
        URL source = new URL(
            getTestProperty(
                "data.http.html"
                )
            ) ;
        //
        // Import some data.
        myspace.putUrl(
            source,
            this.getUserPath(
                "imported.work"
                ),
            false
            );
        //
        // Get the file info.
        StoreFile file = myspace.getFile(
            this.getUserPath(
                "imported.work"
                )
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_WORKFLOW,
            file.getMimeType()
            ) ;
        //
        // Get the file URL from the manager.
        URL stored = myspace.getUrl(
            this.getUserPath(
                "imported.work"
                )
            ) ;
        //
        // Check the stored mime type.
        assertEquals(
            FileProperties.MIME_TYPE_WORKFLOW,
            stored.openConnection().getContentType()
            ) ;
        }

    /**
     * Check that importing a URL as 'imported.flow' has the right mime type.
     *
     */
    public void testImportUrlAsFlow()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Get the URL from our config.
        URL source = new URL(
            getTestProperty(
                "data.http.html"
                )
            ) ;
        //
        // Import some data.
        myspace.putUrl(
            source,
            this.getUserPath(
                "imported.flow"
                ),
            false
            );
        //
        // Get the file info.
        StoreFile file = myspace.getFile(
            this.getUserPath(
                "imported.flow"
                )
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_WORKFLOW,
            file.getMimeType()
            ) ;
        //
        // Get the file URL from the manager.
        URL stored = myspace.getUrl(
            this.getUserPath(
                "imported.flow"
                )
            ) ;
        //
        // Check the stored mime type.
        assertEquals(
            FileProperties.MIME_TYPE_WORKFLOW,
            stored.openConnection().getContentType()
            ) ;
        }

    /**
     * Check that importing a URL as 'imported.workflow' has the right mime type.
     *
     */
    public void testImportUrlAsWorkFlow()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Get the URL from our config.
        URL source = new URL(
            getTestProperty(
                "data.http.html"
                )
            ) ;
        //
        // Import some data.
        myspace.putUrl(
            source,
            this.getUserPath(
                "imported.workflow"
                ),
            false
            );
        //
        // Get the file info.
        StoreFile file = myspace.getFile(
            this.getUserPath(
                "imported.workflow"
                )
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_WORKFLOW,
            file.getMimeType()
            ) ;
        //
        // Get the file URL from the manager.
        URL stored = myspace.getUrl(
            this.getUserPath(
                "imported.workflow"
                )
            ) ;
        //
        // Check the stored mime type.
        assertEquals(
            FileProperties.MIME_TYPE_WORKFLOW,
            stored.openConnection().getContentType()
            ) ;
        }

    /**
     * Check that importing a URL as 'imported.job' has the right mime type.
     *
     */
    public void testImportUrlAsJob()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Get the URL from our config.
        URL source = new URL(
            getTestProperty(
                "data.http.html"
                )
            ) ;
        //
        // Import some data.
        myspace.putUrl(
            source,
            this.getUserPath(
                "imported.job"
                ),
            false
            );
        //
        // Get the file info.
        StoreFile file = myspace.getFile(
            this.getUserPath(
                "imported.job"
                )
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_JOB,
            file.getMimeType()
            ) ;
        //
        // Get the file URL from the manager.
        URL stored = myspace.getUrl(
            this.getUserPath(
                "imported.job"
                )
            ) ;
        //
        // Check the stored mime type.
        assertEquals(
            FileProperties.MIME_TYPE_JOB,
            stored.openConnection().getContentType()
            ) ;
        }

    /**
     * Check that importing a URL as 'imported.adql' has the right mime type.
     *
     */
    public void testImportUrlAsAdql()
        throws Exception
        {
        //
        // Create our delegate.
        MySpaceIt05Delegate myspace = delegate() ;
        //
        // Create the user space.
        myspace.createUser(
            this.getUserObject()
            );
        //
        // Get the URL from our config.
        URL source = new URL(
            getTestProperty(
                "data.http.html"
                )
            ) ;
        //
        // Import some data.
        myspace.putUrl(
            source,
            this.getUserPath(
                "imported.adql"
                ),
            false
            );
        //
        // Get the file info.
        StoreFile file = myspace.getFile(
            this.getUserPath(
                "imported.adql"
                )
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_ADQL,
            file.getMimeType()
            ) ;
        //
        // Get the file URL from the manager.
        URL stored = myspace.getUrl(
            this.getUserPath(
                "imported.adql"
                )
            ) ;
        //
        // Check the stored mime type.
        assertEquals(
            FileProperties.MIME_TYPE_ADQL,
            stored.openConnection().getContentType()
            ) ;
        }

//
// What happens is we overwrite an existing file ?
// Currently, Manager will just import a new file.
// Leaving the old data in place on the filestore.
//

    }

