/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/database/Attic/DatabaseConfigurationFactory.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/20 21:11:05 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseConfigurationFactory.java,v $
 *   Revision 1.2  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.1.2.2  2004/02/20 19:34:11  dave
 *   Added JNDI Resource for community database.
 *   Removed multiple calls to loadDatabaseConfiguration .
 *
 *   Revision 1.1.2.1  2004/02/19 14:51:00  dave
 *   Changed DatabaseManager to DatabaseConfigurationFactory.
 *
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

/**
 * An interface to handle database configurations.
 *
 */
public interface DatabaseConfigurationFactory
    {

    /**
     * Access to a named database.
     * This looks for a matching DatabaseConfiguration and returns a new JDO Database connection.
     *
     */
    public Database getDatabase(String name)
        throws DatabaseNotFoundException, PersistenceException ;

    /**
     * Access to a named database configuration.
     *
    public DatabaseConfiguration getDatabaseConfiguration(String name) ;
     */

    }

