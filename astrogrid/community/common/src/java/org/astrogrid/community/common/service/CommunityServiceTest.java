/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/service/CommunityServiceTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/09 01:19:50 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityServiceTest.java,v $
 *   Revision 1.7  2004/09/09 01:19:50  dave
 *   Updated MIME type handling in MySpace.
 *   Extended test coverage for MIME types in FileStore and MySpace.
 *   Added VM memory data to community ServiceStatusData.
 *
 *   Revision 1.6.74.1  2004/09/07 04:01:47  dave
 *   Added memory stats ...
 *
 *   Revision 1.6  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.5.32.2  2004/06/17 14:50:03  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.5.32.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.service ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.common.junit.JUnitTestBase ;

import org.astrogrid.community.common.service.data.ServiceStatusData ;
import org.astrogrid.community.common.database.manager.DatabaseManager ;

import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;

import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

/**
 * A JUnit test case for our common CommunityService interface.
 * This is designed to be extended by each set of tests, mock, client and server.
 *
 */
public class CommunityServiceTest
    extends JUnitTestBase
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Public constructor.
     *
     */
    public CommunityServiceTest()
        {
        }

    /**
     * Public constructor, with reference to target service.
     *
     */
    public CommunityServiceTest(CommunityService service)
        {
        this.setCommunityService(service) ;
        }

    /**
     * Create a local Ivorn.
     *
     */
    public Ivorn createLocal(String ident)
        throws CommunityServiceException, CommunityIdentifierException
        {
        return CommunityAccountIvornFactory.createLocal(ident) ;
        }

    /**
     * Our target CommunityService.
     *
     */
    private CommunityService communityService ;

    /**
     * Get our target CommunityService.
     *
     */
    public CommunityService getCommunityService()
        {
        return this.communityService ;
        }

    /**
     * Set our target CommunityService.
     *
     */
    public void setCommunityService(CommunityService service)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityServiceTest.setCommunityService()") ;
        if (DEBUG_FLAG) System.out.println("  Service : " + service.getClass()) ;
        //
        // Set our CommunityService reference.
        this.communityService = service ;
        }

    /**
     * Test the service status.
     * Just checks that the return is not null.
     *
     */
    public void testServiceStatus()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityServiceTest.testServiceStatus()") ;
        if (DEBUG_FLAG) System.out.println("  Service : " + communityService.getClass()) ;
        assertNotNull(
            communityService.getServiceStatus()
            ) ;
        }

    /**
     * Test the service memory.
     *
     */
    public void testServiceMemory()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityServiceTest.testServiceMemory()") ;
        if (DEBUG_FLAG) System.out.println("  Service : " + communityService.getClass()) ;
		//
		// Get the service status.
        ServiceStatusData status = communityService.getServiceStatus() ;
		//
		// Log the available memory.
        if (DEBUG_FLAG) System.out.println("  Free  memory : " + status.getFreeMemory()) ;
        if (DEBUG_FLAG) System.out.println("  Total memory : " + status.getTotalMemory()) ;
        }


    /**
     * Our target DatabaseManager.
     *
     */
    private DatabaseManager databaseManager ;

    /**
     * Get our target DatabaseManager.
     *
     */
    public DatabaseManager getDatabaseManager()
        {
        return this.databaseManager ;
        }

    /**
     * Set our target DatabaseManager.
     *
     */
    public void setDatabaseManager(DatabaseManager manager)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityServiceTest.setDatabaseManager()") ;
        if (DEBUG_FLAG) System.out.println("  Manager : " + manager.getClass()) ;
        //
        // Set our DatabaseManager reference.
        this.databaseManager = manager ;
        }

    /**
     * Setup our test.
     * Use our DatabaseManager to reset our database tables.
     *
     */
    public void resetDatabase()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityServiceTest:resetDatabase()") ;
        //
        // Use our manager to reset our tables.
        databaseManager.resetDatabaseTables() ;
        }

    }
