/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/FileManagerMock.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:20:27 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerMock.java,v $
 *   Revision 1.2  2004/11/25 00:20:27  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.23  2004/11/18 14:39:32  dave
 *   Added SOAP delegate, RemoteException decoding and test case.
 *
 *   Revision 1.1.2.22  2004/11/16 03:25:37  dave
 *   Updated API to use full ivorn rather than ident ...
 *
 *   Revision 1.1.2.21  2004/11/13 01:39:03  dave
 *   Modifications to support the new client API ...
 *
 *   Revision 1.1.2.20  2004/11/11 17:53:10  dave
 *   Removed Node interface from the server side ....
 *
 *   Revision 1.1.2.19  2004/11/11 16:36:19  dave
 *   Changed getChildren to retunr array of names ...
 *
 *   Revision 1.1.2.18  2004/11/11 15:41:44  dave
 *   Renamed importInitEx and exportInitEx back to the original names ...
 *
 *   Revision 1.1.2.17  2004/11/11 15:30:37  dave
 *   Moving manager API to property[] rather than Node.
 *
 *   Revision 1.1.2.16  2004/11/10 18:32:57  dave
 *   Moved getAccount API to use properties ...
 *
 *   Revision 1.1.2.15  2004/11/10 17:00:11  dave
 *   Moving the manager API towards property based rather than node based ...
 *
 *   Revision 1.1.2.14  2004/11/06 20:03:17  dave
 *   Implemented ImportInit and ExportInit using properties
 *
 *   Revision 1.1.2.13  2004/11/05 05:58:31  dave
 *   Refactored the properties handling in importInitEx() ..
 *
 *   Revision 1.1.2.12  2004/11/05 02:23:45  dave
 *   Refactored identifiers are properties ...
 *
 *   Revision 1.1.2.11  2004/11/04 15:50:17  dave
 *   Added ivorn pareser and factory.
 *
 *   Revision 1.1.2.10  2004/11/04 04:15:34  dave
 *   Sketched out the new business logic for import init ....
 *
 *   Revision 1.1.2.9  2004/11/04 02:33:38  dave
 *   Refactored test to include multiple filestores ...
 *
 *   Revision 1.1.2.8  2004/11/02 15:05:13  dave
 *   Added old path tests back in ...
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
 *   Revision 1.1.2.4  2004/10/13 06:33:17  dave
 *   Refactored exceptions ...
 *   Refactored the container API
 *   Added placeholder file interface ...
 *
 *   Revision 1.1.2.3  2004/10/09 04:28:31  dave
 *   Added initial account and container methods ....
 *
 *   Revision 1.1.2.2  2004/10/07 16:04:38  dave
 *   Added exception to addAccount
 *
 *   Revision 1.1.2.1  2004/10/07 14:29:08  dave
 *   Added initial interface and implementations ....
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common ;

import java.util.Map ;
import java.util.HashMap ;
import java.util.Iterator ;
import java.util.Collection ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.file.FileProperties ;

import org.astrogrid.filestore.common.transfer.UrlGetRequest ;
import org.astrogrid.filestore.common.transfer.UrlPutTransfer ;
import org.astrogrid.filestore.common.transfer.TransferProperties ;

import org.astrogrid.filestore.common.exception.FileStoreServiceException ;
import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;

import org.astrogrid.filestore.client.FileStoreDelegate ;
import org.astrogrid.filestore.resolver.FileStoreDelegateResolver ;

import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory ;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornParser ;

import org.astrogrid.filemanager.common.exception.NodeNotFoundException ;
import org.astrogrid.filemanager.common.exception.DuplicateNodeException ;
import org.astrogrid.filemanager.common.exception.FileManagerServiceException ;
import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException ;
import org.astrogrid.filemanager.common.exception.FileManagerPropertiesException ;

/**
 * A mock implementation of the file manager interface.
 *
 */
public class FileManagerMock
	implements FileManager
	{
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileManagerMock.class);

	/**
	 * Our FileManager configuration.
	 *
	 */
	private FileManagerConfig config ;

	/**
	 * The default test config.
	 * This is used by the no-param FileManagerMock constructor.
	 *
	 */
	public static FileManagerConfig defaultConfig ;

	/**
	 * Our FileStore resolver.
	 *
	 */
	private FileStoreDelegateResolver resolver ;

	/**
	 * The default test resolver.
	 * This is used by the no-param FileManagerMock constructor.
	 *
	 */
	public static FileStoreDelegateResolver defaultResolver ;

	/**
	 * Our identifier factory.
	 *
	 */
	private FileManagerIvornFactory factory ;

	/**
	 * The default test factory.
	 * This is used by the no-param FileManagerMock constructor.
	 *
	 */
	public static FileManagerIvornFactory defaultFactory ;

	/**
	 * Public constructor, the default configuration, identifier factory and resolver.
	 * @param config The local file manager configuration.
	 * @param factory A factory for creating resource identifiers.
	 * @param resolver A resolver to locate filestores.
	 *
	 */
	public FileManagerMock()
		{
		this(
			defaultConfig,
			defaultFactory,
			defaultResolver
			);
		}

	/**
	 * Public constructor, using a custom configuration, identifier factory and resolver.
	 * @param config The local file manager configuration.
	 * @param factory A factory for creating resource identifiers.
	 * @param resolver A resolver to locate filestores.
	 *
	 */
	public FileManagerMock(
		FileManagerConfig config,
		FileManagerIvornFactory factory,
		FileStoreDelegateResolver resolver
		)
		{
		if (null == config)
			{
			throw new IllegalArgumentException(
				"Null manager configuration"
				);
			}
		if (null == factory)
			{
			throw new IllegalArgumentException(
				"Null identifier factory"
				);
			}
		if (null == resolver)
			{
			throw new IllegalArgumentException(
				"Null FileStore resolver"
				);
			}
		this.config   = config ;
		this.factory  = factory ;
		this.resolver = resolver ;
		}

	/**
	 * Get the manager identifier.
	 * @return The manager identifier (ivorn).
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
	 *
	 */
	public String getIdentifier()
		throws FileManagerServiceException
		{
		return config.getFileManagerIvorn().toString() ;
		}

	/**
	 * Our internal map of accounts.
	 *
	 */
	private Map accounts = new HashMap() ;

	/**
	 * Our internal map of nodes.
	 *
	 */
	private Map nodes = new HashMap() ;

	/**
	 * Create a node for a new account.
	 * @param account The identifier for the account.
	 * @return An array of properties for the new account node.
	 * @throws DuplicateNodeException If the the account already exists.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
	 *
	 */
	public FileProperty[] addAccount(String account)
		throws FileManagerServiceException, DuplicateNodeException
		{
		if (null == account)
			{
			throw new IllegalArgumentException(
				"Null account identifier"
				);
			}
		//
		// Check if the account already exists.
		if (accounts.containsKey(account))
			{
			throw new DuplicateNodeException() ;
			}
		//
		// Create the new node.
		NodeMock node = this.addNode(
			(NodeMock) null,
			"home",
			FileManagerProperties.CONTAINER_NODE_TYPE
			) ;
		//
		// Add the node to our map of accounts.
		accounts.put(
			account,
			node
			) ;
		//
		// Return the new node.
		return node.getProperties() ;
		}

	/**
	 * Get the root node for an account
	 * @param account The identifier of the account.
	 * @return An array of properties for the account node.
	 * @throws NodeNotFoundException If the node does not exist.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
	 *
	 */
	public FileProperty[] getAccount(String account)
		throws FileManagerServiceException, NodeNotFoundException
		{
		if (null == account)
			{
			throw new IllegalArgumentException(
				"Null identifier"
				) ;
			}
		//
		// Check if the node exists.
		if (accounts.containsKey(account))
			{
			return ((NodeMock) accounts.get(account)).getProperties() ;
			}
		//
		// If the node does not exist.
		else {
			throw new NodeNotFoundException() ;
			}
		}

	/**
	 * Get a specific node, indexed by ivorn.
	 * @param ivorn The node ivorn (as a string).
	 * @return The node specified by the identifier.
	 * @throws FileManagerIdentifierException If the node identifier is invalid.
	 * @throws NodeNotFoundException If the node does not exist.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
	 *
	 */
	public FileProperty[] getNode(String ivorn)
		throws FileManagerServiceException, FileManagerIdentifierException, NodeNotFoundException
		{
		if (null == ivorn)
			{
			throw new IllegalArgumentException(
				"Null node identifier"
				) ;
			}
		//
		// Parse the node ivorn.
		String ident = new FileManagerIvornParser(
			ivorn
			).getResourceIdent();
		//
		// Check if the node exists.
		if (nodes.containsKey(ident))
			{
			return ((NodeMock) nodes.get(ident)).getProperties() ;
			}
		//
		// If the node does not exist.
		else {
			throw new NodeNotFoundException() ;
			}
		}

	/**
	 * Get a specific child node, indexed by path.
	 * @param root The identifier of the node to start from.
	 * @param path The target node path.
	 * @return The node specified by the path.
	 * @throws FileManagerIdentifierException If the node identifier is invalid.
	 * @throws NodeNotFoundException If the node does not exist.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
	 *
	 */
	public FileProperty[] getChild(String ivorn, String path)
		throws FileManagerServiceException, FileManagerIdentifierException, NodeNotFoundException
		{
		log.debug("");
		log.debug("FileManagerMock.getNodeByPath()");
		log.debug("  Ivorn : " + ivorn);
		log.debug("  Path  : " + path);
		if (null == ivorn)
			{
			throw new IllegalArgumentException(
				"Null node identifier"
				) ;
			}
		if (null == path)
			{
			throw new IllegalArgumentException(
				"Null path"
				) ;
			}
		//
		// Parse the parent ivorn.
		String ident = new FileManagerIvornParser(
			ivorn
			).getResourceIdent();
		//
		// Check if the root node exists.
		if (nodes.containsKey(ident))
			{
			NodeMock node = (NodeMock) nodes.get(ident) ;
			log.debug("");
			log.debug("  Node : " + node.getName());
			log.debug("  Node : " + node.getIdent());
			//
			// Get the child node.
			return node.getChild(path).getProperties() ;
			}
		//
		// If the root node does not exist.
		else {
			throw new NodeNotFoundException() ;
			}
		}

	/**
	 * Add a new node.
	 * @param ivorn The identifier of the parent node.
	 * @param name  The new node name.
	 * @param type  The new node type.
	 * @return The new node.
	 * @throws DuplicateNodeException If a node with the same name already exists.
	 * @throws NodeNotFoundException If the parent node does not exist.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
	 * @todo NodeTypeNotSupportedException If the specified type is not supported. ***
	 *
	 */
	public FileProperty[] addNode(String ivorn, String name, String type)
		throws FileManagerServiceException, FileManagerIdentifierException, DuplicateNodeException, NodeNotFoundException
		{
		log.debug("");
		log.debug("FileManagerMock.addNode(String, String, String)");
		log.debug("  Parent : " + ivorn);
		log.debug("  Name   : " + name);
		log.debug("  Type   : " + type);
		if (null == ivorn)
			{
			throw new IllegalArgumentException(
				"Null parent identifier"
				) ;
			}
		//
		// Parse the parent ivorn.
		String ident = new FileManagerIvornParser(
			ivorn
			).getResourceIdent();
		//
		// Get the parent node.
		NodeMock node = (NodeMock) nodes.get(ident) ;
		//
		// If we found the parent node.
		if (null != node)
			{
			//
			// If the node is a container 
			if (FileManagerProperties.CONTAINER_NODE_TYPE.equals(node.getType()))
				{
				NodeMock child = this.addNode(
					node,
					name,
					type
					) ;
				return child.getProperties() ;
				}
			//
			// If the node isn't a container.
			else {
				throw new UnsupportedOperationException(
					"Parent must be a container"
					);
				}
			}
		//
		// If we didn't find the parent node.
		else {
			throw new NodeNotFoundException() ;
			}
		}

	/**
	 * Add a new node.
	 * @param parent The parent node.
	 * @param name   The new node name.
	 * @param type   The new node type.
	 * @return The new node.
	 * @throws DuplicateNodeException If a node with the same name already exists.
	 * @todo NodeTypeNotSupportedException If the specified type is not supported. ***
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
	 *
	 */
	protected NodeMock addNode(NodeMock parent, String name, String type)
		throws FileManagerServiceException, DuplicateNodeException
		{
		log.debug("");
		log.debug("FileManagerMock.addNode(Node, Node)");
		if (null == parent)
			{
			log.debug("  Parent : -");
			}
		else {
			log.debug("  Parent : " + parent.getIdent());
			log.debug("  Parent : " + parent.getName());
			}
		log.debug("  Name   : " + name);
		log.debug("  Type   : " + type);
		if (null == name)
			{
			throw new IllegalArgumentException(
				"Null name"
				) ;
			}
		if (null == type)
			{
			throw new IllegalArgumentException(
				"Null type"
				) ;
			}
		try {
			//
			// Create the new node.
			NodeMock node = new NodeMock(
				parent,
				name,
				type
				);
			//
			// Set the node ivorn.
			node.setIvorn(
				factory.ivorn(
					config.getFileManagerIvorn()
					)
				);
			//
			// Add the node to our map.
			nodes.put(
				node.getIdent(),
				node
				) ;
			//
			// Add the child to the parent.
			if (null != parent)
				{
				parent.addNode(
					node
					) ;
				}
			//
			// Return the new node.
			return node ;
			}
		catch (FileManagerIdentifierException ouch)
			{
			log.debug("Unable to create node identifier");
			log.debug("Exception : " + ouch);
			throw new FileManagerServiceException(
				"Unable to create node identifier"
				);
			}
		}

	/**
	 * Get the children of a specific node.
	 * @param ivorn The identifier of the parent node.
	 * @return An array of the child node names.
	 * @throws FileManagerIdentifierException If the node identifier is invalid.
	 * @throws NodeNotFoundException If the parent node does not exist.
	 */
	public String[] getChildren(String ivorn)
		throws FileManagerIdentifierException, NodeNotFoundException
		{
		log.debug("");
		log.debug("FileManagerMock.getChildren(String)");
		log.debug("  Ivorn : " + ivorn);
		if (null == ivorn)
			{
			throw new IllegalArgumentException(
				"Null parent identifier"
				) ;
			}
		//
		// Parse the parent ivorn.
		String ident = new FileManagerIvornParser(
			ivorn
			).getResourceIdent();
		//
		// Check if the node exists.
		if (nodes.containsKey(ident))
			{
			//
			// Get the parent node.
			NodeMock node = (NodeMock) nodes.get(ident) ;
			log.debug("");
			log.debug("  Node : " + node.getName());
			log.debug("  Node : " + node.getIdent());
			//
			// Create an array of the child nodes.
			Collection nodes = node.getChildren() ;
			String[] array = new String[nodes.size()] ;
			Iterator iter = nodes.iterator() ;
			for(int i = 0 ; iter.hasNext() ; i++)
				{
				array[i] = ((NodeMock) iter.next()).getName() ;
				}
			return array ;
			}
		//
		// If the node does not exist.
		else {
			throw new NodeNotFoundException() ;
			}
		}

	/**
	 * Resolve a filestore.
	 * @param ivorn The file store ivorn.
	 * @return A delegate interface for the filestore.
	 * @throws FileManagerServiceException If unbale to resolve the filestore.
	 * @todo Ping the filestore to check it is available ?
	 *
	 */
	protected FileStoreDelegate resolve(Ivorn ivorn)
		throws FileManagerServiceException
		{
		try {
			return resolver.resolve(
				ivorn
				) ;
			}
		catch (Exception ouch)
			{
			log.debug("Exception thrown by FileStoreResolver.resolve()");
			log.debug("  Exception : " + ouch);
			throw new FileManagerServiceException(
				"Unable to locate FileStore service"
				);
			}
		}

	/**
	 * Initialise a data transfer into a FileStore.
	 * The request properties need to specify the identifier of an existing node, or the identifier of a parent node and the new node name.
	 * @param properties The request properties.
	 * @throws NodeNotFoundException If the target node does not exist.
	 * @throws FileManagerServiceException If the service is unable to handle the request.
	 * @throws FileManagerPropertiesException If the required properties are incomplete.
	 *
	 */
	public TransferProperties importInit(FileProperty[] properties)
		throws FileManagerServiceException, FileManagerPropertiesException, NodeNotFoundException
		{
		log.debug("");
		log.debug("FileManagerMock.importInit(Properties)");
		if (null == properties)
			{
			throw new FileManagerPropertiesException(
				"Null request properties"
				) ;
			}
		//
		// Wrap the request properties.
		FileManagerProperties request = new FileManagerProperties(
			properties
			);
		//
		// Parse the node identifier.
		String ident = null ;
		try {
			ident = request.getManagerResourceIdent();
			}
		catch (FileManagerIdentifierException ouch)
			{
			throw new NodeNotFoundException(
				"Unable to parse node identifier"
				);
			}
		//
		// If we don't have a node identifier.
		if (null == ident)
			{
			throw new NodeNotFoundException(
				"Null node identifier"
				);
			//
			// If the properties contain a parent and name.
				//
				// Create a new node.

				//
				// If we couldn't create the node.
					//
					// Throw an Exception.
			//
			// If we don't have enough to create the node.
				//
				// Throw an Exception.
			}
		//
		// If we have a node identifier.
		else {
			//
			// Check if the node exists.
			if (nodes.containsKey(ident))
				{
				NodeMock node = (NodeMock) nodes.get(ident) ;
				log.debug("");
				log.debug("  Node : " + node.getName());
				log.debug("  Node : " + node.getIdent());
				//
				// Initiate the transfer.
				return this.importInit(
					node,
					request
					) ;
				}
			//
			// If the node does not exist.
			else {
				throw new NodeNotFoundException();
				}
			}
		}

	/**
	 * Initialise a data transfer into a FileStore.
	 * @param node    The target node.
	 * @param request The request properties.
	 * @throws NodeNotFoundException If the target node does not exist.
	 * @throws FileManagerServiceException If the service is unable to handle the request.
	 * @throws FileManagerPropertiesException If the required properties are incomplete.
	 *
	 */
	protected TransferProperties importInit(NodeMock node, FileManagerProperties request)
		throws FileManagerServiceException, FileManagerPropertiesException, NodeNotFoundException
		{
		log.debug("");
		log.debug("FileManagerMock.importInit(Node, Properties)");
		log.debug("  Node : " + node.getName());
		log.debug("  Node : " + node.getIdent());
		//
		// Get the current node properties.
		FileManagerProperties current = new FileManagerProperties(
			node.getProperties()
			) ;
		//
		// Check for an impled move
		// request.filemanager.name   != current.filemanager.name
		// request.filemanager.name   != current.filemanager.type
		// request.filemanager.parent != current.filemanager.parent

		//
		// Get the current location.
		Ivorn target = null ;
		try {
			target = current.getStoreResourceIvorn() ;
			}
		catch (FileStoreIdentifierException ouch)
			{
			throw new FileManagerServiceException(
				"Unable to parse current filestore ivorn"
				);
			}
		//
		// If the node has already been stored.
		if (null != target)
			{
			//
			// Override the request location.
//
// Check for an implied move ?
			request.setStoreResourceIvorn(
				target
				);
			}
		//
		// If the node has not been stored yet.
		else {
			//
			// Use the request location.
			try {
				target = request.getStoreResourceIvorn() ;
				}
			catch (FileStoreIdentifierException ouch)
				{
				throw new FileManagerServiceException(
					"Unable to parse resource identifier"
					);
				}
			//
			// If the request location is still null.
			if (null == target)
				{
				//
				// Use the default filestore.
				target = config.getFileStoreIvorn() ;
				}
			}
		//
		// Update the request properties.
		try {
			request.setManagerResourceIvorn(
				current.getManagerResourceIvorn()
				);
			}
		catch (FileManagerIdentifierException ouch)
			{
			throw new FileManagerServiceException(
				"Unable to parse resource ivorn"
				);
			}
		try {
			request.setManagerParentIvorn(
				current.getManagerParentIvorn()
				);
			}
		catch (FileManagerIdentifierException ouch)
			{
			throw new FileManagerServiceException(
				"Unable to parse parent ivorn"
				);
			}
		request.setManagerResourceName(
			current.getManagerResourceName()
			);
		request.setManagerResourceType(
			current.getManagerResourceType()
			);
		//
		// Resolve the target filestore.
		FileStoreDelegate filestore = resolve(target) ;
		//
		// Call the filestore to initiate the transfer.
		TransferProperties transfer = null ;
		try {
			transfer = 
				filestore.importInit(
					new UrlPutTransfer(
						request
						)
					) ;
			}
		catch (Exception ouch)
			{
			log.debug("Exception thrown by FileStore.importInit()");
			log.debug("  Exception : " + ouch);
			throw new FileManagerServiceException(
				"Error occurred when calling FileStore service"
				);
			}
		//
		// Get the response properties.
		FileManagerProperties response = new FileManagerProperties(
			transfer.getFileProperties()
			) ;
		//
		// Update the response properties.
		// This repairs any changes to the manager properties made by the filestore.
		try {
			response.setManagerResourceIvorn(
				current.getManagerResourceIvorn()
				);
			}
		catch (FileManagerIdentifierException ouch)
			{
			throw new FileManagerServiceException(
				"Unable to parse resource ivorn"
				);
			}
		try {
			response.setManagerParentIvorn(
				current.getManagerParentIvorn()
				);
			}
		catch (FileManagerIdentifierException ouch)
			{
			throw new FileManagerServiceException(
				"Unable to parse parent ivorn"
				);
			}
		response.setManagerResourceName(
			current.getManagerResourceName()
			);
		response.setManagerResourceType(
			current.getManagerResourceType()
			);
		//
		// Save these as the new properties.
		node.setProperties(
			response
			) ;
		//
		// Update the transfer properties.
		transfer.setFileProperties(
			response
			);
		return transfer ;
		}

	/**
	 * Initialise a data transfer from a FileStore.
	 * This calls the FileStore to get the HTTP (GET) URL to read the data from the FileStore.
	 * @param properties The request properties.
	 * @throws NodeNotFoundException If the target node does not exist.
	 * @throws FileManagerServiceException If the service is unable to handle the request.
	 * @throws FileManagerPropertiesException If the required properties are incomplete.
	 *
	 */
	public TransferProperties exportInit(FileProperty[] properties)
		throws FileManagerServiceException, FileManagerPropertiesException, NodeNotFoundException
		{
		log.debug("");
		log.debug("FileManagerMock.exportInit(Properties)");
		if (null == properties)
			{
			throw new FileManagerPropertiesException(
				"Null request properties"
				) ;
			}
		//
		// Wrap the request properties.
		FileManagerProperties request = new FileManagerProperties(
			properties
			);
		//
		// Parse the node identifier.
		String ident = null ;
		try {
			ident = request.getManagerResourceIdent() ;
			}
		catch (FileManagerIdentifierException ouch)
			{
			throw new NodeNotFoundException(
				"Unable to parse resource ivorn"
				);
			}
		//
		// If we don't have a node identifier.
		if (null == ident)
			{
			throw new NodeNotFoundException(
				"Null node identifier"
				);
			}
		//
		// If we have a node identifier.
		else {
			//
			// Check if the node exists.
			if (nodes.containsKey(ident))
				{
				NodeMock node = (NodeMock) nodes.get(ident) ;
				log.debug("");
				log.debug("  Node : " + node.getName());
				log.debug("  Node : " + node.getIdent());
				//
				// Initiate the transfer.
				return this.exportInit(
					node,
					request
					) ;
				}
			//
			// If the node does not exist.
			else {
				throw new NodeNotFoundException();
				}
			}
		}

	/**
	 * Initialise a data transfer from a FileStore.
	 * @param node    The target node.
	 * @param request The request properties.
	 * @throws NodeNotFoundException If the target node does not exist.
	 * @throws FileManagerServiceException If the service is unable to handle the request.
	 * @throws FileManagerPropertiesException If the required properties are incomplete.
	 *
	 */
	protected TransferProperties exportInit(NodeMock node, FileManagerProperties request)
		throws FileManagerServiceException, FileManagerPropertiesException, NodeNotFoundException
		{
		log.debug("");
		log.debug("FileManagerMock.exportInit(Node, Properties)");
		log.debug("  Node : " + node.getName());
		log.debug("  Node : " + node.getIdent());
		//
		// Get the current node properties.
		FileManagerProperties current = new FileManagerProperties(
			node.getProperties()
			) ;
		//
		// Check for an impled move
		// request.filemanager.name   != current.filemanager.name
		// request.filemanager.name   != current.filemanager.type
		// request.filemanager.parent != current.filemanager.parent
		//
		// Get the current location.
		Ivorn target = null ;
		try {
			target = current.getStoreResourceIvorn() ;
			}
		catch (FileStoreIdentifierException ouch)
			{
			throw new FileManagerServiceException(
				"Unable to parse current filestore ivorn"
				);
			}
		//
		// If the node has already been stored.
		if (null != target)
			{
			//
			// Override the request location.
			request.setStoreResourceIvorn(
				target
				);
			}
		//
		// If the node has not been stored yet.
		else {
			throw new NodeNotFoundException(
				"Node does not have any data"
				);
			}
		//
		// Update the request properties.
		try {
			request.setManagerResourceIvorn(
				current.getManagerResourceIvorn()
				);
			}
		catch (FileManagerIdentifierException ouch)
			{
			throw new FileManagerServiceException(
				"Unable to parse resource ivorn"
				);
			}
		try {
			request.setManagerParentIvorn(
				current.getManagerParentIvorn()
				);
			}
		catch (FileManagerIdentifierException ouch)
			{
			throw new FileManagerServiceException(
				"Unable to parse parent ivorn"
				);
			}
		request.setManagerResourceName(
			current.getManagerResourceName()
			);
		request.setManagerResourceType(
			current.getManagerResourceType()
			);
		//
		// Resolve the target filestore.
		FileStoreDelegate filestore = resolve(target) ;
		//
		// Call the filestore to initiate the transfer.
		TransferProperties transfer = null ;
		try {
			transfer = 
				filestore.exportInit(
					new UrlPutTransfer(
						request
						)
					) ;
			}
		catch (Exception ouch)
			{
			log.debug("Exception thrown by FileStore.importInit()");
			log.debug("  Exception : " + ouch);
			throw new FileManagerServiceException(
				"Error occurred when calling FileStore service"
				);
			}
		//
		// Get the response properties.
		FileManagerProperties response = new FileManagerProperties(
			transfer.getFileProperties()
			) ;
		//
		// Update the response properties.
		// This repairs any changes to the manager properties made by the filestore.
		try {
			response.setManagerResourceIvorn(
				current.getManagerResourceIvorn()
				);
			}
		catch (FileManagerIdentifierException ouch)
			{
			throw new FileManagerServiceException(
				"Unable to parse resource ivorn"
				);
			}
		try {
			response.setManagerParentIvorn(
				current.getManagerParentIvorn()
				);
			}
		catch (FileManagerIdentifierException ouch)
			{
			throw new FileManagerServiceException(
				"Unable to parse parent ivorn"
				);
			}
		response.setManagerResourceName(
			current.getManagerResourceName()
			);
		response.setManagerResourceType(
			current.getManagerResourceType()
			);
		//
		// Save these as the new properties.
		node.setProperties(
			response
			) ;
		//
		// Update the transfer properties.
		transfer.setFileProperties(
			response
			);
		return transfer ;
		}

	}
