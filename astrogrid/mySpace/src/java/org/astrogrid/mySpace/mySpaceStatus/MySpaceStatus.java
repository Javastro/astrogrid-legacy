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
 *  msStatus.addCode(MySpaceStatusCode.CODE, MySpaceStatusCode.ERROR);
 * </code>
 * </p>
 * <p>
 * or more conveniently these details can be passed using a constructor:
 * </p>
 * <p>
 * <code>
 *   MySpaceStatus msStatus = new MySpaceStatus(MySpaceStatusCode.CODE,
 *    MySpaceStatusCode.ERROR);
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

   public MySpaceStatus (int code, int type)
   {  this.addCode(code, type);
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

   public void addCode(int code,  int type)
   {  MySpaceStatusCode newCode = new MySpaceStatusCode(code, type);

      codes.add(newCode);

      if (type == MySpaceStatusCode.ERROR)
      {  successStatus = false;
      }
      else if (type == MySpaceStatusCode.WARN)
      {  warningStatus = true;
      }
   }

/**
 * Output any accumulated codes to standard output.
 */

   public void outputCodes()
   {  int numCodes = codes.size();

      for (int loop=0; loop<numCodes; loop++)
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
        MySpaceStatusCode.INFO);

      String translatedCode = dummyCode.getCode();

      return translatedCode;
   }
}
