/*
 * $Id: SampleStarsPlugin.java,v 1.7 2011/05/09 15:31:24 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigReader;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.Configuration;
import org.astrogrid.dataservice.queriers.DatabaseAccessException;
import org.astrogrid.dataservice.queriers.QuerierPluginFactory;
import org.astrogrid.tableserver.jdbc.JdbcConnections;
import org.astrogrid.tableserver.jdbc.JdbcPlugin;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;

/**
 * This plugin works with a 'fixed' set of values in an HSQL database.  So
 * SQL statements can be run against it.
 *
 * @author M Hill
 */

public class SampleStarsPlugin extends JdbcPlugin {
  private static final Log LOG = LogFactory.getLog(SampleStarsPlugin.class);
   
   private static boolean initialised = false;

   private static final SimpleDateFormat sqlDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
   
   public SampleStarsPlugin() throws IOException
   {
      if (!initialised ) { initialise(); }
      
   }

   /** Sets up the configuration etc for accessing this database.
    * NOTE:   */
   public static void initConfig() {
      
      ConfigFactory.getCommonConfig().setProperty(QuerierPluginFactory.QUERIER_PLUGIN_KEY, SampleStarsPlugin.class.getName());

      // Some important preliminaries
      ConfigFactory.getCommonConfig().setProperty(
          "datacenter.metadoc.file","samplestars.metadoc.xml");
      ConfigFactory.getCommonConfig().setProperty(
         "datacenter.authorityId","astrogrid.org");
      ConfigFactory.getCommonConfig().setProperty(
         "datacenter.resourceKey","test-dsa-catalog");


      ConfigFactory.getCommonConfig().setProperty(
          "db.trigfuncs.in.radians","true");
      ConfigFactory.getCommonConfig().setProperty(
          "datacenter.implements.conesearch","true");
      ConfigFactory.getCommonConfig().setProperty(
          "datacenter.implements.multicone","true");
      ConfigFactory.getCommonConfig().setProperty(
          "conesearch.radius.limit","180.0");
      ConfigFactory.getCommonConfig().setProperty(
          "multicone.radius.limit","180.0");

      //set up properties so we connect to the db and translate to the correct
      //flavour of SQL
      //
      ConfigFactory.getCommonConfig().setProperty(
          JdbcConnections.JDBC_DRIVERS_KEY, "org.hsqldb.jdbcDriver");
    //in memory db - doesn't seem to persist between calls...
    //ConfigFactory.getCommonConfig().setProperty(
    //       JdbcConnections.JDBC_URL_KEY, "jdbc:hsqldb:.");
    // This creates db on disk
    // Need "shutdown=true" to force the DB to shut down when no
    // active connections exist (required from v1.7.2 onwards)
      new File("target").mkdir();
      ConfigFactory.getCommonConfig().setProperty(
          JdbcConnections.JDBC_URL_KEY, "jdbc:hsqldb:target/dummydb;shutdown=true");
      ConfigFactory.getCommonConfig().setProperty(
          JdbcConnections.JDBC_USER_KEY, "sa");
      ConfigFactory.getCommonConfig().setProperty(
          JdbcConnections.JDBC_PASSWORD_KEY, "");

      Configuration.setAdqlStylesheetName("HSQLDB-1.8.0.xsl");
      
      //it's a bit naughty setting this, but it sorts out most tests
      ConfigFactory.getCommonConfig().setProperty("datacenter.url", ConfigFactory.getCommonConfig().getProperty("datacenter.url", "http://localhost:8080/pal-Sample/"));
      
      //set where to find the data description meta document
      //this works OK for unit test, but not deployment...
      URL url = SampleStarsPlugin.class.getResource("samplestars.metadoc.xml");
      LOG.debug("Sample-stars metadoc URL: " + url);
      if (url == null) {
         //this works OK for deployment, but not unit tests...
         try {
            url = ConfigReader.resolveFilename("samplestars.metadoc.xml");
         }
         catch (IOException e) {
            throw new RuntimeException(e);
         }
      }
      ConfigFactory.getCommonConfig().setProperty(TableMetaDocInterpreter.TABLE_METADOC_URL_KEY, url.toString());
      TableMetaDocInterpreter.clear();
      try {
        TableMetaDocInterpreter.loadAndValidateMetadoc(url);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
      
      //configure which resources to produce
     // ConfigFactory.getCommonConfig().setProperty("datacenter.resource.register.v0_10","disabled");  NO LONGER NEEDED
      ConfigFactory.getCommonConfig().setProperty("datacenter.resource.register.v1_0","enabled");

      //set up the properties for the authority bit
      ConfigFactory.getCommonConfig().setProperty("datacenter.name", "Default Astrogrid DSA/Catalog running test database");
      //ConfigFactory.getCommonConfig().setProperty("datacenter.publisher", "AstroGrid");
      ConfigFactory.getCommonConfig().setProperty("datacenter.description", "This is a default (unconfigured) DSA/catalog installation.  It accesses a small HSQLDB database containing fictitious tables of stars and galaxies for testing and demonstration purposes.");

      ConfigFactory.getCommonConfig().setProperty("default.table","TabName_SampleStars");
      // Conesearch and self-test properties
      ConfigFactory.getCommonConfig().setProperty("datacenter.self-test.catalog","SampleStarsCat");
      ConfigFactory.getCommonConfig().setProperty("datacenter.self-test.table","SampleStars");
      ConfigFactory.getCommonConfig().setProperty("datacenter.self-test.column1","RA");
      ConfigFactory.getCommonConfig().setProperty("datacenter.self-test.column2","DEC");
      ConfigFactory.getCommonConfig().setProperty("datacenter.max.return","0");

      //
      ConfigFactory.getCommonConfig().setProperty("datacenter.authorityId", "astrogrid.org");
      ConfigFactory.getCommonConfig().setProperty("datacenter.resourceKey", "test-dsa-catalog");
    }
    
   public static synchronized void initialise() throws DatabaseAccessException {
      
      if (initialised) return;
      
      initConfig();

      populateDb();
    }
       
   /** Creates & populates the in-memory database.
    * Static so we can reach it from our test harness
    */
   public static synchronized void populateDb() throws DatabaseAccessException {
       
      //connect
      Connection connection = null;
      try {
         connection = JdbcConnections.makeFromConfig().getConnection();
      }
      catch (SQLException se) {
         throw new DatabaseAccessException("Could not connect to JDBC: "+se);
      }
      
      //first remove in case there in memory still from previous test
      try {
         connection.createStatement().execute("DROP TABLE SampleStars"  );
         connection.createStatement().execute("DROP TABLE SampleStars2"  );
         connection.createStatement().execute("DROP TABLE SampleGalaxies"  );
         connection.createStatement().execute("DROP TABLE Plates"  );
      }
      catch (SQLException se) {
         //ignore - may not exist
      }
      
      LOG.info("Populating Database");

      try {
         //populate stars
         createStarTable(connection, "SampleStars");
         createStarTable(connection, "SampleStars2");
         
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
   
         //populate plate tables
         connection.createStatement().execute(
            "CREATE TABLE Plates (Id INTEGER IDENTITY,  CenterRa DOUBLE,  CenterDec DOUBLE,  ObsTime DATETIME) "
         );
         long todayTime = new Date().getTime();
         for (int i=0;i<5;i++) {
            connection.createStatement().execute(
//               "INSERT INTO Plates VALUES ("+i+", "+(100+i*20)+", "+(100-i*20)+", "+sqlDateFormat.format(new java.sql.Date(todayTime-i*1000))+")"
               "INSERT INTO Plates VALUES ("+i+", "+(100+i*20)+", "+(100-i*20)+", '"+new java.sql.Timestamp(todayTime-i*1000)+"')"
            );
         }
         
         
         connection.commit();
         connection.close();
      }
      catch (SQLException se) {
         LOG.error("Populating demo db",se);
      }

      initialised = true;
      
      LOG.info("...database populated"); //so that we can mark how long it took
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


   public static void createStarTable(Connection connection, String tableName) 
            throws SQLException, DatabaseAccessException 
   {
      //populate stars
      //create table
      connection.createStatement().execute(
         "CREATE TABLE " + tableName + " (Id INTEGER IDENTITY,  Name VARCHAR(30), Ra DOUBLE,  Dec DOUBLE,  Mag DOUBLE, Flag BOOLEAN)  "
      );

      //create index on table
      connection.createStatement().execute(
         "CREATE INDEX "+tableName+"_ssIndex ON " + tableName + " (Ra, Dec)  "
      );
      
      
      //add some stars
      for (int i=0;i<20;i++) {
         String flag;
         if (i/2 == 0) {
            flag="true";
         }
         else {
            flag="false";
         }
         connection.createStatement().execute(
            "INSERT INTO " + tableName + " VALUES ("+i+", 'A star', "+(30+i*2)+", "+(30-i*2)+", "+i+","+flag+")"
         );
      }

      //add false pleidies.  These are stars grouped < 0.3 degree across on ra=56.75, dec=23.867
      int id=21;
      connection.createStatement().execute("INSERT INTO " + tableName + " VALUES ("+id+", 'Pleidies LE', 56.6, 23.65, 10, false)"); id++;
      connection.createStatement().execute("INSERT INTO " + tableName + " VALUES ("+id+", 'Pleidies RE', 56.9, 23.65, 10, true)"); id++;
      connection.createStatement().execute("INSERT INTO " + tableName + " VALUES ("+id+", 'Pleidies Nose', 56.75, 23.87, 8, false)"); id++;
      connection.createStatement().execute("INSERT INTO " + tableName + " VALUES ("+id+", 'Pleidies Grin', 56.5, 23.9, 12, true)"); id++;
      connection.createStatement().execute("INSERT INTO " + tableName + " VALUES ("+id+", 'Pleidies Grin', 56.7, 24.0, 12, false)"); id++;
      connection.createStatement().execute("INSERT INTO " + tableName + " VALUES ("+id+", 'Pleidies Grin', 56.8, 24.0, 12, true)"); id++;
      connection.createStatement().execute("INSERT INTO " + tableName + " VALUES ("+id+", 'Pleidies Grin', 57.0, 23.9, 12, false)"); id++;

      //add stars that are outside the above group but nearby
      connection.createStatement().execute("INSERT INTO " + tableName + " VALUES ("+id+", 'Not Pleidies', 56.6, 23.6, 10, true)"); id++;
      connection.createStatement().execute("INSERT INTO " + tableName + " VALUES ("+id+", 'Not Pleidies', 56, 23, 5, false)"); id++;
      connection.createStatement().execute("INSERT INTO " + tableName + " VALUES ("+id+", 'Not Pleidies', 58, 24.5, 5, true)"); id++;
      connection.createStatement().execute("INSERT INTO " + tableName + " VALUES ("+id+", 'Not Pleidies', 56, 24.5, 5, false)"); id++;
      connection.createStatement().execute("INSERT INTO " + tableName + " VALUES ("+id+", 'Not Pleidies', 58, 23, 5, true)"); id++;
      
      //add even spread (in coordinate space) of background stars
      for (double ra=0;ra<360;ra=ra+2) {
         for (double dec=-90;dec<90;dec=dec+2) {
            connection.createStatement().execute("INSERT INTO " + tableName + " VALUES  ("+id+", 'Background', "+ra+", "+dec+", 20, false)"); id++;
         }
      }
   }



   /* Sample SQL statements to help with above:
   
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

