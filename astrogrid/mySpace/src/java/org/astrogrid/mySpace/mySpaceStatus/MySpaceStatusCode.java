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
   public static final String MS_E_REGDWRT = "MS_E_REGDWRT"; //"Failed to write registry data file."
   public static final String MS_E_REGDCRT="MS_E_REGDCRT"; //"Registry data file is corrupt."
   public static final String MS_E_REGDRED="MS_E_REGDRED"; //"Failed to read registry data file."
   public static final String MS_E_DHNTFND="MS_E_DHNTFND"; //"Requested Data Holder not found."
   public static final String MS_I_NDHMTCH="MS_I_NDHMTCH"; //"No Data Holders matched query string."
   public static final String MS_E_FLCRTDH="MS_E_FLCRTDH"; //"Failed to create Data Holder."
   public static final String MS_E_FCRTDHR="MS_E_FCRTDHR"; //"Failed to create Data Holder (registry update failure)."
   public static final String MS_E_CPYDHCN="MS_E_CPYDHCN"; //"Attempt to copy a container instead of a Data Holder."
   public static final String MS_E_FLMOVDH="MS_E_FLMOVDH"; //"Failed to move Data Holder."
   public static final String MS_E_MOVDHCN="MS_E_MOVDHCN"; //"Attempt to move a container instead of a Data Holder."
   public static final String MS_E_FACCSDH="MS_E_FACCSDH"; //"Cannot access Data Holder."
   public static final String MS_E_FNTCNDH="MS_E_FNTCNDH"; //"Item is neither a container nor a Data Holder."
   public static final String MS_E_FLCRTCN="MS_E_FLCRTCN"; //"Failed to create container."
   public static final String MS_E_DNEMPCN="MS_E_DNEMPCN"; //"Attempt to delete a container which is not empty."
   public static final String MS_E_FLDELDH="MS_E_FLDELDH"; //"Failed to delete Data Holder."
   public static final String MS_E_NTPERDH="MS_E_NTPERDH"; //"User not permitted to delete this Data Holder or container."
   public static final String MS_E_NPRWPCN="MS_E_NPRWPCN"; //"User not permitted to write to parent container."
   public static final String MS_E_PCNNTEX="MS_E_PCNNTEX"; //"The parent container does not exist."
   public static final String MS_E_DHCNAEX="MS_E_DHCNAEX"; //"Data Holder or container already exists."
   public static final String NULL_FILE_DELETE="NULL_FILE_DELETE"; //There is no file to delete, check your file path and try again.otherwise this fuction is failed!
   public static final String NULL_FILE_SAVE="NULL_FILE_SAVE"; //There is no file to save, check file path and try again.
   public static final String ERROR_READING_FILE="ERROR_READING_FILE"; //Error Reading from file.
   public static final String PARSE_REQUEST_ERROR="PARSE_REQUEST_ERROR"; //Error parsing xmlRequest in MySpaceUtils.parseRequest.
   public static final String NUMBER_FORMAT_ERROR="NUMBER_FORMAT_ERROR"; //Error Casting from String to int.
   public static final String ERROR_CALL_SERVER_MANAGER="ERROR_CALL_SERVER_MANAGER"; //Error Calling MySpace ServerManager.
   public static final String NULL_POINTER_GETTING_REQUEST="NULL_POINTER_GETTING_REQUEST"; //Null pointer getting one or more of the attributes from request string in MySpace.
   public static final String ERR_SAVE_DATAHOLDER="ERR_SAVE_DATAHOLDER"; //Error Saving DataHolder.
   public static final String ERR_MOVE_DATA_HOLDER="ERR_MOVE_DATA_HOLDER"; //Error Moving DataHolder.
   public static final String DATA_HOLDER_MOVED="DATA_HOLDER_MOVED"; //Data Holder Has Been Moved.
   public static final String ERR_COPY_DATA_HOLDER="ERR_COPY_DATA_HOLDER"; //Error Copying DataHolder.
   public static final String DATA_HOLDER_COPIED="DATA_HOLDER_COPIED"; //Data Holder Has Been Copied.
   public static final String ERR_SECURITY_DELETE_DATA_HOLDER="ERR_SECURITY_DELETE_DATA_HOLDER"; //Can't delete the data holder because of security issues.
   public static final String ERR_DELETE_DATA_HOLDER="ERR_DELETE_DATA_HOLDER"; //Unkown Error occourd whild deleting data holder.
   public static final String FILE_NOT_EXIST="FILE_NOT_EXIST";//File not exist!
   public static final String ERR_IO_BUILD_RESPONS="ERR_IO_BUILD_RESPONS"; //There is an io exception whild building response xml.
   public static final String MS_E_UPLOAD="MS_E_UPLOAD"; //Error Uploading file from datacentre.
   public static final String MS_E_LOOKUP_DATAHOLDERS="MS_E_LOOKUP_DATAHOLDERS"; //Error look up dataholderS details.
   public static final String MS_E_LOOKUP_DATAHOLDER="MS_E_LOOKUP_DATAHOLDER"; //Error look up dataholder details.
   public static final String MS_E_EXPORT="MS_E_EXPORT"; //Error Exporting Data Holder.

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
