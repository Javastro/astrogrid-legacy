/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/Attic/FileManagerDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/12/16 17:25:49 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerDelegate.java,v $
 *   Revision 1.3  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.6  2004/12/08 17:54:54  dave
 *   Added update to FileManager client and server side ...
 *
 *   Revision 1.1.2.5  2004/12/02 19:11:54  dave
 *   Added move name and parent to manager ...
 *
 *   Revision 1.1.2.4  2004/11/29 18:05:07  dave
 *   Refactored methods names ....
 *   Added stubs for delete, copy and move.
 *
 *   Revision 1.1.2.3  2004/11/24 16:15:08  dave
 *   Added node functions to client ...
 *
 *   Revision 1.1.2.2  2004/11/16 03:26:14  dave
 *   Added initial tests for adding accounts, containers and files ...
 *
 *   Revision 1.1.2.1  2004/11/13 01:41:26  dave
 *   Created initial client API ....
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.client ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filemanager.common.exception.NodeNotFoundException ;
import org.astrogrid.filemanager.common.exception.DuplicateNodeException;
import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException;
import org.astrogrid.filemanager.common.exception.FileManagerServiceException;

/**
 * The public API for a FileManager delegate.
 *
 */
public interface FileManagerDelegate
	{
	/**
	 * Get the manager identifier.
	 * @return The manager ivorn identifier.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
	 *
	 */
	public Ivorn getServiceIvorn()
		throws
			FileManagerServiceException;

	/**
	 * Create a node for a new account.
	 * @param ivorn The ivorn identifier for the account.
	 * @return A node representing the account home.
	 * @throws DuplicateNodeException If the the account already exists.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
	 *
	 */
	public FileManagerNode addAccount(Ivorn ident)
		throws
			FileManagerServiceException,
			DuplicateNodeException;

	/**
	 * Get the root node for an account
	 * @param ivorn The identifier of the account.
	 * @return An new node for the account home.
	 * @throws NodeNotFoundException If the account does not exist.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
	 *
	 */
	public FileManagerNode getAccount(Ivorn ivorn)
		throws
			FileManagerServiceException,
			NodeNotFoundException;

	/**
	 * Get a node from the node Ivorn
	 * @param ivorn The identifier of the node.
	 * @return The node for the ivorn.
	 * @throws NodeNotFoundException If the node does not exist.
	 * @throws FileManagerIdentifierException If the node identifier is invalid.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
	 *
	 */
	public FileManagerNode getNode(Ivorn ivorn)
		throws
			FileManagerServiceException,
			FileManagerIdentifierException,
			NodeNotFoundException;

	}

