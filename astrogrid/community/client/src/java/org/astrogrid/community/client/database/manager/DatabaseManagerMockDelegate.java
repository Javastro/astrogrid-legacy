/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/database/manager/DatabaseManagerMockDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:19 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerMockDelegate.java,v $
 *   Revision 1.5  2004/06/18 13:45:19  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.4.38.2  2004/06/17 15:10:02  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.4.38.1  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.database.manager ;

import org.astrogrid.community.common.database.manager.DatabaseManagerMock ;

/**
 * Mock delegate for our DatabaseManager service.
 *
 */
public class DatabaseManagerMockDelegate
    extends DatabaseManagerCoreDelegate
    implements DatabaseManagerDelegate
    {
    /**
     * Public constructor.
     *
     */
    public DatabaseManagerMockDelegate()
        {
        super() ;
        //
        // Set our DatabaseManager service.
        this.setDatabaseManager(
            new DatabaseManagerMock()
            ) ;
        }
    }
