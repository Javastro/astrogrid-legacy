package org.astrogrid.mySpace.mySpaceManager;

import junit.framework.*;

import org.astrogrid.mySpace.mySpaceStatus.Logger;
import org.astrogrid.mySpace.mySpaceManager.ServerDriver;

/**
 * Junit tests for the <code>ServerDriver</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 3.
 */

public class ServerDriverTest extends TestCase
{  private static Logger logger = new Logger(false, false, false, "");

/**
 * Standard constructor for JUnit test classes.
 */

   public ServerDriverTest (String name)
   {  super(name);
   }

/**
 * There is a single method to test all the ServerDriver methods because
 * the ServerDriver methods import, copy and delete files.  They must
 * be invoked in a specified order so as to create the files which they
 * operate on.  The following methods are tested:
 *
 * <code>upLoadString</code>,
 * <code>importDataHolder</code>,
 * <code>copyDataHolder</code> and
 * <code>deleteDataHolder</code>.
 */

   public void testServerDriver()
   {  ServerDriver server = new ServerDriver();

//
//   Test the <code>upLoadString</code> method.

      boolean result = server.upLoadString(
        "I wandered lonely as a cloud..", "./file1");
      Assert.assertTrue(result);
      System.out.println("Tested upLoadString...");

//
//   Test the <code>importDataHolder</code> method.

      result = server.importDataHolder("http://www.cnn.com/", "./file2");
      Assert.assertTrue(result);
      System.out.println("Tested importDataHolder...");

//
//   Test the <code>copyDataHolder</code> method.

      result = server.copyDataHolder("./file2", "./file3");
      Assert.assertTrue(result);
      System.out.println("Tested copyDataHolder...");

//
//   Test the <code>deleteDataHolder</code> method.  Also delete all
//   the files created during the test.

      result = server.deleteDataHolder("./file1");
      Assert.assertTrue(result);

      result = server.deleteDataHolder("./file2");
      Assert.assertTrue(result);

      result = server.deleteDataHolder("./file3");
      Assert.assertTrue(result);

      System.out.println("Tested deleteDataHolder...");
  }

/**
 * Main method to run the class.
 */

   public static void main (String[] args)
   {
    junit.textui.TestRunner.run (ServerDriverTest.class);
//      junit.swingui.TestRunner.run (ServerDriverTest.class);
   }
}