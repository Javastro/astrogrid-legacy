/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/FileManagerStoreMock.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerStoreMock.java,v $
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

import java.util.Map ;
import java.util.HashMap ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filemanager.common.exception.NodeNotFoundException ;
import org.astrogrid.filemanager.common.exception.DuplicateNodeException ;
import org.astrogrid.filemanager.common.exception.FileManagerServiceException ;

/*
 * A HashTable implementation of the FileManagerStore interface.
 *
 */
public class FileManagerStoreMock
    implements FileManagerStore
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileManagerStoreMock.class);

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
    private Map nodes = new HashMap() ;

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
        log.debug("");
        log.debug("FileManagerStoreMock.addNode(String, String, String, String)");
        log.debug("  Parent : " + ((null != parent) ? parent.toString() : null));
        log.debug("  Ivorn  : " + ((null != ivorn)  ? ivorn.toString()  : null));
        log.debug("  Name   : " + name);
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
        log.debug("");
        log.debug("FileManagerStoreMock.getNode(String)");
        log.debug("  Ident  : " + ident);
        if (null == ident)
            {
            throw new IllegalArgumentException(
                "Null node identifier"
                );
            }
        if (nodes.containsKey(ident))
            {
            return (FileManagerStoreNode) nodes.get(
                ident
                );
            }
        else {
            throw new NodeNotFoundException(
                "Node not in store"
                );
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
        }

    /**
     * Our internal map of accounts.
     *
     */
    private Map accounts = new HashMap() ;

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
        if (null == ident)
            {
            throw new IllegalArgumentException(
                "Null account identifier"
                );
            }
        if (accounts.containsKey(ident))
            {
            return (FileManagerStoreNode) accounts.get(
                ident
                );
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
        }

    }
