// package org.astrogrid.mySpace.mySpaceRegistry;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

/**
 * The <code>DataItemRecord</code> class represents the details held in
 * the MySpace registry for a single <code>DataHolder</code> (in practice
 * a file, typically a VOTable).  These details are things like: the name
 * of the <code>DataHolder</code>, its unique (numeric) identifier, the
 * identifier of the User who owns it, its creation date etc.
 *
 * <p>
 * The class has two constructors.  In one values for all the member
 * variables are passed as arguments, and there are no <code>set</code>
 * methods.  This approach is adopted because all the member variables
 * must be specified when a <code>DataItemRecord</code> is created and
 * thereafter their values cannot change.  <code>DataItemRecords</code>
 * are created either (i) when the details for a <code>DataHolder</code>
 * is read from the MySpace registry or (ii) in preparation for writing
 * the details of a <code>DataHolder</code> to the MySpace registry.
 * The second constructor has no arguments and all the member variables
 * are set to null.  It is provided because of Java's scoping rules.
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
 * @version Iteration 2.
 */

public class DataItemRecord implements Serializable
{  private String dataItemName;     // Full Name.
   private int    dataItemID;       // Identifier in the registry.
   private String dataItemFile;     // Corresponding server file name.
   private String ownerID;          // Owner's identifier.
   private Date   creationDate;     // Creation date.
   private Date   expiryDate;       // Expiry date.
   private int    size;             // Size (in bytes).
   private String type;             // Type (VOTable etc).
   private String permissionsMask;  // Access permissions mask.

//
// Constructors.

/**
 * Constructor in which arguments are passed to set all the member
 * variables.
 */

   public DataItemRecord (String dataItemName,  int dataItemID,
     String dataItemFile,  String ownerID,  Date creationDate,
     Date expiryDate,  int size,  String type,  String permissionsMask)
   {  this.dataItemName = dataItemName;
      this.dataItemID = dataItemID;
      this.dataItemFile = dataItemFile;
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
   {  this.dataItemName = null;
      this.dataItemID = -1;
      this.dataItemFile = null;
      this.ownerID = null;
      this.creationDate = null;
      this.expiryDate = null;
      this.size = -1;
      this.type = null;
      this.permissionsMask = null;
   }

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

   public int getSize()
   {  return size;
   }

/**
  * Return the type of the <code>DataHolder</code>.
  */

   public String getType()
   {  return type;
   }

/**
  * Return the access permissions mask of the <code>DataHolder</code>.
  */

   public String getPermissionsMask()
   {  return permissionsMask;
   }

//
// Other methods.

/**
 * Produce a reasonable string representation of a
 * <code>DataItemRecord</code>.
 */

   public String toString()
   {  return dataItemName + " (" + size + " bytes, created " +
        creationDate + ")";
   }
}
