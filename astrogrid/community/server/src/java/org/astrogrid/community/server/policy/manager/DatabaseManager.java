/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/policy/manager/Attic/DatabaseManager.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/01/07 10:45:45 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManager.java,v $
 *   Revision 1.2  2004/01/07 10:45:45  dave
 *   Merged development branch, dave-dev-20031224, back into HEAD
 *
 *   Revision 1.1.2.1  2003/12/24 05:54:48  dave
 *   Initial Maven friendly structure (only part of the service implemented)
 *
 *   Revision 1.3  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.2  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
 *
 *   Revision 1.1  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.policy.manager ;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException ;

public interface DatabaseManager
    {

    /**
     * Access to our database.
     *
     */
    public Database getDatabase() ;

    /**
     * Create an object in the database.
     *
    public void create(Object ident)
        throws PersistenceException ;
     */

    /**
     * Select an object from the database.
     *
    public Object select(Class type, Object ident)
        throws PersistenceException ;
     */

    /**
     * Update an object in the database.
     *
    public void update(Object object)
        throws PersistenceException ;
     */

    /**
     * Delete an object in the database.
     *
    public void delete(Class type, Object ident)
        throws PersistenceException ;
     */

    }
