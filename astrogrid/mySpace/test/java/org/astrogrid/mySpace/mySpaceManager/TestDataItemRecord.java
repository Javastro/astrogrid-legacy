package org.astrogrid.mySpace.mySpaceManager;

import junit.framework.*;
import java.util.*;

import org.astrogrid.mySpace.mySpaceManager.DataItemRecord;

/**
 * Junit tests for the <code>DataItemRecord</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 2.
 */

public class TestDataItemRecord extends TestCase
{

/**
 * Standard constructor for JUnit test classes.
 */

   public TestDataItemRecord (String name)
   {  super(name);
   }

/**
 * Test the <code>resetDataItemFile</code> method.
 */

   public void testResetDataItemFile()
   {  Date creation = new Date(0);
      DataItemRecord dataItem = new DataItemRecord("name", 0, "file",
        "owner", creation, creation, 0, DataItemRecord.UNKNOWN,
        "permissions");

//
//   Check that the dataItemFile has been set correctly, reset it and
//   check that it is null.

      Assert.assertEquals(dataItem.getDataItemFile(), "file");
      dataItem.resetDataItemFile();
      Assert.assertEquals(dataItem.getDataItemFile(), null);
   }

/**
 * Test the <code>toString</code> method.
 */

   public void testToString()
   {  Date creation = new Date(0);
      DataItemRecord dataItem = new DataItemRecord("name", 0, "file",
        "owner", creation, creation, 0, DataItemRecord.UNKNOWN,
        "permissions");

      Assert.assertEquals(dataItem.toString(),
       "name (unknown, created 01/01/70 01:00)");
   }

/**
 * Main method to run the class.
 */

   public static void main (String[] args)
   {
//    junit.textui.TestRunner.run (TestDataItemRecord.class);
      junit.swingui.TestRunner.run (TestDataItemRecord.class);
   }
}