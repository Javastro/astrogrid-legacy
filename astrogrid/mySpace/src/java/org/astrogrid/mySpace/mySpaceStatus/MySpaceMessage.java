package org.astrogrid.mySpace.mySpaceStatus;

/**
 * The <code>MySpaceMessage</code> class holds messages from the MySpace
 * system which are intended for eventual delivery to the User.
 * 
 * <p>
 * The <code>MySpaceMessage</code> class is intended for use with the
 * <code>MySpaceStatus</code> class, which stores and returns
 * <code>MySpaceMessage</code> objects.  Each <code>MySpaceMessage</code>
 * object comprises two components: a message and a type.  The message
 * is a human-readable string set by the MySpace system and ultimately
 * intended for delivery to the User.  The type indicates the type of
 * event to which the message refers, coded as follows:
 * </p>
 * <ul>
 *   <li><code>"i"</code> - information (that is, nothing is amiss),</li>
 *   <li><code>"w"</code> - warning,</li>
 *   <li><code>"e"</code> - error.</li>
 * </ul>
 * <p>
 * The class has a single constructor to which the message and type are
 * passed as arguments.  There are get methods for both the message and
 * type and a <code>toString</code> method to produce a reasonable
 * representation.
 * 
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 2.
 */

public class MySpaceMessage
{  private String message;     // Message type.
   private String type;        // Type of message: "i", "w" or "e".

//
// Constructor.

/**
 * Create a <code>MySpaceMessage</code>, setting the message and type.
 */

   public MySpaceMessage (String message,  String type)
   {  this.message = message;
      this.type = type;
   }

//
// Get methods.
//
// The MySpaceMessage class has a get method for every member variable.

/**
 * Return the message string associated with the <code>MySpaceMessage</code>.
 */

   public String getMessage()
   {  return message;
   }

/**
 * Return the type associated with the <code>MySpaceMessage</code>.
 */

   public String getType()
   {  return type;
   }

//
// Other methods.

/**
 * Produce a reasonable string representation of a MySpaceMessage.
 */

   public String toString()
   {  String returnString = "";

      if (type.equals("i"))
      {  returnString = "!Info: " + message;
      }
      else if (type.equals("w"))
      {  returnString = "!Warning: " + message;
      }
      else if (type.equals("e")) 
      {  returnString = "!Error: " + message;
      }
      else
      {  returnString = "!Unknown: " + message;
      }

      return returnString;
   }
}
