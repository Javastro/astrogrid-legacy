/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/Attic/StoreClientWrapperTest.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: StoreClientWrapperTest.java,v $
 *   Revision 1.2  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.1.2.3  2005/01/12 14:20:57  dave
 *   Replaced tabs with spaces ....
 *
 *   Revision 1.1.2.2  2005/01/10 11:29:29  dave
 *   Tweaked comments for getPath() ....
 *
 *   Revision 1.1.2.1  2005/01/07 12:18:00  dave
 *   Added StoreClientWrapperTest
 *   Added StoreFileWrapper
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

import org.astrogrid.store.Agsl;
import org.astrogrid.store.Msrl;
import org.astrogrid.store.Ivorn ;

import org.astrogrid.filestore.common.transfer.TransferUtil;

import org.astrogrid.filemanager.common.BaseTest;

import org.astrogrid.filemanager.common.exception.NodeNotFoundException ;
import org.astrogrid.filemanager.common.exception.DuplicateNodeException;

/**
 * A generic test for the StoreClientWrapper API.
 *
 */
public class StoreClientWrapperTest
    extends BaseTest
    {

    /**
     * A test string.
     * "A short test string ...."
     *
     */
    public static final String TEST_STRING = "A short test string ...." ;

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
     * A set of ivorn identifiers for our target file stores.
     *
     */
    protected Ivorn[] filestores ;

    /**
     * Our target FileManager delegate.
     *
     */
    protected FileManagerDelegate delegate;

    /**
     * Our target StoreClientWrapper wrapper.
     *
     */
    protected StoreClientWrapper wrapper;

    /**
     * Set up our test.
     *
     */
    public void setUp()
        throws Exception
        {
        super.setUp();
        }

    /**
     * Check have a delegate.
     *
     */
    public void testCreateDelegate()
        throws Exception
        {
        assertNotNull(
            delegate
            );
        }

    /**
     * Create an account.
     *
     */
    protected FileManagerNode account()
        throws Exception
        {
        return delegate.addAccount(
            accountIvorn
            ) ;
        }

    /**
     * Check that we can create a new account.
     *
     */
    public void testCreateAccount()
        throws Exception
        {
        assertNotNull(
            account()
            );
        }

    /**
     * Check have a wrapper.
     *
     */
    public void testCreateWrapper()
        throws Exception
        {
        assertNotNull(
            wrapper
            );
        }

    /**
     * Check that we can get the endpoint Agsl.
     *
     */
    public void testGetEndpoint()
        throws Exception
        {
        Agsl agsl = wrapper.getEndpoint();
        System.out.println(agsl.toString());
        assertNotNull(
            agsl
            );
        }

    /**
     * Check that the file path method works.
     *
     */
    public void testFilePath()
        throws Exception
        {
        assertNull(
            wrapper.getFilePath(
                ""
                )
            );
        assertNull(
            wrapper.getFilePath(
                "/"
                )
            );
        assertNull(
            wrapper.getFilePath(
                "frog"
                )
            );
        assertNull(
            wrapper.getFilePath(
                "/frog"
                )
            );
        assertEquals(
            "frog",
            wrapper.getFilePath(
                "frog/"
                )
            );
        assertEquals(
            "/frog",
            wrapper.getFilePath(
                "/frog/"
                )
            );
        assertEquals(
            "frog",
            wrapper.getFilePath(
                "frog/toad"
                )
            );
        assertEquals(
            "/frog",
            wrapper.getFilePath(
                "/frog/toad"
                )
            );
        assertEquals(
            "frog/",
            wrapper.getFilePath(
                "frog//toad"
                )
            );
        assertEquals(
            "/frog/",
            wrapper.getFilePath(
                "/frog//toad"
                )
            );
        }

    /**
     * Check that the file name method works.
     *
     */
    public void testFileName()
        throws Exception
        {
        assertNull(
            wrapper.getFileName(
                "/"
                )
            );

        assertEquals(
            "frog",
            wrapper.getFileName(
                "frog"
                )
            );
        assertEquals(
            "frog",
            wrapper.getFileName(
                "/frog"
                )
            );

        assertNull(
            wrapper.getFileName(
                "frog/"
                )
            );
        assertNull(
            wrapper.getFileName(
                "/frog/"
                )
            );

        assertEquals(
            "toad",
            wrapper.getFileName(
                "frog/toad"
                )
            );
        assertEquals(
            "toad",
            wrapper.getFileName(
                "/frog/toad"
                )
            );

        assertEquals(
            "toad",
            wrapper.getFileName(
                "frog//toad"
                )
            );
        assertEquals(
            "toad",
            wrapper.getFileName(
                "/frog//toad"
                )
            );
        }

    /**
     * Check that we can get an Agsl for a file.
     *
     */
    public void testGetAgsl()
        throws Exception
        {
        Agsl agsl = this.wrapper.getAgsl(
            "root/path/file"
            );
        System.out.println(agsl.toString());
        assertNotNull(
            agsl
            );
        }

    /**
     * Check that we can create a file.
     *
     */
    public void testAddFile()
        throws Exception
        {
        FileManagerNode home = account() ;
        Ivorn  base = home.ivorn();
        String root = base.getFragment();
        String path = root + "/frog" ;
        System.out.println("");
        System.out.println("testAddFile()");
        System.out.println("Base : " + base.toString());
        System.out.println("Path : " + path);
        System.out.println("");
        FileManagerNode frog = wrapper.newFile(
            path
            );
        assertNotNull(
            frog
            );
        }

    /**
     * Check that we can get a file.
     *
     */
    public void testGetFile()
        throws Exception
        {
        FileManagerNode home = account() ;
        Ivorn  base = home.ivorn();
        String root = base.getFragment();
        String path = root + "/frog" ;
        System.out.println("");
        System.out.println("testGetFile()");
        System.out.println("Base : " + base.toString());
        System.out.println("Path : " + path);
        System.out.println("");
        FileManagerNode frog = wrapper.newFile(
            path
            );
        assertNotNull(
            wrapper.getFile(
                path
                )
            );
        }

    /**
     * Check we can create a folder.
     *
     */
    public void testAddFolder()
        throws Exception
        {
        FileManagerNode home = account() ;
        Ivorn  base = home.ivorn();
        String root = base.getFragment();
        String path = root + "/frog" ;
        System.out.println("");
        System.out.println("testAddFolder()");
        System.out.println("Base : " + base.toString());
        System.out.println("Path : " + path);
        System.out.println("");
        wrapper.newFolder(
            path
            );
        }

    /**
     * Check we can get a folder.
     *
     */
    public void testGetFolder()
        throws Exception
        {
        FileManagerNode home = account() ;
        Ivorn  base = home.ivorn();
        String root = base.getFragment();
        String path = root + "/frog" ;
        System.out.println("");
        System.out.println("testGetFolder()");
        System.out.println("Base : " + base.toString());
        System.out.println("Path : " + path);
        System.out.println("");
        wrapper.newFolder(
            path
            );
        assertNotNull(
            wrapper.getFile(
                path
                )
            );
        }

    /**
     * Check we can create nested file(s).
     *
     */
    public void testAddFiles()
        throws Exception
        {
        FileManagerNode home = account() ;
        Ivorn  base = home.ivorn();
        String path = base.getFragment();
        System.out.println("");
        System.out.println("testAddFiles()");
        System.out.println("Base : " + base.toString());
        System.out.println("Root : " + path);
        System.out.println("");
        wrapper.newFolder(
            path + "/frog"
            );
        wrapper.newFolder(
            path + "/frog/toad"
            );
        wrapper.newFile(
            path + "/frog/toad/newt"
            );
        assertNotNull(
            wrapper.getFile(
                path
                )
            );
        assertNotNull(
            wrapper.getFile(
                path + "/frog"
                )
            );
        assertNotNull(
            wrapper.getFile(
                path + "/frog/toad"
                )
            );
        assertNotNull(
            wrapper.getFile(
                path + "/frog/toad/newt"
                )
            );
        }

    /**
     * Check that we can create intermediate nodes.
     * This will fail until the server side can create intermediate folders.
     *
    public void testCreateBetween()
        throws Exception
        {
        FileManagerNode home = account() ;
        Ivorn  base = home.ivorn();
        String path = base.getFragment();
        System.out.println("");
        System.out.println("testAddFiles()");
        System.out.println("Base : " + base.toString());
        System.out.println("Root : " + path);
        System.out.println("");
        //
        // Create the file, and the required folders.
        wrapper.newFile(
            path + "/frog/toad/newt"
            );
        assertNotNull(
            wrapper.getFile(
                path
                )
            );
        assertNotNull(
            wrapper.getFile(
                path + "/frog"
                )
            );
        assertNotNull(
            wrapper.getFile(
                path + "/frog/toad"
                )
            );
        assertNotNull(
            wrapper.getFile(
                path + "/frog/toad/newt"
                )
            );
        }
     */


    /**
     * Check that a file has the right name.
     *
     */

    /**
     * Check that a file has the right path.
     *
     */

    /**
     * Test we can open an output stream to a file that exists.
     *
     */
    public void testPutStream()
        throws Exception
        {
        FileManagerNode home = account() ;
        Ivorn  base = home.ivorn();
        String root = base.getFragment();
        String path = root + "/frog" ;
        System.out.println("");
        System.out.println("testPutStream()");
        System.out.println("Base : " + base.toString());
        System.out.println("Path : " + path);
        System.out.println("");
        assertNotNull(
            wrapper.newFile(
                path
                )
            );
        assertNotNull(
            wrapper.putStream(
                path,
                false
                )
            );
        }

    /**
     * Test we can send data to a file that exists.
     *
     */
    public void testPutStreamWrite()
        throws Exception
        {
        FileManagerNode home = account() ;
        Ivorn  base = home.ivorn();
        String root = base.getFragment();
        String path = root + "/frog" ;
        System.out.println("");
        System.out.println("testPutStreamWrite()");
        System.out.println("Base : " + base.toString());
        System.out.println("Path : " + path);
        System.out.println("");
        assertNotNull(
            wrapper.newFile(
                path
                )
            );
        OutputStream put =
            wrapper.putStream(
                path,
                false
                );
        put.write(
            TEST_BYTES
            ) ;
        put.close() ;
        }

    /**
     * Test we can open an output stream to a new file.
     *
     */
    public void testPutStreamNew()
        throws Exception
        {
        FileManagerNode home = account() ;
        Ivorn  base = home.ivorn();
        String root = base.getFragment();
        String path = root + "/frog" ;
        System.out.println("");
        System.out.println("testPutStreamNew()");
        System.out.println("Base : " + base.toString());
        System.out.println("Path : " + path);
        System.out.println("");
        assertNotNull(
            wrapper.putStream(
                path,
                false
                )
            );
        }

    /**
     * Test we can send data to a new file.
     *
     */
    public void testPutStreamNewWrite()
        throws Exception
        {
        FileManagerNode home = account() ;
        Ivorn  base = home.ivorn();
        String root = base.getFragment();
        String path = root + "/frog" ;
        System.out.println("");
        System.out.println("testPutStreamNewWrite()");
        System.out.println("Base : " + base.toString());
        System.out.println("Path : " + path);
        System.out.println("");
        OutputStream put =
            wrapper.putStream(
                path,
                false
                );
        put.write(
            TEST_BYTES
            ) ;
        put.close() ;
        }

    /**
     * Check that we can write bytes to an existing file.
     *
     */
    public void testPutBytes()
        throws Exception
        {
        FileManagerNode home = account() ;
        Ivorn  base = home.ivorn();
        String root = base.getFragment();
        String path = root + "/frog" ;
        System.out.println("");
        System.out.println("testPutBytes()");
        System.out.println("Base : " + base.toString());
        System.out.println("Path : " + path);
        System.out.println("");
        //
        // Create the file.
        assertNotNull(
            wrapper.newFile(
                path
                )
            );
        //
        // Write the data to the file.
        wrapper.putBytes(
            TEST_BYTES,
            0,
            TEST_BYTES.length,
            path,
            false
            );
        }

    /**
     * Check that we can write a string to an existing file.
     *
     */
    public void testPutString()
        throws Exception
        {
        FileManagerNode home = account() ;
        Ivorn  base = home.ivorn();
        String root = base.getFragment();
        String path = root + "/frog" ;
        System.out.println("");
        System.out.println("testPutBytes()");
        System.out.println("Base : " + base.toString());
        System.out.println("Path : " + path);
        System.out.println("");
        //
        // Create the file.
        assertNotNull(
            wrapper.newFile(
                path
                )
            );
        //
        // Write the data to the file.
        wrapper.putString(
            TEST_STRING,
            path,
            false
            );
        }

    /**
     * Check that we can open an input stream from a file.
     *
     */
    public void testGetStreamRead()
        throws Exception
        {
        FileManagerNode home = account() ;
        Ivorn  base = home.ivorn();
        String root = base.getFragment();
        String path = root + "/frog" ;
        System.out.println("");
        System.out.println("testGetStreamRead()");
        System.out.println("Base : " + base.toString());
        System.out.println("Path : " + path);
        System.out.println("");
        //
        // Send our test data.
        OutputStream put =
            wrapper.putStream(
                path,
                false
                );
        put.write(
            TEST_BYTES
            ) ;
        put.close() ;
        //
        // Try reading our data back.
        InputStream get =
            wrapper.getStream(
                path
                );
        //
        // Create our buffer stream.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream() ;
        //
        // Read the data from the file.
        new TransferUtil(
            get,
            buffer
            ).transfer();
        get.close();
        //
        // Check we got the right data back.
        assertEquals(
            TEST_BYTES,
            buffer.toByteArray()
            );
        }

    }

