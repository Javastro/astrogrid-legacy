/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/FileManager.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/12/16 17:25:49 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManager.java,v $
 *   Revision 1.3  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.24  2004/12/14 14:11:56  dave
 *   Added delete to the server API ....
 *
 *   Revision 1.1.2.23  2004/12/11 05:59:17  dave
 *   Added internal copy for nodes ...
 *   Added local copy for data ...
 *
 *   Revision 1.1.2.22  2004/12/08 17:54:55  dave
 *   Added update to FileManager client and server side ...
 *
 *   Revision 1.1.2.21  2004/12/02 19:11:54  dave
 *   Added move name and parent to manager ...
 *
 *   Revision 1.1.2.20  2004/11/16 06:23:38  dave
 *   Updated commetnts and todo ...
 *
 *   Revision 1.1.2.19  2004/11/16 03:25:37  dave
 *   Updated API to use full ivorn rather than ident ...
 *
 *   Revision 1.1.2.18  2004/11/13 01:39:03  dave
 *   Modifications to support the new client API ...
 *
 *   Revision 1.1.2.17  2004/11/11 16:36:19  dave
 *   Changed getChildren to retunr array of names ...
 *
 *   Revision 1.1.2.16  2004/11/11 15:41:44  dave
 *   Renamed importInitEx and exportInitEx back to the original names ...
 *
 *   Revision 1.1.2.15  2004/11/11 15:30:37  dave
 *   Moving manager API to property[] rather than Node.
 *
 *   Revision 1.1.2.14  2004/11/10 18:32:57  dave
 *   Moved getAccount API to use properties ...
 *
 *   Revision 1.1.2.13  2004/11/10 17:00:11  dave
 *   Moving the manager API towards property based rather than node based ...
 *
 *   Revision 1.1.2.12  2004/11/06 20:03:17  dave
 *   Implemented ImportInit and ExportInit using properties
 *
 *   Revision 1.1.2.11  2004/11/05 05:58:31  dave
 *   Refactored the properties handling in importInitEx() ..
 *
 *   Revision 1.1.2.10  2004/11/05 02:23:45  dave
 *   Refactored identifiers are properties ...
 *
 *   Revision 1.1.2.9  2004/11/04 04:15:34  dave
 *   Sketched out the new business logic for import init ....
 *
 *   Revision 1.1.2.8  2004/11/04 02:33:38  dave
 *   Refactored test to include multiple filestores ...
 *
 *   Revision 1.1.2.7  2004/11/01 16:23:22  dave
 *   Started integrating import and export with FileStore ...
 *
 *   Revision 1.1.2.6  2004/10/21 21:08:52  dave
 *   Added config interface and mock implementation.
 *   Partial implementation of data import into FileStore via direct URL transfer.
 *
 *   Revision 1.1.2.5  2004/10/19 14:52:36  dave
 *   Refactored container and file into just node.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common ;

import java.rmi.Remote ;
import java.rmi.RemoteException ;

import org.astrogrid.filestore.common.file.FileProperty;
import org.astrogrid.filestore.common.transfer.TransferProperties;

import org.astrogrid.filemanager.common.exception.NodeNotFoundException;
import org.astrogrid.filemanager.common.exception.DuplicateNodeException;
import org.astrogrid.filemanager.common.exception.FileManagerServiceException;
import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException;
import org.astrogrid.filemanager.common.exception.FileManagerPropertiesException;

/**
 * The public interface for a file manager service.
 *
 */
public interface FileManager
	extends java.rmi.Remote
	{
	/**
	 * Get the manager identifier.
	 * @return The manager identifier (ivorn).
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
     * @throws RemoteException If the WebService call fails.
	 *
	 */
	public String getIdentifier()
		throws
			RemoteException,
			FileManagerServiceException;

	/**
	 * Create a node for a new account.
	 * @param account The identifier for the account.
	 * @return An array of properties for the new account node.
	 * @throws DuplicateNodeException If the the account already exists.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
     * @throws RemoteException If the WebService call fails.
	 *
	 */
	public FileProperty[] addAccount(String account)
		throws
			RemoteException,
			FileManagerServiceException,
			DuplicateNodeException;

	/**
	 * Get the root node for an account
	 * @param account The identifier of the account.
	 * @return An array of properties for the account node.
	 * @throws NodeNotFoundException If the node does not exist.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
     * @throws RemoteException If the WebService call fails.
	 *
	 */
	public FileProperty[] getAccount(String ident)
		throws
			RemoteException,
			FileManagerServiceException,
			NodeNotFoundException;

	/**
	 * Get a specific node, indexed by ident.
	 * @param ivorn The node identifier.
	 * @return The node specified by the identifier.
	 * @throws FileManagerIdentifierException If the node identifier is invalid.
	 * @throws NodeNotFoundException If the node does not exist.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
     * @throws RemoteException If the WebService call fails.
	 *
	 */
	public FileProperty[] getNode(String ivorn)
		throws
			RemoteException,
			FileManagerIdentifierException,
			FileManagerServiceException,
			NodeNotFoundException;

	/**
	 * Update the properties for a node, indexed by ident.
	 * If this node has stored data, this will trigger a call to the FileStore to update the data properties.
	 * @param ivorn The node identifier.
	 * @return The node properties.
	 * @throws FileManagerIdentifierException If the node identifier is invalid.
	 * @throws NodeNotFoundException If the node does not exist.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
     * @throws RemoteException If the WebService call fails.
	 *
	 */
	public FileProperty[] update(String ivorn)
		throws
			RemoteException,
			FileManagerIdentifierException,
			FileManagerServiceException,
			NodeNotFoundException;

	/**
	 * Get a specific child node, indexed by path.
	 * @param ivorn The identifier of the parent node.
	 * @param path The target node path.
	 * @return The node specified by the path.
	 * @throws FileManagerIdentifierException If the node identifier is invalid.
	 * @throws NodeNotFoundException If the node does not exist.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
     * @throws RemoteException If the WebService call fails.
	 *
	 */
	public FileProperty[] getChild(String ivorn, String path)
		throws
			RemoteException,
			FileManagerIdentifierException,
			FileManagerServiceException,
			NodeNotFoundException;

	/**
	 * Add a new node.
	 * @param ivorn The identifier of the parent node.
	 * @param name  The new node name.
	 * @param type  The new node type.
	 * @return The new node.
	 * @throws FileManagerIdentifierException If the node identifier is invalid.
	 * @throws DuplicateNodeException If a node with the same name already exists.
	 * @throws NodeNotFoundException If the parent node does not exist.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
     * @throws RemoteException If the WebService call fails.
	 * @todo NodeTypeNotSupportedException If the specified type is not supported. ***
	 *
	 */
	public FileProperty[] addNode(String ivorn, String name, String type)
		throws
			RemoteException,
			FileManagerIdentifierException,
			FileManagerServiceException,
			DuplicateNodeException,
			NodeNotFoundException;

	/**
	 * Get the children of a specific node.
	 * @param ivorn The identifier of the parent node.
	 * @return An array of the child node names.
	 * @throws FileManagerIdentifierException If the node identifier is invalid.
	 * @throws NodeNotFoundException If the parent node does not exist.
     * @throws RemoteException If the WebService call fails.
	 * @todo Change this to return a list of the child ivorn(s) ... or 'child' objects (name, ivorn, type).
	 *
	 */
	public String[] getChildren(String ivorn)
		throws
			RemoteException,
			FileManagerIdentifierException, 
			NodeNotFoundException;

	/**
	 * Initialise a data transfer into a FileStore.
	 * The request properties need to specify the identifier of an existing node, or the identifier of a parent node and the new node name.
	 * @param request The request properties.
	 * @throws NodeNotFoundException If the target node does not exist.
	 * @throws FileManagerServiceException If the service is unable to handle the request.
	 * @throws FileManagerPropertiesException If the required properties are incomplete.
     * @throws RemoteException If the WebService call fails.
	 *
	 */
	public TransferProperties importInit(FileProperty[] request)
		throws
			RemoteException,
			FileManagerServiceException,
			FileManagerPropertiesException,
			NodeNotFoundException ;

	/**
	 * Initialise a data transfer from a FileStore.
	 * This calls the FileStore to get the HTTP (GET) URL to read the data from the FileStore.
	 * @param request The request properties.
	 * @throws NodeNotFoundException If the target node does not exist.
	 * @throws FileManagerServiceException If the service is unable to handle the request.
	 * @throws FileManagerPropertiesException If the transfer properties are incomplete.
     * @throws RemoteException If the WebService call fails.
	 *
	 */
	public TransferProperties exportInit(FileProperty[] request)
		throws
			RemoteException,
			NodeNotFoundException,
			FileManagerServiceException,
			FileManagerPropertiesException;

	/**
	 * Create a copy of a node.
	 * If the node already has stored data, then this will create a new copy of the data.
	 * @param  properties The request properties.
	 * @return A set of properties for the new node.
	 * @throws DuplicateNodeException If a node with the same name already exists in the metadata tree.
	 * @throws NodeNotFoundException If the current node is no longer in the database.
	 * @throws NodeNotFoundException If the new parent node is no longer in the database.
	 * @throws FileManagerPropertiesException If the transfer properties are incomplete.
	 * @throws FileManagerServiceException If a problem occurs when handling the request.
     * @throws RemoteException If the WebService call fails.
	 *
	 */
	public FileProperty[] copy(FileProperty[] request)
		throws
			RemoteException,
			NodeNotFoundException,
			DuplicateNodeException,
			FileManagerPropertiesException,
			FileManagerServiceException;

	/**
	 * Move a node to a new location.
	 * If the node already has stored data, then this may involve transfering the data to a new location.
	 * @param  properties The request properties.
	 * @return A new set of properties for the node.
	 * @throws DuplicateNodeException If a node with the same name already exists in the metadata tree.
	 * @throws NodeNotFoundException If the current node is no longer in the database.
	 * @throws NodeNotFoundException If the new parent node is no longer in the database.
	 * @throws FileManagerPropertiesException If the transfer properties are incomplete.
	 * @throws FileManagerServiceException If a problem occurs when handling the request.
     * @throws RemoteException If the WebService call fails.
	 *
	 */
	public FileProperty[] move(FileProperty[] request)
		throws
			RemoteException,
			NodeNotFoundException,
			DuplicateNodeException,
			FileManagerPropertiesException,
			FileManagerServiceException;

	/**
	 * Delete a node.
	 * @param ident The node identifier.
	 * @throws NodeNotFoundException If the node does not exist.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
     * @throws RemoteException If the WebService call fails.
	 *
	 */
	public void delete(String ident)
		throws
			RemoteException,
			FileManagerServiceException,
			NodeNotFoundException;

	}
