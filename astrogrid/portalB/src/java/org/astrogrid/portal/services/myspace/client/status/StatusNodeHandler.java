/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/status/Attic/StatusNodeHandler.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/22 04:03:41 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: StatusNodeHandler.java,v $
 * Revision 1.1  2003/06/22 04:03:41  dave
 * Added actions and parsers for MySpace messages
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client.status ;

import java.util.Map ;
import java.util.TreeMap ;
import java.util.Iterator ;

/**
 * Interface for something that can handle a StatusNode.
 *
 */
public interface StatusNodeHandler
	{
	/**
	 * Handle a StatusNode.
	 *
	 */
	public void handleStatusNode(StatusNode status) ;
	}
