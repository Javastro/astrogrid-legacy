/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/junit/org/astrogrid/community/client/security/service/SecurityServiceMockDelegateTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceMockDelegateTestCase.java,v $
 *   Revision 1.4  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.3.14.1  2004/03/19 03:31:21  dave
 *   Changed AccountManagerMock to recognise DatabaseManager reset()
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
 *   Revision 1.1.2.1  2004/03/04 17:13:30  dave
 *   Added DatabaseManager delegates
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.security.service ;

import org.astrogrid.community.client.policy.manager.PolicyManagerMockDelegate ;
import org.astrogrid.community.client.database.manager.DatabaseManagerMockDelegate ;
import org.astrogrid.community.client.security.manager.SecurityManagerMockDelegate ;
import org.astrogrid.community.client.security.service.SecurityServiceMockDelegate ;

import org.astrogrid.community.common.security.service.SecurityServiceTest ;

/**
 * A JUnit test case for our SecurityService service.
 *
 */
public class SecurityServiceMockDelegateTestCase
    extends SecurityServiceTest
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Setup our test.
     * Creates the Mock delegates to test.
     *
     */
    public void setUp()
        throws Exception
        {
        //
        // Create our test targets.
        this.setDatabaseManager(
            new DatabaseManagerMockDelegate()
            ) ;
        this.setAccountManager(
            new PolicyManagerMockDelegate()
            ) ;
        this.setSecurityManager(
            new SecurityManagerMockDelegate()
            ) ;
        this.setSecurityService(
            new SecurityServiceMockDelegate()
            ) ;
        //
        // Reset our database tables.
        this.resetDatabase() ;
        }
    }
