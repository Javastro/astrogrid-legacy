package org.astrogrid.store.delegate.myspaceItn05;

import junit.framework.*;
import java.util.*;
import java.net.URL;

import java.lang.reflect.Array;

import org.astrogrid.community.User;

import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.StoreFile;
import org.astrogrid.store.delegate.StoreClient;


/**
 * Junit tests for the MySpace middle delegate.
 *
 * @author A C Davenhall (Edinburgh)
 * @since Iteration 5.
 * @version Iteration 5.
 */

public class MySpaceIt05DelegateTest extends TestCase
{  private String mssUrl =
     "http://grendel12.roe.ac.uk:8080/astrogrid-mySpace/services/Manager";

//
//Create a specified User.

   private  User operator = new User("someuser", "group", "token");


/**
 * Standard constructor for JUnit test classes.
 */

   public MySpaceIt05DelegateTest (String name)
   {  super(name);
   }


//
// Test classes.
//
// First test the heartBeat method (if this does not work then none
// of the others will).


   public void testHeartBeat() throws Exception
   {  try
      {  MySpaceIt05Delegate middle = new MySpaceIt05Delegate(
           operator, mssUrl);

         Assert.assertTrue(middle.heartBeat() );
      }
      catch(Exception e)
      {  System.out.println("Exception thrown in testHeartBeat.");
          throw e;
      }
   }

// -----------------------------------------------------------------------

//
// Test the methods in the StoreClient Interface.

   public void testGetOperator() throws Exception
   {  try
      {  MySpaceIt05Delegate middle = new MySpaceIt05Delegate(
           operator, mssUrl);

         User returnOperator = middle.getOperator();
         Assert.assertEquals(returnOperator, operator);
      }
      catch(Exception e)
      {  System.out.println("Exception thrown in testGetOperator.");
      throw e;
      }
   }

   public void testGetEndpoint() throws Exception
   {  try
      {  MySpaceIt05Delegate middle = new MySpaceIt05Delegate(
           operator, mssUrl);

         Agsl returnAgsl = middle.getEndpoint();
         URL url = returnAgsl.resolveURL();
         String returnEndPoint = url.toString();
         System.out.println("\nEnd point:\n  " + returnEndPoint);
         Assert.assertEquals(returnEndPoint, mssUrl);
      }
      catch(Exception e)
      {  System.out.println("Exception thrown in testGetEndpoint.");
      throw e;
      }
   }

   public void testGetFiles()
   {
       fail("Unwritten test");
   }

   public void testListFiles() throws Exception
   {  try
      {  MySpaceIt05Delegate middle = new MySpaceIt05Delegate(
           operator, mssUrl);

         middle.setTest(true);
         middle.setThrow(true);

         StoreFile[] files = middle.getFiles("/acd/con1/file*").listFiles();

         ArrayList statusList = middle.getStatusList();
         int numStatus = statusList.size();
         Assert.assertEquals(numStatus, 1);

         StatusMessage status = (StatusMessage)statusList.get(0);

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "getEntriesList executed successfully.");

         middle.resetStatusList();

//
//      Additional test for the entries which satisfy the query.

         int numFiles = Array.getLength(files);
         Assert.assertEquals(numFiles, 1);

         String entryName = files[0].getPath();
         Assert.assertEquals(entryName, "/acd/con1/filexyz");
      }
      catch(Exception e)
      {  System.out.println("Exception thrown in testListFiles.");
      throw e;
      }
   }

   public void testGetFile() throws Exception
   {  try
      {  MySpaceIt05Delegate middle = new MySpaceIt05Delegate(
           operator, mssUrl);

         middle.setTest(true);
         middle.setThrow(true);

         StoreFile file = middle.getFile("/acd/con1/fileabc");

         ArrayList statusList = middle.getStatusList();
         int numStatus = statusList.size();
         Assert.assertEquals(numStatus, 1);

         StatusMessage status = (StatusMessage)statusList.get(0);

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "getEntriesList executed successfully.");

         middle.resetStatusList();

//
//      Additional test for the entry returned.

         String entryName = file.getName();
         Assert.assertEquals(entryName, "/acd/con1/fileabc");
      }
      catch(Exception e)
      {  System.out.println("Exception thrown in testGetFile.");
      throw e;
      }
   }

   public void testPutBytes() throws Exception
   {  try
      {  MySpaceIt05Delegate middle = new MySpaceIt05Delegate(
           operator, mssUrl);

         middle.setTest(true);
         middle.setThrow(true);

         byte[] bytes = new byte[] {1, 2, 3, 4, 5};
         middle.putBytes(bytes, 0, 5, "somename", false);

         ArrayList statusList = middle.getStatusList();
         int numStatus = statusList.size();
         Assert.assertEquals(numStatus, 1);

         StatusMessage status = (StatusMessage)statusList.get(0);

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "putBytes executed successfully.");

         middle.resetStatusList();
      }
      catch(Exception e)
      {  System.out.println("Exception thrown in testPutBytes.");
      throw e;
      }
   }

   public void testPutString() throws Exception
   {  try
      {  MySpaceIt05Delegate middle = new MySpaceIt05Delegate(
           operator, mssUrl);

         middle.setTest(true);
         middle.setThrow(true);

         middle.putString("some string", "somename", false);

         ArrayList statusList = middle.getStatusList();
         int numStatus = statusList.size();
         Assert.assertEquals(numStatus, 1);

         StatusMessage status = (StatusMessage)statusList.get(0);

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "putString executed successfully.");

         middle.resetStatusList();
      }
      catch(Exception e)
      {  System.out.println("Exception thrown in testPutString.");
      throw e;
      }
   }

   public void testPutUrl() throws Exception
   {  try
      {  MySpaceIt05Delegate middle = new MySpaceIt05Delegate(
           operator, mssUrl);

         middle.setTest(true);
         middle.setThrow(true);

         URL url = new URL("http://www.google.com");
         middle.putUrl(url, "somename", false);

         ArrayList statusList = middle.getStatusList();
         int numStatus = statusList.size();
         Assert.assertEquals(numStatus, 1);

         StatusMessage status = (StatusMessage)statusList.get(0);

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "putUri executed successfully.");

         middle.resetStatusList();
      }
      catch(Exception e)
      {  System.out.println("Exception thrown in testPutUrl.");
      throw e;
      }
   }

   public void testPutStream()
   {
       fail("Fix me!");
   }

   public void testGetStream()
   {
       fail("I'm not finished!");
   }

   public void testGetUrl() throws Exception
   {  try
      {  MySpaceIt05Delegate middle = new MySpaceIt05Delegate(
           operator, mssUrl);

         middle.setTest(true);
         middle.setThrow(true);

         URL url = middle.getUrl("/acd/con1/fileabc");

         ArrayList statusList = middle.getStatusList();
         int numStatus = statusList.size();
         Assert.assertEquals(numStatus, 1);

         StatusMessage status = (StatusMessage)statusList.get(0);

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "getEntriesList executed successfully.");

         middle.resetStatusList();

//
//      Additional test for the URL returned.

         Assert.assertEquals(url.toString(), "http://blue.nowhere.org/f1");

      }
      catch(Exception e)
      {  System.out.println("Exception thrown in testGetUrl.");
      throw e;
      }
   }

   public void testDelete() throws Exception
   {  try
      {  MySpaceIt05Delegate middle = new MySpaceIt05Delegate(
           operator, mssUrl);

         middle.setTest(true);
         middle.setThrow(true);

         middle.delete("somename");

         ArrayList statusList = middle.getStatusList();
         int numStatus = statusList.size();
         Assert.assertEquals(numStatus, 1);

         StatusMessage status = (StatusMessage)statusList.get(0);

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "deleteFile executed successfully.");

         middle.resetStatusList();
      }
      catch(Exception e)
      {  System.out.println("Exception thrown in testDelete.");
          throw e;
      }
   }

//
//First copy: String to Agsl.

   public void testCopy() throws Exception
   {  try
      {  MySpaceIt05Delegate middle = new MySpaceIt05Delegate(
           operator, mssUrl);

         middle.setTest(true);
         middle.setThrow(true);

         URL url = new URL("http://www.google.com");
         Agsl someAgsl = new Agsl(url);
         middle.copy("somename", someAgsl);

         ArrayList statusList = middle.getStatusList();
         int numStatus = statusList.size();
         Assert.assertEquals(numStatus, 1);

         StatusMessage status = (StatusMessage)statusList.get(0);

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "copyFile executed successfully.");

         middle.resetStatusList();
      }
      catch(Exception e)
      {  System.out.println("Exception thrown in testCopy.");
      throw e;
      }
   }

//
//Second copy, Agsl to String.

   public void testCopy2() throws Exception
   {  try
      {  MySpaceIt05Delegate middle = new MySpaceIt05Delegate(
           operator, mssUrl);

         middle.setTest(true);
         middle.setThrow(true);

         URL url = new URL("http://www.google.com");
         Agsl someAgsl = new Agsl(url);
         middle.copy(someAgsl, "somename");

         ArrayList statusList = middle.getStatusList();
         int numStatus = statusList.size();
         Assert.assertEquals(numStatus, 1);

         StatusMessage status = (StatusMessage)statusList.get(0);

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "copyFile executed successfully.");

         middle.resetStatusList();
      }
      catch(Exception e)
      {  System.out.println("Exception thrown in testCopy.");
      throw e;
      }
   }

//
//First move, String to Agsl.

   public void testMove() throws Exception
   {  try
      {  MySpaceIt05Delegate middle = new MySpaceIt05Delegate(
           operator, mssUrl);

         middle.setTest(true);
         middle.setThrow(true);

         URL url = new URL("http://www.google.com");
         Agsl someAgsl = new Agsl(url);
         middle.move("somename", someAgsl);

         ArrayList statusList = middle.getStatusList();
         int numStatus = statusList.size();
         Assert.assertEquals(numStatus, 1);

         StatusMessage status = (StatusMessage)statusList.get(0);

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "moveFile executed successfully.");

         middle.resetStatusList();
      }
      catch(Exception e)
      {  System.out.println("Exception thrown in testMove.");
      throw e;
      }
   }

//
//Second move, Agsl to String.

   public void testMove2() throws Exception
   {  try
      {  MySpaceIt05Delegate middle = new MySpaceIt05Delegate(
           operator, mssUrl);

         middle.setTest(true);
         middle.setThrow(true);

         URL url = new URL("http://www.google.com");
         Agsl someAgsl = new Agsl(url);
         middle.move(someAgsl, "somename");

         ArrayList statusList = middle.getStatusList();
         int numStatus = statusList.size();
         Assert.assertEquals(numStatus, 1);

         StatusMessage status = (StatusMessage)statusList.get(0);

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "moveFile executed successfully.");

         middle.resetStatusList();
      }
      catch(Exception e)
      {  System.out.println("Exception thrown in testMove.");
      throw e;
      }
   }

   public void testNewFolder() throws Exception
   {  try
      {  MySpaceIt05Delegate middle = new MySpaceIt05Delegate(
           operator, mssUrl);

         middle.setTest(true);
         middle.setThrow(true);

         middle.newFolder("somecontainer");

         ArrayList statusList = middle.getStatusList();
         int numStatus = statusList.size();
         Assert.assertEquals(numStatus, 1);

         StatusMessage status = (StatusMessage)statusList.get(0);

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "createContainer executed successfully.");

         middle.resetStatusList();
      }
      catch(Exception e)
      {  System.out.println("Exception thrown testNewFolder.");
      throw e;
      }
   }

// -----------------------------------------------------------------------

//
// Test the methods in the StoreAdminClient Interface.

   public void testCreateUser() throws Exception
   {  try
      {  MySpaceIt05Delegate middle = new MySpaceIt05Delegate(
           operator, mssUrl);

         middle.setTest(true);
         middle.setThrow(true);

         User account = new User();
         middle.createUser(account);

         ArrayList statusList = middle.getStatusList();
         int numStatus = statusList.size();
         Assert.assertEquals(numStatus, 1);

         StatusMessage status = (StatusMessage)statusList.get(0);

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "createAccount executed successfully.");

         middle.resetStatusList();
      }
      catch(Exception e)
      {  System.out.println("Exception thrown in testCreateUser.");
      throw e;
      }
   }

   public void testDeleteUser() throws Exception
   {  try
      {  MySpaceIt05Delegate middle = new MySpaceIt05Delegate(
           operator, mssUrl);

         middle.setTest(true);
         middle.setThrow(true);

         User account = new User();
         middle.deleteUser(account);

         ArrayList statusList = middle.getStatusList();
         int numStatus = statusList.size();
         Assert.assertEquals(numStatus, 1);

         StatusMessage status = (StatusMessage)statusList.get(0);

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "deleteAccount executed successfully.");

         middle.resetStatusList();
      }
      catch(Exception e)
      {  System.out.println("Exception thrown in testDeleteUser.");
      throw e;
      }
   }

// -----------------------------------------------------------------------

//
// Test additional methods which are in neither the StorClient nor the
// StoreAdminClient interface.  Note that the heartBeat method has
// already been tested at the very beginning.

   public void testExtendLifetime() throws Exception
   {  try
      {  MySpaceIt05Delegate middle = new MySpaceIt05Delegate(
           operator, mssUrl);

         middle.setTest(true);
         middle.setThrow(true);

         Date newExpiryDate = new Date(0);
         middle.extendLifetime("somename", newExpiryDate);

         ArrayList statusList = middle.getStatusList();
         int numStatus = statusList.size();
         Assert.assertEquals(numStatus, 1);

         StatusMessage status = (StatusMessage)statusList.get(0);

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "extendLifetime executed successfully.");

         middle.resetStatusList();
      }
      catch(Exception e)
      {  System.out.println("Exception thrown in testExtendLifetime.");
      throw e;
      }
   }

   public void testChangeOwner() throws Exception
   {  try
      {  MySpaceIt05Delegate middle = new MySpaceIt05Delegate(
           operator, mssUrl);

         middle.setTest(true);
         middle.setThrow(true);

         User newOwner = new User();
         middle.changeOwner("somename", newOwner);

         ArrayList statusList = middle.getStatusList();
         int numStatus = statusList.size();
         Assert.assertEquals(numStatus, 1);

         StatusMessage status = (StatusMessage)statusList.get(0);

         int severity = status.getSeverity();
         Assert.assertEquals(severity, StatusCodes.INFO);

         String message = status.getMessage();
         Assert.assertEquals(message,
           "changeOwner executed successfully.");

         middle.resetStatusList();
      }
      catch(Exception e)
      {  System.out.println("Exception thrown in testChangeOwner.");
      throw e;
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

    junit.textui.TestRunner.run (MySpaceIt05DelegateTest.class);
//      junit.swingui.TestRunner.run (MySpaceIt05DelegateTest.class);
   }
}
