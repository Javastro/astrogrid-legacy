package org.astrogrid.store.delegate.myspaceItn05;

import junit.framework.*;
import java.util.*;

import org.astrogrid.store.delegate.myspaceItn05.ManagerFake;
import java.lang.reflect.Array;

/**
 * Junit tests for the <code>ManagerFake</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @since Iteration 5.
 * @version Iteration 5.
 */

public class ManagerFakeTest extends TestCase
{

/**
 * Standard constructor for JUnit test classes.
 */

   public ManagerFakeTest (String name)
   {  super(name);
   }


//
// Test classes.

   public void testHeartBeat()
   {  ManagerFake fake = new ManagerFake();

      try
      {  String results = fake.heartBeat();
         Assert.assertEquals(results, "Adsum.");
      }
      catch(java.rmi.RemoteException e)
      {  System.out.println("Remote exception thrown.");
      }
   }

   public void testGetEntriesList()
   {  ManagerFake fake = new ManagerFake();

      try
      {  String query = "/acd/con1/con2/file*";
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
      catch(java.rmi.RemoteException e)
      {  System.out.println("Remote exception thrown.");
      }
   }


   public void testPutString()
   {  ManagerFake fake = new ManagerFake();

      try
      {  KernelResults results = fake.putString("some name",
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
      catch(java.rmi.RemoteException e)
      {  System.out.println("Remote exception thrown.");
      }
   }


   public void testPutBytes()
   {  ManagerFake fake = new ManagerFake();

      try
      {  byte[] contentsBytes = new byte[] {1, 2, 3, 4, 5};
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
      catch(java.rmi.RemoteException e)
      {  System.out.println("Remote exception thrown.");
      }
   }


   public void testPutUri()
   {  ManagerFake fake = new ManagerFake();

      try
      {  KernelResults results = fake.putUri("some name",
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
      catch(java.rmi.RemoteException e)
      {  System.out.println("Remote exception thrown.");
      }
   }


   public void testGetString()
   {  ManagerFake fake = new ManagerFake();

      try
      {  KernelResults results = fake.getString("some name", true);

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
      catch(java.rmi.RemoteException e)
      {  System.out.println("Remote exception thrown.");
      }
   }


   public void testGetBytes()
   {  ManagerFake fake = new ManagerFake();

      try
      {  KernelResults results = fake.getBytes("some name",  true);

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
      catch(java.rmi.RemoteException e)
      {  System.out.println("Remote exception thrown.");
      }
   }


   public void testCreateContainer()
   {  ManagerFake fake = new ManagerFake();

      try
      {  KernelResults results = fake.createContainer("some container",
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
      catch(java.rmi.RemoteException e)
      {  System.out.println("Remote exception thrown.");
      }
   }


   public void testCopyFile()
   {  ManagerFake fake = new ManagerFake();

      try
      {  KernelResults results = fake.copyFile("some name",
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
      catch(java.rmi.RemoteException e)
      {  System.out.println("Remote exception thrown.");
      }
   }


   public void testMoveFile()
   {  ManagerFake fake = new ManagerFake();

      try
      {  KernelResults results = fake.moveFile("some name",
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
      catch(java.rmi.RemoteException e)
      {  System.out.println("Remote exception thrown.");
      }
   }


   public void testDeleteFile()
   {  ManagerFake fake = new ManagerFake();

      try
      {  KernelResults results = fake.deleteFile("some name", true);

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
      catch(java.rmi.RemoteException e)
      {  System.out.println("Remote exception thrown.");
      }
   }


   public void testExtendLifetime()
   {  ManagerFake fake = new ManagerFake();

      try
      {  long newExpiryDate = 0;
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
      catch(java.rmi.RemoteException e)
      {  System.out.println("Remote exception thrown.");
      }
   }


   public void testChangeOwner()
   {  ManagerFake fake = new ManagerFake();

      try
      {  KernelResults results = fake.changeOwner("some name",
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
      catch(java.rmi.RemoteException e)
      {  System.out.println("Remote exception thrown.");
      }
   }


   public void testCreateAccount()
   {  ManagerFake fake = new ManagerFake();

      try
      {  KernelResults results = fake.createAccount("some account", true);

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
      catch(java.rmi.RemoteException e)
      {  System.out.println("Remote exception thrown.");
      }
   }


   public void testDeleteAccount()
   {  ManagerFake fake = new ManagerFake();

      try
      {  KernelResults results = fake.deleteAccount("some account",  true);

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
      catch(java.rmi.RemoteException e)
      {  System.out.println("Remote exception thrown.");
      }
   }

// -----------------------------------------------------------------------

/**
 * Main method to run the class.
 */

   public static void main (String[] args)
   {
//    junit.textui.TestRunner.run (ManagerFakeTest.class);
      junit.swingui.TestRunner.run (ManagerFakeTest.class);
   }
}
