/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/Attic/FileManagerCoreDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:20:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerCoreDelegate.java,v $
 *   Revision 1.2  2004/11/25 00:20:29  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.6  2004/11/18 14:39:32  dave
 *   Added SOAP delegate, RemoteException decoding and test case.
 *
 *   Revision 1.1.2.5  2004/11/17 07:56:33  dave
 *   Added server mock and webapp build scripts ...
 *
 *   Revision 1.1.2.4  2004/11/16 07:56:08  dave
 *   Added last set of tests for delegate ....
 *
 *   Revision 1.1.2.3  2004/11/16 06:22:20  dave
 *   Added tests for child nodes and paths ...
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

import java.net.URISyntaxException;

import java.rmi.RemoteException ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filestore.common.file.FileProperty;
import org.astrogrid.filestore.common.transfer.TransferProperties;

import org.astrogrid.filemanager.common.FileManager;
import org.astrogrid.filemanager.common.FileManagerProperties;

import org.astrogrid.filemanager.common.exception.NodeNotFoundException ;
import org.astrogrid.filemanager.common.exception.DuplicateNodeException;
import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException;
import org.astrogrid.filemanager.common.exception.FileManagerServiceException;
import org.astrogrid.filemanager.common.exception.FileManagerPropertiesException;


/**
 * The core implementation for the FileManager delegate.
 *
 */
public class FileManagerCoreDelegate
	implements FileManagerDelegate
	{

    /**
     * Our debug logger.
     *
     */
    protected static Log log = LogFactory.getLog(FileManagerCoreDelegate.class);

	/**
	 * Reference to our FileManager service.
	 *
	 */
	protected FileManager manager ;

	/**
	 * Protected constructor.
	 *
	 */
	public FileManagerCoreDelegate()
		{
		}

	/**
	 * Protected constructor, for a FileManager service.
	 *
	 */
	public FileManagerCoreDelegate(FileManager manager)
		{
		this.manager = manager ;
		}

	/**
	 * Get the manager identifier.
	 * @return The manager ivorn identifier.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
	 *
	 */
	public Ivorn getServiceIvorn()
		throws FileManagerServiceException
		{
		try {
			return new Ivorn(
				manager.getIdentifier()
				) ;
			}
		catch (URISyntaxException ouch)
			{
			throw new FileManagerServiceException(
				"Unable to parse service ivorn"
				) ;
			}
		catch (RemoteException ouch)
			{
			//
			// Look for the expected exceptions.
			serviceException(ouch);
			//
			// Throw a generic service exception.
			throw new FileManagerServiceException(
				"Error occurred in WebService call",
				ouch
				) ;
			}
		}

	/**
	 * Create a node from an array of properties.
	 * @param properties The node properties.
	 * @return A new FileManagerNode constructed from the properties.
	 * @todo Distinguish between the node types ....
	 *
	 */
	protected FileManagerNode node(FileProperty[] properties)
		{
		return new FileManagerNodeImpl(
			properties
			);
		}

	/**
	 * Create a node for a new account.
	 * @param ivorn The ivorn identifier for the account.
	 * @return A node representing the account home.
	 * @throws DuplicateNodeException If the the account already exists.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
	 *
	 */
	public FileManagerNode addAccount(Ivorn ivorn)
		throws FileManagerServiceException, DuplicateNodeException
		{
		if (null == ivorn)
			{
			throw new IllegalArgumentException(
				"Null account identifier"
				);
			}
		try {
			return this.node(
				manager.addAccount(
					ivorn.toString()
					)
				);
			}
		catch (RemoteException ouch)
			{
			//
			// Look for the expected exceptions.
			serviceException(ouch);
			duplicateNodeException(ouch);
			//
			// Throw a generic service exception.
			throw new FileManagerServiceException(
				"Error occurred in WebService call",
				ouch
				) ;
			}
		}

	/**
	 * Get the root node for an account
	 * @param ivorn The identifier of the account.
	 * @return An new node for the account home.
	 * @throws NodeNotFoundException If the account does not exist.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
	 *
	 */
	public FileManagerNode getAccount(Ivorn ivorn)
		throws FileManagerServiceException, NodeNotFoundException
		{
		if (null == ivorn)
			{
			throw new IllegalArgumentException(
				"Null account identifier"
				);
			}
		try {
			return this.node(
				manager.getAccount(
					ivorn.toString()
					)
				);
			}
		catch (RemoteException ouch)
			{
			//
			// Look for the expected exceptions.
			serviceException(ouch);
			nodeNotFoundException(ouch);
			//
			// Throw a generic service exception.
			throw new FileManagerServiceException(
				"Error occurred in WebService call",
				ouch
				) ;
			}
		}

	/**
	 * Add a container node.
	 * @param parent The parent node.
	 * @param name The container name.
	 * @return A new node for the container.
	 * @throws FileManagerIdentifierException If the parent identifier is invalid.
	 * @throws NodeNotFoundException If the parent node does not exist.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
	 *
	 */
	protected FileManagerNode addContainer(FileManagerNode parent, String name)
		throws FileManagerServiceException, FileManagerIdentifierException, DuplicateNodeException, NodeNotFoundException
		{
		if (null == parent)
			{
			throw new IllegalArgumentException(
				"Null parent node"
				);
			}
		if (null == parent.getIvorn())
			{
			throw new IllegalArgumentException(
				"Null parent identifier"
				);
			}
		if (null == name)
			{
			throw new IllegalArgumentException(
				"Null container name"
				);
			}
		//
		// Check the parent is a container.
		if (parent.isContainer())
			{
			//
			// Try to create the node.
			try {
				return this.node(
					manager.addNode(
						parent.getIvorn().toString(),
						name,
						FileManagerProperties.CONTAINER_NODE_TYPE
						)
					) ;
				}
			catch (RemoteException ouch)
				{
				//
				// Look for the expected exceptions.
				serviceException(ouch);
				identifierException(ouch);
				duplicateNodeException(ouch);
				nodeNotFoundException(ouch);
				//
				// Throw a generic service exception.
				throw new FileManagerServiceException(
					"Error occurred in WebService call",
					ouch
					) ;
				}
			}
		//
		// If the parent is not a container.
		else {
			throw new IllegalArgumentException(
				"Parent node is not a container"
				);
			}
		}

	/**
	 * Add a file node.
	 * @param parent The parent node.
	 * @param name The file name.
	 * @return A new node for the file.
	 * @throws FileManagerIdentifierException If the parent identifier is invalid.
	 * @throws NodeNotFoundException If the parent node does not exist.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
	 *
	 */
	protected FileManagerNode addFile(FileManagerNode parent, String name)
		throws FileManagerServiceException, FileManagerIdentifierException, DuplicateNodeException, NodeNotFoundException
		{
		if (null == parent)
			{
			throw new IllegalArgumentException(
				"Null parent node"
				);
			}
		if (null == parent.getIvorn())
			{
			throw new IllegalArgumentException(
				"Null parent identifier"
				);
			}
		if (null == name)
			{
			throw new IllegalArgumentException(
				"Null file name"
				);
			}
		//
		// Check the parent is a container.
		if (parent.isContainer())
			{
			//
			// Try to create the node.
			try {
				return this.node(
					manager.addNode(
						parent.getIvorn().toString(),
						name,
						FileManagerProperties.DATA_NODE_TYPE
						)
					) ;
				}
			catch (RemoteException ouch)
				{
				//
				// Look for the expected exceptions.
				serviceException(ouch);
				identifierException(ouch);
				duplicateNodeException(ouch);
				nodeNotFoundException(ouch);
				//
				// Throw a generic service exception.
				throw new FileManagerServiceException(
					"Error occurred in WebService call",
					ouch
					) ;
				}
			}
		//
		// If the parent is not a container.
		else {
			throw new IllegalArgumentException(
				"Parent node is not a container"
				);
			}
		}

	/**
	 * Get a specific node, indexed by ident.
	 * @param ivorn The node identifier.
	 * @return The node specified by the identifier.
	 * @throws FileManagerIdentifierException If the node identifier is invalid.
	 * @throws NodeNotFoundException If the node does not exist.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
	 *
	 */
	protected FileManagerNode getNode(Ivorn ivorn)
		throws FileManagerServiceException, FileManagerIdentifierException, NodeNotFoundException
		{
		if (null == ivorn)
			{
			throw new IllegalArgumentException(
				"Null identifier"
				);
			}
		try {
			return this.node(
				manager.getNode(
					ivorn.toString()
					)
				);
			}
		catch (RemoteException ouch)
			{
			//
			// Look for the expected exceptions.
			serviceException(ouch);
			identifierException(ouch);
			nodeNotFoundException(ouch);
			//
			// Throw a generic service exception.
			throw new FileManagerServiceException(
				"Error occurred in WebService call",
				ouch
				) ;
			}
		}

	/**
	 * Get a specific child node, indexed by path.
	 * @param ivorn The identifier of the parent node.
	 * @param path  The target node path.
	 * @return The node specified by the path.
	 * @throws FileManagerIdentifierException If the node identifier is invalid.
	 * @throws NodeNotFoundException If the node does not exist.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
	 *
	 */
	protected FileManagerNode getChild(Ivorn ivorn, String path)
		throws FileManagerServiceException, FileManagerIdentifierException, NodeNotFoundException
		{
		if (null == ivorn)
			{
			throw new IllegalArgumentException(
				"Null identifier"
				);
			}
		if (null == path)
			{
			throw new IllegalArgumentException(
				"Null path"
				);
			}
		try {
			return this.node(
				manager.getChild(
					ivorn.toString(),
					path
					)
				);
			}
		catch (RemoteException ouch)
			{
			//
			// Look for the expected exceptions.
			serviceException(ouch);
			identifierException(ouch);
			nodeNotFoundException(ouch);
			//
			// Throw a generic service exception.
			throw new FileManagerServiceException(
				"Error occurred in WebService call",
				ouch
				) ;
			}
		}

	/**
	 * Get the children of a specific node.
	 * @param ivorn The identifier of the parent node.
	 * @return An array of the child node names.
	 * @throws FileManagerIdentifierException If the node identifier is invalid.
	 * @throws NodeNotFoundException If the parent node does not exist.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
	 * @todo Change this to return a list of the child ivorn(s) ... or 'child' objects (name, ivorn, type).
	 *
	 */
	protected String[] getChildren(Ivorn ivorn)
		throws FileManagerServiceException, FileManagerIdentifierException, NodeNotFoundException
		{
		if (null == ivorn)
			{
			throw new IllegalArgumentException(
				"Null identifier"
				);
			}
		try {
			return manager.getChildren(
				ivorn.toString()
				);
			}
		catch (RemoteException ouch)
			{
			//
			// Look for the expected exceptions.
			serviceException(ouch);
			identifierException(ouch);
			nodeNotFoundException(ouch);
			//
			// Throw a generic service exception.
			throw new FileManagerServiceException(
				"Error occurred in WebService call",
				ouch
				) ;
			}
		}

	/**
	 * Initialise a data transfer into a FileStore.
	 * @param request The request properties.
	 * @throws NodeNotFoundException If the target node does not exist.
	 * @throws FileManagerServiceException If the service is unable to handle the request.
	 * @throws FileManagerPropertiesException If the required properties are incomplete.
	 *
	 */
	protected TransferProperties importInit(FileManagerProperties request)
		throws FileManagerServiceException, FileManagerPropertiesException, NodeNotFoundException
		{
		if (null == request)
			{
			throw new IllegalArgumentException(
				"Null request properties"
				);
			}
		try {
			return manager.importInit(
				request.toArray()
				);
			}
		catch (RemoteException ouch)
			{
			//
			// Look for the expected exceptions.
			serviceException(ouch);
			propertiesException(ouch);
			nodeNotFoundException(ouch);
			//
			// Throw a generic service exception.
			throw new FileManagerServiceException(
				"Error occurred in WebService call",
				ouch
				) ;
			}
		}

	/**
	 * Initialise a data transfer from a FileStore.
	 * @param request The request properties.
	 * @throws NodeNotFoundException If the target node does not exist.
	 * @throws FileManagerServiceException If the service is unable to handle the request.
	 * @throws FileManagerPropertiesException If the required properties are incomplete.
	 *
	 */
	protected TransferProperties exportInit(FileManagerProperties request)
		throws FileManagerServiceException, FileManagerPropertiesException, NodeNotFoundException
		{
		if (null == request)
			{
			throw new IllegalArgumentException(
				"Null request properties"
				);
			}
		try {
			return manager.exportInit(
				request.toArray()
				);
			}
		catch (RemoteException ouch)
			{
			//
			// Look for the expected exceptions.
			serviceException(ouch);
			propertiesException(ouch);
			nodeNotFoundException(ouch);
			//
			// Throw a generic service exception.
			throw new FileManagerServiceException(
				"Error occurred in WebService call",
				ouch
				) ;
			}
		}

	/**
	 * A converter utility to unpack a FileStoreServiceException from a RemoteException.
	 * @throws FileStoreServiceException If the RemoteException cause was a FileStoreServiceException.
	 *
	 */
	public void serviceException(RemoteException ouch)
		throws FileManagerServiceException
		{
		log.debug("----") ;
		log.debug("FileManagerCoreDelegate.serviceException") ;
		log.debug("  Exception : " + ouch) ;
		log.debug("  Type      : " + ouch.getClass()) ;
		log.debug("  Cause     : " + ouch.getCause()) ;
		//
		// If we have the original Exception.
		if (ouch.getCause() != null)
			{
			log.debug("  Got cause") ;
			if (ouch.getCause() instanceof FileManagerServiceException)
				{
				throw (FileManagerServiceException) ouch.getCause() ;
				}
			}
		//
		// If we don't have the original Exception.
		else {
			log.debug("  Null cause") ;
			//
			// If the message starts with our class name.
			String message  = ouch.getMessage() ;
			String template = FileManagerServiceException.class.getName() + ": " ;
			log.debug("  Message  : '" + message  + "'") ;
			log.debug("  Template : '" + template + "'") ;
			if (null != message)
				{
				if (message.startsWith(template))
					{
					log.debug("  Matches template") ;
					throw new FileManagerServiceException(
						message.substring(
							template.length()
							)
						) ;
					}
				}
			}
		log.debug("  Not handled") ;
		}

	/**
	 * A converter utility to unpack a NodeNotFoundException from a RemoteException.
	 * @throws NodeNotFoundException If the RemoteException cause was a NodeNotFoundException.
	 *
	 */
	public void nodeNotFoundException(RemoteException ouch)
		throws NodeNotFoundException
		{
		log.debug("----") ;
		log.debug("FileManagerCoreDelegate.nodeNotFoundException") ;
		log.debug("  Exception : " + ouch) ;
		log.debug("  Type      : " + ouch.getClass()) ;
		log.debug("  Cause     : " + ouch.getCause()) ;
		//
		// If we have the original Exception.
		if (ouch.getCause() != null)
			{
			log.debug("  Got cause") ;
			if (ouch.getCause() instanceof NodeNotFoundException)
				{
				throw (NodeNotFoundException) ouch.getCause() ;
				}
			}
		//
		// If we don't have the original Exception.
		else {
			log.debug("  Null cause") ;
			//
			// If the message starts with our class name.
			String message  = ouch.getMessage() ;
			String template = NodeNotFoundException.class.getName() + ": " ;
			log.debug("  Message  : '" + message  + "'") ;
			log.debug("  Template : '" + template + "'") ;
			if (null != message)
				{
				if (message.startsWith(template))
					{
					log.debug("  Matches template") ;
					throw new NodeNotFoundException(
						message.substring(
							template.length()
							)
						) ;
					}
				}
			}
		log.debug("  Not handled") ;
		}

	/**
	 * A converter utility to unpack a NodeNotFoundException from a RemoteException.
	 * @throws NodeNotFoundException If the RemoteException cause was a NodeNotFoundException.
	 *
	 */
	public void duplicateNodeException(RemoteException ouch)
		throws DuplicateNodeException
		{
		log.debug("----") ;
		log.debug("FileManagerCoreDelegate.duplicateNodeException") ;
		log.debug("  Exception : " + ouch) ;
		log.debug("  Type      : " + ouch.getClass()) ;
		log.debug("  Cause     : " + ouch.getCause()) ;
		//
		// If we have the original Exception.
		if (ouch.getCause() != null)
			{
			log.debug("  Got cause") ;
			if (ouch.getCause() instanceof DuplicateNodeException)
				{
				throw (DuplicateNodeException) ouch.getCause() ;
				}
			}
		//
		// If we don't have the original Exception.
		else {
			log.debug("  Null cause") ;
			//
			// If the message starts with our class name.
			String message  = ouch.getMessage() ;
			String template = DuplicateNodeException.class.getName() + ": " ;
			log.debug("  Message  : '" + message  + "'") ;
			log.debug("  Template : '" + template + "'") ;
			if (null != message)
				{
				if (message.startsWith(template))
					{
					log.debug("  Matches template") ;
					throw new DuplicateNodeException(
						message.substring(
							template.length()
							)
						) ;
					}
				}
			}
		log.debug("  Not handled") ;
		}

	/**
	 * A converter utility to unpack a FileManagerIdentifierException from a RemoteException.
	 * @throws FileManagerIdentifierException If the RemoteException cause was a FileManagerIdentifierException.
	 *
	 */
	public void identifierException(RemoteException ouch)
		throws FileManagerIdentifierException
		{
		log.debug("----") ;
		log.debug("FileManagerCoreDelegate.identifierException") ;
		log.debug("  Exception : " + ouch) ;
		log.debug("  Type      : " + ouch.getClass()) ;
		log.debug("  Cause     : " + ouch.getCause()) ;
		//
		// If we have the original Exception.
		if (ouch.getCause() != null)
			{
			log.debug("  Got cause") ;
			if (ouch.getCause() instanceof FileManagerIdentifierException)
				{
				throw (FileManagerIdentifierException) ouch.getCause() ;
				}
			}
		//
		// If we don't have the original Exception.
		else {
			log.debug("  Null cause") ;
			//
			// If the message starts with our class name.
			String message  = ouch.getMessage() ;
			String template = FileManagerIdentifierException.class.getName() + ": " ;
			log.debug("  Message  : '" + message  + "'") ;
			log.debug("  Template : '" + template + "'") ;
			if (null != message)
				{
				if (message.startsWith(template))
					{
					log.debug("  Matches template") ;
					throw new FileManagerIdentifierException(
						message.substring(
							template.length()
							)
						) ;
					}
				}
			}
		log.debug("  Not handled") ;
		}

	/**
	 * A converter utility to unpack a FileManagerPropertiesException from a RemoteException.
	 * @throws FileManagerPropertiesException If the RemoteException cause was a FileManagerPropertiesException.
	 *
	 */
	public void propertiesException(RemoteException ouch)
		throws FileManagerPropertiesException
		{
		log.debug("----") ;
		log.debug("FileManagerCoreDelegate.propertiesException") ;
		log.debug("  Exception : " + ouch) ;
		log.debug("  Type      : " + ouch.getClass()) ;
		log.debug("  Cause     : " + ouch.getCause()) ;
		//
		// If we have the original Exception.
		if (ouch.getCause() != null)
			{
			log.debug("  Got cause") ;
			if (ouch.getCause() instanceof FileManagerPropertiesException)
				{
				throw (FileManagerPropertiesException) ouch.getCause() ;
				}
			}
		//
		// If we don't have the original Exception.
		else {
			log.debug("  Null cause") ;
			//
			// If the message starts with our class name.
			String message  = ouch.getMessage() ;
			String template = FileManagerPropertiesException.class.getName() + ": " ;
			log.debug("  Message  : '" + message  + "'") ;
			log.debug("  Template : '" + template + "'") ;
			if (null != message)
				{
				if (message.startsWith(template))
					{
					log.debug("  Matches template") ;
					throw new FileManagerPropertiesException(
						message.substring(
							template.length()
							)
						) ;
					}
				}
			}
		log.debug("  Not handled") ;
		}

	}

