package org.astrogrid.mySpace.mySpaceManager;

import junit.framework.*;
import java.util.*;

import org.astrogrid.mySpace.mySpaceManager.DataItemRecord;

/**
 * Junit tests for the <code>DataItemRecord</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 3.
 */

public class DataItemRecordTest extends TestCase
{

/**
 * Standard constructor for JUnit test classes.
 */

   public DataItemRecordTest (String name)
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
 * Test the <code>translateType</code> method.
 */

   public void testTranslateType()
   {  Assert.assertEquals(DataItemRecord.VOT,
        DataItemRecord.translateType("VOT") );

      Assert.assertEquals(DataItemRecord.WORKFLOW,
        DataItemRecord.translateType("wf") );

      Assert.assertEquals(DataItemRecord.QUERY,
        DataItemRecord.translateType("qUeRy") );

      Assert.assertEquals(DataItemRecord.UNKNOWN,
        DataItemRecord.translateType("burble burble") );
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
//    junit.textui.TestRunner.run (DataItemRecordTest.class);
      junit.swingui.TestRunner.run (DataItemRecordTest.class);
   }
}