// package org.astrogrid.mySpace.mySpaceStatus;

import junit.framework.*;

import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatusCode;

/**
 * Junit tests for the <code>MySpaceStatusCode</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 3.
 */

public class TestMySpaceStatusCode extends TestCase
{

/**
 * Standard constructor for JUnit test classes.
 */

   public TestMySpaceStatusCode (String name)
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
//    junit.textui.TestRunner.run (TestMySpaceStatusCode.class);
      junit.swingui.TestRunner.run (TestMySpaceStatusCode.class);
   }
}
