// package org.astrogrid.mySpace.mySpaceManager;

import java.util.*;
import junit.framework.*;

import org.astrogrid.mySpace.mySpaceManager.MySpaceActions;
import org.astrogrid.mySpace.mySpaceManager.DataItemRecord;

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

public class TestMySpaceActions extends TestCase
{  Date creation = new Date();

   DataItemRecord itemRec = new DataItemRecord("fred", 99999,
     "fred.VOT", "acd", creation, creation, 0, 0, "permissions");

/**
 * Standard constructor for JUnit test classes.
 */

   public TestMySpaceActions (String name)
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
 *   <code>exportDataHolder</code> and
 *   <code>deleteDataHolder</code>.
 */

   public void testActions()
   {

//
//   Create a MySpaceActions object and set the registry to be used.

      MySpaceActions myspace = new MySpaceActions();
      myspace.setRegistryName("example");

//
//   Test the <code>createContainer</code> method.

      DataItemRecord dataItem = myspace.createContainer("clq",
        "leicester", "j1234", "/clq/serv1/test");

      Assert.assertTrue(dataItem != null);
      if (dataItem != null)
      {  Assert.assertEquals(dataItem.getDataItemName(), "/clq/serv1/test");
         Assert.assertEquals(dataItem.getType(), DataItemRecord.CON);
      }

      System.out.println("Tested createContainer...");

//
//   Test the <code>importDataHolder</code> method.

      dataItem = myspace.importDataHolder("clq", "leicester", "j1234",
         "/clq/serv1/test/vot1", "file", 99);

      Assert.assertTrue(dataItem != null);
      if (dataItem != null)
      {  Assert.assertEquals(dataItem.getDataItemName(),
           "/clq/serv1/test/vot1");
         Assert.assertEquals(dataItem.getType(), DataItemRecord.VOT);
      }

      System.out.println("Tested importDataHolder...");

//
//   Test the <code>copyDataHolder</code> method.

      int dataItemId = dataItem.getDataItemID();

      dataItem = myspace.copyDataHolder("clq", "leicester", "j1234",
        dataItemId, "/clq/serv1/test/vot2");

      Assert.assertTrue(dataItem != null);
      if (dataItem != null)
      {  Assert.assertEquals(dataItem.getDataItemName(),
           "/clq/serv1/test/vot2");
         Assert.assertEquals(dataItem.getType(), DataItemRecord.VOT);
         Assert.assertEquals(dataItem.getOwnerID(), "clq");
         Assert.assertEquals(dataItem.getSize(), 99);
      }

      System.out.println("Tested copyDataHolder...");

//
//   Test the <code>moveDataHolder</code> method.

      dataItem = myspace.moveDataHolder("clq", "leicester", "j1234",
        dataItemId, "/clq/serv1/test/vot3");

      Assert.assertTrue(dataItem != null);
      if (dataItem != null)
      {  Assert.assertEquals(dataItem.getDataItemName(),
           "/clq/serv1/test/vot3");
         Assert.assertEquals(dataItem.getType(), DataItemRecord.VOT);
         Assert.assertEquals(dataItem.getOwnerID(), "clq");
         Assert.assertEquals(dataItem.getSize(), 99);
      }

      System.out.println("Tested moveDataHolder...");

//
//   Test the <code>lookupDataHolderDetails</code> method.

      dataItemId = dataItem.getDataItemID();

      dataItem = myspace.lookupDataHolderDetails("clq", "leicester",
        "j1234", dataItemId);

      Assert.assertTrue(dataItem != null);
      if (dataItem != null)
      {  Assert.assertEquals(dataItem.getDataItemName(),
           "/clq/serv1/test/vot3");
         Assert.assertEquals(dataItem.getType(), DataItemRecord.VOT);
         Assert.assertEquals(dataItem.getOwnerID(), "clq");
         Assert.assertEquals(dataItem.getSize(), 99);
      }

      System.out.println("Tested lookupDataHolderDetails...");

//
//   Test the <code>lookupDataHoldersDetails</code> method.

      Vector itemsVec = myspace.lookupDataHoldersDetails("clq",
        "leicester", "j1234", "/clq/serv1/test*");

      Assert.assertEquals(itemsVec.size(), 3);

      int dataItemId1 = 0;
      int dataItemId2 = 0;
      int dataItemId3 = 0;

      if (itemsVec.size() == 3)
      {  dataItem = (DataItemRecord)itemsVec.elementAt(0);

         Assert.assertEquals(dataItem.getDataItemName(), "/clq/serv1/test");
         Assert.assertEquals(dataItem.getType(), DataItemRecord.CON);

         dataItemId1 = dataItem.getDataItemID();

         dataItem = (DataItemRecord)itemsVec.elementAt(1);
         dataItemId2 = dataItem.getDataItemID();

         dataItem = (DataItemRecord)itemsVec.elementAt(2);
         dataItemId3 = dataItem.getDataItemID();
      }

      System.out.println("Tested lookupDataHoldersDetails...");

//
//   Test the <code>exportDataHolder</code> method.

      String exportURI = myspace.exportDataHolder("clq", "leicester",
        "j1234", dataItemId3);

      Assert.assertEquals(exportURI, "http://www.blue.nowhere.org/s1/file");

//
//   Test the <code>deleteDataHolder</code> method.
//
//   First delete the two VOTables, then the test container.

      Assert.assertTrue(myspace.deleteDataHolder("clq", "leicester",
        "j1234", dataItemId3) );
      Assert.assertTrue(myspace.deleteDataHolder("clq", "leicester",
        "j1234", dataItemId2) );
      Assert.assertTrue(myspace.deleteDataHolder("clq", "leicester",
        "j1234", dataItemId1) );

      System.out.println("Tested exportDataHolder...");

//
//   Finally check that no errors or warnings have been issued.

      MySpaceStatus status = new MySpaceStatus();
      Assert.assertTrue(status.getSuccessStatus() );
      Assert.assertTrue(!status.getWarningStatus() );

      System.out.println("Tested for spurious bad error status.");
   }

/**
 * Main method to run the class.
 */

   public static void main (String[] args)
   {
//    junit.textui.TestRunner.run (TestMySpaceActions.class);
      junit.swingui.TestRunner.run (TestMySpaceActions.class);
   }
}
