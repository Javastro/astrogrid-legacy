/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/FileManagerStoreMock.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/02/10 14:17:21 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerStoreMock.java,v $
 *   Revision 1.3  2005/02/10 14:17:21  jdt
 *   merge from  filemanager-nww-903
 *
 *   Revision 1.2.4.1  2005/02/10 13:12:03  nw
 *   fixed logging.
 *   made this a cloning store - to prevent aliasing behaviour
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

import org.astrogrid.filemanager.common.exception.DuplicateNodeException;
import org.astrogrid.filemanager.common.exception.FileManagerServiceException;
import org.astrogrid.filemanager.common.exception.NodeNotFoundException;
import org.astrogrid.store.Ivorn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * A HashTable implementation of the FileManagerStore interface.
 * @modified nww - previous implementation wasn't a true model of a realistic store, as it 
 * allowed aliasing - i.e. after adding a node to the store, further manipulations through a held
 * reference were reflected in the store. This can't be implemented efficiency and simply in a
 * persistent store. <p>
 * Instead will clone copies of nodes as they are accessed the store, to remove
 * possibliity of aliasing. <p>
 * Will then add explicit <tt>commit()</tt> operation, and adjust unit tests until they succeed again. 
 *
 */
public class FileManagerStoreMock
    implements FileManagerStore
    {
    /**
     * Our debug logger.
     *
     */
    private static Log logger = LogFactory.getLog(FileManagerStoreMock.class);

    /**
     * Public constructor.
     *
     */
    public FileManagerStoreMock()
        {
        }

    /**
     * Our internal hash table.
     *
     */
    private CloningMap nodes = new CloningMap();

    /**
     * Check if the store contains a node.
     * @param indent The node identifier.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public boolean hasNode(String ident)
        throws FileManagerServiceException
        {
        if (null == ident)
            {
            throw new IllegalArgumentException(
                "Null node identifier"
                );
            }
        return nodes.containsKey(ident) ;
        }

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
        throws DuplicateNodeException, FileManagerServiceException
        {
        if (logger.isDebugEnabled()) {
            logger.debug("addNode(parent = " + parent + ", ivorn = " + ivorn
                    + ", name = " + name + ", type = " + type + ") - start");
        }        

        if (null == ivorn)
            {
            throw new IllegalArgumentException(
                "Null node identifier"
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
        // Create the new node.
        FileManagerStoreNode node = new FileManagerStoreNodeMock(
            parent,
            ivorn,
            name,
            type
            );
        //
        // Add it to our map.
        nodes.put(
            ivorn.getFragment(),
            node
            );
        //
        // Return the new node.

        if (logger.isDebugEnabled()) {
            logger.debug("addNode() - end");
        }
        return node ;
        }

    /**
     * Get a node, based on the identifier.
     * @throws NodeNotFoundException If the node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public FileManagerStoreNode getNode(String ident)
        throws NodeNotFoundException, FileManagerServiceException
        {
        if (logger.isDebugEnabled()) {
            logger.debug("getNode(ident = " + ident + ") - start");
        }

        if (null == ident)
            {
            throw new IllegalArgumentException(
                "Null node identifier"
                );
            }
        if (nodes.containsKey(ident))
            {
            FileManagerStoreNode returnFileManagerStoreNode = (FileManagerStoreNode) nodes
                    .get(ident);
            if (logger.isDebugEnabled()) {
                logger.debug("getNode() - end");
            }
            return returnFileManagerStoreNode;
            }
        else {
            throw new NodeNotFoundException(
                "Node not in store"
                );
            }
        }
    /**
     * @see org.astrogrid.filemanager.common.FileManagerStore#commitNode(org.astrogrid.filemanager.common.FileManagerStoreNode)
     */
    public void commitNode(FileManagerStoreNode node) throws NodeNotFoundException, FileManagerServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("commitNode(node = " + node + ") - start");
        }

        if (node == null) { 
            throw new IllegalArgumentException("Null node");
        }
        String ident = node.getIdent();
        if (ident == null) {
            throw new IllegalArgumentException("Null node identifier");
        }
        if (!nodes.containsKey(ident)) {
            throw new NodeNotFoundException("Node not in store");
        }
        nodes.put(ident,node);                    

        if (logger.isDebugEnabled()) {
            logger.debug("commitNode() - end");
        }
    }
    /**
     * Remove an existing node from our store.
     * @param node The node to store.
     * @throws NodeNotFoundException If the node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public void delNode(FileManagerStoreNode node)
        throws NodeNotFoundException, FileManagerServiceException
        {
        if (logger.isDebugEnabled()) {
            logger.debug("delNode(node = " + node + ") - start");
        }

        if (null == node)
            {
            throw new IllegalArgumentException(
                "Null node"
                );
            }
        String ident = node.getIdent();
        if (null == ident)
            {
            throw new IllegalArgumentException(
                "Null node identifier"
                );
            }
        if (nodes.containsKey(ident))
            {
            nodes.remove(
                ident
                );
            }
        else {
            throw new NodeNotFoundException(
                "Node not in store"
                );
            }

        if (logger.isDebugEnabled()) {
            logger.debug("delNode() - end");
        }
        }

    /**
     * Our internal map of accounts.
     *
     */
    private CloningMap accounts = new CloningMap() ;

    /**
     * Check if the store contains an account.
     * @param indent The account identifier.
     * @return true if the store contains the account.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public boolean hasAccount(String ident)
        throws FileManagerServiceException
        {
        if (null == ident)
            {
            throw new IllegalArgumentException(
                "Null account identifier"
                );
            }
        return accounts.containsKey(
            ident
            );
        }

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
        throws DuplicateNodeException, FileManagerServiceException
        {
        if (logger.isDebugEnabled()) {
            logger.debug("addAccount(ident = " + ident + ", root = " + root
                    + ") - start");
        }

        if (null == ident)
            {
            throw new IllegalArgumentException(
                "Null account identifier"
                );
            }
        if (null == root)
            {
            throw new IllegalArgumentException(
                "Null account node"
                );
            }
        if (accounts.containsKey(ident))
            {
            throw new DuplicateNodeException(
                "Account already exists"
                );
            }
        else {
            accounts.put(
                ident,
                root
                );
            }

        if (logger.isDebugEnabled()) {
            logger.debug("addAccount() - end");
        }
        }

    /**
     * Get the root node for an account.
     * @param ident  The account identifier.
     * @return The root node for the account.
     * @throws NodeNotFoundException If the account does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public FileManagerStoreNode getAccount(String ident)
        throws NodeNotFoundException, FileManagerServiceException
        {
        if (logger.isDebugEnabled()) {
            logger.debug("getAccount(ident = " + ident + ") - start");
        }

        if (null == ident)
            {
            throw new IllegalArgumentException(
                "Null account identifier"
                );
            }
        if (accounts.containsKey(ident))
            {
            FileManagerStoreNode returnFileManagerStoreNode = (FileManagerStoreNode) accounts
                    .get(ident);
            if (logger.isDebugEnabled()) {
                logger.debug("getAccount() - end");
            }
            return returnFileManagerStoreNode;
            }
        else {
            throw new NodeNotFoundException(
                "Account not found"
                );
            }
        }

    /**
     * Remove an account from our store.
     * @param ident  The account identifier.
     * @throws NodeNotFoundException If the node does not exist.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public void delAccount(String ident)
        throws NodeNotFoundException, FileManagerServiceException
        {
        if (logger.isDebugEnabled()) {
            logger.debug("delAccount(ident = " + ident + ") - start");
        }

        if (null == ident)
            {
            throw new IllegalArgumentException(
                "Null account identifier"
                );
            }
        if (accounts.containsKey(ident))
            {
            accounts.remove(
                ident
                );
            }
        else {
            throw new NodeNotFoundException(
                "Account not found"
                );
            }

        if (logger.isDebugEnabled()) {
            logger.debug("delAccount() - end");
        }
        }

    /** subset of standard map that uses <tt>clone()</tt> to prevent aliasing problems
     * not a full impl of map - only overrides methods necessary - i.e the ones used in this class. */
    protected static class CloningMap {
        /**
         * Commons Logger for this class
         */
        private static final Log logger = LogFactory.getLog(CloningMap.class);

        protected final Map map = new HashMap();
       
        public void put(Object key, FileManagerStoreNode value) {
            if (! (value instanceof FileManagerStoreNodeMock)) {
                throw new IllegalArgumentException("value must be of class FileManagerStoreNodeMock");
            }
            try {
                map.put(key, ((FileManagerStoreNodeMock)value).clone());
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
            public FileManagerStoreNode get(Object key) {
                Object o = map.get(key);
                try {
                    return (FileManagerStoreNode) ((FileManagerStoreNodeMock)o).clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
        }
        public boolean containsKey(Object key) {
            return this.map.containsKey(key);
        }
        public void remove(Object key) {
            this.map.remove(key);
        }
}


    
    }
