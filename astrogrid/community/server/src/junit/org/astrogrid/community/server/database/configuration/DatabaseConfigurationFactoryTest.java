/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/database/configuration/Attic/DatabaseConfigurationFactoryTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseConfigurationFactoryTest.java,v $
 *   Revision 1.4  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.3.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.3  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.2.54.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.database.configuration ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import junit.framework.TestCase ;

import org.exolab.castor.jdo.JDO ;
import org.exolab.castor.jdo.Database ;
import org.exolab.castor.jdo.DatabaseNotFoundException ;

import java.io.FileNotFoundException ;

/**
 * Test cases for our DatabaseConfiguration.
 *
 */
public class DatabaseConfigurationFactoryTest
    extends TestCase
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(DatabaseConfigurationFactoryTest.class);

    /**
     * Try creating a DatabaseConfigurationFactory.
     *
     */
    public void testCreateManager()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseConfigurationFactoryTest:testCreateManager()") ;
        //
        // Create our database manager.
        assertNotNull("Null manager",
            new DatabaseConfigurationFactory()
            ) ;
        }

    /**
     * Try initialising a database configuration.
     *
     */
    public void testInitConfiguration()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseConfigurationFactoryTest:testCreateConfiguration()") ;
        //
        // Create our database manager.
        DatabaseConfigurationFactory manager = new DatabaseConfigurationFactory() ;
        assertNotNull("Null manager", manager) ;
        //
        // Load our database configuration.
        manager.loadDatabaseConfiguration("test-database-001") ;
        //
        // Check we got a valid configuration
        assertNotNull("Null database configuration",
            manager.getDatabaseConfiguration("test-database-001")
            ) ;
        //
        // Check we can get a database connection.
        assertNotNull("Null database connection",
            manager.getDatabase("test-database-001")
            ) ;
        }

    /**
     * Try initialising two different database configurations.
     *
     * TODO Need to use different resource files.
     * Using same resource files DatabaseConfigurationFactory does not load the configuration again.
     */
    public void testInitDifferent()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseConfigurationFactoryTest:testInitDifferent()") ;
        //
        // Create our database manager.
        DatabaseConfigurationFactory manager = new DatabaseConfigurationFactory() ;
        assertNotNull("Null manager", manager) ;

        //
        // Create a new database configuration.
        manager.loadDatabaseConfiguration("test-database-002") ;
        //
        // Check we got a valid configuration
        assertNotNull("Null database configuration",
            manager.getDatabaseConfiguration("test-database-002")
            ) ;
        //
        // Check we can get a database connection.
        assertNotNull("Null database connection",
            manager.getDatabase("test-database-002")
            ) ;

        //
        // Create a new database configuration.
        manager.loadDatabaseConfiguration("test-database-003") ;
        //
        // Check we got a valid configuration
        assertNotNull("Null database configuration",
            manager.getDatabaseConfiguration("test-database-003")
            ) ;
        //
        // Check we can get a database connection.
        assertNotNull("Null database connection",
            manager.getDatabase("test-database-003")
            ) ;
        }

    /**
     * Try initialising a specific database configuration with a named resource.
     *
     */
    public void testInitResource()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseConfigurationFactoryTest:testInitResource()") ;
        //
        // Create our database manager.
        DatabaseConfigurationFactory manager = new DatabaseConfigurationFactory() ;
        //
        // Create a new database configuration.
        manager.loadDatabaseConfiguration("test-database-004", "test-database-004.xml", null) ;
        //
        // Check we got a valid configuration
        assertNotNull("Null database configuration",
            manager.getDatabaseConfiguration("test-database-004")
            ) ;
        //
        // Check we can get a database connection.
        assertNotNull("Null database connection",
            manager.getDatabase("test-database-004")
            ) ;
        }
    }
