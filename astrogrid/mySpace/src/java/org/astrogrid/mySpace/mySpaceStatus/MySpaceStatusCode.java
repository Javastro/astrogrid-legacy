package org.astrogrid.mySpace.mySpaceStatus;

/**
 * <p>
 * The <code>MySpaceStatusCode</code> class represents codes from the
 * MySpace system which are intended for eventual translation into
 * messages and then delivery to the User.
 * </p>
 * <p>
 * The <code>MySpaceStatusCode</code> class is intended for use with
 * the <code>MySpaceStatus</code> class, which stores and returns
 * <code>MySpaceStatusCode</code> objects.  Each
 * <code>MySpaceStatusCode</code> object comprises two components: a
 * code and a type.  The code is a string whose value corresponds to
 * some unique event in the MySpace system.  The code is set by the
 * MySpace system and is ultimately intended for translation into a
 * message and delivery to the User.  The type indicates the type of
 * event to which the code refers, coded as follows:
 * </p>
 * <ul>
 *   <li><code>MySpaceMessage.INFO</code> - information (that is,
*     nothing is amiss),</li>
 *   <li><code>MySpaceMessage.WARN</code> - warning,</li>
 *   <li><code>MySpaceMessage.ERROR</code> - error.</li>
 * </ul>
 * <p>
 * The class has a single constructor to which the code and type are
 * passed as arguments.  There are get methods for both the code and
 * type and a <code>toString</code> method to produce a reasonable
 * representation.
 * 
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 2.
 */

public class MySpaceStatusCode
{

//
//Public constants defining the permitted codes for the message type.

   public static final int INFO  = 1;  // Information (ie. nothing amiss).
   public static final int WARN  = 2;  // Warning.
   public static final int ERROR = 3;  // Error.

   private String code;    // Code.
   private int    type;    // Code type code: INFO, WARN or ERROR.

//
// Constructor.

/**
 * Create a <code>MySpaceStatusCode</code>, setting the code and type.
 */

   public MySpaceStatusCode (String code,  int type)
   {  this.code = code;
      this.type = type;
   }

//
// Get methods.
//
// The MySpaceStatusCode class has a get method for every member variable.

/**
 * Return the code associated with the <code>MySpaceStatusCode</code>.
 */

   public String getCode()
   {  return code;
   }

/**
 * Return the type associated with the <code>MySpaceMessage</code>.
 */

   public int getType()
   {  return type;
   }

//
// Other methods.

/**
 * Produce a reasonable string representation of a MySpaceStatusCode.
 */

   public String toString()
   {  String returnString = "";

      if (type == INFO)
      {  returnString = "!Info: code = " + code;
      }
      else if (type == WARN)
      {  returnString = "!Warning: code = " + code;
      }
      else if (type == ERROR)
      {  returnString = "!Error: code = " + code;
      }
      else
      {  returnString = "!Unknown: code = " + code;
      }

      return returnString;
   }
}
