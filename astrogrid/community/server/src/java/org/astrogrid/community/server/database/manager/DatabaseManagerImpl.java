/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/database/manager/Attic/DatabaseManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/20 21:11:05 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerImpl.java,v $
 *   Revision 1.2  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.1.2.1  2004/02/19 21:09:26  dave
 *   Refactored ServiceStatusData into a common package.
 *   Refactored CommunityServiceImpl constructor to take a parent service.
 *   Refactored default database for CommunityServiceImpl
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.database.manager ;

import java.rmi.RemoteException ;

import org.astrogrid.community.common.config.CommunityConfig ;

import org.astrogrid.community.common.database.manager.DatabaseManager ;

import org.astrogrid.community.server.common.CommunityServiceImpl ;
import org.astrogrid.community.server.database.DatabaseConfiguration ;

public class DatabaseManagerImpl
    extends CommunityServiceImpl
    implements DatabaseManager
    {
    /**
     * Switch for our debug statements.
     *
     */
    protected static final boolean DEBUG_FLAG = true ;

    /**
     * Public constructor, using default database configuration.
     *
     */
    public DatabaseManagerImpl()
        {
        super() ;
        }

    /**
     * Public constructor, using specific database configuration.
     *
     */
    public DatabaseManagerImpl(DatabaseConfiguration config)
        {
        super(config) ;
        }

    }



