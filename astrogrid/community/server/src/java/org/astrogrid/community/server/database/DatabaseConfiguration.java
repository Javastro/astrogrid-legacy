/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/database/Attic/DatabaseConfiguration.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/12 08:12:13 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseConfiguration.java,v $
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.1  2004/01/26 13:18:08  dave
 *   Added new DatabaseManager to enable local JUnit testing
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.database ;

import org.exolab.castor.jdo.Database ;
import org.exolab.castor.jdo.DatabaseNotFoundException ;
import org.exolab.castor.jdo.PersistenceException ;

import java.net.URL ;

/**
 * An interface to handle the configuration data for a database connection.
 *
 */
public interface DatabaseConfiguration
    {

    /**
     * Access to our config filename.
     *
     */
    public String getConfigName() ;

    /**
     * Access to our config URL.
     *
     */
    public URL getConfigUrl() ;

    /**
     * Access to our database name.
     *
     */
    public String getName() ;

    /**
     * Get a new JDO database connection.
     * This will open a connection, even if the database files are not there.
     *
     */
    public Database getDatabase()
        throws DatabaseNotFoundException, PersistenceException ;

    }

