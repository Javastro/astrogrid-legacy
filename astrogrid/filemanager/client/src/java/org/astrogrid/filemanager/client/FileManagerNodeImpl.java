/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/Attic/FileManagerNodeImpl.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerNodeImpl.java,v $
 *   Revision 1.4  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.3.4.2  2005/01/12 14:20:57  dave
 *   Replaced tabs with spaces ....
 *
 *   Revision 1.3.4.1  2005/01/07 12:18:00  dave
 *   Added StoreClientWrapperTest
 *   Added StoreFileWrapper
 *
 *   Revision 1.3  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.14  2004/12/14 10:39:17  dave
 *   Added copy to node API ....
 *
 *   Revision 1.1.2.13  2004/12/14 10:32:18  dave
 *   Added copy to node API ....
 *
 *   Revision 1.1.2.12  2004/12/11 01:57:30  dave
 *   Added automatic trigger to node update() on OutputStream close() ....
 *
 *   Revision 1.1.2.11  2004/12/10 05:21:25  dave
 *   Added node and iterator to client API ...
 *
 *   Revision 1.1.2.10  2004/12/08 17:54:55  dave
 *   Added update to FileManager client and server side ...
 *
 *   Revision 1.1.2.9  2004/12/08 01:56:04  dave
 *   Added filestore location to move ...
 *
 *   Revision 1.1.2.8  2004/12/04 05:22:21  dave
 *   Fixed null parent mistake ...
 *
 *   Revision 1.1.2.7  2004/12/02 19:11:54  dave
 *   Added move name and parent to manager ...
 *
 *   Revision 1.1.2.6  2004/11/29 18:05:07  dave
 *   Refactored methods names ....
 *   Added stubs for delete, copy and move.
 *
 *   Revision 1.1.2.5  2004/11/25 13:41:14  dave
 *   Added export stream handling to node ...
 *
 *   Revision 1.1.2.4  2004/11/24 19:23:29  dave
 *   Started to add input and output streams to node ...
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

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.util.List ;
import java.util.Iterator ;

import java.io.IOException ;
import java.io.InputStream ;
import java.io.OutputStream ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filestore.common.file.FileProperty;
import org.astrogrid.filestore.common.FileStoreInputStream;
import org.astrogrid.filestore.common.FileStoreOutputStream;
import org.astrogrid.filestore.common.transfer.TransferUtil;
import org.astrogrid.filestore.common.transfer.TransferProperties;
import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;

import org.astrogrid.filemanager.common.FileManagerProperties;
import org.astrogrid.filemanager.common.exception.NodeNotFoundException ;
import org.astrogrid.filemanager.common.exception.DuplicateNodeException;
import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException;
import org.astrogrid.filemanager.common.exception.FileManagerServiceException;
import org.astrogrid.filemanager.common.exception.FileManagerPropertiesException;

/**
 * Client implementation of the FileManagerNode interface.
 *
 */
public class FileManagerNodeImpl
    implements FileManagerNode
    {

    /**
     * Our debug logger.
     *
     */
    protected static Log log = LogFactory.getLog(FileManagerNodeImpl.class);

    /**
     * A reference to the FileManagerDelegate that created this node.
     *
     */
    private FileManagerCoreDelegate delegate ;

    /**
     * Protected constructor from an array of file properties.
     * @param delegate A reference to the FileManagerDelegate that created this node.
     * @param properties An array of properties for the node.
     *
     */
    protected FileManagerNodeImpl(FileManagerCoreDelegate delegate, FileProperty[] properties)
        {
        this(
            delegate,
            new FileManagerProperties(
                properties
                )
            );
        }

    /**
     * Protected constructor from a FileManagerProperties map.
     * @param delegate A reference to the FileManagerDelegate that created this node.
     * @param properties An array of properties for the node.
     *
     */
    protected FileManagerNodeImpl(FileManagerCoreDelegate delegate, FileManagerProperties properties)
        {
        if (null == delegate)
            {
            throw new IllegalArgumentException(
                "Null delegate"
                );
            }
        if (null == properties)
            {
            throw new IllegalArgumentException(
                "Null properties"
                );
            }
        this.delegate = delegate ;
        this.properties = properties ;
        }

    /**
     * Our node properties.
     *
     */
    private FileManagerProperties properties ;

    /**
     * Get the node ivorn.
     * @return The ivorn identifier for this node.
     * @throws FileManagerIdentifierException If the ivorn is invalid.
     *
     */
    public Ivorn ivorn()
        throws FileManagerIdentifierException
        {
        return properties.getManagerResourceIvorn();
        }

    /**
     * Get the node name.
     * @return The name for this node.
     *
     */
    public String name()
        {
        return properties.getManagerResourceName();
        }

    /**
     * Get the content size for a data node.
     * @return The size of the stored data for a data node, or -1 for a container node.
     *
     */
    public long size()
        {
        return properties.getContentSize();
        }

    /**
     * Get the node (FileStore) location of the node data.
     * This returns the (FileStore) location where the data is actually stored.
     * @return The Ivorn for the (FileStore) location where the data is currently stored, or null if the node does not contain any data.
     * @throws UnsupportedOperationException If the node represents a container (although future extensions may allow this).
     * @throws FileManagerIdentifierException If the location Ivorn is not valid.
     *
     */
    public Ivorn location()
        throws UnsupportedOperationException, FileManagerIdentifierException
        {
        return properties.getManagerLocationIvorn();
        }

    /**
     * Get the parent node.
     * @return The parent node, or null for a root node.
     * @throws NodeNotFoundException If the node is not in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public FileManagerNode parent()
        throws NodeNotFoundException, FileManagerServiceException
        {
        try {
            return delegate.getNode(
                properties.getManagerParentIvorn()
                );
            }
        catch(FileManagerIdentifierException ouch)
            {
            throw new FileManagerServiceException(
                ouch.getMessage()
                );
            }
        }

    /**
     * Check if this represents a file.
     * @return true if this represents a file.
     *
     */
    public boolean isFile()
        {
        return FileManagerProperties.DATA_NODE_TYPE.equals(
            properties.getManagerResourceType()
            ) ;
        }

    /**
     * Check if this represents a container.
     * @return true if this represents a container.
     *
     */
    public boolean isContainer()
        {
        return FileManagerProperties.CONTAINER_NODE_TYPE.equals(
            properties.getManagerResourceType()
            ) ;
        }

    /**
     * Delete this node.
     * @throws NodeNotFoundException If the node is not in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public void delete()
        throws NodeNotFoundException, FileManagerServiceException
        {
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
    public FileManagerNode add(String name, String type)
        throws UnsupportedOperationException, NodeNotFoundException, DuplicateNodeException , FileManagerServiceException
        {
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
        if (!this.isContainer())
            {
            throw new UnsupportedOperationException(
                "Node is not a container."
                );
            }
        try {
            return delegate.add(
                this,
                name,
                type
                ) ;
            }
        catch (FileManagerIdentifierException ouch)
            {
            throw new FileManagerServiceException(
                "Invalid parent node identifier"
                );
            }
        }

    /**
     * Get a child node by path.
     * @param path The path to the child node.
     * @return A reference to the child node.
     * @throws UnsupportedOperationException If this node represents a file.
     * @throws NodeNotFoundException If the node is not in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public FileManagerNode child(String path)
        throws UnsupportedOperationException, NodeNotFoundException, FileManagerServiceException
        {
        if (null == path)
            {
            throw new IllegalArgumentException(
                "Null path"
                );
            }
        if (!this.isContainer())
            {
            throw new UnsupportedOperationException(
                "Node is not a container."
                );
            }
        try {
            return delegate.getChild(
                this,
                path
                ) ;
            }
        catch (FileManagerIdentifierException ouch)
            {
            throw new FileManagerServiceException(
                "Invalid parent node identifier"
                );
            }
        }

    /**
     * Open an output stream to the node.
     * @throws IOException If a problem occurs openning the stream.
     * @throws UnsupportedOperationException If the node represents a container.
     * @throws NodeNotFoundException If the node is not in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public OutputStream output()
        throws
            IOException,
            UnsupportedOperationException,
            NodeNotFoundException,
            FileManagerServiceException
        {
        if (!this.isFile())
            {
            throw new UnsupportedOperationException(
                "Node is not a file."
                );
            }
        //
        // Call our delegate to initiate the import.
        try {
            TransferProperties transfer =
                delegate.importInit(
                    this.properties
                    ) ;
            //
            // Check we got a transfer location (URL).
            if (null != transfer.getLocation())
                {
                //
                // Open an output stream to the target.
                FileStoreOutputStream stream = new FileStoreOutputStream(
                    transfer.getLocation()
                    )
                    {
                    public void close()
                        throws IOException
                        {
                        super.close();
                        try {
                            refresh();
                            }
                        catch (Exception ouch)
                            {
                            log.warn("Exception in refresh() following IOStream close()");
                            }
                        }
                    } ;
                stream.open() ;
                return stream ;
                }
            //
            // If we didn't get a transfer location (URL).
            else {
                throw new FileManagerServiceException(
                    "Unable to get transfer location (URL)"
                    );
                }
            }
        catch (FileManagerPropertiesException ouch)
            {
            throw new FileManagerServiceException(
                "Unable to get transfer location (URL)",
                ouch
                );
            }
        }

    /**
     * Open an input stream to the node.
     * @throws IOException If a problem occurs openning the stream.
     * @throws UnsupportedOperationException If the node represents a container.
     * @throws NodeNotFoundException If the node is not in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public InputStream input()
        throws
            IOException,
            UnsupportedOperationException,
            NodeNotFoundException,
            FileManagerServiceException
        {
        if (!this.isFile())
            {
            throw new UnsupportedOperationException(
                "Node is not a file."
                );
            }
        //
        // Call our delegate to initiate the export.
        try {
            TransferProperties transfer =
                delegate.exportInit(
                    this.properties
                    ) ;
            //
            // Check we got a transfer location (URL).
            if (null != transfer.getLocation())
                {
                //
                // Open an output stream to the target.
                FileStoreInputStream stream = new FileStoreInputStream(
                    transfer.getLocation()
                    ) ;
                stream.open() ;
                return stream ;
                }
            //
            // If we didn't get a transfer location (URL).
            else {
                throw new FileManagerServiceException(
                    "Unable to get transfer location (URL)"
                    );
                }
            }
        catch (FileManagerPropertiesException ouch)
            {
            throw new FileManagerServiceException(
                "Unable to get transfer location (URL)",
                ouch
                );
            }
        }


    /**
     * Create a copy of this node.
     * If the node already has stored data, then this will create a copy of the data.
     * @return A reference to the new node.
     * @param name  The name of the new Node.
     * @param node  The new parent Node in the metadata tree (null to create the new node in the same location in the tree).
     * @param store The Ivorn of the FileStore for new Node (null to store the copy at the same location).
     * @throws DuplicateNodeException If a node with the same name already exists in the metadata tree.
     * @throws NodeNotFoundException If the current node is no longer in the database.
     * @throws NodeNotFoundException If the new parent node is no longer in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public FileManagerNode copy(String name, FileManagerNode parent, Ivorn location)
        throws DuplicateNodeException, NodeNotFoundException, FileManagerServiceException
        {
        FileManagerProperties request = new FileManagerProperties();
        try {
            request.setManagerResourceIvorn(
                this.ivorn()
                );
            }
        catch (FileManagerIdentifierException ouch)
            {
            throw new FileManagerServiceException(
                "Unable to parse node ivorn"
                );
            }
        if (null != name)
            {
            request.setManagerResourceName(
                name
                );
            }
        if (null != parent)
            {
            try {
                request.setManagerParentIvorn(
                    parent.ivorn()
                    );
                }
            catch (FileManagerIdentifierException ouch)
                {
                throw new FileManagerServiceException(
                    "Unable to parse parent node ivorn"
                    );
                }
            }
        if (null != location)
            {
            request.setManagerLocationIvorn(
                location
                );
            }
        try {
            return delegate.copy(
                request
                );
            }
        catch (FileManagerPropertiesException ouch)
            {
            throw new FileManagerServiceException(
                "Error in client code, invalid request properties"
                );
            }
        }

    /**
     * Move this node to a new location.
     * If the node already has stored data, then this may involve transfering the data to a new location.
     * @param name  The name of the new Node.
     * @param node  The new parent Node in the metadata tree (null to leave the node in the same location in the tree).
     * @param store The Ivorn of the FileStore location (null to leave the data at the same location).
     * @throws DuplicateNodeException If a node with the same name already exists in the metadata tree.
     * @throws NodeNotFoundException If the current node is no longer in the database.
     * @throws NodeNotFoundException If the new parent node is no longer in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public void move(String name, FileManagerNode parent, Ivorn location)
        throws DuplicateNodeException, NodeNotFoundException, FileManagerServiceException
        {
        FileManagerProperties request = new FileManagerProperties();
        try {
            request.setManagerResourceIvorn(
                this.ivorn()
                );
            }
        catch (FileManagerIdentifierException ouch)
            {
            throw new FileManagerServiceException(
                "Unable to parse node ivorn"
                );
            }
        if (null != name)
            {
            request.setManagerResourceName(
                name
                );
            }
        if (null != parent)
            {
            try {
                request.setManagerParentIvorn(
                    parent.ivorn()
                    );
                }
            catch (FileManagerIdentifierException ouch)
                {
                throw new FileManagerServiceException(
                    "Unable to parse parent node ivorn"
                    );
                }
            }
        if (null != location)
            {
            request.setManagerLocationIvorn(
                location
                );
            }
        try {
            this.properties = new FileManagerProperties(
                delegate.move(
                    request
                    )
                );
            }
        catch (FileManagerPropertiesException ouch)
            {
            throw new FileManagerServiceException(
                "Error in client code, invalid request properties"
                );
            }
        }

    /**
     * Refresh the node properties.
     * If the node has stored data, this will trigger a call to the FileStore to refresh the data properties.
     * Clients should call this method after a data import has completed to update the node properties.
     * @throws NodeNotFoundException If the node no longer exists in the server database.
     * @throws FileManagerServiceException If a problem occurs when handling the request. 
     *
     */
    public void refresh()
        throws NodeNotFoundException, FileManagerServiceException
        {
        log.debug("");
        log.debug("FileManagerNodeImpl.refresh()");
        try {
            this.properties = new FileManagerProperties(
                delegate.refresh(
                    this
                    )
                );
            }
        catch (FileManagerIdentifierException ouch)
            {
            throw new FileManagerServiceException(
                "Error in client code, invalid node identifier"
                );
            }
        }

    /**
     * Inner class to provide an iterator on child nodes.
     *
     */
    protected class NodeIteratorImpl
        implements FileManagerNode.NodeIterator
        {
        /**
         * Our internal iterator of the ivorn list.
         *
         */
        private Iterator iter ;

        /**
         * Create an iterator for a list of nodes.
         *
         */
        public NodeIteratorImpl(List list)
            {
            log.debug("");
            log.debug("NodeIteratorImpl.NodeIteratorImpl()");
            this.iter = list.iterator() ;
            }

        /**
         * Check if the there are more nodes in the iteration.
         *
         */
        public boolean hasNext()
            {
            return this.iter.hasNext();
            }

        /**
         * Get the next node in the iteration.
         * @throws NodeNotFoundException If the node is no longer in the server database (this can happen if another client deletes the node after this iterator was created).
         * @throws FileManagerServiceException If a problem occurs when handling the request.
         *
         */
        public FileManagerNode next()
            throws NodeNotFoundException, FileManagerServiceException
            {
            log.debug("");
            log.debug("NodeIteratorImpl.next()");
            try {
                return delegate.getNode(
                    (Ivorn) this.iter.next()
                    );
                }
            catch (FileManagerIdentifierException ouch)
                {
                throw new FileManagerServiceException(
                    "Unable to parse node ivorn",
                    ouch
                    );
                }
            }
        }

    /**
     * Get an iterator for the child nodes of this node.
     * @throws NodeNotFoundException If this node is no longer in the server database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     * @throws UnsupportedOperationException If the node does not represent a container.
     *
     */
    public FileManagerNode.NodeIterator iterator()
        throws NodeNotFoundException, FileManagerServiceException
        {
        log.debug("");
        log.debug("FileManagerNodeImpl.iterator()");
        try {
            return new NodeIteratorImpl(
                delegate.getChildren(
                    this.ivorn()
                    )
                );
            }
        catch (FileManagerIdentifierException ouch)
            {
            throw new FileManagerServiceException(
                "Error in client code, invalid node identifier"
                );
            }
        }
    }
