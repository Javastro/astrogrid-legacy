
package org.astrogrid.mySpace.mySpaceManager;

import java.sql.*;
import java.util.*;

import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatus;
import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatusCode;

/**
 * The <code>RegistryManager</code> class is used to access entries in
 * a MySpace registry and to access details of the servers in the MySpace
 * system.
 * 
 * <p>
 * The following functions are provided to access registry entries:
 * </p>
 * <ul>
 *   <li>add an entry to the registry,</li>
 *   <li>update an entry in the registry,</li>
 *   <li>delete an entry from the registry,</li>
 *   <li>look up a single entry in the registry,</li>
 *   <li>look up a set of named entries in the registry.</li>
 * </ul>
 * <p>
 * The operations are performed at quite a low-level: the entries are
 * merely read from or written to the registry.  No checks for logical
 * consistency are made.  For example, if an entry is being added there is
 * no check that the DataHolder name corresponds to a container which already
 * already exists.  Such checks are assumed to have already been performed
 * at a higher level.
 * </p>
 * <p>
 * The following functions are provided to access the servers known to the
 * MySpace system:
 * </p>
 * <ul>
 *   <li>obtain the names of all the servers in the MySpace system,</li>
 *   <li>check whether a server with a given name exists in the MySpace
 *    system,</li>
 *   <li>obtain the expiry period, in days, of a given server,</li>
 *   <li>obtain the URI of a given server,</li>
 *   <li>obtain the base directory of a given server.</li>
 * </ul>
 * <p>
 * There are no methods to update the details of a server or to add or
 * delete a server.
 * </p>
 * <p>
 * Constructors to either access an existing registry or to create a new
 * registry are supplied.  In addition there is a dummy constructor with
 * no arguments.
 * </p>
 * <p>
 * The registry entries and server details are held as two tables in a
 * DBMS and accessed using JDBC.
 * </p>
 * 
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 4.
 */

public class RegistryManager
{  private boolean DEBUG = false;

   private String jdbcDriverClass = "org.hsqldb.jdbcDriver";

   private String registryName;
                           // Name of the registry.
   private String registryDBName;
                           // Name of the database holding the registry.


//
// -- Constructors ---------------------------------------------------

/**
 * Constructor to create a <code>RegistryManager</code> object for an
 * existing, populated registry.
 *
 * @param registryName The name of the registry.
 */

   public RegistryManager(String registryName)
   {  this.registryName = registryName;
      registryDBName = "jdbc:hsqldb:" + registryName + ".db";
   }

// -------------------------------------------------------------------

/**
 * Constructor to create a <code>RegistryManager</code> object for a new,
 * empty registry.
 *
 * @param registryName The name of the new registry.
 * @param servers Vector of <code>ServerDetails</code> containing the
 *   details of the servers in the MySpace system.
 */

   public RegistryManager(String registryName, Vector servers)
   {  this.registryName = registryName;
      registryDBName = "jdbc:hsqldb:" + registryName + ".db";

      try
      {
//
//      Assemble the SQL statement to create the registry table.

         String sqlStatement = "CREATE TABLE reg(" +
           "dataItemName VARCHAR(150), " +
           "dataItemID INTEGER IDENTITY, " +
           "dataItemFile VARCHAR(15), " +
           "ownerID VARCHAR(20), " +
           "creationDate DATE, " +
           "expiryDate DATE, " +
           "size INTEGER, " +
           "type INTEGER,  " +
           "permissionsMask VARCHAR(20) )";

//
//      Attempt to execute the SQL statement.

         Vector dbvec = this.transact(sqlStatement, true);
         if (dbvec == null)
         {  throw new Exception("JDBC error : " + sqlStatement);
         }

//
//      Assemble the SQL statement to create the servers table.

         sqlStatement = "CREATE TABLE servers(" +
           "name VARCHAR(15), " +
           "expiryPeriod INTEGER, " +
           "URI VARCHAR(50), " +
           "directory VARCHAR(50) )";

//
//      Attempt to execute the SQL statement.

         dbvec = this.transact(sqlStatement, true);
         if (dbvec == null)
         {  throw new Exception("JDBC error : " + sqlStatement);
         }

//
//      Attempt to add an entry for every server to the servers table.
//      For each server attempt to assemble an SQL statement and execute
//      it.

         if (servers.size() > 0)
         {  for (int loop = 0; loop < servers.size(); loop++)
            {  ServerDetails server = (ServerDetails)servers.elementAt(loop);

               sqlStatement = "INSERT INTO servers(" +
                 "name, expiryPeriod, URI, directory)" +
                 "VALUES (" +
                 "'" + server.getName() + "', " +
                 server.getExpiryPeriod() + ", " +
                 "'" + server.getURI() + "', " +
                 "'" + server.getDirectory() + "')"; 

//
//            Attempt to execute the SQL statement.

               dbvec = this.transact(sqlStatement, true);
               if (dbvec == null)
               {  throw new Exception("JBDC error : " + sqlStatement);
               }
            }
         }
      }
      catch (Exception all)
      {  if (DEBUG) all.printStackTrace();

         MySpaceStatus exStatus = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00106, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }
   }

// -------------------------------------------------------------------

/**
 * Dummy constructor with no arguments.
 */

   public RegistryManager()
   {  registryName = null;
      registryDBName = null;
   }

//
// -- Methods --------------------------------------------------------

/**
 * Add a <code>DataItemRecord</code> to the registry.
 *
 * @param dataItemRecord The <code>DataItemRecord</code> to be added to
 *   the registry.  Any <code>DataItemID</code> that has been set will
 *   be ignored (<code>addDataItemRecord</code> will itself allocate
 *   a <code>DataItemID</code>).
 * @return The <code>DataItemID</code> allocated for the
 *   <code>DataItemRecord</code>.
 */

   public DataItemRecord addDataItemRecord(DataItemRecord dataItemRecord)
   {  DataItemRecord returnDataItem = new DataItemRecord();

      int dataItemID = -1;
      String dataItemFile = "";

      try
      {
//
//      Assemble the SQL statement to add the dataItemRecord.
//      But first convert dates in the java.util.Date format to the
//      java.sql.Date formats.
//
//      Note that the dataItemID column is not added because a unique
//      value is generated internally by the database.  However, the
//      code that would be used to add this column is left in as
//      comments, for purposes of illustration.
//
//      Also note that the expiry date is generated from the creation
//      date by adding the expiry period for the server.

         java.util.Date creationDate = dataItemRecord.getCreationDate();
         long creationTime = creationDate.getTime();
         java.sql.Date sqlCreation = new java.sql.Date(creationTime);

         String serverName = dataItemRecord.getServer();
         int expiryPeriod;
         if (serverName != "")
         {  expiryPeriod = this.getServerExpiryPeriod(
              dataItemRecord.getServer() );
         }
         else
         {  expiryPeriod = 0;
         }

         Calendar cal = Calendar.getInstance();
         cal.setTime(creationDate);
         cal.add(Calendar.DATE, expiryPeriod);
         java.util.Date expiryDate = cal.getTime();

         long expiryTime = expiryDate.getTime();
         java.sql.Date sqlExpiry = new java.sql.Date(expiryTime);

         String sqlStatement = "INSERT INTO reg(" +
           "dataItemName, " +
//            dataItemID, 
//            dataItemFile, 
           "ownerID, " +
           "creationDate, expiryDate, " +
           "size, type, permissionsMask) " +
           "VALUES (" +
            "'" + dataItemRecord.getDataItemName() + "', " +
//            "'" + dataItemRecord.getDataItemID() + "', " +
//            "'" + dataItemRecord.getDataItemFile() + "', " +
            "'" + dataItemRecord.getOwnerID() + "', " +
            "'" + sqlCreation + "', " +
            "'" + sqlExpiry + "', " +
            dataItemRecord.getSize() + ", " +
            dataItemRecord.getType() + ", " +
            "'" + dataItemRecord.getPermissionsMask() + "')"; 

//
//      Attempt to execute the SQL statement.

         Vector dbvec = this.transact(sqlStatement, true);
         if (dbvec == null)
         {  throw new Exception("JDBC error : " + sqlStatement);
         }

//
//      Assemble the SQL statement to retrieve the new DataItemRecord.

         sqlStatement = "SELECT * FROM reg WHERE (dataItemName='" +
           dataItemRecord.getDataItemName() + "')";

//
//      Attempt to execute this SQL statement.

         dbvec = this.transact(sqlStatement, false);

//
//      Attempt to retrieve the dataItemID from the returned
//      DataItemRecord.

         DataItemRecord newRec = new DataItemRecord();

         if (dbvec != null)
         {  if (dbvec.size() == 1)
            {  newRec = (DataItemRecord)dbvec.elementAt(0);
               dataItemID = newRec.getDataItemID();
            }
            else
            {  throw new Exception("JDBC error : " + sqlStatement);
            }
         }
         else
         {  throw new Exception("JDBC error : " + sqlStatement);
         }

//
//      Attempt to generate the file name (dataItemFile) corresponding
//      to the dataItemID and then update the record in the database
//      to contain this name.
//
//      Note that if the dataItemRecord corresponds to a container
//      then dataItemFile is set to "none" (because there is no
//      corresponding file in this case.

         if (dataItemID > -1)
         {  if (dataItemRecord.getType() != DataItemRecord.CON)
            {  dataItemFile = "f" + dataItemID;
            }
            else
            {  dataItemFile = "none";
            }

            sqlStatement = "UPDATE reg SET " +
              "dataItemFile = '" + dataItemFile + "' " +
              "WHERE (dataItemID=" + dataItemID + ")";

            dbvec = this.transact(sqlStatement, true);
            if (dbvec == null)
            {  throw new Exception("JDBC error : " + sqlStatement);
            }
         }

//
//      Assemble the return dataItemRecord.

         if (dataItemID > -1)
         {  returnDataItem = newRec;
            returnDataItem.setDataItemFile(dataItemFile);
         }
      }
      catch (Exception all)
      {  returnDataItem = null;

         if (DEBUG) all.printStackTrace();

         MySpaceStatus exStatus = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00106, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }

      return returnDataItem;
   }

// -------------------------------------------------------------------

/**
 * Update a <code>DataItemRecord</code> in the registry.  The given
 * new <code>DataItemRecord</code> is used to replace an existing
 * <code>DataItemRecord</code> in the registry with the same
 * <code>dataItemID</code>.
 *
 * @param dataItemRecord The <code>DataItemRecord</code> to be updated,
 *   with the new values in place.
 * @return true if the update succeeded, otherwise false.
 */

   public boolean updateDataItemRecord(DataItemRecord dataItemRecord)
   {  boolean status = true;

      try
      {
//
//      Assemble the SQL statement to update the dataItemRecord.
//      But first convert dates in the java.util.Date format to the
//      java.sql.Date formats.

         java.util.Date creationDate = dataItemRecord.getCreationDate();
         long creationTime = creationDate.getTime();
         java.sql.Date sqlCreation = new java.sql.Date(creationTime);

         java.util.Date expiryDate = dataItemRecord.getExpiryDate();
         long expiryTime = expiryDate.getTime();
         java.sql.Date sqlExpiry = new java.sql.Date(expiryTime);

         String sqlStatement = "UPDATE reg SET " +
           "dataItemName = '" + dataItemRecord.getDataItemName() + "', " +
           "dataItemFile = '" + dataItemRecord.getDataItemFile() + "', " +
           "ownerID = '" + dataItemRecord.getOwnerID() + "', " +
           "creationDate = '" + sqlCreation + "', " +
           "expiryDate = '" + sqlExpiry + "', " +
           "size = " + dataItemRecord.getSize() + ", " +
           "type = " + dataItemRecord.getType() + ", " +
           "permissionsMask = '" + dataItemRecord.getPermissionsMask()
              + "' " + 
           "WHERE (dataItemID=" + dataItemRecord.getDataItemID() + ")";

//
//      Attempt to execute the SQL statement.

         Vector dbvec = this.transact(sqlStatement, true);
         if (dbvec == null)
         {  throw new Exception("JDBC error : " + sqlStatement);
        }
      }
      catch (Exception all)
      {  status = false;

         if (DEBUG) all.printStackTrace();

         MySpaceStatus exStatus = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00106, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }

      return status;
   }

//
// -------------------------------------------------------------------

/**
 * Delete a <code>DataItemRecord</code> from the registry.  The
 * <code>DataItemRecord</code> to be deleted is specified by its
 * identifier, which is passed as the argument.
 *
 * @param dataItemID The <code>dataItemID</code> (unique integer
 *   identifer) for the <code>DataItemRecord</code>.
 * @return true if the deletion succeeded, otherwise false.
 */

   public boolean deleteDataItemRecord(int dataItemID)
   {  boolean status = true;

      try
      {
//
//      Assemble the SQL to delete the specified DataItemRecord.

         String sqlStatement =
           "DELETE FROM reg WHERE (dataItemID=" + dataItemID + ")";

//
//      Attempt to execute the SQL statement.

         Vector dbvec = this.transact(sqlStatement, true);
         if (dbvec == null)
         {  throw new Exception("JDBC error : " + sqlStatement);
         }
      }
      catch (Exception all)
      {  status = false;

         if (DEBUG) all.printStackTrace();

         MySpaceStatus exStatus = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00106, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }

      return  status;
   }

//
// -------------------------------------------------------------------

/**
 * Look up a given <code>DataItemRecord</code> in the registry.
 *
 * @param dataItemID The <code>dataItemID</code> (unique integer
 *   identifer) for the required <code>DataItemRecord</code>.
 * @return The <code>DataItemRecord</code> found.  If the specified
 *   entry was not found then null is returned.
 */

   public DataItemRecord lookupDataItemRecord(int dataItemID)
   {  DataItemRecord returnItemRecord = null;

      try
      {
//
//      Assemble the SQL to select the specified DataItemRecord.

         String sqlStatement =
           "SELECT * FROM reg WHERE (dataItemID=" + dataItemID + ")";

//
//      Attempt to execute the SQL statement.

         Vector dbvec = this.transact(sqlStatement, false);
         if (dbvec != null)
         {  if (dbvec.size() == 1)
            {  returnItemRecord = (DataItemRecord)dbvec.elementAt(0);
               String uri = this.getServerURI("serv1") +
                 returnItemRecord.getDataItemFile();
               returnItemRecord.setDataItemUri(uri);
            }
            else
            {  throw new Exception("JDBC error : " + sqlStatement);
            }
         }
         else
         {  throw new Exception("JDBC error : " + sqlStatement);
         }
      }
      catch (Exception all)
      {  returnItemRecord = null;

         if (DEBUG) all.printStackTrace();

         MySpaceStatus status = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00106, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }

      return returnItemRecord;
   }

// -------------------------------------------------------------------

/**
 * Look up the <code>DataItemRecord</code>s in the registry which match a
 * given expression.  This expression comprises a DataHolder name which
 * can optionally contain a wild-card.  The wild-card character is an
 * asterisk ("*").
 *
 * @param dataHolderNameExpr The string which
 *   <code>DataItemRecord</code> names must match in order to be
 *   selected.
 * @return a Vector of <code>DataItemRecord</code>s which match the
 *   given name.  If no entries match null is returned.
 */

   public Vector lookupDataItemRecords(String dataHolderNameExpr)
   {  Vector returnItemRecords = new Vector();

      try
      {
//
//      Assemble the SQL to select the specified DataItemRecords.
//      First convert Unix regular-expression style wildcards to SQL
//      style wild cards.

         String localDataHolderNameExpr = dataHolderNameExpr.replace(
           '*', '%');

         String sqlStatement =
           "SELECT * FROM reg WHERE dataItemName LIKE '" +
           localDataHolderNameExpr + "'" +
           "ORDER BY dataItemName ASC";

//
//      Attempt to execute the SQL statement.

         Vector dbvec = this.transact(sqlStatement, false);
         if (dbvec != null)
         {  if (dbvec.size() > 0)
            {  for(int loop=0; loop<dbvec.size(); loop++)
               {  DataItemRecord currRec =
                    (DataItemRecord)dbvec.elementAt(loop);
                  String uri = this.getServerURI("serv1") +
                    currRec.getDataItemFile();
                  currRec.setDataItemUri(uri);
                  returnItemRecords.add(currRec);
               }
            }
         }
         else
         {  throw new Exception("JDBC error : " + sqlStatement);
         }
      }
      catch (Exception all)
      {  returnItemRecords = null;

         if (DEBUG) all.printStackTrace();

         MySpaceStatus status = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00106, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }

      if (returnItemRecords != null)
      {  if (returnItemRecords.size() == 0)
         {  returnItemRecords = null;
            System.out.println("hello");
         }
      }

      return returnItemRecords;
   }

// -------------------------------------------------------------------

/**
 * Return a Vector of server names.
 *
 * @return A Vector comprising a list of the names of all the servers
 *   belonging to the current MySpace system.
 */

   public Vector getServerNames()
   {  Vector serverNames = new Vector();

      try
      {
//
//      Assemble the SQL to select all the servers.

         String sqlStatement = "SELECT * FROM servers";

//
//      Attempt to execute the SQL statement.

         Vector dbvec = this.transact(sqlStatement, false);

//
//      Retrieve the names of the selected servers.

         if (dbvec != null)
         {  if (dbvec.size() > 0)
            {  for (int loop = 0; loop < dbvec.size(); loop++)
               {  ServerDetails currentServer =
                    (ServerDetails)dbvec.elementAt(loop);
                  serverNames.add(currentServer.getName() );
               }
            }
            else
            {  throw new Exception("JDBC error : " + sqlStatement);
            }
         }
         else
         {  throw new Exception("JDBC error : " + sqlStatement);
         }
      }
      catch (Exception all)
      {  serverNames = null;

         if (DEBUG) all.printStackTrace();

         MySpaceStatus status = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00107, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }

      return serverNames;
   }

// -------------------------------------------------------------------

/**
 * Check whether a given name is the name of a server in the MySpace
 * system.
 *
 * @param serverName The name of the server.
 * @return true if the name was found, otherwise false.
 */

   public boolean isServerName(String serverName)
   {  boolean isName;

      try
      {  Vector serverNames = this.getServerNames();

         int serverIndex = serverNames.indexOf(serverName);

         if (serverIndex > -1)
         {  isName = true;
         }
         else
         {  isName = false;
         }
      }
      catch (Exception all)
      {  isName = false;
      }

      return isName;
   }

// -------------------------------------------------------------------

/**
 * Return the expiry period for a given server.
 *
 * @param serverName The name of the server.
 * @return The expiry period, in days, of the server.
 */

   public int getServerExpiryPeriod(String serverName)
   {  int serverExpiryPeriod = -1;

      try
      {
//
//      Assemble the SQL to select the specified server.

         String sqlStatement =
           "SELECT * FROM servers WHERE (name='" + serverName + "')" ;

//
//      Attempt to execute the SQL statement.

         Vector dbvec = this.transact(sqlStatement, false);

//
//      Retrieve the expiry period of the specified server.

         if (dbvec != null)
         {  if (dbvec.size() == 1)
            {  ServerDetails currentServer =
                 (ServerDetails)dbvec.elementAt(0);
               serverExpiryPeriod = currentServer.getExpiryPeriod();
            }
            else
            {  throw new Exception("JDBC error : " + sqlStatement);
            }
         }
         else
         {  throw new Exception("JDBC error : " + sqlStatement);
         }
      }
      catch (Exception all)
      {  serverExpiryPeriod = -1;

         if (DEBUG) all.printStackTrace();

         MySpaceStatus status = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00107, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }

      return serverExpiryPeriod;
   }

// -------------------------------------------------------------------

/**
 * Return the base URI for a given server.
 *
 * @param serverName The name of the server.
 * @return The URI of the server.
 */

   public String getServerURI(String serverName)
   {  String serverURI = null;

      try
      {
//
//      Assemble the SQL to select the specified server.

         String sqlStatement =
           "SELECT * FROM servers WHERE (name='" + serverName + "')" ;

//
//      Attempt to execute the SQL statement.

         Vector dbvec = this.transact(sqlStatement, false);

//
//      Retrieve the URI of the specified server.

         if (dbvec != null)
         {  if (dbvec.size() == 1)
            {  ServerDetails currentServer =
                 (ServerDetails)dbvec.elementAt(0);
               serverURI = currentServer.getURI();
            }
            else
            {  throw new Exception("JDBC error : " + sqlStatement);
            }
         }
         else
         {  throw new Exception("JDBC error : " + sqlStatement);
         }
      }
      catch (Exception all)
      {  serverURI = null;

         if (DEBUG) all.printStackTrace();

         MySpaceStatus status = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00107, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }

      return serverURI;
   }

// -------------------------------------------------------------------

/**
 * Return the base directory for a given server.
 *
 * @param serverName The name of the server.
 * @return The base directory of the server.
 */

   public String getServerDirectory(String serverName)
   {  String serverDirectory = null;

      try
      {
//
//      Assemble the SQL to select the specified server.

         String sqlStatement =
           "SELECT * FROM servers WHERE (name='" + serverName + "')" ;

//
//      Attempt to execute the SQL statement.

         Vector dbvec = this.transact(sqlStatement, false);

//
//      Retrieve the base directory of the specified server.

         if (dbvec != null)
         {  if (dbvec.size() == 1)
            {  ServerDetails currentServer =
                 (ServerDetails)dbvec.elementAt(0);
               serverDirectory = currentServer.getDirectory();
            }
            else
            {  throw new Exception("JDBC error : " + sqlStatement);
            }
         }
         else
         {  throw new Exception("JDBC error : " + sqlStatement);
         }
      }
      catch (Exception all)
      {  serverDirectory = null;

         if (DEBUG) all.printStackTrace();

         MySpaceStatus status = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00107, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }

      return serverDirectory;
   }

//
// -- Private Methods ------------------------------------------------

/**
 * Execute a database transaction.
 *
 * @param The sqlStatement SQL statement to be executed.
 * @param update Update or query flag: true if the transaction is an
 *   update which will modify the database, false if it is a passive
 *   query which will leave the database unchanged.
 * @return If the transaction is a query a Vector of selected rows are
 *   returned.  If the transaction is an update a dummy vector is
 *   returned (containing one element comprising the string "true").
 *   In either case if an error occurred then null is returned.
 */

   private Vector transact (String sqlStatement, boolean update)
   {  Vector vec = null;

      if (DEBUG)
      {  if (update)
         {  System.out.println("\n" + "SQL update: " + sqlStatement);
         }
         else
         {  System.out.println("\n" + "SQL query: " + sqlStatement);
         }
      }

      try
      {
//
//      Load the HSQLDB JDBC driver.

         Class.forName(jdbcDriverClass);

//
//      Establish a connection to the database.

         Connection conn = DriverManager.getConnection(
           registryDBName, "sa", "");

//
//      Create a statement.

         Statement st = null;
         st = conn.createStatement();

//
//      Check whether the operation is an update or a query.

         if (update)
         {
//
//         Execute the SQL to perform the update.

            int sqlStatus = st.executeUpdate(sqlStatement);

            if (sqlStatus == -1)
            {  throw new Exception("JDBC error : " + sqlStatement);
            }
            else
            {  Vector newvec = new Vector();
               newvec.add("true");
               vec = newvec;
            }
         }
         else
         {
//
//         Perform the query.

            ResultSet sqlResults = st.executeQuery(sqlStatement);

//
//         Convert the ResultSet returned into a Vector of dataItemRecords
//         or Servers.  The number of columns in the ResultSet is
//         used to distinguish between the two cases.

            Vector newvec = new Vector();

            ResultSetMetaData meta = sqlResults.getMetaData();
            int numColumns = meta.getColumnCount();

//            System.out.println("numColumns: " + numColumns);

            if (numColumns == 9)
            {  DataItemRecord itemRec = new DataItemRecord();

               while (sqlResults.next() )
               {  java.sql.Date sqlCreation =
                    sqlResults.getDate("creationDate");
                  java.sql.Date sqlExpiry =
                    sqlResults.getDate("expiryDate");

                  long creationTime = sqlCreation.getTime();
                  long expiryTime = sqlExpiry.getTime();

                  java.util.Date creation = new java.util.Date(creationTime);
                  java.util.Date expiry = new java.util.Date(expiryTime);

                  itemRec = new DataItemRecord(
                    sqlResults.getString("dataItemName"),
                    sqlResults.getInt("dataItemID"),
                    sqlResults.getString("dataItemFile"),
                    sqlResults.getString("ownerID"),
                    creation,
                    expiry,
                    sqlResults.getInt("size"),
                    sqlResults.getInt("type"),
                    sqlResults.getString("permissionsMask")
                  );
                  newvec.add(itemRec);
               }

            }
            else
            {  ServerDetails server = new ServerDetails();

               while (sqlResults.next() )
               {  server = new ServerDetails(
                    sqlResults.getString("name"),
                    sqlResults.getInt("expiryPeriod"),
                    sqlResults.getString("URI"),
                    sqlResults.getString("directory")
                  );
                  newvec.add(server);
               }
            }
            vec = newvec;
         }

//
//      Close the connection to the database.

         conn.close(); 
      }
      catch (Exception all)
      {  vec = null;

         if (DEBUG) all.printStackTrace();

         MySpaceStatus exStatus = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00106, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }

      return vec;
   }

//
// -------------------------------------------------------------------

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