/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/junit/org/astrogrid/community/common/policy/manager/ResourceManagerTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ResourceManagerTestCase.java,v $
 *   Revision 1.3  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.2.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.2  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.1.2.2  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.community.common.database.manager.DatabaseManager ;
import org.astrogrid.community.common.database.manager.DatabaseManagerMock ;

/**
 * A JUnit test using a ResourceManagerMock service.
 *
 */
public class ResourceManagerTestCase
    extends ResourceManagerTest
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(ResourceManagerTestCase.class);

    /**
     * Setup our test.
     * Creates a target ResourceManagerMock.
     *
     */
    public void setUp()
        throws Exception
        {
        //
        // Set up test targets.
        this.setDatabaseManager(
            new DatabaseManagerMock()
            ) ;
        this.setResourceManager(
            new ResourceManagerMock()
            ) ;
        //
        // Reset our database tables.
        this.resetDatabase() ;
        }
    }
