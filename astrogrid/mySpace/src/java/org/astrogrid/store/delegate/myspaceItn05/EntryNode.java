package org.astrogrid.store.delegate.myspaceItn05;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.text.*;

import org.astrogrid.store.delegate.StoreFile;
import org.astrogrid.store.Agsl;

/**
 * A <code>EntryNode</code> object stores the details held in a
 * MySpace registry for a single entry in a MySpace Service (that is, a
 * file, perhaps typically a VOTable).  The details held are things
 * like: the MySpace name of the entry, the identifier of the user who
 * owns it, its creation date etc.
 *
 * <p>
 * If the <code>EntryNode</code> corresponds to a container then it
 * will contain a list of any child <code>EntryNode</code>s.  Thus the
 * <code>EntryNode</code> class is hierarchical and a given
 * <code>EntryNode</code> will contain within itself an entire tree of
 * any children.
 * </p>
 *
 * @author A C Davenhall (Edinburgh)
 * @since Iteration 5.
 * @version Iteration 5.
 */

public class EntryNode implements StoreFile
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

   private ArrayList children = new ArrayList();
   private EntryNode parent = null;

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

   public EntryNode (String entryName,  int entryId,
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

      parent = null;
   }


/**
 * Create a <code>EntryNode</code> object from the equivalent
 * <code>EntryResults</code> object.
 */

   public EntryNode (EntryResults entryResults)
   {  this.entryName = entryResults.getEntryName();
      this.entryId = entryResults.getEntryId();
      this.entryUri = entryResults.getEntryUri();
      this.ownerId = entryResults.getOwnerId();
      this.creationDate = new Date( entryResults.getCreationDate() );
      this.expiryDate = new Date( entryResults.getExpiryDate() );
      this.size = entryResults.getSize();
      this.type = entryResults.getType();
      this.permissionsMask = entryResults.getPermissionsMask();

      parent = null;
   }


/**
 * Create a <code>EntryNode</code> object from the equivalent
 * <code>EntryResults</code> object and set the parent.
 */

   public EntryNode (EntryResults entryResults, EntryNode parent)
   {  this.entryName = entryResults.getEntryName();
      this.entryId = entryResults.getEntryId();
      this.entryUri = entryResults.getEntryUri();
      this.ownerId = entryResults.getOwnerId();
      this.creationDate = new Date( entryResults.getCreationDate() );
      this.expiryDate = new Date( entryResults.getExpiryDate() );
      this.size = entryResults.getSize();
      this.type = entryResults.getType();
      this.permissionsMask = entryResults.getPermissionsMask();

      this.parent = parent;
   }


/**
 * Constructor with no arguments.  All the member variables are set to
 * null (or -1 in the case of <code>int</code>s).
 */

   public EntryNode ()
   {  this.entryName = null;
      this.entryId = -1;
      this.entryUri = null;
      this.ownerId = null;
      this.creationDate = null;
      this.expiryDate = null;
      this.size = -1;
      this.type = EntryCodes.UNKNOWN;
      this.permissionsMask = null;

      parent = null;
   }


/**
 * Create a <code>EntryNode</code> object from a list of EntryResults.
 */

   public EntryNode (String path, ArrayList entries)
   {  
//
//   Create a fake root node corresponding to the path.

//      System.out.println("orig path: " + path);
      int pathLen = path.length();
      int trailAst = path.lastIndexOf("*");
      if (trailAst == pathLen-1)
      {  int lastSep = path.lastIndexOf("/");
//         System.out.println("lastSep " + lastSep);
         path = path.substring(0, lastSep+1);
      }

      pathLen = path.length();
      int trailSep = path.lastIndexOf("/");
      if (trailSep == pathLen-1  && pathLen > 1)
      {  path = path.substring(0, pathLen-1);
      }

//      System.out.println("fixed path: " + path);

      this.entryName = path;
      this.entryId = -1;
      this.entryUri = null;
      this.ownerId = null;
      this.creationDate = new Date();
      this.expiryDate = new Date();
      this.size = -1;
      this.type = EntryCodes.CON;
      this.permissionsMask = null;


//      for (int loop=0; loop<entries.size(); loop++)
//      {  EntryResults temp = (EntryResults)entries.get(loop);
//           System.out.println(loop + " " +
//             temp.getEntryName() );
//      }

//
//   Set both the root and current element to this element.

      parent = null;

      EntryNode root = this;
      EntryNode currentEntry = this;
//      System.out.println("top name: " + currentEntry.getName() );

      ArrayList parentEntries = new ArrayList();
      parentEntries.add(null);

//
//   Grow the tree.

      boolean more = true;
      int currentIndex = -1;
      EntryNode newEntry = null;

      while (more)
      {
//
//      Attempt to obtain the next entry from the list.

//         System.out.println("currentIndex, size: " +
//           currentIndex + " " + entries.size() );
         if (currentIndex < entries.size() - 1)
         {  currentIndex = currentIndex + 1;
            EntryNode nextEntry = new
              EntryNode( (EntryResults)entries.get(currentIndex),
                 currentEntry);

//            System.out.println("current, next: " +
//              currentEntry.getName() +" " + nextEntry.getName() );

            if (this.isChild(nextEntry, currentEntry) )
            {  currentEntry.addChild(nextEntry);
               parentEntries.add(currentEntry);
               currentEntry = nextEntry;
               entries.remove(currentIndex);

               currentIndex = -1;
            }
         }
         else
         {  currentIndex = -1;
            currentEntry = 
              (EntryNode)parentEntries.get(parentEntries.size() -1);
            parentEntries.remove(parentEntries.size() -1);

            if (entries.size() < 1)
            {  more = false;
            }
         }
      }

   }

/**
 * Determine whether first entry is a child of the second entry.
 */

  private boolean isChild(EntryNode firstEntry, EntryNode secondEntry)
  {  boolean child = false;

     String firstName = firstEntry.getName();
     String secondName = secondEntry.getName();

     int lastPos = firstName.lastIndexOf("/");

     if ((secondName.equals("/") || secondName.equals("/*"))
            && (lastPos == 0) )
     {  child = true;
     }
     else
     {  String firstParent = firstName.substring(0, lastPos);
//        System.out.println("firstParent: " + firstParent);

        if (firstParent.equals(secondName) )
        {  child = true;
        }
     }

//     System.out.println("first, second, child: " + firstName + " " +
//      secondName + " " + child);

     return child;
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
   {  return parent;
   }
   
/**
 * Returns true if this EntryNode is a container which can hold other
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
 * Return true if this <code>EntryNode</code> is a self-contained file.
 * For example, a database table might be represented as a StoreFile but
 * it is not a file.  A value of false is always returned because currently
 * MySpace cannot handle database tables.
 */
   public boolean isFile()
   {  return false;
   }
   
/**
 * If the <code>EntryNode</code> is a container then list all its
 * children.  Note that only direct child <code>EntryNode</code>s
 * are listed, not grand-children or great-grandchildren.
 */

   public StoreFile[] listFiles()
   {  StoreFile[] childArray = null;

      if (children != null)
      {  if (children.size() > 0)
         {  childArray = new EntryNode[children.size()];

            for (int loop=0; loop<children.size(); loop++)
            {  childArray[loop] = (StoreFile)children.get(loop);
            }
         }
      }

      return childArray;
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
 * Return where to find this file using an AstroGrid Store Locator.
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
// The EntryNode class has a get method for every member variable.

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

/**
 * Return the short name of the entry (ie. the name without any
 * preceding container path).
 */

   public String getShortName()
   {  String shortName = "";

      int lastSep = entryName.lastIndexOf("/");
      if (lastSep>-1)
      {  int stop = entryName.length();
         shortName = entryName.substring(lastSep, stop);
      }
      else
      {  shortName = entryName;
      }

      return shortName;
   }
   

// ----------------------------------------------------------------------

//
// Methods for manipulating children.

/**
 * Add a child node.
 */

   private void addChild(EntryNode child)
   {  // System.out.println("type: " + type);

      if (type == EntryCodes.CON)
      {  children.add(child);
//         System.out.println("added...");
      }
   }


/**
 * Get the Nth child of the current Node.
 */

   public EntryNode getChild(int n)
   {  EntryNode child = new EntryNode();

//      System.out.println("n, cildren.size(): " +
//         n + " " + children.size() );
      if (n < children.size() )
      {  child = (EntryNode)children.get(n);
//         System.out.println("child name: " + child.getName() );
      }
      else
      {  child = null;
      }

      return child;
   }


/**
 * Get the number of children.
 */

  public int getNumChildren()
  {  int numChild = 0;
     try
     {  if (type  == EntryCodes.CON)
        {  numChild = children.size();
        }
        else
        {  numChild = 0;
        }
     }
     catch (Exception e)
     {  numChild = 0;
     }

     return numChild;

  }


// ----------------------------------------------------------------------

//
// Other methods.

/**
 * Produce a reasonable string representation of an entry.
 */

   public String toString()
   {  String returnString;

      if (type != EntryCodes.CON)
      {  returnString = entryName;
      }
      else
      {  StringBuffer buffer = new StringBuffer(
           entryName + " (container)" );

         int level = 1;
         boolean more = true;

         int numChildren = this.getNumChildren();
         int currentChild = 0;

//         EntryNode currentEntry = new EntryNode();
         EntryNode currentEntry = this;

         ArrayList parentEntries = new ArrayList();
         parentEntries.add(null);
         ArrayList parentCurChild  = new ArrayList();
         parentCurChild.add(null);

         if (numChildren < 1)
         {  more = false;
         }

//         System.out.println("numChildren, more: " + numChildren 
//           + " " + more);

         while (more)
         {  numChildren = currentEntry.getNumChildren();
            if (currentChild < numChildren)
            {  
               parentEntries.add(currentEntry);
               parentCurChild.add(new Integer(currentChild));

//               System.out.println("  new name, currentChild" + 
//                  currentEntry.getName() + " " + currentChild);
               currentEntry = currentEntry.getChild(currentChild);
//               System.out.println("  new name" + currentEntry.getName() );
               currentChild = 0;
               level = level + 1;

               String currentLine = "\n";
               for (int loop=0; loop<level-1; loop++)
               {  currentLine = currentLine + "  ";
               }
               currentLine = currentLine + currentEntry.getShortName();
               if (currentEntry.getType() == EntryCodes.CON)
               {  currentLine = currentLine + " (container)";
               }
               buffer.append(currentLine);
//               System.out.println("\n>>> " + buffer.toString() );

            }
            else
            {  boolean ascend = true;

               while (ascend)
               {  currentEntry = (EntryNode)parentEntries.get(
                   parentEntries.size() -1);
                  parentEntries.remove(parentEntries.size() -1);

                  if (currentEntry != null)
                  {  Integer ccc = (Integer)parentCurChild.get(
                       parentCurChild.size() -1);
                     currentChild = ccc.intValue();
                     parentCurChild.remove(parentCurChild.size() -1);

                     level = level - 1;

                     numChildren = currentEntry.getNumChildren();
                     if (currentChild < numChildren)
                     {  ascend = false;
                        currentChild = currentChild + 1;
                     }
                  }
                  else
                  {  ascend = false;
                     more = false;
                  }
               }
            }
         }
   
         returnString = buffer.toString();
      }

      return returnString;
   }
}
