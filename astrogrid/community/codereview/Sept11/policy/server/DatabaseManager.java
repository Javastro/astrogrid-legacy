/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/codereview/Sept11/policy/server/Attic/DatabaseManager.java,v $</cvs:source>
 * <cvs:author>$Author: KevinBenson $</cvs:author>
 * <cvs:date>$Date: 2003/09/11 10:24:21 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManager.java,v $
 *   Revision 1.1  2003/09/11 10:24:21  KevinBenson
 *   *** empty log message ***
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
package org.astrogrid.community.policy.server ;

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
