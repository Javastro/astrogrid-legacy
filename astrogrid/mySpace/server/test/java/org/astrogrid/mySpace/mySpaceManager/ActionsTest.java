package org.astrogrid.mySpace.mySpaceManager;

import java.io.*;
import java.util.*;
import junit.framework.*;

import org.astrogrid.mySpace.mySpaceManager.Configuration;
import org.astrogrid.mySpace.mySpaceManager.Actions;
import org.astrogrid.mySpace.mySpaceManager.RegistryManager;
import org.astrogrid.mySpace.mySpaceManager.DataItemRecord;

import org.astrogrid.store.delegate.myspaceItn05.ManagerCodes;
import org.astrogrid.store.delegate.myspaceItn05.EntryCodes;

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
 * @since Iteration 3.
 * @version Iteration 5.
 */

public class ActionsTest extends TestCase
{  private Logger logger = new Logger(false, false, false, "");
   private Configuration config = new Configuration(true, false,
     Configuration.MANAGERONLY);

   private Date creation = new Date();

/**
 * Standard constructor for JUnit test classes.
 */

   public ActionsTest (String name)
   {  super(name);
   }

/**
 * Test the `Action' methods in the <code>MySpaceActions</code> class.
 * There is a single test method to test all the Action methods because
 * the Actions must be invoked in a specified order.
 *
 * <p>
 * The following Actions are tested:
 *   <code>createAccount</code>,
 *   <code>createContainer</code>,
 *   <code>putContents</code>,
 *   <code>copyFile</code>,
 *   <code>moveFile</code>,
 *   <code>lookupDataHolderDetails</code>,
 *   <code>getEntriesList</code>,
 *   <code>getString</code> and
 *   <code>deleteFile</code>,
 *   <code>deleteAccount</code>.
 */

   public void testActions()
   {
//
//   Create and populate a Vector of servers.  Note that in Iteration
//   5 a MySpace Manager can handle only a single server.

      Vector servers = new Vector();

      ServerDetails server = new ServerDetails("serv1", 23,
        "http://www.blue.nowhere.org/s1/", "/base/direct/s1/");
      servers.add(server);

//
//   Create a registry object.

      RegistryManager reg = new RegistryManager("testreg", servers);

//
//   Create an Actions object and set the registry to be used.

      Actions actions = new Actions();
      actions.setRegistryName("testreg");

//
//   Test the <code>createAccount</code> method.

      actions.createAccount("admin", "clq");

//
//   Test the <code>createContainer</code> method.

      boolean isOk = actions.createContainer("clq", "/clq/newcontainer");
      Assert.assertTrue(isOk);
      System.out.println("Tested createContainer...");

//
//   Test the <code>putContents</code> method.

      String contents = "The Snark was a Boojum, you see.";
      byte[] dummy = new byte[1];
      isOk = actions.putContents("clq", "/clq/newfile", true,
        contents, dummy, EntryCodes.VOT, ManagerCodes.LEAVE);
      Assert.assertTrue(isOk);
      System.out.println("Tested putContents...");

//
//   Test the <code>putUri</code> method.

      isOk = actions.putUri("clq", "/clq/newwebfile",
        "http://www.google.com", EntryCodes.UNKNOWN,
         ManagerCodes.LEAVE);
      Assert.assertTrue(isOk);
      System.out.println("Tested putUri...");

//
//   Test the <code>copyFile</code> method.

      int dataItemId = actions.getId("clq", "/clq/newfile");
      isOk = actions.copyFile("clq", dataItemId,
        "/clq/newcontainer/file");
      Assert.assertTrue(isOk);
      System.out.println("Tested copyFile...");

//
//   Test the <code>moveFile</code> method.

      isOk = actions.moveFile("clq", dataItemId,
        "/clq/newcontainer/file2");
      Assert.assertTrue(isOk);
      System.out.println("Tested moveFile...");

//
//   Test the <code>lookupDataHolderDetails</code> method.

      dataItemId = actions.getId("clq", "/clq/newcontainer/file2");
      DataItemRecord dataItem = actions.lookupDataHolderDetails("clq", 
        dataItemId);
      Assert.assertTrue(dataItem != null);
      if (dataItem != null)
      {  Assert.assertEquals(dataItem.getDataItemName(),
           "/clq/newcontainer/file2");
         Assert.assertEquals(dataItem.getType(), DataItemRecord.VOT);
         Assert.assertEquals(dataItem.getOwnerID(), "clq");
         Assert.assertEquals(dataItem.getSize(), contents.length() );
      }
      System.out.println("Tested lookupDataHolderDetails...");

//
//   Test the <code>getEntriesList</code> method.

      Vector itemsVec = actions.getEntriesList("clq", "/clq/*");
      Assert.assertEquals(itemsVec.size(), 7);

      for (int loop=0; loop<itemsVec.size(); loop++)
      {  dataItem = (DataItemRecord)itemsVec.elementAt(loop);
         System.out.println("[" + loop + "]: " +
             dataItem.getDataItemName() + "  " +
             dataItem.getDataItemID() + "  " +
             dataItem.getDataItemFile() + "  " +
             dataItem.getDataItemUri() );
      }

      if (itemsVec.size() == 7)
      {  dataItem = (DataItemRecord)itemsVec.elementAt(0);
         Assert.assertEquals(dataItem.getDataItemName(),
           "/clq/newcontainer");
         Assert.assertEquals(dataItem.getType(), EntryCodes.CON);

         dataItem = (DataItemRecord)itemsVec.elementAt(1);
         Assert.assertEquals(dataItem.getDataItemName(),
           "/clq/newcontainer/file");
         Assert.assertEquals(dataItem.getDataItemID(), 7);
         Assert.assertEquals(dataItem.getType(), EntryCodes.VOT);
         Assert.assertEquals(dataItem.getDataItemUri(),
           "http://www.blue.nowhere.org/s1/f7");
      }

      System.out.println("Tested getEntriesList...");

//
//   Test the <code>deleteDataHolder</code> method.
//
//   First delete the two files, then the test container.

      dataItemId = actions.getId("clq", "/clq/newwebfile");
      isOk = actions.deleteFile("clq", dataItemId);
      Assert.assertTrue(isOk);

      dataItemId = actions.getId("clq", "/clq/newcontainer/file");
      isOk = actions.deleteFile("clq", dataItemId);
      Assert.assertTrue(isOk);

      dataItemId = actions.getId("clq", "/clq/newcontainer/file2");
      isOk = actions.deleteFile("clq", dataItemId);
      Assert.assertTrue(isOk);

      dataItemId = actions.getId("clq", "/clq/newcontainer");
      isOk = actions.deleteFile("clq", dataItemId);
      Assert.assertTrue(isOk);

      System.out.println("Tested deletFile...");

//
//   Attempt to delete an account.

      isOk = actions.deleteAccount("admin", "clq");
      Assert.assertTrue(isOk);
      System.out.println("Tested deleteAccount...");

//
//   Finally check that no errors or warnings have been issued.

      MySpaceStatus status = new MySpaceStatus();
      Assert.assertTrue(status.getSuccessStatus() );
      Assert.assertTrue(!status.getWarningStatus() );

      System.out.println("Tested for spurious bad error status.");

      if (!status.getSuccessStatus())
      {   status.outputCodes();
      }

//
//   Tidy up by removing the registry.

      boolean propertiesSuccess = true;
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
//    junit.textui.TestRunner.run (ActionsTest.class);
      junit.swingui.TestRunner.run (ActionsTest.class);
   }
}
