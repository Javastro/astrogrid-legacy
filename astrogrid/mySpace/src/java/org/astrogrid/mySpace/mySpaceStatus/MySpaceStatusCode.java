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
 * @version Iteration 2.
 */

public class MySpaceStatusCode
{

//
//Public constants defining the permitted codes for the message type.

   public static final int INFO  = 1;  // Information (ie. nothing amiss).
   public static final int WARN  = 2;  // Warning.
   public static final int ERROR = 3;  // Error.

   public static final int REGISTRYCONF = 0;
   public static final int TEMPPATHTO = 1;
   public static final int MYSPACEMANAGER = 2;
   public static final int SERVERMANAGER = 3;
   public static final int AGMMCE00030 = 4;
   public static final int AGMMCE00031 = 5;
   public static final int AGMMCE00032 = 6;
   public static final int AGMMCE00033 = 7;
   public static final int AGMMCE00034 = 8;
   public static final int AGMMCE00035 = 9;
   public static final int AGMMCE00036 = 10;
   public static final int AGMMCE00037 = 11;
   public static final int AGMMCE00038 = 12;
   public static final int AGMMCE00039 = 13;
   public static final int AGMMCE00040 = 14;
   public static final int AGMMCE00041 = 15;
   public static final int AGMMCE00042 = 16;
   public static final int AGMMCE00043 = 17;
   public static final int AGMMCE00044 = 18;
   public static final int AGMMCE00045 = 19;
   public static final int AGMMCE00046 = 20;
   public static final int AGMMCE00047 = 21;
   public static final int AGMMCE00048 = 22;
   public static final int AGMMCE00049 = 45;
   public static final int AGMMCE00052 = 23;
   public static final int AGMMCE00053 = 24;
   public static final int AGMMCW00032 = 25;
   public static final int AGMMCE00050 = 26;
   public static final int AGMMCW00031 = 27;
   public static final int AGMMCE00060 = 28;
   
   public static final int AGMSCE00030 = 29;
   public static final int AGMSCE00031 = 30;
   public static final int AGMSCE00032 = 31;
   public static final int AGMSCE00033 = 32;
   public static final int AGMSCE00034 = 33;
   public static final int AGMSCE00035 = 34;
   public static final int AGMSCE00036 = 35;
   public static final int AGMSCE00040 = 36;
   public static final int AGMSCE00041 = 37;
   public static final int AGMSCE00042 = 38;
   public static final int AGMSCE00043 = 39;
   public static final int AGMSCE00044 = 40;
   public static final int AGMSCE00045 = 41;
   public static final int AGMSCE00046 = 42;
   public static final int AGMSCE00047 = 43;
   public static final int AGMSCE00048 = 44;
  
   public static final int AGMMCE00054 = 46;
   public static final int AGMMCE00055 = 47;
   public static final int AGMMCE00056 = 48;


   private int code;    // Code.
   private int type;    // Code type code: INFO, WARN or ERROR.

//
// Constructor.

/**
 * Create a <code>MySpaceStatusCode</code>, setting the code and type.
 */

   public MySpaceStatusCode (int code,  int type)
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
   {  String codeString = null;

      if (code == MySpaceStatusCode.REGISTRYCONF)
      {  codeString = "REGISTRYCONF";
      }
      else if (code == MySpaceStatusCode.TEMPPATHTO)
      {  codeString = "TEMPPATHTO";
      }
      else if (code == MySpaceStatusCode.MYSPACEMANAGER)
      {  codeString = "MYSPACEMANAGER";
      }
      else if (code == MySpaceStatusCode.SERVERMANAGER)
      {  codeString = "SERVERMANAGER";
      }
      else if (code == MySpaceStatusCode.AGMMCE00030)
      {  codeString = "AGMMCE00030";
      }
      else if (code == MySpaceStatusCode.AGMMCE00031)
      {  codeString = "AGMMCE00031";
      }
      else if (code == MySpaceStatusCode.AGMMCE00032)
      {  codeString = "AGMMCE00032";
      }
      else if (code == MySpaceStatusCode.AGMMCE00033)
      {  codeString = "AGMMCE00033";
      }
      else if (code == MySpaceStatusCode.AGMMCE00034)
      {  codeString = "AGMMCE00034";
      }
      else if (code == MySpaceStatusCode.AGMMCE00035)
      {  codeString = "AGMMCE00035";
      }
      else if (code == MySpaceStatusCode.AGMMCE00036)
      {  codeString = "AGMMCE00036";
      }
      else if (code == MySpaceStatusCode.AGMMCE00037)
      {  codeString = "AGMMCE00037";
      }
      else if (code == MySpaceStatusCode.AGMMCE00038)
      {  codeString = "AGMMCE00038";
      }
      else if (code == MySpaceStatusCode.AGMMCE00039)
      {  codeString = "AGMMCE00039";
      }
      else if (code == MySpaceStatusCode.AGMMCE00040)
      {  codeString = "AGMMCE00040";
      }
      else if (code == MySpaceStatusCode.AGMMCE00041)
      {  codeString = "AGMMCE00041";
      }
      else if (code == MySpaceStatusCode.AGMMCE00042)
      {  codeString = "AGMMCE00042";
      }
      else if (code == MySpaceStatusCode.AGMMCE00043)
      {  codeString = "AGMMCE00043";
      }
      else if (code == MySpaceStatusCode.AGMMCE00044)
      {  codeString = "AGMMCE00044";
      }
      else if (code == MySpaceStatusCode.AGMMCE00045)
      {  codeString = "AGMMCE00045";
      }
      else if (code == MySpaceStatusCode.AGMMCE00046)
      {  codeString = "AGMMCE00046";
      }
      else if (code == MySpaceStatusCode.AGMMCE00047)
      {  codeString = "AGMMCE00047";
      }
      else if (code == MySpaceStatusCode.AGMMCE00048)
      {  codeString = "AGMMCE00048";
      }
      else if (code == MySpaceStatusCode.AGMMCE00052)
      {  codeString = "AGMMCE00052";
      }
      else if (code == MySpaceStatusCode.AGMMCE00053)
      {  codeString = "AGMMCE00053";
      }
      else if (code == MySpaceStatusCode.AGMMCW00032)
      {  codeString = "AGMMCW00032";
      }
      else if (code == MySpaceStatusCode.AGMMCE00050)
      {  codeString = "AGMMCE00140";
      }
      else if (code == MySpaceStatusCode.AGMMCW00031)
      {  codeString = "AGMMCW00031";
      }
      else if (code == MySpaceStatusCode.AGMMCE00060)
      {  codeString = "AGMMCE00060";
      }
      else if (code == MySpaceStatusCode.AGMSCE00030)
      {  codeString = "AGMSCE00030";
      }
      else if (code == MySpaceStatusCode.AGMSCE00031)
      {  codeString = "AGMSCE00031";
      }
      else if (code == MySpaceStatusCode.AGMSCE00032)
      {  codeString = "AGMSCE00032";
      }
      else if (code == MySpaceStatusCode.AGMSCE00033)
      {  codeString = "AGMSCE00033";
      }
      else if (code == MySpaceStatusCode.AGMSCE00034)
      {  codeString = "AGMSCE00034";
      }
      else if (code == MySpaceStatusCode.AGMSCE00035)
      {  codeString = "AGMSCE00035";
      }
      else if (code == MySpaceStatusCode.AGMSCE00036)
      {  codeString = "AGMSCE00036";
      }
      else if (code == MySpaceStatusCode.AGMSCE00040)
      {  codeString = "AGMSCE00040";
      }
      else if (code == MySpaceStatusCode.AGMSCE00041)
      {  codeString = "AGMSCE00041";
      }
      else if (code == MySpaceStatusCode.AGMSCE00042)
      {  codeString = "AGMSCE00042";
      }
      else if (code == MySpaceStatusCode.AGMSCE00043)
      {  codeString = "AGMSCE00043";
      }
      else if (code == MySpaceStatusCode.AGMSCE00044)
      {  codeString = "AGMSCE00044";
      }
      else if (code == MySpaceStatusCode.AGMSCE00045)
      {  codeString = "AGMSCE00045";
      }
      else if (code == MySpaceStatusCode.AGMSCE00046)
      {  codeString = "AGMSCE00046";
      }
      else if (code == MySpaceStatusCode.AGMSCE00047)
      {  codeString = "AGMSCE00047";
      }
      else if (code == MySpaceStatusCode.AGMSCE00048)
      {  codeString = "AGMSCE00048";
      }
      else if (code == MySpaceStatusCode.AGMMCE00049)
      {  codeString = "AGMMCE00049";
      }
      else if (code == MySpaceStatusCode.AGMMCE00054)
      {  codeString = "AGMMCE00054";
      }
      else if (code == MySpaceStatusCode.AGMMCE00055)
      {  codeString = "AGMMCE00055";
      }
      else if (code == MySpaceStatusCode.AGMMCE00056)
      {  codeString = "AGMMCE00056";
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

      if (code == MySpaceStatusCode.REGISTRYCONF)
      {  message = "usr/local/jakarta-tomcat-4.1.24/conf/astrogrid/mySpace/exampl";
      }
      else if (code == MySpaceStatusCode.TEMPPATHTO)
      {  message = "tmp/mySpaceTest";
      }
      else if (code == MySpaceStatusCode.MYSPACEMANAGER)
      {  message = "ttp://localhost:8080/axis/services/MySpaceManage";
      }
      else if (code == MySpaceStatusCode.SERVERMANAGER)
      {  message = "ttp://localhost:8080/axis/services/ServerManage";
      }
      else if (code == MySpaceStatusCode.AGMMCE00030)
      {  message = "Failed to write registry data file.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00031)
      {  message = "Registry data file is corrupt.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00032)
      {  message = "Failed to read registry data file.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00033)
      {  message = "Requested Data Holder not found.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00034)
      {  message = "No Data Holders matched query string.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00035)
      {  message = "Failed to create Data Holder.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00036)
      {  message = "Failed to create Data Holder (registry update failure).";
      }
      else if (code == MySpaceStatusCode.AGMMCE00037)
      {  message = "Attempt to copy a container instead of a Data Holder.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00038)
      {  message = "Failed to move Data Holder.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00039)
      {  message = "Attempt to move a container instead of a Data Holder.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00040)
      {  message = "Cannot access Data Holder.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00041)
      {  message = "Item is neither a container nor a Data Holder.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00042)
      {  message = "Failed to create container.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00043)
      {  message = "Attempt to delete a container which is not empty.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00044)
      {  message = "Failed to delete Data Holder.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00045)
      {  message = "User not permitted to delete this Data Holder or container.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00046)
      {  message = "User not permitted to write to parent container.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00047)
      {  message = "The parent container does not exist.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00048)
      {  message = "Data Holder or container already exists.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00052)
      {  message = "Unknown server name.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00053)
      {  message = "Illegal or unknown server or user container.";
      }
      else if (code == MySpaceStatusCode.AGMMCW00032)
      {  message = "Registry configuration file contains an illegal expiry period.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00050)
      {  message = "Registry configuration does not contain at least one server.";
      }
      else if (code == MySpaceStatusCode.AGMMCW00031)
      {  message = "Registry configuration contains an invalid server entry.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00060)
      {  message = "Registry data file is corrupt (contains an unrecognised object).";
      }
      else if (code == MySpaceStatusCode.AGMSCE00030)
      {  message = "There is no file to delete; check file path and try again.";
      }
      else if (code == MySpaceStatusCode.AGMSCE00031)
      {  message = "There is no file to save, check file path and try again.";
      }
      else if (code == MySpaceStatusCode.AGMSCE00032)
      {  message = "Error Reading from file.";
      }
      else if (code == MySpaceStatusCode.AGMSCE00033)
      {  message = "Error parsing xmlRequest in MySpaceUtils.parseRequest.";
      }
      else if (code == MySpaceStatusCode.AGMSCE00034)
      {  message = "Error Casting from String to integer.";
      }
      else if (code == MySpaceStatusCode.AGMSCE00035)
      {  message = "Error Calling MySpace ServerManager.";
      }
      else if (code == MySpaceStatusCode.AGMSCE00036)
      {  message = "Null pointer getting one or more of the attributes from request string.";
      }
      else if (code == MySpaceStatusCode.AGMSCE00040)
      {  message = "Error Saving DataHolder.";
      }
      else if (code == MySpaceStatusCode.AGMSCE00041)
      {  message = "Error Moving DataHolder.";
      }
      else if (code == MySpaceStatusCode.AGMSCE00042)
      {  message = "Data Holder Has Been Moved.";
      }
      else if (code == MySpaceStatusCode.AGMSCE00043)
      {  message = "Error Copying DataHolder.";
      }
      else if (code == MySpaceStatusCode.AGMSCE00044)
      {  message = "Data Holder Has Been Copied.";
      }
      else if (code == MySpaceStatusCode.AGMSCE00045)
      {  message = "Cannot delete the dataHolder because of security issues.";
      }
      else if (code == MySpaceStatusCode.AGMSCE00046)
      {  message = "Unknown error occurred whilst deleting data holder.";
      }
      else if (code == MySpaceStatusCode.AGMSCE00047)
      {  message = "File does not exist.";
      }
      else if (code == MySpaceStatusCode.AGMSCE00048)
      {  message = "There has been an I/O exception whilst building the response XML.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00049)
      {  message = "Error Uploading file from datacentre.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00054)
      {  message = "Error looking up the details of a set of dataholders.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00055)
      {  message = "Error looking up the details of a dataholder.";
      }
      else if (code == MySpaceStatusCode.AGMMCE00056)
      {  message = "Error exporting a dataHolder.";
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
