package org.astrogrid.mySpace.mySpaceManager;

import java.io.*;
import java.util.*;
import junit.framework.*;

import org.astrogrid.mySpace.mySpaceManager.RegistryManager;
import org.astrogrid.mySpace.mySpaceManager.DataItemRecord;

import org.astrogrid.mySpace.mySpaceStatus.Logger;
import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatus;
import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatusCode;

/**
 * Junit tests for the <code>RegistryManager</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 4.
 */

public class RegistryManagerTest extends TestCase
{  private static Logger logger = new Logger(false, false, false, "");

	/**
	 * Standard constructor for JUnit test classes.
	public RegistryManagerTest (String name)
		{
		super(name);
		}
	 */

	/**
	 * Test the <code>getServerNames</code> method.
	 *
	 * <p>
	 * This method retrieves the list of servers in the MySpace system.
	 * The test is to get the list and check that it contains the expected
	 * entries.
	 */
	public void testGetServerNames()
		{
		RegistryManager reg = new RegistryManager("testreg");
		assertNotNull(reg) ;
		Vector serverNames = reg.getServerNames();
		assertNotNull(
			serverNames
			);
		assertEquals(
			3,
			serverNames.size()
			);
		assertEquals("serv1",
			(String)serverNames.elementAt(0)
			);
//
//   Also check that the status is still ok and no warnings have been 
//   issued.
		MySpaceStatus status = new MySpaceStatus();
		assertTrue(
			status.getSuccessStatus()
			);
		assertFalse(
			status.getWarningStatus()
			);
		}

/**
 * Test the <code>isServerName</code> method.  
 *
 * <p>
 * Check for both a valid and an invalid server name: the example
 * registry does contain a server called "serv1", but not one called
 * "burble".
 */
	public void testIsServerName()
		{
		RegistryManager reg = new RegistryManager("testreg");
		assertNotNull(reg) ;
		assertTrue(
			reg.isServerName("serv1")
			);
		assertFalse(
			reg.isServerName("burble")
			);
		}

/**
 * Test the <code>getServerURI</code> method.  
 *
 * <p>
 * Lookup the URI of one of the servers and compare the result to the
 * the expected value.
 */
	public void testGetServerURI()
		{
		RegistryManager reg = new RegistryManager("testreg");
		assertNotNull(reg) ;
		assertEquals(
			"http://www.blue.nowhere.org/s1/",
			reg.getServerURI("serv1")
			);
		}

	/**
	 * Test the <code>getServerDirectory</code> method.  
	 *
	 * <p>
	 * Lookup the diectory of one of the servers and compare the result to
	 * expected value.
	 */
	public void testGetserverDirectory()
		throws Exception
		{
		RegistryManager reg = new RegistryManager("testreg");
		assertNotNull(reg) ;
		assertNotNull(
			reg.getServerDirectory(
				"serv2"
				)
			) ;
		assertEquals(
			"/base/direct/s2/",
			reg.getServerDirectory(
				"serv2"
				)
			);
		}

	/**
	 * Test the <code>addDataItemRecord</code>,
	 * <code>lookupDataItemRecord</code>, <code>lookupDataItemRecords</code>,
	 * <code>updateDataItemRecord</code> and 
	 * <code>deleteDataItemRecord</code> methods.
	 *
	 * <p>
	 * A <code>DataItemRecord</code> is added to the registry, the
	 * <code>lookupDataItemRecord</code> and
	 * <code>lookupDataItemRecords</code> methods are invoked to lookup
	 * details of this record and finally the <code>DataItemRecord</code> is
	 * deleted.  For the add and delete methods the returned boolean status
	 * code is checked and the lookup methods the returned values are checked.
	 *
	 * <p>
	 * The four target methods are checked in a single test method rather
	 * than separately because they must be invoked in a specified order.
	 *
	 *
	 *
	 */
	public void testAddDeleteAndLookupDataItemRecord()
		{
		//
		// Create a new myspace registry.
		RegistryManager reg = new RegistryManager("testreg");
		assertNotNull(reg) ;
		//
		// Add a DataItemRecord to the registry.
		Date creation = new Date();
		DataItemRecord itemRec = new DataItemRecord(
			"/acd@roe/serv1/fred",
			-1,
			"fred.VOT",
			null,
			null,
			null,
			"acd@roe",
			creation,
			creation,
			0,
			0,
			"permissions"
			);

		DataItemRecord  newItem = reg.addDataItemRecord(itemRec);
		Assert.assertNotNull(newItem);
		int dataItemID = newItem.getDataItemID();
		assertTrue(dataItemID > -1);
		System.out.println("Tested addDataItemRecord...");

		//
		// Lookup the details of the new DataItemRecord by identifier.
		DataItemRecord itemRec1 = reg.lookupDataItemRecord(dataItemID);
		assertEquals(itemRec1.getDataItemID(), dataItemID);
		assertEquals(itemRec1.getOwnerID(), "acd@roe");
		System.out.println("Tested lookupDataItemRecord...");

//
//   Lookup the details of the new DataItemRecord by name.

		Vector vec = reg.lookupDataItemRecords(
			"/acd@roe/serv1/fred"
			);
		assertEquals(
			1,
			vec.size()
			);

		DataItemRecord itemRec2 = (DataItemRecord)vec.elementAt(0);
		assertEquals(
			dataItemID,
			itemRec2.getDataItemID()
			);
		assertEquals(
			"acd@roe",
			itemRec2.getOwnerID()
			);

//
//   Attempt to lookup the details of a DataItemRecord which is not
//   in the registry.

      Vector vec2 = reg.lookupDataItemRecords("billy");

      if (vec2 != null)
      {  assertEquals(vec2.size(), 0);
      }
      else
      {  assertEquals(vec2, null);
      }

      System.out.println("Tested lookupDataItemRecords...");

//
//   Update the DataItemRecord.

		DataItemRecord itemRec3 = new DataItemRecord(
			"/acd@roe/serv1/fred",
			dataItemID,
			"fred.VOT",
			null,
			null,
			null,
			"acd@roe",
			creation,
			creation,
			0,
			0,
			"different"
			);
		assertTrue(reg.updateDataItemRecord(itemRec3));

		DataItemRecord itemRec4 = reg.lookupDataItemRecord(dataItemID);
		assertEquals(
			itemRec4.getPermissionsMask(),
			"different"
			);
		System.out.println("Tested updateDataItemRecord...");

		//
		// Delete the DataItemRecord from the registry.
		assertTrue(
			reg.deleteDataItemRecord(
				dataItemID
				)
			);
		System.out.println("Tested deleteDataItemRecord...");

		//
		// Finally check that no errors or warnings have been issued.
		MySpaceStatus status = new MySpaceStatus();
			if (!status.getSuccessStatus())
				{
				System.out.println("*** Error occurred.");
				status.outputCodes();
				}

		assertTrue(status.getSuccessStatus());
		assertFalse(status.getWarningStatus());

		System.out.println("Tested for spurious bad error status.");
		}

// --------------------------------------------------------------------

/**
 * Set-up method run prior to a tests.
 *
 * <p>
 * Here a the registry to be used in the tests is created.  First its
 * configuration file is written, then a new empty registry is created,
 * a couple of records are written to it and then the registry file is
 * (re)-written.
 */
   protected void setUp()
   {  
//
//   Create and populate a Vector of servers.
      Vector servers = new Vector();

      ServerDetails server = new ServerDetails("serv1", 23,
        "http://www.blue.nowhere.org/s1/", "/base/direct/s1/");
      servers.add(server);

      server = new ServerDetails("serv2", 34,
        "http://www.blue.nowhere.org/s2/", "/base/direct/s2/");
      servers.add(server);

      server = new ServerDetails("serv3", 45,
        "http://www.blue.nowhere.org/s3/", "/base/direct/s3/");
      servers.add(server);

//
//   Create a registry object.
      RegistryManager reg = new RegistryManager("testreg", servers);

//
//   Check that all is ok.

      MySpaceStatus status = new MySpaceStatus();
      if (!status.getSuccessStatus() )
      {  System.out.println("*** Failed to create test registry.");
         status.outputCodes();
      }
   }

/**
 * Method to tidy up after running the tests.  The registry files which
 * were created are deleted.
 */
   protected void tearDown()
   {  boolean propertiesSuccess = true;
      boolean scriptSuccess = true;
      try
      {  File propertiesFile = new File("./testreg.db.properties");
         propertiesFile.delete();
      }
      catch (Exception e)
      {  e.printStackTrace();
         propertiesSuccess = false;
      }

      try
      {  File scriptFile = new File("./testreg.db.script");
         scriptFile.delete();
      }
      catch (Exception e)
      {  e.printStackTrace();
         scriptSuccess = false;
      }

//      System.out.println("propertiesSuccess, scriptSuccess: " +
//        propertiesSuccess + "  " + scriptSuccess);

      if (!propertiesSuccess || !scriptSuccess)
      {  System.out.println("*** Failed to delete registry files.");
      }
   }

// --------------------------------------------------------------------

/**
 * Main method to run the class.

   public static void main (String[] args)
   {  try
      {
//       junit.textui.TestRunner.run (RegistryManagerTest.class);
         junit.swingui.TestRunner.run (RegistryManagerTest.class);
      }
      catch (Exception e)
      {   System.out.println("*** Exception thown during test.");
          e.printStackTrace();
      }
   }
 */

}
