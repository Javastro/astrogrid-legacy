// package org.astrogrid.mySpace.mySpaceStatus;

import junit.framework.*;

import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatus;
import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatusCode;

/**
 * Junit tests for the <code>MySpaceStatus</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 2.
 */

public class TestMySpaceStatus extends TestCase
{

/**
 * Standard constructor for JUnit test classes.
 */

   public TestMySpaceStatus (String name)
   {  super(name);
   }

/**
 * Test the <code>addCode</code> method.  An information, warning and
 * error conditition are set and assertions made about the success and
 * warning flags after each.  Note that the <code>reset</code> method
 * is also invoked and must be working correctly for the test to succeed.  
 */

   public void testAddCode()
   {  MySpaceStatus msStatus = new
        MySpaceStatus(MySpaceStatusCode.MS_I_NDHMTCH,
          MySpaceStatusCode.INFO);

      Assert.assertTrue(msStatus.getSuccessStatus() );
      Assert.assertTrue(!msStatus.getWarningStatus() );

      msStatus.reset();
      msStatus.addCode(MySpaceStatusCode.MS_W_RGCSRVI,
       MySpaceStatusCode.WARN);

      Assert.assertTrue(msStatus.getSuccessStatus() );
      Assert.assertTrue(msStatus.getWarningStatus() );

      msStatus.reset();
      msStatus.addCode(MySpaceStatusCode.MS_E_REGDWRT,
       MySpaceStatusCode.ERROR);

      Assert.assertTrue(!msStatus.getSuccessStatus() );
      Assert.assertTrue(!msStatus.getWarningStatus() );
   }

/**
 * Test the <code>outputCodes</code> method.  A couple of statuses
 * are set and then output.  Note that the test will fail if output
 * to standard output fails.
 */

   public void testOutputCodes()
   {  MySpaceStatus msStatus = new
        MySpaceStatus(MySpaceStatusCode.MS_I_NDHMTCH,
          MySpaceStatusCode.INFO);

      msStatus.addCode(MySpaceStatusCode.MS_W_RGCSRVI,
       MySpaceStatusCode.WARN);

      msStatus.outputCodes();

      Assert.assertTrue(!msStatus.getSuccessStatus() );
   }

/**
 * Test the <code>reset</code> method.  An error and a warning message
 * are set, the <code>reset</code> method is invoked and then checks are
 * made that the success and warning flags have returned to their
 * initial values.  
 */

   public void testReset()
   {  MySpaceStatus msStatus = new
        MySpaceStatus(MySpaceStatusCode.MS_E_REGDWRT,
          MySpaceStatusCode.ERROR);

      msStatus.addCode(MySpaceStatusCode.MS_W_RGCSRVI,
        MySpaceStatusCode.WARN);

      msStatus.reset();

      Assert.assertTrue(msStatus.getSuccessStatus() );
      Assert.assertTrue(!msStatus.getWarningStatus() );
   }

/**
 * Main method to run the class.
 */

   public static void main (String[] args)
   {
//    junit.textui.TestRunner.run (TestMySpaceStatus.class);
      junit.swingui.TestRunner.run (TestMySpaceStatus.class);
   }
}
