/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/FileManagerMock.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:43:58 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerMock.java,v $
 *   Revision 1.5  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.4.2.2  2005/01/25 11:15:58  dave
 *   Fixed NullPointer bug in manager.
 *   Refactored client test case ...
 *
 *   Revision 1.4.2.1  2005/01/20 07:17:15  dave
 *   Added import data from URL to server side logic ....
 *   Tidied up tabs in some files.
 *
 *   Revision 1.4  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.3.4.8  2005/01/12 14:33:48  dave
 *   Removed System.out.println debug ...
 *
 *   Revision 1.3.4.7  2005/01/12 13:16:27  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.3.4.6  2005/01/12 12:40:08  dave
 *   Added account handling to store ...
 *
 *   Revision 1.3.4.5  2005/01/10 21:27:47  dave
 *   Refactores NodeMock as FileManagerStoreNode ...
 *
 *   Revision 1.3.4.4  2005/01/10 15:36:27  dave
 *   Refactored store into a separate interface and mock impl ...
 *
 *   Revision 1.3.4.3  2005/01/07 12:18:00  dave
 *   Added StoreClientWrapperTest
 *   Added StoreFileWrapper
 *
 *   Revision 1.3.4.2  2004/12/24 02:42:45  dave
 *   Changed delete to use ivorn ...
 *
 *   Revision 1.3.4.1  2004/12/24 02:05:05  dave
 *   Refactored exception handling, removing IdentifierException from the public API ...
 *
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
        log.debug("");
        log.debug("FileManagerMock.addAccount()");
        log.debug("  Account : " + ident);
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
        return node.getProperties().toArray() ;
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
        log.debug("");
        log.debug("FileManagerMock.getAccount()");
        log.debug("  Account : " + ident);
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
            return node.getProperties().toArray() ;
            }
        //
        // If the node does not exist.
        else {
            throw new NodeNotFoundException() ;
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
        log.debug("");
        log.debug("FileManagerMock.getNode()");
        log.debug("  Ivorn : " + ivorn);
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
        log.debug("");
        log.debug("FileManagerMock.refresh()");
        log.debug("  Ivorn : " + ivorn);
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
                log.debug("Found node ...");
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
        log.debug("");
        log.debug("FileManagerMock.getChild()");
        log.debug("  Ivorn : " + ivorn);
        log.debug("  Path  : " + path);
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
            throw new NodeNotFoundException(
                "Unable to parse parent identifier"
                );
            }
        //
        // Check if the root node exists.
        if (store.hasNode(ident))
            {
            FileManagerStoreNode node = store.getNode(ident) ;
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
        String ident = null ;
        try {
            ident = new FileManagerIvornParser(
                ivorn
                ).getResourceIdent();
            }
        catch (FileManagerIdentifierException ouch)
            {
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
                }
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
        String ident = null ;
        try {
            ident = new FileManagerIvornParser(
                ivorn
                ).getResourceIdent();
            }
        catch (FileManagerIdentifierException ouch)
            {
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
                    log.warn("Unable to parse child node ivorn");
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
        log.debug("");
        log.debug("FileManagerMock.importInit(FileProperty[])");
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
        log.debug("");
        log.debug("FileManagerMock.importInit(Node, Properties)");
        log.debug("  Node : " + node.getName());
        log.debug("  Node : " + node.getIdent());
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
     * This calls the FileStore to request the HTTP (GET) URL to read the data from the FileStore.
     * @param request The request properties.
     * @throws NodeNotFoundException If the target node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public TransferProperties exportInit(FileProperty[] request)
        throws FileManagerServiceException, NodeNotFoundException
        {
        log.debug("");
        log.debug("FileManagerMock.exportInit(FileProperty[])");
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
        log.debug("");
        log.debug("FileManagerMock.move(FileProperty[])");
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
        log.debug("");
        log.debug("FileManagerMock.moveNode(Node, String, Node)");
        log.debug("  Node : " + node.getName());
        log.debug("  Node : " + node.getIdent());
        log.debug("  Name : " + name);
        log.debug("  Dest : " + dest.getName());
        log.debug("  Dest : " + dest.getIdent());
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
//                    throw new FileManagerServiceException(
//                        "Error occurred when calling FileStore service"
//                        );
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
        log.debug("");
        log.debug("FileManagerMock.copy(FileProperty[])");
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
        log.debug("");
        log.debug("FileManagerMock.delete(String)");
        log.debug("  Ivorn : " + ivorn);
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
        log.debug("");
        log.debug("FileManagerMock.delete(Node)");
        log.debug("  Node : " + node.getName());
        log.debug("  Node : " + node.getIdent());
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
//                    throw new FileManagerServiceException(
//                        "Error occurred when calling FileStore service"
//                        );
                    }
                }
            //
            // If the node does not have any stored data.
            else {
                log.debug("  PASS  : Node is empty");
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
                }
            catch (NodeNotFoundException ouch)
                {
                //
                // Log the error.
                log.warn("");
                log.warn("Move failed to remove node from original parent");
                log.warn("Parent : " + parent.getIdent());
                log.warn("Child  : " + node.getIdent());
                }
            }
        //
        // If the node is a container.
        else {
            log.debug("");
            log.debug("FAIL  : Node is a container node");
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
        log.debug("");
        log.debug("FileManagerMock.importData(TransferProperties)");
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
                log.debug("");
                log.debug("  Node : " + node.getName());
                log.debug("  Node : " + node.getIdent());
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
        log.debug("");
        log.debug("FileManagerMock.importData(Node, TransferProperties)");
        log.debug("  Node : " + node.getName());
        log.debug("  Node : " + node.getIdent());
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
            log.warn("");
            log.warn("Unable to parse store location");
            log.warn(ouch);
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
                log.warn("");
                log.warn("Unable to parse store location");
                log.warn(ouch);
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
            log.debug("Exception thrown by FileStore.importdata()");
            log.debug("  Exception : " + ouch);
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
