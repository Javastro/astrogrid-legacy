package org.astrogrid.mySpace.mySpaceManager;

import java.io.*;
import java.util.*;

import org.astrogrid.mySpace.mySpaceStatus.*;

/**
 * The <code>RegistryManager</code> class is used to access entries in
 * a MySpace registry.
 * 
 * <p>
 * The <code>RegistryManager</code> class provides the following functions:
 * </p>
 * <ul>
 *   <li>add an entry to the registry,</li>
 *   <li>delete an entry from the registry,</li>
 *   <li>lookup a single entry in the registry,</li>
 *   <li>lookup a set of named entries in the registry.</li>
 * </ul>
 * <p>
 * The <code>RegistryManager</code> class is quite low-level.  It merely
 * reads and writes entries in the registry.  It does not check the entries
 * for consistency.  For example, if an entry is being added it does not
 * check that the DataHolder name corresponds to a container which already
 * already exists.  Such checks are assumed to have already been performed
 * at a higher level.
 * </p>
 * <p>
 * In Iteration 2 the registry is a simple file.  This file is read when a
 * <code>RegistryManager</code> object is created and is re-written when
 * the object is no longer required.  Two constructors are supplied.  The 
 * simplest has a single argument: the name of an existing registry file,
 * which it reads.  The second constructor has two arguments and is used
 * when a new registry file is to be created.  The first argument is the
 * name of this file.  The second argument is a dummy, but is
 * conventionally set to <code>"new"</code>.
 * </p>
 * <p>
 * A finalizer is provided to re-write the registry file when the registry
 * manager is no longer required.
 * 
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 2.
 */

public class RegistryManager
{  private Map dataItemRecordHashMap = new HashMap ();
                           // Hash map holding the MySpace system registry.
   private String registryFileName;
                           // Name of the file holding the registry.

//
// Constructors.

/**
 * Constructor to create a <code>RegistryManager</code> object by reading
 * an existing registry file.  The argument passed to the constructor is
 * the name of this file.
 */

   public RegistryManager(String registryFileName)
   {  this.registryFileName = registryFileName;

//
//   Attempt to open the file containing the MySpace registry and set
//   for reading and de-serialising from it.

      try
      {  File registryFile = new File(registryFileName);
         FileInputStream ris = new FileInputStream(registryFile);
         ObjectInputStream ois = new ObjectInputStream(ris);

//
//      Read and de-serialise DataItemRecord from the input file.

         boolean more = true;
         int count = 0;

         while (more == true)
         {  try
            {  DataItemRecord itemRec = (DataItemRecord)ois.readObject();
               count = count + 1;

//
//            Generate a key from the DataItem identifier and and the
//            DataItemRecord to the Hash Map.

               String key = "" + itemRec.getDataItemID();
               dataItemRecordHashMap.put(key, itemRec);
            }
            catch (IOException e)
            {  more = false;
            }
            catch (ClassNotFoundException cnfe)
            {  more = false;
               MySpaceStatus stat1  = new MySpaceStatus(
                 "Registry file " + registryFileName + " is corrupt.",
                 "e");
            }
         }

//
//      Close the input file.

         ois.close();
      }
      catch (IOException e)
      {  MySpaceStatus stat1  = new MySpaceStatus(
           "Failure reading registry file " + registryFileName + ".",
           "e");
      }
   }

/**
 * Constructor to create a <code>RegistryManager</code> object for a new,
 * empty registry.  The first argument is the name of the file to which
 * this registry will, in due course, be written.  The second argument
 * is a dummy, but is conventionally set to <code>"new"</code>.
 */

   public RegistryManager(String registryFileName, String dummy)
   {  this.registryFileName = registryFileName;
   }

/**
 * Dummy constructor with no arguments.
 */

   public RegistryManager()
   {  registryFileName = "";
   }

//
// Finaliser.

/**
 * The finalizer writes out the registry file when the
 * <code>RegistryManager</code> is no longer required.
 */

   public void finalize()
   {

//
//   Attempt to re-open the file containing the MySpace registry and set
//   for serialising and writing to it.

      try
      {  File registryFile = new File(registryFileName);
         FileOutputStream ros = new FileOutputStream(registryFile);
         ObjectOutputStream oos = new ObjectOutputStream(ros);

//
//      While there are more DataItemRecords in the Hash Map obtain
//      the next one, serialise it and write it to the output file.

         Set keys = dataItemRecordHashMap.keySet();

         for (Iterator keyIter = keys.iterator(); keyIter.hasNext(); )
         {  String key =  (String)keyIter.next();
            DataItemRecord itemRec =
              (DataItemRecord)dataItemRecordHashMap.get(key);
            oos.writeObject(itemRec);
         }

//
//      Close the output file.

         oos.close();
      }
      catch (IOException e)
      {  MySpaceStatus stat1  = new MySpaceStatus(
           "Failure writing registry file " + registryFileName + ".",
           "e");
      }
   }

/**
 * Add a <code>DataItemRecord</code> to the registry.
 */

   public boolean addDataItemRecord(DataItemRecord dataItemRecord)
   {

//
//   Generate the key from dataItemRecord's dataItemID.

      String key = "" + dataItemRecord.getDataItemID();

//
//   Check that a DataItemRecord with this key does not already
//   exist, and if not add dataItemRecord to the Hash Map holding the
//   registry.

      boolean status;

      if (dataItemRecordHashMap.containsKey(key))
      {  status = false;
      }
      else
      {  dataItemRecordHashMap.put(key, dataItemRecord);
         status = true;
      }

      return status;
   }

/**
 * Delete a <code>DataItemRecord</code> from the registry.  The
 * <code>DataItemRecord</code> to be deleted is specified by its
 * identifier, which is passed as the argument.
 */

   public boolean deleteDataItemRecord(int dataItemID)
   {

//
//   Generate the key from the dataItemID.

      String key = "" + dataItemID;

//
//   Check that a DataItemRecord with this key exists and then delete
//   the corresponding entry from the Hash Map holding the registry.

      boolean status;

      if (dataItemRecordHashMap.containsKey(key))
      {  dataItemRecordHashMap.remove(key);
         status = true;
      }
      else
      {  status = false;
      }

      return  status;
   }

/**
 * Lookup a <code>DataItemRecord</code> in the registry.  The
 * <code>DataItemRecord</code> to be deleted is specified by its
 * identifier, which is passed as the argument.  If the specified
 * <code>DataItemRecord</code> could not be found then a null
 * <code>DataItemRecord</code> is returned.
 */

   public DataItemRecord lookupDataItemRecord(int dataItemID)
   {

//
//   Generate the key from the dataItemID.

      String key = "" + dataItemID;

//
//   Check that a <code>DataItemRecord</code> with this key exists and if
//   so then look it up in the Hash Map holding the registry.  If the key
//   does not exist the return a null <code>DataItemRecord</code>.

      DataItemRecord itemRecord = new DataItemRecord();

      if (dataItemRecordHashMap.containsKey(key))
      {  itemRecord = (DataItemRecord)dataItemRecordHashMap.get(key);
      }
      else
      {  itemRecord = null;
      }
      
      return itemRecord;
   }

/**
 * Lookup the <code>DataItemRecord</code>s in the registry which match a
 * given expression.  This expression comprises a DataHolder name which
 * can optionally contain a wild-card.  The wild-card character is an
 * asterisk (`*').  In Iteration 2 the wild-card can only occur at the
 * end of the expression.
 */

   public Vector lookupDataItemRecords(String dataHolderNameExpr)
   {

//
//   Initialise the returned vector of DataItemRecords.

      Vector itemRecords = new Vector();
      int count = 0;

//
//   Check that the input string is not blank.

      int len = dataHolderNameExpr.length();

      if (len > 0)
      {

//
//      Check whether the given expression includes a terminating wild-card
//      and assemble the comparison string that will be checked against the
//      the entries in the registry.  If the input string does not
//      contain a terminating wild-card then it is simply copied.  If it
//      does then the portion of the string before the wild-card is copied.

         boolean wildCard;
         String comparisonString;

         if (dataHolderNameExpr.endsWith("*") == true)
         {  wildCard = true;
            comparisonString = dataHolderNameExpr.substring(0, len-1);
         }
         else
         {  wildCard = false;
            comparisonString = dataHolderNameExpr;
         }

//
//      Obtain the key set for the Registry Hash Map.

         Set keys = dataItemRecordHashMap.keySet();

//
//      Use the keys to examine every object.

         for (Iterator keyIter = keys.iterator(); keyIter.hasNext(); )
         {  String key =  (String)keyIter.next();
            DataItemRecord itemRec =
              (DataItemRecord)dataItemRecordHashMap.get(key);

//
//         Get the name for the current DataItemRecord.

            String dataItemName = itemRec.getDataItemName();

//
//         Check the name against the comparison string.  If it matches
//         then add the current DataItemRecord to the return vector.

            if (!wildCard)
            {  if (dataItemName.equals(comparisonString))
               {  itemRecords.add(itemRec);
               }
            }
            else
            {  if (dataItemName.startsWith(comparisonString))
               {  itemRecords.add(itemRec);
               }
            }
         }
      }

      return itemRecords;
   }
}
