package org.astrogrid.mySpace.mySpaceStatus;

import junit.framework.*;

import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatusCode;

/**
 * Junit tests for the <code>MySpaceStatusCode</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 3.
 */

public class MySpaceStatusCodeTest extends TestCase
{

/**
 * Standard constructor for JUnit test classes.
 */

   public MySpaceStatusCodeTest (String name)
   {  super(name);
   }

/**
 * Test the <code>getCode</code> method.  
 */

   public void testGetCode()
   {  MySpaceStatusCode msStatusCode = new MySpaceStatusCode(
        MySpaceStatusCode.AGMMCE00030, MySpaceStatusCode.ERROR,
          "thisclass");
      
      Assert.assertEquals(msStatusCode.getCode(), "AGMMCE00030");
   }

/**
 * Test the <code>getCodeValue</code> method.  
 */

   public void testGetCodeValue()
   {  MySpaceStatusCode msStatusCode = new MySpaceStatusCode(
        MySpaceStatusCode.AGMMCE00030, MySpaceStatusCode.ERROR,
          "thisclass");
      
      Assert.assertEquals(msStatusCode.getCodeValue(),
        MySpaceStatusCode.AGMMCE00030);
   }

/**
 * Test the <code>getCodeMessage</code> method.  
 */

   public void testGetCodeMessage()
   {  MySpaceStatusCode msStatusCode = new MySpaceStatusCode(
        MySpaceStatusCode.AGMMCE00030, MySpaceStatusCode.ERROR,
          "thisclass");
      
      Assert.assertEquals(msStatusCode.getCodeMessage(),
        "(thisclass) Error writing error codes to standard output.");
   }

/**
 * Test the <code>getType</code> method.  
 */

   public void testGetType()
   {  MySpaceStatusCode msStatusCode = new MySpaceStatusCode(
        MySpaceStatusCode.AGMMCE00030, MySpaceStatusCode.ERROR,
          "thisclass");
      
      Assert.assertEquals(msStatusCode.getType(), MySpaceStatusCode.ERROR);
   }

/**
 * Test the <code>toString</code> method.  
 */

   public void testToString()
   {  MySpaceStatusCode msStatusCode = new MySpaceStatusCode(
        MySpaceStatusCode.AGMMCE00101, MySpaceStatusCode.ERROR,
          "thisclass");

      String message = msStatusCode.toString();
      System.out.println(message);
      
      Assert.assertEquals(message,
   "!Error:   [AGMMCE00101]: (thisclass) Failed to write registry data file.");
   }

/**
 * Main method to run the class.
 */

   public static void main (String[] args)
   {
//    junit.textui.TestRunner.run (MySpaceStatusCodeTest.class);
      junit.swingui.TestRunner.run (MySpaceStatusCodeTest.class);
   }
}
