package org.astrogrid.mySpace.mySpaceUtil;

import java.io.*;
import junit.framework.*;

import java.lang.reflect.Array;

import org.astrogrid.mySpace.mySpaceStatus.Logger;
import org.astrogrid.mySpace.mySpaceUtil.MySpaceUtils;

/**
 * Junit tests for the <code>MySpaceUtils</code> class.
 *
 * Note that only a couple of the methods are tested.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 3.
 */

public class MySpaceUtilsTest extends TestCase
{  private static Logger logger = new Logger (false, false, false, "");

/**
 * Standard constructor for JUnit test classes.
 */

   public MySpaceUtilsTest (String name)
   {  super(name);
   }

/**
 * Test the <code>writeToFile</code> and <code>readFromFile</code>
 * methods.  The tests for the two methods are combined in a single
 * method so that a file can first be written and then read.
 */

   public void testWriteRead()
   {  MySpaceUtils msUtil = new MySpaceUtils();
      File testFile = new File("./testfile");

//
//   Write a file.

      String contents = "I wandered lonely as a cloud...";
      Assert.assertTrue(msUtil.writeToFile(testFile, contents, false) );
      System.out.println("Tested writeToFile...");

//
//   Read the file back.

      String readback = msUtil.readFromFile(testFile);
      Assert.assertEquals(readback, contents);
      System.out.println("Tested readFromFile: " + readback);

//
//   Tidy up by deleting the test file.

      testFile.delete();
   }


/**
 * Test the <code>writeToBinaryFile</code> and <code>readFromBinaryFile</code>
 * methods.  The tests for the two methods are combined in a single
 * method so that a file can first be written and then read.
 */

   public void testWriteReadBinary()
   {  MySpaceUtils msUtil = new MySpaceUtils();
      File testBinaryFile = new File("./testfilebinary");

//
//   Write a file.

      byte[] contents = new byte[] {1, 2, 3, 4, 5, 6, 7};
      Assert.assertTrue(msUtil.writeToBinaryFile(testBinaryFile,
        contents, false) );
      System.out.println("Tested writeToBinaryFile...");

//
//   Read the file back.

      byte[] readback = msUtil.readFromBinaryFile(testBinaryFile);

      int contentsLength = Array.getLength(contents);
      int readLength = Array.getLength(readback);
      Assert.assertEquals(readLength, contentsLength);

      if (contentsLength == readLength)
      {  for (int loop=0; loop<readLength; loop++)
         {  Assert.assertEquals(readback[loop], contents[loop]);
         }
      }

      System.out.print("Tested readFromBinaryFile: ");
      for (int loop=0; loop<readLength; loop++)
      {  System.out.print(readback[loop] + " ");
      }
      System.out.println("");

//
//   Tidy up by deleting the test file.

      testBinaryFile.delete();
   }


/**
 * Main method to run the class.
 */

   public static void main (String[] args)
   {
//    junit.textui.TestRunner.run (MySpaceUtilsTest.class);
      junit.swingui.TestRunner.run (MySpaceUtilsTest.class);
   }
}
