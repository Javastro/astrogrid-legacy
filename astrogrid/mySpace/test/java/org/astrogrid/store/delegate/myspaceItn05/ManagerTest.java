package org.astrogrid.store.delegate.myspaceItn05;

import junit.framework.*;
import java.util.*;

import java.lang.reflect.Array;

/**
 * Junit tests for the MySpace inner delegate.
 *
 * @author A C Davenhall (Edinburgh)
 * @since Iteration 5.
 * @version Iteration 5.
 */

public class ManagerTest extends TestCase
{  private String mssUrl =
     "http://grendel12.roe.ac.uk:8080/astrogrid-myspace/services/Manager";

/**
 * Standard constructor for JUnit test classes.
 */

   public ManagerTest (String name)
   {  super(name);
   }


//
// Test classes.

   public void testHeartBeat()
   {  try
      {  ManagerService myspace = new ManagerServiceLocator();
         Manager fake = myspace.getAstrogridMyspace(
           new java.net.URL(mssUrl) );

         String results = fake.heartBeat();
         Assert.assertEquals(results, "Adsum.");
      }
      catch(Exception e)
      {  System.out.println("Exception thrown.");
      }
   }

   public void testGetEntriesList()
   {  try
      {ManagerService myspace = new ManagerServiceLocator();
         Manager fake = myspace.getAstrogridMyspace(
           new java.net.URL(mssUrl) );

         String query = "/acd/con1/con2/file*";
         KernelResults results = fake.getEntriesList(query, true);

         Object[] statusList = results.getStatusList();
         int numStatus = Array.getLength(statusList);
         Assert.assertEquals(numStatus, 1);

         StatusResults status = new StatusResults();
         status = (StatusResults)statusList[0];

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "getEntriesList executed successfully.");

//
//      Additional test for the entries which satisfy the query.

         Object[] entries = results.getEntries();
         int numEntries = Array.getLength(entries);
         Assert.assertEquals(numEntries, 1);

         EntryResults entry = new EntryResults();
         entry = (EntryResults)entries[0];

         String entryName = entry.getEntryName();
         Assert.assertEquals(entryName, "/acd/con1/con2/filexyz");
      }
      catch(Exception e)
      {  System.out.println("Exception thrown.");
      }
   }


   public void testPutString()
   {  try
      {  ManagerService myspace = new ManagerServiceLocator();
         Manager fake = myspace.getAstrogridMyspace(
           new java.net.URL(mssUrl) );

         KernelResults results = fake.putString("some name",
           "some contents", EntryCodes.VOT, ManagerCodes.LEAVE, true);

         Object[] statusList = results.getStatusList();
         int numStatus = Array.getLength(statusList);
         Assert.assertEquals(numStatus, 1);

         StatusResults status = new StatusResults();
         status = (StatusResults)statusList[0];

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "putString executed successfully.");
      }
      catch(Exception e)
      {  System.out.println("Exception thrown.");
      }
   }


   public void testPutBytes()
   {  try
      {  ManagerService myspace = new ManagerServiceLocator();
         Manager fake = myspace.getAstrogridMyspace(
           new java.net.URL(mssUrl) );

         byte[] contentsBytes = new byte[] {1, 2, 3, 4, 5};
         KernelResults results = fake.putBytes("some name",
           contentsBytes, EntryCodes.UNKNOWN, ManagerCodes.LEAVE, true);

         Object[] statusList = results.getStatusList();
         int numStatus = Array.getLength(statusList);
         Assert.assertEquals(numStatus, 1);

         StatusResults status = new StatusResults();
         status = (StatusResults)statusList[0];

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "putBytes executed successfully.");
      }
      catch(Exception e)
      {  System.out.println("Exception thrown.");
      }
   }


   public void testPutUri()
   {  try
      {  ManagerService myspace = new ManagerServiceLocator();
         Manager fake = myspace.getAstrogridMyspace(
           new java.net.URL(mssUrl) );

         KernelResults results = fake.putUri("some name",
           "some URI", EntryCodes.VOT, ManagerCodes.LEAVE, true);

         Object[] statusList = results.getStatusList();
         int numStatus = Array.getLength(statusList);
         Assert.assertEquals(numStatus, 1);

         StatusResults status = new StatusResults();
         status = (StatusResults)statusList[0];

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "putUri executed successfully.");
      }
      catch(Exception e)
      {  System.out.println("Exception thrown.");
      }
   }


   public void testGetString()
   {  try
      {  ManagerService myspace = new ManagerServiceLocator();
         Manager fake = myspace.getAstrogridMyspace(
           new java.net.URL(mssUrl) );

         KernelResults results = fake.getString("some name", true);

         Object[] statusList = results.getStatusList();
         int numStatus = Array.getLength(statusList);
         Assert.assertEquals(numStatus, 1);

         StatusResults status = new StatusResults();
         status = (StatusResults)statusList[0];

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "getString executed successfully.");

//
//      Additional test to check the contents String returned.

         String contents = results.getContentsString();
         Assert.assertEquals(contents, "The Snark was a Boojum you see.");
      }
      catch(Exception e)
      {  System.out.println("Exception thrown.");
      }
   }


   public void testGetBytes()
   {  try
      {  ManagerService myspace = new ManagerServiceLocator();
         Manager fake = myspace.getAstrogridMyspace(
           new java.net.URL(mssUrl) );

         KernelResults results = fake.getBytes("some name",  true);

         Object[] statusList = results.getStatusList();
         int numStatus = Array.getLength(statusList);
         Assert.assertEquals(numStatus, 1);

         StatusResults status = new StatusResults();
         status = (StatusResults)statusList[0];

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "getBytes executed successfully.");

//
//      Additional test to check the byte array returned.

         byte[] contents = results.getContentsBytes();
         byte[] expected = new byte[] {1, 2, 3, 4, 5};

         int numBytes = Array.getLength(contents);
         Assert.assertEquals(numBytes, 5);

         for (int loop=0; loop<numBytes; loop++)
         {  Assert.assertEquals(contents[loop], expected[loop]);
         }
      }
      catch(Exception e)
      {  System.out.println("Exception thrown.");
      }
   }


   public void testCreateContainer()
   {  try
      {  ManagerService myspace = new ManagerServiceLocator();
         Manager fake = myspace.getAstrogridMyspace(
           new java.net.URL(mssUrl) );

         KernelResults results = fake.createContainer("some container",
           true);

         Object[] statusList = results.getStatusList();
         int numStatus = Array.getLength(statusList);
         Assert.assertEquals(numStatus, 1);

         StatusResults status = new StatusResults();
         status = (StatusResults)statusList[0];

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "createContainer executed successfully.");
      }
      catch(Exception e)
      {  System.out.println("Exception thrown.");
      }
   }


   public void testCopyFile()
   {  try
      {  ManagerService myspace = new ManagerServiceLocator();
         Manager fake = myspace.getAstrogridMyspace(
           new java.net.URL(mssUrl) );

         KernelResults results = fake.copyFile("some name",
           "some other name", true);

         Object[] statusList = results.getStatusList();
         int numStatus = Array.getLength(statusList);
         Assert.assertEquals(numStatus, 1);

         StatusResults status = new StatusResults();
         status = (StatusResults)statusList[0];

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "copyFile executed successfully.");
      }
      catch(Exception e)
      {  System.out.println("Exception thrown.");
      }
   }


   public void testMoveFile()
   {  try
      {  ManagerService myspace = new ManagerServiceLocator();
         Manager fake = myspace.getAstrogridMyspace(
           new java.net.URL(mssUrl) );

         KernelResults results = fake.moveFile("some name",
           "some other name",  true);

         Object[] statusList = results.getStatusList();
         int numStatus = Array.getLength(statusList);
         Assert.assertEquals(numStatus, 1);

         StatusResults status = new StatusResults();
         status = (StatusResults)statusList[0];

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "moveFile executed successfully.");
      }
      catch(Exception e)
      {  System.out.println("Exception thrown.");
      }
   }


   public void testDeleteFile()
   {  try
      {  ManagerService myspace = new ManagerServiceLocator();
         Manager fake = myspace.getAstrogridMyspace(
           new java.net.URL(mssUrl) );

         KernelResults results = fake.deleteFile("some name", true);

         Object[] statusList = results.getStatusList();
         int numStatus = Array.getLength(statusList);
         Assert.assertEquals(numStatus, 1);

         StatusResults status = new StatusResults();
         status = (StatusResults)statusList[0];

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "deleteFile executed successfully.");
      }
      catch(Exception e)
      {  System.out.println("Exception thrown.");
      }
   }


   public void testExtendLifetime()
   {  try
      {  ManagerService myspace = new ManagerServiceLocator();
         Manager fake = myspace.getAstrogridMyspace(
           new java.net.URL(mssUrl) );

         long newExpiryDate = 0;
         KernelResults results = fake.extendLifetime("some name",
           newExpiryDate, true);

         Object[] statusList = results.getStatusList();
         int numStatus = Array.getLength(statusList);
         Assert.assertEquals(numStatus, 1);

         StatusResults status = new StatusResults();
         status = (StatusResults)statusList[0];

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "extendLifetime executed successfully.");
      }
      catch(Exception e)
      {  System.out.println("Exception thrown.");
      }
   }


   public void testChangeOwner()
   {  try
      {  ManagerService myspace = new ManagerServiceLocator();
         Manager fake = myspace.getAstrogridMyspace(
           new java.net.URL(mssUrl) );

         KernelResults results = fake.changeOwner("some name",
           "some owner", true);

         Object[] statusList = results.getStatusList();
         int numStatus = Array.getLength(statusList);
         Assert.assertEquals(numStatus, 1);

         StatusResults status = new StatusResults();
         status = (StatusResults)statusList[0];

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "changeOwner executed successfully.");
      }
      catch(Exception e)
      {  System.out.println("Exception thrown.");
      }
   }


   public void testCreateAccount()
   {  try
      {  ManagerService myspace = new ManagerServiceLocator();
         Manager fake = myspace.getAstrogridMyspace(
           new java.net.URL(mssUrl) );

         KernelResults results = fake.createAccount("some account", true);

         Object[] statusList = results.getStatusList();
         int numStatus = Array.getLength(statusList);
         Assert.assertEquals(numStatus, 1);

         StatusResults status = new StatusResults();
         status = (StatusResults)statusList[0];

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "createAccount executed successfully.");
      }
      catch(Exception e)
      {  System.out.println("Exception thrown.");
      }
   }


   public void testDeleteAccount()
   {  try
      {  ManagerService myspace = new ManagerServiceLocator();
         Manager fake = myspace.getAstrogridMyspace(
           new java.net.URL(mssUrl) );

         KernelResults results = fake.deleteAccount("some account",  true);

         Object[] statusList = results.getStatusList();
         int numStatus = Array.getLength(statusList);
         Assert.assertEquals(numStatus, 1);

         StatusResults status = new StatusResults();
         status = (StatusResults)statusList[0];

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "deleteAccount executed successfully.");
      }
      catch(Exception e)
      {  System.out.println("Exception thrown.");
      }
   }

// -----------------------------------------------------------------------

/**
 * Main method to run the class.
 */

   public static void main (String[] args)
   {

//
// Note: for some reason this test will only run with the text test
// harness; the GUI one generates spurious errors about being unable to
// find standard classes.

    junit.textui.TestRunner.run (ManagerTest.class);
//      junit.swingui.TestRunner.run (ManagerTest.class);
   }
}
