/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/actions/extendlease/Attic/ExtendleaseResultHandler.java,v $</cvs:source>
 * <cvs:date>$Author: clq2 $</cvs:date>
 * <cvs:author>$Date: 2003/09/09 13:56:43 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: ExtendleaseResultHandler.java,v $
 * Revision 1.1  2003/09/09 13:56:43  clq2
 * MySpace new UI
 *
 * Revision 1.1  2003/06/22 04:03:41  dave
 * Added actions and parsers for MySpace messages
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client.actions.extendlease ;

import org.astrogrid.portal.services.myspace.client.data.DataNode ;
import org.astrogrid.portal.services.myspace.client.data.DataNodeHandler ;

import org.astrogrid.portal.services.myspace.client.status.StatusNode ;
import org.astrogrid.portal.services.myspace.client.status.StatusNodeHandler ;

/**
 * An interface for something that can handle copy results.
 *
 */
public interface ExtendleaseResultHandler
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
