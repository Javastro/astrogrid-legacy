/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/mySpace/server/test/java/org/astrogrid/mySpace/mySpaceManager/FileStoreDriverTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/02 10:25:41 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreDriverTest.java,v $
 *   Revision 1.4  2004/09/02 10:25:41  dave
 *   Updated FileStore and MySpace to handle mime type and file size.
 *   Updated Community deployment script.
 *
 *   Revision 1.3.2.2  2004/09/01 23:41:15  dave
 *   Extended DataItemRecord and database table to contain mime type.
 *
 *   Revision 1.3.2.1  2004/09/01 03:01:48  dave
 *   Updated to pass mime type to filestore.
 *
 *   Revision 1.3  2004/08/27 22:43:15  dave
 *   Updated filestore and myspace to report file size correctly.
 *
 *   Revision 1.2.12.1  2004/08/27 14:06:53  dave
 *   Modified FileStoreDriver and DataItemRecord to propagate size.
 *
 *   Revision 1.2  2004/08/18 19:00:01  dave
 *   Myspace manager modified to use remote filestore.
 *   Tested before checkin - integration tests at 91%.
 *
 *   Revision 1.1.2.2  2004/08/02 14:54:15  dave
 *   Trying to get integration tests to run
 *
 *   Revision 1.1.2.1  2004/07/28 05:01:09  dave
 *   Started adding the FileStore driver .... extremely broke at the moment
 *
 * </cvs:log>
 *
 */
package org.astrogrid.mySpace.mySpaceManager;

import java.net.URL ;
import junit.framework.TestCase;

import org.astrogrid.mySpace.mySpaceStatus.Logger ;
import org.astrogrid.mySpace.mySpaceManager.FileStoreDriver ;

import org.astrogrid.filestore.common.file.FileProperties ;

/**
 * Junit tests for the <code>FileStoreDriverTest</code> class.
 *
 */
public class FileStoreDriverTest
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
	 * Reference to our target driver.
	 *
	 */
	private FileStoreDriver driver ;

	/**
	 * Reference to our target data item.
	 *
	 */
	private DataItemRecord item ;

	/**
	 * Set up our test.
	 *
	 */
	public void setUp()
		throws Exception
		{
		driver = FileStoreDriver.create() ;
		item   = new DataItemRecord() ;
		}

	/**
	 * Test we can create a FileStoreDriver.
	 *
	 */
	public void testCreate()
		throws Exception
		{
		assertNotNull(
			"Failed to create FileStoreDriver",
			driver
			) ;
		}

	/**
	 * Test that we can import a string.
	 *
	 */
	public void testImportString()
		throws Exception
		{
		driver.importString(
			item,
			TEST_STRING
			) ;
		}

	/**
	 * Test that an imported string returns an ivorn.
	 *
	 */
	public void testImportStringIvorn()
		throws Exception
		{
		driver.importString(
			item,
			TEST_STRING
			) ;
		assertNotNull(
			item.getDataItemIvorn()
			) ;
		}

	/**
	 * Test that an imported string returns a URI.
	 *
	 */
	public void testImportStringUri()
		throws Exception
		{
		driver.importString(
			item,
			TEST_STRING
			) ;
		assertNotNull(
			item.getDataItemUri()
			) ;
		}

	/**
	 * Test that an imported string returns a file ID.
	 *
	 */
	public void testImportStringFile()
		throws Exception
		{
		driver.importString(
			item,
			TEST_STRING
			) ;
		assertNotNull(
			item.getDataItemFile()
			) ;
		}

	/**
	 * Test that an imported string returns the right size.
	 *
	 */
	public void testImportStringSize()
		throws Exception
		{
		driver.importString(
			item,
			TEST_STRING
			) ;
		assertEquals(
			(long) TEST_STRING.length(),
			item.getSize()
			) ;
		}

	/**
	 * Test that a '.xml' name returns the right type.
	 *
	 */
	public void testImportNameXml()
		throws Exception
		{
		//
		// Set the item name.
		item.setDataItemName(
			"test.xml"
			) ;
		//
		// Import some data ....
		driver.importString(
			item,
			TEST_STRING
			) ;
		//
		// Check the mime type.
		assertEquals(
			FileProperties.MIME_TYPE_XML,
			item.getDataItemMime()
			) ;
		}

	/**
	 * Test that a '.vot' name returns the right type.
	 *
	 */
	public void testImportNameVot()
		throws Exception
		{
		//
		// Set the item name.
		item.setDataItemName(
			"test.vot"
			) ;
		//
		// Import some data ....
		driver.importString(
			item,
			TEST_STRING
			) ;
		//
		// Check the mime type.
		assertEquals(
			FileProperties.MIME_TYPE_VOTABLE,
			item.getDataItemMime()
			) ;
		}

	/**
	 * Test that a '.vol' name returns the right type.
	 *
	 */
	public void testImportNameVol()
		throws Exception
		{
		//
		// Set the item name.
		item.setDataItemName(
			"test.vol"
			) ;
		//
		// Import some data ....
		driver.importString(
			item,
			TEST_STRING
			) ;
		//
		// Check the mime type.
		assertEquals(
			FileProperties.MIME_TYPE_VOLIST,
			item.getDataItemMime()
			) ;
		}

	/**
	 * Test that a '.xml' name returns the right type.
	 *
	 */
	public void testImportXmlUrl()
		throws Exception
		{
		//
		// Set the item name.
		item.setDataItemName(
			"test.xml"
			) ;
		//
		// Import some data ....
		driver.importUrl(
			item,
			new URL(
				"http://www.astrogrid.org/maven/"
				)
			) ;
		//
		// Check the mime type.
		assertEquals(
			FileProperties.MIME_TYPE_XML,
			item.getDataItemMime()
			) ;
		}
	}
