package org.astrogrid.mySpace.mySpaceManager;

import java.io.*;
import java.util.*;

import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatus;
import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatusCode;

/**
 * The <code>RegistryManager</code> class is used to access entries in
 * a MySpace registry.
 * 
 * <p>
 * The <code>RegistryManager</code> class provides the following functions:
 * </p>
 * <ul>
 *   <li>add an entry to the registry,</li>
 *   <li>update an entry in the registry,</li>
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
 * A method is provided to re-write the registry file when the as required.
 * 
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 3.
 */

public class RegistryManager
{  private static boolean DEBUG = true;
	
   private Map dataItemRecordHashMap = new HashMap (1100);
                           // Hash map holding the MySpace system registry.
   private String registryName;
                           // Name of the registry.
   private String registryFileName;
                           // Name of the file holding the registry.
   private String registryConfigFileName;
                           // Name of the registry configuration file.
   private int dataItemIDSeqNo;
                           // Sequence number for new dataItems.

   private int expiryPeriod = -1;
                           // Expiry period for the registry (days).

   private Vector serverNames = new Vector();
          // Names of the servers known to this MySpace registry.
   private Vector serverURIs = new Vector();
          // URIs of the servers known to this MySpace registry.
   private Vector serverDirectories = new Vector();
          // Base directories of the servers known to this MySpace registry.

//
// -- Constructors ---------------------------------------------------

/**
 * Constructor to create a <code>RegistryManager</code> object by reading
 * an existing registry file.  The argument passed to the constructor is
 * the name of this file.
 */

   public RegistryManager(String registryName)
   {  this.registryName = registryName;

      try
      {
//
//      Attempt to read the registry configuration file.

         registryConfigFileName = registryName + ".config";
         this.readConfigFile(registryConfigFileName);

//
//      Attempt to read the user details file for the registry.

//       TBD.

//
//      Attempt to read the file containing the MySpace registry.

         registryFileName = registryName + ".reg";
         this.readRegistryFile(registryFileName);
      }
      catch (Exception all)
      {  MySpaceStatus stat1  = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00100, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }
   }

// -------------------------------------------------------------------

/**
 * Constructor to create a <code>RegistryManager</code> object for a new,
 * empty registry.  The first argument is the name of the file to which
 * this registry will, in due course, be written.  The second argument
 * is a dummy, but is conventionally set to <code>"new"</code>.
 */

   public RegistryManager(String registryName, String dummy)
   {  this.registryName = registryName;

      try
      {
//
//      Attempt to read the registry configuration file.

         registryConfigFileName = registryName + ".config";
         this.readConfigFile(registryConfigFileName);

//
//      Attempt to read the user details file for the registry.

//       TBD.

//
//      Set the name of the (currently empty) file which will contain the
//      MySpace registry.

         registryFileName = registryName + ".reg";
         dataItemIDSeqNo = 0;
      }
      catch (Exception all)
      {  MySpaceStatus stat1  = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00100, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }
   }

// -------------------------------------------------------------------

/**
 * Dummy constructor with no arguments.
 */

   public RegistryManager()
   {  registryName = "";
      registryFileName = "";

      dataItemIDSeqNo = 0;
   }

//
// -- Methods --------------------------------------------------------

/**
 * The rewrite the registry file when required.
 */

   public void rewriteRegistryFile()
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
           MySpaceStatusCode.AGMMCE00101, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }
   }

// -------------------------------------------------------------------

/**
  * Read an existing Registry file.
  */

   private void readRegistryFile(String registryFileName)
   {

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
         dataItemIDSeqNo = 0;

//         System.out.println(" ");

         while (more == true)
         {  try
            {  DataItemRecord itemRec = (DataItemRecord)ois.readObject();
               count = count + 1;

//
//            Generate a key from the DataItem identifier and and the
//            DataItemRecord to the Hash Map.

               String key = "" + itemRec.getDataItemID();
               dataItemRecordHashMap.put(key, itemRec);

//               System.out.println("dataItemID, dataItemIDSeqNo: "
//                 + itemRec.getDataItemID() + " " + dataItemIDSeqNo);

               if (itemRec.getDataItemID() > dataItemIDSeqNo)
               {  dataItemIDSeqNo = itemRec.getDataItemID();
               }
            }
            catch (IOException e)
            {  more = false;
            }
            catch (ClassNotFoundException cnfe)
            {  more = false;
               MySpaceStatus stat1  = new MySpaceStatus(
                 MySpaceStatusCode.AGMMCE00102,
                 MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
                 this.getClassName() );
            }
         }

//         System.out.println("dataItemIDSeqNo: " + dataItemIDSeqNo);

//
//      Close the input file.

         ois.close();
      }
      catch (IOException e)
      {  MySpaceStatus stat1  = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00103, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }
   }

// -------------------------------------------------------------------

/**
  * Read the Registry configuration file.
  */

   private void readConfigFile(String configFileName)
   {

//
//   Try to read the file

      try
      {  File configFile = new File(configFileName);
         FileReader in = new FileReader(configFile);
         char c[] = new char[(int)configFile.length()];
         in.read(c);
         String s = new String(c);

//
//      Convert the input file into a vector of individual lines.

         Vector inputLines = new Vector();

         StringTokenizer token = new StringTokenizer(s, "\n");

         while(token.hasMoreTokens())
         {  inputLines.add(token.nextToken());
         }

//
//      Examine every line, decoding the details of the registry
//      configuration.

         String currentLine = "";
         int lineCount = 0;
         int numServers = 0;

         expiryPeriod = -1;

         Iterator iter = inputLines.iterator();
         while(iter.hasNext())
         {  lineCount = lineCount + 1;

            currentLine = (String)iter.next();

//            System.out.println("processing line " + lineCount 
//              + ": " + currentLine);

//
//         Remove any comments.  Remember that comments start with a
//         '#' character.

            int commentPos = currentLine.indexOf("#");
            if (commentPos > 0)
            {  currentLine = currentLine.substring(0, commentPos-1);
            }

//
//         Split the remaining line into a series of elements (or
//         'tokens') separated by blanks.  Proceed if there are any
//         such elements.

            StringTokenizer currentLineToken =
              new StringTokenizer(currentLine);

            int    currentElementCount = -1;
            Vector currentElements = new Vector();

            while(currentLineToken.hasMoreTokens())
            {  currentElementCount = currentElementCount + 1;

               String currentElem = currentLineToken.nextToken();
//               System.out.println(currentElem);

               currentElements.add(
                 currentElementCount, currentElem);
            }

            int numElements = currentElements.size();

            if (numElements > 0)
            {

//
//            Check if the first element corresponds to an expiry
//            date.

               if (((String)currentElements.get(0)).equals("expiryperiod"))
               {  if (numElements > 0)
                  {  Integer expiryBuffer = new
                       Integer((String)currentElements.get(1));
                     int expiryTemp = expiryBuffer.intValue();

                     if (expiryTemp > 0)
                     {  expiryPeriod = expiryTemp;
                     }
                     else
                     {  MySpaceStatus status  =
                          new MySpaceStatus(
                            MySpaceStatusCode.AGMMCW00153,
                            MySpaceStatusCode.WARN,
                            MySpaceStatusCode.NOLOG,
                            this.getClassName() );
                     }
                  }
                  else
                  {  MySpaceStatus status  =
                       new MySpaceStatus(
                         MySpaceStatusCode.AGMMCW00153,
                         MySpaceStatusCode.WARN,
                         MySpaceStatusCode.NOLOG,
                         this.getClassName() );
                  }
               }

//
//            Check if the first element corresponds to a server.

               if (((String)currentElements.get(0)).equals("server"))
               {  if (numElements > 3)
                  {  String currentServer = (String)currentElements.get(1);
                     String currentURI = (String)currentElements.get(2);
                     String currentDirectory =
                       (String)currentElements.get(3);

                     numServers = numServers + 1;

                     serverNames.add(currentServer);
                     serverURIs.add(currentURI);
                     serverDirectories.add(currentDirectory);
                  }
                  else
                  {  MySpaceStatus status  =
                       new MySpaceStatus(
                         MySpaceStatusCode.AGMMCW00151,
                         MySpaceStatusCode.WARN,
                         MySpaceStatusCode.NOLOG,
                         this.getClassName() );
                  }
               }
            }
         }

//
//      Check that an expiry period has been set and if not issue a
//      warning.

         if (expiryPeriod < 1)
         {  expiryPeriod = 30;

            MySpaceStatus status  =
              new MySpaceStatus(MySpaceStatusCode.AGMMCW00153,
                MySpaceStatusCode.WARN, MySpaceStatusCode.NOLOG,
                this.getClassName() );
         }

//
//      Check that at least one server has been specified, and if not
//      report an error.

         if (serverNames.size() < 1)
         {  MySpaceStatus status  =
              new MySpaceStatus(MySpaceStatusCode.AGMMCE00104,
                MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
                this.getClassName());
         }

         in.close();
      }

//
//   Note: any exception caught here is likely to be an I/O exception,
//   but other types are possible, so a catch-all exception is used.

      catch (Exception all)
      {  MySpaceStatus status = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00104, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );

//         System.out.println("Cannot read input file.");
//         ex.printStackTrace();
      }
   }

// -------------------------------------------------------------------

/**
 * Return the expiry period of the registry (in days).
 */

   public int getExpiryPeriod()
   {  return expiryPeriod;
   }

// -------------------------------------------------------------------

/**
 * Return the Vector of server names.
 */

   public Vector getServerNames()
   {  return serverNames;
   }

// -------------------------------------------------------------------

/**
 * Check whether a given name is the name of a server.  A value of
 * true is returned if the name is valid; otherwise false is returned.
 */

   public boolean isServerName(String serverName)
   {  boolean isName;

      int serverIndex = serverNames.indexOf(serverName);

      if (serverIndex > -1)
      {  isName = true;
      }
      else
      {  isName = false;
      }

      return isName;
   }

// -------------------------------------------------------------------

/**
 * Return the base URI for a given server.
 */

   public String getServerURI(String serverName)
   {  String serverURI;

      int serverIndex = serverNames.indexOf(serverName);

      if (serverIndex > -1)
      {  serverURI = (String)serverURIs.get(serverIndex);
      }
      else
      {  serverURI = null;
      }

      return serverURI;
   }

// -------------------------------------------------------------------

/**
 * Return the base directory for a given server.
 */

   public String getServerDirectory(String serverName)
   {  String serverDirectory;

      int serverIndex = serverNames.indexOf(serverName);

      if (serverIndex > -1)
      {  serverDirectory = (String)serverDirectories.get(serverIndex);
      }
      else
      {  serverDirectory = null;
      }

      return serverDirectory;
   }

// -------------------------------------------------------------------

/**
  * Return the sequence number to be used for a new DataItemRecord.
  */

   public int getNextDataItemID()
   {  dataItemIDSeqNo = dataItemIDSeqNo + 1;
      return dataItemIDSeqNo;
   }

// -------------------------------------------------------------------

/**
 * Add a <code>DataItemRecord</code> to the registry.
 */

   public boolean addDataItemRecord(DataItemRecord dataItemRecord)
   {  boolean status = false;

      try
      {
//
//      Generate the key from dataItemRecord's dataItemID.

         String key = "" + dataItemRecord.getDataItemID();

//
//      Check that a DataItemRecord with this key does not already
//      exist, and if not add dataItemRecord to the Hash Map holding the
//      registry.

         if (dataItemRecordHashMap.containsKey(key))
         {  status = false;
         }
         else
         {  dataItemRecordHashMap.put(key, dataItemRecord);
            status = true;
         }

//         System.out.println("Status from addDataItemRecord: "
//           + status + "  key = " + key);

      }
      catch (Exception all)
      {  MySpaceStatus exStatus = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00106, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }

      return status;
   }
// -------------------------------------------------------------------

/**
 * Update a <code>DataItemRecord</code> in the registry.  The given
 * new <code>DataItemRecord</code> is used to replace an existing
 * <code>DataItemRecord</code> in the registry with the same key.
 */

   public boolean updateDataItemRecord(DataItemRecord dataItemRecord)
   {  boolean status = false;

      try
      {
//
//      Generate the key from dataItemRecord's dataItemID.

         String key = "" + dataItemRecord.getDataItemID();

//
//      Check that a DataItemRecord with this key already exists.  If
//      so then delete it and insert the new DataItemRecord.

         if (dataItemRecordHashMap.containsKey(key))
         {  dataItemRecordHashMap.remove(key);
            dataItemRecordHashMap.put(key, dataItemRecord);
            status = true;
         }
         else
         {  status = false;
         }

//         System.out.println("Status from updateDataItemRecord: "
//           + status + "  key = " + key);

      }
      catch (Exception all)
      {  MySpaceStatus exStatus = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00106, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }

      return status;
   }

// -------------------------------------------------------------------

/**
 * Delete a <code>DataItemRecord</code> from the registry.  The
 * <code>DataItemRecord</code> to be deleted is specified by its
 * identifier, which is passed as the argument.
 */

   public boolean deleteDataItemRecord(int dataItemID)
   {  boolean status = false;

      try
      {

//
//      Generate the key from the dataItemID.

         String key = "" + dataItemID;

//
//      Check that a DataItemRecord with this key exists and then delete
//      the corresponding entry from the Hash Map holding the registry.

//         System.out.println("key to be removed: " + key);

         if (dataItemRecordHashMap.containsKey(key))
         {  dataItemRecordHashMap.remove(key);
//            System.out.println(" ... key removed.");
            status = true;
         }
         else
         {  status = false;
         }
      }
      catch (Exception all)
      {  MySpaceStatus exStatus = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00106, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }

      return  status;
   }

// -------------------------------------------------------------------

/**
 * Lookup a <code>DataItemRecord</code> in the registry.  The
 * <code>DataItemRecord</code> to be deleted is specified by its
 * identifier, which is passed as the argument.  If the specified
 * <code>DataItemRecord</code> could not be found then a null
 * <code>DataItemRecord</code> is returned.
 */

   public DataItemRecord lookupDataItemRecord(int dataItemID)
   {  DataItemRecord itemRecord = new DataItemRecord();
      itemRecord = null;

      try
      {

//
//      Generate the key from the dataItemID.

         String key = "" + dataItemID;

//
//      Check that a <code>DataItemRecord</code> with this key exists and if
//      so then look it up in the Hash Map holding the registry.  If the key
//      does not exist the return a null <code>DataItemRecord</code>.

         if (dataItemRecordHashMap.containsKey(key))
         {  itemRecord = (DataItemRecord)dataItemRecordHashMap.get(key);
         }
         else
         {  itemRecord = null;
         }
      }
      catch (Exception all)
      {  MySpaceStatus status = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00106, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }

      return itemRecord;
   }

// -------------------------------------------------------------------

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

      Comparator comparator = new DataItemRecordComparator();
      Set itemRecords = new TreeSet(comparator);
      Vector returnItemRecords = new Vector();

      int count = 0;

      try
      {

//
//      Check that the input string is not blank.

         int len = dataHolderNameExpr.length();

         if (len > 0)
         {

//
//         Check whether the given expression includes a terminating wild-card
//         and assemble the comparison string that will be checked against the
//         the entries in the registry.  If the input string does not
//         contain a terminating wild-card then it is simply copied.  If it
//         does then the portion of the string before the wild-card is copied.

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
//         Obtain the key set for the Registry Hash Map.

            Set keys = dataItemRecordHashMap.keySet();

//
//         Use the keys to examine every object.

            for (Iterator keyIter = keys.iterator(); keyIter.hasNext(); )
            {  String key =  (String)keyIter.next();
               DataItemRecord itemRec =
                 (DataItemRecord)dataItemRecordHashMap.get(key);

//
//            Get the name for the current DataItemRecord.

               String dataItemName = itemRec.getDataItemName();

//
//            Check the name against the comparison string.  If it matches
//            then add the current DataItemRecord to the (alphabetically
//            sorted) TreeSet holding the list of matches.

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

//
//      Copy the entries in the alphabetically sorted TreeSet into the
//      the return Vector.  Consequently, the elements in the Vector are
//      also alphabetically sorted.

         for(Iterator iter = itemRecords.iterator();  iter.hasNext(); )
         {  returnItemRecords.add(iter.next());
         }
      }
      catch (Exception all)
      {  MySpaceStatus status = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00106, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }

      return returnItemRecords;
   }

/**
 * Obtain the name of the current Java class.
 */

   protected String getClassName()
   { Class currentClass = this.getClass();
     String name =  currentClass.getName();
     int dotPos = name.lastIndexOf(".");
     if (dotPos > -1)
     {  name = name.substring(dotPos+1, name.length() );
     }

     return name;
   }    
}