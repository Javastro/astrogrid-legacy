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
 *   <li><code>MySpaceStatusCode.INFO</code> - information (that is,
*     nothing is amiss),</li>
 *   <li><code>MySpaceStatusCode.WARN</code> - warning,</li>
 *   <li><code>MySpaceStatusCode.ERROR</code> - error.</li>
 * </ul>
 * <p>
 * The class has a single constructor to which the code and type are
 * passed as arguments.  There are get methods for both the code and
 * type and a <code>toString</code> method to produce a reasonable
 * representation.
 * 
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 3.
 */

public class MySpaceStatusCode
{

//
//Public constants defining the permitted codes for the message type.

   public static final int INFO  = 1;  // Information (ie. nothing amiss).
   public static final int WARN  = 2;  // Warning.
   public static final int ERROR = 3;  // Error.

//
//Public constants defining whether errors are logged.

   public static final int LOG  = 0;    // Log the error.
   public static final int NOLOG  = 1;  // Do not log the error.

   public static final int SUBSYSTEM = 0;
   public static final int AGMMCZ00001 = 1;
   public static final int AGMMCZ00002 = 2;
   public static final int AGMMCZ00003 = 3;
   public static final int AGMMCZ00004 = 4;
   public static final int AGMMCZ00005 = 5;
   public static final int AGMMCE00030 = 6;
   public static final int AGMMCE00031 = 7;
   public static final int AGMMCE00100 = 8;
   public static final int AGMMCE00101 = 9;
   public static final int AGMMCE00102 = 10;
   public static final int AGMMCE00103 = 11;
   public static final int AGMMCE00104 = 12;
   public static final int AGMMCE00105 = 13;
   public static final int AGMMCE00106 = 14;
   public static final int AGMMCW00150 = 15;
   public static final int AGMMCW00151 = 16;
   public static final int AGMMCW00152 = 17;
   public static final int AGMMCW00153 = 18;
   public static final int AGMMCW00154 = 19;
   public static final int AGMMCE00201 = 20;
   public static final int AGMMCE00202 = 21;
   public static final int AGMMCE00203 = 22;
   public static final int AGMMCE00204 = 23;
   public static final int AGMMCE00205 = 24;
   public static final int AGMMCE00206 = 25;
   public static final int AGMMCE00207 = 26;
   public static final int AGMMCE00208 = 27;
   public static final int AGMMCE00209 = 28;
   public static final int AGMMCE00210 = 29;
   public static final int AGMMCE00211 = 30;
   public static final int AGMMCE00212 = 31;
   public static final int AGMMCE00213 = 32;
   public static final int AGMMCE00214 = 33;
   public static final int AGMMCE00215 = 34;
   public static final int AGMMCE00216 = 35;
   public static final int AGMMCE00217 = 36;
   public static final int AGMMCE00218 = 37;
   public static final int AGMMCE00219 = 38;
   public static final int AGMMCE00220 = 39;
   public static final int AGMMCE00221 = 40;
   public static final int AGMMCI00250 = 41;
   public static final int AGMMCE00300 = 42;
   public static final int AGMMCE00301 = 43;
   public static final int AGMSCE01000 = 44;
   public static final int AGMSCE01001 = 45;
   public static final int AGMSCE01002 = 46;
   public static final int AGMSCE01003 = 47;
   public static final int AGMSCE01004 = 48;
   public static final int AGMSCE01005 = 49;
   public static final int AGMSCE01006 = 50;
   public static final int AGMSCE01040 = 51;
   public static final int AGMSCE01041 = 52;
   public static final int AGMSCE01042 = 53;
   public static final int AGMSCE01043 = 54;
   public static final int AGMSCE01044 = 55;
   public static final int AGMSCE01045 = 56;
   public static final int AGMSCE01046 = 57;
   public static final int AGMSCE01047 = 58;
   public static final int AGMSCE01048 = 59;


   private int code;    // Code.
   private int type;    // Code type code: INFO, WARN or ERROR.
   private String invokingClass; // Name of the class invoking the error.

//
// Constructor.

/**
 * Create a <code>MySpaceStatusCode</code>, setting the code and type.
 */

   public MySpaceStatusCode (int code,  int type, String invokingClass)
   {  this.code = code;
      this.type = type;
      this.invokingClass = invokingClass;
   }

//
// Get methods.
//
// The MySpaceStatusCode class has a get method for every member variable.

/**
 * Return the code associated with the <code>MySpaceStatusCode</code>.
 */

   public String getCode()
   {  String codeString = null;

      if (code == MySpaceStatusCode.SUBSYSTEM)
      {  codeString = "SUBSYSTEM";
      }
      else if (code == MySpaceStatusCode.AGMMCZ00001)
      {  codeString = "AGMMCZ00001";
      }
      else if (code == MySpaceStatusCode.AGMMCZ00002)
      {  codeString = "AGMMCZ00002";
      }
      else if (code == MySpaceStatusCode.AGMMCZ00003)
      {  codeString = "AGMMCZ00003";
      }
      else if (code == MySpaceStatusCode.AGMMCZ00004)
      {  codeString = "AGMMCZ00004";
      }
      else if (code == MySpaceStatusCode.AGMMCZ00005)
      {  codeString = "AGMMCZ00005";
      }
      else if (code == MySpaceStatusCode.AGMMCE00030)
      {  codeString = "AGMMCE00030";
      }
      else if (code == MySpaceStatusCode.AGMMCE00031)
      {  codeString = "AGMMCE00031";
      }
      else if (code == MySpaceStatusCode.AGMMCE00100)
      {  codeString = "AGMMCE00100";
      }
      else if (code == MySpaceStatusCode.AGMMCE00101)
      {  codeString = "AGMMCE00101";
      }
      else if (code == MySpaceStatusCode.AGMMCE00102)
      {  codeString = "AGMMCE00102";
      }
      else if (code == MySpaceStatusCode.AGMMCE00103)
      {  codeString = "AGMMCE00103";
      }
      else if (code == MySpaceStatusCode.AGMMCE00104)
      {  codeString = "AGMMCE00104";
      }
      else if (code == MySpaceStatusCode.AGMMCE00105)
      {  codeString = "AGMMCE00105";
      }
      else if (code == MySpaceStatusCode.AGMMCE00106)
      {  codeString = "AGMMCE00106";
      }
      else if (code == MySpaceStatusCode.AGMMCW00150)
      {  codeString = "AGMMCW00150";
      }
      else if (code == MySpaceStatusCode.AGMMCW00151)
      {  codeString = "AGMMCW00151";
      }
      else if (code == MySpaceStatusCode.AGMMCW00152)
      {  codeString = "AGMMCW00152";
      }
      else if (code == MySpaceStatusCode.AGMMCW00153)
      {  codeString = "AGMMCW00153";
      }
      else if (code == MySpaceStatusCode.AGMMCW00154)
      {  codeString = "AGMMCW00154";
      }
      else if (code == MySpaceStatusCode.AGMMCE00201)
      {  codeString = "AGMMCE00201";
      }
      else if (code == MySpaceStatusCode.AGMMCE00202)
      {  codeString = "AGMMCE00202";
      }
      else if (code == MySpaceStatusCode.AGMMCE00203)
      {  codeString = "AGMMCE00203";
      }
      else if (code == MySpaceStatusCode.AGMMCE00204)
      {  codeString = "AGMMCE00204";
      }
      else if (code == MySpaceStatusCode.AGMMCE00205)
      {  codeString = "AGMMCE00205";
      }
      else if (code == MySpaceStatusCode.AGMMCE00206)
      {  codeString = "AGMMCE00206";
      }
      else if (code == MySpaceStatusCode.AGMMCE00207)
      {  codeString = "AGMMCE00207";
      }
      else if (code == MySpaceStatusCode.AGMMCE00208)
      {  codeString = "AGMMCE00208";
      }
      else if (code == MySpaceStatusCode.AGMMCE00209)
      {  codeString = "AGMMCE00209";
      }
      else if (code == MySpaceStatusCode.AGMMCE00210)
      {  codeString = "AGMMCE00210";
      }
      else if (code == MySpaceStatusCode.AGMMCE00211)
      {  codeString = "AGMMCE00211";
      }
      else if (code == MySpaceStatusCode.AGMMCE00212)
      {  codeString = "AGMMCE00212";
      }
      else if (code == MySpaceStatusCode.AGMMCE00213)
      {  codeString = "AGMMCE00213";
      }
      else if (code == MySpaceStatusCode.AGMMCE00214)
      {  codeString = "AGMMCE00214";
      }
      else if (code == MySpaceStatusCode.AGMMCE00215)
      {  codeString = "AGMMCE00215";
      }
      else if (code == MySpaceStatusCode.AGMMCE00216)
      {  codeString = "AGMMCE00216";
      }
      else if (code == MySpaceStatusCode.AGMMCE00217)
      {  codeString = "AGMMCE00217";
      }
      else if (code == MySpaceStatusCode.AGMMCE00218)
      {  codeString = "AGMMCE00218";
      }
      else if (code == MySpaceStatusCode.AGMMCE00219)
      {  codeString = "AGMMCE00219";
      }
      else if (code == MySpaceStatusCode.AGMMCE00220)
      {  codeString = "AGMMCE00220";
      }
      else if (code == MySpaceStatusCode.AGMMCE00221)
      {  codeString = "AGMMCE00221";
      }
      else if (code == MySpaceStatusCode.AGMMCI00250)
      {  codeString = "AGMMCI00250";
      }
      else if (code == MySpaceStatusCode.AGMMCE00300)
      {  codeString = "AGMMCE00300";
      }
      else if (code == MySpaceStatusCode.AGMMCE00301)
      {  codeString = "AGMMCE00301";
      }
      else if (code == MySpaceStatusCode.AGMSCE01000)
      {  codeString = "AGMSCE01000";
      }
      else if (code == MySpaceStatusCode.AGMSCE01001)
      {  codeString = "AGMSCE01001";
      }
      else if (code == MySpaceStatusCode.AGMSCE01002)
      {  codeString = "AGMSCE01002";
      }
      else if (code == MySpaceStatusCode.AGMSCE01003)
      {  codeString = "AGMSCE01003";
      }
      else if (code == MySpaceStatusCode.AGMSCE01004)
      {  codeString = "AGMSCE01004";
      }
      else if (code == MySpaceStatusCode.AGMSCE01005)
      {  codeString = "AGMSCE01005";
      }
      else if (code == MySpaceStatusCode.AGMSCE01006)
      {  codeString = "AGMSCE01006";
      }
      else if (code == MySpaceStatusCode.AGMSCE01040)
      {  codeString = "AGMSCE01040";
      }
      else if (code == MySpaceStatusCode.AGMSCE01041)
      {  codeString = "AGMSCE01041";
      }
      else if (code == MySpaceStatusCode.AGMSCE01042)
      {  codeString = "AGMSCE01042";
      }
      else if (code == MySpaceStatusCode.AGMSCE01043)
      {  codeString = "AGMSCE01043";
      }
      else if (code == MySpaceStatusCode.AGMSCE01044)
      {  codeString = "AGMSCE01044";
      }
      else if (code == MySpaceStatusCode.AGMSCE01045)
      {  codeString = "AGMSCE01045";
      }
      else if (code == MySpaceStatusCode.AGMSCE01046)
      {  codeString = "AGMSCE01046";
      }
      else if (code == MySpaceStatusCode.AGMSCE01047)
      {  codeString = "AGMSCE01047";
      }
      else if (code == MySpaceStatusCode.AGMSCE01048)
      {  codeString = "AGMSCE01048";
      }


      return codeString;
   }

/**
 * Return the integer code value associated with the
 * <code>MySpaceStatusCode</code>.
 */

   public int getCodeValue()
   {  return code;
   }

/**
 * Return the message associated with the <code>MySpaceStatusCode</code>.
 */

   public String getCodeMessage()
   {  String message = null;

      if (code == MySpaceStatusCode.SUBSYSTEM)
      {  message = "MMC";
      }
      else if (code == MySpaceStatusCode.AGMMCZ00001)
      {  message = "AGDTCZ00001:MySpaceManager: Could not read configuration file {0}";
      }
      else if (code == MySpaceStatusCode.AGMMCZ00002)
      {  message = "AGDTCZ00002:MySpaceManager: Not initialised; is configuration file missing?";
      }
      else if (code == MySpaceStatusCode.AGMMCZ00003)
      {  message = "AGDTCZ00003:Message: Message key is null.";
      }
      else if (code == MySpaceStatusCode.AGMMCZ00004)
      {  message = "AGDTCZ00004:Message: Message pattern or inserts are invalid";
      }
      else if (code == MySpaceStatusCode.AGMMCZ00005)
      {  message = "AGDTCZ00005:Message: Message not found in ResourceBundle";
      }
      else if (code == MySpaceStatusCode.AGMMCE00030)
      {  message = "({0}) Error writing error codes to standard output.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00031)
      {  message = "({0})  Class not found.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00100)
      {  message = "({0}) Failed to create registry manager object.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00101)
      {  message = "({0}) Failed to write registry data file.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00102)
      {  message = "({0}) Registry data file is corrupt (contains unrecognised object).";
      }
      else if (code == MySpaceStatusCode.AGMMCE00103)
      {  message = "({0}) Failed to read registry data file.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00104)
      {  message = "({0})  No servers specified in the registry configuration file.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00105)
      {  message = "({0}) Failed to read the registry configuration file.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00106)
      {  message = "({0}) Failed to access item in registry.";
      }
      else if (code == MySpaceStatusCode.AGMMCW00150)
      {  message = "({0}) Invalid expiry period in the registry configuration file.";
      }
      else if (code == MySpaceStatusCode.AGMMCW00151)
      {  message = "({0}) Invalid server details in the registry configuration file.";
      }
      else if (code == MySpaceStatusCode.AGMMCW00152)
      {  message = "({0}) Expiry period not set; default adopted.";
      }
      else if (code == MySpaceStatusCode.AGMMCW00153)
      {  message = "({0}) Registry configuration file contains an illegal expiry period.";
      }
      else if (code == MySpaceStatusCode.AGMMCW00154)
      {  message = "({0}) Registry configuration contains an invalid server entry.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00201)
      {  message = "({0}) Requested Data Holder not found.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00202)
      {  message = "({0}) Failed to create Data Holder.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00203)
      {  message = "({0}) Failed to create Data Holder (registry update failure).";
      }
      else if (code == MySpaceStatusCode.AGMMCE00204)
      {  message = "({0}) Attempt to copy a container instead of a Data Holder.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00205)
      {  message = "({0}) Failed to move Data Holder.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00206)
      {  message = "({0}) Attempt to move a container instead of a Data Holder.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00207)
      {  message = "({0}) Cannot access Data Holder.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00208)
      {  message = "({0}) Item is neither a container nor a Data Holder.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00209)
      {  message = "({0}) Failed to create container.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00210)
      {  message = "({0}) Attempt to delete a container which is not empty.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00211)
      {  message = "({0}) Failed to delete Data Holder.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00212)
      {  message = "({0}) User not permitted to delete this Data Holder or container.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00213)
      {  message = "({0}) User not permitted to write to parent container.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00214)
      {  message = "({0}) The parent container does not exist.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00215)
      {  message = "({0}) Data Holder or container already exists.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00216)
      {  message = "({0}) I/O exception while building response XML.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00217)
      {  message = "({0}) Error look up dataholderS details.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00218)
      {  message = "({0}) Error look up dataholder details.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00219)
      {  message = "({0}) Error Exporting Data Holder.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00220)
      {  message = "({0}) Failed to delete container.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00221)
      {  message = "({0}) Failed to delete user.";
      }
      else if (code == MySpaceStatusCode.AGMMCI00250)
      {  message = "({0}) No Data Holders matched query string.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00300)
      {  message = "({0}) Invalid server name.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00301)
      {  message = "({0}) Illegal attempt to create or modify user or server containers.";
      }
      else if (code == MySpaceStatusCode.AGMSCE01000)
      {  message = "({0}) No file to delete; check file path and try again.";
      }
      else if (code == MySpaceStatusCode.AGMSCE01001)
      {  message = "({0}) No file to save; check file path and try again.";
      }
      else if (code == MySpaceStatusCode.AGMSCE01002)
      {  message = "({0}) Error Reading from file.";
      }
      else if (code == MySpaceStatusCode.AGMSCE01003)
      {  message = "({0}) Error parsing xmlRequest in MySpaceUtils.parseRequest.";
      }
      else if (code == MySpaceStatusCode.AGMSCE01004)
      {  message = "({0}) Error Casting from String to int.";
      }
      else if (code == MySpaceStatusCode.AGMSCE01005)
      {  message = "({0}) Error Calling MySpace ServerManager.";
      }
      else if (code == MySpaceStatusCode.AGMSCE01006)
      {  message = "({0}) Null pointer getting one or more of the attributes from request string in MySpace.";
      }
      else if (code == MySpaceStatusCode.AGMSCE01040)
      {  message = "({0}) Error Saving DataHolder.";
      }
      else if (code == MySpaceStatusCode.AGMSCE01041)
      {  message = "({0}) Error Moving DataHolder.";
      }
      else if (code == MySpaceStatusCode.AGMSCE01042)
      {  message = "({0}) Data Holder has been moved.";
      }
      else if (code == MySpaceStatusCode.AGMSCE01043)
      {  message = "({0}) Error Copying DataHolder.";
      }
      else if (code == MySpaceStatusCode.AGMSCE01044)
      {  message = "({0}) Data Holder has Been Copied. ";
      }
      else if (code == MySpaceStatusCode.AGMSCE01045)
      {  message = "({0}) Cannot delete DataHolder because of security issues.";
      }
      else if (code == MySpaceStatusCode.AGMSCE01046)
      {  message = "({0}) Unkown Error while deleting data holder.";
      }
      else if (code == MySpaceStatusCode.AGMSCE01047)
      {  message = "({0}) File does not exist.";
      }
      else if (code == MySpaceStatusCode.AGMSCE01048)
      {  message = "({0}) I/O exception while building response XML.";
      }


//
//   Translate the token corresponding to the invoking class.

      int start = message.indexOf("{0}");

      if (start > -1)
      {  int stop = start + 2;
         String temp = message.substring(0, start)
           + invokingClass
           + message.substring(stop+1, message.length() );

         message = temp;
      }

      return message;
   }

/**
 * Return the type associated with the <code>MySpaceStatusCode</code>.
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

      String currentCode = this.getCode();
      String currentMessage = this.getCodeMessage();

      if (type == INFO)
      {  returnString = "!Info:    [" + currentCode + "]: " + currentMessage;
      }
      else if (type == WARN)
      {  returnString = "!Warning: [" + currentCode + "]: " + currentMessage;
      }
      else if (type == ERROR)
      {  returnString = "!Error:   [" + currentCode + "]: " + currentMessage;
      }
      else
      {  returnString = "!Unknown: [" + currentCode + "]: " + currentMessage;
      }

      return returnString;
   }
}
