/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/database/configuration/Attic/TestDatabaseConfigurationFactory.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: TestDatabaseConfigurationFactory.java,v $
 *   Revision 1.5  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.4.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.4  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.3.52.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.database.configuration ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

/**
 * Factory for deafult test database configurations.
 *
 */
public class TestDatabaseConfigurationFactory
    extends DatabaseConfigurationFactory
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(TestDatabaseConfigurationFactory.class);

    /**
     * Our test database name.
     *
     */
    public static final String TEST_DATABASE_NAME = "test-database-001" ;

    /**
     * Our test database XML file.
     *
     */
    public static final String TEST_DATABASE_XML = "test-database-001.xml" ;

    /**
     * Our test database SQL file.
     *
     */
    public static final String TEST_DATABASE_SQL = "test-database-001.sql" ;

    /**
     * Load our test database configuration.
     *
     */
    public DatabaseConfiguration testDatabaseConfiguration()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("TestDatabaseConfigurationFactory:testDatabaseConfiguration()") ;
        //
        // Load our database configuration.
        DatabaseConfiguration config = this.loadDatabaseConfiguration(
            TEST_DATABASE_NAME,
            TEST_DATABASE_XML,
            TEST_DATABASE_SQL) ;
        //
        // Reset the database tables.
//        config.createDatabaseTables() ;
        //
        // Return the new database configuration.
        return config ;
        }
    }
