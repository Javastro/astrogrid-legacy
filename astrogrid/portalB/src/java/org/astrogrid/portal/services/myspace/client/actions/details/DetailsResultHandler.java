/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/actions/details/Attic/DetailsResultHandler.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/22 04:03:41 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: DetailsResultHandler.java,v $
 * Revision 1.1  2003/06/22 04:03:41  dave
 * Added actions and parsers for MySpace messages
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client.actions.details ;

import org.astrogrid.portal.services.myspace.client.data.DataNode ;
import org.astrogrid.portal.services.myspace.client.data.DataNodeHandler ;

import org.astrogrid.portal.services.myspace.client.status.StatusNode ;
import org.astrogrid.portal.services.myspace.client.status.StatusNodeHandler ;

/**
 * An interface for something that can handle details results.
 *
 */
public interface DetailsResultHandler
	extends DataNodeHandler, StatusNodeHandler
	{

	/**
	 * Handle a DataNode.
	 *
	 */
	public void handleDataNode(DataNode node) ;

	/**
	 * Handle a StatusNode.
	 *
	 */
	public void handleStatusNode(StatusNode status) ;

	}
