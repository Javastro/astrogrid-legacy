/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/common/junit/Attic/JUnitConfigTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/11/06 15:35:26 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitConfigTest.java,v $
 *   Revision 1.2  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.1  2003/09/13 02:18:52  dave
 *   Extended the jConfig configuration code.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.junit ;

import org.jconfig.Configuration ;

import junit.framework.TestCase ;

import org.astrogrid.community.common.CommunityConfig ;

/**
 * JUnit test for the CommunityConfig.
 *
 */
public class JUnitConfigTest
    extends TestCase
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static final boolean DEBUG_FLAG = true ;

    /**
     * Switch for our assert statements.
     *
     */
    private static final boolean ASSERT_FLAG = false ;

    /**
     * Setup our tests.
     *
     */
    protected void setUp()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("setUp()") ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check we can load the config.
     *
     */
    public void testLoadConfig()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testLoadConfig()") ;

        Configuration config = CommunityConfig.loadConfig() ;
        assertNotNull("Null configuration", config) ;

        String databaseConfig = CommunityConfig.getProperty("database.config") ;
        assertNotNull("Null database config", databaseConfig) ;
        String databaseName = CommunityConfig.getProperty("database.name") ;
        assertNotNull("Null database name", databaseName) ;
        String databaseMap  = CommunityConfig.getProperty("database.mapping") ;
        assertNotNull("Null database mapping", databaseMap) ;

        if (DEBUG_FLAG) System.out.println("Database name    : " + databaseName) ;
        if (DEBUG_FLAG) System.out.println("Database config  : " + databaseConfig) ;
        if (DEBUG_FLAG) System.out.println("Database mapping : " + databaseMap) ;

        String community  = CommunityConfig.getCommunityName() ;
        assertNotNull("Null community name", community) ;
        String manager  = CommunityConfig.getManagerUrl() ;
        assertNotNull("Null manager url", manager) ;
        String service  = CommunityConfig.getServiceUrl() ;
        assertNotNull("Null service url", service) ;

        if (DEBUG_FLAG) System.out.println("Community name   : " + community) ;
        if (DEBUG_FLAG) System.out.println("Manager url      : " + manager) ;
        if (DEBUG_FLAG) System.out.println("Service url      : " + service) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    }
