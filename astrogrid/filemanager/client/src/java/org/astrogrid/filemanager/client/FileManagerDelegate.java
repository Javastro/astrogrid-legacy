/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/Attic/FileManagerDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:20:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerDelegate.java,v $
 *   Revision 1.2  2004/11/25 00:20:29  jdt
 *   Merge from dave-dev-200410061224-200411221626
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


	}

