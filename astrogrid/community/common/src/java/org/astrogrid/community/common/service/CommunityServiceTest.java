/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/service/CommunityServiceTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityServiceTest.java,v $
 *   Revision 1.4  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.3.16.1  2004/03/22 15:31:10  dave
 *   Added CommunitySecurityException.
 *   Updated SecurityManager and SecurityService to use Exceptions.
 *
 *   Revision 1.3  2004/03/08 13:42:33  dave
 *   Updated Maven goals.
 *   Replaced tabs with Spaces.
 *
 *   Revision 1.2.2.1  2004/03/08 12:53:17  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.2  2004/03/02 23:31:00  dave
 *   Added DatabaseManager to service tests
 *
 *   Revision 1.1.2.1  2004/02/23 19:43:47  dave
 *   Refactored DatabaseManager tests to test the interface.
 *   Refactored DatabaseManager tests to use common DatabaseManagerTest.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.service ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.common.junit.JUnitTestBase ;

import org.astrogrid.community.common.database.manager.DatabaseManager ;

import org.astrogrid.community.common.service.data.ServiceStatusData ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
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
     * @todo Check that the service returns useful info.
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
            "getServiceStatus returned NULL",
            communityService.getServiceStatus()
            ) ;
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
