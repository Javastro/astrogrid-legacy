package org.astrogrid.mySpace.mySpaceManager;

import java.io.*;
import java.util.*;
import junit.framework.*;

import org.astrogrid.mySpace.mySpaceManager.Configuration;
import org.astrogrid.mySpace.mySpaceManager.MySpaceActions;
import org.astrogrid.mySpace.mySpaceManager.RegistryManager;
import org.astrogrid.mySpace.mySpaceManager.DataItemRecord;

import org.astrogrid.mySpace.mySpaceStatus.Logger;
import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatus;
import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatusCode;

/**
 * Junit tests for the <code>MySpaceActions</code> class.
 *
 * <p>
 * Note that this test requires the example registry to be present
 * in the test directory.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 3.
 */

public class MySpaceActionsTest extends TestCase
{  private Logger logger = new Logger(false, true, true, "./myspace.log");
   private Configuration config = new Configuration(true, false,
     Configuration.MANAGERONLY);

   private Date creation = new Date();

//   DataItemRecord itemRec = new DataItemRecord("fred", 99999,
//     "fred.VOT", "acd", creation, creation, 0, 0, "permissions");

/**
 * Standard constructor for JUnit test classes.
 */

   public MySpaceActionsTest (String name)
   {  super(name);
   }

/**
 * Test the `Action' methods in the <code>MySpaceActions</code> class.
 * There is a single test method to test all the Action methods because
 * the Actions must be invoked in a specified order.
 *
 * <p>
 * The following Actions are tested:
 *   <code>createContainer</code>,
 *   <code>importDataHolder</code>,
 *   <code>copyDataHolder</code>,
 *   <code>moveDataHolder</code>,
 *   <code>lookupDataHolderDetails</code>,
 *   <code>lookupDataHoldersDetails</code>,
 *   <code>changeOwnerDataHolder</code>,
 *   <code>advanceExpiryDataHolder</code>,
 *   <code>listExpiredDataHolders</code>,
 *   <code>exportDataHolder</code> and
 *   <code>deleteDataHolder</code>.
 */

   public void testActions()
   {

//
//   Create a MySpaceActions object and set the registry to be used.

      MySpaceActions myspace = new MySpaceActions();
      myspace.setRegistryName("testreg");

//
//   Test the <code>createContainer</code> method.

      DataItemRecord dataItem = myspace.createContainer("clq",
        "lei", "credentials", "/clq@lei/serv1/test");

      Assert.assertTrue(dataItem != null);
      if (dataItem != null)
      {  Assert.assertEquals(dataItem.getDataItemName(),
           "/clq@lei/serv1/test");
         Assert.assertEquals(dataItem.getType(), DataItemRecord.CON);
      }

      System.out.println("Tested createContainer...");

//
//   Test the <code>upLoadDataHolder</code> method.

      dataItem = myspace.upLoadDataHolder("clq", "lei", "credentials",
         "/clq@lei/serv1/test/votupl", "any text", "vot",
          myspace.LEAVE);

      Assert.assertTrue(dataItem != null);
      if (dataItem != null)
      {  Assert.assertEquals(dataItem.getDataItemName(),
           "/clq@lei/serv1/test/votupl");
         Assert.assertEquals(dataItem.getType(), DataItemRecord.VOT);
      }

      System.out.println("Tested upLoadDataHolder...");

//
//   Test the <code>importDataHolder</code> method.

      dataItem = myspace.importDataHolder("clq", "lei", "credentials",
         "http://www.cnn.com/", "/clq@lei/serv1/test/vot1", "vot",
         myspace.LEAVE);

      Assert.assertTrue(dataItem != null);
      if (dataItem != null)
      {  Assert.assertEquals(dataItem.getDataItemName(),
           "/clq@lei/serv1/test/vot1");
         Assert.assertEquals(dataItem.getType(), DataItemRecord.VOT);
      }

      System.out.println("Tested importDataHolder...");

//
//   Test the <code>copyDataHolder</code> method.

      int dataItemId = dataItem.getDataItemID();

      dataItem = myspace.copyDataHolder("clq", "lei", "credentials",
        dataItemId, "/clq@lei/serv1/test/vot2");

      Assert.assertTrue(dataItem != null);
      if (dataItem != null)
      {  Assert.assertEquals(dataItem.getDataItemName(),
           "/clq@lei/serv1/test/vot2");
         Assert.assertEquals(dataItem.getType(), DataItemRecord.VOT);
         Assert.assertEquals(dataItem.getOwnerID(), "clq");
         Assert.assertEquals(dataItem.getSize(), 10);
      }

      System.out.println("Tested copyDataHolder...");

//
//   Test the <code>moveDataHolder</code> method.

      dataItem = myspace.moveDataHolder("clq", "lei", "credentials",
        dataItemId, "/clq@lei/serv1/test/vot3");

      Assert.assertTrue(dataItem != null);
      if (dataItem != null)
      {  Assert.assertEquals(dataItem.getDataItemName(),
           "/clq@lei/serv1/test/vot3");
         Assert.assertEquals(dataItem.getType(), DataItemRecord.VOT);
         Assert.assertEquals(dataItem.getOwnerID(), "clq");
         Assert.assertEquals(dataItem.getSize(), 10);
      }

      System.out.println("Tested moveDataHolder...");

//
//   Test the <code>lookupDataHolderDetails</code> method.

      dataItemId = dataItem.getDataItemID();

      dataItem = myspace.lookupDataHolderDetails("clq", "lei",
        "credentials", dataItemId);

      Assert.assertTrue(dataItem != null);
      if (dataItem != null)
      {  Assert.assertEquals(dataItem.getDataItemName(),
           "/clq@lei/serv1/test/vot3");
         Assert.assertEquals(dataItem.getType(), DataItemRecord.VOT);
         Assert.assertEquals(dataItem.getOwnerID(), "clq");
         Assert.assertEquals(dataItem.getSize(), 10);
      }

      System.out.println("Tested lookupDataHolderDetails...");

//
//   Test the <code>lookupDataHoldersDetails</code> method.

      Vector itemsVec = myspace.lookupDataHoldersDetails("clq",
        "lei", "credentials", "/clq@lei/serv1/test*");

      Assert.assertEquals(itemsVec.size(), 4);

      System.out.println("\n");
      for (int loop=0; loop<itemsVec.size(); loop++)
      {  dataItem = (DataItemRecord)itemsVec.elementAt(loop);
         System.out.println("[" + loop + "]: " +
             dataItem.getDataItemName() + "  " +
             dataItem.getDataItemID() + "  " +
             dataItem.getDataItemFile() );
      }

      int dataItemId1 = 0;
      int dataItemId2 = 0;
      int dataItemId3 = 0;
      int dataItemId4 = 0;

      if (itemsVec.size() == 4)
      {  dataItem = (DataItemRecord)itemsVec.elementAt(0);

         Assert.assertEquals(dataItem.getDataItemName(),
           "/clq@lei/serv1/test");
         Assert.assertEquals(dataItem.getType(), DataItemRecord.CON);

         dataItemId1 = dataItem.getDataItemID();

         dataItem = (DataItemRecord)itemsVec.elementAt(1);
         dataItemId2 = dataItem.getDataItemID();

         dataItem = (DataItemRecord)itemsVec.elementAt(2);
         dataItemId3 = dataItem.getDataItemID();

         dataItem = (DataItemRecord)itemsVec.elementAt(3);
         dataItemId4 = dataItem.getDataItemID();
      }

      System.out.println("Tested lookupDataHoldersDetails...");

//
//   Test the <code>changeOwnerDataHolder</code> method.

      dataItemId = dataItem.getDataItemID();
      dataItem = myspace.changeOwnerDataHolder("clq", "lei",
         "credentials", dataItemId, "acd");

      Assert.assertTrue(dataItem != null);
      if (dataItem != null)
      {  Assert.assertEquals(dataItem.getOwnerID(), "acd");
      }

      System.out.println("Tested changeOwnerDataHolder...");

//
//   Test the <code>advanceExpiryDataHolder</code> method.
//   The expiry date is advanced by 17 days.

      int advance = 17;

      dataItemId = dataItem.getDataItemID();

      Date currentExpiryDate = dataItem.getExpiryDate();

      Calendar cal = Calendar.getInstance();
      cal.setTime(currentExpiryDate);
      cal.add(Calendar.DATE, advance);
      Date newExpiryDate = cal.getTime();

      DataItemRecord dataItem4 =
         myspace.advanceExpiryDataHolder("clq", "lei", "credentials", 
         dataItemId, advance);

      Assert.assertTrue(dataItem4 != null);
      if (dataItem != null)
      {  Assert.assertEquals(dataItem4.getExpiryDate(), newExpiryDate);
      }

      System.out.println("Tested advanceExpiryDataHolder...");

//
//   Test the <code>listExpiredDataHolders</code> method.
//   Decrement the expiry date of a dataHolder into the past, then
//   search for expired dataHolders.

      Vector dataItems = myspace.lookupDataHoldersDetails("clq",
        "lei", "credentials", "/clq@lei/serv1/test/vot3");
      dataItem = (DataItemRecord)dataItems.elementAt(0);
      dataItemId = dataItem.getDataItemID();

      advance = -300;

      currentExpiryDate = dataItem.getExpiryDate();

      cal = Calendar.getInstance();
      cal.setTime(currentExpiryDate);
      cal.add(Calendar.DATE, advance);
      newExpiryDate = cal.getTime();

      dataItem4 =
         myspace.advanceExpiryDataHolder("clq", "lei", "credentials", 
         dataItemId, advance);

      Vector exiredItems = myspace.listExpiredDataHolders("clq",
        "lei", "credentials", "/clq@lei/serv1/test/*");

      Assert.assertTrue(exiredItems.size() == 1);
      if (exiredItems.size() > 0)
      {  dataItem = (DataItemRecord)exiredItems.elementAt(0);

//         System.out.println("expired: " + dataItem.getDataItemName());

         Assert.assertEquals(dataItem.getDataItemName(),
           "/clq@lei/serv1/test/vot3");
         Assert.assertEquals(dataItem.getType(), DataItemRecord.VOT);
         Assert.assertEquals(dataItem.getExpiryDate(), newExpiryDate);
      }

      System.out.println("Tested listExpiredDataHolders...");

//
//   Test the <code>exportDataHolder</code> method.

      String exportURI = myspace.exportDataHolder("clq", "lei",
        "credentials", dataItemId3);

      System.out.println("dataItemId3: " + dataItemId3);
      System.out.println("exportURI: " + exportURI);

      Assert.assertEquals(exportURI, "http://www.blue.nowhere.org/s1/f4");

      System.out.println("Tested exportDataHolder...");

//
//   Test the <code>deleteDataHolder</code> method.
//
//   First delete the two VOTables, then the test container.

      Assert.assertTrue(myspace.deleteDataHolder("clq", "lei",
        "credentials", dataItemId4) );
      Assert.assertTrue(myspace.deleteDataHolder("clq", "lei",
        "credentials", dataItemId3) );
      Assert.assertTrue(myspace.deleteDataHolder("clq", "lei",
        "credentials", dataItemId2) );
      Assert.assertTrue(myspace.deleteDataHolder("clq", "lei",
        "credentials", dataItemId1) );

      System.out.println("Tested deleteDataHolder...");

//
//   Attempt to create a user.

      Vector servers = new Vector();
      servers.add("serv1");
      servers.add("serv2");
      servers.add("serv3");

      Assert.assertTrue(myspace.createUser("acd", "roe", "credentials",
        servers) );

      System.out.println("Tested createUser...");

//
//   Attempt to delete a user.

       Assert.assertTrue(myspace.deleteUser("acd", "roe",
         "credentials") );

       System.out.println("Tested deleteUser...");

//
//   Finally check that no errors or warnings have been issued.

      MySpaceStatus status = new MySpaceStatus();
      Assert.assertTrue(status.getSuccessStatus() );
      Assert.assertTrue(!status.getWarningStatus() );

      System.out.println("Tested for spurious bad error status.");

      if (!status.getSuccessStatus())
      {   status.outputCodes();
      }
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
//   Create the user required by the tests.

      boolean success = true;
      Date creation = new Date();

      DataItemRecord initItem = new DataItemRecord("/clq@lei",
        -1, "", "clq@lei", creation, creation, 0, DataItemRecord.CON,
        "permissions");
      initItem = reg.addDataItemRecord(initItem);
      if (initItem == null)
      {  success = false;
      }

      initItem = new DataItemRecord("/clq@lei/serv1", -1,
        "", "clq@lei", creation, creation, 0, DataItemRecord.CON,
        "permissions");
      initItem = reg.addDataItemRecord(initItem);
      if (initItem == null)
      {  success = false;
      }

      if (!success)
      {  System.out.println("*** Failed to create user.");
      }

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
 */

   public static void main (String[] args)
   {
//    junit.textui.TestRunner.run (MySpaceActionsTest.class);
      junit.swingui.TestRunner.run (MySpaceActionsTest.class);
   }
}
