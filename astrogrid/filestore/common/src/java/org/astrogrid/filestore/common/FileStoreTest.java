/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/FileStoreTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/02 10:25:41 $</cvs:date>
 * <cvs:version>$Revision: 1.9 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreTest.java,v $
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

import junit.framework.TestCase ;

import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.file.FileProperties ;
import org.astrogrid.filestore.common.ivorn.FileStoreIvornFactory ;
import org.astrogrid.filestore.common.transfer.TransferProperties ;
import org.astrogrid.filestore.common.exception.FileStoreException ;
import org.astrogrid.filestore.common.exception.FileStoreNotFoundException ;
import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;
import org.astrogrid.filestore.common.exception.FileStoreTransferException ;

import org.astrogrid.filestore.common.transfer.UrlGetTransfer ;

/**
 * A JUnit test case for the store service.
 *
 */
public class FileStoreTest
	extends TestCase
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	protected static final boolean DEBUG_FLAG = true ;

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
	public static final String TEST_PROPERTY_PREFIX = "org.astrogrid.filestore.test" ;

	/**
	 * Helper method to get a local property.
	 *
	 */
	public String getTestProperty(String name)
		{
		return System.getProperty(TEST_PROPERTY_PREFIX + "." + name) ;
		}

	/**
	 * Debug utility to print an array of bytes.
	 *
	 */
	public static void printBytes(byte[] data)
		{
		System.out.println("--------") ;
		for (int i = 0 ; i < data.length ; i++)
			{
			System.out.println(
				"[" + i + "] '" + Integer.toHexString(data[i]) + "'"
				) ;
			}
		System.out.println("--------") ;
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
	 * Internal reference to our target service.
	 *
	 */
	protected FileStore target ;

	/**
	 * Test that the Exception handling works.
	 * Removed, because it causes a SAX Exception in Axis.
	 * Need to figure out why before we use this test.
	public void testIdentifierException()
		throws Exception
		{
		try {
			target.throwIdentifierException() ;
			}
		catch (FileStoreIdentifierException ouch)
			{
			return ;
			}
		fail("Expected FileStoreIdentifierException") ;
		}
	 */

	/**
	 * Test that we can get the service identifier..
	 *
	 */
	public void testGetServiceIdentifier()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testGetServiceIdentifier()") ;
		assertNotNull(
			"Null service identifier",
			target.identifier()
			) ;
		}

//
// Property tests.
//

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
			properties.getProperty(
				FileProperties.STORE_SERVICE_IVORN
				)
			) ;
		//
		// Check the resource ident is not null.
		assertNotNull(
			"Null resource ident in properties",
			properties.getProperty(
				FileProperties.STORE_RESOURCE_IDENT
				)
			) ;
		//
		// Check the resource ivorn is correct.
		assertEquals(
			"Wrong resource ident in properties",
			properties.getProperty(
				FileProperties.STORE_RESOURCE_IVORN
				),
			FileStoreIvornFactory.createIdent(
				target.identifier(),
				properties.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					)
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportNullString()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportString()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportStringInfo()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testExportNullString()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testExportUnknownString()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportStringExportString()") ;
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
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					)
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportStringExportBytes()") ;
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
					imported.getProperty(
						FileProperties.STORE_RESOURCE_IDENT
						)
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportNullBytes()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportBytes()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportBytesInfo()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testExportNullBytes()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testExportUnknownBytes()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportBytesExportBytes()") ;
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
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					)
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportBytesExportString()") ;
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
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					)
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testRequestNullInfo()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testRequestUnknownInfo()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testRequestStringInfo()") ;
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
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					)
				)
			) ;
		//
		// Check the identifiers are the same.
		assertEquals(
			"Requested ident not the same as imported",
			imported.getProperty(
				FileProperties.STORE_RESOURCE_IDENT
				),
			requested.getProperty(
				FileProperties.STORE_RESOURCE_IDENT
				)
			) ;
		}

	/**
	 * Check we can import bytes and request the info for it.
	 *
	 */
	public void testRequestBytesInfo()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testRequestBytesInfo()") ;
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
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					)
				)
			) ;
		//
		// Check the identifiers are the same.
		assertEquals(
			"Requested ident not the same as imported",
			imported.getProperty(
				FileProperties.STORE_RESOURCE_IDENT
				),
			requested.getProperty(
				FileProperties.STORE_RESOURCE_IDENT
				)
			) ;
		}

	/**
	 * Check we get the right Exception for a null ident.
	 *
	 */
	public void testDeleteNull()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testDeleteNull()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testDeleteUnknown()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testDeleteString()") ;
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
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					)
				)
			) ;
		//
		// Check the properties are the same.
		assertEquals(
			"Deleted properties not the same as imported",
			imported.getProperty(
				FileProperties.STORE_RESOURCE_IDENT
				),
			deleted.getProperty(
				FileProperties.STORE_RESOURCE_IDENT
				)
			) ;
		}

	/**
	 * Check we can import a byte array and delete it.
	 *
	 */
	public void testDeleteBytes()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testDeleteBytes()") ;
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
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					)
				)
			) ;
		//
		// Check the properties are the same.
		assertEquals(
			"Deleted properties not the same as imported",
			imported.getProperty(
				FileProperties.STORE_RESOURCE_IDENT
				),
			deleted.getProperty(
				FileProperties.STORE_RESOURCE_IDENT
				)
			) ;
		}

	/**
	 * Check we can't query the info on something that we have deleted.
	 *
	 */
	public void testInfoDeleted()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testInfoDeleted()") ;
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
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					)
				)
			) ;
		//
		// Check that we can't find it.
		try {
			target.properties(
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					)
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testExportDeleted()") ;
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
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					)
				)
			) ;
		//
		// Check that we can't export it.
		try {
			target.exportString(
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					)
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testAppendNullString()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testAppendStringNullIdent()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testAppendStringUnknownIdent()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportStringAppendString()") ;
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
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					),
				EXTRA_STRING
				)
			) ;
		//
		// Check we get the right string back.
		assertEquals(
			"Wrong string returned",
			(TEST_STRING + EXTRA_STRING),
			target.exportString(
				modified.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					)
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportStringAppendBytes()") ;
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
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					),
				EXTRA_BYTES
				)
			) ;
		//
		// Check we get the right string back.
		assertEquals(
			"Wrong string returned",
			(TEST_STRING + new String(EXTRA_BYTES)),
			target.exportString(
				modified.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					)
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testDuplicateNullIdent()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testDuplicateUnknownIdent()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testDuplicateIdent()") ;
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
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					),
				null
				)
			) ;
		//
		// Check the identifiers are different.
		assertFalse(
			"Duplicate identifiers",
			imported.getProperty(
				FileProperties.STORE_RESOURCE_IDENT
				).equals(
					duplicate.getProperty(
						FileProperties.STORE_RESOURCE_IDENT
						)
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testDuplicateString()") ;
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
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					),
				null
				)
			) ;
		//
		// Check we get the same string back.
		assertEquals(
			"Wrong string returned",
			TEST_STRING,
			target.exportString(
				duplicate.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					)
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testDuplicateInfo()") ;
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
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					),
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testDuplicateAppend()") ;
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
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					),
				null
				)
			) ;
		//
		// Modify the duplicate.
		FileProperties modified = new FileProperties(
			target.appendString(
				duplicate.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					),
				EXTRA_STRING
				)
			) ;
		//
		// Check the original is unchanged.
		assertEquals(
			"Wrong string returned",
			TEST_STRING,
			target.exportString(
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					)
				)
			) ;
		//
		// Check the duplicate has changed.
		assertEquals(
			"Wrong string returned",
			(TEST_STRING + EXTRA_STRING),
			target.exportString(
				modified.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					)
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testCreateNullUrlGetTransfer()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testCreateUrlGetTransfer()") ;
		System.out.println("--------") ;
		System.out.println(
			getTestProperty(
				"data.file.text"
				)
			) ;
		System.out.println("--------") ;
		assertNotNull(
			"Null transfer info",
			new UrlGetTransfer(
				new URL(
					getTestProperty(
						"data.file.text"
						)
					)
				)
			) ;
		}

	/**
	 * Check we get the right Exception for a null transfer properties.
	 *
	 */
	public void testImportNullTransfer()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportNullTransfer()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportMissing()") ;
		try {
			target.importData(
				new UrlGetTransfer(
					new URL(
						getTestProperty(
							"data.file.miss"
							)
						)
					)
				) ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportFile()") ;
		TransferProperties transfer = 
			target.importData(
				new UrlGetTransfer(
					new URL(
						getTestProperty(
							"data.file.text"
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
	 * Check that we can import from a http server.
	 *
	 */
	public void testImportHttp()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportHttp()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportProperties()") ;
		TransferProperties transfer = 
			target.importData(
				new UrlGetTransfer(
					new URL(
						getTestProperty(
							"data.file.text"
							)
						)
					)
				) ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportContent()") ;
		//
		// Import a text file.
		TransferProperties transfer = 
			target.importData(
				new UrlGetTransfer(
					new URL(
						getTestProperty(
							"data.file.text"
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
		// Check the imported content.
		assertEquals(
			"Wrong content from URL import",
			TEST_STRING,
			target.exportString(
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					)
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportTypeHtml()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportTypeJar()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportSource()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportBytesSize()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportStringSize()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testAppendBytesSize()") ;
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
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					),
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportFileUrlSize()") ;
		//
		// Create our URL.
		URL url = new URL(
			getTestProperty(
				"data.file.text"
				)
			) ;
		//
		// Import some bytes.
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
	 * Check that an imported http URL has the right size.
	 *
	 */
	public void testImportHttpUrlSize()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportHttpUrlSize()") ;
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportStringAsXmlVotable()") ;
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
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					)
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testAppendStringAsXmlVotable()") ;
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
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					),
				EXTRA_STRING
				)
			) ;
		//
		// Get the file properties.
		FileProperties requested = new FileProperties(
			target.properties(
				imported.getProperty(
					FileProperties.STORE_RESOURCE_IDENT
					)
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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreTest.testImportUrlAsVotable()") ;
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
	}
