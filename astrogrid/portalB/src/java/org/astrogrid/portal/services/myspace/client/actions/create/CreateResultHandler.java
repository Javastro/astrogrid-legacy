/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/actions/create/Attic/CreateResultHandler.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/23 23:21:12 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: CreateResultHandler.java,v $
 * Revision 1.1  2003/06/23 23:21:12  dave
 * Updated the page actions
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client.actions.create ;

import org.astrogrid.portal.services.myspace.client.data.DataNode ;
import org.astrogrid.portal.services.myspace.client.data.DataNodeHandler ;

import org.astrogrid.portal.services.myspace.client.status.StatusNode ;
import org.astrogrid.portal.services.myspace.client.status.StatusNodeHandler ;

/**
 * An interface for something that can handle create container results.
 *
 */
public interface CreateResultHandler
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
