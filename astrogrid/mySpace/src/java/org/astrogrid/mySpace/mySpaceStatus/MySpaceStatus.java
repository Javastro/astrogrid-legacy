package org.astrogrid.mySpace.mySpaceStatus;

import java.io.*;
import java.util.*;

/**
 * The <code>MySpaceStatus</code> class is used for recording error
 * status information in the MySpace system.
 * 
 * <p>
 * All the member variables in the <code>MySpaceStatus</code> class are
 * declared static, so that they act as global variables.  Thus, there
 * is no need to pass a <code>MySpaceStatus</code> object throughout
 * the MySpace system in order to record the status information.  Rather
 * <code>MySpaceStatus</code> objects can be created locally and used
 * to set global status information.
 * </p>
 * <p>
 * The <code>MySpaceStatus</code> class is used to track whether errors
 * or warnings have occurred and to accumulate a Vector of codes, each
 * of which corresponds to a specific error, warning or informational 
 * event.  (Informational messages are purely to supply information to
 * the User; they do not imply any misadventure.)  To accumulate a code
 * and set the status, simply create a <code>MySpaceStatus</code> object
 * and invoke its <code>addCode</code> method:
 * </p>
 * <p>
 * <code>MySpaceStatus msStatus = new MySpaceStatus();</code>
 * </p>
 * <p>
 * <code>
 *  msStatus.addCode(MySpaceStatusCode.CODE, MySpaceStatusCode.ERROR,
 *    MySpaceStatusCode.NOLOG, invokingClassName);
 * </code>
 * </p>
 * <p>
 * or more conveniently these details can be passed using a constructor:
 * </p>
 * <p>
 * <code>
 *   MySpaceStatus msStatus = new MySpaceStatus(MySpaceStatusCode.CODE,
 *    MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
 *    invokingClassName););
 * </code>
 * </p>
 * <p>
 * In both cases the first argument is the code, and the second is the
 * type of event being described, coded as an <code>int</code> as follows:
 * </p>
 * <ul>
 *   <li><code>MySpaceStatusCode.INFO</code> - information (that is, nothing
 *     is amiss),</li>
 *   <li><code>MySpaceStatusCode.WARN</code> - warning,</li>
 *   <li><code>MySpaceStatusCode.ERROR</code> - error.</li>
 * </ul>
 * <p>
 * The third argument indicates whether the error is to be added to the
 * cumulative AstroGrid error log, as well as reported to the user.  The
 * options are NOLOG and LOG.  As a general rule, only serious errors
 * indicating that something is wrong with the AstroGrid system, should
 * be logged.  The final argument is a String containing the name of
 * the class which has invoked the error system.
 * </p>
 * <p>
 * In addition to setting error and warnings there are also methods to
 * get back the error (or rather success) and warnings flags and to 
 * retrieve the stack of codes (each represented as a
 * <code>MySpaceStatusCode</code>).  Note that warnings are not considered
 * errors and <code>MySpaceStatus</code> will report success if warnings
 * but no errors have been set.  There is also a convenience method to
 * report the codes to standard output.
 * 
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 2.
 */

public class MySpaceStatus
{  private static boolean successStatus = true;   // Running success status.
   private static boolean warningStatus = false;; // Running warning status.

   private static String jobID = ""; // Job identifier.

   private static Vector codes = new Vector();
                   // Log of MySpace info, warning and error codes.

//
// Constructors.

/**
 * Create a <code>MySpaceStatus</code> object.
 */

   public MySpaceStatus ()
   {
   }

/**
 * Create a <code>MySpaceStatus</code> object and set the Job identifier.
 */

   public MySpaceStatus (String jobID)
   {  this.jobID = jobID;
   }


/**
 * Create a <code>MySpaceStatus</code> object and accumulate a code
 * and its associated type (information, warning or error, see above).
 */

   public MySpaceStatus (int code, int type, int logFlag,
     String invokingClass)
   {  this.addCode(code, type, logFlag, invokingClass);
   }

//
// Get methods.

/**
 * Return the global success flag.  A value of true is returned if no
 * errors have been set.
 */

   public boolean getSuccessStatus()
   {  return successStatus;
   }

/**
 * Return the global warnings flag.  A value of true is returned if any
 * warnings have been set.
 */

   public boolean getWarningStatus()
   {  return warningStatus;
   }

/**
 * Return the job identifier.
 */

   public String getJobID()
   {  return jobID;
   }

/**
 * Return a Vector of <code>MySpaceStatusCode</code>s containing any messages
 * that have been set.
 */

   public Vector getCodes()
   {  return codes;
   }

//
// Other methods.

/**
 * Accumulate a new code in the <code>MySpaceStatus</code>.
 */

   public void addCode(int code,  int type, int logFlag,
     String invokingClass)
   {  MySpaceStatusCode newCode = new MySpaceStatusCode(code, type,
       invokingClass);

      codes.add(newCode);

      if (type == MySpaceStatusCode.ERROR)
      {  successStatus = false;
      }
      else if (type == MySpaceStatusCode.WARN)
      {  warningStatus = true;
      }

//
//   Log the error if required.

      if (logFlag == MySpaceStatusCode.LOG)
      {
      }
   }

/**
 * Output any accumulated codes to standard output.
 */

   public void outputCodes()
   {  int numCodes = codes.size();

      try
      {  for (int loop=0; loop<numCodes; loop++)
         {  MySpaceStatusCode currentCode =
              (MySpaceStatusCode)codes.elementAt(loop);

            String code = currentCode.getCode();
            String message = currentCode.getCodeMessage();
            int type = currentCode.getType();

            if (type == MySpaceStatusCode.INFO)
            {  System.out.println(
                 "!(" + loop + ") Info:    [" + code + "]: " + message);
            }
            else if (type == MySpaceStatusCode.WARN)
            {  System.out.println(
                 "!(" + loop + ") Warning: [" + code + "]: " + message);
            }
            else if (type == MySpaceStatusCode.ERROR)
            {  System.out.println(
                 "!(" + loop + ") Error:   [" + code + "]: " + message);
            }
            else
            {  System.out.println(
                 "!(" + loop + ") Unknown: [" + code + "]: " + message);
            }
         }
      }
      catch (Exception all)
      {  this.addCode(MySpaceStatusCode.AGMMCE00030,
           MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
           this.getClassName() );
      }
   }

/**
 * Reset the <code>MySpaceStatus</code>.  The error and warning statuses
 * are reset (to success and no warnings, respectively) and any
 * accumulated codes are lost.
 */

   public void reset()
   {  successStatus = true;
      warningStatus = false;

      codes = new Vector();
   }

/**
 * Translate a MySpaceStatus code into its corresponding string
 * representation.  If an invalid code is given a null string is
 * returned.
 */

   public String translateCode(int code)
   {  MySpaceStatusCode dummyCode = new MySpaceStatusCode(code, 
        MySpaceStatusCode.INFO, "dummy");

      String translatedCode = dummyCode.getCode();

      return translatedCode;
   }

/**
 * Obtain the name of the current Java class.
 */

   protected String getClassName()
   { Class currentClass = this.getClass();
     String name =  currentClass.getName();
     return name;
   }    
}
