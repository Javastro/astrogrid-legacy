/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/junit/org/astrogrid/community/common/security/manager/SecurityManagerTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerTestCase.java,v $
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
package org.astrogrid.community.common.security.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.community.common.database.manager.DatabaseManager ;
import org.astrogrid.community.common.database.manager.DatabaseManagerMock ;

import org.astrogrid.community.common.policy.manager.AccountManager ;
import org.astrogrid.community.common.policy.manager.AccountManagerMock ;

import org.astrogrid.community.common.security.manager.SecurityManager ;
import org.astrogrid.community.common.security.manager.SecurityManagerMock ;

/**
 * A JUnit test case for our mock SecurityManager.
 *
 */
public class SecurityManagerTestCase
    extends SecurityManagerTest
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(SecurityManagerTestCase.class);

    /**
     * Setup our test.
     * Creates a mock SecurityManager to test.
     *
     */
    public void setUp()
        throws Exception
        {
        //
        // Set out test targets.
        this.setDatabaseManager(
            new DatabaseManagerMock()
            ) ;
        this.setAccountManager(
            new AccountManagerMock()
            ) ;
        this.setSecurityManager(
            new SecurityManagerMock()
            ) ;
        //
        // Reset our database tables.
        this.resetDatabase() ;
        }
    }
