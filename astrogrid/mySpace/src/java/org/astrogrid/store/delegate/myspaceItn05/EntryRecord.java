package org.astrogrid.store.delegate.myspaceItn05;

import java.util.Date;
import java.util.Locale;
import java.text.*;

import org.astrogrid.store.delegate.StoreFile;
import org.astrogrid.store.Agsl;

/**
 * A <code>EntryRecord</code> object stores the details held in a
 * MySpace registry for a single entry in a MySpace Service (that is, a
 * file, perhaps typically a VOTable).  The details held are things
 * like: the MySpace name of the entry, the identifier of the user who
 * owns it, its creation date etc.
 *
 * <p>
 * <code>EntryRecord</code> objects are returned by various methods of
 * the  MySpace delegate.  These objects are the mechanism by which the
 * delegate delivers information about the entries in a MySpace Service
 * to the application which is invoking it.  Consequently, the
 * <code>EntryRecord</code> class has get methods to return its various
 * member variables, but no corresponding set methods.  Rather, there is
 * a constructor in which all the member variables are set.  This
 * constructor is usually only used inside the delegate.  There is also
 * a second constructor with no arguments (and which sets all its member 
 * variables to null or dummy values), which is more likely to be used
 * by an application invoking the delegate.
 * </p>
 *
 * <p>
 * Formally <code>EntryRecord</code> implements the
 * <code>StoreFile</code> interface.  However, the code>StoreFile</code>
 * methods concerned with accessing parents and children always return
 * null.
 * </p>
 *
 * @author A C Davenhall (Edinburgh)
 * @since Iteration 5.
 * @version Iteration 5.
 */

public class EntryRecord implements StoreFile
{  
//
//Member variables defining the entry record.

   private String entryName;        // Full MySpace entry name.
   private int    entryId;          // Internal MySpace identifier.
   private String entryUri;         // URI for the entry.
   private String ownerId;          // Owner's identifier.
   private Date   creationDate;     // Creation date.
   private Date   expiryDate;       // Expiry date.
   private int    size;             // Size (in bytes).
   private int    type;             // Type code (VOTable etc).
   private String permissionsMask;  // Access permissions mask.

//
// Constructors.

/**
 * Constructor in which arguments are passed to set all the member
 * variables.
 *
 * @param entryName Full MySpace entry name.
 * @param entryId Internal MySpace identifier.
 * @param entryUri URI for the entry (for external access).
 * @param ownerId Owner's identifier.
 * @param creationDate Creation date.
 * @param expiryDate Expiry date.
 * @param size Size of the entry (in bytes).
 * @param type Type code for the entry.  One of
 *   <code>EntryCodes.UNKNOWN</code>, <code>EntryCodes.CON</code>,
 *   <code>EntryCodes.VOT</code>,  <code>EntryCodes.QUERY</code>,
 *   <code>EntryCodes.WORKFLOW</code> or <code>EntryCodes.XML</code>.
 * @param permissionsMask Access permissions mask.
 */

   public EntryRecord (String entryName,  int entryId,
     String entryUri,  String ownerId,  Date creationDate,
     Date expiryDate,  int size,  int type,  String permissionsMask)
   {  this.entryName = entryName;
      this.entryId = entryId;
      this.entryUri = entryUri;
      this.ownerId = ownerId;
      this.creationDate = creationDate;
      this.expiryDate = expiryDate;
      this.size = size;
      this.type = type;
      this.permissionsMask = permissionsMask;
   }


/**
 * Create a <code>EntryRecord</code> object from the equivalent
 * <code>EntryResults</code> object.
 */

   public EntryRecord (EntryResults entryResults)
   {  this.entryName = entryResults.getEntryName();
      this.entryId = entryResults.getEntryId();
      this.entryUri = entryResults.getEntryUri();
      this.ownerId = entryResults.getOwnerId();
      this.creationDate = new Date( entryResults.getCreationDate() );
      this.expiryDate = new Date( entryResults.getExpiryDate() );
      this.size = entryResults.getSize();
      this.type = entryResults.getType();
      this.permissionsMask = entryResults.getPermissionsMask();
   }




/**
 * Constructor with no arguments.  All the member variables are set to
 * null (or -1 in the case of <code>int</code>s).
 */

   public EntryRecord ()
   {  this.entryName = null;
      this.entryId = -1;
      this.entryUri = null;
      this.ownerId = null;
      this.creationDate = null;
      this.expiryDate = null;
      this.size = -1;
      this.type = EntryCodes.UNKNOWN;
      this.permissionsMask = null;
   }


// ----------------------------------------------------------------------

//
// Methods implementing the StoreFile interface.

/**
 * Return the fully resolved MySpace name of the entry.
 *
 * @return The fully resolved MySpace name of the entry.
 */

   public String getName()
   {  return entryName;
   }

/**
 * Return the parent folder of this file/folder.  For this class null
 * is always returned.
 */
   public StoreFile getParent()
   {  return null;
   }
   
/**
 * Returns true if this EntryRecord is a container which can hold other
 * files or folders.
 */
   public boolean isFolder()
   {  boolean isContainer;

      if (type == EntryCodes.CON)
      {  isContainer = true;
      }
      else
      {  isContainer = false;
      }

      return isContainer;
   }
   
/** 
 * Return true if this EntryRecord is a self-contained file.  For
 * example, a database table might be represented as a StoreFile but it
 * is not a file.  A value of false is always returned because currently
 * MySpace cannot handle database tables.
 */
   public boolean isFile()
   {  return false;
   }
   
/**
 * If the EntryRecord is a container then list all its children.  If it
 * is not a container then return null.  Note that here a value of
 * null is always returned, even if the EntryRecord is a container.
 */

   public StoreFile[] listFiles()
   {  return null;
   }
   
/** 
 * Return the path to this file on the MySpace Service.
 */
   public String getPath()
   {  String path = "/";

      int lastSep = entryName.lastIndexOf("/");
      if (lastSep>0)
      {  path = entryName.substring(0, lastSep+1);
      }

      return path;
   }
   
/**
 * Return where to find this file using an AStrogrid Store Locator.
 *
 * [TODO] Hmmm.  The following may or may not be correct!

 */
   public Agsl toAgsl()
   {  try
      {  Agsl agsl = new Agsl( new java.net.URL(entryUri) );
         return agsl;
      }
      catch (Exception e)
      {  return null;
      }
   }
   
/**
 * Return true if this represents the same file as the given one, within
 * this server.  This won't check for references from different stores
 * to the same file.
 */
   public boolean equals(StoreFile anotherFile)
   {  String anotherName = anotherFile.getName();

      boolean isSame = entryName.equals(anotherName);

      return isSame;
   }


// ----------------------------------------------------------------------

//
// Additional get methods.
//
// The EntryRecord class has a get method for every member variable.

/**
 * Return the internal MySpace identifier of the of the entry.
 *
 * @return The internal MySpace identifier of the of the entry.
 */

   public int getEntryId()
   {  return entryId;
   }

/**
 * Return the URI for external access to the entry.
 *
 * @return The URI for the entry.
 */

   public String getEntryUri()
   {  return entryUri;
   }

/**
 * Return the identifer of the owner of the entry.
 *
 * @return The identifer of the owner of the entry.
 */

   public String getOwnerId()
   {  return ownerId;
   }

/**
 * Return the creation date of the entry.
 *
 * @return The creation date of the entry.
 */

   public Date getCreationDate()
   {  return creationDate;
   }

/**
 * Return the expiry date of the entry.
 *
 * @return The expiry date of the entry.
 */

   public Date getExpiryDate()
   {  return expiryDate;
   }

/**
 * Return the size of the entry.
 *
 * @return The size of the entry (in bytes).
 */

   public int getSize()
   {  return size;
   }

/**
 * Return the type of the entry.
 *
 * @return The type of the entry.  One of: <code>EntryCodes.UNKNOWN</code>, 
 *   <code>EntryCodes.CON</code>, <code>EntryCodes.VOT</code>, 
 *   <code>EntryCodes.QUERY</code>, <code>EntryCodes.WORKFLOW</code> or
 *   <code>EntryCodes.XML</code>.
 */

   public int getType()
   {  return type;
   }

/**
 * Return the access permissions mask of the entry.
 *
 * @return The access permissions mask of the entry.
 */

   public String getPermissionsMask()
   {  return permissionsMask;
   }


// ----------------------------------------------------------------------

//
// Other methods.

/**
 * Produce a reasonable string representation of an entry.
 */

   public String toString()
   {  String returnString;

      DateFormat dateRepn = DateFormat.getDateTimeInstance(
        DateFormat.SHORT, DateFormat.SHORT, Locale.UK);

      if (type == EntryCodes.CON)
      {  returnString = entryName + " (container, created " +
           dateRepn.format(creationDate) + ")";
      }
      else if (type == EntryCodes.VOT)
      {  returnString = entryName + " (VOTable, " + size + " bytes, " +
           dateRepn.format(creationDate) + ")";
      }
      else if (type == EntryCodes.QUERY)
      {  returnString = entryName + " (query, " + size + " bytes, " +
           dateRepn.format(creationDate) + ")";
      }
      else if (type == EntryCodes.WORKFLOW)
      {  returnString = entryName + " (workflow, " + size + " bytes, " +
           dateRepn.format(creationDate) + ")";
      }
      else if (type == EntryCodes.XML)
      {  returnString = entryName + " (generic XML, " + size + " bytes, " +
           dateRepn.format(creationDate) + ")";
      }
      else
      {  returnString = entryName + " (unknown, created " +
           dateRepn.format(creationDate) + ")";
      }

      return returnString;
   }
}
