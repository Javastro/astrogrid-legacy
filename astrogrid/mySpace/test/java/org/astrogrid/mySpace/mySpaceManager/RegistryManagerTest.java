package org.astrogrid.mySpace.mySpaceManager;

import java.io.*;
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

public class RegistryManagerTest extends TestCase
{

/**
 * Standard constructor for JUnit test classes.
 */

   public RegistryManagerTest (String name)
   {  super(name);
   }


/**
 * Test the <code>rewriteRegistryFile</code> method.  
 */

   public void testRewriteRegistryFile()
   {  RegistryManager reg = new RegistryManager("testreg");

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
   {  RegistryManager reg = new RegistryManager("testreg");

      Assert.assertTrue(reg.isServerName("serv1") );
      Assert.assertTrue(!reg.isServerName("burble") );
   }

/**
 * Test the <code>getServerURI</code> method.  
 *
 * <p>
 * Lookup the URI of one of the servers and compare the result to the
 * the expected value.
 */

   public void testGetServerURI()
   {  RegistryManager reg = new RegistryManager("testreg");

      Assert.assertEquals(reg.getServerURI("serv1"),
        "http://www.blue.nowhere.org/s1/");
   }

/**
 * Test the <code>getServerDirectory</code> method.  
 *
 * <p>
 * Lookup the diectory of one of the servers and compare the result to
 * expected value.
 */

   public void testGetserverDirectory()
   {  RegistryManager reg = new RegistryManager("testreg");

      Assert.assertEquals(reg.getServerDirectory("serv2"),
        "/base/direct/s2/");
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
   {  RegistryManager reg = new RegistryManager("testreg");

//
//   Add a DataItemRecord to the registry.

      Date creation = new Date();

      DataItemRecord itemRec = new DataItemRecord(
        "/acd@roe/serv1/fred", 99999, "fred.VOT", "acd@roe",
        creation, creation, 0, 0, "permissions");

      Assert.assertTrue(reg.addDataItemRecord(itemRec) );

      System.out.println("Tested addDataItemRecord...");

//
//   Lookup the details of the new DataItemRecord by identifier.

      DataItemRecord itemRec1 = reg.lookupDataItemRecord(99999);

      Assert.assertEquals(itemRec1.getDataItemID(), 99999);
      Assert.assertEquals(itemRec1.getDataItemFile(), "fred.VOT");
      Assert.assertEquals(itemRec1.getOwnerID(), "acd@roe");

      System.out.println("Tested lookupDataItemRecord...");

//
//   Lookup the details of the new DataItemRecord by name.

      Vector vec = reg.lookupDataItemRecords(
        "/acd@roe/serv1/fred");
      Assert.assertEquals(vec.size(), 1);
 
      if (vec.size() == 1)
      {  DataItemRecord itemRec2 = (DataItemRecord)vec.elementAt(0);

         Assert.assertEquals(itemRec2.getDataItemID(), 99999);
         Assert.assertEquals(itemRec2.getDataItemFile(), "fred.VOT");
         Assert.assertEquals(itemRec2.getOwnerID(), "acd@roe");
      }

//
//   Attempt to lookup the details of a DataItemRecord which is not
//   in the registry.

      Vector vec2 = reg.lookupDataItemRecords("billy");
      Assert.assertEquals(vec2.size(), 0);

      System.out.println("Tested lookupDataItemRecords...");

//
//   Update the DataItemRecord.

      DataItemRecord itemRec3 = new DataItemRecord(
        "/acd@roe/serv1/fred", 99999, "fred.VOT", "acd@roe",
        creation, creation, 0, 0, "different");
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
      if (!status.getSuccessStatus() )
      {  System.out.println("*** Error occurred.");
         status.outputCodes();
      }

      Assert.assertTrue(status.getSuccessStatus() );
      Assert.assertTrue(!status.getWarningStatus() );

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
   {  boolean success = true;

//
//   Write the configuraton file.

      try
      {  File configFile = new File("./testreg.config");
         FileOutputStream fos = new FileOutputStream(configFile);
         PrintWriter pos = new PrintWriter(fos);

         pos.write("\n");
         pos.write("# Config. file for RegistryManager JUnit test.\n\n");
         pos.write("expiryperiod 35\n\n");

         pos.write(
          "server serv1 http://www.blue.nowhere.org/s1/ /base/direct/s1/\n");
         pos.write(
          "server serv2 http://www.blue.nowhere.org/s2/ /base/direct/s2/\n");

         pos.close();
         fos.flush();
         fos.close();
      }
      catch (IOException ioe)
      {  System.out.println("*** Failed to write configuration file.");
         ioe.printStackTrace();
         success = false;
      }

//
//   Create the registry.

      RegistryManager reg = new RegistryManager("testreg", "new");

//
//   Add a couple of entries to the registry.

      Date creation = new Date();

      int seqNo = reg.getNextDataItemID();
      DataItemRecord itemRec1 = new DataItemRecord("/acd@roe/serv1/f1",
        seqNo, "fred1.VOT", "acd@roe", creation, creation, 0, 0,
        "permissions");
      if (!reg.addDataItemRecord(itemRec1) )
      {  success = false;
      }

      seqNo = reg.getNextDataItemID();
      DataItemRecord itemRec2 = new DataItemRecord("/acd@roe/serv1/f2",
        seqNo, "fred1.VOT", "acd@roe", creation, creation, 0, 0,
        "permissions");
      if (!reg.addDataItemRecord(itemRec2) )
      {  success = false;
      }

//
//   (Re)-write the registry.

      reg.rewriteRegistryFile();
      MySpaceStatus status = new MySpaceStatus();
      if (!status.getSuccessStatus() )
      {  success = false;
      }

//
//   Report error if anything is amiss.

      if (!success)
      {  System.out.println("*** Failed to create test registry.");
         status.outputCodes();
      }
   }

/**
 * Method to tidy up after running the tests.  The registry files which
 * were created are deleted.
 */

   protected void tearDown()
   {  boolean configSuccess = true;
      boolean registSuccess = true;

      try
      {  File configFile = new File("./testreg.config");
         configFile.delete();
      }
      catch (Exception e)
      {  e.printStackTrace();
         configSuccess = false;
      }

      try
      {  File registFile = new File("./testreg.reg");
         registFile.delete();
      }
      catch (Exception e)
      {  e.printStackTrace();
         registSuccess = false;
      }

      if (!configSuccess || !registSuccess)
      {  System.out.println("*** Failed to delete registry files.");
      }
   }

// --------------------------------------------------------------------

/**
 * Main method to run the class.
 */

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
}
