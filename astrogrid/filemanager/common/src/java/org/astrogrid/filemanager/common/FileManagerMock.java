/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/FileManagerMock.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/02/10 14:38:34 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 */
package org.astrogrid.filemanager.common ;

import java.net.URL ;

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

import org.astrogrid.filestore.common.transfer.UrlGetRequest  ;
import org.astrogrid.filestore.common.transfer.UrlGetTransfer ;
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
 *@modified nww - added calls to commitNode where necessary/
 */
public class FileManagerMock
    implements FileManager
    {
    /**
     * Our debug logger.
     *
     */
    private static Log logger = LogFactory.getLog(FileManagerMock.class);

    /**
     * Our FileManager configuration.
     *
     */
    private FileManagerConfig config ;

    /**
     * Our FileManager store.
     *
     */
    private FileManagerStore store ;

    /**
     * Our FileStore resolver.
     *
     */
    private FileStoreDelegateResolver resolver ;

    /**
     * Our identifier factory.
     *
     */
    private FileManagerIvornFactory factory ;

    /**
     * Public constructor, using a custom configuration, identifier factory and resolver.
     * @param config   The local file manager configuration.
     * @param store    The local file manager store.
     * @param factory  A factory for creating resource identifiers.
     * @param resolver A resolver to locate filestores.
     *
     */
    public FileManagerMock(
        FileManagerConfig config,
        FileManagerStore  store,
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
        if (null == store)
            {
            throw new IllegalArgumentException(
                "Null manager store"
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
        this.store    = store ;
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
     * Create a root node for a new account.
     * @param ident The identifier for the account.
     * @return An array of properties for the new account node.
     * @throws DuplicateNodeException If the the account already exists.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public FileProperty[] addAccount(String ident)
        throws FileManagerServiceException, DuplicateNodeException
        {
        if (logger.isDebugEnabled()) {
            logger.debug("addAccount(ident = " + ident + ") - start");
        }

        if (null == ident)
            {
            throw new IllegalArgumentException(
                "Null account identifier"
                );
            }
        //
        // Check if the account already exists.
        if (store.hasAccount(ident))
            {
            throw new DuplicateNodeException(
                "Account already exists"
                ) ;
            }
        //
        // Create the new node.
        FileManagerStoreNode node = this.addNode(
            (FileManagerStoreNode) null,
            "home",
            FileManagerProperties.CONTAINER_NODE_TYPE
            ) ;
        //
        // Add the node to our map of accounts.
        store.addAccount(
            ident,
            node
            ) ;
        //
        // Return the new node.
        FileProperty[] returnFilePropertyArray = node.getProperties().toArray();
        if (logger.isDebugEnabled()) {
            logger.debug("addAccount() - end");
        }
        return returnFilePropertyArray ;
        }

    /**
     * Get the root node for an account
     * @param ident The account identifier.
     * @return An array of properties for the account node.
     * @throws NodeNotFoundException If the node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public FileProperty[] getAccount(String ident)
        throws FileManagerServiceException, NodeNotFoundException
        {
        if (logger.isDebugEnabled()) {
            logger.debug("getAccount(ident = " + ident + ") - start");
        }

        if (null == ident)
            {
            throw new IllegalArgumentException(
                "Null account identifier"
                ) ;
            }
        //
        // Check if the node exists.
        if (store.hasAccount(ident))
            {
            //
            // Get the account root.
            FileManagerStoreNode node = store.getAccount(ident);
            //
            // Return the node properties.
            FileProperty[] returnFilePropertyArray = node.getProperties()
                    .toArray();
            if (logger.isDebugEnabled()) {
                logger.debug("getAccount() - end");
            }
            return returnFilePropertyArray ;
            }
        //
        // If the node does not exist.
        else {
            throw new NodeNotFoundException(ident) ;
            }
        }

    /**
     * Get a specific node, indexed by ivorn identifier.
     * @param ivorn The node (ivorn) identifier.
     * @return The node specified by the identifier.
     * @throws NodeNotFoundException If the node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     * @todo Refactor this as getNode(ivorn, [path])
     *
     */
    public FileProperty[] getNode(String ivorn)
        throws FileManagerServiceException, NodeNotFoundException
        {
        if (logger.isDebugEnabled()) {
            logger.debug("getNode(ivorn = " + ivorn + ") - start");
        }

        if (null == ivorn)
            {
            throw new NodeNotFoundException(
                "Null identifier"
                ) ;
            }
        //
        // Parse the node ivorn.
        String path = null ;
        try {
            path = new FileManagerIvornParser(
                ivorn
                ).getResourceIdent();
            }
        catch (FileManagerIdentifierException ouch)
            {
            logger.error("getNode(String)", ouch);

            throw new NodeNotFoundException(
                "Unable to parse node identifier"
                );
            }
        if (null == path)
            {
            throw new NodeNotFoundException(
                "Null node identifier"
                ) ;
            }
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
            if (store.hasNode(ident))
                {
                FileManagerStoreNode node = store.getNode(ident) ;
                //
                // If we have more tokens.
                if (tokens.hasMoreTokens())
                    {
                    node = node.getChild(tokens) ;
                    }
                FileProperty[] returnFilePropertyArray = node.getProperties()
                        .toArray();
                if (logger.isDebugEnabled()) {
                    logger.debug("getNode() - end");
                }
                return returnFilePropertyArray ;
                }
            //
            // If the node does not exist.
            else {
                throw new NodeNotFoundException(ident) ;
                }
            }
        //
        // If the path is empty
        else {
            throw new NodeNotFoundException("no path") ;
            }
        }

    /**
     * Refresh the properties for a node, indexed by ivorn identifier.
     * If this node has stored data, this will trigger a call to the FileStore to update the data properties.
     * @param ivorn The node (ivorn) identifier.
     * @return The node properties.
     * @throws NodeNotFoundException If the node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public FileProperty[] refresh(String ivorn)
        throws FileManagerServiceException, NodeNotFoundException
        {
        if (logger.isDebugEnabled()) {
            logger.debug("refresh(ivorn = " + ivorn + ") - start");
        }

        if (null == ivorn)
            {
            throw new IllegalArgumentException(
                "Null identifier"
                ) ;
            }
        //
        // Parse the node ivorn.
        String path = null ;
        try {
            path = new FileManagerIvornParser(
                ivorn
                ).getResourceIdent();
            }
        catch (FileManagerIdentifierException ouch)
            {
            logger.error("refresh(String)", ouch);

            throw new NodeNotFoundException(
                "Unable to parse node identifier"
                );
            }
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
            if (store.hasNode(ident))
                {
                FileManagerStoreNode node = store.getNode(ident) ;
                //
                // If we have more tokens.
                if (tokens.hasMoreTokens())
                    {
                    node = node.getChild(tokens) ;
                    }
                logger.debug("Found node ...");
                //
                // Get the current node properties.
                FileManagerProperties current = node.getProperties();
                //
                // If this is a file node.
                if (node.isDataNode())
                    {
                    logger.debug("Found data node ...");
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
                        logger.warn("Unable to parse current store resource ivorn",ouch);
                        throw new FileManagerServiceException(
                            "Unable to parse current store resource ivorn"
                            );
                        }
                    //
                    // If the node has stored data.
                    if (null != dataIvorn)
                        {
                        logger.debug("  Found store location " + dataIvorn);
                        //
                        // Resolve the source filestore
                        FileStoreDelegate filestore = resolve(
                            dataIvorn
                            ) ;
                        logger.debug("  PASS  : Got data filestore");
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
                            logger.debug("  PASS  : Got updated properties");
                            logger.debug("  Size  : " + updated.getContentSize());
                            //
                            // Update the local properties.
                            current.merge(
                                updated,
                                new FileManagerPropertyFilter()
                                );
                            logger.debug("  PASS  : Merged updated properties");
                            logger.debug("  Size  : " + current.getContentSize());
                            }
//
// Should we treat this differently ?
// FileStoreNotFoundException
                        catch(Exception ouch)
                            {
                            logger.warn("Exception thrown by FileStore.properties()",ouch);
//
// Set a property to indicate the data can't be accessed ?
//
                            }
                        }
                    }
                //
                // Return the node properties.
                FileProperty[] returnFilePropertyArray = current.toArray();
                if (logger.isDebugEnabled()) {
                    logger.debug("refresh() - end");
                }
                return returnFilePropertyArray ;
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
     * @param ivorn The parent node (ivorn) identifier.
     * @param path  The target node path, from the parent node.
     * @return The node specified by the parent and path.
     * @throws NodeNotFoundException If the node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     * @todo Refactor this as getNode(ivorn, [path])
     *
     */
    public FileProperty[] getChild(String ivorn, String path)
        throws FileManagerServiceException, NodeNotFoundException
        {
        if (logger.isDebugEnabled()) {
            logger.debug("getChild(ivorn = " + ivorn + ", path = " + path
                    + ") - start");
        }

        if (null == ivorn)
            {
            throw new IllegalArgumentException(
                "Null identifier"
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
        String ident = null ;
        try {
            ident = new FileManagerIvornParser(
                ivorn
                ).getResourceIdent();
            }
        catch (FileManagerIdentifierException ouch)
            {
            logger.error("getChild(String, String)", ouch);

            throw new NodeNotFoundException(
                "Unable to parse parent identifier"
                );
            }
        //
        // Check if the root node exists.
        if (store.hasNode(ident))
            {
            FileManagerStoreNode node = store.getNode(ident) ;
            logger.debug(node);
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
     * Add a new child node.
     * @param ivorn The (ivorn) identifier of the parent node.
     * @param path  The new node name (and path).
     * @param type  The new node type.
     * @return The new node.
     * @throws DuplicateNodeException If a node with the same name already exists.
     * @throws NodeNotFoundException If the parent node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     * @todo Refactor this to handle path, and create missing folders.
     *
     */
    public FileProperty[] addNode(String ivorn, String name, String type)
        throws FileManagerServiceException, DuplicateNodeException, NodeNotFoundException
        {
        if (logger.isDebugEnabled()) {
            logger.debug("addNode(ivorn = " + ivorn + ", name = " + name
                    + ", type = " + type + ") - start");
        }

        if (null == ivorn)
            {
            throw new IllegalArgumentException(
                "Null parent identifier"
                ) ;
            }
        //
        // Parse the parent ivorn.
        String ident = null ;
        try {
            ident = new FileManagerIvornParser(
                ivorn
                ).getResourceIdent();
            }
        catch (FileManagerIdentifierException ouch)
            {
            logger.error("addNode(String, String, String)", ouch);

            throw new NodeNotFoundException(
                "Unable to parse parent identifier"
                );
            }
        //
        // Get the parent node.
        FileManagerStoreNode node = store.getNode(ident) ;
        //
        // If we found the parent node.
        if (null != node)
            {
            //
            // If the node is a container 
            if (node.isContainer())
                {
                FileManagerStoreNode child = this.addNode(
                    node,
                    name,
                    type
                    ) ;
                FileProperty[] returnFilePropertyArray = child.getProperties()
                        .toArray();
                if (logger.isDebugEnabled()) {
                    logger.debug("addNode() - end");
                }
                return returnFilePropertyArray ;
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
     * Add a new child node.
     * @param parent The parent node.
     * @param path   The new node name (and path).
     * @param type   The new node type.
     * @return The new node.
     * @throws DuplicateNodeException If a node with the same name already exists.
     * @throws NodeNotFoundException If the parent node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     * @todo Refactor this to handle path, and create missing folders.
     *
     */
    protected FileManagerStoreNode addNode(FileManagerStoreNode parent, String name, String type)
        throws FileManagerServiceException, DuplicateNodeException
        {
        if (logger.isDebugEnabled()) {
            logger.debug("addNode(parent = " + parent + ", name = " + name
                    + ", type = " + type + ") - start");
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
            FileManagerStoreNode node = store.addNode(
                (null != parent) ? parent.getIvorn() : null ,
                factory.ivorn(
                    config.getFileManagerIvorn()
                    ),
                name,
                type
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
                  store.commitNode(node);
                }
            //
            // Add the child to the parent.
            if (null != parent)
                {
                parent.addNode(
                    node
                    ) ;
                store.commitNode(parent);
                store.commitNode(node); // inefficient - possibly 2 commits. but clear for now.
                // @todo in future, need to clean up the modifications to different objects.
                
                }
            //
            // Return the new node.

            if (logger.isDebugEnabled()) {
                logger.debug("addNode() - end");
            }
            return node ;
            }        
        catch (FileManagerIdentifierException ouch)
            {
            logger.debug("Unable to create node identifier",ouch);
            throw new FileManagerServiceException(
                "Unable to create node identifier"
                );            
        } catch (NodeNotFoundException e) {
            logger.debug("Couldn't find node to commit",e);
            throw new FileManagerServiceException("Couldn't find node to commit",e);
        }
        }

    /**
     * Template for generating an array of strings.
     *
     */
    private static final String[] template = new String[0] ;

    /**
     * Get a list of the children of a specific node.
     * @param ivorn The parent node (ivorn) identifier.
     * @return An array of ivorn(s) for the child node(s).
     * @throws NodeNotFoundException If the parent node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     * @todo Refactor this to listNodes(ivorn, [path])
     *
     */
    public String[] getChildren(String ivorn)
        throws FileManagerServiceException, NodeNotFoundException
        {
        if (logger.isDebugEnabled()) {
            logger.debug("getChildren(ivorn = " + ivorn + ") - start");
        }

        if (null == ivorn)
            {
            throw new IllegalArgumentException(
                "Null parent identifier"
                ) ;
            }
        //
        // Parse the parent ivorn.
        String ident = null ;
        try {
            ident = new FileManagerIvornParser(
                ivorn
                ).getResourceIdent();
            }
        catch (FileManagerIdentifierException ouch)
            {
            logger.error("getChildren(String)", ouch);

            throw new NodeNotFoundException(
                "Unable to parse parent identifier"
                );
            }
        //
        // Check if the node exists.
        if (store.hasNode(ident))
            {
            //
            // Get the parent node.
            FileManagerStoreNode node = store.getNode(ident) ;
            logger.debug(node);
            //
            // Create an array of the child nodes.
            Collection nodes = node.getChildren() ;
            Iterator   iter  = nodes.iterator() ;
            Vector    vector = new Vector();
            while(iter.hasNext())
                {
                try {
                    Ivorn child = ((FileManagerStoreNode) iter.next()).getIvorn();
                    if (null != child)
                        {
                        vector.add(
                            child.toString()
                            );
                        }
                    }
                catch (FileManagerIdentifierException ouch)
                    {
                    logger.warn("Unable to parse child node ivorn");
                    }
                }
            String[] array = (String[]) vector.toArray(
                template
                );

            if (logger.isDebugEnabled()) {
                logger.debug("getChildren() - end");
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
            logger.debug("Exception thrown by FileStoreResolver.resolve()",ouch);
            throw new FileManagerServiceException(
                "Unable to locate FileStore service"
                );
            }
        }

    /**
     * Initialise a data transfer into a FileStore.
     * The request properties need to specify the (ivorn) identifier of an existing node, or the identifier of a parent node and the new node name and path.
     * @param request The request properties.
     * @throws NodeNotFoundException If the target node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     * @todo Refactor this to handle path, and create missing folders.
     *
     */
    public TransferProperties importInit(FileProperty[] request)
        throws FileManagerServiceException, NodeNotFoundException
        {
        logger.debug("FileManagerMock.importInit(FileProperty[])");
        if (null == request)
            {
            throw new NodeNotFoundException(
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
            if (store.hasNode(ident))
                {
                FileManagerStoreNode node = store.getNode(ident) ;
                logger.debug(node);
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
     * The request properties need to specify the (ivorn) identifier of an existing node, or the identifier of a parent node and the new node name and path.
     * @param node    The target node.
     * @param request The request properties.
     * @throws NodeNotFoundException If the target node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    protected TransferProperties importInit(FileManagerStoreNode node, FileManagerProperties request)
        throws FileManagerServiceException, NodeNotFoundException
        {
        if (logger.isDebugEnabled()) {
            logger.debug("importInit(node = " + node + ", request = " + request
                    + ") - start");
        }

        //
        // Check the node is a data node.
        if (node.isDataNode() != true)
            {
            throw new FileManagerServiceException(
                "Invalid operation, not a data node"
                );
            }
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
            logger.warn("Unable to parse store location",ouch);
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
                logger.warn("Unable to parse store location",ouch);
                throw new FileManagerServiceException(
                    "Unable to parse store location"
                    );
                }
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
            logger.warn("Unable to set resource ivorn",ouch);
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
            logger.warn("Unable to set parent ivorn",ouch);
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
            logger.debug("Exception thrown by FileStore.importInit()",ouch);
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
            logger.warn("");
            logger.warn("Unable to set resource ivorn");
            logger.warn(ouch);
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
            logger.warn("");
            logger.warn("Unable to set parent ivorn");
            logger.warn(ouch);
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
            logger.warn("");
            logger.warn("Unable to set location ivorn");
            logger.warn(ouch);
            response.setManagerLocationIvorn(
                target
                );
            }
        //
        // Save these as the new properties.
        node.setProperties(
            response
            ) ;
        store.commitNode(node);
        //
        // Update the transfer properties.
        transfer.setFileProperties(
            response
            );

        if (logger.isDebugEnabled()) {
            logger.debug("importInit() - end");
        }
        return transfer ;
        }

    /**
     * Initialise a data transfer from a FileStore.
     * This calls the FileStore to request the HTTP (GET) URL to read the data from the FileStore.
     * @param request The request properties.
     * @throws NodeNotFoundException If the target node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public TransferProperties exportInit(FileProperty[] request)
        throws FileManagerServiceException, NodeNotFoundException
        {
        logger.debug("");
        logger.debug("FileManagerMock.exportInit(FileProperty[])");
        if (null == request)
            {
            throw new NodeNotFoundException(
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
            if (store.hasNode(ident))
                {
                FileManagerStoreNode node = store.getNode(ident) ;
                logger.debug("");
                logger.debug("  Node : " + node.getName());
                logger.debug("  Node : " + node.getIdent());
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
     * This calls the FileStore to request the HTTP (GET) URL to read the data from the FileStore.
     * @param node    The target node.
     * @param request The request properties.
     * @throws NodeNotFoundException If the target node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    protected TransferProperties exportInit(FileManagerStoreNode node, FileManagerProperties request)
        throws FileManagerServiceException, NodeNotFoundException
        {
        logger.debug("");
        logger.debug("FileManagerMock.exportInit(Node, Properties)");
        logger.debug("  Node : " + node.getName());
        logger.debug("  Node : " + node.getIdent());
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
            logger.debug("Exception thrown by FileStore.importInit()");
            logger.debug("  Exception : " + ouch);
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
        store.commitNode(node);
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
     * @param  request The request properties.
     * @return A new set of properties for the node.
     * @throws DuplicateNodeException If a node with the same name already exists in the metadata tree.
     * @throws NodeNotFoundException If the current node is no longer in the database.
     * @throws NodeNotFoundException If the new parent node is no longer in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public FileProperty[] move(FileProperty[] request)
        throws FileManagerServiceException, DuplicateNodeException, NodeNotFoundException
        {
        logger.debug("");
        logger.debug("FileManagerMock.move(FileProperty[])");
        if (null == request)
            {
            throw new NodeNotFoundException(
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
     * @throws NodeNotFoundException If the new parent node is no longer in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public FileProperty[] move(FileManagerStoreNode node, FileManagerProperties request)
        throws DuplicateNodeException, NodeNotFoundException, FileManagerServiceException
        {
        logger.debug("");
        logger.debug("FileManagerMock.move(Node, FileManagerProperties)");
        logger.debug("  Node : " + node.getName());
        logger.debug("  Node : " + node.getIdent());
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
     * @param dest The (ivorn) identifier of the new parent.
     * @return The updated node.
     * @throws DuplicateNodeException If a node with the same name already exists in the metadata tree.
     * @throws NodeNotFoundException If the new parent node is no longer in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    protected void moveNode(FileManagerStoreNode node, String name, Ivorn dest)
        throws FileManagerServiceException, DuplicateNodeException, NodeNotFoundException
        {
        logger.debug("");
        logger.debug("FileManagerMock.move(Node, String, Ivorn)");
        logger.debug("  Node : " + node.getName());
        logger.debug("  Node : " + node.getIdent());
        logger.debug("  Name : " + name);
        logger.debug("  Dest : " + ((null != dest) ? dest.toString() : "null"));
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
            FileManagerStoreNode parent = null ;
            try {
                parent = this.node(
                    node.getParentIvorn()
                    );
                }
            catch (FileManagerIdentifierException ouch)
                {
                throw new FileManagerServiceException(
                    "Unable to oarse parent node ivorn"
                    );
                }
            this.moveNode(
                node,
                name,
                parent
                );
            }
        }

    /**
     * Move a node in the tree.
     * @param node The node to move.
     * @param name The new name for the node.
     * @param dest The new parent node.
     * @return The updated node.
     * @throws DuplicateNodeException If a node with the same name already exists in the metadata tree.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    protected void moveNode(FileManagerStoreNode node, String name, FileManagerStoreNode dest)
        throws FileManagerServiceException, DuplicateNodeException
        {
        logger.debug("");
        logger.debug("FileManagerMock.moveNode(Node, String, Node)");
        logger.debug("  Node : " + node.getName());
        logger.debug("  Node : " + node.getIdent());
        logger.debug("  Name : " + name);
        logger.debug("  Dest : " + dest.getName());
        logger.debug("  Dest : " + dest.getIdent());
        //
        // Get the current parent.
        FileManagerStoreNode parent = null;
        try {
            parent = this.node(
                node.getParentIvorn()
                );
            }
        catch (FileManagerIdentifierException ouch)
            {
            throw new FileManagerServiceException(
                "Unable to parse parent node identifier"
                );
            }
        catch (NodeNotFoundException ouch)
            {
            throw new FileManagerServiceException(
                "Unable to locate parent node"
                );
            }
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
            try {
                store.commitNode(node);
            } catch (NodeNotFoundException e) {
                throw new FileManagerServiceException("Node does not exist");
            }
            }
        //
        // Add the node into the new parent.
        try {
            dest.addNode(
                node
                );            
            store.commitNode(node);
        } catch (NodeNotFoundException e) {
                throw new FileManagerServiceException("Destination does not exist");
        }
        catch (DuplicateNodeException ouch)
            {
            //
            // Put the name back to what it was.
            node.setName(
                prev
                );
            try {
                store.commitNode(node);
            } catch (NodeNotFoundException e) {
                // oh well. 
            }
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
              store.commitNode(parent);
            }
        catch (NodeNotFoundException ouch)
            {
            //
            // Log the error.
            logger.warn("");
            logger.warn("Move failed to remove node from original parent");
            logger.warn("Parent : " + parent.getIdent());
            logger.warn("Child  : " + node.getIdent());
            }
        }

    /**
     * Get a node from our map.
     * @param ivorn The node (ivorn) identifier.
     * @throws NodeNotFoundException If the node is not in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    private FileManagerStoreNode node(Ivorn ivorn)
        throws NodeNotFoundException, FileManagerServiceException
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
     * @param ident The node identifier.
     * @throws NodeNotFoundException If the node is not in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    private FileManagerStoreNode node(String ident)
        throws NodeNotFoundException, FileManagerServiceException
        {
        if (null != ident)
            {
            if (store.hasNode(ident))
                {
                return store.getNode(ident) ;
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
     * @param node The node to move.
     * @param targetIvorn The destination filestore (ivorn) identifier).
     * @throws DuplicateNodeException If the target node already exists.
     * @throws NodeNotFoundException If the current node does not exist.
     * @throws NodeNotFoundException If the target parent node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    protected void moveStore(FileManagerStoreNode node, Ivorn targetIvorn)
        throws DuplicateNodeException, NodeNotFoundException, FileManagerServiceException
        {
        logger.debug("");
        logger.debug("FileManagerMock.moveStore(Node, Ivorn)");
        logger.debug("  Node  : " + node.getName());
        logger.debug("  Node  : " + node.getIdent());
        logger.debug("  Dest  : " + targetIvorn.toString());
        //
        // If the node is a data node.
        if (node.isDataNode())
            {
            logger.debug("");
            logger.debug("  PASS  : Node is a data node");
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
                logger.warn("");
                logger.warn("Unable to parse current store resource ivorn");
                logger.warn(ouch);
                throw new FileManagerServiceException(
                    "Unable to parse current store resource ivorn"
                    );
                }
            //
            // If the node has some stored data.
            if (null != sourceIvorn)
                {
                logger.debug("");
                logger.debug("  PASS  : Got store resource ivorn");
                logger.debug("  Ivorn : " + sourceIvorn.toString());
                //
                // Resolve the source filestore
                FileStoreDelegate sourceStore = resolve(
                    sourceIvorn
                    ) ;
                logger.debug("  PASS  : Got source filestore");
                //
                // Resolve the target filestore
                FileStoreDelegate targetStore = resolve(
                    targetIvorn
                    ) ;
                logger.debug("  PASS  : Got target filestore");
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
                    logger.debug("Exception thrown by FileStore.importInit()");
                    logger.debug("  Exception : " + ouch);
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
                    logger.debug("Exception thrown by FileStore.importInit()");
                    logger.debug("  Exception : " + ouch);
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
                    logger.warn("Exception thrown by FileStore.delete()");
                    logger.warn("  Exception : " + ouch);
//
// Ignore any problems with the delete.
// If the data has moved to the new server, then we shouldn't throw an Exception.
//                    throw new FileManagerServiceException(
//                        "Error occurred when calling FileStore service"
//                        );
                    }
                }
            //
            // If the node does not have any stored data yet.
            else {
                logger.debug("  PASS  : No data stored yet.");
                logger.debug("Setting location ivorn");
                logger.debug("  Dest  : " + targetIvorn.toString());
//
// Should we try to resolve the filestore, just to check ?
//
                //
                // Update the default store location.
                current.setManagerLocationIvorn(
                    targetIvorn
                    );
                }
                store.commitNode(node);
            }
        //
        // If the node is not a data node.
        else {
            logger.debug("");
            logger.debug("FAIL  : Node is not a data node");
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
     * @param  request The request properties.
     * @return A set of properties for the new node.
     * @throws DuplicateNodeException If a node with the same name already exists in the metadata tree.
     * @throws NodeNotFoundException If the current node is no longer in the database.
     * @throws NodeNotFoundException If the new parent node is no longer in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public FileProperty[] copy(FileProperty[] request)
        throws
            NodeNotFoundException,
            DuplicateNodeException,
            FileManagerServiceException
        {
        logger.debug("");
        logger.debug("FileManagerMock.copy(FileProperty[])");
        if (null == request)
            {
            throw new NodeNotFoundException(
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
     * @return A set of properties for the new node.
     * @throws DuplicateNodeException If a node with the same name already exists in the metadata tree.
     * @throws NodeNotFoundException If the new parent node is no longer in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    protected FileManagerStoreNode copy(FileManagerStoreNode node, FileManagerProperties request)
        throws DuplicateNodeException, NodeNotFoundException, FileManagerServiceException
        {
        logger.debug("");
        logger.debug("FileManagerMock.copy(Node, FileManagerProperties)");
        logger.debug("  Node : " + node.getName());
        logger.debug("  Node : " + node.getIdent());
        //
        // Check the node is a data node.
        if (node.isDataNode())
            {
            logger.debug("");
            logger.debug("PASS  : Node is a data node");
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
            FileManagerStoreNode parentNode = null ;
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
            FileManagerStoreNode resultNode = this.addNode(
                parentNode,
                targetProperties.getManagerResourceName(),
                FileManagerProperties.DATA_NODE_TYPE
                );
            logger.debug("  PASS  : Created node copy");
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
                logger.debug("  PASS  : Node has data");
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
                        logger.debug("Exception thrown by FileStore.importInit()");
                        logger.debug("  Exception : " + ouch);
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
                        logger.debug("Exception thrown by FileStore.importInit()");
                        logger.debug("  Exception : " + ouch);
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
                    logger.debug("  PASS  : Location not changed");
                    //
                    // Resolve the current filestore.
                    FileStoreDelegate filestore = resolve(
                        currentStoreIvorn
                        ) ;
                    logger.debug("  PASS  : Got current filestore");
                    //
                    // Ask the filestore to duplicate the data.
                    try {
                        logger.debug("  PASS  : Asking filestore for duplicate");
                        //
                        // Ask the filestore to duplicate the data.
                        FileManagerProperties updatedProperties = new FileManagerProperties(
                            filestore.duplicate(
                                currentStoreIdent,
                                resultProperties.toArray()
                                )
                            );
                        logger.debug("  PASS  : Got duplicate from filestore");
                        //
                        // Update the local properties.
                        resultProperties.merge(
                            updatedProperties,
                            new FileManagerPropertyFilter()
                            );
                        logger.debug("  PASS  : Merged updated properties");
                        }
                    catch(Exception ouch)
                        {
                        logger.warn("Exception thrown by FileStore.duplicate()");
                        logger.warn("  Exception : " + ouch);
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
                logger.debug("  PASS  : Node is empty");
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
                    logger.debug("  PASS  : Location changed");
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
            logger.debug("");
            logger.debug("FAIL  : Node is not a data node");
            //
            // This implies a recursive copy for all the child nodes ....
            throw new UnsupportedOperationException(
                "Container location copy not implemented yet"
                );
            }
        }

    /**
     * Delete a node.
     * @param ivorn The node (ivorn) identifier.
     * @throws NodeNotFoundException If the node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     * @todo Refactor this to take an ivorn.
     *
     */
    public void delete(String ivorn)
        throws
            NodeNotFoundException,
            FileManagerServiceException
        {
        logger.debug("");
        logger.debug("FileManagerMock.delete(String)");
        logger.debug("  Ivorn : " + ivorn);
        if (null == ivorn)
            {
            throw new IllegalArgumentException(
                "Null node identifier"
                ) ;
            }
        //
        // Parse the node ivorn.
        String ident = null ;
        try {
            ident = new FileManagerIvornParser(
                ivorn
                ).getResourceIdent();
            }
        catch (FileManagerIdentifierException ouch)
            {
            throw new NodeNotFoundException(
                "Unable to parse node identifier"
                );
            }
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
     *
     */
    protected void deleteNode(FileManagerStoreNode node)
        throws NodeNotFoundException, FileManagerServiceException
        {
        logger.debug("");
        logger.debug("FileManagerMock.delete(Node)");
        logger.debug("  Node : " + node.getName());
        logger.debug("  Node : " + node.getIdent());
        //
        // Get the node parent.
        FileManagerStoreNode parent = null;
        try {
            parent = this.node(
                node.getParentIvorn()
                );
            }
        catch (FileManagerIdentifierException ouch)
            {
            throw new FileManagerServiceException(
                "Unable to parse parent node identifier"
                );
            }
        catch (NodeNotFoundException ouch)
            {
            throw new FileManagerServiceException(
                "Unable to locate parent node"
                );
            }
        //
        // If the node is a data node.
        if (node.isDataNode())
            {
            logger.debug("");
            logger.debug("PASS  : Node is a data node");
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
                logger.warn("");
                logger.warn("Unable to parse current store resource ivorn");
                logger.warn(ouch);
                throw new FileManagerServiceException(
                    "Unable to parse current store resource ivorn"
                    );
                }
            //
            // If the node has some stored data.
            if (null != sourceIvorn)
                {
                logger.debug("");
                logger.debug("  PASS  : Node has stored data");
                logger.debug("  PASS  : Got store resource ivorn");
                logger.debug("  Ivorn : " + sourceIvorn.toString());
                //
                // Resolve the source filestore
                FileStoreDelegate sourceStore = resolve(
                    sourceIvorn
                    ) ;
                logger.debug("  PASS  : Got source filestore");
                //
                // Remove the data from the filestore.
                try {
                    sourceStore.delete(
                        sourceIdent
                        ) ;
                    }
                catch (Exception ouch)
                    {
                    logger.warn("Exception thrown by FileStore.delete()");
                    logger.warn("  Exception : " + ouch);
//
// Ignore any problems with the delete.
//                    throw new FileManagerServiceException(
//                        "Error occurred when calling FileStore service"
//                        );
                    }
                }
            //
            // If the node does not have any stored data.
            else {
                logger.debug("  PASS  : Node is empty");
                }
            //
            // Remove the node from our global map.
            store.delNode(
                node
                );
            //
            // Remove the node from the tree.
            try {
                parent.delNode(
                    node.getName()
                    );
                 store.commitNode(parent);
                }
            catch (NodeNotFoundException ouch)
                {
                //
                // Log the error.
                logger.warn("");
                logger.warn("Move failed to remove node from original parent");
                logger.warn("Parent : " + parent.getIdent());
                logger.warn("Child  : " + node.getIdent());
                }
            }
        //
        // If the node is a container.
        else {
            logger.debug("");
            logger.debug("FAIL  : Node is a container node");
            }
        }

    /**
     * Transfer data from a source URL into a node.
     * @param request The transfer request properties.
     * @return The updated file properties for the node, after the transfer.
     * @throws NodeNotFoundException If the target node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     * @todo Refactor this to handle path, and create missing folders.
     *
     */
    public FileProperty[] importData(TransferProperties request)
        throws
            NodeNotFoundException,
            FileManagerServiceException
        {
        logger.debug("");
        logger.debug("FileManagerMock.importData(TransferProperties)");
        if (null == request)
            {
            throw new NodeNotFoundException(
                "Null transfer properties"
                ) ;
            }
        //
        // Get the node properties from the transfer.
        FileManagerProperties properties = new FileManagerProperties(
            request.getFileProperties()
            );
        if (null == properties)
            {
            throw new NodeNotFoundException(
                "Null target properties"
                ) ;
            }
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
            if (store.hasNode(ident))
                {
                FileManagerStoreNode node = store.getNode(ident) ;
                logger.debug("");
                logger.debug("  Node : " + node.getName());
                logger.debug("  Node : " + node.getIdent());
                //
                // Initiate the transfer.
                return this.importData(
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
     * Transfer data from a source URL into a node.
     * @param node The target node.
     * @param request The transfer request properties.
     * @return The updated properties for the node.
     * @throws NodeNotFoundException If the target node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     * @todo Refactor this to handle path, and create missing folders.
     *
     */
    protected FileProperty[] importData(FileManagerStoreNode node, TransferProperties request)
        throws FileManagerServiceException, NodeNotFoundException
        {
        logger.debug("");
        logger.debug("FileManagerMock.importData(Node, TransferProperties)");
        logger.debug("  Node : " + node.getName());
        logger.debug("  Node : " + node.getIdent());
        //
        // Check the node is a data node.
        if (node.isDataNode() != true)
            {
            throw new FileManagerServiceException(
                "Invalid operation, not a data node"
                );
            }
        //
        // Check the transfer request has a URL.
        if (null == request.getLocation())
            {
            throw new FileManagerServiceException(
                "Null source URL"
                );
            }
        //
        // Parse the source URL.
        URL source = null ;
        try {
            source = new URL(
                request.getLocation()
                );
            }
        catch(Exception ouch)
            {
            throw new FileManagerServiceException(
                "Invalid source URL",
                ouch
                );
            }
        //
        // Get the current node properties.
        FileManagerProperties current = node.getProperties();
        //
        // Merge the request properties.
        current.merge(
            request.getFileProperties(),
            new FileManagerPropertyFilter()
            );
        //
        // Create a new transfer request.
        TransferProperties transfer = new UrlGetTransfer(
            source,
            current
            );
        //
        // Get the current filestore.
        Ivorn target = null ;
        try {
            target = current.getStoreResourceIvorn() ;
            }
        catch (FileStoreIdentifierException ouch)
            {
            logger.warn("");
            logger.warn("Unable to parse store location");
            logger.warn(ouch);
            throw new FileManagerServiceException(
                "Unable to parse store location"
                );
            }
        //
        // If we don't have a target filestore.
        if (null == target)
            {
            //
            // Use the node location.
            try {
                target = current.getManagerLocationIvorn();
                }
            catch (FileManagerIdentifierException ouch)
                {
                logger.warn("");
                logger.warn("Unable to parse store location");
                logger.warn(ouch);
                throw new FileManagerServiceException(
                    "Unable to parse store location"
                    );
                }
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
        // Resolve the target filestore.
        FileStoreDelegate filestore = resolve(
            target
            ) ;
        //
        // Call the filestore to transfer the data.
        TransferProperties response = null ;
        try {
            response = 
                filestore.importData(
                    transfer
                    ) ;
            }
        catch (Exception ouch)
            {
            logger.debug("Exception thrown by FileStore.importdata()");
            logger.debug("  Exception : " + ouch);
            throw new FileManagerServiceException(
                "Error occurred when calling FileStore service"
                );
            }
        //
        // Merge the response properties.
        current.merge(
            response.getFileProperties(),
            new FileManagerPropertyFilter()
            );
        //
        // Save these as the new properties.
        node.setProperties(
            current
            ) ;
        //
        // Return the response properties.
        return current.toArray() ;
        }


    /**
     * Transfer data transfer from a node into destination URL.
     * @param request The request properties.
     * @throws NodeNotFoundException If the target node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public TransferProperties exportData(FileProperty[] request)
        throws
            NodeNotFoundException,
            FileManagerServiceException
        {
        return null ;
        }

    }
