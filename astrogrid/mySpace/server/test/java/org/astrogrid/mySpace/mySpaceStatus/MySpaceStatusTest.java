package org.astrogrid.mySpace.mySpaceStatus;

import junit.framework.*;

import java.util.ArrayList;

import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatus;
import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatusCode;

import org.astrogrid.store.delegate.myspaceItn05.StatusCodes;
import org.astrogrid.store.delegate.myspaceItn05.StatusResults;

/**
 * Junit tests for the <code>MySpaceStatus</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @since Iteration 3.
 * @version Iteration 5.
 */

public class MySpaceStatusTest extends TestCase
{

/**
 * Standard constructor for JUnit test classes.
 */

   public MySpaceStatusTest (String name)
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
        MySpaceStatus(MySpaceStatusCode.AGMMCI00250,
          MySpaceStatusCode.INFO, MySpaceStatusCode.NOLOG,
          this.getClassName() );

      Assert.assertTrue(msStatus.getSuccessStatus() );
      Assert.assertTrue(!msStatus.getWarningStatus() );

      msStatus.reset();
      msStatus.addCode(MySpaceStatusCode.AGMMCW00150,
        MySpaceStatusCode.WARN, MySpaceStatusCode.NOLOG,
        this.getClassName() );

      Assert.assertTrue(msStatus.getSuccessStatus() );
      Assert.assertTrue(msStatus.getWarningStatus() );

      msStatus.reset();
      msStatus.addCode(MySpaceStatusCode.AGMMCE00100,
        MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
        this.getClassName() );

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
        MySpaceStatus(MySpaceStatusCode.AGMMCI00250,
          MySpaceStatusCode.INFO, MySpaceStatusCode.NOLOG,
          this.getClassName() );

      msStatus.addCode(MySpaceStatusCode.AGMMCW00150,
        MySpaceStatusCode.WARN, MySpaceStatusCode.NOLOG,
        this.getClassName() );

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
        MySpaceStatus(MySpaceStatusCode.AGMMCE00030,
          MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
          this.getClassName() );

      msStatus.addCode(MySpaceStatusCode.AGMMCW00150,
        MySpaceStatusCode.WARN, MySpaceStatusCode.NOLOG,
        this.getClassName() );

      msStatus.reset();

      Assert.assertTrue(msStatus.getSuccessStatus() );
      Assert.assertTrue(!msStatus.getWarningStatus() );
   }

/**
 * Test the <code>translateCode</code> method.  A code is translated
 * and compared with the expected String representation.
 */

   public void testTranslateCode()
   {   MySpaceStatus msStatus = new MySpaceStatus();
       String code = msStatus.translateCode(MySpaceStatusCode.AGMMCE00031);
       Assert.assertEquals(code, "AGMMCE00031");
   }

/**
 * Test the <code>getStatusResults</code> method.
 */

  public void testGetStatusResults()
  {  MySpaceStatus msStatus = new
        MySpaceStatus(MySpaceStatusCode.AGMMCI00250,
          MySpaceStatusCode.INFO, MySpaceStatusCode.NOLOG,
          this.getClassName() );

      msStatus.addCode(MySpaceStatusCode.AGMMCW00150,
        MySpaceStatusCode.WARN, MySpaceStatusCode.NOLOG,
        this.getClassName() );

      ArrayList statusList = msStatus.getStatusResults();

      Assert.assertEquals(statusList.size(), 2);

      StatusResults result = (StatusResults)statusList.get(0);

      int severity = result.getSeverity();
//      String message = result.getMessage();
//      System.out.println("severity, message: " + severity + " " +
//        message);

      Assert.assertEquals(severity, StatusCodes.INFO);
  }


/**
 * Obtain the name of the current Java class.
 */

   protected String getClassName()
   { Class currentClass = this.getClass();
     String name =  currentClass.getName();
     int dotPos = name.lastIndexOf(".");
     if (dotPos > -1)
     {  name = name.substring(dotPos+1, name.length() );
     }

     return name;
   }    

/**
 * Main method to run the class.
 */

   public static void main (String[] args)
   {
//    junit.textui.TestRunner.run (MySpaceStatusTest.class);
      junit.swingui.TestRunner.run (MySpaceStatusTest.class);
   }
}
