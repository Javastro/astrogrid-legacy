/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/mySpace/server/test/java/org/astrogrid/mySpace/mySpaceManager/FileStoreDriverTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/09 01:19:50 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreDriverTest.java,v $
 *   Revision 1.5  2004/09/09 01:19:50  dave
 *   Updated MIME type handling in MySpace.
 *   Extended test coverage for MIME types in FileStore and MySpace.
 *   Added VM memory data to community ServiceStatusData.
 *
 *   Revision 1.4.6.2  2004/09/08 13:58:44  dave
 *   Fixed typo ....
 *
 *   Revision 1.4.6.1  2004/09/08 13:20:24  dave
 *   Updated mime type handling and tests ...
 *
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
	 * Test that importing a string as 'test.xml' name gets the right mime type.
	 *
	 */
	public void testImportStringAsXml()
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
	 * Test that importing a string as 'test.vot' name gets the right mime type.
	 *
	 */
	public void testImportStringAsVot()
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
	 * Test that importing a string as 'test.votable' name gets the right mime type.
	 *
	 */
	public void testImportStringAsVoTable()
		throws Exception
		{
		//
		// Set the item name.
		item.setDataItemName(
			"test.votable"
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
	 * Test that importing a string as 'test.vol' name gets the right mime type.
	 *
	 */
	public void testImportStringAsVol()
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
	 * Test that importing a string as 'test.volist' name gets the right mime type.
	 *
	 */
	public void testImportStringAsVoList()
		throws Exception
		{
		//
		// Set the item name.
		item.setDataItemName(
			"test.volist"
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
	 * Test that importing a string as 'test.work' name gets the right mime type.
	 *
	 */
	public void testImportStringAsWork()
		throws Exception
		{
		//
		// Set the item name.
		item.setDataItemName(
			"test.work"
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
			FileProperties.MIME_TYPE_WORKFLOW,
			item.getDataItemMime()
			) ;
		}

	/**
	 * Test that importing a string as 'test.flow' name gets the right mime type.
	 *
	 */
	public void testImportStringAsFlow()
		throws Exception
		{
		//
		// Set the item name.
		item.setDataItemName(
			"test.flow"
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
			FileProperties.MIME_TYPE_WORKFLOW,
			item.getDataItemMime()
			) ;
		}

	/**
	 * Test that importing a string as 'test.workflow' name gets the right mime type.
	 *
	 */
	public void testImportStringAsWorkFlow()
		throws Exception
		{
		//
		// Set the item name.
		item.setDataItemName(
			"test.workflow"
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
			FileProperties.MIME_TYPE_WORKFLOW,
			item.getDataItemMime()
			) ;
		}

	/**
	 * Test that importing a string as 'test.job' name gets the right mime type.
	 *
	 */
	public void testImportStringAsJob()
		throws Exception
		{
		//
		// Set the item name.
		item.setDataItemName(
			"test.job"
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
			FileProperties.MIME_TYPE_JOB,
			item.getDataItemMime()
			) ;
		}

	/**
	 * Test that importing a string as 'test.adql' name gets the right mime type.
	 *
	 */
	public void testImportStringAsAdql()
		throws Exception
		{
		//
		// Set the item name.
		item.setDataItemName(
			"test.adql"
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
			FileProperties.MIME_TYPE_ADQL,
			item.getDataItemMime()
			) ;
		}

	/**
	 * Test that importing a URL as 'test.xml' name gets the right mime type.
	 *
	 */
	public void testImportUrlAsXml()
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

	/**
	 * Test that importing a URL as 'test.vot' name gets the right mime type.
	 *
	 */
	public void testImportUrlAsVot()
		throws Exception
		{
		//
		// Set the item name.
		item.setDataItemName(
			"test.vot"
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
			FileProperties.MIME_TYPE_VOTABLE,
			item.getDataItemMime()
			) ;
		}

	/**
	 * Test that importing a URL as 'test.votable' name gets the right mime type.
	 *
	 */
	public void testImportUrlAsVoTable()
		throws Exception
		{
		//
		// Set the item name.
		item.setDataItemName(
			"test.votable"
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
			FileProperties.MIME_TYPE_VOTABLE,
			item.getDataItemMime()
			) ;
		}

	/**
	 * Test that importing a URL as 'test.vol' name gets the right mime type.
	 *
	 */
	public void testImportUrlAsVol()
		throws Exception
		{
		//
		// Set the item name.
		item.setDataItemName(
			"test.vol"
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
			FileProperties.MIME_TYPE_VOLIST,
			item.getDataItemMime()
			) ;
		}

	/**
	 * Test that importing a URL as 'test.volist' name gets the right mime type.
	 *
	 */
	public void testImportUrlAsVoList()
		throws Exception
		{
		//
		// Set the item name.
		item.setDataItemName(
			"test.volist"
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
			FileProperties.MIME_TYPE_VOLIST,
			item.getDataItemMime()
			) ;
		}

	/**
	 * Test that importing a URL as 'test.work' name gets the right mime type.
	 *
	 */
	public void testImportUrlAsWork()
		throws Exception
		{
		//
		// Set the item name.
		item.setDataItemName(
			"test.work"
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
			FileProperties.MIME_TYPE_WORKFLOW,
			item.getDataItemMime()
			) ;
		}

	/**
	 * Test that importing a URL as 'test.flow' name gets the right mime type.
	 *
	 */
	public void testImportUrlAsFlow()
		throws Exception
		{
		//
		// Set the item name.
		item.setDataItemName(
			"test.flow"
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
			FileProperties.MIME_TYPE_WORKFLOW,
			item.getDataItemMime()
			) ;
		}

	/**
	 * Test that importing a URL as 'test.workflow' name gets the right mime type.
	 *
	 */
	public void testImportUrlAsWorkFlow()
		throws Exception
		{
		//
		// Set the item name.
		item.setDataItemName(
			"test.workflow"
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
			FileProperties.MIME_TYPE_WORKFLOW,
			item.getDataItemMime()
			) ;
		}

	/**
	 * Test that importing a URL as 'test.job' name gets the right mime type.
	 *
	 */
	public void testImportUrlAsJob()
		throws Exception
		{
		//
		// Set the item name.
		item.setDataItemName(
			"test.job"
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
			FileProperties.MIME_TYPE_JOB,
			item.getDataItemMime()
			) ;
		}

	/**
	 * Test that importing a URL as 'test.adql' name gets the right mime type.
	 *
	 */
	public void testImportUrlAsAdql()
		throws Exception
		{
		//
		// Set the item name.
		item.setDataItemName(
			"test.adql"
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
			FileProperties.MIME_TYPE_ADQL,
			item.getDataItemMime()
			) ;
		}

	/**
	 * Test that setting the mime type works.
	 *
	 */
	public void testImportUrlWithMimeXml()
		throws Exception
		{
		//
		// Set the item name.
		item.setDataItemName(
			"test.adql"
			) ;
		//
		// Set the item type.
		item.setDataItemMime(
			FileProperties.MIME_TYPE_XML
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
