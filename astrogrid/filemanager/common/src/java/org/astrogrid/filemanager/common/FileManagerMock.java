/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/FileManagerMock.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/12/16 17:25:49 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerMock.java,v $
 *   Revision 1.3  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.40  2004/12/14 17:17:01  dave
 *   Added delete from filemanager ....
 *
 *   Revision 1.1.2.39  2004/12/14 16:51:27  dave
 *   Added delete from filemanager ....
 *
 *   Revision 1.1.2.38  2004/12/14 16:02:09  dave
 *   Added delete from filemanager ....
 *
 *   Revision 1.1.2.37  2004/12/14 15:48:54  dave
 *   Added delete from filemanager ....
 *
 *   Revision 1.1.2.36  2004/12/14 15:44:12  dave
 *   Added delete from filemanager ....
 *
 *   Revision 1.1.2.35  2004/12/14 14:56:25  dave
 *   Added location change for copy empty ...
 *
 *   Revision 1.1.2.34  2004/12/14 14:44:46  dave
 *   Added test for copy empty location ...
 *
 *   Revision 1.1.2.33  2004/12/14 14:11:57  dave
 *   Added delete to the server API ....
 *
 *   Revision 1.1.2.32  2004/12/11 21:19:53  dave
 *   Added copy accross remote filestore(s) ...
 *
 *   Revision 1.1.2.31  2004/12/11 05:59:17  dave
 *   Added internal copy for nodes ...
 *   Added local copy for data ...
 *
 *   Revision 1.1.2.30  2004/12/10 05:21:25  dave
 *   Added node and iterator to client API ...
 *
 *   Revision 1.1.2.29  2004/12/08 17:54:55  dave
 *   Added update to FileManager client and server side ...
 *
 *   Revision 1.1.2.28  2004/12/08 01:56:04  dave
 *   Added filestore location to move ...
 *
 *   Revision 1.1.2.27  2004/12/06 13:29:02  dave
 *   Added initial code for move location ....
 *
 *   Revision 1.1.2.26  2004/12/04 05:22:21  dave
 *   Fixed null parent mistake ...
 *
 *   Revision 1.1.2.25  2004/12/02 19:11:54  dave
 *   Added move name and parent to manager ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common ;

import java.util.Map ;
import java.util.Vector ;
import java.util.HashMap ;
import java.util.Iterator ;
import java.util.Collection ;
import java.util.StringTokenizer ;

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
	 * Enables JUnit tests to setup the service environment for Axis.
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
		log.debug("");
		log.debug("FileManagerMock.addAccount()");
		log.debug("  Account : " + account);
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
		return node.getProperties().toArray() ;
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
		log.debug("");
		log.debug("FileManagerMock.getAccount()");
		log.debug("  Account : " + account);
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
			return ((NodeMock) accounts.get(account)).getProperties().toArray() ;
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
		log.debug("");
		log.debug("FileManagerMock.getNode()");
		log.debug("  Ivorn : " + ivorn);
		if (null == ivorn)
			{
			throw new IllegalArgumentException(
				"Null node identifier"
				) ;
			}
		//
		// Parse the node ivorn.
		String path = new FileManagerIvornParser(
			ivorn
			).getResourceIdent();
		//
		// Split the ident on '/'
		StringTokenizer tokens = new StringTokenizer(path, "/") ;
		//
		// If we have an initial token.
		if (tokens.hasMoreTokens())
			{
			String ident = tokens.nextToken();
			//
			// Check if the node exists.
			if (nodes.containsKey(ident))
				{
				NodeMock node = (NodeMock) nodes.get(ident) ;
				//
				// If we have more tokens.
				if (tokens.hasMoreTokens())
					{
					node = node.getChild(tokens) ;
					}
				return node.getProperties().toArray() ;
				}
			//
			// If the node does not exist.
			else {
				throw new NodeNotFoundException() ;
				}
			}
		//
		// If the path is empty
		else {
			throw new NodeNotFoundException() ;
			}
		}

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
		throws FileManagerServiceException, FileManagerIdentifierException, NodeNotFoundException
		{
		log.debug("");
		log.debug("FileManagerMock.update()");
		log.debug("  Ivorn : " + ivorn);
		if (null == ivorn)
			{
			throw new IllegalArgumentException(
				"Null node identifier"
				) ;
			}
		//
		// Parse the node ivorn.
		String path = new FileManagerIvornParser(
			ivorn
			).getResourceIdent();
		//
		// Split the ident on '/'
		StringTokenizer tokens = new StringTokenizer(path, "/") ;
		//
		// If we have an initial token.
		if (tokens.hasMoreTokens())
			{
			String ident = tokens.nextToken();
			//
			// Check if the node exists.
			if (nodes.containsKey(ident))
				{
				NodeMock node = (NodeMock) nodes.get(ident) ;
				//
				// If we have more tokens.
				if (tokens.hasMoreTokens())
					{
					node = node.getChild(tokens) ;
					}
				log.debug("Found node ...");
				log.debug("  Node : " + node.getIvorn());
				//
				// Get the current node properties.
				FileManagerProperties current = node.getProperties();
				//
				// If this is a file node.
				if (node.isDataNode())
					{
					log.debug("Found data node ...");
					//
					// Get the current store location.
					Ivorn  dataIvorn = null ;
					String dataIdent = null ;
					try {
						dataIvorn = current.getStoreResourceIvorn() ;
						dataIdent = current.getStoreResourceIdent() ;
						}
					catch (FileStoreIdentifierException ouch)
						{
						log.warn("");
						log.warn("Unable to parse current store resource ivorn");
						log.warn(ouch);
						throw new FileManagerServiceException(
							"Unable to parse current store resource ivorn"
							);
						}
					//
					// If the node has stored data.
					if (null != dataIvorn)
						{
						log.debug("Found store location ...");
						log.debug("  Ivorn : " + dataIvorn.toString());
						//
						// Resolve the source filestore
						FileStoreDelegate filestore = resolve(
							dataIvorn
							) ;
						log.debug("  PASS  : Got data filestore");
						//
						// Update the filestore properties.
						try {
							//
							// Get the filestore properties.
							FileManagerProperties updated = new FileManagerProperties(
								filestore.properties(
									dataIdent
									)
								);
							log.debug("  PASS  : Got updated properties");
							log.debug("  Size  : " + String.valueOf(updated.getContentSize()));
							//
							// Update the local properties.
							current.merge(
								updated,
								new FileManagerPropertyFilter()
								);
							log.debug("  PASS  : Merged updated properties");
							log.debug("  Size  : " + String.valueOf(current.getContentSize()));
							}
//
// Should we treat this differently ?
// FileStoreNotFoundException
						catch(Exception ouch)
							{
							log.warn("Exception thrown by FileStore.properties()");
							log.warn("  Exception : " + ouch);
//
// Set a property to indicate the data can't be accessed ?
//
							}
						}
					}
				//
				// Return the node properties.
				return current.toArray() ;
				}
			//
			// If the node does not exist.
			else {
				throw new NodeNotFoundException() ;
				}
			}
		//
		// If the path is empty
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
		log.debug("FileManagerMock.getChild()");
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
			return node.getChild(path).getProperties().toArray() ;
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
			if (node.isContainer())
				{
				NodeMock child = this.addNode(
					node,
					name,
					type
					) ;
				return child.getProperties().toArray() ;
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
		log.debug("FileManagerMock.addNode(Node, String, String)");
		log.debug("  Name   : " + name);
		log.debug("  Type   : " + type);
		if (null == parent)
			{
			log.debug("  Parent : -");
			}
		else {
			log.debug("  Parent : " + parent.getIdent());
			log.debug("  Parent : " + parent.getName());
			}
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
			// If it is a data node.
			if (node.isDataNode())
				{
				//
				// Set the default location.
				node.getProperties().setManagerLocationIvorn(
					config.getFileStoreIvorn()
					);
				}
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
	 * Template for generating an array of strings.
	 *
	 */
	private static final String[] template = new String[0] ;

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
			Iterator   iter  = nodes.iterator() ;
			Vector    vector = new Vector();
			while(iter.hasNext())
				{
				Ivorn child = ((NodeMock) iter.next()).getIvorn();
				if (null != child)
					{
					vector.add(
						child.toString()
						);
					}
				}
			String[] array = (String[]) vector.toArray(
				template
				);
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
	 * @param request The request properties.
	 * @throws NodeNotFoundException If the target node does not exist.
	 * @throws FileManagerServiceException If the service is unable to handle the request.
	 * @throws FileManagerPropertiesException If the required properties are incomplete.
	 *
	 */
	public TransferProperties importInit(FileProperty[] request)
		throws FileManagerServiceException, FileManagerPropertiesException, NodeNotFoundException
		{
		log.debug("");
		log.debug("FileManagerMock.importInit(Properties)");
		if (null == request)
			{
			throw new FileManagerPropertiesException(
				"Null request properties"
				) ;
			}
		//
		// Wrap the request properties.
		FileManagerProperties properties = new FileManagerProperties(
			request
			);
		//
		// Parse the node identifier.
		String ident = null ;
		try {
			ident = properties.getManagerResourceIdent();
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
					properties
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
		FileManagerProperties current = node.getProperties();
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
			log.warn("");
			log.warn("Unable to parse store location");
			log.warn(ouch);
			throw new FileManagerServiceException(
				"Unable to parse store location"
				);
			}
		//
		// If the node has already been stored.
		if (null != target)
			{
			//
			// Use the current store location.
			request.setStoreResourceIvorn(
				target
				);
			}
		//
		// If the node has not been stored yet.
		else {
			//
			// Use the node location.
			try {
				target = current.getManagerLocationIvorn();
				}
			catch (FileManagerIdentifierException ouch)
				{
				log.warn("");
				log.warn("Unable to parse store location");
				log.warn(ouch);
				throw new FileManagerServiceException(
					"Unable to parse store location"
					);
				}
/*
 *
			//
			// Use the request location.
//TODO
// Check for an implied move ?
//
			try {
				target = request.getStoreResourceIvorn() ;
				}
			catch (FileStoreIdentifierException ouch)
				{
				throw new FileManagerServiceException(
					"Unable to parse resource identifier"
					);
				}
 *
 */
			//
			// If the target location is still null.
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
			log.warn("");
			log.warn("Unable to set resource ivorn");
			log.warn(ouch);
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
			log.warn("");
			log.warn("Unable to set parent ivorn");
			log.warn(ouch);
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
		FileStoreDelegate filestore = resolve(
			target
			) ;
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
			log.warn("");
			log.warn("Unable to set resource ivorn");
			log.warn(ouch);
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
			log.warn("");
			log.warn("Unable to set parent ivorn");
			log.warn(ouch);
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
		// Update the store location.
		try {
			response.setManagerLocationIvorn(
				response.getStoreServiceIvorn()
				);
			}
		catch (FileStoreIdentifierException ouch)
			{
			log.warn("");
			log.warn("Unable to set location ivorn");
			log.warn(ouch);
			response.setManagerLocationIvorn(
				target
				);
			}
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
	 * @param request The request properties.
	 * @throws NodeNotFoundException If the target node does not exist.
	 * @throws FileManagerServiceException If the service is unable to handle the request.
	 * @throws FileManagerPropertiesException If the required properties are incomplete.
	 *
	 */
	public TransferProperties exportInit(FileProperty[] request)
		throws FileManagerServiceException, FileManagerPropertiesException, NodeNotFoundException
		{
		log.debug("");
		log.debug("FileManagerMock.exportInit(Properties)");
		if (null == request)
			{
			throw new FileManagerPropertiesException(
				"Null request properties"
				) ;
			}
		//
		// Wrap the request properties.
		FileManagerProperties properties = new FileManagerProperties(
			request
			);
		//
		// Parse the node identifier.
		String ident = null ;
		try {
			ident = properties.getManagerResourceIdent() ;
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
					properties
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
	 * @throws FileManagerPropertiesException If the transfer properties are incomplete.
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
		FileManagerProperties current = node.getProperties();
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
					new UrlGetRequest(
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
	 * Move a node to a new location.
	 * If the node already has stored data, then this may involve transfering the data to a new location.
	 * @param  properties The request properties.
	 * @return A new set of properties for the node.
	 * @throws DuplicateNodeException If a node with the same name already exists in the metadata tree.
	 * @throws NodeNotFoundException If the current node is no longer in the database.
	 * @throws NodeNotFoundException If the new parent node is no longer in the database.
	 * @throws FileManagerPropertiesException If the transfer properties are incomplete.
	 * @throws FileManagerServiceException If a problem occurs when handling the request.
	 *
	 */
	public FileProperty[] move(FileProperty[] request)
		throws FileManagerPropertiesException, DuplicateNodeException, NodeNotFoundException, FileManagerServiceException
		{
		log.debug("");
		log.debug("FileManagerMock.move(FileProperty[])");
		if (null == request)
			{
			throw new FileManagerPropertiesException(
				"Null request properties"
				) ;
			}
		//
		// Wrap the request properties.
		FileManagerProperties properties = new FileManagerProperties(
			request
			);
		//
		// Parse the node ivorn.
		Ivorn ivorn = null ;
		try {
			ivorn = properties.getManagerResourceIvorn();
			}
		catch (FileManagerIdentifierException ouch)
			{
			throw new NodeNotFoundException(
				"Unable to parse node ivorn"
				);
			}
		//
		// Initiate the transfer.
		return this.move(
			this.node(
				ivorn
				),
			properties
			) ;
		}

	/**
	 * Move a node to a new location.
	 * If the node already has stored data, then this may involve transfering the data to a new location.
	 * @param  node    The target node.
	 * @param  request The request properties.
	 * @return A new set of properties for the node.
	 * @throws DuplicateNodeException If a node with the same name already exists in the metadata tree.
	 * @throws NodeNotFoundException If the current node is no longer in the database.
	 * @throws NodeNotFoundException If the new parent node is no longer in the database.
	 * @throws FileManagerServiceException If a problem occurs when handling the request.
	 *
	 */
	public FileProperty[] move(NodeMock node, FileManagerProperties request)
		throws DuplicateNodeException, NodeNotFoundException, FileManagerServiceException
		{
		log.debug("");
		log.debug("FileManagerMock.move(Node, FileManagerProperties)");
		log.debug("  Node : " + node.getName());
		log.debug("  Node : " + node.getIdent());
		//
		// Get the current node properties.
		FileManagerProperties current = node.getProperties();
		//
		// Get the set of changed properties.
		FileManagerProperties changed = current.difference(
			request
			);
		//
		// Get the target parent node.
		Ivorn dest = null ;
		try {
			dest = changed.getManagerParentIvorn();
			}
		catch (FileManagerIdentifierException ouch)
			{
			throw new NodeNotFoundException(
				"Unable to parse node ivorn"
				);
			}
		//
		// Get the target node name.
		String name = changed.getManagerResourceName();
		//
		// If the node name or parent have changed.
		if ((null != name) || (null != dest))
			{
			this.moveNode(
				node,
				name,
				dest
				);
			}
		//
		// Get the target store location.
		Ivorn store = null ;
		try {
			store = changed.getManagerLocationIvorn();
			}
		catch (FileManagerIdentifierException ouch)
			{
			throw new NodeNotFoundException(
				"Unable to parse store ivorn"
				);
			}
		//
		// If the node location has changed.
		if (null != store)
			{
			this.moveStore(
				node,
				store
				);
			}
		//
		// Return the new node properties.
		return node.getProperties().toArray();
		}

	/**
	 * Move a node in the tree.
	 * @param node The node to move.
	 * @param name The new name for the node.
	 * @param dest The Ivorn of the destination node.
	 * @return The updated node.
	 *
	 */
	protected void moveNode(NodeMock node, String name, Ivorn dest)
		throws DuplicateNodeException, NodeNotFoundException, FileManagerServiceException
		{
		log.debug("");
		log.debug("FileManagerMock.move(Node, String, Ivorn)");
		log.debug("  Node : " + node.getName());
		log.debug("  Node : " + node.getIdent());
		log.debug("  Name : " + name);
		log.debug("  Dest : " + ((null != dest) ? dest.toString() : "null"));
		//
		// If the destination parent is set.
		if (null != dest)
			{
			//
			// Locate the destination node and use that.
			this.moveNode(
				node,
				name,
				this.node(
					dest
					)
				);
			}
		//
		// If the destination parent is not set.
		else {
			//
			// Locate the current parent and use that.
			this.moveNode(
				node,
				name,
				this.node(
					node.getParentIvorn()
					)
				);
			}
		}

	/**
	 * Move a node in the tree.
	 * @param node The node to move.
	 * @param name The new name for the node.
	 * @param dest The destination parent node.
	 * @return The updated node.
	 *
	 */
	protected void moveNode(NodeMock node, String name, NodeMock dest)
		throws DuplicateNodeException, NodeNotFoundException, FileManagerServiceException
		{
		log.debug("");
		log.debug("FileManagerMock.moveNode(Node, String, Node)");
		log.debug("  Node : " + node.getName());
		log.debug("  Node : " + node.getIdent());
		log.debug("  Name : " + name);
		log.debug("  Dest : " + dest.getName());
		log.debug("  Dest : " + dest.getIdent());
		//
		// Get the current parent.
		NodeMock parent = this.node(
			node.getParentIvorn()
			);
		//
		// Keep a record of the old name.
		String prev = node.getName();
		//
		// If the name has changed.
		if (null != name)
			{
			//
			// Check the name does not contain a '/'.
			if (name.indexOf('/') != -1)
				{
				throw new FileManagerServiceException(
					"Node name cannot contain '/'"
					);
				}
			node.setName(
				name
				);
			}
		//
		// Add the node into the new parent.
		try {
			dest.addNode(
				node
				);
			}
		catch (DuplicateNodeException ouch)
			{
			//
			// Put the name back to what it was.
			node.setName(
				prev
				);
			//
			// Re-throw the Exception.
			throw ouch ;
			}
		//
		// Remove the node from the old parent.
		try {
			parent.delNode(
				prev
				);
			}
		catch (NodeNotFoundException ouch)
			{
			//
			// Log the error.
			log.warn("");
			log.warn("Move failed to remove node from original parent");
			log.warn("Parent : " + parent.getIdent());
			log.warn("Child  : " + node.getIdent());
			//
			// Re-throw the Exception.
			throw ouch ;
			}
		}

	/**
	 * Get a node from our map.
	 *
	 */
	private NodeMock node(Ivorn ivorn)
		throws NodeNotFoundException
		{
		try {
			return this.node(
				new FileManagerIvornParser(
					ivorn
					).getResourceIdent()
				);
			}
		catch (FileManagerIdentifierException ouch)
			{
			throw new NodeNotFoundException(
				"Unable to parse node ivorn"
				) ;
			}
		}

	/**
	 * Get a node from our map.
	 *
	 */
	private NodeMock node(String ident)
		throws NodeNotFoundException
		{
		if (null != ident)
			{
			if (nodes.containsKey(ident))
				{
				return (NodeMock) nodes.get(ident) ;
				}
			else {
				throw new NodeNotFoundException(
					"Unable to locate node"
					) ;
				}
			}
		else {
			throw new NodeNotFoundException(
				"Null node identifier"
				) ;
			}
		}

	/**
	 * Move a file from one location to another.
	 *
	 */
	protected void moveStore(NodeMock node, Ivorn targetIvorn)
		throws DuplicateNodeException, NodeNotFoundException, FileManagerServiceException
		{
		log.debug("");
		log.debug("FileManagerMock.moveStore(Node, Ivorn)");
		log.debug("  Node  : " + node.getName());
		log.debug("  Node  : " + node.getIdent());
		log.debug("  Dest  : " + targetIvorn.toString());
		//
		// If the node is a data node.
		if (node.isDataNode())
			{
			log.debug("");
			log.debug("  PASS  : Node is a data node");
			//
			// Get the current node properties.
			FileManagerProperties current = node.getProperties();
			//
			// Get the current store location.
			Ivorn  sourceIvorn = null ;
			String sourceIdent = null ;
			try {
				sourceIvorn = current.getStoreResourceIvorn() ;
				sourceIdent = current.getStoreResourceIdent() ;
				}
			catch (FileStoreIdentifierException ouch)
				{
				log.warn("");
				log.warn("Unable to parse current store resource ivorn");
				log.warn(ouch);
				throw new FileManagerServiceException(
					"Unable to parse current store resource ivorn"
					);
				}
			//
			// If the node has some stored data.
			if (null != sourceIvorn)
				{
				log.debug("");
				log.debug("  PASS  : Got store resource ivorn");
				log.debug("  Ivorn : " + sourceIvorn.toString());
				//
				// Resolve the source filestore
				FileStoreDelegate sourceStore = resolve(
					sourceIvorn
					) ;
				log.debug("  PASS  : Got source filestore");
				//
				// Resolve the target filestore
				FileStoreDelegate targetStore = resolve(
					targetIvorn
					) ;
				log.debug("  PASS  : Got target filestore");
				//
				// Initiate the transfer from the source store.
				TransferProperties transfer = null ;
				try {
					transfer = 
						sourceStore.exportInit(
							new UrlGetRequest(
								current
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
				// Transfer the data into the destination store.
				TransferProperties result = null ;
				try {
					result = 
						targetStore.importData(
							transfer
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
				// Update the local properties.
				current.merge(
					result.getFileProperties(),
					new FileManagerPropertyFilter()
					);
				//
				// Set the store location.
				current.setManagerLocationIvorn(
					targetIvorn
					);
				//
				// Remove the data from the original store.
				try {
					sourceStore.delete(
						sourceIdent
						) ;
					}
				catch (Exception ouch)
					{
					log.warn("Exception thrown by FileStore.delete()");
					log.warn("  Exception : " + ouch);
//
// Ignore any problems with the delete.
// If the data has moved to the new server, then we shouldn't throw an Exception.
//					throw new FileManagerServiceException(
//						"Error occurred when calling FileStore service"
//						);
					}
				}
			//
			// If the node does not have any stored data yet.
			else {
				log.debug("  PASS  : No data stored yet.");
				log.debug("Setting location ivorn");
				log.debug("  Dest  : " + targetIvorn.toString());
//
// Should we try to resolve the filestore, just to check ?
//
				//
				// Update the default store location.
				current.setManagerLocationIvorn(
					targetIvorn
					);
				}
			}
		//
		// If the node is not a data node.
		else {
			log.debug("");
			log.debug("FAIL  : Node is not a data node");
			//
			// This implies a recursive move for all the child nodes ....
			//
			throw new UnsupportedOperationException(
				"Container location move not implemented yet"
				);
			}
		}

	/**
	 * Create a copy of a node.
	 * If the node already has stored data, then this will create a new copy of the data.
	 * @param  properties The request properties.
	 * @return An array of properties for the new node.
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
			NodeNotFoundException,
			DuplicateNodeException,
			FileManagerPropertiesException,
			FileManagerServiceException
		{
		log.debug("");
		log.debug("FileManagerMock.copy(FileProperty[])");
		if (null == request)
			{
			throw new FileManagerPropertiesException(
				"Null request properties"
				) ;
			}
		//
		// Wrap the request properties.
		FileManagerProperties properties = new FileManagerProperties(
			request
			);
		//
		// Parse the node ivorn.
		Ivorn ivorn = null ;
		try {
			ivorn = properties.getManagerResourceIvorn();
			}
		catch (FileManagerIdentifierException ouch)
			{
			throw new NodeNotFoundException(
				"Unable to parse node ivorn"
				);
			}
		//
		// Copy the node.
		return this.copy(
			this.node(
				ivorn
				),
			properties
			).getProperties().toArray() ;
		}

	/**
	 * Create a copy of a node.
	 * If the node already has stored data, then this will create a new copy of the data.
	 * @param  node    The target node.
	 * @param  request The request properties.
	 * @return An array of properties for the new node.
	 * @throws DuplicateNodeException If a node with the same name already exists in the metadata tree.
	 * @throws NodeNotFoundException If the current node is no longer in the database.
	 * @throws NodeNotFoundException If the parent node is no longer in the database.
	 * @throws FileManagerServiceException If a problem occurs when handling the request.
	 *
	 */
	protected NodeMock copy(NodeMock node, FileManagerProperties request)
		throws DuplicateNodeException, NodeNotFoundException, FileManagerServiceException
		{
		log.debug("");
		log.debug("FileManagerMock.copy(Node, FileManagerProperties)");
		log.debug("  Node : " + node.getName());
		log.debug("  Node : " + node.getIdent());
		//
		// Check the node is a data node.
		if (node.isDataNode())
			{
			log.debug("");
			log.debug("PASS  : Node is a data node");
			//
			// Get the current node properties.
			FileManagerProperties currentProperties = node.getProperties();
			//
			// Get the set of changed properties.
			FileManagerProperties changedProperties = currentProperties.difference(
				request
				);
			//
			// Create the target properties.
			FileManagerProperties targetProperties = new FileManagerProperties(
				currentProperties
				);
			targetProperties.merge(
				request,
				new FileManagerResourceFilter()
				);
			//
			// Try to find our target parent.
			NodeMock parentNode = null ;
			try {
				parentNode = this.node(
					targetProperties.getManagerParentIvorn()
					);
				}
			catch (FileManagerIdentifierException ouch)
				{
				throw new NodeNotFoundException(
					"Unable to parse parent node ivorn"
					);
				}
			//
			// Try to create the new node.
			NodeMock resultNode = this.addNode(
				parentNode,
				targetProperties.getManagerResourceName(),
				FileManagerProperties.DATA_NODE_TYPE
				);
			log.debug("  PASS  : Created node copy");
			//
			// Get the new node properties.
			FileManagerProperties resultProperties = resultNode.getProperties();
			//
			// Get the current data location.
			Ivorn  currentStoreIvorn = null ;
			String currentStoreIdent = null ;
			try {
				currentStoreIvorn = currentProperties.getStoreResourceIvorn() ;
				currentStoreIdent = currentProperties.getStoreResourceIdent() ;
				}
			catch (FileStoreIdentifierException ouch)
				{
				throw new FileManagerServiceException(
					"Unable to parse current filestore ivorn"
					);
				}
			//
			// If the node has already been stored.
			if (null != currentStoreIvorn)
				{
				log.debug("  PASS  : Node has data");
				//
				// Get the changed data location.
				Ivorn changedLocation = null ;
				try {
					changedLocation = changedProperties.getManagerLocationIvorn() ;
					}
				catch (FileManagerIdentifierException ouch)
					{
					throw new FileManagerServiceException(
						"Unable to parse changed filestore ivorn"
						);
					}
				//
				// If the data location has changed.
				if (null != changedLocation)
					{
					FileStoreDelegate sourceStore = null ;
					FileStoreDelegate targetStore = null ;
					//
					// Resolve the current filestore.
					try {
						sourceStore = resolve(
							currentStoreIvorn
							) ;
						}
					catch (Exception ouch)
						{
						throw new FileManagerServiceException(
							"Unable to resolve current filestore"
							);
						}
					//
					// Resolve the target filestore.
					try {
						targetStore = resolve(
							changedLocation
							) ;
						}
					catch (Exception ouch)
						{
						throw new FileManagerServiceException(
							"Unable to resolve target filestore"
							);
						}
					//
					// Initiate the transfer from the source store.
					TransferProperties transfer = null ;
					try {
						transfer = 
							sourceStore.exportInit(
								new UrlGetRequest(
									currentProperties
									)
								) ;
						}
					catch (Exception ouch)
						{
						log.debug("Exception thrown by FileStore.importInit()");
						log.debug("  Exception : " + ouch);
						throw new FileManagerServiceException(
							"Unable to initiate data transfer"
							);
						}
					//
					// Transfer the data into the destination store.
					TransferProperties transferResult = null ;
					try {
						transferResult = 
							targetStore.importData(
								transfer
								) ;
						}
					catch (Exception ouch)
						{
						log.debug("Exception thrown by FileStore.importInit()");
						log.debug("  Exception : " + ouch);
						throw new FileManagerServiceException(
							"Unable to complete data transfer"
							);
						}
					//
					// Update the result properties.
					resultProperties.merge(
						transferResult.getFileProperties(),
						new FileManagerPropertyFilter()
						);
					//
					// Set the store location.
					resultProperties.setManagerLocationIvorn(
						changedLocation
						);
					}
				//
				// If the data location has not changed.
				else {
					log.debug("  PASS  : Location not changed");
					//
					// Resolve the current filestore.
					FileStoreDelegate filestore = resolve(
						currentStoreIvorn
						) ;
					log.debug("  PASS  : Got current filestore");
					//
					// Ask the filestore to duplicate the data.
					try {
						log.debug("  PASS  : Asking filestore for duplicate");
						//
						// Ask the filestore to duplicate the data.
						FileManagerProperties updatedProperties = new FileManagerProperties(
							filestore.duplicate(
								currentStoreIdent,
								resultProperties.toArray()
								)
							);
						log.debug("  PASS  : Got duplicate from filestore");
						//
						// Update the local properties.
						resultProperties.merge(
							updatedProperties,
							new FileManagerPropertyFilter()
							);
						log.debug("  PASS  : Merged updated properties");
						}
					catch(Exception ouch)
						{
						log.warn("Exception thrown by FileStore.duplicate()");
						log.warn("  Exception : " + ouch);
//
// Set a property to indicate the data can't be accessed ?
// Pass the Exception on ?
// Rollback the transaction and delete the new node ?
//
						}
					}
				}
			//
			// If the node has not been stored yet.
			else {
				log.debug("  PASS  : Node is empty");
				//
				// Get the changed data location.
				Ivorn changedLocation = null ;
				try {
					changedLocation = changedProperties.getManagerLocationIvorn() ;
					}
				catch (FileManagerIdentifierException ouch)
					{
					throw new FileManagerServiceException(
						"Unable to parse changed filestore ivorn"
						);
					}
				//
				// If the data location has changed.
				if (null != changedLocation)
					{
					log.debug("  PASS  : Location changed");
					//
					// Set the target location.
					resultProperties.setManagerLocationIvorn(
						changedLocation
						);
//
// Make sure the store ivorn is blank ?
// Should already be, but need to check ...
//
					}
				}
			//
			// Return the new node properties.
			return resultNode ;
			}
		//
		// If the node is not a data node.
		else {
			log.debug("");
			log.debug("FAIL  : Node is not a data node");
			//
			// This implies a recursive copy for all the child nodes ....
			throw new UnsupportedOperationException(
				"Container location copy not implemented yet"
				);
			}
		}

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
			FileManagerServiceException,
			NodeNotFoundException
		{
		log.debug("");
		log.debug("FileManagerMock.delete(String)");
		log.debug("  Ident : " + ident);
		//
		// Try to delete the node.
		deleteNode(
			this.node(
				ident
				)
			);
		}

	/**
	 * Delete a node.
	 * @param node The node to delete.
	 * @throws NodeNotFoundException If the node does not exist.
	 * @throws FileManagerServiceException If a problem occurs when handling the request. 
     * @throws RemoteException If the WebService call fails.
	 *
	 */
	protected void deleteNode(NodeMock node)
		throws
			FileManagerServiceException,
			NodeNotFoundException
		{
		log.debug("");
		log.debug("FileManagerMock.delete(Node)");
		log.debug("  Node : " + node.getName());
		log.debug("  Node : " + node.getIdent());
		//
		// Get the node parent.
		NodeMock parent = this.node(
			node.getParentIvorn()
			);
		//
		// If the node is a data node.
		if (node.isDataNode())
			{
			log.debug("");
			log.debug("PASS  : Node is a data node");
			//
			// Get the current node properties.
			FileManagerProperties current = node.getProperties();
			//
			// Get the current store location.
			Ivorn  sourceIvorn = null ;
			String sourceIdent = null ;
			try {
				sourceIvorn = current.getStoreResourceIvorn() ;
				sourceIdent = current.getStoreResourceIdent() ;
				}
			catch (FileStoreIdentifierException ouch)
				{
				log.warn("");
				log.warn("Unable to parse current store resource ivorn");
				log.warn(ouch);
				throw new FileManagerServiceException(
					"Unable to parse current store resource ivorn"
					);
				}
			//
			// If the node has some stored data.
			if (null != sourceIvorn)
				{
				log.debug("");
				log.debug("  PASS  : Node has stored data");
				log.debug("  PASS  : Got store resource ivorn");
				log.debug("  Ivorn : " + sourceIvorn.toString());
				//
				// Resolve the source filestore
				FileStoreDelegate sourceStore = resolve(
					sourceIvorn
					) ;
				log.debug("  PASS  : Got source filestore");
				//
				// Remove the data from the filestore.
				try {
					sourceStore.delete(
						sourceIdent
						) ;
					}
				catch (Exception ouch)
					{
					log.warn("Exception thrown by FileStore.delete()");
					log.warn("  Exception : " + ouch);
//
// Ignore any problems with the delete.
//					throw new FileManagerServiceException(
//						"Error occurred when calling FileStore service"
//						);
					}
				}
			//
			// If the node does not have any stored data.
			else {
				log.debug("  PASS  : Node is empty");
				}
			//
			// Remove the node from our global map.
			nodes.remove(
				node.getIdent()
				);
			//
			// Remove the node from the tree.
			try {
				parent.delNode(
					node.getName()
					);
				}
			catch (NodeNotFoundException ouch)
				{
				//
				// Log the error.
				log.warn("");
				log.warn("Move failed to remove node from original parent");
				log.warn("Parent : " + parent.getIdent());
				log.warn("Child  : " + node.getIdent());
				//
				// Re-throw the Exception.
				throw ouch ;
				}
			}
		//
		// If the node is a container.
		else {
			log.debug("");
			log.debug("PASS  : Node is a container node");
			}
		}

	}
