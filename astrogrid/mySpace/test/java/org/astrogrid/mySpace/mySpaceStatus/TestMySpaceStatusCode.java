// package org.astrogrid.mySpace.mySpaceStatus;

import junit.framework.*;

import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatusCode;

/**
 * Junit tests for the <code>MySpaceStatusCode</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 2.
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
        MySpaceStatusCode.MS_E_REGDWRT, MySpaceStatusCode.ERROR);
      
      Assert.assertEquals(msStatusCode.getCode(), "MS_E_REGDWRT");
   }

/**
 * Test the <code>toString</code> method.  
 */

   public void testToString()
   {  MySpaceStatusCode msStatusCode = new MySpaceStatusCode(
        MySpaceStatusCode.MS_E_REGDWRT, MySpaceStatusCode.ERROR);
      
      Assert.assertEquals(msStatusCode.toString(),
        "!Error:   [MS_E_REGDWRT]: Failed to write registry data file.");
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
