package org.astrogrid.store.delegate.myspaceItn05;

import junit.framework.*;
import java.io.*;
import java.util.*;
import java.net.URL;

import java.lang.reflect.Array;

import org.astrogrid.community.User;

import org.astrogrid.store.Agsl;

/**
 * Junit tests for a deployed MySpace Manager.
 *
 * @author A C Davenhall (Edinburgh)
 * @since Iteration 5.
 * @version Iteration 5.
 */

public class DeployedManagerTest extends TestCase
{  

/**
 * Standard constructor for JUnit test classes.
 */

   public DeployedManagerTest (String name)
   {  super(name);
   }


//
// Test class.
//
// This JUnit test has a single test class, which performs a sequence
// of operations on the remote Manager.


   public void testDeployedManager()
   {  try
      {
//
//      Create the contents which will subsequently be imported as a
//      file.  Note that the contents deliberately includes XML-like
//      tags and new-line characters.

         StringBuffer contentsBuffer = new StringBuffer("<outer>\n");

         int rows = 2000;

         for (int loop=0; loop<rows; loop++)
         {  String thisRow = "  <tag>some content " + loop + "</tag>\n";
            contentsBuffer.append(thisRow);
         }

         contentsBuffer.append("</outer>\n");

         String contents = contentsBuffer.toString();
//         System.out.println(contents);

//
//      Create a delegate to access the remote Manager.
//
//      [TODO]: The Manager end-point should be obtained from a properties
//      file rather than hard-wired.

         String endPoint =
   "http://grendel12.roe.ac.uk:8080/astrogrid-mySpace/services/Manager";

         User operator = new User("someuser", "group", "token");

         MySpaceIt05Delegate middle = new MySpaceIt05Delegate(
           operator, endPoint);

//
//      Configure the delegate to (i) not generate test responses and
//      (ii) not to throw exceptions if errors are returned.

         middle.setTest(false);
         middle.setThrow(false);

//
//      Create an account.

         User testAccount = new User("testxyz", "", "", "");

         middle.createUser(testAccount);
         System.out.println("Account created...");

//
//      Import a file into this account.

         middle.putString(contents, "/testxyz/file1", false);
         System.out.println("File imported...");

//
//      Copy this file.

         Agsl someAgsl = new Agsl("http://blue.nowhere.org",
           "/testxyz/file2");

         middle.copy("/testxyz/file1", someAgsl);
         System.out.println("File copied...");

//
//      Move this file.

         someAgsl = new Agsl("http://blue.nowhere.org",
           "/testxyz/file3");

         middle.move("/testxyz/file1", someAgsl);
         System.out.println("File moved...");

//
//      Export the contents of the moved file.

         String retrievedContents = middle.getString("/testxyz/file2");
         Assert.assertEquals(contents, retrievedContents);
         System.out.println("File exported...");

//
//      Import and export the file as an array of bytes rather than
//      a String.

         byte[] byteContents = contents.getBytes();
         int lengthByteContents = Array.getLength(byteContents);

         middle.putBytes(byteContents, 0, lengthByteContents,
           "/testxyz/file4", false);
         System.out.println("File imported as bytes...");

         byte[] retrievedBytes = middle.getBytes("/testxyz/file4");
         int lengthRetrievedBytes  = Array.getLength(retrievedBytes);
         Assert.assertEquals(lengthByteContents, lengthRetrievedBytes);

         String restoredContents = new String(retrievedBytes);
         Assert.assertEquals(contents, restoredContents);
         System.out.println("File exported as bytes...");

//
//      Import and export the file as an array of bytes using the
//      streaming methods.

         OutputStream outStream = middle.putStream("/testxyz/file5",
           true);
         outStream.write(byteContents);
         outStream.close();
         System.out.println("File imported as bytes using streaming...");

         InputStream inStream = middle.getStream("/testxyz/file5");

         int current;
         int retrievedStreamSize = 0;
         StringBuffer buffer = new StringBuffer();
         byte[] byteBuffer = new byte[1];

         while( (current = inStream.read())  !=  -1)
         {  byteBuffer[0] = (byte)current;
            String currentChar = new String(byteBuffer);
            buffer.append(currentChar);
            retrievedStreamSize = retrievedStreamSize + 1;
         }

         Assert.assertEquals(lengthByteContents, retrievedStreamSize);

         String streamedContents = buffer.toString();
         Assert.assertEquals(contents, streamedContents);
         System.out.println("File exported as bytes using streaming...");

//
//      Delete the files.

         middle.delete("/testxyz/file2");
         middle.delete("/testxyz/file3");
         middle.delete("/testxyz/file4");
         middle.delete("/testxyz/file5");
         System.out.println("Files deleted...");

//
//      Delete the test account.
//
//      Note that the account can only be deleted if the attempts to
//      delete the files above succeeded.  Thus the assertions here are
//      also checking that the file deletions succeeded.

         middle.resetStatusList();
         middle.deleteUser(testAccount);
         System.out.println("Account deleted...");

         ArrayList statusList = middle.getStatusList();
         int numMessages = statusList.size();
         Assert.assertEquals(numMessages, 1);

         if (numMessages > 0)
         {  StatusMessage message =
                 (StatusMessage)statusList.get(0);
            int severity = message.getSeverity();
            Assert.assertEquals(message.getSeverity(), StatusCodes.INFO);
         }
      }
      catch(Exception e)
      {  System.out.println("Exception thrown.");
         e.printStackTrace();
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

    junit.textui.TestRunner.run (DeployedManagerTest.class);
//      junit.swingui.TestRunner.run (DeployedManagerTest.class);
   }
}
