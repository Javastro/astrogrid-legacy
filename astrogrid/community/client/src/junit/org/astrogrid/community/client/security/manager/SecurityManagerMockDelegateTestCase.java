/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/junit/org/astrogrid/community/client/security/manager/SecurityManagerMockDelegateTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerMockDelegateTestCase.java,v $
 *   Revision 1.7  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.6.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.6  2004/06/18 13:45:19  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.5.38.1  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.security.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.community.client.policy.manager.PolicyManagerMockDelegate ;
import org.astrogrid.community.client.database.manager.DatabaseManagerMockDelegate ;
import org.astrogrid.community.client.security.manager.SecurityManagerMockDelegate ;
import org.astrogrid.community.client.security.service.SecurityServiceMockDelegate ;

import org.astrogrid.community.common.security.manager.SecurityManagerTest ;

/**
 * Test case for our SecurityManagerMockDelegate.
 *
 */
public class SecurityManagerMockDelegateTestCase
    extends SecurityManagerTest
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(SecurityManagerMockDelegateTestCase.class);

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
        //
        // Reset our database tables.
        this.resetDatabase() ;
        }
    }
