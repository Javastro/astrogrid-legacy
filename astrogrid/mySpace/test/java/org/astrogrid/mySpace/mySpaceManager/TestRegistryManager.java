// package org.astrogrid.mySpace.mySpaceManager;

import java.util.*;
import junit.framework.*;

import org.astrogrid.mySpace.mySpaceManager.RegistryManager;
import org.astrogrid.mySpace.mySpaceManager.DataItemRecord;

import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatus;
import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatusCode;

/**
 * Junit tests for the <code>RegistryManager</code> class.
 * 
 * <p>
 * Note that this test requires the example registry to be present
 * in the test directory.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 3.
 */

public class TestRegistryManager extends TestCase
{  Date creation = new Date();

   DataItemRecord itemRec = new DataItemRecord("fred", 99999,
     "fred.VOT", "acd", creation, creation, 0, 0, "permissions");

/**
 * Standard constructor for JUnit test classes.
 */

   public TestRegistryManager (String name)
   {  super(name);
   }


/**
 * Test the <code>rewriteRegistryFile</code> method.  
 */

   public void testRewriteRegistryFile()
   {  RegistryManager reg = new RegistryManager("example");

//
//   Attempt to rewrite the registry file and then check that it has
//   succeeded by asserting that the status is still ok and no warnings
//   have been issued.

      reg.rewriteRegistryFile();

      MySpaceStatus status = new MySpaceStatus();
      Assert.assertTrue(status.getSuccessStatus() );
      Assert.assertTrue(!status.getWarningStatus() );
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
   {  RegistryManager reg = new RegistryManager("example");

      Assert.assertTrue(reg.isServerName("serv1") );
      Assert.assertTrue(!reg.isServerName("burble") );
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
 */

   public void testAddDeleteAndLookupDataItemRecord()
   {  RegistryManager reg = new RegistryManager("example");

//
//   Add a DataItemRecord to the registry.

      Assert.assertTrue(reg.addDataItemRecord(itemRec) );

      System.out.println("Tested addDataItemRecord...");

//
//   Lookup the details of the new DataItemRecord by identifier.

      DataItemRecord itemRec1 = reg.lookupDataItemRecord(99999);

      Assert.assertEquals(itemRec1.getDataItemID(), 99999);
      Assert.assertEquals(itemRec1.getDataItemFile(), "fred.VOT");
      Assert.assertEquals(itemRec1.getOwnerID(), "acd");

      System.out.println("Tested lookupDataItemRecord...");

//
//   Lookup the details of the new DataItemRecord by name.

      Vector vec = reg.lookupDataItemRecords("fred");
      Assert.assertEquals(vec.size(), 1);
 
      if (vec.size() == 1)
      {  DataItemRecord itemRec2 = (DataItemRecord)vec.elementAt(0);

         Assert.assertEquals(itemRec2.getDataItemID(), 99999);
         Assert.assertEquals(itemRec2.getDataItemFile(), "fred.VOT");
         Assert.assertEquals(itemRec2.getOwnerID(), "acd");
      }

//
//   Attempt to lookup the details of a DataItemRecord which is not
//   in the registry.

      Vector vec2 = reg.lookupDataItemRecords("billy");
      Assert.assertEquals(vec2.size(), 0);

      System.out.println("Tested lookupDataItemRecords...");

//
//   Update the DataItemRecord.

      DataItemRecord itemRec3 = new DataItemRecord("fred", 99999,
        "fred.VOT", "acd", creation, creation, 0, 0, "different");
      Assert.assertTrue(reg.updateDataItemRecord(itemRec3) );

      DataItemRecord itemRec4 = reg.lookupDataItemRecord(99999);
      Assert.assertEquals(itemRec4.getPermissionsMask(), "different");

      System.out.println("Tested updateDataItemRecord...");

//
//   Delete the DataItemRecord from the registry.

      Assert.assertTrue(reg.deleteDataItemRecord(99999) );

      System.out.println("Tested deleteDataItemRecord...");

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
//    junit.textui.TestRunner.run (TestRegistryManager.class);
      junit.swingui.TestRunner.run (TestRegistryManager.class);
   }
}
