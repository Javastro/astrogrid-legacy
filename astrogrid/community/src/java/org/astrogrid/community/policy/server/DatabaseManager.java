/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/DatabaseManager.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/06 20:10:07 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManager.java,v $
 *   Revision 1.1  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server ;

import org.exolab.castor.jdo.Database;

public interface DatabaseManager
	{

	/**
	 * Access to our database.
	 *
	 */
	public Database getDatabase() ;

	}
