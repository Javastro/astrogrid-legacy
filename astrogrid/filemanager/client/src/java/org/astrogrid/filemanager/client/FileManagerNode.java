/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/FileManagerNode.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:43:57 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerNode.java,v $
 *   Revision 1.5  2005/01/28 10:43:57  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.4.2.6  2005/01/26 12:24:19  dave
 *   Removed type from add(), replaced with addNode() and addFile() ...
 *
 *   Revision 1.4.2.5  2005/01/21 14:41:57  dave
 *   Added importData(URL url) to the API (needs implementation and tests).
 *
 *   Revision 1.4.2.4  2005/01/21 13:07:37  dave
 *   Added exportURL to the node API ...
 *
 *   Revision 1.4.2.3  2005/01/21 12:18:54  dave
 *   Changed input() and output() to exportStream() and importStream() ...
 *
 *   Revision 1.4.2.2  2005/01/19 11:39:26  dave
 *   Added combined create and modify date ...
 *
 *   Revision 1.4.2.1  2005/01/15 08:25:20  dave
 *   Added file create and modify dates to manager and client API ...
 *
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
 *   Revision 1.1.2.10  2004/12/14 10:32:17  dave
 *   Added copy to node API ....
 *
 *   Revision 1.1.2.9  2004/12/10 05:21:25  dave
 *   Added node and iterator to client API ...
 *
 *   Revision 1.1.2.8  2004/12/08 17:54:55  dave
 *   Added update to FileManager client and server side ...
 *
 *   Revision 1.1.2.7  2004/12/08 01:56:04  dave
 *   Added filestore location to move ...
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

import java.net.URL ;

import java.util.Date ;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.astrogrid.store.Ivorn;

import org.astrogrid.filemanager.common.FileManagerProperties;
import org.astrogrid.filemanager.common.exception.NodeNotFoundException;
import org.astrogrid.filemanager.common.exception.DuplicateNodeException;
import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException;
import org.astrogrid.filemanager.common.exception.FileManagerServiceException;
import org.astrogrid.filemanager.common.exception.FileManagerPropertiesException;

/**
 * The public API for a FileManager node.
 * @todo Mime type, create date etc ....
 *
 *
 *
 */
public interface FileManagerNode
    {

    /**
     * The node type for a file.
     *
    public static final String FILE_NODE = FileManagerProperties.DATA_NODE_TYPE ;
     */

    /**
     * The node type for a container.
     *
    public static final String CONTAINER_NODE = FileManagerProperties.CONTAINER_NODE_TYPE ;
     */

    /**
     * Get the node ivorn.
     * @return The ivorn identifier for this node.
     * @throws FileManagerIdentifierException If the ivorn is invalid.
     *
     */
    public Ivorn ivorn()
        throws FileManagerIdentifierException;

    /**
     * Get the node name.
     * @return The current name of the node.
     *
     */
    public String name();

    /**
     * Get the parent node.
     * @return The parent node, or null for a root node.
     * @throws NodeNotFoundException If the node is not in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public FileManagerNode parent()
        throws NodeNotFoundException, FileManagerServiceException;

    /**
     * Delete this node.
     * @throws NodeNotFoundException If the node is not in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public void delete()
        throws NodeNotFoundException, FileManagerServiceException;

    /**
     * Check if this node represents a file.
     * @return true if this node represents a file.
     *
     */
    public boolean isFile() ;

    /**
     * Check if this node represents a container.
     * @return true if this node represents a container.
     *
     */
    public boolean isContainer() ;

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
        throws DuplicateNodeException, NodeNotFoundException, FileManagerServiceException;

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
        throws DuplicateNodeException, NodeNotFoundException, FileManagerServiceException;

    /**
     * Add a new child node.
     * @param name The node name.
     * @return A new node for the container.
     * @throws UnsupportedOperationException If this node represents a file.
     * @throws DuplicateNodeException If a node with the same name already exists.
     * @throws NodeNotFoundException If the current node is no longer in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public FileManagerNode addNode(String name)
        throws UnsupportedOperationException, NodeNotFoundException, DuplicateNodeException , FileManagerServiceException;

    /**
     * Add a new file node.
     * @param name The node name.
     * @return A new node for the file.
     * @throws UnsupportedOperationException If this node represents a file.
     * @throws DuplicateNodeException If a node with the same name already exists.
     * @throws NodeNotFoundException If the current node is no longer in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public FileManagerNode addFile(String name)
        throws UnsupportedOperationException, NodeNotFoundException, DuplicateNodeException , FileManagerServiceException;

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
        throws UnsupportedOperationException, NodeNotFoundException, FileManagerServiceException;

    /**
     * Open a java OutputStream to send (import) data into the node.
     * @return An OutputStream connected directly to the node store.
     * @throws IOException If a problem occurs openning the stream.
     * @throws UnsupportedOperationException If the node represents a container.
     * @throws NodeNotFoundException If the node is not in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public OutputStream importStream()
        throws IOException, UnsupportedOperationException, NodeNotFoundException, FileManagerServiceException;

    /**
     * Open a java InputStream to read (export) data from the node.
     * @return An InputStream connected directly to the node store.
     * @throws IOException If a problem occurs openning the stream.
     * @throws UnsupportedOperationException If the node represents a container.
     * @throws NodeNotFoundException If the node is not in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public InputStream exportStream()
        throws IOException, UnsupportedOperationException, NodeNotFoundException, FileManagerServiceException;

    /**
     * Get a URL to access the node data from.
     * @return A URL which connects directly to the node store.
     * @throws UnsupportedOperationException If the node represents a container.
     * @throws NodeNotFoundException If the node is not in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public URL exportURL()
        throws UnsupportedOperationException, NodeNotFoundException, FileManagerServiceException;

    /**
     * Import data from a URL.
     * @throws UnsupportedOperationException If the node represents a container.
     * @throws NodeNotFoundException If the node is not in the database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public void importData(URL url)
        throws UnsupportedOperationException, NodeNotFoundException, FileManagerServiceException;

    /**
     * Get the ivorn identifier of the FileStore for the node data.
     * @return The Ivorn for the FileStore where the data is stored.
     * @throws UnsupportedOperationException If the node represents a container (although future extensions may allow this).
     * @throws FileManagerIdentifierException If the location Ivorn is not valid.
     *
     */
    public Ivorn location()
        throws UnsupportedOperationException, FileManagerIdentifierException;

    /**
     * Refresh the node properties after a file transfer has completed.
     * If the node has stored data, this will trigger a call to the FileStore to refresh the data properties.
     * @throws NodeNotFoundException If the node no longer exists in the server database.
     * @throws FileManagerServiceException If a problem occurs when handling the request. 
     *
     */
    public void refresh()
        throws NodeNotFoundException, FileManagerServiceException;

    /**
     * Get the content size for a data node.
     * @return The size of the stored data for a data node, or -1 for a container node.
     *
     */
    public long size() ;

    /**
     * Get the data file create date.
     * This is the create date of the imported data in the filestore.
     * To get the general create date, use <code>getCreateDate()</code>.
     * @return The file create date, if the node has stored data, or null if the node does not contains any data.
     *
     */
    public Date getFileCreateDate();

    /**
     * Get the data file modified date.
     * This is the modified date of the imported data in the filestore.
     * To get the general modified date, use <code>getModifyDate()</code>.
     * @return The file modified date, if the node has stored data, or null if the node does not contains any data.
     *
     */
    public Date getFileModifyDate();

    /**
     * Get the node create date.
     * This is the create date of the metadata node in the filemanager.
     * To get the general create date, use <code>getCreateDate()</code>.
     * @return The node create date.
     *
     */
    public Date getNodeCreateDate();

    /**
     * Get the node modified date.
     * This is the modified date of the metadata node in the filemanager.
     * To get the general modified date, use <code>getModifyDate()</code>.
     * @return The node modified date.
     *
     */
    public Date getNodeModifyDate();

    /**
     * Get the create date.
     * @return The create date.
     *
     */
    public Date getCreateDate();

    /**
     * Get the modified date.
     * @return The modified date.
     *
     */
    public Date getModifyDate();

    /**
     * An iterator for child nodes.
     *
     */
    public interface NodeIterator
        {
        /**
         * Check if the there are more nodes in the iteration.
         *
         */
        public boolean hasNext() ;

        /**
         * Get the next node in the iteration.
         * @throws NodeNotFoundException If the node is no longer in the server database (this can happen if another client deletes the node after this iterator was created).
         * @throws FileManagerServiceException If a problem occurs when handling the request.
         *
         */
        public FileManagerNode next()
            throws NodeNotFoundException, FileManagerServiceException ;

        }

    /**
     * Get an iterator for the child nodes of this node.
     * @throws NodeNotFoundException If this node is no longer in the server database.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     * @throws UnsupportedOperationException If the node does not represent a container.
     *
     */
    public NodeIterator iterator()
        throws NodeNotFoundException, FileManagerServiceException ;

    }





