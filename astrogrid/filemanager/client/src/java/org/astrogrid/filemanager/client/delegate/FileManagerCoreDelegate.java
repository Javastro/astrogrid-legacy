/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/delegate/Attic/FileManagerCoreDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:43:58 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerCoreDelegate.java,v $
 *   Revision 1.2  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.1.2.1  2005/01/22 07:54:16  dave
 *   Refactored delegate into a separate package ....
 *
 *   Revision 1.4  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.3.4.3  2005/01/12 14:20:57  dave
 *   Replaced tabs with spaces ....
 *
 *   Revision 1.3.4.2  2005/01/07 12:17:59  dave
 *   Added StoreClientWrapperTest
 *   Added StoreFileWrapper
 *
 *   Revision 1.3.4.1  2004/12/22 07:38:36  dave
 *   Started to move towards StoreClient API ...
 *
 *   Revision 1.3  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.13  2004/12/13 10:07:15  dave
 *   Fixed copy tests for delegate ...
 *
 *   Revision 1.1.2.12  2004/12/10 05:21:25  dave
 *   Added node and iterator to client API ...
 *
 *   Revision 1.1.2.11  2004/12/08 17:54:54  dave
 *   Added update to FileManager client and server side ...
 *
 *   Revision 1.1.2.10  2004/12/03 13:27:52  dave
 *   Core of internal move is in place ....
 *
 *   Revision 1.1.2.9  2004/12/02 19:11:54  dave
 *   Added move name and parent to manager ...
 *
 *   Revision 1.1.2.8  2004/11/29 18:05:07  dave
 *   Refactored methods names ....
 *   Added stubs for delete, copy and move.
 *
 *   Revision 1.1.2.7  2004/11/24 16:15:08  dave
 *   Added node functions to client ...
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
package org.astrogrid.filemanager.client.delegate ;

import java.util.List ;
import java.util.Arrays ;
import java.util.ArrayList ;
import java.util.Iterator ;
import java.util.Collections ;

import java.rmi.RemoteException ;
import java.net.URISyntaxException;

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

import org.astrogrid.filemanager.client.FileManagerNode;

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
     * Protected constructor, for a FileManager service.
     *
     */
    protected FileManagerCoreDelegate(FileManager manager)
        {
        if (null == manager)
            {
            throw new IllegalArgumentException(
                "Null manager reference"
                );
            }
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
        log.debug("");
        log.debug("FileManagerCoreDelegate.getServiceIvorn()");
        try {
            Ivorn ivorn = new Ivorn(
                manager.getIdentifier()
                ) ;
            log.debug("  Ivorn : " + ivorn.toString());
            return ivorn ;
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
        return new FileManagerDelegateNode(
            this,
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
     * Add a new child node.
     * @param name The node name.
     * @param type The node type (either FILE_NODE or CONTAINER_NODE).
     * @return A new node for the container.
     * @throws UnsupportedOperationException If this node represents a file.
     * @throws DuplicateNodeException If a node with the same name already exists.
     * @throws NodeNotFoundException If the current node is no longer in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     * @see FILE_NODE
     * @see CONTAINER_NODE
     *
     */
    protected FileManagerNode add(FileManagerNode parent, String name, String type)
        throws UnsupportedOperationException, DuplicateNodeException, NodeNotFoundException, FileManagerServiceException, FileManagerIdentifierException 
        {
        if (null == parent)
            {
            throw new IllegalArgumentException(
                "Null parent node"
                );
            }
        if (null == parent.ivorn())
            {
            throw new IllegalArgumentException(
                "Null parent identifier"
                );
            }
        if (null == name)
            {
            throw new IllegalArgumentException(
                "Null node name"
                );
            }
        if (null == type)
            {
            throw new IllegalArgumentException(
                "Null node type"
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
                        parent.ivorn().toString(),
                        name,
                        type
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
            throw new UnsupportedOperationException(
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
    public FileManagerNode getNode(Ivorn ivorn)
        throws FileManagerServiceException, FileManagerIdentifierException, NodeNotFoundException
        {
        return this.node(
            this.getProperties(
                ivorn
                )
            ) ;
        }

    /**
     * Get the properties for a specific node, indexed by ident.
     * @param ivorn The node identifier.
     * @return The array of properties for the node.
     * @throws FileManagerIdentifierException If the node identifier is invalid.
     * @throws NodeNotFoundException If the node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request. 
     *
     */
    public FileProperty[] getProperties(Ivorn ivorn)
        throws FileManagerServiceException, FileManagerIdentifierException, NodeNotFoundException
        {
        if (null == ivorn)
            {
            throw new IllegalArgumentException(
                "Null identifier"
                );
            }
        try {
            return manager.getNode(
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
     * Get a specific child node, indexed by path.
     * @param node The parent node.
     * @param path  The target node path.
     * @return The node specified by the path.
     * @throws FileManagerIdentifierException If the node identifier is invalid.
     * @throws NodeNotFoundException If the node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request. 
     *
     */
    protected FileManagerNode getChild(FileManagerNode node, String path)
        throws FileManagerServiceException, FileManagerIdentifierException, NodeNotFoundException
        {
        if (null == node)
            {
            throw new IllegalArgumentException(
                "Null parent node"
                );
            }
        return getChild(
            node.ivorn(),
            path
            );
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
                "Null parent identifier"
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
     * @return A List of Ivorns for the child nodes.
     * @throws FileManagerIdentifierException If the node identifier is invalid.
     * @throws NodeNotFoundException If the parent node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request. 
     * @todo Change this to return a list of the child ivorn(s) ... or 'child' objects (name, ivorn, type).
     *
     */
    protected List getChildren(Ivorn ivorn)
        throws FileManagerServiceException, FileManagerIdentifierException, NodeNotFoundException
        {
        if (null == ivorn)
            {
            throw new IllegalArgumentException(
                "Null identifier"
                );
            }
        try {
            List ivorns  = new ArrayList();
            List strings = Arrays.asList(
                manager.getChildren(
                    ivorn.toString()
                    )
                );
            Iterator iter = strings.iterator();
            while (iter.hasNext())
                {
                try {
                    ivorns.add(
                        new Ivorn(
                            (String) iter.next()
                            )
                        );
                    }
                catch (URISyntaxException ouch)
                    {
                    throw new FileManagerServiceException(
                        "Unable to parse node ivorn"
                        ) ;
                    }
                }
            return Collections.unmodifiableList(
                ivorns
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

    /**
     * Move a node to a new location.
     * If the node already has stored data, then this may involve transfering the data to a new location.
     * @param request The properties for the move.
     * @return A new set of properties describing the node.
     *
     * @throws DuplicateNodeException If a node with the same name already exists in the metadata tree.
     * @throws NodeNotFoundException If the current node is no longer in the database.
     * @throws NodeNotFoundException If the new parent node is no longer in the database.
     * @throws FileManagerPropertiesException If the transfer properties are incomplete.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    protected FileManagerProperties move(FileManagerProperties request)
        throws DuplicateNodeException, NodeNotFoundException, FileManagerPropertiesException, FileManagerServiceException
        {
        if (null == request)
            {
            throw new IllegalArgumentException(
                "Null request properties"
                );
            }
        try {
            return new FileManagerProperties(
                manager.move(
                    request.toArray()
                    )
                );
            }
        catch (RemoteException ouch)
            {
            //
            // Look for the expected exceptions.
            serviceException(ouch);
            propertiesException(ouch);
            nodeNotFoundException(ouch);
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
     * Update the properties for a node.
     * If this node has stored data, this will trigger a call to the FileStore to refresh the data properties.
     * @param node The node to refresh.
     * @return A new set of properties describing the node.
     *
     * @throws NodeNotFoundException If the node does not exist.
     * @throws FileManagerIdentifierException If the node identifier is invalid.
     * @throws FileManagerServiceException If a problem occurs when handling the request. 
     *
     */
    public FileManagerProperties refresh(FileManagerNode node)
        throws NodeNotFoundException, FileManagerIdentifierException, FileManagerServiceException
        {
        log.debug("");
        log.debug("FileManagerCoreDelegate.refresh()");
        if (null == node)
            {
            throw new IllegalArgumentException(
                "Null node"
                );
            }
        if (null == node.ivorn())
            {
            throw new IllegalArgumentException(
                "Null node"
                );
            }
        log.debug("  Node : " + node.ivorn().toString());
        try {
            return new FileManagerProperties(
                manager.refresh(
                    node.ivorn().toString()
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
     * Create a copy of a node.
     * If the node already has stored data, then this will create a new copy of the data.
     * @param  properties The request properties.
     * @return The new node.
     * @throws DuplicateNodeException If a node with the same name already exists in the metadata tree.
     * @throws NodeNotFoundException If the current node is no longer in the database.
     * @throws NodeNotFoundException If the new parent node is no longer in the database.
     * @throws FileManagerPropertiesException If the transfer properties are incomplete.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    protected FileManagerNode copy(FileManagerProperties request)
        throws DuplicateNodeException, NodeNotFoundException, FileManagerPropertiesException, FileManagerServiceException
        {
        if (null == request)
            {
            throw new IllegalArgumentException(
                "Null request properties"
                );
            }
        try {
            return this.node(
                manager.copy(
                    request.toArray()
                    )
                );
            }
        catch (RemoteException ouch)
            {
            //
            // Look for the expected exceptions.
            serviceException(ouch);
            propertiesException(ouch);
            nodeNotFoundException(ouch);
            duplicateNodeException(ouch);
            //
            // Throw a generic service exception.
            throw new FileManagerServiceException(
                "Error occurred in WebService call",
                ouch
                ) ;
            }
        }
    }

