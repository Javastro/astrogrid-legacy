/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/FileManagerStore.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/02/10 14:17:20 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerStore.java,v $
 *   Revision 1.3  2005/02/10 14:17:20  jdt
 *   merge from  filemanager-nww-903
 *
 *   Revision 1.2.4.1  2005/02/10 13:11:15  nw
 *   added a commitNode method
 *
 *   Revision 1.2  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.1.2.4  2005/01/12 13:16:27  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.1.2.3  2005/01/12 12:40:08  dave
 *   Added account handling to store ...
 *
 *   Revision 1.1.2.2  2005/01/10 21:27:47  dave
 *   Refactores NodeMock as FileManagerStoreNode ...
 *
 *   Revision 1.1.2.1  2005/01/10 15:36:28  dave
 *   Refactored store into a separate interface and mock impl ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filemanager.common.exception.NodeNotFoundException ;
import org.astrogrid.filemanager.common.exception.DuplicateNodeException ;
import org.astrogrid.filemanager.common.exception.FileManagerServiceException ;

/**
 * Public interface for a FileManager data store.
 *@modified nww added a {@link #commitNode} method
 */
public interface FileManagerStore
    {
    /**
     * Check if the store contains an account.
     * @param indent The account identifier.
     * @return true if the store contains the account.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public boolean hasAccount(String ident)
        throws FileManagerServiceException;

    /**
     * Add a new account to our store.
     * @param ident The account identifier.
     * @param node  The root node for the account.
     * @return A new root node for the account.
     * @throws DuplicateNodeException If the the node already exists.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public void addAccount(String ident, FileManagerStoreNode root)
        throws DuplicateNodeException, FileManagerServiceException ;

    /**
     * Get the root node for an account.
     * @param ident  The account identifier.
     * @return The root node for the account.
     * @throws NodeNotFoundException If the account does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public FileManagerStoreNode getAccount(String ident)
        throws NodeNotFoundException, FileManagerServiceException ;
        
    /**
     * Remove an account from our store.
     * @param ident  The account identifier.
     * @throws NodeNotFoundException If the node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public void delAccount(String ident)
        throws NodeNotFoundException, FileManagerServiceException ;

    /**
     * Check if the store contains a node.
     * @param indent The node identifier.
     * @return true if the store contains the identifier.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public boolean hasNode(String ident)
        throws FileManagerServiceException;

    /**
     * Add a new node to our store.
     * @param parent The parent node identifier.
     * @param ivorn  The node identifier.
     * @param name   The node name.
     * @param type   The node type.
     * @throws DuplicateNodeException If the the node already exists.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public FileManagerStoreNode addNode(Ivorn parent, Ivorn ivorn, String name, String type)
        throws DuplicateNodeException, FileManagerServiceException ;

    /**
     * Get a node, based on the identifier.
     * @param  ident The node identifier.
     * @return The matching node.
     * @throws NodeNotFoundException If the node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public FileManagerStoreNode getNode(String ident)
        throws NodeNotFoundException, FileManagerServiceException ;

    /** 
     * commit changes to an existing node back into the store
     * @param node node to commit changes on
     * @throws NodeNotFoundException if node is not in store already
     * @throws FileManagerServiceException if a problem occurs when handling the request.
     */
    public void commitNode(FileManagerStoreNode node) throws NodeNotFoundException, FileManagerServiceException;
    
    /**
     * Remove an existing node from our store.
     * @param node The node to store.
     * @throws NodeNotFoundException If the node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public void delNode(FileManagerStoreNode node)
        throws NodeNotFoundException, FileManagerServiceException ;

    }
