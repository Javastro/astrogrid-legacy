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
   public static final int MS_E_REGDWRT = 4;
   public static final int MS_E_REGDCRT = 5;
   public static final int MS_E_REGDRED = 6;
   public static final int MS_E_DHNTFND = 7;
   public static final int MS_I_NDHMTCH = 8;
   public static final int MS_E_FLCRTDH = 9;
   public static final int MS_E_FCRTDHR = 10;
   public static final int MS_E_CPYDHCN = 11;
   public static final int MS_E_FLMOVDH = 12;
   public static final int MS_E_MOVDHCN = 13;
   public static final int MS_E_FACCSDH = 14;
   public static final int MS_E_FNTCNDH = 15;
   public static final int MS_E_FLCRTCN = 16;
   public static final int MS_E_DNEMPCN = 17;
   public static final int MS_E_FLDELDH = 18;
   public static final int MS_E_NTPERDH = 19;
   public static final int MS_E_NPRWPCN = 20;
   public static final int MS_E_PCNNTEX = 21;
   public static final int MS_E_DHCNAEX = 22;
   public static final int MS_E_SRVINVN = 23;
   public static final int MS_E_ILLSRCN = 24;
   public static final int MS_W_RGCEXPM = 25;
   public static final int MS_E_RGCSRVM = 26;
   public static final int MS_W_RGCSRVI = 27;
   public static final int MS_E_REGDCPT = 28;
   public static final int NULL_FILE_DELETE = 29;
   public static final int NULL_FILE_SAVE = 30;
   public static final int ERROR_READING_FILE = 31;
   public static final int PARSE_REQUEST_ERROR = 32;
   public static final int NUMBER_FORMAT_ERROR = 33;
   public static final int ERROR_CALL_SERVER_MANAGER = 34;
   public static final int NULL_POINTER_GETTING_REQUEST = 35;
   public static final int ERR_SAVE_DATAHOLDER = 36;
   public static final int ERR_MOVE_DATA_HOLDER = 37;
   public static final int DATA_HOLDER_MOVED = 38;
   public static final int ERR_COPY_DATA_HOLDER = 39;
   public static final int DATA_HOLDER_COPIED = 40;
   public static final int ERR_SECURITY_DELETE_DATA_HOLDER = 41;
   public static final int ERR_DELETE_DATA_HOLDER = 42;
   public static final int FILE_NOT_EXIST = 43;
   public static final int ERR_IO_BUILD_RESPONS = 44;
   public static final int MS_E_UPLOAD = 45;
   public static final int MS_E_LOOKUP_DATAHOLDERS = 46;
   public static final int MS_E_LOOKUP_DATAHOLDER = 47;
   public static final int MS_E_EXPORT = 48;


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
      else if (code == MySpaceStatusCode.MS_E_REGDWRT)
      {  codeString = "MS_E_REGDWRT";
      }
      else if (code == MySpaceStatusCode.MS_E_REGDCRT)
      {  codeString = "MS_E_REGDCRT";
      }
      else if (code == MySpaceStatusCode.MS_E_REGDRED)
      {  codeString = "MS_E_REGDRED";
      }
      else if (code == MySpaceStatusCode.MS_E_DHNTFND)
      {  codeString = "MS_E_DHNTFND";
      }
      else if (code == MySpaceStatusCode.MS_I_NDHMTCH)
      {  codeString = "MS_I_NDHMTCH";
      }
      else if (code == MySpaceStatusCode.MS_E_FLCRTDH)
      {  codeString = "MS_E_FLCRTDH";
      }
      else if (code == MySpaceStatusCode.MS_E_FCRTDHR)
      {  codeString = "MS_E_FCRTDHR";
      }
      else if (code == MySpaceStatusCode.MS_E_CPYDHCN)
      {  codeString = "MS_E_CPYDHCN";
      }
      else if (code == MySpaceStatusCode.MS_E_FLMOVDH)
      {  codeString = "MS_E_FLMOVDH";
      }
      else if (code == MySpaceStatusCode.MS_E_MOVDHCN)
      {  codeString = "MS_E_MOVDHCN";
      }
      else if (code == MySpaceStatusCode.MS_E_FACCSDH)
      {  codeString = "MS_E_FACCSDH";
      }
      else if (code == MySpaceStatusCode.MS_E_FNTCNDH)
      {  codeString = "MS_E_FNTCNDH";
      }
      else if (code == MySpaceStatusCode.MS_E_FLCRTCN)
      {  codeString = "MS_E_FLCRTCN";
      }
      else if (code == MySpaceStatusCode.MS_E_DNEMPCN)
      {  codeString = "MS_E_DNEMPCN";
      }
      else if (code == MySpaceStatusCode.MS_E_FLDELDH)
      {  codeString = "MS_E_FLDELDH";
      }
      else if (code == MySpaceStatusCode.MS_E_NTPERDH)
      {  codeString = "MS_E_NTPERDH";
      }
      else if (code == MySpaceStatusCode.MS_E_NPRWPCN)
      {  codeString = "MS_E_NPRWPCN";
      }
      else if (code == MySpaceStatusCode.MS_E_PCNNTEX)
      {  codeString = "MS_E_PCNNTEX";
      }
      else if (code == MySpaceStatusCode.MS_E_DHCNAEX)
      {  codeString = "MS_E_DHCNAEX";
      }
      else if (code == MySpaceStatusCode.MS_E_SRVINVN)
      {  codeString = "MS_E_SRVINVN";
      }
      else if (code == MySpaceStatusCode.MS_E_ILLSRCN)
      {  codeString = "MS_E_ILLSRCN";
      }
      else if (code == MySpaceStatusCode.MS_W_RGCEXPM)
      {  codeString = "MS_W_RGCEXPM";
      }
      else if (code == MySpaceStatusCode.MS_E_RGCSRVM)
      {  codeString = "MS_E_RGCSRVM";
      }
      else if (code == MySpaceStatusCode.MS_W_RGCSRVI)
      {  codeString = "MS_W_RGCSRVI";
      }
      else if (code == MySpaceStatusCode.MS_E_REGDCPT)
      {  codeString = "MS_E_REGDCPT";
      }
      else if (code == MySpaceStatusCode.NULL_FILE_DELETE)
      {  codeString = "NULL_FILE_DELETE";
      }
      else if (code == MySpaceStatusCode.NULL_FILE_SAVE)
      {  codeString = "NULL_FILE_SAVE";
      }
      else if (code == MySpaceStatusCode.ERROR_READING_FILE)
      {  codeString = "ERROR_READING_FILE";
      }
      else if (code == MySpaceStatusCode.PARSE_REQUEST_ERROR)
      {  codeString = "PARSE_REQUEST_ERROR";
      }
      else if (code == MySpaceStatusCode.NUMBER_FORMAT_ERROR)
      {  codeString = "NUMBER_FORMAT_ERROR";
      }
      else if (code == MySpaceStatusCode.ERROR_CALL_SERVER_MANAGER)
      {  codeString = "ERROR_CALL_SERVER_MANAGER";
      }
      else if (code == MySpaceStatusCode.NULL_POINTER_GETTING_REQUEST)
      {  codeString = "NULL_POINTER_GETTING_REQUEST";
      }
      else if (code == MySpaceStatusCode.ERR_SAVE_DATAHOLDER)
      {  codeString = "ERR_SAVE_DATAHOLDER";
      }
      else if (code == MySpaceStatusCode.ERR_MOVE_DATA_HOLDER)
      {  codeString = "ERR_MOVE_DATA_HOLDER";
      }
      else if (code == MySpaceStatusCode.DATA_HOLDER_MOVED)
      {  codeString = "DATA_HOLDER_MOVED";
      }
      else if (code == MySpaceStatusCode.ERR_COPY_DATA_HOLDER)
      {  codeString = "ERR_COPY_DATA_HOLDER";
      }
      else if (code == MySpaceStatusCode.DATA_HOLDER_COPIED)
      {  codeString = "DATA_HOLDER_COPIED";
      }
      else if (code == MySpaceStatusCode.ERR_SECURITY_DELETE_DATA_HOLDER)
      {  codeString = "ERR_SECURITY_DELETE_DATA_HOLDER";
      }
      else if (code == MySpaceStatusCode.ERR_DELETE_DATA_HOLDER)
      {  codeString = "ERR_DELETE_DATA_HOLDER";
      }
      else if (code == MySpaceStatusCode.FILE_NOT_EXIST)
      {  codeString = "FILE_NOT_EXIST";
      }
      else if (code == MySpaceStatusCode.ERR_IO_BUILD_RESPONS)
      {  codeString = "ERR_IO_BUILD_RESPONS";
      }
      else if (code == MySpaceStatusCode.MS_E_UPLOAD)
      {  codeString = "MS_E_UPLOAD";
      }
      else if (code == MySpaceStatusCode.MS_E_LOOKUP_DATAHOLDERS)
      {  codeString = "MS_E_LOOKUP_DATAHOLDERS";
      }
      else if (code == MySpaceStatusCode.MS_E_LOOKUP_DATAHOLDER)
      {  codeString = "MS_E_LOOKUP_DATAHOLDER";
      }
      else if (code == MySpaceStatusCode.MS_E_EXPORT)
      {  codeString = "MS_E_EXPORT";
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
      else if (code == MySpaceStatusCode.MS_E_REGDWRT)
      {  message = "Failed to write registry data file.";
      }
      else if (code == MySpaceStatusCode.MS_E_REGDCRT)
      {  message = "Registry data file is corrupt.";
      }
      else if (code == MySpaceStatusCode.MS_E_REGDRED)
      {  message = "Failed to read registry data file.";
      }
      else if (code == MySpaceStatusCode.MS_E_DHNTFND)
      {  message = "Requested Data Holder not found.";
      }
      else if (code == MySpaceStatusCode.MS_I_NDHMTCH)
      {  message = "No Data Holders matched query string.";
      }
      else if (code == MySpaceStatusCode.MS_E_FLCRTDH)
      {  message = "Failed to create Data Holder.";
      }
      else if (code == MySpaceStatusCode.MS_E_FCRTDHR)
      {  message = "Failed to create Data Holder (registry update failure).";
      }
      else if (code == MySpaceStatusCode.MS_E_CPYDHCN)
      {  message = "Attempt to copy a container instead of a Data Holder.";
      }
      else if (code == MySpaceStatusCode.MS_E_FLMOVDH)
      {  message = "Failed to move Data Holder.";
      }
      else if (code == MySpaceStatusCode.MS_E_MOVDHCN)
      {  message = "Attempt to move a container instead of a Data Holder.";
      }
      else if (code == MySpaceStatusCode.MS_E_FACCSDH)
      {  message = "Cannot access Data Holder.";
      }
      else if (code == MySpaceStatusCode.MS_E_FNTCNDH)
      {  message = "Item is neither a container nor a Data Holder.";
      }
      else if (code == MySpaceStatusCode.MS_E_FLCRTCN)
      {  message = "Failed to create container.";
      }
      else if (code == MySpaceStatusCode.MS_E_DNEMPCN)
      {  message = "Attempt to delete a container which is not empty.";
      }
      else if (code == MySpaceStatusCode.MS_E_FLDELDH)
      {  message = "Failed to delete Data Holder.";
      }
      else if (code == MySpaceStatusCode.MS_E_NTPERDH)
      {  message = "User not permitted to delete this Data Holder or container.";
      }
      else if (code == MySpaceStatusCode.MS_E_NPRWPCN)
      {  message = "User not permitted to write to parent container.";
      }
      else if (code == MySpaceStatusCode.MS_E_PCNNTEX)
      {  message = "The parent container does not exist.";
      }
      else if (code == MySpaceStatusCode.MS_E_DHCNAEX)
      {  message = "Data Holder or container already exists.";
      }
      else if (code == MySpaceStatusCode.MS_E_SRVINVN)
      {  message = "Unknown server name.";
      }
      else if (code == MySpaceStatusCode.MS_E_ILLSRCN)
      {  message = "Illegal or unknown server or user container.";
      }
      else if (code == MySpaceStatusCode.MS_W_RGCEXPM)
      {  message = "Registry configuration file contains an illegal expiry period.";
      }
      else if (code == MySpaceStatusCode.MS_E_RGCSRVM)
      {  message = "Registry configuration does not contain at least one server.";
      }
      else if (code == MySpaceStatusCode.MS_W_RGCSRVI)
      {  message = "Registry configuration contains an invalid server entry.";
      }
      else if (code == MySpaceStatusCode.MS_E_REGDCPT)
      {  message = "Registry data file is corrupt (contains an unrecognised object).";
      }
      else if (code == MySpaceStatusCode.NULL_FILE_DELETE)
      {  message = "There is no file to delete; check file path and try again.";
      }
      else if (code == MySpaceStatusCode.NULL_FILE_SAVE)
      {  message = "There is no file to save, check file path and try again.";
      }
      else if (code == MySpaceStatusCode.ERROR_READING_FILE)
      {  message = "Error Reading from file.";
      }
      else if (code == MySpaceStatusCode.PARSE_REQUEST_ERROR)
      {  message = "Error parsing xmlRequest in MySpaceUtils.parseRequest.";
      }
      else if (code == MySpaceStatusCode.NUMBER_FORMAT_ERROR)
      {  message = "Error Casting from String to integer.";
      }
      else if (code == MySpaceStatusCode.ERROR_CALL_SERVER_MANAGER)
      {  message = "Error Calling MySpace ServerManager.";
      }
      else if (code == MySpaceStatusCode.NULL_POINTER_GETTING_REQUEST)
      {  message = "Null pointer getting one or more of the attributes from request string.";
      }
      else if (code == MySpaceStatusCode.ERR_SAVE_DATAHOLDER)
      {  message = "Error Saving DataHolder.";
      }
      else if (code == MySpaceStatusCode.ERR_MOVE_DATA_HOLDER)
      {  message = "Error Moving DataHolder.";
      }
      else if (code == MySpaceStatusCode.DATA_HOLDER_MOVED)
      {  message = "Data Holder Has Been Moved.";
      }
      else if (code == MySpaceStatusCode.ERR_COPY_DATA_HOLDER)
      {  message = "Error Copying DataHolder.";
      }
      else if (code == MySpaceStatusCode.DATA_HOLDER_COPIED)
      {  message = "Data Holder Has Been Copied.";
      }
      else if (code == MySpaceStatusCode.ERR_SECURITY_DELETE_DATA_HOLDER)
      {  message = "Cannot delete the dataHolder because of security issues.";
      }
      else if (code == MySpaceStatusCode.ERR_DELETE_DATA_HOLDER)
      {  message = "Unknown error occurred whilst deleting data holder.";
      }
      else if (code == MySpaceStatusCode.FILE_NOT_EXIST)
      {  message = "File does not exist.";
      }
      else if (code == MySpaceStatusCode.ERR_IO_BUILD_RESPONS)
      {  message = "There has been an I/O exception whilst building the response XML.";
      }
      else if (code == MySpaceStatusCode.MS_E_UPLOAD)
      {  message = "Error Uploading file from datacentre.";
      }
      else if (code == MySpaceStatusCode.MS_E_LOOKUP_DATAHOLDERS)
      {  message = "Error looking up the details of a set of dataholders.";
      }
      else if (code == MySpaceStatusCode.MS_E_LOOKUP_DATAHOLDER)
      {  message = "Error looking up the details of a dataholder.";
      }
      else if (code == MySpaceStatusCode.MS_E_EXPORT)
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
