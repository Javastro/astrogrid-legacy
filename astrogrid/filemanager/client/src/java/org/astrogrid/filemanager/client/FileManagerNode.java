/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/FileManagerNode.java,v $</cvs:source>
 * <cvs:author>$Author: nw $</cvs:author>
 * <cvs:date>$Date: 2005/04/14 12:05:23 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerNode.java,v $
 *   Revision 1.7  2005/04/14 12:05:23  nw
 *   another cache fix - enable to peek inside the cache.
 *   doesn't effect existing code.
 *
 *   Revision 1.6  2005/03/11 13:37:06  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.5.4.4  2005/03/01 15:07:30  nw
 *   close to finished now.
 *
 *   Revision 1.5.4.3  2005/02/27 23:03:12  nw
 *   first cut of talking to filestore
 *
 *   Revision 1.1.2.2  2005/02/18 15:50:15  nw
 *   lots of changes.
 *   introduced new schema-driven soap binding, got soap-based unit tests
 *   working again (still some failures)
 *
 *   Revision 1.1.2.1  2005/02/11 14:27:52  nw
 *   refactored, split out candidate classes.
 *
 *   Revision 1.5.4.1  2005/02/10 16:23:14  nw
 *   formatted code
 *
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
package org.astrogrid.filemanager.client;

import org.astrogrid.filemanager.client.delegate.NodeDelegate;
import org.astrogrid.filemanager.common.DuplicateNodeFault;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.store.Ivorn;

import org.apache.axis.types.URI.MalformedURIException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Observer;

import javax.swing.tree.TreeNode;

/**
 * Represents a file or folder in the file manager.
 * <p>
 * Extends the standard javax.swing.TreeNode - so structures of FileManagerNodes can be trivially viewed in gui components.
 * 
 * @modified nww renamed accessors to conform with bean conventions. (i.e. parent -> getParent, etc)
 * @modified nww renamed to Node - shorter - let the package name take the strain.
 * @moified nww split out NodeIterator into separate class
 * @modified moved metadata getters and setters into separate interface - precendent here with JDBC metadata interfaces.
 * 
 * 
 *  
 */
public interface FileManagerNode extends TreeNode{


    /**
     * Get the human-readable ivorn represenation of this resource
     * <p>
     * Will return a fullpath ivorn, rooted from the user's homespace.
     * for a shorter, machine-readable form, use <tt>getMetadata().getNodeIvorn()</tt>
     * @return The ivorn identifier for this node
     * @throws RemoteException
     * @throws NodeNotFoundFault
     * @throws FileManagerFault
     *  
     */
    public Ivorn getIvorn() throws FileManagerFault, NodeNotFoundFault, RemoteException ;
        

    /**
     * Get the node name.
     * 
     * @return The current name of the node.
     *  
     */
    public String getName();

    /**
     * Get the parent node.
     * 
     * @return The parent node, or null for a root node.
     * @throws RemoteException
     * @throws NodeNotFoundException
     *             If the node is not in the database.
     * @throws FileManagerServiceException
     *             If a problem occurs when handling the request.
     *  
     */
    public FileManagerNode getParentNode() throws NodeNotFoundFault,
            FileManagerFault, RemoteException;

    /**
     * Delete this node.
     * @throws RemoteException
     * 
     * @throws NodeNotFoundException
     *             If the node is not in the database.
     * @throws FileManagerServiceException
     *             If a problem occurs when handling the request.
     *  
     */
    public void delete() throws NodeNotFoundFault,
            FileManagerFault, RemoteException;

    /**
     * Check if this node represents a file.
     * 
     * @return true if this node represents a file.
     *  
     */
    public boolean isFile();

    /**
     * Check if this node represents a folder
     * 
     * @return true if this node represents a folder
     *  
     */
    public boolean isFolder();

    /**
     * Create a copy of this node. If the node already has stored data, then
     * this will create a copy of the data.
     * 
     * @return A reference to the new node.
     * @param name
     *            The name of the new Node.
     * @param node
     *            The new parent Node in the metadata tree (null to create the
     *            new node in the same location in the tree).
     * @param store
     *            The Ivorn of the FileStore for new Node (null to store the
     *            copy at the same location).
     * @throws RemoteException
     * @throws DuplicateNodeException
     *             If a node with the same name already exists in the metadata
     *             tree.
     * @throws NodeNotFoundException
     *             If the current node is no longer in the database.
     * @throws NodeNotFoundException
     *             If the new parent node is no longer in the database.
     * @throws FileManagerServiceException
     *             If a problem occurs when handling the request.
     *  
     */
    public FileManagerNode copy(String name, FileManagerNode parent, Ivorn location)
            throws DuplicateNodeFault, NodeNotFoundFault,
            FileManagerFault, RemoteException;

    /**
     * Move this node to a new location. If the node already has stored data,
     * then this may involve transfering the data to a new location.
     * 
     * @param name
     *            The name of the new Node.
     * @param node
     *            The new parent Node in the metadata tree (null to leave the
     *            node in the same location in the tree).
     * @param store
     *            The Ivorn of the FileStore location (null to leave the data at
     *            the same location).
     * @throws RemoteException
     * @throws DuplicateNodeException
     *             If a node with the same name already exists in the metadata
     *             tree.
     * @throws NodeNotFoundException
     *             If the current node is no longer in the database.
     * @throws NodeNotFoundException
     *             If the new parent node is no longer in the database.
     * @throws FileManagerServiceException
     *             If a problem occurs when handling the request.
     *  
     */
    public void move(String name, FileManagerNode parent, Ivorn location)
            throws DuplicateNodeFault, NodeNotFoundFault,
            FileManagerFault, RemoteException;

    /**
     * Add a new child filder node.
     * 
     * @param name
     *            The node name.
     * @return A new node for the folder.
     * @throws UnsupportedOperationException
     *             If this node represents a file.
     * @throws RemoteException
     * @throws DuplicateNodeException
     *             If a node with the same name already exists.
     * @throws NodeNotFoundException
     *             If the current node is no longer in the database.
     * @throws FileManagerServiceException
     *             If a problem occurs when handling the request.
     */
    public FileManagerNode addFolder(String name)
            throws UnsupportedOperationException, DuplicateNodeFault, NodeNotFoundFault,
            FileManagerFault, RemoteException;

    /**
     * Add a new file node.
     * 
     * @param name
     *            The node name.
     * @return A new node for the file.
     * @throws UnsupportedOperationException
     *             If this node represents a file.
     * @throws RemoteException
     * @throws DuplicateNodeException
     *             If a node with the same name already exists.
     * @throws NodeNotFoundException
     *             If the current node is no longer in the database.
     * @throws FileManagerServiceException
     *             If a problem occurs when handling the request.
     *  
     */
    public FileManagerNode addFile(String name)
            throws UnsupportedOperationException, DuplicateNodeFault, NodeNotFoundFault,
            FileManagerFault, RemoteException;

    /**
     * Get a child node by path.
     * 
     * @param name
     *            The name of the child node. NB nested paths (e.g. <code>'foo/bar/choo'</code>) aren't supported. 
     * Instead do <code>n.getChild('foo').getChild('bar').getChild('choo')</code>
     * @return A reference to the child node.
     * @throws UnsupportedOperationException
     *             If this node represents a file.
     * @throws RemoteException
     * @throws NodeNotFoundException
     *             If the node is not in the database.
     * @throws FileManagerServiceException
     *             If a problem occurs when handling the request.
     *  
     */
    public FileManagerNode getChild(String name)
            throws UnsupportedOperationException,  NodeNotFoundFault,
            FileManagerFault, RemoteException;

    /**
     * Open a java OutputStream to write data into the node.
     * 
     * @return An OutputStream connected directly to the node store.
     * @throws IOException
     *             If a problem occurs openning the stream.
     * @throws UnsupportedOperationException
     *             If the node represents a container.
     * @throws NodeNotFoundException
     *             If the node is not in the database.
     * @throws FileManagerServiceException
     *             If a problem occurs when handling the request.
     *  
     */
    public OutputStream writeContent() throws IOException,
            UnsupportedOperationException, NodeNotFoundFault,
            FileManagerFault;

    /** open a java output stream to append data into the node 
     * 
     * @see #writeContent()
     */
    public OutputStream appendContent() throws IOException,
    UnsupportedOperationException, NodeNotFoundFault,
    FileManagerFault;

    /**
     * Open a java InputStream to read the  data content of a node.
     * 
     * @return An InputStream connected directly to the node store.
     * @throws IOException
     *             If a problem occurs openning the stream.
     * @throws UnsupportedOperationException
     *             If the node represents a container.
     * @throws NodeNotFoundException
     *             If the node is not known, or has no data.
     * @throws FileManagerServiceException
     *             If a problem occurs when handling the request.
     *  
     */
    public InputStream readContent() throws IOException,
            UnsupportedOperationException,NodeNotFoundFault,
            FileManagerFault;

    /**
     * Get a URL to access the node data from.
     * 
     * @return A URL which connects directly to the node store.
     * @throws UnsupportedOperationException
     *             If the node represents a folder
     * @throws RemoteException
     * @throws MalformedURLException
     * @throws NodeNotFoundException
     *             If the node is not in the database.
     * @throws FileManagerServiceException
     *             If a problem occurs when handling the request.
     *  
     */
    public URL contentURL() throws UnsupportedOperationException,
        NodeNotFoundFault, FileManagerFault, MalformedURLException, RemoteException;

    /**
     * copy content of an external resource into the node. (import a URL resource)
     * 
     * @throws UnsupportedOperationException
     *             If the node represents a folder
     * @throws MalformedURIException
     * @throws RemoteException
     * @throws NodeNotFoundException
     *             If the node is not in the database.
     * @throws FileManagerServiceException
     *             If a problem occurs when handling the request.
     *  
     */
    public void copyURLToContent(URL url) throws UnsupportedOperationException,
            NodeNotFoundFault, FileManagerFault, RemoteException, MalformedURIException;

  
    /**
     * export node content to a URL.
     * 
     * @throws UnsupportedOperationException
     *             If the node represents a folder
     * @throws MalformedURIException
     * @throws RemoteException
     * @throws NodeNotFoundException
     *             If the node is not in the database.
     * @throws FileManagerServiceException
     *             If a problem occurs when handling the request.
     *  
     */
    public void copyContentToURL(URL url) throws UnsupportedOperationException,
            NodeNotFoundFault, FileManagerFault, RemoteException, MalformedURIException;
  
    
   /** synchronize this node with the filemanager server.
    * - local node will be updated to reflect any changes on server, and any observers of the node notified.
    * @throws NodeNotFoundFault
    *  if the node is not known.
    * @throws FileManagerFault
    *   somethings gone wrong.
    * @throws RemoteException
    * somethings gone very wrong.
    */
    public void refresh() throws NodeNotFoundFault,
            FileManagerFault, RemoteException;

    /** Notify the filemanager that a write to the content of this node has completed.
     * <p>
     * This will cause the filemanager to update its properties (size, modification date, etc) based on the new content.
     * This in turn causes a {@link #refresh()} to happen, which updates the local node based on changes in the server.
     * 
     * <p>
     * Temporary work-around until filestore calls back to filemanager with this information itself.
     * @throws RemoteException
     * 
     * @throws NodeNotFoundException
     *             If the node no longer exists in the server database.
     * @throws FileManagerServiceException
     *             If a problem occurs when handling the request.
     *  
     */
    public void transferCompleted() throws NodeNotFoundFault, FileManagerFault, RemoteException;

    /**
     * Get an iterator for the child nodes of this node.
     * 
     * @throws NodeNotFoundException
     *             If this node is no longer in the server database.
     * @throws FileManagerServiceException
     *             If a problem occurs when handling the request.
     * @throws UnsupportedOperationException
     *             If the node does not represent a container.
     *  
     */
    public NodeIterator iterator() throws NodeNotFoundFault,
            FileManagerFault;
    
    /** access the metadata for a node 
     * @return metadata object (never null)
     * */
    public NodeMetadata getMetadata();
        
    /** add an observer to this node - the observer will be notified whenever the data of this node changes */
    public void addObserver(Observer o);
    /** remove an observer from this node */
    public void deleteObserver(Observer o);
    
    /** method that peeks inside the implementation, and tells what's in the cache.
     * useful for gui implementation - can tell whether a traversal is going to require a call to the server.
     * @return if this node is a follder, true if (some) of it's children are in the cache.
     *     if this node is a file, will return true;
     */
    public boolean isChildrenInCache();
    
    /** access the node delegate this node is working with- useful sometimes */
    public NodeDelegate getNodeDelegate();
    
  

}

