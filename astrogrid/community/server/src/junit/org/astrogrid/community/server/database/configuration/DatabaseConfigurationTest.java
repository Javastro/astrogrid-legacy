package org.astrogrid.community.server.database.configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DatabaseNotFoundException;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.mapping.MappingException;

/**
 * Test cases for our DatabaseConfiguration.
 *
 */
public class DatabaseConfigurationTest extends TestCase {
  
  private static Log log = LogFactory.getLog(DatabaseConfigurationTest.class);

  /**
   * Try creating a DatabaseConfiguration with a non-existient config file.
   */
  public void testCreateUnknownConfig() throws Exception {
    log.debug("DatabaseConfigurationTest:testCreateUnknownConfig()") ;
    try {
      URL u = new URL("file:/missing.xml");
      DatabaseConfiguration config = new DatabaseConfiguration("unknown-database", u);
      fail("FAIL : Should have thrown an exception") ;
    }
    catch (IOException ouch) {
      // Expected.
    }
  }

    /**
     * Try creating a DatabaseConfiguration with a valid config file.
     *
     */
    public void testCreateValid()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseConfigurationTest:testValidConfig()") ;
        //
        // Create a new database configuration.
        URL u = this.getClass().getResource("/test-database-001.xml");
        DatabaseConfiguration config = new DatabaseConfiguration("test-database-001", u);
        assertNotNull(
            "Null configuration",
            config) ;
        //
        // Check the database name.
        assertEquals(
            "Wrong database name",
            config.getDatabaseName(),
            "test-database-001") ;
        }

  /**
   * Try opening a connection with an invalid database-name.
   * Here, the DB configuration is OK, but does not include the requested
   * DB.
   */
  public void testInvalidName() throws Exception {
    log.debug("DatabaseConfigurationTest:testInvalidName()") ;
    try {
      URL u = this.getClass().getResource("/test-database-001.xml");
      DatabaseConfiguration config = 
          new DatabaseConfiguration("INVALID-NAME", u);
      fail("Created configuration for an invalid DB-name.");
    }
    catch(Exception e) {
      // Expected.
      System.out.println(e);
    }
  }

  public void testConnectValid() throws Exception {
    log.debug("DatabaseConfigurationTest:testConnectValid()");
    URL u = this.getClass().getResource("/test-database-001.xml");
    DatabaseConfiguration config = new DatabaseConfiguration("test-database-001", u);
    assertNotNull("Null configuration", config);
    assertNotNull("Null JDO database connection", config.getDatabase());
  }

  /**
   * Try accessing the JDO engine. The DB escription is known to come from the
   * engine (viewing this method as a white-box test), so we check that.
   */
  public void testCreateEngine() throws Exception {
    log.debug("DatabaseConfigurationTest:testCreateEngine()");
    
    URL u = this.getClass().getResource("/test-database-001.xml");
    DatabaseConfiguration config = new DatabaseConfiguration("test-database-001", u);
    assertNotNull("Null configuration", config);
    String dbDescription = config.getDatabaseDescription();
    assertNotNull("Null database description", dbDescription);
    System.out.println(dbDescription);
  }

  /**
   * Try checking the database test data.
   *
   */
  public void testDatabaseTables() throws Exception {
    log.debug("DatabaseConfigurationTest:testDatabaseTables()");
    URL u = this.getClass().getResource("/test-database-001.xml");
    DatabaseConfiguration config = new DatabaseConfiguration("test-database-001", u);
    assertNotNull("Null configuration", config);
  }

  /**
   * Tests creation of the DB schema when no tables exist.
   */
  public void testCreateTables() throws Exception {
    log.debug("DatabaseConfigurationTest:testCreateTables()");
     
    // Create a new database configuration.
    URL u = this.getClass().getResource("/test-database-002.xml");
    DatabaseConfiguration config = new DatabaseConfiguration("test-database-002", u);
    assertNotNull("Null configuration", config);
        
    // Destroy any existing tables.
    StringBuffer sql = new StringBuffer();
    sql.append("CALL SQL DROP TABLE testdata IF EXISTS ");
    sql.append("AS org.astrogrid.community.server.database.configuration.DatabaseConfigurationTestData");
    Database db = config.getDatabase();
    db.begin();
    OQLQuery q = db.getOQLQuery(sql.toString());
    q.execute();
    db.commit();
    db.close();
    assertFalse("Tables exist", config.checkDatabaseTables());
    
    config.resetDatabaseTables() ;
    assertTrue("Tables are missing", config.checkDatabaseTables());
  }

}
