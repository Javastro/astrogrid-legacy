package org.astrogrid.mySpace.mySpaceManager;

import java.sql.*;
// import java.io.*;
import java.util.*;

// import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatus;
// import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatusCode;

/**
 * The <code>RegistryManager</code> class is used to access entries in
 * a MySpace registry.
 * 
 * <p>
 * Prototype version using JDBC.
 * 
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 4.
 */

public class RegistryManager
{  private String jdbcDriverClass = "org.hsqldb.jdbcDriver";

   private String registryName;
                           // Name of the registry.
   private String registryDBName;
                           // Name of the database holding the registry.

   private int dataItemIDSeqNo;
                           // Sequence number for new dataItems.


//
// -- Constructors ---------------------------------------------------

/**
 * Constructor to create a <code>RegistryManager</code> object by reading
 * an existing registry file.  The argument passed to the constructor is
 * the name of this file.
 */

   public RegistryManager(String registryName)
   {  this.registryName = registryName;
      registryDBName = "jdbc:hsqldb:" + registryName + ".db";
   }

// -------------------------------------------------------------------

/**
 * Constructor to create a <code>RegistryManager</code> object for a new,
 * empty registry.  The first argument is the name of the file to which
 * this registry will, in due course, be written.  The second argument
 * is a Vector containing details of the Servers known to the MySpace system.
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
         {  
            System.out.println("db error : " + sqlStatement);
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
         {  
            System.out.println("db error : " + sqlStatement);
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
               {  System.out.println("db error : " + sqlStatement);
               }
            }
         }
      }
      catch (Exception all)
      {  all.printStackTrace();

//       MySpaceStatus exStatus = new MySpaceStatus(
//         MySpaceStatusCode.AGMMCE00106, MySpaceStatusCode.ERROR,
//         MySpaceStatusCode.LOG, this.getClassName() );
      }
   }

// -------------------------------------------------------------------

/**
 * Dummy constructor with no arguments.
 */

   public RegistryManager()
   {  registryName = null;
      registryDBName = null;

      dataItemIDSeqNo = 0;
   }

//
// -- Methods --------------------------------------------------------

/**
 * Add a <code>DataItemRecord</code> to the registry.
 */

   public int addDataItemRecord(DataItemRecord dataItemRecord)
   {  int returnDataItemID = -1;

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

         java.util.Date creationDate = dataItemRecord.getCreationDate();
         long creationTime = creationDate.getTime();
         java.sql.Date sqlCreation = new java.sql.Date(creationTime);

         java.util.Date expiryDate = dataItemRecord.getExpiryDate();
         long expiryTime = expiryDate.getTime();
         java.sql.Date sqlExpiry = new java.sql.Date(expiryTime);

         String sqlStatement = "INSERT INTO reg(" +
           "dataItemName, " +
//            dataItemID, 
           "dataItemFile, ownerID, " +
           "creationDate, expiryDate, " +
           "size, type, permissionsMask) " +
           "VALUES (" +
            "'" + dataItemRecord.getDataItemName() + "', " +
//            dataItemRecord.getDataItemID() + ", " +
            "'" + dataItemRecord.getDataItemFile() + "', " +
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
         {  returnDataItemID = -1;
            System.out.println("db error : " + sqlStatement);
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

         if (dbvec != null)
         {  if (dbvec.size() == 1)
            {  DataItemRecord newRec = (DataItemRecord)dbvec.elementAt(0);
               returnDataItemID = newRec.getDataItemID();
            }
         }
      }
      catch (Exception all)
      {  returnDataItemID = -1;

         all.printStackTrace();

//       MySpaceStatus exStatus = new MySpaceStatus(
//         MySpaceStatusCode.AGMMCE00106, MySpaceStatusCode.ERROR,
//         MySpaceStatusCode.LOG, this.getClassName() );
      }

      return returnDataItemID;
   }

// -------------------------------------------------------------------

/**
 * Update a <code>DataItemRecord</code> in the registry.  The given
 * new <code>DataItemRecord</code> is used to replace an existing
 * <code>DataItemRecord</code> in the registry with the same key.
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
         {  status = false;
            System.out.println("db error : " + sqlStatement);
        }
      }
      catch (Exception all)
      {  status = false;

//       MySpaceStatus exStatus = new MySpaceStatus(
//         MySpaceStatusCode.AGMMCE00106, MySpaceStatusCode.ERROR,
//         MySpaceStatusCode.LOG, this.getClassName() );
      }

      return status;
   }

//
// -------------------------------------------------------------------

/**
 * Delete a <code>DataItemRecord</code> from the registry.  The
 * <code>DataItemRecord</code> to be deleted is specified by its
 * identifier, which is passed as the argument.
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
         {  status = false;
         }
      }
      catch (Exception all)
      {  status = false;

//       MySpaceStatus exStatus = new MySpaceStatus(
//         MySpaceStatusCode.AGMMCE00106, MySpaceStatusCode.ERROR,
//         MySpaceStatusCode.LOG, this.getClassName() );
      }

      return  status;
   }

//
// -------------------------------------------------------------------

/**
 * Lookup a <code>DataItemRecord</code> in the registry.  The
 * <code>DataItemRecord</code> to be obtained is specified by its
 * identifier, which is passed as the argument.  If the specified
 * <code>DataItemRecord</code> could not be found then a null
 * <code>DataItemRecord</code> is returned.
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
            }
        }
      }
      catch (Exception all)
      {  returnItemRecord = null;

         all.printStackTrace();

//       MySpaceStatus status = new MySpaceStatus(
//         MySpaceStatusCode.AGMMCE00106, MySpaceStatusCode.ERROR,
//         MySpaceStatusCode.LOG, this.getClassName() );
      }

      return returnItemRecord;
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
   {  Vector returnItemRecords = null;

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
           localDataHolderNameExpr + "'";

//
//      Attempt to execute the SQL statement.

         Vector dbvec = this.transact(sqlStatement, false);
         if (dbvec != null)
         {  returnItemRecords = dbvec;
         }
      }
      catch (Exception all)
      {  

//       MySpaceStatus status = new MySpaceStatus(
//         MySpaceStatusCode.AGMMCE00106, MySpaceStatusCode.ERROR,
//         MySpaceStatusCode.LOG, this.getClassName() );
      }

      return returnItemRecords;
   }

// -------------------------------------------------------------------

/**
 * Return the Vector of server names.  This method returns a Vector
 * comprising a list of the names of all the servers belonging to the
 * current MySpace manager.
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
            {  serverNames = null;
            }
         }
         else
         {  serverNames = null;
         }
      }
      catch (Exception all)
      {  serverNames = null;
      }

      return serverNames;
   }

// -------------------------------------------------------------------

/**
 * Check whether a given name is the name of a server.  A value of
 * true is returned if the name is valid; otherwise false is returned.
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
 * Return the expiry period, in days, for a given server.
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
         }
      }
      catch (Exception all)
      {  serverExpiryPeriod = -1;
      }

      return serverExpiryPeriod;
   }

// -------------------------------------------------------------------

/**
 * Return the base URI for a given server.
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
         }
      }
      catch (Exception all)
      {  serverURI = null;
      }

      return serverURI;
   }

// -------------------------------------------------------------------

/**
 * Return the base directory for a given server.
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
         }
      }
      catch (Exception all)
      {  serverDirectory = null;
      }

      return serverDirectory;
   }

//
// -- Private Methods ------------------------------------------------

/**
 * Execute a database transaction.
 */

   public Vector transact (String sqlStatement, boolean update)
   {  Vector vec = null;

      if (update)
      {  System.out.println("\n" + "Update...");
      }
      else
      {  System.out.println("\n" + "Query...");
      }
      System.out.println("sqlStatement: " + sqlStatement);

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
            {  System.out.println("db error : " + sqlStatement);
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

            System.out.println("numColumns: " + numColumns);

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

         all.printStackTrace();

//       MySpaceStatus exStatus = new MySpaceStatus(
//         MySpaceStatusCode.AGMMCE00106, MySpaceStatusCode.ERROR,
//         MySpaceStatusCode.LOG, this.getClassName() );
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