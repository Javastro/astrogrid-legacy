/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/database/castor/Attic/CastorDatabaseConfigurationFactoryTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/20 21:11:05 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CastorDatabaseConfigurationFactoryTest.java,v $
 *   Revision 1.2  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.1.2.1  2004/02/19 14:51:00  dave
 *   Changed DatabaseManager to DatabaseConfigurationFactory.
 *
 *   Revision 1.2.2.1  2004/02/16 15:20:54  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.3  2004/01/30 03:21:23  dave
 *   Added initial code for SecurityManager and SecurityService
 *
 *   Revision 1.1.2.2  2004/01/26 16:59:22  dave
 *   Experimented with in-memory databases - they don't work
 *
 *   Revision 1.1.2.1  2004/01/26 13:18:08  dave
 *   Added new DatabaseManager to enable local JUnit testing
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.database.castor ;

import org.astrogrid.community.server.database.DatabaseConfiguration ;
import org.astrogrid.community.server.database.DatabaseConfigurationFactory ;

import junit.framework.TestCase ;

import org.exolab.castor.jdo.JDO ;
import org.exolab.castor.jdo.Database ;
import org.exolab.castor.jdo.DatabaseNotFoundException ;

import java.io.FileNotFoundException ;

/**
 * Test cases for our DatabaseConfiguration.
 *
 */
public class CastorDatabaseConfigurationFactoryTest
    extends TestCase
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Try creating a CastorDatabaseConfigurationFactory.
     *
     */
    public void testCreateManager()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfigurationFactoryTest:testCreateManager()") ;
        //
        // Create our database manager.
        assertNotNull("Null manager",
            new CastorDatabaseConfigurationFactory()
            ) ;
        }

    /**
     * Try initialising a database configuration.
     *
     */
    public void testInitConfiguration()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfigurationFactoryTest:testCreateConfiguration()") ;
        //
        // Create our database manager.
        CastorDatabaseConfigurationFactory manager = new CastorDatabaseConfigurationFactory() ;
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
     * Using same resource files CastorDatabaseConfigurationFactory does not load the configuration again.
     */
    public void testInitDifferent()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfigurationFactoryTest:testInitDifferent()") ;
        //
        // Create our database manager.
        CastorDatabaseConfigurationFactory manager = new CastorDatabaseConfigurationFactory() ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfigurationFactoryTest:testInitResource()") ;
        //
        // Create our database manager.
        CastorDatabaseConfigurationFactory manager = new CastorDatabaseConfigurationFactory() ;
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
