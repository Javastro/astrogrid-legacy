/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/database/manager/DatabaseManagerMockDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerMockDelegate.java,v $
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/04 17:13:30  dave
 *   Added DatabaseManager delegates
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.database.manager ;

import org.astrogrid.community.common.database.manager.DatabaseManager ;
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
	 * Switch for our debug statements.
	 *
	 */
	private static boolean DEBUG_FLAG = true ;

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
