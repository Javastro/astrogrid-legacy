package org.astrogrid.mySpace.mySpaceManager;

import java.io.*;
import java.util.Date;
import java.util.Locale;
import java.text.*;

import org.astrogrid.store.delegate.myspaceItn05.EntryCodes;
import org.astrogrid.store.delegate.myspaceItn05.EntryResults;

/**
 * The <code>DataItemRecord</code> class represents the details held in
 * the MySpace registry for a single <code>DataHolder</code> (in practice
 * a file, typically a VOTable).  These details are things like: the name
 * of the <code>DataHolder</code>, its unique (numeric) identifier, the
 * identifier of the User who owns it, its creation date etc.
 *
 * <p>
 * The class has two constructors.  In one values for all the member
 * variables are passed as arguments, and there is only one <code>set</code>
 * method.  This approach is adopted because the member variables
 * must be specified when a <code>DataItemRecord</code> is created and
 * thereafter their values cannot change.  The exception is the
 * <code>dataItemFile</code> which is reset after the
 * <code>DataItemRecord</code> has been created.
 * <code>DataItemRecord</code>s are created either (i) when the details
 * for a <code>DataHolder</code> is read from the MySpace registry or
 * (ii) in preparation for writing the details of a
 * <code>DataHolder</code> to the MySpace registry.  The second
 * constructor has no arguments and all the member variables are set to
 * null.  It is provided because of Java's scoping rules.
 * </p>
 * <p>
 * In contrast, there is a <code>get</code> method for every member variable.
 * </p>
 * <p>
 * Note that the <code>DataItemRecord</code> class implements Serializable
 * because in Iteration two it is written to and read from a file as a
 * serialised object.  In future iterations the information in each
 * <code>DataItemRecord</code> will probably be stored in a DBMS, and
 * <code>DataItemRecord</code> might then no longer need to be
 * Serializable.
 * </p>
 *
 * @author A C Davenhall (Edinburgh)
 * @since Iteration 2.
 * @version Iteration 5.
 */

public class DataItemRecord implements Serializable
{  

//
//Public constants defining the permitted codes for the DataHolder type.

   public static final int UNKNOWN = EntryCodes.UNKNOWN;   // Unknown.
   public static final int CON = EntryCodes.CON;           // Container.
   public static final int VOT = EntryCodes.VOT;           // VOTable (XML).
   public static final int QUERY = EntryCodes.QUERY;       // Query (XML).
   public static final int WORKFLOW = EntryCodes.WORKFLOW; // Workflow (XML).
   public static final int XML = EntryCodes.XML;           // Generic XML.

   private String dataItemName;     // Full Name.
   private int    dataItemID;       // Identifier in the registry.
   private String dataItemFile;     // Corresponding server file name.
   private String dataItemUri;      // URI to access the file.
   private String dataItemIvorn;    // Ivorn to access the file.
   private String dataItemMime;     // The file mime type.
   private String ownerID;          // Owner's identifier.
   private Date   creationDate;     // Creation date.
   private Date   expiryDate;       // Expiry date.
   private long   size;             // Size (in bytes).
   private int    type;             // Type code (VOTable etc).
   private String permissionsMask;  // Access permissions mask.

//
// Constructors.

/**
 * Constructor in which arguments are passed to set all the member
 * variables (except dataItemUri is not passed).

   public DataItemRecord (String dataItemName,  int dataItemID,
     String dataItemFile,  String ownerID,  Date creationDate,
     Date expiryDate,  int size,  int type,  String permissionsMask)
   {  this.dataItemName = dataItemName;
      this.dataItemID = dataItemID;
      this.dataItemFile = dataItemFile;
      this.ownerID = ownerID;
      this.creationDate = creationDate;
      this.expiryDate = expiryDate;
      this.size = size;
      this.type = type;
      this.permissionsMask = permissionsMask;

      this.dataItemUri = null;
   }
 */

	/**
	 * Constructor in which all arguments are passed to set all the member variables.
	 */
	public DataItemRecord(
		String dataItemName,
		int dataItemID,
		String dataItemFile,
		String dataItemUri,
		String dataItemIvorn,
		String dataItemMime,
		String ownerID,
		Date creationDate,
		Date expiryDate,
		long size,
		int type, 
		String permissionsMask
		) {
		this.dataItemName = dataItemName;
		this.dataItemID = dataItemID;
		this.dataItemFile = dataItemFile;
		this.dataItemUri = dataItemUri;
		this.dataItemIvorn = dataItemIvorn;
		this.dataItemMime = dataItemMime;
		this.ownerID = ownerID;
		this.creationDate = creationDate;
		this.expiryDate = expiryDate;
		this.size = size;
		this.type = type;
		this.permissionsMask = permissionsMask;
		}

	/**
	 * Constructor with no arguments.  All the member variables are set to
	 * null (or -1 in the case of <code>int</code>s).
	 */
	public DataItemRecord ()
		{
		this.dataItemName = null;
		this.dataItemID = -1;
		this.dataItemUri = null;
		this.dataItemFile = null;
		this.dataItemIvorn = null;
		this.ownerID = null;
		this.creationDate = null;
		this.expiryDate = null;
		this.size = -1L;
		this.type = UNKNOWN;
		this.permissionsMask = null;
		}

// ----------------------------------------------------------------------

/**
 * Set the data item name.
 *
 */
   public void setDataItemName(String name)
   {  this.dataItemName = name;
   }

/**
 * Set the URI name corresponding to the <code>DataHolder</code>
 * in the appropriate filestore.
 * @deprecated - the uri should be replaced by the ivorn.
 *
 */
   public void setDataItemUri(String dataItemUri)
   {  this.dataItemUri = dataItemUri;
   }


/**
 * Set the file name corresponding to the <code>DataHolder</code>
 * in the appropriate filestore.
 *
 */
   protected void setDataItemFile(String dataItemFile)
   {  this.dataItemFile = dataItemFile;
   }

/**
 * Reset the DataItemFile.  The DataItemFile name should not be exported
 * out of the mySpace system.  This method resets the name in the current
 * DataItemRecord to null.  It is typically invoked prior to exporting
 * the DataItemRecord.
 *

   public void resetDataItemFile()
   {  this.dataItemFile = null;
   }
 */


/**
 * Set the Ivorn  corresponding to the <code>DataHolder</code>
 * in the appropriate filestore server.
 *
 */
   protected void setDataItemIvorn(String dataItemIvorn)
   {  this.dataItemIvorn = dataItemIvorn;
   }

/**
 * Set the file size.
 *
 */
   protected void setSize(long size)
   {  this.size = size;
   }

/**
 * Set the data item mime type.
 *
 */
   public void setDataItemMime(String mime)
   {
   this.dataItemMime = mime ;
   }

// ----------------------------------------------------------------------

//
// Get methods.
//
// The DataItemRecord class has a get method for every member variable.

/**
 * Return the fully resolved name of the <code>DataHolder</code>.
 */

   public String getDataItemName()
   {  return dataItemName;
   }

/**
 * Return the identifier of the of the <code>DataHolder</code>.
 */

   public int getDataItemID()
   {  return dataItemID;
   }

/**
 * Return the file name corresponding to the <code>DataHolder</code>
 * in the appropriate MySpace server.
 */

   public String getDataItemFile()
   {  return dataItemFile;
   }

/**
 * Return the URI name corresponding to the <code>DataHolder</code>
 * in the appropriate MySpace server.
 * @deprecated - the uri should be replaced by the ivorn.
 *
 */

   public String getDataItemUri()
   {  return dataItemUri;
   }

/**
 * Return the Ivorn corresponding to the <code>DataHolder</code>
 * in the appropriate file store.
 */
   public String getDataItemIvorn()
   {  return dataItemIvorn;
   }


/**
 * Return the identifer of the owner of the <code>DataHolder</code>.
 */

   public String getOwnerID()
   {  return ownerID;
   }

/**
 * Return the creation date of the <code>DataHolder</code>.
 */

   public Date getCreationDate()
   {  return creationDate;
   }

/**
 * Return the expiry date of the <code>DataHolder</code>.
 */

   public Date getExpiryDate()
   {  return expiryDate;
   }

/**
 * Return the size of the <code>DataHolder</code> (in bytes).
 */

   public long getSize()
   {  return size;
   }

/**
 * Return the type of the <code>DataHolder</code>.
 */

   public int getType()
   {  return type;
   }

/**
 * Return the access permissions mask of the <code>DataHolder</code>.
 */

   public String getPermissionsMask()
   {  return permissionsMask;
   }

/**
 * Return the name of the server on which the <code>DataHolder</code>
 * is stored.  The server is determined by examining the
 * <code>dataItemName</code>.
 */

// Note that this method differs from the other get methods in that
// it derives a value rather than simply return a member variable.

   public String getServer()
   {  String serverName = "";

//
//   [TODO]: do not hard-wire the server name.

      serverName = "serv1";

      return serverName;
   }

/**
 * Get the data item mime type.
 *
 */
   public String getDataItemMime()
   {
   return this.dataItemMime;
   }


/**
 * Return an EntryResults object constructed from the DataItemRecord.
 */

   public EntryResults getEntryResults()
   {  EntryResults entryResult = new EntryResults();

      long creation = creationDate.getTime();
      long expiry = expiryDate.getTime();

      entryResult.setCreationDate(creation);
      entryResult.setEntryId(dataItemID);
      entryResult.setEntryName(dataItemName);
      entryResult.setEntryUri(dataItemUri);
      entryResult.setExpiryDate(expiry);
      entryResult.setOwnerId(ownerID);
      entryResult.setPermissionsMask(permissionsMask);
      entryResult.setSize(size);
      entryResult.setType(type);
      entryResult.setMime(dataItemMime);

      return entryResult;
   }

// ----------------------------------------------------------------------

/**
 * Produce a reasonable string representation of a
 * <code>DataItemRecord</code>.
 */

   public String toString()
   {  StringBuffer returnString;

      DateFormat dateRepn = DateFormat.getDateTimeInstance(
        DateFormat.SHORT, DateFormat.SHORT, Locale.UK);
      returnString = new StringBuffer(" ( "+dataItemName+" created="+dateRepn.format(creationDate) +" owner="+ownerID+" type=");
      if (type == CON)
      {  
          returnString.append("container)"); 
         
      }
      else if (type == VOT)
      {  
          returnString.append("VoTable, size=" + size +")");
      }
      else if (type == QUERY)
      {           returnString.append("query, size=" + size +")");

      }
      else if (type == WORKFLOW)
      {            
          returnString.append("workflow, size=" + size +")");

      }
      else
      {           
          returnString.append("unknown, size=" + size +")");

      }

      return returnString.toString();
   }

//
// Static methods.

/**
 * Convert a string representation of a DataItemRecord content type into
 *  the corresponding integer code.
 */

   public static int translateType(String contentType)
   {  int returnType = DataItemRecord.UNKNOWN;

      if (contentType.equalsIgnoreCase("QUERY") )
      {  returnType = DataItemRecord.QUERY;
      }
      else if (contentType.equalsIgnoreCase("WF") )
      {  returnType = DataItemRecord.WORKFLOW;
      }
      else if (contentType.equalsIgnoreCase("VOT") ||
               contentType.equalsIgnoreCase("") )
      {  returnType = DataItemRecord.VOT;
      }

      return returnType;
   }



}
