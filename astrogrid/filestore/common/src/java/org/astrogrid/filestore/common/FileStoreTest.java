/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/FileStoreTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/19 23:42:07 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreTest.java,v $
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

import junit.framework.TestCase ;

import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.file.FileProperties ;
import org.astrogrid.filestore.common.transfer.TransferInfo ;
import org.astrogrid.filestore.common.exception.FileStoreException ;
import org.astrogrid.filestore.common.exception.FileStoreNotFoundException ;
import org.astrogrid.filestore.common.exception.FileIdentifierException ;

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
		catch (FileIdentifierException ouch)
			{
			return ;
			}
		fail("Expected FileIdentifierException") ;
		}
	 */

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

//
// Property tests.
//
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
		// Check the server identifier is correct.
		assertEquals(
			"Wrong service identifier in FileProperties",
			target.identifier(),
			properties.getProperty(
				FileProperties.STORE_SERVICE_IDENTIFIER
				)
			) ;
		//
		// Check the internal identifier is not null.
		assertNotNull(
			"Null internal identifier in properties",
			properties.getProperty(
				FileProperties.STORE_INTERNAL_IDENTIFIER
				)
			) ;
		}

	/**
	 * Set the type properties.
	 *
	 */
	protected void configTypeProperties(FileProperties properties, String mime, String ivoa)
		{
		properties.setProperty(
			FileProperties.MIME_TYPE_PROPERTY,
			mime
			) ;
		properties.setProperty(
			FileProperties.IVOA_TYPE_PROPERTY,
			ivoa
			) ;
		}

	/**
	 * Check the type properties.
	 *
	 */
	protected void checkTypeProperties(FileProperties properties, String mime, String ivoa)
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
		//
		// Check the ivoa type.
		assertEquals(
			"Wrong ivoa type property",
			ivoa,
			properties.getProperty(
				FileProperties.IVOA_TYPE_PROPERTY
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
			null,
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
			FileProperties.MIME_TYPE_XML,
			FileProperties.IVOA_TYPE_VOTABLE
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
			FileProperties.MIME_TYPE_XML,
			FileProperties.IVOA_TYPE_VOTABLE
			) ;
		//
		// Check the test properties.
		checkTestProperty(
			imported,
			TEST_PROPERTY_VALUE
			) ;
		}

//
// Export strings.
//

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
		catch (FileIdentifierException ouch)
			{
			return ;
			}
		fail("Expected FileIdentifierException") ;
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
				imported.getProperty(
					FileProperties.STORE_INTERNAL_IDENTIFIER
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
						FileProperties.STORE_INTERNAL_IDENTIFIER
						)
					)
				)
			) ;
		}


//
// Import bytes.
//

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
			null,
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
			FileProperties.MIME_TYPE_XML,
			FileProperties.IVOA_TYPE_VOTABLE
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
			FileProperties.MIME_TYPE_XML,
			FileProperties.IVOA_TYPE_VOTABLE
			) ;
		//
		// Check the test properties.
		checkTestProperty(
			imported,
			TEST_PROPERTY_VALUE
			) ;
		}

//
// Export bytes.
//

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
		catch (FileIdentifierException ouch)
			{
			return ;
			}
		fail("Expected FileIdentifierException") ;
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
				imported.getProperty(
					FileProperties.STORE_INTERNAL_IDENTIFIER
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
					FileProperties.STORE_INTERNAL_IDENTIFIER
					)
				)
			) ;
		}

//
// Info request.
//

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
		catch (FileIdentifierException ouch)
			{
			return ;
			}
		fail("Expected FileIdentifierException") ;
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
				imported.getProperty(
					FileProperties.STORE_INTERNAL_IDENTIFIER
					)
				)
			) ;
		//
		// Check the identifiers are the same.
		assertEquals(
			"Requested ident not the same as imported",
			imported.getProperty(
				FileProperties.STORE_INTERNAL_IDENTIFIER
				),
			requested.getProperty(
				FileProperties.STORE_INTERNAL_IDENTIFIER
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
					FileProperties.STORE_INTERNAL_IDENTIFIER
					)
				)
			) ;
		//
		// Check the identifiers are the same.
		assertEquals(
			"Requested ident not the same as imported",
			imported.getProperty(
				FileProperties.STORE_INTERNAL_IDENTIFIER
				),
			requested.getProperty(
				FileProperties.STORE_INTERNAL_IDENTIFIER
				)
			) ;
		}

//
// Delete
//

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
		catch (FileIdentifierException ouch)
			{
			return ;
			}
		fail("Expected FileIdentifierException") ;
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
				imported.getProperty(
					FileProperties.STORE_INTERNAL_IDENTIFIER
					)
				)
			) ;
		//
		// Check the properties are the same.
		assertEquals(
			"Deleted properties not the same as imported",
			imported.getProperty(
				FileProperties.STORE_INTERNAL_IDENTIFIER
				),
			deleted.getProperty(
				FileProperties.STORE_INTERNAL_IDENTIFIER
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
					FileProperties.STORE_INTERNAL_IDENTIFIER
					)
				)
			) ;
		//
		// Check the properties are the same.
		assertEquals(
			"Deleted properties not the same as imported",
			imported.getProperty(
				FileProperties.STORE_INTERNAL_IDENTIFIER
				),
			deleted.getProperty(
				FileProperties.STORE_INTERNAL_IDENTIFIER
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
					FileProperties.STORE_INTERNAL_IDENTIFIER
					)
				)
			) ;
		//
		// Check that we can't find it.
		try {
			target.properties(
				imported.getProperty(
					FileProperties.STORE_INTERNAL_IDENTIFIER
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
					FileProperties.STORE_INTERNAL_IDENTIFIER
					)
				)
			) ;
		//
		// Check that we can't export it.
		try {
			target.exportString(
				imported.getProperty(
					FileProperties.STORE_INTERNAL_IDENTIFIER
					)
				) ;
			}
		catch (FileStoreNotFoundException ouch)
			{
			return ;
			}
		fail("Expected FileStoreNotFoundException") ;
		}

//
// Append string
//

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
		catch (FileIdentifierException ouch)
			{
			return ;
			}
		fail("Expected FileIdentifierException") ;
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
				imported.getProperty(
					FileProperties.STORE_INTERNAL_IDENTIFIER
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
					FileProperties.STORE_INTERNAL_IDENTIFIER
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
					FileProperties.STORE_INTERNAL_IDENTIFIER
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
					FileProperties.STORE_INTERNAL_IDENTIFIER
					)
				)
			) ;
		}

//
// Replicate ....
//

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
		catch (FileIdentifierException ouch)
			{
			return ;
			}
		fail("Expected FileIdentifierException") ;
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
				imported.getProperty(
					FileProperties.STORE_INTERNAL_IDENTIFIER
					),
				null
				)
			) ;
		//
		// Check the identifiers are different.
		assertFalse(
			"Duplicate identifiers",
			imported.getProperty(
				FileProperties.STORE_INTERNAL_IDENTIFIER
				).equals(
					duplicate.getProperty(
						FileProperties.STORE_INTERNAL_IDENTIFIER
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
					FileProperties.STORE_INTERNAL_IDENTIFIER
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
					FileProperties.STORE_INTERNAL_IDENTIFIER
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
		//
		// Create our data info.
		FileProperties properties = new FileProperties() ;
		//
		// Set the type properties.
		configTypeProperties(
			properties,
			FileProperties.MIME_TYPE_XML,
			FileProperties.IVOA_TYPE_VOTABLE
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
					FileProperties.STORE_INTERNAL_IDENTIFIER
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
			FileProperties.MIME_TYPE_XML,
			FileProperties.IVOA_TYPE_VOTABLE
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
				imported.getProperty(
					FileProperties.STORE_INTERNAL_IDENTIFIER
					),
				null
				)
			) ;
		//
		// Modify the duplicate.
		FileProperties modified = new FileProperties(
			target.appendString(
				duplicate.getProperty(
					FileProperties.STORE_INTERNAL_IDENTIFIER
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
					FileProperties.STORE_INTERNAL_IDENTIFIER
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
					FileProperties.STORE_INTERNAL_IDENTIFIER
					)
				)
			) ;
		}
	}
