package org.astrogrid.portal.services.myspace.client.actions.createUser ;

import org.astrogrid.portal.services.myspace.client.data.DataNode ;
import org.astrogrid.portal.services.myspace.client.data.DataNodeHandler ;

import org.astrogrid.portal.services.myspace.client.status.StatusNode ;
import org.astrogrid.portal.services.myspace.client.status.StatusNodeHandler ;

/**
 * An interface for something that can handle copy results.
 *
 */
public interface CreateUserResultHandler
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
