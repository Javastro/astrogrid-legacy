/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/FileStoreTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2006/06/01 14:53:12 $</cvs:date>
 * <cvs:version>$Revision: 1.15 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreTest.java,v $
 *   Revision 1.15  2006/06/01 14:53:12  clq2
 *   dave-dev-200605311657 - fix the broken selftests page
 *
 *   Revision 1.14.20.1  2006/06/01 13:01:14  dave
 *   Fixed self test page in webapp
 *
 *   Revision 1.14  2005/11/04 17:31:05  clq2
 *   axis_gtr_1046
 *
 *   Revision 1.13.54.1  2005/10/11 15:07:06  gtr
 *   The properties org.astrogrid.filestore.test.data.file.miss and org.astrogrid.filestore.test.data.file.text now take just file-names with no attempt to construct file:// URLs in the properties. The construction of the URLs from the file-names is now done in the test classes.
 *
 *   Revision 1.13  2005/03/22 11:41:04  jdt
 *   merge from FS_KMB_1004
 *
 *   Revision 1.12.16.1  2005/03/18 15:37:06  KevinBenson
 *   Added jsp files and a small change or two to some other files for a selftest.jsp
 *
 *   Revision 1.12  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.11.10.7  2005/01/20 07:18:04  dave
 *   Tided up tabs to spaces ..
 *
 *   Revision 1.11.10.6  2005/01/19 13:22:25  dave
 *   Removed fragile date test (fails if it takes less than 1ms to update a file) ..
 *
 *   Revision 1.11.10.5  2005/01/19 12:56:16  dave
 *   Added sleep to date tests to avoid race condition ...
 *
 *   Revision 1.11.10.4  2005/01/17 17:19:21  dave
 *   Fixed bug in FileManagerImpl test (missing '/' in repository path on Unix) ...
 *   Changed tabs to spaces ..
 *
 *   Revision 1.11.10.3  2005/01/15 08:25:50  dave
 *   Refactored file created and modified date handling ..
 *
 *   Revision 1.11.10.2  2005/01/15 04:50:58  dave
 *   Added created and modified dates to server ....
 *   Removed log debug messages from JUnit tests ...
 *
 *   Revision 1.11.10.1  2005/01/15 03:46:50  dave
 *   Added initial tests for create and modified date(s) ..
 *
 *   Revision 1.11  2004/11/25 00:19:19  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.10.14.5  2004/11/09 17:41:36  dave
 *   Added file:// URL handling to allow server URLs to be tested.
 *   Added importInit and exportInit to server implementation.
 *   Moved remaining tests out of extended test abd removed it.
 *
 *   Revision 1.10.14.4  2004/11/06 19:12:18  dave
 *   Refactored identifier properties ...
 *
 *   Revision 1.10.14.3  2004/10/27 18:58:48  dave
 *   Uncommented the rest of the tests ...
 *
 *   Revision 1.10.14.2  2004/10/27 10:56:30  dave
 *   Changed inport init to save the details, and simplified tests for debug
 *
 *   Revision 1.10.14.1  2004/10/21 21:00:13  dave
 *   Added mock://xyz URL handler to enable testing of transfer.
 *   Implemented importInit to the mock service and created transfer tests.
 *
 *   Revision 1.10  2004/09/17 06:57:10  dave
 *   Added commons logging to FileStore.
 *   Updated logging properties in Community.
 *   Fixed bug in AGINAB deployment.
 *   Removed MySpace tests with hard coded grendel address.
 *
 *   Revision 1.9.16.1  2004/09/17 01:08:36  dave
 *   Updated debug to use commons logging API ....
 *
 *   Revision 1.9  2004/09/02 10:25:41  dave
 *   Updated FileStore and MySpace to handle mime type and file size.
 *   Updated Community deployment script.
 *
 *   Revision 1.8.2.1  2004/09/01 02:58:07  dave
 *   Updated to use external mime type for imported files.
 *
 *   Revision 1.8  2004/08/27 22:43:15  dave
 *   Updated filestore and myspace to report file size correctly.
 *
 *   Revision 1.7.12.3  2004/08/27 11:48:41  dave
 *   Added getContentSize to FileProperties.
 *
 *   Revision 1.7.12.2  2004/08/26 19:41:47  dave
 *   Updated tests to check import URL size.
 *
 *   Revision 1.7.12.1  2004/08/26 19:06:50  dave
 *   Modified filestore to return file size in properties.
 *
 *   Revision 1.7  2004/08/18 19:00:01  dave
 *   Myspace manager modified to use remote filestore.
 *   Tested before checkin - integration tests at 91%.
 *
 *   Revision 1.6.10.3  2004/08/09 10:16:28  dave
 *   Added resource URL to the properties.
 *
 *   Revision 1.6.10.2  2004/08/06 22:25:06  dave
 *   Refactored bits and broke a few tests ...
 *
 *   Revision 1.6.10.1  2004/08/02 14:54:15  dave
 *   Trying to get integration tests to run
 *
 *   Revision 1.6  2004/07/23 09:11:16  dave
 *   Merged development branch, dave-dev-200407221513, into HEAD
 *
 *   Revision 1.5.4.2  2004/07/23 03:37:06  dave
 *   Debugged tests and parser bugs
 *
 *   Revision 1.5.4.1  2004/07/23 02:10:58  dave
 *   Added IvornFactory and IvornParser
 *
 *   Revision 1.5  2004/07/22 13:40:26  dave
 *   Merged development branch, dave-dev-200407211922, into HEAD
 *
 *   Revision 1.4.2.1  2004/07/22 13:10:37  dave
 *   Updated integration test config
 *
 *   Revision 1.4  2004/07/21 18:11:55  dave
 *   Merged development branch, dave-dev-200407201059, into HEAD
 *
 *   Revision 1.3.2.2  2004/07/21 16:28:16  dave
 *   Added content properties and tests
 *
 *   Revision 1.3.2.1  2004/07/20 19:10:40  dave
 *   Refactored to implement URL import
 *
 *   Revision 1.3  2004/07/19 23:42:07  dave
 *   Merged development branch, dave-dev-200407151443, into HEAD
 *
 *   Revision 1.2.4.1  2004/07/19 19:40:28  dave
 *   Debugged and worked around Axis Exception handling
 *
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.5  2004/07/13 16:37:29  dave
 *   Refactored common and client to use an array of FileProperties (more SOAP friendly)
 *
 *   Revision 1.1.2.4  2004/07/12 14:39:03  dave
 *   Added server repository classes
 *
 *   Revision 1.1.2.3  2004/07/07 14:54:35  dave
 *   Changed DataInfo to File Info (leaves room to use DataInfo for the more abstract VoStore interface).
 *
 *   Revision 1.1.2.2  2004/07/07 11:55:54  dave
 *   Fixed byte array compare in tests
 *
 *   Revision 1.1.2.1  2004/07/05 04:50:29  dave
 *   Created initial FileStore components
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common ;

import java.net.URL ;
import java.net.URLConnection ;
import java.net.HttpURLConnection ;

import java.util.Date ;

import java.io.OutputStream ;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import junit.framework.TestCase ;

import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.file.FileProperties ;
import org.astrogrid.filestore.common.ivorn.FileStoreIvornFactory ;
import org.astrogrid.filestore.common.transfer.TransferProperties ;
import org.astrogrid.filestore.common.exception.FileStoreException ;
import org.astrogrid.filestore.common.exception.FileStoreNotFoundException ;
import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;
import org.astrogrid.filestore.common.exception.FileStoreTransferException ;

import org.astrogrid.filestore.common.transfer.TransferUtil ;
import org.astrogrid.filestore.common.transfer.UrlGetRequest ;
import org.astrogrid.filestore.common.transfer.UrlGetTransfer ;
import org.astrogrid.filestore.common.transfer.UrlPutTransfer ;
import org.astrogrid.filestore.common.transfer.TransferProperties ;

/**
 * A JUnit test case for the store service.
 *
 */
public class FileStoreTest
    extends TestCase
    {
    /**
     * The name of our test property.
     *
     */
    public static final String TEST_PROPERTY_NAME  = "test.property" ;

    /**
     * The value of our test property.
     *
     */
    public static final String TEST_PROPERTY_VALUE  = "alphabet" ;

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
     * Test properties prefix.
     *
     */
    //public static final String TEST_PROPERTY_PREFIX = "org.astrogrid.filestore.test" ;
    public static String TEST_PROPERTY_PREFIX = "org.astrogrid.filestore.test" ;

    /**
     * Helper method to get a local property.
     *
     */
    public String getTestProperty(String name)
        {
        return System.getProperty(TEST_PROPERTY_PREFIX + "." + name) ;
        }

    /**
     * Test utility to compare two arrays of bytes.
     *
     */
    public static void assertEquals(byte[] left, byte[] right)
        {
        assertEquals(
            "Different array length",
            left.length,
            right.length
            ) ;
        for (int i = 0 ; i < left.length ; i++)
            {
            assertEquals(
                "Wrong value for byte[" + i + "]",
                left[i],
                right[i]
                ) ;
            }
        }

    /**
     * Internal reference to our target service.
     *
     */
    protected FileStore target ;

    /**
     * Test that we can get the service identifier..
     *
     */
    public void testGetServiceIdentifier()
        throws Exception
        {
        assertNotNull(
            "Null service identifier",
            target.identifier()
            ) ;
        }

    /**
     * Check the identifier properties.
     *
     */
    protected void checkIdentProperties(FileProperty[] properties)
        throws Exception
        {
        checkIdentProperties(
            new FileProperties(
                properties
                )
            ) ;
        }

    /**
     * Check the identifier properties.
     *
     */
    protected void checkIdentProperties(FileProperties properties)
        throws Exception
        {
        //
        // Check the info is not null.
        assertNotNull(
            "Null properties",
            properties
            ) ;
        //
        // Check the server ivorn is correct.
        assertEquals(
            "Wrong service ivorn in FileProperties",
            target.identifier(),
            properties.getStoreServiceIvorn().toString()
            ) ;
        //
        // Check the resource ident is not null.
        assertNotNull(
            "Null resource ident in properties",
            properties.getStoreResourceIdent()
            ) ;
        //
        // Check the resource ivorn is correct.
        assertEquals(
            "Wrong resource ident in properties",
            properties.getStoreResourceIvorn().toString(),
            FileStoreIvornFactory.createIdent(
                target.identifier(),
                properties.getStoreResourceIdent()
                )
            ) ;
        //
        // Check the resource URL is not null.
        assertNotNull(
            "Null resource URL in properties",
            properties.getProperty(
                FileProperties.STORE_RESOURCE_URL
                )
            ) ;
        }

    /**
     * Set the type properties.
     *
     */
    protected void configTypeProperties(FileProperties properties, String mime)
        {
        properties.setProperty(
            FileProperties.MIME_TYPE_PROPERTY,
            mime
            ) ;
        }

    /**
     * Check the type properties.
     *
     */
    protected void checkTypeProperties(FileProperties properties, String mime)
        throws Exception
        {
        //
        // Check the info is not null.
        assertNotNull(
            "Null properties",
            properties
            ) ;
        //
        // Check the mime type.
        assertEquals(
            "Wrong mime type property",
            mime,
            properties.getProperty(
                FileProperties.MIME_TYPE_PROPERTY
                )
            ) ;
        }

    /**
     * Set the test property.
     *
     */
    protected void configTestProperty(FileProperties properties, String value)
        {
        properties.setProperty(
            TEST_PROPERTY_NAME,
            value
            ) ;
        }

    /**
     * Check the test property.
     *
     */
    protected void checkTestProperty(FileProperties properties, String value)
        throws Exception
        {
        //
        // Check the ivoa type.
        assertEquals(
            "Wrong test property value",
            value,
            properties.getProperty(
                TEST_PROPERTY_NAME
                )
            ) ;
        }

//
// Import strings.
//

    /**
     * Check we get the right Exception if we import a null string.
     *
     */
    public void testImportNullString()
        throws Exception
        {
        try {
            target.importString(
                null,
                null
                ) ;
            }
        catch (FileStoreException ouch)
            {
            return ;
            }
        fail("Expected FileStoreException") ;
        }

    /**
     * Check we can import a string.
     *
     */
    public void testImportString()
        throws Exception
        {
        //
        // Import the test string.
        FileProperties properties = new FileProperties(
            target.importString(
                null,
                TEST_STRING
                )
            ) ;
        //
        // Check the identifier properties.
        checkIdentProperties(
            properties
            ) ;
        //
        // Check the type properties.
        checkTypeProperties(
            properties,
            null
            ) ;
        //
        // Check the test properties.
        checkTestProperty(
            properties,
            null
            ) ;
        }

    /**
     * Check we can store a string, with additional info.
     *
     */
    public void testImportStringInfo()
        throws Exception
        {
        //
        // Create our data info.
        FileProperties properties = new FileProperties() ;
        //
        // Set the type properties.
        configTypeProperties(
            properties,
            FileProperties.MIME_TYPE_XML
            ) ;
        //
        // Set the test properties.
        configTestProperty(
            properties,
            TEST_PROPERTY_VALUE
            ) ;
        //
        // Import the test string.
        FileProperties imported = new FileProperties(
            target.importString(
                properties.toArray(),
                TEST_STRING
                )
            ) ;
        //
        // Check the identifier properties.
        checkIdentProperties(
            imported
            ) ;
        //
        // Check the type properties.
        checkTypeProperties(
            imported,
            FileProperties.MIME_TYPE_XML
            ) ;
        //
        // Check the test properties.
        checkTestProperty(
            imported,
            TEST_PROPERTY_VALUE
            ) ;
        }

    /**
     * Check we get the right Exception for a null ident.
     *
     */
    public void testExportNullString()
        throws Exception
        {
        try {
            target.exportString(
                null
                ) ;
            }
        catch (FileStoreIdentifierException ouch)
            {
            return ;
            }
        fail("Expected FileStoreIdentifierException") ;
        }

    /**
     * Check we get the right Exception for an unknown ident.
     *
     */
    public void testExportUnknownString()
        throws Exception
        {
        try {
            target.exportString(
                "unknown"
                ) ;
            }
        catch (FileStoreNotFoundException ouch)
            {
            return ;
            }
        fail("Expected FileStoreNotFoundException") ;
        }

    /**
     * Check we can import a string and export it as a string.
     *
     */
    public void testImportStringExportString()
        throws Exception
        {
        //
        // Import the test string.
        FileProperties imported = new FileProperties(
            target.importString(
                null,
                TEST_STRING
                )
            ) ;
        //
        // Check the identifier properties.
        checkIdentProperties(
            imported
            ) ;
        //
        // Check that we get the same string back.
        assertEquals(
            "Wrong string returned",
            TEST_STRING,
            target.exportString(
                imported.getStoreResourceIdent()
                )
            ) ;
        }

    /**
     * Check we can import a string and export it as bytes.
     *
     */
    public void testImportStringExportBytes()
        throws Exception
        {
        //
        // Import the test string.
        FileProperties imported = new FileProperties(
            target.importString(
                null,
                TEST_STRING
                )
            ) ;
        //
        // Check the identifier properties.
        checkIdentProperties(
            imported
            ) ;
        //
        // Check that we get the same string back.
        assertEquals(
            "Wrong bytes returned",
            TEST_STRING,
            new String(
                target.exportBytes(
                    imported.getStoreResourceIdent()
                    )
                )
            ) ;
        }

    /**
     * Check we get the right Exception if we import a null byte array.
     *
     */
    public void testImportNullBytes()
        throws Exception
        {
        try {
            target.importBytes(
                null,
                null
                ) ;
            }
        catch (FileStoreException ouch)
            {
            return ;
            }
        fail("Expected FileStoreException") ;
        }

    /**
     * Check we can import a byte array.
     *
     */
    public void testImportBytes()
        throws Exception
        {
        FileProperties imported = new FileProperties(
            target.importBytes(
                null,
                TEST_BYTES
                )
            ) ;
        //
        // Check the identifier properties.
        checkIdentProperties(
            imported
            ) ;
        //
        // Check the type properties.
        checkTypeProperties(
            imported,
            null
            ) ;
        //
        // Check the test properties.
        checkTestProperty(
            imported,
            null
            ) ;
        }

    /**
     * Check we can import a byte array, with additional info.
     *
     */
    public void testImportBytesInfo()
        throws Exception
        {
        //
        // Create our data info.
        FileProperties properties = new FileProperties() ;
        //
        // Set the type properties.
        configTypeProperties(
            properties,
            FileProperties.MIME_TYPE_XML
            ) ;
        //
        // Set the test properties.
        configTestProperty(
            properties,
            TEST_PROPERTY_VALUE
            ) ;
        //
        // Import the test bytes.
        FileProperties imported = new FileProperties(
            target.importBytes(
                properties.toArray(),
                TEST_BYTES
                )
            ) ;
        //
        // Check the identifier properties.
        checkIdentProperties(
            imported
            ) ;
        //
        // Check the type properties.
        checkTypeProperties(
            imported,
            FileProperties.MIME_TYPE_XML
            ) ;
        //
        // Check the test properties.
        checkTestProperty(
            imported,
            TEST_PROPERTY_VALUE
            ) ;
        }

    /**
     * Check we get the right Exception for a null ident.
     *
     */
    public void testExportNullBytes()
        throws Exception
        {
        try {
            target.exportBytes(
                null
                ) ;
            }
        catch (FileStoreIdentifierException ouch)
            {
            return ;
            }
        fail("Expected FileStoreIdentifierException") ;
        }

    /**
     * Check we get the right Exception for an unknown ident.
     *
     */
    public void testExportUnknownBytes()
        throws Exception
        {
        try {
            target.exportBytes(
                "unknown"
                ) ;
            }
        catch (FileStoreNotFoundException ouch)
            {
            return ;
            }
        fail("Expected FileStoreNotFoundException") ;
        }

    /**
     * Check we can import bytes and export it as bytes.
     *
     */
    public void testImportBytesExportBytes()
        throws Exception
        {
        //
        // Import the test bytes.
        FileProperties imported = new FileProperties(
            target.importBytes(
                null,
                TEST_BYTES
                )
            ) ;
        //
        // Check the identifier properties.
        checkIdentProperties(
            imported
            ) ;
        //
        // Check that we get the same data back.
        assertEquals(
            TEST_BYTES,
            target.exportBytes(
                imported.getStoreResourceIdent()
                )
            ) ;
        }

    /**
     * Check we can import bytes and export it as a string.
     *
     */
    public void testImportBytesExportString()
        throws Exception
        {
        //
        // Import the test bytes.
        FileProperties imported = new FileProperties(
            target.importBytes(
                null,
                TEST_BYTES
                )
            ) ;
        //
        // Check the identifier properties.
        checkIdentProperties(
            imported
            ) ;
        //
        // Check that we get the right string back.
        assertEquals(
            "Wrong bytes returned",
            new String(
                TEST_BYTES
                ),
            target.exportString(
                imported.getStoreResourceIdent()
                )
            ) ;
        }

    /**
     * Check we get the right Exception for a null ident.
     *
     */
    public void testRequestNullInfo()
        throws Exception
        {
        try {
            target.properties(
                null
                ) ;
            }
        catch (FileStoreIdentifierException ouch)
            {
            return ;
            }
        fail("Expected FileStoreIdentifierException") ;
        }

    /**
     * Check we get the right Exception for an unknown ident.
     *
     */
    public void testRequestUnknownInfo()
        throws Exception
        {
        try {
            target.properties(
                "unknown"
                ) ;
            }
        catch (FileStoreNotFoundException ouch)
            {
            return ;
            }
        fail("Expected FileStoreNotFoundException") ;
        }

    /**
     * Check we can import a string and request the info for it.
     *
     */
    public void testRequestStringInfo()
        throws Exception
        {
        //
        // Store the test string.
        FileProperties imported = new FileProperties(
            target.importString(
                null,
                TEST_STRING
                )
            ) ;
        //
        // Check that we can find it.
        FileProperties requested = new FileProperties(
            target.properties(
                imported.getStoreResourceIdent()
                )
            ) ;
        //
        // Check the identifiers are the same.
        assertEquals(
            "Requested ident not the same as imported",
            imported.getStoreResourceIdent(),
            requested.getStoreResourceIdent()
            ) ;
        }

    /**
     * Check we can import bytes and request the info for it.
     *
     */
    public void testRequestBytesInfo()
        throws Exception
        {
        //
        // Store the test string.
        FileProperties imported = new FileProperties(
            target.importBytes(
                null,
                TEST_BYTES
                )
            ) ;
        //
        // Check that we can find it.
        FileProperties requested = new FileProperties(
            target.properties(
                imported.getStoreResourceIdent()
                )
            ) ;
        //
        // Check the identifiers are the same.
        assertEquals(
            "Requested ident not the same as imported",
            imported.getStoreResourceIdent(),
            requested.getStoreResourceIdent()
            ) ;
        }

    /**
     * Check we get the right Exception for a null ident.
     *
     */
    public void testDeleteNull()
        throws Exception
        {
        try {
            target.delete(
                null
                ) ;
            }
        catch (FileStoreIdentifierException ouch)
            {
            return ;
            }
        fail("Expected FileStoreIdentifierException") ;
        }

    /**
     * Check we get the right Exception for an unknown ident.
     *
     */
    public void testDeleteUnknown()
        throws Exception
        {
        try {
            target.delete(
                "unknown"
                ) ;
            }
        catch (FileStoreNotFoundException ouch)
            {
            return ;
            }
        fail("Expected FileStoreNotFoundException") ;
        }

    /**
     * Check we can import a string and delete it.
     *
     */
    public void testDeleteString()
        throws Exception
        {
        //
        // Store the test string.
        FileProperties imported = new FileProperties(
            target.importString(
                null,
                TEST_STRING
                )
            ) ;
        //
        // Check that we can delete it.
        FileProperties deleted = new FileProperties(
            target.delete(
                imported.getStoreResourceIdent()
                )
            ) ;
        //
        // Check the properties are the same.
        assertEquals(
            "Deleted properties not the same as imported",
            imported.getStoreResourceIdent(),
            deleted.getStoreResourceIdent()
            ) ;
        }

    /**
     * Check we can import a byte array and delete it.
     *
     */
    public void testDeleteBytes()
        throws Exception
        {
        //
        // Store the test string.
        FileProperties imported = new FileProperties(
            target.importBytes(
                null,
                TEST_BYTES
                )
            ) ;
        //
        // Check that we can delete it.
        FileProperties deleted = new FileProperties(
            target.delete(
                imported.getStoreResourceIdent()
                )
            ) ;
        //
        // Check the properties are the same.
        assertEquals(
            "Deleted properties not the same as imported",
            imported.getStoreResourceIdent(),
            deleted.getStoreResourceIdent()
            ) ;
        }

    /**
     * Check we can't query the info on something that we have deleted.
     *
     */
    public void testInfoDeleted()
        throws Exception
        {
        //
        // Store the test string.
        FileProperties imported = new FileProperties(
            target.importString(
                null,
                TEST_STRING
                )
            ) ;
        //
        // Check that we can delete it.
        FileProperties deleted = new FileProperties(
            target.delete(
                imported.getStoreResourceIdent()
                )
            ) ;
        //
        // Check that we can't find it.
        try {
            target.properties(
                imported.getStoreResourceIdent()
                ) ;
            }
        catch (FileStoreNotFoundException ouch)
            {
            return ;
            }
        fail("Expected FileStoreNotFoundException") ;
        }

    /**
     * Check we can't export something that we have deleted.
     *
     */
    public void testExportDeleted()
        throws Exception
        {
        //
        // Store the test string.
        FileProperties imported = new FileProperties(
            target.importString(
                null,
                TEST_STRING
                )
            ) ;
        //
        // Check that we can delete it.
        FileProperties deleted = new FileProperties(
            target.delete(
                imported.getStoreResourceIdent()
                )
            ) ;
        //
        // Check that we can't export it.
        try {
            target.exportString(
                imported.getStoreResourceIdent()
                ) ;
            }
        catch (FileStoreNotFoundException ouch)
            {
            return ;
            }
        fail("Expected FileStoreNotFoundException") ;
        }

    /**
     * Check we get the right Exception for a null string.
     *
     */
    public void testAppendNullString()
        throws Exception
        {
        try {
            target.appendString(
                "anything",
                null
                ) ;
            }
        catch (FileStoreException ouch)
            {
            return ;
            }
        fail("Expected FileStoreException") ;
        }

    /**
     * Check we get the right Exception for a null ident.
     *
     */
    public void testAppendStringNullIdent()
        throws Exception
        {
        try {
            target.appendString(
                null,
                EXTRA_STRING
                ) ;
            }
        catch (FileStoreIdentifierException ouch)
            {
            return ;
            }
        fail("Expected FileStoreIdentifierException") ;
        }

    /**
     * Check we get the right Exception for an unknown ident.
     *
     */
    public void testAppendStringUnknownIdent()
        throws Exception
        {
        try {
            target.appendString(
                "unknown",
                EXTRA_STRING
                ) ;
            }
        catch (FileStoreNotFoundException ouch)
            {
            return ;
            }
        fail("Expected FileStoreNotFoundException") ;
        }

    /**
     * Check we can append a string.
     *
     */
    public void testImportStringAppendString()
        throws Exception
        {
        //
        // Import the test string.
        FileProperties imported = new FileProperties(
            target.importString(
                null,
                TEST_STRING
                )
            ) ;
        //
        // Append the extra string.
        FileProperties modified = new FileProperties(
            target.appendString(
                imported.getStoreResourceIdent(),
                EXTRA_STRING
                )
            ) ;
        //
        // Check we get the right string back.
        assertEquals(
            "Wrong string returned",
            (TEST_STRING + EXTRA_STRING),
            target.exportString(
                modified.getStoreResourceIdent()
                )
            ) ;
        }

    /**
     * Check we can append some bytes.
     *
     */
    public void testImportStringAppendBytes()
        throws Exception
        {
        //
        // Import the test string.
        FileProperties imported = new FileProperties(
            target.importString(
                null,
                TEST_STRING
                )
            ) ;
        //
        // Append the extra string.
        FileProperties modified = new FileProperties(
            target.appendBytes(
                imported.getStoreResourceIdent(),
                EXTRA_BYTES
                )
            ) ;
        //
        // Check we get the right string back.
        assertEquals(
            "Wrong string returned",
            (TEST_STRING + new String(EXTRA_BYTES)),
            target.exportString(
                modified.getStoreResourceIdent()
                )
            ) ;
        }

    /**
     * Check we get the right Exception for a null ident.
     *
     */
    public void testDuplicateNullIdent()
        throws Exception
        {
        try {
            target.duplicate(
                null,
                null
                ) ;
            }
        catch (FileStoreIdentifierException ouch)
            {
            return ;
            }
        fail("Expected FileStoreIdentifierException") ;
        }

    /**
     * Check we get the right Exception for an unknown ident.
     *
     */
    public void testDuplicateUnknownIdent()
        throws Exception
        {
        try {
            target.duplicate(
                "unknown",
                null
                ) ;
            }
        catch (FileStoreNotFoundException ouch)
            {
            return ;
            }
        fail("Expected FileStoreNotFoundException") ;
        }

    /**
     * Check a duplicate gets a new ident.
     *
     */
    public void testDuplicateIdent()
        throws Exception
        {
        //
        // Import the test string.
        FileProperties imported = new FileProperties(
            target.importString(
                null,
                TEST_STRING
                )
            ) ;
        //
        // Create a duplicate
        FileProperties duplicate = new FileProperties(
            target.duplicate(
                imported.getStoreResourceIdent(),
                null
                )
            ) ;
        //
        // Check the identifiers are different.
        assertFalse(
            "Duplicate identifiers",
            imported.getStoreResourceIdent().equals(
                duplicate.getStoreResourceIdent()
                )
            ) ;
        }

    /**
     * Check a duplicate gets the right data.
     *
     */
    public void testDuplicateString()
        throws Exception
        {
        //
        // Import the test string.
        FileProperties imported = new FileProperties(
            target.importString(
                null,
                TEST_STRING
                )
            ) ;
        //
        // Create a duplicate
        FileProperties duplicate = new FileProperties(
            target.duplicate(
                imported.getStoreResourceIdent(),
                null
                )
            ) ;
        //
        // Check we get the same string back.
        assertEquals(
            "Wrong string returned",
            TEST_STRING,
            target.exportString(
                duplicate.getStoreResourceIdent()
                )
            ) ;
        }

    /**
     * Check a duplicate gets the right info.
     *
     */
    public void testDuplicateInfo()
        throws Exception
        {
        //
        // Create our data info.
        FileProperties properties = new FileProperties() ;
        //
        // Set the type properties.
        configTypeProperties(
            properties,
            FileProperties.MIME_TYPE_XML
            ) ;
        //
        // Set the test properties.
        configTestProperty(
            properties,
            TEST_PROPERTY_VALUE
            ) ;
        //
        // Import the test bytes.
        FileProperties imported = new FileProperties(
            target.importBytes(
                properties.toArray(),
                TEST_BYTES
                )
            ) ;
        //
        // Create a duplicate
        FileProperties duplicate = new FileProperties(
            target.duplicate(
                imported.getStoreResourceIdent(),
                null
                )
            ) ;
        //
        // Check the identifier properties.
        checkIdentProperties(
            duplicate
            ) ;
        //
        // Check the type properties.
        checkTypeProperties(
            duplicate,
            FileProperties.MIME_TYPE_XML
            ) ;
        //
        // Check the test properties.
        checkTestProperty(
            duplicate,
            TEST_PROPERTY_VALUE
            ) ;
        }

    /**
     * Check we can modify a duplicate.
     *
     */
    public void testDuplicateAppend()
        throws Exception
        {
        //
        // Import the test string.
        FileProperties imported = new FileProperties(
            target.importString(
                null,
                TEST_STRING
                )
            ) ;
        //
        // Create a duplicate.
        FileProperties duplicate = new FileProperties(
            target.duplicate(
                imported.getStoreResourceIdent(),
                null
                )
            ) ;
        //
        // Modify the duplicate.
        FileProperties modified = new FileProperties(
            target.appendString(
                duplicate.getStoreResourceIdent(),
                EXTRA_STRING
                )
            ) ;
        //
        // Check the original is unchanged.
        assertEquals(
            "Wrong string returned",
            TEST_STRING,
            target.exportString(
                imported.getStoreResourceIdent()
                )
            ) ;
        //
        // Check the duplicate has changed.
        assertEquals(
            "Wrong string returned",
            (TEST_STRING + EXTRA_STRING),
            target.exportString(
                modified.getStoreResourceIdent()
                )
            ) ;
        }

    /**
     * Check we get the right Exception for a null url.
     *
     */
    public void testCreateNullUrlGetTransfer()
        throws Exception
        {
        try {
            new UrlGetTransfer(
                null
                ) ;
            }
        catch (IllegalArgumentException ouch)
            {
            return ;
            }
        fail("Expected IllegalArgumentException") ;
        }

    /**
     * Check we can create a transfer info.
     *
     */
    public void testCreateUrlGetTransfer()
        throws Exception
        {
        assertNotNull(
            "Null transfer info",
            new UrlGetTransfer(this.urlForTestTextFile())
            );
        }

    /**
     * Check we get the right Exception for a null transfer properties.
     *
     */
    public void testImportNullTransfer()
        throws Exception
        {
        try {
            target.importData(
                null
                ) ;
            }
        catch (FileStoreTransferException ouch)
            {
            return ;
            }
        fail("Expected FileStoreTransferException") ;
        }

    /**
     * Check that we get the right exception from a failed import.
     *
     */
    public void testImportMissing()
        throws Exception
        {
        try {
            target.importData(new UrlGetTransfer(this.urlForTestMissingFile()));
            }
        catch (FileStoreTransferException ouch)
            {
            return ;
            }
        fail("Expected FileStoreTransferException") ;
        }

    /**
     * Check that we can import a local file.
     *
     */
    public void testImportFile()
        throws Exception
        {
        TransferProperties transfer
            = target.importData(new UrlGetTransfer(this.urlForTestTextFile()));
        assertNotNull(
            "Null transfer properties",
            transfer
            ) ;
        }

    /**
     * Check that we can import from a http server.
     *
     */
    public void testImportHttp()
        throws Exception
        {
        TransferProperties transfer =
            target.importData(
                new UrlGetTransfer(
                    new URL(
                        getTestProperty(
                            "data.http.html"
                            )
                        )
                    )
                ) ;
        assertNotNull(
            "Null transfer properties",
            transfer
            ) ;
        }

    /**
     * Check that we get valid file properties from an import.
     *
     */
    public void testImportProperties()
        throws Exception
        {
        TransferProperties transfer =
            target.importData(new UrlGetTransfer(this.urlForTestTextFile()));
        assertNotNull(
            "Null transfer properties",
            transfer
            ) ;
        checkIdentProperties(
            transfer.getFileProperties()
            ) ;
        }

//
// Check test properties after an import.
//

//
// Check the source URL after an import.
//

//
// Check the MD5 after an import.
//

    /**
     * Check that an imported file contains the right content.
     *
     */
    public void testImportContent()
        throws Exception
        {
        //
        // Import a text file.
        TransferProperties transfer =
            target.importData(new UrlGetTransfer(this.urlForTestTextFile()));
        //
        // Get the imported data properties.
        FileProperties imported = new FileProperties(
            transfer.getFileProperties()
            ) ;
        //
        // Check the imported content.
        assertEquals(
            "Wrong content from URL import",
            TEST_STRING,
            target.exportString(
                imported.getStoreResourceIdent()
                )
            ) ;
        }

    /**
     * Check that imported HTML has the right type.
     *
     */
    public void testImportTypeHtml()
        throws Exception
        {
        //
        // Import a text file.
        TransferProperties transfer =
            target.importData(
                new UrlGetTransfer(
                    new URL(
                        getTestProperty(
                            "data.http.html"
                            )
                        )
                    )
                ) ;
        //
        // Get the imported data properties.
        FileProperties imported = new FileProperties(
            transfer.getFileProperties()
            ) ;
        //
        // Check the imported data has the right type.
        checkTypeProperties(
            imported,
            "text/html; charset=ISO-8859-1"
            ) ;
        }

    /**
     * Check that an imported jar has the right type.
     *
     */
    public void testImportTypeJar()
        throws Exception
        {
        //
        // Import a text file.
        TransferProperties transfer =
            target.importData(
                new UrlGetTransfer(
                    new URL(
                        getTestProperty(
                            "data.http.jar"
                            )
                        )
                    )
                ) ;
        //
        // Get the imported data properties.
        FileProperties imported = new FileProperties(
            transfer.getFileProperties()
            ) ;
        //
        // Check the imported data has the right type.
        checkTypeProperties(
            imported,
            "text/plain; charset=ISO-8859-1"
            ) ;
        }

    /**
     * Check that an imported properties has the right source.
     *
     */
    public void testImportSource()
        throws Exception
        {
        //
        // Import a text file.
        TransferProperties transfer =
            target.importData(
                new UrlGetTransfer(
                    new URL(
                        getTestProperty(
                            "data.http.html"
                            )
                        )
                    )
                ) ;
        //
        // Get the imported data properties.
        FileProperties imported = new FileProperties(
            transfer.getFileProperties()
            ) ;
        //
        // Check the imported data has the right source.
        assertEquals(
            "Wrong string returned",
            getTestProperty(
                "data.http.html"
                ),
            imported.getProperty(
                FileProperties.TRANSFER_SOURCE_URL
                )
            ) ;
        }

    /**
     * Check that an imported byte array has the right size.
     *
     */
    public void testImportBytesSize()
        throws Exception
        {
        //
        // Import some bytes.
        FileProperties imported = new FileProperties(
            target.importBytes(
                null,
                TEST_BYTES
                )
            ) ;
        //
        // Check the imported file size.
        assertEquals(
            TEST_BYTES.length,
            imported.getContentSize()
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
        // Import the test string.
        FileProperties imported = new FileProperties(
            target.importString(
                null,
                TEST_STRING
                )
            ) ;
        //
        // Check the imported file size.
        assertEquals(
            TEST_STRING.getBytes().length,
            imported.getContentSize()
            ) ;
        }

    /**
     * Check that an appended byte array has the right size.
     *
     */
    public void testAppendBytesSize()
        throws Exception
        {
        //
        // Import some bytes.
        FileProperties imported = new FileProperties(
            target.importBytes(
                null,
                TEST_BYTES
                )
            ) ;
        //
        // Append some bytes.
        FileProperties modified = new FileProperties(
            target.appendBytes(
                imported.getStoreResourceIdent(),
                EXTRA_BYTES
                )
            ) ;
        //
        // Check the imported file size.
        assertEquals(
            TEST_BYTES.length + EXTRA_BYTES.length,
            modified.getContentSize()
            ) ;
        }

    /**
     * Check that an imported file URL has the right size.
     *
     */
    public void testImportFileUrlSize()
        throws Exception
        {
        //
        // Import some bytes.
        TransferProperties transfer =
            target.importData(new UrlGetTransfer(this.urlForTestTextFile()));
        //
        // Get the file properties.
        FileProperties properties = new FileProperties(
            transfer.getFileProperties()
            ) ;
        //
        // Check the imported file size.
        assertEquals(
            this.urlForTestTextFile().openConnection().getContentLength(),
            properties.getContentSize()
            ) ;
        }

    /**
     * Check that an imported http URL has the right size.
     *
     */
    public void testImportHttpUrlSize()
        throws Exception
        {
        //
        // Create our URL.
        URL url = new URL(
            getTestProperty(
                "data.http.jar"
                )
            ) ;
        //
        // Import the data.
        TransferProperties transfer =
            target.importData(
                new UrlGetTransfer(
                    url
                    )
                ) ;
        //
        // Get the file properties.
        FileProperties properties = new FileProperties(
            transfer.getFileProperties()
            ) ;
        //
        // Check the imported file size.
        assertEquals(
            url.openConnection().getContentLength(),
            properties.getContentSize()
            ) ;
        }

    /**
     * Check that imported string has the right type.
     *
     */
    public void testImportStringAsXmlVotable()
        throws Exception
        {
        //
        // Create our data info.
        FileProperties properties = new FileProperties() ;
        //
        // Set the mime type property.
        properties.setProperty(
            FileProperties.MIME_TYPE_PROPERTY,
            FileProperties.MIME_TYPE_VOTABLE
            ) ;
        //
        // Import the test string.
        FileProperties imported = new FileProperties(
            target.importString(
                properties.toArray(),
                TEST_STRING
                )
            ) ;
        //
        // Get the file properties.
        FileProperties requested = new FileProperties(
            target.properties(
                imported.getStoreResourceIdent()
                )
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_VOTABLE,
            requested.getProperty(
                FileProperties.MIME_TYPE_PROPERTY
                )
            ) ;
        }

    /**
     * Check that appended string has the right type.
     *
     */
    public void testAppendStringAsXmlVotable()
        throws Exception
        {
        //
        // Create our file properties.
        FileProperties properties = new FileProperties() ;
        //
        // Set the mime type property.
        properties.setProperty(
            FileProperties.MIME_TYPE_PROPERTY,
            FileProperties.MIME_TYPE_VOTABLE
            ) ;
        //
        // Import the test string.
        FileProperties imported = new FileProperties(
            target.importString(
                properties.toArray(),
                TEST_STRING
                )
            ) ;
        //
        // Append the extra string.
        FileProperties modified = new FileProperties(
            target.appendString(
                imported.getStoreResourceIdent(),
                EXTRA_STRING
                )
            ) ;
        //
        // Get the file properties.
        FileProperties requested = new FileProperties(
            target.properties(
                imported.getStoreResourceIdent()
                )
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_VOTABLE,
            requested.getProperty(
                FileProperties.MIME_TYPE_PROPERTY
                )
            ) ;
        }

    /**
     * Check that an imported URL has the right type.
     *
     */
    public void testImportUrlAsVotable()
        throws Exception
        {
        //
        // Create our file properties.
        FileProperties properties = new FileProperties() ;
        //
        // Set the mime type property.
        properties.setProperty(
            FileProperties.MIME_TYPE_PROPERTY,
            FileProperties.MIME_TYPE_VOTABLE
            ) ;
        //
        // Transfer the data.
        TransferProperties transfer =
            target.importData(
                new UrlGetTransfer(
                    new URL(
                        getTestProperty(
                            "data.http.jar"
                            )
                        ),
                    properties
                    )
                ) ;
        //
        // Get the file properties.
        FileProperties requested = new FileProperties(
            transfer.getFileProperties()
            ) ;
        //
        // Check the mime type.
        assertEquals(
            FileProperties.MIME_TYPE_VOTABLE,
            requested.getProperty(
                FileProperties.MIME_TYPE_PROPERTY
                )
            ) ;
        }

/*
 * Gradually moving tests from ExtendedTestCase into here ...
 *
 */

    /**
     * Check we can send a null transfer properties.
     *
     */
    public void testImportInitNullTransfer()
        throws Exception
        {
        try {
            target.importInit(
                null
                ) ;
            }
        catch (FileStoreTransferException ouch)
            {
            return ;
            }
        fail("Expected FileStoreTransferException") ;
        }

    /**
     * Check we can initiate an import.
     *
     */
    public void testImportInit()
        throws Exception
        {
        TransferProperties transfer = new UrlPutTransfer() ;
        assertNotNull(
            target.importInit(
                transfer
                )
            );
        }

    /**
     * Check that the transfer properties contains a URL.
     *
     */
    public void testImportInitURL()
        throws Exception
        {
        TransferProperties transfer = new UrlPutTransfer() ;
        transfer = target.importInit(
            transfer
            ) ;
        assertNotNull(
            transfer.getLocation()
            );
        }

    /**
     * Check that we can open the import URL.
     *
     */
    public void testImportInitURLConnection()
        throws Exception
        {
        TransferProperties transfer = new UrlPutTransfer() ;
        transfer = target.importInit(
            transfer
            ) ;
        URL url = new URL(
            transfer.getLocation()
            ) ;
        assertNotNull(
            url.openConnection()
            );
        }

    /**
     * Check that we get valid file properties for an import.
     *
     */
    public void testImportInitProperties()
        throws Exception
        {
        TransferProperties transfer = new UrlPutTransfer() ;
        transfer = target.importInit(
            transfer
            ) ;
        assertNotNull(
            transfer.getFileProperties()
            );
        }

    /**
     * Check that we can transfer some data.
     *
     */
    public void testImportInitWrite()
        throws Exception
        {
        //
        // Initiate our transfer.
        TransferProperties transfer = target.importInit(
            new UrlPutTransfer()
            ) ;
        //
        // Transfer our data.
        FileStoreOutputStream stream = new FileStoreOutputStream(
            transfer.getLocation()
            ) ;
        stream.open() ;
        stream.write(
            TEST_BYTES
            ) ;
        stream.close() ;
        }

//
// .............
//

    /**
     * Check we get the right Exception for a null transfer properties.
     *
     */
    public void testExportInitNullTransfer()
        throws Exception
        {
        try {
            target.exportInit(
                null
                ) ;
            }
        catch (FileStoreTransferException ouch)
            {
            return ;
            }
        fail("Expected FileStoreTransferException") ;
        }

    /**
     * Check we get the right Exception for an unknown file.
     *
     */
    public void testExportInitNullProperties()
        throws Exception
        {
        TransferProperties transfer = new UrlGetRequest() ;
        try {
            target.exportInit(
                transfer
                ) ;
            }
        catch (FileStoreNotFoundException ouch)
            {
            return ;
            }
        fail("Expected FileStoreNotFoundException") ;
        }

    /**
     * Check that we get a transfer properties for an export.
     *
     */
    public void testExportInitTransfer()
        throws Exception
        {
        //
        // Initiate our import transfer.
        TransferProperties importTransfer = target.importInit(
            new UrlPutTransfer()
            ) ;
        //
        // Open an output stream.
        FileStoreOutputStream importStream = new FileStoreOutputStream(
            importTransfer.getLocation()
            ) ;
        //
        // Open our stream.
        importStream.open() ;
        //
        // Transfer our data.
        importStream.write(
            TEST_BYTES
            ) ;
        //
        // Close our stream.
        importStream.close() ;
        //
        // Get the file properties.
        FileProperties properties = new FileProperties(
            importTransfer.getFileProperties()
            ) ;
        //
        // Initiate our export transfer.
        TransferProperties exportTransfer = target.exportInit(
            new UrlGetRequest(
                properties
                )
            ) ;
        assertNotNull(
            exportTransfer
            );
        }

    /**
     * Check that we get the right Exception for a null file identifier.
     *
     */
    public void testExportInitEmptyProperties()
        throws Exception
        {
        //
        // Create our file properties.
        FileProperties properties = new FileProperties() ;
        //
        // Create our transfer request.
        TransferProperties transfer = new UrlGetRequest(
            properties
            ) ;
        try {
            target.exportInit(
                transfer
                ) ;
            }
        catch (FileStoreNotFoundException ouch)
            {
            return ;
            }
        fail("Expected FileStoreNotFoundException") ;
        }

    /**
     * Check that we get the right Exception for an unknown file identifier.
     *
     */
// ....

    /**
     * Check that the transfer properties contains a location URL.
     *
     */
    public void testExportInitTransferLocation()
        throws Exception
        {
        //
        // Initiate our import transfer.
        TransferProperties importTransfer = target.importInit(
            new UrlPutTransfer()
            ) ;
        //
        // Open an output stream.
        FileStoreOutputStream importStream = new FileStoreOutputStream(
            importTransfer.getLocation()
            ) ;
        //
        // Open our stream.
        importStream.open() ;
        //
        // Transfer our data.
        importStream.write(
            TEST_BYTES
            ) ;
        //
        // Close our stream.
        importStream.close() ;
        //
        // Get the file properties.
        FileProperties properties = new FileProperties(
            importTransfer.getFileProperties()
            ) ;
        //
        // Initiate our export transfer.
        TransferProperties exportTransfer = target.exportInit(
            new UrlGetRequest(
                properties
                )
            ) ;
        assertNotNull(
            exportTransfer.getLocation()
            );
        }

    /**
     * Check that we read data from an export stream.
     *
     */
    public void testExportInitRead()
        throws Exception
        {
        //
        // Initiate our import transfer.
        TransferProperties importTransfer = target.importInit(
            new UrlPutTransfer()
            ) ;
        //
        // Open an output stream.
        FileStoreOutputStream importStream = new FileStoreOutputStream(
            importTransfer.getLocation()
            ) ;
        //
        // Open our stream.
        importStream.open() ;
        //
        // Transfer our data.
        importStream.write(
            TEST_BYTES
            ) ;
        //
        // Close our stream.
        importStream.close() ;
        //
        // Get the file properties.
        FileProperties properties = new FileProperties(
            importTransfer.getFileProperties()
            ) ;
        //
        // Initiate our export transfer.
        TransferProperties exportTransfer = target.exportInit(
            new UrlGetRequest(
                properties
                )
            ) ;
        //
        // Get an input stream to the file.
        FileStoreInputStream exportStream = new FileStoreInputStream(
            exportTransfer.getLocation()
            );
        //
        // Open our input stream.
        exportStream.open() ;
        //
        // Create our buffer stream.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream() ;
        //
        // Read our test bytes.
        TransferUtil util = new TransferUtil(
            exportStream,
            buffer
            );
        util.transfer();
        //
        // Close our input stream.
        exportStream.close() ;
        //
        // Check we got the right data back.
        assertEquals(
            TEST_BYTES,
            buffer.toByteArray()
            );
        }

    /**
     * Check we can get the initial dates for a file.
     *
     */
    public void testInitialDates()
        throws Exception
        {
        //
        // Import the test string.
        FileProperties properties = new FileProperties(
            target.importString(
                null,
                TEST_STRING
                )
            ) ;
        //
        // Check the create date.
        Date created = properties.getFileCreateDate();
        assertNotNull(
            created
            );
        //
        // Check the modified date.
        Date modified = properties.getFileModifyDate();
        assertNotNull(
            modified
            );
        }

    /**
     * Check that adding some data changes the modified date.
     *
     */
    public void testModifiedDates()
        throws Exception
        {
        //
        // Import the test string.
        FileProperties imported = new FileProperties(
            target.importString(
                null,
                TEST_STRING
                )
            ) ;
        //
        // Get the initial dates.
        Date initialCreateDate = imported.getFileCreateDate();
        Date initialModifyDate = imported.getFileModifyDate();
        //
        // Pause for a bit ....
        Thread.sleep(1000);
        //
        // Append the extra string.
        FileProperties modified = new FileProperties(
            target.appendString(
                imported.getStoreResourceIdent(),
                EXTRA_STRING
                )
            ) ;
        //
        // Get the updated dates.
        Date updatedCreateDate = modified.getFileCreateDate();
        Date updatedModifyDate = modified.getFileModifyDate();
        //
        // Check the create dates are the same.
        assertEquals(
            initialCreateDate,
            updatedCreateDate
            );
        //
        // Check the modified date has changed.
        assertTrue(
            updatedModifyDate.after(
                initialModifyDate
                )
            );
        }

    /**
     * Determines the URL for a test file.
     * The file is specified in the system properties.
     * No test is made that the file exists or is accessible.
     *
     * @return The URL.
     * @throws Exception If the given file-name cannot be converted to a URL.
     */
    protected URL urlForTestTextFile() throws Exception {
      String name = getTestProperty("data.file.text");
      File file = new File(name);
      URL url = file.toURL();
      return url;
    }

    /**
     * Determines the URL for a test file.
     * The file is specified in the system properties.
     * No test is made that the file exists or is accessible.
     *
     * @return The URL.
     * @throws Exception If the given file-name cannot be converted to a URL.
     */
    protected URL urlForTestMissingFile() throws Exception {
      String name = getTestProperty("data.file.miss");
      File file = new File(name);
      URL url = file.toURL();
      return url;
    }

    }

