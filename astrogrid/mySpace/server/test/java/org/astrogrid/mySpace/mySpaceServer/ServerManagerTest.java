package org.astrogrid.mySpace.mySpaceServer;

import java.util.*;
import junit.framework.*;

import java.lang.reflect.Array;

import org.astrogrid.mySpace.mySpaceServer.ServerManager;
import org.astrogrid.mySpace.mySpaceStatus.Logger;

/**
 * Junit tests for the <code>ServerManager</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @since Iteration 3.
 * @version Iteration 4.
 */

public class ServerManagerTest extends TestCase
{  private static Logger logger = new Logger(false, false, false, "");

/**
 * Standard constructor for JUnit test classes.
 */

   public ServerManagerTest (String name)
   {  super(name);
   }

/**
 * Test the methods in the <code>ServerManager</code> class.  There is
 * a single test method to test all the <code>ServerManager</code>
 * methods because the methods involve importing, copying and deleting
 * files and hence must be invoked in a specified order in order to
 * work.
 *
 * <p>
 * The following methods are tested:
 *   <code>upLoadString</code>,
 *   <code>importDataHolder</code>,
 *   <code>copyDataHolder</code>,
 *   <code>retrieveString</code>,
 *   <code>retrieveBytes</code>,
 *   <code>deleteDataHolder</code>.
 */

   public void testAllMethods()
   {  String response;

      ServerManager server = new ServerManager();

//
//   Test up-loading a string.

      String stringContents = "Mary had a little lamb.";
      byte[] byteContents = new byte[] {1, 2, 3, 4, 5, 6};

      response = server.upLoadString(true, stringContents, byteContents,
        "testfile1", false);

      System.out.println(" ");
      System.out.println("Up-load String: " + response);
      Assert.assertEquals(response, "SUCCESS Wrote file: testfile1");

//
//   Test up-loading an array of bytes.

      response = server.upLoadString(false, stringContents, byteContents,
        "testfile5", false);

      System.out.println(" ");
      System.out.println("Up-load byte array: " + response);
      Assert.assertEquals(response, "SUCCESS Wrote file: testfile5");

//
//   Test importing a file from a remote URL.  Both local(ish) and
//   remote URLs are tried (well, local to Edinburgh).

      response = server.importDataHolder(
        "http://www.google.com", "testfile2", false);
      System.out.println("Import: " + response);
      Assert.assertEquals(response, "SUCCESS File imported.");

      response = server.importDataHolder(
        "http://www.google.com/", "testfile3", false);
      System.out.println("Import: " + response);
      Assert.assertEquals(response, "SUCCESS File imported.");

//
//   Test attempting to import a bad (ie. non-existent) URL.

      response = server.importDataHolder(
        "http://www.bluenowhere/", "testfilebad", false);
      System.out.println("Deliberate attempt to import from " +
        "non-existent URL: " + response);
      Assert.assertEquals(response, "FAULT AGMSCE01040");

//
//   Test copying a file.

      response = server.copyDataHolder("testfile1", "testfile4");
      System.out.println("Copy: " + response);
      Assert.assertEquals(response, "SUCCESS File copied.");

//
//   Test retrieving a file as a String.

      String stringReadback = server.retrieveString("testfile1");
      Assert.assertEquals(stringContents, stringReadback);

//
//   Test retrieving a file as an array of bytes.

      byte[] byteReadback = server.retrieveBytes("testfile5");

      int contentsLength = Array.getLength(byteContents);
      int readLength = Array.getLength(byteReadback);
      Assert.assertEquals(readLength, contentsLength);

      if (contentsLength == readLength)
      {  for (int loop=0; loop<readLength; loop++)
         {  Assert.assertEquals(byteReadback[loop], byteContents[loop]);
         }
      }

//
//   Test deleting a file; delete all the files which have been created.


      response = server.deleteDataHolder("testfile1");
      System.out.println("Delete: " + response);
      Assert.assertEquals(response, "SUCCESS File deleted.");

      response = server.deleteDataHolder("testfile2");
      System.out.println("Delete: " + response);
      Assert.assertEquals(response, "SUCCESS File deleted.");

      response = server.deleteDataHolder("testfile3");
      System.out.println("Delete: " + response);
      Assert.assertEquals(response, "SUCCESS File deleted.");

      response = server.deleteDataHolder("testfile4");
      System.out.println("Delete: " + response);
      Assert.assertEquals(response, "SUCCESS File deleted.");

      response = server.deleteDataHolder("testfile5");
      System.out.println("Delete: " + response);
      Assert.assertEquals(response, "SUCCESS File deleted.");

//
//   Test attempting to delete a file which does not exist.

      response = server.deleteDataHolder("testfile4");
      System.out.println("Deliberate attempt to delete non-existent file: "
        + response);
      Assert.assertEquals(response, "FAULT AGMSCE01046");
   }

/**
 * Main method to run the class.
 */

   public static void main (String[] args)
   {
    junit.textui.TestRunner.run (ServerManagerTest.class);
//      junit.swingui.TestRunner.run (ServerManagerTest.class);
   }
}
