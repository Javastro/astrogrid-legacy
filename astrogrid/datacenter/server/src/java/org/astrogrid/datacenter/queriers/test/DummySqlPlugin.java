/*
 * $Id: DummySqlPlugin.java,v 1.5 2004/07/07 19:33:59 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.test;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierPluginFactory;
import org.astrogrid.datacenter.queriers.sql.JdbcConnections;
import org.astrogrid.datacenter.queriers.sql.JdbcPlugin;
import org.astrogrid.datacenter.queriers.sql.SqlMaker;
import org.astrogrid.datacenter.queriers.sql.StdSqlMaker;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/**
 * This plugin works with a 'fixed' set of values in an HSQL database.  So
 * SQL statements can be run against it.
 *
 * @author M Hill
 */

public class DummySqlPlugin extends JdbcPlugin
{
   
   private static boolean populated = false;
   
   public DummySqlPlugin(Querier querier) throws IOException
   {
      super(querier);
      
      if (!populated) { populateDb(); }
      
   }

   /** Sets up the configuration etc for accessing this database */
   public static void initConfig() {
      
      SimpleConfig.setProperty(QuerierPluginFactory.PLUGIN_KEY, DummySqlPlugin.class.getName());
      SimpleConfig.setProperty(JdbcPlugin.SQL_TRANSLATOR, StdSqlMaker.class.getName());

      SimpleConfig.setProperty(SqlMaker.CONE_SEARCH_RA_COL_KEY, "RA");
      SimpleConfig.setProperty(SqlMaker.CONE_SEARCH_DEC_COL_KEY,"DEC");
      SimpleConfig.setProperty(SqlMaker.CONE_SEARCH_TABLE_KEY,  "SampleStars");
      
      //set up properties so we connect to the db
      SimpleConfig.setProperty(JdbcConnections.JDBC_DRIVERS_KEY, "org.hsqldb.jdbcDriver");
//      SimpleConfig.setProperty(JdbcConnections.JDBC_URL_KEY, "jdbc:hsqldb:."); //in memory db - doesn't seem to persist between calls...
      SimpleConfig.setProperty(JdbcConnections.JDBC_URL_KEY, "jdbc:hsqldb:dummydb"); //db on disk
      SimpleConfig.setProperty(JdbcConnections.JDBC_USER_KEY, "sa");
      SimpleConfig.setProperty(JdbcConnections.JDBC_PASSWORD_KEY, "");
      
   }
   

   /** Creates & populates the in-memory database.
    * Static so we can reach it from our test harness
    */
   public static synchronized void populateDb() throws DatabaseAccessException {
      
      if (populated) return;
      
      initConfig();
      
      //connect
      Connection connection = null;
      try {
         connection = JdbcConnections.makeFromConfig().createConnection();
      }
      catch (SQLException se) {
         throw new DatabaseAccessException("Could not connect to JDBC: "+se);
      }
      
      //first remove in case there in memory still from previous test
      try {
         connection.createStatement().execute("DROP TABLE SampleStars"  );
         connection.createStatement().execute("DROP TABLE SampleGalaxies"  );
      }
      catch (SQLException se) {
         //ignore - may not exist
      }
      
      log.info("Populating Database");

   
      //populate stars
      try {
         //create table
         connection.createStatement().execute(
            "CREATE TABLE SampleStars (Id INTEGER IDENTITY,  Ra DOUBLE,  Dec DOUBLE,  Mag DOUBLE)  "
         );
         
         //add stars
         for (int i=0;i<20;i++) {
            connection.createStatement().execute(
               "INSERT INTO SampleStars VALUES ("+i+", "+(30+i*2)+", "+(30-i*2)+", "+i+")"
            );
         }
      
         //populate galaxies
         //create table
         connection.createStatement().execute(
            "CREATE TABLE SampleGalaxies (Id INTEGER IDENTITY,  Ra DOUBLE,  Dec DOUBLE,  Shape VARCHAR(20)) "
         );
         
         //add stars
         String[] shapes = new String[] {"ELLIPTICAL", "SPIRAL", "IRREGULAR" };
         for (int i=0;i<20;i++) {
            
            connection.createStatement().execute(
               "INSERT INTO SampleGalaxies VALUES ("+i+", "+(200+i*2)+", "+(200-i*2)+", '"+shapes[i % 2]+"')"
            );
         }
         
         connection.commit();
         connection.close();
      }
      catch (SQLException se) {
         log.error("Populating demo db",se);
      }

      populated = true;
      
      //check metadata
      /*
      try {
         JdbcPlugin plugin = new DummySqlPlugin(null);
         Document metadata = plugin.getMetadata();
         log.info(DomHelper.DocumentToString(metadata));
      } catch (IOException ioe) {
         throw new RuntimeException(ioe);
      }
       */
   }

   /* Sample SQL statemetns to help with above:
   
   CREATE TABLE Customer (Customer_no INTEGER IDENTITY, firstname VARCHAR(15), lastname VARCHAR(50), address VARCHAR(150), postalcode VARCHAR(7));

   CREATE TABLE Movies (Movie_id INTEGER IDENTITY, title VARCHAR(50), classification VARCHAR(5), star_1 VARCHAR(35), star_2 VARCHAR(35), release_date DATETIME);

   CREATE TABLE Rentals (Customer_no INTEGER, Movie_id INTEGER, date_out DATETIME, date_in DATETIME);
   
   INSERT INTO Customer VALUES (null, 'Joe','Smith','100 Main St','T5T 1A1');
   INSERT INTO Customer VALUES (null, 'Joe','Smith','100 Main St','T5T 1A1');
   INSERT INTO Customer VALUES (null, 'Jane','Jones','100 Main St','T5T 1A1');
   INSERT INTO Customer VALUES (null, 'Bill','Black','100 Main St','T5T 1A1');


   INSERT INTO Movies VALUES (null, 'Return of the Jedi', 'G','star 1', 'star 2', 'now');
   INSERT INTO Movies VALUES (null, 'MASH', 'A','star 1', 'star 2', 'now');
   INSERT INTO Movies VALUES (null, 'Signs', 'PG','star 1', 'star 2', 'now');
    */
   
}
   /*
   $Log: DummySqlPlugin.java,v $
   Revision 1.5  2004/07/07 19:33:59  mch
   Fixes to get Dummy db working and xslt sheets working both for unit tests and deployed

   Revision 1.4  2004/07/06 18:48:34  mch
   Series of unit test fixes

   Revision 1.3  2004/03/13 23:38:46  mch
   Test fixes and better front-end JSP access

   Revision 1.2  2004/03/12 20:04:57  mch
   It05 Refactor (Client)

   Revision 1.1  2004/03/12 04:45:26  mch
   It05 MCH Refactor

    */
