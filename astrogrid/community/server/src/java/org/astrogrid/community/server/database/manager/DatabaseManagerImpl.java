/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/database/manager/Attic/DatabaseManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerImpl.java,v $
 *   Revision 1.6  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.5.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.5  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.4.52.2  2004/06/17 15:24:31  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.4.52.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.database.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL ;

import org.exolab.castor.jdo.JDO ;

import org.astrogrid.community.common.database.manager.DatabaseManager ;

import org.astrogrid.community.server.service.CommunityServiceImpl ;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;

/**
 * Server side implementation of the DatabaseManager service.
 *
 */
public class DatabaseManagerImpl
    extends CommunityServiceImpl
    implements DatabaseManager
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(DatabaseManagerImpl.class);

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

    /**
     * Get the current database name.
     *
     */
    public String getDatabaseName()
        {
        String result = null ;
        if (null != this.getDatabaseConfiguration())
            {
            result = this.getDatabaseConfiguration().getDatabaseName() ;
            }
        return result ;
        }

    /**
     * Get our JDO configuration resource name.
     *
     */
    public String getDatabaseConfigResource()
        {
        String result = null ;
        if (null != this.getDatabaseConfiguration())
            {
            result = this.getDatabaseConfiguration().getDatabaseConfigResource() ;
            }
        return result ;
        }

    /**
     * Get the database SQL script name.
     *
     */
    public String getDatabaseScriptResource()
        {
        String result = null ;
        if (null != this.getDatabaseConfiguration())
            {
            result = this.getDatabaseConfiguration().getDatabaseScriptResource() ;
            }
        return result ;
        }

    /**
     * Get the database configuration URL.
     *
     */
    public String getDatabaseConfigUrl()
        {
        String result = null ;
        if (null != this.getDatabaseConfiguration())
            {
            URL url = this.getDatabaseConfiguration().getDatabaseConfigUrl() ;
            if (null != url)
                {
                result = url.toString() ;
                }
            }
        return result ;
        }

    /**
     * Get the database engine description.
     *
     */
    public String getDatabaseDescription()
        {
        String result = null ;
        if (null != this.getDatabaseConfiguration())
            {
            JDO jdo = this.getDatabaseConfiguration().getDatabaseEngine() ;
            if (null != jdo)
                {
                result = jdo.getDescription() ;
                }
            }
        return result ;
        }

    /**
     * Check our database tables.
     *
     */
    public boolean checkDatabaseTables()
        {
        boolean result = false ;
        if (null != this.getDatabaseConfiguration())
            {
            try {
                result = this.getDatabaseConfiguration().checkDatabaseTables() ;
                }
            catch (Exception ouch)
                {
                logException(ouch, "DatabaseManagerImpl.checkDatabaseTables()") ;
                }
            }
        return result ;
        }

    /**
     * Create our database tables.
     * Use with care ... this may delete any existing data.
     *
     */
    public void resetDatabaseTables()
        {
        if (null != this.getDatabaseConfiguration())
            {
            try {
                this.getDatabaseConfiguration().resetDatabaseTables() ;
                }
            catch (Exception ouch)
                {
                logException(ouch, "DatabaseManagerImpl.createDatabaseTables()") ;
                }
            }
        }
    }



