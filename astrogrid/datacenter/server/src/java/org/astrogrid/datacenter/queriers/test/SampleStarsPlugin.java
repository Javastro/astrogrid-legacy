/*
 * $Id: SampleStarsPlugin.java,v 1.1 2004/09/01 13:40:32 mch Exp $
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

public class SampleStarsPlugin extends JdbcPlugin
{
   
   private static boolean populated = false;
   
   public SampleStarsPlugin(Querier querier) throws IOException
   {
      super(querier);
      
      if (!populated) { populateDb(); }
      
   }

   /** Sets up the configuration etc for accessing this database */
   public static void initConfig() {
      
      SimpleConfig.setProperty(QuerierPluginFactory.PLUGIN_KEY, SampleStarsPlugin.class.getName());
      SimpleConfig.setProperty(JdbcPlugin.SQL_TRANSLATOR, StdSqlMaker.class.getName());

      SimpleConfig.setProperty(SqlMaker.CONE_SEARCH_RA_COL_KEY, "RA");
      SimpleConfig.setProperty(SqlMaker.CONE_SEARCH_DEC_COL_KEY,"DEC");
      SimpleConfig.setProperty(SqlMaker.CONE_SEARCH_TABLE_KEY,  "SampleStars");
      
      SimpleConfig.setProperty(SqlMaker.DB_TRIGFUNCS_IN_RADIANS, "True");

      SimpleConfig.setProperty(SqlMaker.CONE_SEARCH_COL_UNITS_KEY, "deg");
      
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
            "CREATE TABLE SampleStars (Id INTEGER IDENTITY,  Name VARCHAR(30), Ra DOUBLE,  Dec DOUBLE,  Mag DOUBLE)  "
         );

         //create table
         connection.createStatement().execute(
            "CREATE INDEX ssIndex ON SampleStars (Ra, Dec)  "
         );
         
         
         //add some stars
         for (int i=0;i<20;i++) {
            connection.createStatement().execute(
               "INSERT INTO SampleStars VALUES ("+i+", 'A star', "+(30+i*2)+", "+(30-i*2)+", "+i+")"
            );
         }

         //add false pleidies.  These are stars grouped < 0.3 degree across on ra=56.75, dec=23.867
         int id=21;
         connection.createStatement().execute("INSERT INTO SampleStars VALUES ("+id+", 'Pleidies LE', 56.6, 23.65, 10)"); id++;
         connection.createStatement().execute("INSERT INTO SampleStars VALUES ("+id+", 'Pleidies RE', 56.9, 23.65, 10)"); id++;
         connection.createStatement().execute("INSERT INTO SampleStars VALUES ("+id+", 'Pleidies Nose', 56.75, 23.87, 8)"); id++;
         connection.createStatement().execute("INSERT INTO SampleStars VALUES ("+id+", 'Pleidies Grin', 56.5, 23.9, 12)"); id++;
         connection.createStatement().execute("INSERT INTO SampleStars VALUES ("+id+", 'Pleidies Grin', 56.7, 24.0, 12)"); id++;
         connection.createStatement().execute("INSERT INTO SampleStars VALUES ("+id+", 'Pleidies Grin', 56.8, 24.0, 12)"); id++;
         connection.createStatement().execute("INSERT INTO SampleStars VALUES ("+id+", 'Pleidies Grin', 57.0, 23.9, 12)"); id++;

         //add stars that are outside the above group but nearby
         connection.createStatement().execute("INSERT INTO SampleStars VALUES ("+id+", 'Not Pleidies', 56.6, 23.6, 10)"); id++;
         connection.createStatement().execute("INSERT INTO SampleStars VALUES ("+id+", 'Not Pleidies', 56, 23, 5)"); id++;
         connection.createStatement().execute("INSERT INTO SampleStars VALUES ("+id+", 'Not Pleidies', 58, 24.5, 5)"); id++;
         connection.createStatement().execute("INSERT INTO SampleStars VALUES ("+id+", 'Not Pleidies', 56, 24.5, 5)"); id++;
         connection.createStatement().execute("INSERT INTO SampleStars VALUES ("+id+", 'Not Pleidies', 58, 23, 5)"); id++;
         
         //add even spread (in coordinate space) of background stars
         for (double ra=0;ra<360;ra=ra+2) {
//            StringBuffer sql = new StringBuffer("INSERT INTO SampleStars VALUES ");
            for (double dec=-90;dec<90;dec=dec+2) {
//               sql.append(" ("+id+", 'Background', "+ra+", "+dec+", 20) "); id++;
               connection.createStatement().execute("INSERT INTO SampleStars VALUES  ("+id+", 'Background', "+ra+", "+dec+", 20)"); id++;
            }
//           connection.createStatement().execute(sql.toString());
            System.out.print(".");
         }
         
         //populate galaxies
         //create table
         connection.createStatement().execute(
            "CREATE TABLE SampleGalaxies (Id INTEGER IDENTITY,  Ra DOUBLE,  Dec DOUBLE,  Shape VARCHAR(20)) "
         );
         
         //add individual galaxies
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
      
      log.info("...database populated"); //so that we can mark how long it took
      //check metadata
      /*
      try {
         JdbcPlugin plugin = new SampleStarsPlugin(null);
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
   $Log: SampleStarsPlugin.java,v $
   Revision 1.1  2004/09/01 13:40:32  mch
   Renamed dummy to samplestars and added metadata server

   Revision 1.10  2004/08/18 22:28:45  mch
   Switched to search on table (index search fails..) and background stars less dense

   Revision 1.9  2004/08/18 18:44:12  mch
   Created metadata plugin service and added helper methods

   Revision 1.8  2004/08/06 12:04:19  mch
   Added unit description to conesearch columns to cope with ESO milliarcseconds (& others in future)

   Revision 1.3.10.1  2004/08/05 17:57:08  mch
   Merging Itn06 fixes into Itn05

   Revision 1.7  2004/08/05 15:58:19  mch
   Added background stars

   Revision 1.6  2004/07/12 23:26:51  mch
   Fixed (somewhat) SQL for cone searches, added tests to Dummy DB

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
